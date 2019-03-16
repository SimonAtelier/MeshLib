package mesh.creator.platonic;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class IcosahedronCreator implements IMeshCreator {

	private static final float GOLDEN_RATIO = 0.5f * (1 + Mathf.sqrt(5));

	private Mesh3D mesh;

	private void createVertices() {
		createVerticesWithZeroX();
		createVerticesWithZeroZ();
		createVerticesWithZeroY();
	}
	
	private void createVerticesWithZeroX() {
		mesh.addVertex(0, -1, -GOLDEN_RATIO);
		mesh.addVertex(0, 1, -GOLDEN_RATIO);
		mesh.addVertex(0, -1, GOLDEN_RATIO);
		mesh.addVertex(0, 1, GOLDEN_RATIO);
	}
	
	private void createVerticesWithZeroZ() {
		mesh.addVertex(-1, -GOLDEN_RATIO, 0);
		mesh.addVertex(1, -GOLDEN_RATIO, 0);
		mesh.addVertex(-1, GOLDEN_RATIO, 0);
		mesh.addVertex(1, GOLDEN_RATIO, 0);
	}
	
	private void createVerticesWithZeroY() {
		mesh.addVertex(-GOLDEN_RATIO, 0, -1);
		mesh.addVertex(-GOLDEN_RATIO, 0, 1);
		mesh.addVertex(GOLDEN_RATIO, 0, -1);
		mesh.addVertex(GOLDEN_RATIO, 0, 1);
	}

	private void createFaces() {
		mesh.addFace(4, 5, 2);
		mesh.addFace(5, 11, 2);
		mesh.addFace(5, 10, 11);
		mesh.addFace(5, 0, 10);
		mesh.addFace(5, 4, 0);
		mesh.addFace(4, 8, 0);
		mesh.addFace(4, 9, 8);
		mesh.addFace(4, 2, 9);
		mesh.addFace(0, 8, 1);
		mesh.addFace(0, 1, 10);
		mesh.addFace(10, 1, 7);
		mesh.addFace(11, 10, 7);
		mesh.addFace(2, 11, 3);
		mesh.addFace(9, 2, 3);
		mesh.addFace(9, 3, 6);
		mesh.addFace(8, 9, 6);
		mesh.addFace(8, 6, 1);
		mesh.addFace(11, 7, 3);
		mesh.addFace(3, 7, 6);
		mesh.addFace(6, 7, 1);
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}

}
