package nsl.stg.tests;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DECAFChecker {
	private String fn;
	private final int NOISE = 5; // in case uiautomator reports some noise
	private final Rectangle PORTRAIT_AD_SIZE_MAX = new Rectangle(320 + NOISE, 50 + NOISE);
	private final Rectangle PORTRAIT_AD_SIZE_MIN = new Rectangle(320, 50);
	private final Rectangle LANDSCAPE_AD_SIZE_MAX = new Rectangle(682 + NOISE, 60 + NOISE);
	private final Rectangle LANDSCAPE_AD_SIZE_MIN = new Rectangle(480, 32);

	public DECAFChecker(String xml) {
		fn = xml;
	}

	public void run() {
		UiHierarchyXmlLoader loader = new UiHierarchyXmlLoader();
		BasicTreeNode root = loader.parseXml(fn);

		boolean portrait = (root.width < root.height);

		List<Rectangle> allAds = new ArrayList<Rectangle>();
		List<Rectangle> otherClickables = new ArrayList<Rectangle>();

		Queue<BasicTreeNode> Q = new LinkedList<BasicTreeNode>();
		Q.add(root);

		while (!Q.isEmpty()) {
			BasicTreeNode btn = Q.poll();

			if (btn instanceof UiNode) {
				UiNode uinode = (UiNode) btn;
				Rectangle bounds = new Rectangle(uinode.x, uinode.y, uinode.width, uinode.height);

				String clz = uinode.getAttribute("class");
				boolean enabled = Boolean.parseBoolean(uinode.getAttribute("enabled"));
				boolean clickable = Boolean.parseBoolean(uinode.getAttribute("clickable"));

				if (clz.contains("WebView") && enabled) {
					// Util.log("Found webview: " + bounds + " --> " + clz);
					Rectangle tmp = new Rectangle((int) bounds.getWidth(), (int) bounds.getHeight());

					if (portrait) {
						if (PORTRAIT_AD_SIZE_MAX.contains(tmp)) {
							// candidates for ad
							allAds.add(bounds);
						}
					} else {
						if (LANDSCAPE_AD_SIZE_MAX.contains(tmp)) {
							// candidates for ad
							allAds.add(bounds);
						}
					}
				}

				if (!clz.contains("WebView") && enabled && clickable) {
					otherClickables.add(bounds);
				}
			}

			for (BasicTreeNode child : btn.getChildren()) {
				Q.add(child);
			}
		}

		// check many ads
		int num_ads = allAds.size();

		// check small ad
		int small_ad_cnt = 0;
		for (int i = 0; i < allAds.size(); i++) {
			Rectangle bounds = allAds.get(i);
			Rectangle tmp = new Rectangle((int) bounds.getWidth(), (int) bounds.getHeight());

			if ((portrait && PORTRAIT_AD_SIZE_MIN.contains(tmp)) || (!portrait && LANDSCAPE_AD_SIZE_MIN.contains(tmp))) {
				small_ad_cnt++;
			}
		}

		// check intrusive ads
		int intrusive_ad_cnt = 0;
		for (int i = 0; i < allAds.size(); i++) {
			Rectangle ad = allAds.get(i);

			for (int j = 0; j < otherClickables.size(); j++) {
				Rectangle clickable = otherClickables.get(j);
				if (ad.intersects(clickable)) {
					intrusive_ad_cnt++;
				}
			}
		}

		System.out.println(num_ads + "," + small_ad_cnt + "," + intrusive_ad_cnt);
	}
}
