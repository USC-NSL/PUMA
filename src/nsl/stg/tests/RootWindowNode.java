package nsl.stg.tests;

public class RootWindowNode extends BasicTreeNode {

	private final String mWindowName;
	private Object[] mCachedAttributesArray;

	public RootWindowNode(String windowName) {
		mWindowName = windowName;
	}

	@Override
	public String toString() {
		return mWindowName;
	}

	@Override
	public Object[] getAttributesArray() {
		if (mCachedAttributesArray == null) {
			mCachedAttributesArray = new Object[] { new AttributePair("window-name", mWindowName) };
		}
		return mCachedAttributesArray;
	}
}
