package nsl.stg.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityNodeInfo {
	private AccessibilityNodeInfo original;
	private MyAccessibilityNodeInfo parent = null;
	private List<MyAccessibilityNodeInfo> children = new ArrayList<MyAccessibilityNodeInfo>();;

	private boolean needScroll = false;
	private Rect scrollBound;
	private boolean toMerge = false;
	private int clusterId;

	public MyAccessibilityNodeInfo(AccessibilityNodeInfo orig) {
		this.original = orig;
	}

	public Rect getScrollBound() {
		return scrollBound;
	}

	public void setScrollBound(Rect scrollBound) {
		this.scrollBound = scrollBound;
	}

	public boolean isToMerge() {
		return toMerge;
	}

	public void setToMerge(boolean toMerge) {
		this.toMerge = toMerge;
	}

	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public void addChild(MyAccessibilityNodeInfo child) {
		children.add(child);
		child.setParent(this);
	}

	public MyAccessibilityNodeInfo getChild(int index) {
		if ((0 <= index) && (index < children.size())) {
			return children.get(index);
		}

		return null;
	}

	public int getChildCount() {
		return children.size();
	}

	public boolean removeChild(MyAccessibilityNodeInfo target) {
		boolean ret = false;

		if (target != null) {
			int idx = -1;

			for (int i = 0; i < children.size(); i++) {
				MyAccessibilityNodeInfo child = children.get(i);
				if (MyAccessibilityNodeInfoSuite.compareAccessibilityNodeInfo(child.getOriginal(), target.getOriginal())) {
					idx = i;
					break;
				}
			}

			if (idx >= 0) {
				children.remove(idx);
				ret = true;
			}
		}

		return ret;
	}

	public MyAccessibilityNodeInfo getParent() {
		return parent;
	}

	void setParent(MyAccessibilityNodeInfo parent) {
		this.parent = parent;
	}

	public boolean needScroll() {
		return this.needScroll;
	}

	public void setNeedScroll(boolean needScroll) {
		this.needScroll = needScroll;
	}

	public void setNeedScrollRecursive(boolean scrollFlag) {
		this.needScroll = scrollFlag;

		for (Iterator<MyAccessibilityNodeInfo> iter = children.iterator(); iter.hasNext();) {
			MyAccessibilityNodeInfo child = iter.next();
			child.setNeedScrollRecursive(scrollFlag);
		}
	}

	public AccessibilityNodeInfo getOriginal() {
		return original;
	}

	public String dumpShort() {
		return original.getClassName() + "";
	}

	public String dumpTree(int level) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < level; i++) {
			sb.append(" ");
		}

		sb.append(original.toString() + "\n");
		for (int i = 0; i < getChildCount(); i++) {
			sb.append(getChild(i).dumpTree(level + 1));
		}

		return sb.toString();
	}

	public String toString() {
		return original.toString() + "";
	}
}
