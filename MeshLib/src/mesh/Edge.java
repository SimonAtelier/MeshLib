package mesh;

public class Edge {

	private int fromIndex;
	private int toIndex;

	public Edge() {
		initializeIndices();
	}
	
	public Edge(int fromIndex, int toIndex) {
		setFromIndex(fromIndex);
		setToIndex(toIndex);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fromIndex;
		result = prime * result + toIndex;
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
		Edge other = (Edge) obj;
		if (fromIndex != other.fromIndex)
			return false;
		if (toIndex != other.toIndex)
			return false;
		return true;
	}

}
