package nsl.stg.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HashIdDictionary {
	private static Hashtable<String, Integer> dict = new Hashtable<String, Integer>();
	private static List<String> list = new ArrayList<String>();
	private static int cnt = 0;

	// hash --> int
	public static String add(String hash) {
		int ret;
		if (dict.containsKey(hash)) {
			ret = dict.get(hash);
		} else {
			ret = cnt;
			dict.put(hash, cnt);
			list.add(hash);
			cnt++;
		}
		return ret + "";
	}

	// int --> hash
	public static String get(String id) {
		return dict.containsKey(id) ? list.get(Integer.parseInt(id)) : null;
	}
}
