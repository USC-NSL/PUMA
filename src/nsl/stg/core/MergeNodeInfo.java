package nsl.stg.core;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class MergeNodeInfo {
	public AccessibilityNodeInfo source;
	public int fromIndex, toIndex;
	public int scrollX, maxScrollX;
	public int scrollY, maxScrollY;

	public MergeNodeInfo(AccessibilityEvent event) {
		this.source = event.getSource();

		this.fromIndex = event.getFromIndex();
		this.toIndex = event.getToIndex();

		this.scrollX = event.getScrollX();
		this.scrollY = event.getScrollY();

		this.maxScrollX = event.getMaxScrollX();
		this.maxScrollY = event.getMaxScrollY();
	}

	public boolean involveIndex() {
		return (fromIndex > -1 && toIndex > -1);
	}

	public boolean involveCoordinate() {
		return (scrollX > -1 && scrollY > -1);
	}

	public String toString() {
		String s = "hint: " + source.getClassName() + "," + source.getChildCount() + " children";
		if (involveIndex()) {
			s += ",I[" + fromIndex + "," + toIndex + "]";
		} else if (involveCoordinate()) {
			s += ",X[" + scrollX + "/" + maxScrollX + "]";
			s += ",Y[" + scrollY + "/" + maxScrollY + "]";
		}
		return s;
	}
}
