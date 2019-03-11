package mesh;

public class Edge {

	private int fromIndex;
	private int toIndex;

	public Edge() {
		initializeIndices();
	}
	
	private void initializeIndices() {
		fromIndex = -1;
		toIndex = -1;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		if (fromIndex < 0)
			throw new IllegalArgumentException();
		this.fromIndex = fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}

	public void setToIndex(int toIndex) {
		if (toIndex < 0)
			throw new IllegalArgumentException();
		this.toIndex = toIndex;
	}

}
