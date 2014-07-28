package nsl.stg.tests;

import java.awt.Rectangle;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		if (args.length != 2) {
			Util.err("xml-file gn|s3|htc");
			System.exit(-1);
		}

		new AMCChecker(args[0], args[1]).run();
		// new DECAFChecker(args[0]).run();
	}

	// Load and parse stored XML tree file
	private static void parse_xml() {
		UiHierarchyXmlLoader loader = new UiHierarchyXmlLoader();
		BasicTreeNode btn = loader.parseXml("/home/haos/haos.xml");

		Util.log(btn.getChildrenList());
		Util.log(dump_node(btn, 0));

		for (BasicTreeNode child : btn.getChildren()) {
			if (child instanceof UiNode) {
				search_for_clickables((UiNode) child);
			} else {
				Util.err(child + " is NOT UiNode");
			}
		}

		List<Rectangle> nafs = loader.getNafNodes();
		Util.log(nafs);
	}

	// Dump tree rooted at "node"
	private static String dump_node(BasicTreeNode node, int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append("-");
		}
		sb.append(node.toString() + "\n");

		if (node.getChildCount() > 0) {
			for (BasicTreeNode child : node.getChildren()) {
				sb.append(dump_node(child, level + 1));
			}
		}
		return sb.toString();
	}

	// Recursive search for clickable nodes in a tree rooted at "node"
	private static void search_for_clickables(UiNode node) {
		if (node.getAttribute("clickable").equals("true") && !node.hasChild()) {
			Util.log(node);
		}

		if (node.hasChild()) {
			for (BasicTreeNode child : node.getChildren()) {
				if (child instanceof UiNode) {
					search_for_clickables((UiNode) child);
				} else {
					Util.err("NON UiNode: " + child);
				}
			}
		}
	}
}
