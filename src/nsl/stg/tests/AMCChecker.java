package nsl.stg.tests;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class AMCChecker {
	private String fn;
	private String dev;

	private final Map<String, Integer> BTN_SIZE_DICT = new Hashtable<String, Integer>();
	private final Map<String, Integer> BTN_DIST_DICT = new Hashtable<String, Integer>();

	public AMCChecker(String xml, String device) {
		fn = xml;
		dev = device;

		// minimal button size
		BTN_SIZE_DICT.put("gn", 12301);
		BTN_SIZE_DICT.put("s3", 11520);
		BTN_SIZE_DICT.put("htc", 27085);

		// minimal button distance
		BTN_DIST_DICT.put("gn", 186);
		BTN_DIST_DICT.put("s3", 180);
		BTN_DIST_DICT.put("htc", 276);
	}

	public void run() {
		UiHierarchyXmlLoader loader = new UiHierarchyXmlLoader();
		BasicTreeNode root = loader.parseXml(fn);

		// make 1 pass to get list of all buttons and check scrolling on the fly
		List<Rectangle> allButtons = new ArrayList<Rectangle>();
		boolean scrolling_violation = false;

		Queue<BasicTreeNode> Q = new LinkedList<BasicTreeNode>();
		Q.add(root);

		while (!Q.isEmpty()) {
			BasicTreeNode btn = Q.poll();

			if (btn instanceof UiNode) {
				UiNode uinode = (UiNode) btn;

				String clz = uinode.getAttribute("class");
				boolean enabled = Boolean.parseBoolean(uinode.getAttribute("enabled"));
				boolean scrolling = Boolean.parseBoolean(uinode.getAttribute("scrollable"));

				if (clz.contains("Button") && enabled) {
					Rectangle bounds = new Rectangle(uinode.x, uinode.y, uinode.width, uinode.height);
					allButtons.add(bounds);
					// Util.log("Found button: " + bounds + " --> " + clz);
				}

				if (scrolling && !scrolling_violation) {
					scrolling_violation = true;
				}
			}

			for (BasicTreeNode child : btn.getChildren()) {
				Q.add(child);
			}
		}

		int btn_size_violation = 0;
		int btn_dist_violation = 0;

		for (int i = 0; i < allButtons.size(); i++) {
			Rectangle b1 = allButtons.get(i);
			double area = b1.getWidth() * b1.getHeight();

			if (area < BTN_SIZE_DICT.get(dev)) {
				// Util.log(b1 + ": " + area);
				btn_size_violation++;
			}

			for (int j = i + 1; j < allButtons.size(); j++) {
				Rectangle b2 = allButtons.get(j);

				double d = get_distance(b1, b2);
				if (d < BTN_DIST_DICT.get(dev)) {
					// Util.log(b1 + " --> " + b2 + ": " + d);
					btn_dist_violation++;
				}
			}
		}

		System.out.println(btn_size_violation + "," + btn_dist_violation + "," + (scrolling_violation ? 1 : 0));
	}

	private double get_distance(Rectangle r1, Rectangle r2) {
		double x1 = r1.getCenterX();
		double y1 = r1.getCenterY();

		double x2 = r2.getCenterX();
		double y2 = r2.getCenterY();

		double delta_x = Math.abs(x1 - x2);
		double delta_y = Math.abs(y1 - y2);

		return Math.sqrt(delta_x * delta_x + delta_y * delta_y);
	}
}
