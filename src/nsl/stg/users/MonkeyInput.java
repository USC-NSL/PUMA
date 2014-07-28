package nsl.stg.users;

import nsl.stg.core.UIState;

/**
 * Interface for users to provide Monkey-specific input.
 * @author haos
 *
 */
public interface MonkeyInput {

	public boolean compareState(UIState s1, UIState s2);

	public int getNextClick(UIState s);

	public String getTextInput();

	public long getTimeOut();
}
