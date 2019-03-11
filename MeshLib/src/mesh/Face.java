package mesh;

public class Face {

	private int[] indices;
	
	public Face(int...indices) {
		this.indices = indices;
	}
	
	public int[] getIndices() {
		return indices;
	}
	
}
