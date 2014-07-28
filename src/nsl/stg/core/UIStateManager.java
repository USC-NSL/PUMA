package nsl.stg.core;

import java.util.ArrayList;
import java.util.List;

import nsl.stg.tests.Util;

public class UIStateManager {
	private final double SIMILARITY_THRESHOLD = 0.95;
	// private final double SIMILARITY_THRESHOLD = 0.9999;
	private List<UIState> slist;
	// reference to self
	private static UIStateManager self;

	private UIStateManager() {
		/* hide constructor */
		slist = new ArrayList<UIState>();
	}

	public static UIStateManager getInstance() {
		if (self == null) {
			self = new UIStateManager();
		}
		return self;
	}

	public void addState(UIState s) {
		if (getState(s) == null) {
			Util.log("New UIState: " + s.dumpShort());
			slist.add(s);
		}
	}

	public UIState getState(UIState s) {
		for (int i = 0; i < slist.size(); i++) {
			UIState state = slist.get(i);

			// return on the first found
			if (state.computeCosineSimilarity(s) >= SIMILARITY_THRESHOLD) {
				return state;
			}
		}

		return null;
	}

	public UIState getNextTodoState() {
		for (int i = 0; i < slist.size(); i++) {
			UIState s = slist.get(i);
			if (!s.isFullyExplored()) {
				return s;
			}
		}

		return null;
	}

	public List<UIState> getAllTodoState() {
		List<UIState> ret = new ArrayList<UIState>();

		for (int i = 0; i < slist.size(); i++) {
			UIState s = slist.get(i);
			if (!s.isFullyExplored()) {
				ret.add(s);
			}
		}

		return ret;
	}

	public int getExecutionSnapshot() {
		int total = 0, finished = 0;
		for (int i = 0; i < slist.size(); i++) {
			UIState s = slist.get(i);
			total += s.getTotClickables();

			int next = s.getNextClickId();
			if (next >= 0) {
				finished += next;
			}
		}

		return total + finished;
	}

	public String dumpShort() {
		return "{" + slist.size() + " UIStates }";
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		for (int i = 0; i < slist.size(); i++) {
			UIState s = slist.get(i);
			sb.append("  " + i + " " + s.dumpShort());
		}

		sb.append("}");

		return sb.toString();
	}
}
