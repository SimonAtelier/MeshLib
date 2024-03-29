package mesh.creator.special;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class AntiprismCreator implements IMeshCreator {

	private int n;
	private Mesh3D mesh;

	public AntiprismCreator() {
		this(6);
	}

	public AntiprismCreator(int n) {
		this.n = n;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void createFaces() {
		int twoN = n + n;
		int[] indices0 = new int[n];
		int[] indices1 = new int[n];
		
		for (int i = 0; i < 2 * n; i++) {
			if (i % 2 == 1) {
				mesh.addFace(i % twoN, (i + 1) % twoN, (i + 2) % twoN);
				indices0[i / 2] = i;
			} else {
				mesh.addFace(i % twoN, (i + 2) % twoN, (i + 1) % twoN);
				indices1[(n - 1) - (i / 2)] = i;
			}
		}

		mesh.add(new Face3D(indices0));
		mesh.add(new Face3D(indices1));
	}

	private void createVertices() {
		float piDividedByN = Mathf.PI / n;
		float twoPiDividedByN = Mathf.TWO_PI / n;
		float h = Mathf.sqrt(((Mathf.cos(piDividedByN)) - (Mathf.cos(twoPiDividedByN))) / 2.0f);
		for (int k = 0; k < 2 * n; k++) {
			float x = Mathf.cos(k * Mathf.PI / n);
			float z = Mathf.sin(k * Mathf.PI / n);
			float y = Mathf.pow(-1, k) * h;
			mesh.addVertex(x, y, z);
		}
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

}
