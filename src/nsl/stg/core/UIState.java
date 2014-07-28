package nsl.stg.core;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.view.accessibility.AccessibilityNodeInfo;

/***
 * Store list of feature vectors and compute cosine similarity between two states.
 *
 */
public class UIState {

	public static final UIState DUMMY_3RD_PARTY_STATE = new UIState(new Hashtable<String, Integer>(), new ArrayList<AccessibilityNodeInfo>(0));
	private Hashtable<String, Integer> features;
	private int nextClickId;
	private int totClickables;
	private List<AccessibilityNodeInfo> clickables;

	public UIState(Hashtable<String, Integer> map, List<AccessibilityNodeInfo> clickables) {
		features = map;
		nextClickId = 0;
		this.clickables = clickables;
		totClickables = clickables.size();
	}

	public List<AccessibilityNodeInfo> getClickables() {
		return this.clickables;
	}

	public Hashtable<String, Integer> getFeatures() {
		return features;
	}

	public String computeFeatureHash() {
		MessageDigest m = null;

		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			/* ignore*/
		}

		if (m == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (Iterator<Entry<String, Integer>> iter = features.entrySet().iterator(); iter.hasNext();) {
			Entry<String, Integer> entry = iter.next();
			sb.append(entry.getKey() + "," + entry.getValue() + "\n");
		}

		String plaintext = sb.toString();

		m.reset();
		m.update(plaintext.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);

		// Now we need to zero pad it if you actually want the full 32 chars.
		while (hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}

		// return hashtext;
		return HashIdDictionary.add(hashtext);
	}

	public int getNextClickId() {
		return nextClickId;
	}

	public void incrementNextClickId() {
		this.nextClickId++;
	}

	public int getTotClickables() {
		return totClickables;
	}

	/**
	 * Calculate the cosine similarity between two UI states.
	 * @param s2
	 * @return cosine similarity
	 */
	public double computeCosineSimilarity(UIState s2) {
		double sim = 0;

		if (s2 == null) {
			return sim;
		}

		Set<String> U = new HashSet<String>();
		Hashtable<String, Integer> features2 = s2.getFeatures();

		U.addAll(features.keySet());
		U.addAll(features2.keySet());

		// Util.log("|U|=" + U.size());

		double N1 = 0, N2 = 0, dot = 0;
		for (Iterator<String> iter = U.iterator(); iter.hasNext();) {
			String key = iter.next();
			int v1 = features.containsKey(key) ? features.get(key) : 0;
			int v2 = features2.containsKey(key) ? features2.get(key) : 0;
			dot += v1 * v2;
			N1 += v1 * v1;
			N2 += v2 * v2;
		}

		sim = dot / (Math.sqrt(N1) * Math.sqrt(N2));
		return sim;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((features == null) ? 0 : features.hashCode());
		result = prime * result + nextClickId;
		result = prime * result + totClickables;
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
		UIState other = (UIState) obj;
		if (features == null) {
			if (other.features != null)
				return false;
		} else if (!features.equals(other.features))
			return false;
		if (nextClickId != other.nextClickId)
			return false;
		if (totClickables != other.totClickables)
			return false;
		return true;
	}

	public boolean isFullyExplored() {
		return (this.nextClickId == totClickables);
	}

	public String dumpShort() {
		return "[" + computeFeatureHash() + ", " + nextClickId + "/" + totClickables + "]";
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		sb.append("  nextClickId=" + nextClickId + " out of " + totClickables + "\n");
		for (Enumeration<String> enu = features.keys(); enu.hasMoreElements();) {
			String key = enu.nextElement();
			int value = features.get(key);
			sb.append("  <" + key + ", " + value + ">\n");

		}
		sb.append("]\n");
		return sb.toString();
	}
}
