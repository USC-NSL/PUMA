package nsl.stg.core;

import java.util.ArrayList;

import nsl.stg.tests.Util;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ListView;

public class AccessibilityEventProcessor {
	private static final ArrayList<AccessibilityEvent> mEventQueue = new ArrayList<AccessibilityEvent>();
	private static final Object mLock = new Object();
	private static AccessibilityEvent currEvent;
	// private static long mLastEventTimeMillis;
	private static boolean mWaitingForEventDelivery = false;

	private static void dump(AccessibilityEvent event) {
		AccessibilityNodeInfo source = event.getSource();
		if (source == null) {
			Util.err("event source: NULL");
			return;
		}

		Rect bounds = new Rect();
		source.getBoundsInScreen(bounds);
		int cnt = -1;
		if (source.getClassName().equals(ListView.class.getCanonicalName())) {
			cnt = event.getItemCount();
			int from = event.getFromIndex();
			int to = event.getToIndex();
			Util.log(event.getEventTime() + ": " + AccessibilityEvent.eventTypeToString(event.getEventType()) + "," + source.getClassName() + "," + cnt + ", [" + from + " --> " + to + "], "
					+ bounds.toShortString());
		} else {
			Util.log(event.getEventTime() + ": " + AccessibilityEvent.eventTypeToString(event.getEventType()) + "," + source.getClassName() + "," + bounds.toShortString());
		}
	}

	public static void process(AccessibilityEvent event) {
		// dump(event);
		// Util.log(event.getEventTime() + ": " + AccessibilityEvent.eventTypeToString(event.getEventType()));

		synchronized (mLock) {
			// mLastEventTimeMillis = event.getEventTime();
			if (mWaitingForEventDelivery) {
				mEventQueue.add(AccessibilityEvent.obtain(event));
			}
			mLock.notifyAll();
		}
	}

	public static AccessibilityEvent waitForLastEvent(final int eventType, final long timeoutMillis) {
		synchronized (mLock) {
			mEventQueue.clear();
			mWaitingForEventDelivery = true;
		}

		AccessibilityEvent ret = null;

		int eventCnt = 0;
		long avgDelayMillis = 0, currDelayMillis = 0, EWMAMillis = 0;
		double ALPHA = 0.5;
		long lastEventTimeMillis = -1L, currEventTimeMillis;
		long mTimeoutMillis = timeoutMillis;
		long startTimeMillis = SystemClock.uptimeMillis();
		long restartTimeMillis = startTimeMillis;

		synchronized (mLock) {
			try {
				while (true) {
					// Drain the event queue
					while (!mEventQueue.isEmpty()) {
						AccessibilityEvent event = mEventQueue.remove(0);
						// Ignore events fromIndex previous interactions.
						if (event.getEventTime() < startTimeMillis) {
							continue;
						}

						if ((event.getEventType() & eventType) != 0) {
							currEventTimeMillis = event.getEventTime();
							if (lastEventTimeMillis > 0) {
								currDelayMillis = currEventTimeMillis - lastEventTimeMillis;
								avgDelayMillis = (eventCnt * avgDelayMillis + currDelayMillis) / (eventCnt + 1);
								EWMAMillis = (long) (currDelayMillis * ALPHA + avgDelayMillis * (1 - ALPHA));
								mTimeoutMillis = Math.min(EWMAMillis, timeoutMillis);
							}
							restartTimeMillis = currEventTimeMillis;
							lastEventTimeMillis = currEventTimeMillis;

							eventCnt++;
							ret = event;
							// Util.log("Got event: " + event);
							// Util.log("EWMAMillis=" + EWMAMillis + ", mTimeoutMillis=" + mTimeoutMillis);
						}
						// event.recycle();
					}

					long elapsedTimeMillis = SystemClock.uptimeMillis() - restartTimeMillis;
					long remainingTimeMillis = mTimeoutMillis - elapsedTimeMillis;
					// Util.log("remainingTimeMillis=" + remainingTimeMillis);

					if (remainingTimeMillis <= 0) {
						// throw new RuntimeException("No event");
						Util.log("TIMEOUT");
						break;
					}
					try {
						mLock.wait(remainingTimeMillis);
					} catch (InterruptedException ie) {
					}
				}

			} finally {
				mWaitingForEventDelivery = false;
				mEventQueue.clear();
				mLock.notifyAll();
			}
		}
		return ret;
	}

	// None-blocking version of waitForLastEvent without EWMA
	public static void waitForLastEventNonBlocking(final int eventType, final long timeoutMillis, final EventDoneListener callback) {
		synchronized (mLock) {
			mEventQueue.clear();
			mWaitingForEventDelivery = true;
		}

		Thread t = new Thread() {
			public void run() {
				AccessibilityEvent ret = null;
				long startTimeMillis = SystemClock.uptimeMillis();

				synchronized (mLock) {
					try {
						while (true) {
							// Drain the event queue
							while (!mEventQueue.isEmpty()) {
								AccessibilityEvent event = mEventQueue.remove(0);

								if ((event.getEventType() & eventType) != 0) {
									ret = event;
									startTimeMillis = SystemClock.uptimeMillis();
								}
							}

							long elapsedTimeMillis = SystemClock.uptimeMillis() - startTimeMillis;
							long remainingTimeMillis = timeoutMillis - elapsedTimeMillis;

							if (remainingTimeMillis <= 0) {
								Util.log("TIMEOUT");
								break;
							}

							try {
								mLock.wait(remainingTimeMillis);
							} catch (InterruptedException ie) {
							}
						}

					} finally {
						mWaitingForEventDelivery = false;
						mEventQueue.clear();
						mLock.notifyAll();
					}
				}

				// finally invoke the callback
				callback.onEventDone(ret);
			}
		};

		t.start();
	}
}
