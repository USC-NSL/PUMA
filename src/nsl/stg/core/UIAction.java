package nsl.stg.core;

public class UIAction {
	private ExplorationState state;
	private int clickId;

	public UIAction(ExplorationState s, int click) {
		state = s;
		clickId = click;
	}

	public ExplorationState getState() {
		return state;
	}

	public int getClickId() {
		return clickId;
	}

	public String toString() {
		return "[ES" + state.getId() + " @" + clickId + "]";
	}
}
