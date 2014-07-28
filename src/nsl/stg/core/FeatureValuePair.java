package nsl.stg.core;

public class FeatureValuePair {
	public int count, size;

	public FeatureValuePair(int count, int size) {
		this.count = count;
		this.size = size;
	}

	public String toString() {
		return "<" + count + ", " + size + ">";
	}
}
