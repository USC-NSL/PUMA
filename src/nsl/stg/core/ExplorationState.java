package nsl.stg.core;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import nsl.stg.tests.Util;

public class ExplorationState {
	public static final ExplorationState DUMMY_3RD_PARTY_STATE = new ExplorationState(UIState.DUMMY_3RD_PARTY_STATE, UIState.DUMMY_3RD_PARTY_STATE.computeFeatureHash());
	private UIState clusterRef;
	private String hashId;
	private Map<Integer, ExplorationState> reachables;

	public ExplorationState(UIState ref, String featureHash) {
		this.clusterRef = ref;
		this.hashId = featureHash;
		this.reachables = new Hashtable<Integer, ExplorationState>();
	}

	public void addReachableState(int clickId, ExplorationState target) {
		if (reachables.containsKey(clickId)) {
			Util.log("FAILED to link " + dumpShort() + " @" + clickId + " --> " + target.dumpShort());
			// ERROR: should not happen
			Util.err("ERR: addReachableState(): clickId " + clickId + " already used: " + toString());
			return;
		} else {
			reachables.put(clickId, target);
		}

		Util.log("Linked " + dumpShort() + " @" + clickId + " --> " + target.dumpShort());
	}

	public String getId() {
		return hashId;
	}

	public Map<Integer, ExplorationState> getReachables() {
		return reachables;
	}

	public UIState getClusterRef() {
		return clusterRef;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hashId == null) ? 0 : hashId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExplorationState other = (ExplorationState) obj;
		if (hashId == null) {
			if (other.hashId != null)
				return false;
		} else if (!hashId.equals(other.hashId))
			return false;
		return true;
	}

	public String dumpShort() {
		return "[EState: ref=" + clusterRef.dumpShort() + ", id=" + hashId + "]";
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ ref=" + clusterRef.dumpShort());
		sb.append("\n\tid=" + hashId);
		for (Iterator<Entry<Integer, ExplorationState>> iter = reachables.entrySet().iterator(); iter.hasNext();) {
			Entry<Integer, ExplorationState> entry = iter.next();
			sb.append("\n\t" + entry.getKey() + " --> " + entry.getValue().dumpShort());
		}
		sb.append("}\n");

		return sb.toString();
	}
}
