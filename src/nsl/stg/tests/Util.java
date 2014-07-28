package nsl.stg.tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Util {
	public static void log(Object obj) {
		long ts = System.currentTimeMillis();
		System.out.println(ts + ": " + obj);
		System.out.flush();
	}

	public static void err(Object obj) {
		System.err.println(obj.toString());
	}

	public static void log2File(Object obj) {
		if (handle != null) {
			handle.println(obj);
		}
	}

	private static PrintWriter handle;
	private static String fileName;

	public static void openFile(String fn, boolean append) {
		try {
			fileName = fn;
			handle = new PrintWriter(new BufferedWriter(new FileWriter(fileName, append)));
		} catch (IOException e) {
			fileName = null;
			handle = null;
		}
	}

	public static void closeFile(String fn) {
		if (fileName != null && fileName.equals(fn)) {
			handle.close();
		}
	}
}
