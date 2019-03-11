package mesh;

public class Face {

	private int[] indices;

	public Face(int... indices) {
		setIndices(indices);
	}

	private void setIndices(int... indices) {
		if (indices.length <= 2)
			throw new IllegalArgumentException("Number of indices must be greater or equal to three.");
		this.indices = indices;
	}

	public int[] getIndices() {
		return indices;
	}

}
