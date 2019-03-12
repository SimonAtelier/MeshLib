package mesh;

import mesh.exception.NumberOfIndicesMustBeGreaterOrEqualToThree;

public class Face {

	private int[] indices;

	public Face(int... indices) {
		setIndices(indices);
	}

	private void setIndices(int... indices) {
		if (indices.length <= 2)
			throw new NumberOfIndicesMustBeGreaterOrEqualToThree();
		this.indices = indices;
	}

	public int[] getIndices() {
		return indices;
	}

}
