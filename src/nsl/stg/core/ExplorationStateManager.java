package nsl.stg.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import nsl.stg.tests.Util;

public class ExplorationStateManager {
	private static ExplorationStateManager self;
	private static List<ExplorationState> states; // keep some implicit ordering

	private ExplorationStateManager() {
		/* hide constructor */
		states = new ArrayList<ExplorationState>();
	}

	public static ExplorationStateManager getInstance() {
		if (self == null) {
			self = new ExplorationStateManager();
		}

		return self;
	}

	// Not very efficient: Linear traversal
	private ExplorationState find_state(ExplorationState state) {
		ExplorationState ret = null;
		for (Iterator<ExplorationState> iter = states.iterator(); iter.hasNext();) {
			ExplorationState tmpState = iter.next();
			if (tmpState.equals(state)) {
				ret = tmpState;
				break;
			}
		}

		return ret;
	}

	public void addState(ExplorationState fromState, int clickId, ExplorationState toState) {
		ExplorationState myFromState = fromState;
		ExplorationState myToState = toState;

		if (states.contains(fromState)) {
			myFromState = find_state(fromState);
		} else {
			states.add(fromState);
		}

		if (states.contains(toState)) {
			myToState = find_state(toState);
		} else {
			states.add(toState);
		}

		// Util.log("Adding " + fromState.dumpShort() + " @" + clickId + " --> " + toState.dumpShort());
		// Util.log("Before:\n" + myState.toString());
		myFromState.addReachableState(clickId, myToState);
		// Util.log("After:\n" + myState.toString());
	}

	public boolean containsState(ExplorationState targetState) {
		return (find_state(targetState) != null);
	}

	public List<UIAction> copmuteShortestPath(ExplorationState fromState, ExplorationState toState) {
		List<UIAction> ret = new ArrayList<UIAction>();

		if (fromState.equals(toState)) {
			return ret;
		}

		Stack<UIAction> S = new Stack<UIAction>();
		if (states.contains(fromState)) {
			ExplorationState actualFromState = find_state(fromState);
			// BFS
			Queue<ExplorationState> Q = new LinkedList<ExplorationState>();
			Set<ExplorationState> visited = new HashSet<ExplorationState>();
			Map<ExplorationState, UIAction> map = new Hashtable<ExplorationState, UIAction>();

			boolean done = false;
			Q.add(actualFromState);

			while (!Q.isEmpty() && !done) {
				ExplorationState state = Q.remove();

				if (states.contains(state)) {
					state = find_state(state);
				}

				// Util.log("Procesing " + state.dumpShort());

				if (state.equals(toState)) {
					// backtrack to compute path
					ExplorationState parent = state;
					UIAction action;

					while (!parent.equals(fromState)) {
						action = map.get(parent);
						// Util.log("Adding " + action);
						S.push(action);
						parent = action.getState();
					}

					done = true;
				} else {
					visited.add(state);
					Map<Integer, ExplorationState> children = state.getReachables();
					// Util.log("Children " + children);

					for (Iterator<Entry<Integer, ExplorationState>> iter = children.entrySet().iterator(); iter.hasNext();) {
						Entry<Integer, ExplorationState> entry = iter.next();
						ExplorationState child = entry.getValue();

						if (!visited.contains(child)) {
							// Util.log("Adding to Q: " + child.dumpShort());
							Q.add(child);
							int clickId = entry.getKey();
							map.put(child, new UIAction(state, clickId));
							// Util.log(state.dumpShort() + " @" + clickId + " --> " + child.dumpShort());
						}
					}
				}
			}
			Util.log("|Q|=" + Q.size() + ", done=" + done);
		}

		// TODO: BIGGEST BUG EVER!!!
		// for (int i = 0; i < S.size(); i++) {
		while (!S.isEmpty()) {
			UIAction action = S.pop();
			ret.add(action);
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Shortest path: ");
		sb.append(fromState.dumpShort() + "--> " + toState.dumpShort() + " = {");
		sb.append(ret);
		sb.append("}\n");
		Util.log(sb.toString());

		return ret;
	}

	// Find the first exploration state that refers to given UIState.
	// Guarantee that we return the FIRST exploration state for this cluster-head UI state.
	public ExplorationState findExplorationState(UIState clusterRef) {
		ExplorationState ret = null;

		for (Iterator<ExplorationState> iter = states.iterator(); iter.hasNext();) {
			ExplorationState eState = iter.next();
			UIState refState = eState.getClusterRef();

			// Util.log("Comparing " + refState.dumpShort() + " vs " + uiState.dumpShort());

			if (refState.equals(clusterRef)) {
				// Util.log("Found: " + refState.dumpShort());
				ret = eState;
				break;
			}
		}

		return ret;
	}

	public ExplorationState findNearestExplorationState(ExplorationState fromState) {
		List<UIState> allTodos = UIStateManager.getInstance().getAllTodoState();
		ExplorationState toState, ret = null;
		int minSteps = Integer.MAX_VALUE;

		for (int i = 0; i < allTodos.size(); i++) {
			UIState todoState = allTodos.get(i);
			toState = this.findExplorationState(todoState);

			List<UIAction> actions = copmuteShortestPath(fromState, toState);
			int numSteps = actions.size();

			if (numSteps < minSteps) {
				minSteps = numSteps;
				ret = toState;
			}

			Util.log("Considering TO-DO: " + toState.dumpShort() + " " + numSteps + " steps away");

			// shortcut, won't be shorter than this, so stop!
			if (minSteps == 0) {
				break;
			}
		}

		Util.log("Found: " + ((ret == null) ? "NULL" : ret.dumpShort()));

		return ret;
	}

	public String dumpShort() {
		return "{" + states.size() + " ExplorationState }";
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{\n");
		for (Iterator<ExplorationState> iter = states.iterator(); iter.hasNext();) {
			sb.append("\t" + iter.next() + "\n");
		}
		sb.append("}\n");

		return sb.toString();
	}
}
