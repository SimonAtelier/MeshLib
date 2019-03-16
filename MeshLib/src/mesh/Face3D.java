package mesh;

import math.Vector3f;

public class Face3D {

	public int[] indices;
	public Vector3f normal;

	public Face3D() {
		this(new int[0]);
	}

	public Face3D(int... indices) {
		this.indices = new int[indices.length];
		normal = new Vector3f();
		for (int i = 0; i < indices.length; i++) {
			this.indices[i] = indices[i];
		}
	}

	public Face3D(Face3D f) {
		this(f.indices);
	}
	
}
