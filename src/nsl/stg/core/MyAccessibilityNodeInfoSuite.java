package nsl.stg.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nsl.stg.tests.Util;
import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

public class MyAccessibilityNodeInfoSuite {

	// Rebuild the tree with MyAccessibilityNodeInfo class
	public static MyAccessibilityNodeInfo rebuildTree(AccessibilityNodeInfo root) {
		MyAccessibilityNodeInfo newRoot = new MyAccessibilityNodeInfo(root);

		for (int i = 0; i < root.getChildCount(); i++) {
			AccessibilityNodeInfo child = root.getChild(i);
			MyAccessibilityNodeInfo newChild = MyAccessibilityNodeInfoSuite.rebuildTree(child);
			newRoot.addChild(newChild);
		}

		return newRoot;
	}

	// Dump tree info
	public static void dumpTree(MyAccessibilityNodeInfo root, int level) {
		AccessibilityNodeInfo content = root.getOriginal();

		String s = "";
		for (int i = 0; i < level; i++) {
			s += " ";
		}

		Util.log(s + content.getClassName() + "," + content.getText() + "," + root.needScroll() + "," + root.getScrollBound());

		for (int i = 0; i < root.getChildCount(); i++) {
			MyAccessibilityNodeInfoSuite.dumpTree(root.getChild(i), level + 1);
		}
	}

	public static void dumpTree(AccessibilityNodeInfo root, int level) {
		AccessibilityNodeInfo content = root;

		String s = "";
		for (int i = 0; i < level; i++) {
			s += " ";
		}

		Rect bound = new Rect();
		root.getBoundsInParent(bound);
		Util.log(s + content.getClassName() + "," + content.getText() + "," + content.isVisibleToUser() + "," + bound);

		for (int i = 0; i < root.getChildCount(); i++) {
			MyAccessibilityNodeInfoSuite.dumpTree(root.getChild(i), level + 1);
		}
	}

	// Find matching target fromIndex given tree
	public static MyAccessibilityNodeInfo findNode(MyAccessibilityNodeInfo root, AccessibilityNodeInfo target) {
		if (compareAccessibilityNodeInfo(root.getOriginal(), target)) {
			return root;
		}

		for (int i = 0; i < root.getChildCount(); i++) {
			MyAccessibilityNodeInfo found = MyAccessibilityNodeInfoSuite.findNode(root.getChild(i), target);
			if (found != null) {
				return found;
			}
		}

		return null;
	}

	// Find matching target fromIndex given tree
	public static AccessibilityNodeInfo findNode(AccessibilityNodeInfo root, AccessibilityNodeInfo target) {
		if (compareAccessibilityNodeInfo(root, target)) {
			return root;
		}

		for (int i = 0; i < root.getChildCount(); i++) {
			AccessibilityNodeInfo found = MyAccessibilityNodeInfoSuite.findNode(root.getChild(i), target);
			if (found != null) {
				return found;
			}
		}

		return null;
	}

	public static MyAccessibilityNodeInfo findNodeWithClusterId(List<MyAccessibilityNodeInfo> roots, int clusterId) {
		for (Iterator<MyAccessibilityNodeInfo> iter = roots.iterator(); iter.hasNext();) {
			MyAccessibilityNodeInfo found = findNodeWithClusterId(iter.next(), clusterId);
			if (found != null) {
				return found;
			}
		}

		return null;
	}

	private static MyAccessibilityNodeInfo findNodeWithClusterId(MyAccessibilityNodeInfo root, int clusterId) {
		if (root.getClusterId() == clusterId) {
			return root;
		}

		for (int i = 0; i < root.getChildCount(); i++) {
			MyAccessibilityNodeInfo found = findNodeWithClusterId(root.getChild(i), clusterId);
			if (found != null) {
				return found;
			}
		}

		return null;
	}

	// My own equals method for two AccessibilityNodeInfo nodes
	public static boolean compareAccessibilityNodeInfo(AccessibilityNodeInfo n1, AccessibilityNodeInfo n2) {
		if (n1 != null && n2 != null) {
			String c1 = n1.getClassName().toString();
			String c2 = n2.getClassName().toString();

			Rect b1 = new Rect();
			Rect b2 = new Rect();
			n1.getBoundsInScreen(b1);
			n2.getBoundsInScreen(b2);

			int a1 = b1.height() * b1.width();
			b1.intersect(b2);
			int a2 = b1.height() * b1.width();

			return c1.equals(c2) && (a2 > ((int) (a1 * 0.95)));
		} else {
			return false;
		}
	}

	// Mark leaf node's "toMerge" flag to true.
	public static void markLeafNodeForMerging(MyAccessibilityNodeInfo root) {
		Queue<MyAccessibilityNodeInfo> Q = new LinkedList<MyAccessibilityNodeInfo>();
		Q.add(root);

		while (!Q.isEmpty()) {
			MyAccessibilityNodeInfo node = Q.remove();

			if (node == null) {
				Util.log("Processing NULL");
				continue;
			}

			if (!node.getOriginal().isVisibleToUser()) {
				Util.log("Processing HIDEN nodes");
				continue;
			}

			int childCnt = node.getChildCount();
			if (childCnt > 0) {
				for (int i = 0; i < childCnt; i++) {
					MyAccessibilityNodeInfo child = node.getChild(i);
					Q.add(child); // no need to check NULL, checked above
				}
			} else {
				Util.log("Marked leaf: " + node.dumpShort());
				node.setToMerge(true);
			}
		}
	}

	// Traverse the tree (BFS) and find all nodes to be merged next round.
	public static List<MyAccessibilityNodeInfo> findMergeNodes(MyAccessibilityNodeInfo root) {
		List<MyAccessibilityNodeInfo> ret = new ArrayList<MyAccessibilityNodeInfo>();
		Queue<MyAccessibilityNodeInfo> Q = new LinkedList<MyAccessibilityNodeInfo>();
		Q.add(root);

		while (!Q.isEmpty()) {
			MyAccessibilityNodeInfo node = Q.remove();

			if (node == null) {
				Util.log("Processing NULL");
				continue;
			}

			if (node.isToMerge()) {
				ret.add(node);
				continue;
			}

			int childCnt = node.getChildCount();
			if (childCnt > 0) {
				for (int i = 0; i < childCnt; i++) {
					MyAccessibilityNodeInfo child = node.getChild(i);
					Q.add(child); // no need to check NULL, checked above
				}
			}
		}

		return ret;
	}
}
