package nsl.stg.users;

import nsl.stg.core.UIState;

/**
 * This factory class provides set of pre-defined library (frequently used) functions. 
 * @author haos
 *
 */
public class MonkeyInputFactory {

	public static boolean stateExactMatch(UIState s1, UIState s2) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean stateStructureMatch(UIState s1, UIState s2, double threshold) {
		// TODO Auto-generated method stub
		return false;
	}

	public static int nextClickSequential(UIState s) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static int nextClickMaxType(UIState s) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long defaultTimeOut() {
		return 1200000; // 20min * 60 * 1000 = 1,200,000 milli-second
	}

}
