package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class UVSphereCreator implements IMeshCreator {

	private int rings;
	private int segments;
	private float size;
	private Mesh3D mesh;

	public UVSphereCreator() {
		this(16, 32, 1);
	}

	public UVSphereCreator(int rings, int segments, float size) {
		this.rings = rings;
		this.segments = segments;
		this.size = size;
	}

	private void createVertices() {
		float stepTheta = Mathf.PI / rings;
		float stepPhi = Mathf.TWO_PI / segments;
		for (int row = 1; row < rings; row++) {
			float theta = row * stepTheta;
			for (int col = 0; col < segments; col++) {
				float phi = col * stepPhi;
				float x = size * Mathf.cos(phi) * Mathf.sin(theta);
				float y = size * Mathf.cos(theta);
				float z = size * Mathf.sin(phi) * Mathf.sin(theta);
				mesh.addVertex(x, y, z);
			}
		}
		mesh.add(new Vector3f(0, -size, 0));
		mesh.add(new Vector3f(0, size, 0));
	}

	private int getIndex(int row, int col) {
		int idx = segments * row + col;
		return idx % mesh.vertices.size();
	}

	private void createFaces() {
		for (int row = 0; row < rings - 2; row++) {
			for (int col = 0; col < segments; col++) {
				int a = getIndex(row, (col + 1) % segments);
				int b = getIndex(row + 1, (col + 1) % segments);
				int c = getIndex(row + 1, col);
				int d = getIndex(row, col);
				mesh.add(new Face3D(a, b, c, d));
				if (row == 0) 
					mesh.add(new Face3D(d, mesh.vertices.size() - 1, a));
				if (row == rings - 3) 
					mesh.add(new Face3D(c, b, mesh.vertices.size() - 2));
			}
		}
	}

	@Override
	public Mesh3D create() {
		if (rings == 0 && segments == 0)
			return new Mesh3D();

		mesh = new Mesh3D();
		createVertices();
		createFaces();

		return mesh;
	}

	public int getRings() {
		return rings;
	}

	public void setRings(int rings) {
		this.rings = rings;
	}

	public int getSegments() {
		return segments;
	}

	public void setSegments(int segments) {
		this.segments = segments;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

}
