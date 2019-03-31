package mesh.creator.platonic;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class DodecahedronCreator implements IMeshCreator {
	
	private float h;
	private float hSquared;
	private float radius = 1;
	private Mesh3D mesh;
	
	public DodecahedronCreator() {
		h = (-1 - Mathf.sqrt(5)) * 0.5f;
		hSquared = h * h;
	}
	
	private void createVerticesOnCube() {
		mesh.addVertex(1, -1, -1);
		mesh.addVertex(1, -1, 1);
		mesh.addVertex(-1, -1, 1);
		mesh.addVertex(-1, -1, -1);
		mesh.addVertex(1, 1, -1);
		mesh.addVertex(1, 1, 1);
		mesh.addVertex(-1, 1, 1);
		mesh.addVertex(-1, 1, -1);
	}
	
	private void createVertices() {
		createVerticesOnCube();
		mesh.addVertex(0, -(1 + h), (1 -hSquared));
		mesh.addVertex(0, -(1 + h), -(1 - hSquared));
		mesh.addVertex(0, (1 + h), (1 - hSquared));
		mesh.addVertex(0, (1 + h), -(1 - hSquared));
		mesh.addVertex(-(1 + h), (1 - hSquared), 0);
		mesh.addVertex(-(1 + h), -(1 - hSquared), 0);
		mesh.addVertex((1 + h), (1 - hSquared), 0);
		mesh.addVertex((1 + h), -(1 - hSquared), 0);
		mesh.addVertex(-(1 - hSquared), 0, (1 + h));
		mesh.addVertex(-(1 - hSquared), 0, -(1 + h));
		mesh.addVertex((1 - hSquared), 0, (1 + h));
		mesh.addVertex((1 - hSquared), 0, -(1 + h));
	}
	
	private void createFaces() {
		mesh.addFace(12, 1, 11, 2, 14);
		mesh.addFace(12, 0, 16, 17, 1);
		mesh.addFace(1, 17, 5, 9, 11);
		mesh.addFace(2, 11, 9, 6, 19);
		mesh.addFace(12, 14, 3, 10, 0);
		mesh.addFace(0, 10, 8, 4, 16);
		mesh.addFace(14, 2, 19, 18, 3);
		mesh.addFace(3, 18, 7, 8, 10);
		mesh.addFace(9, 5, 13, 15, 6);
		mesh.addFace(16, 4, 13, 5, 17);
		mesh.addFace(18, 19, 6, 15, 7);
		mesh.addFace(15, 13, 4, 8, 7);
	}
	
	private void initializeMesh() {
		mesh = new Mesh3D();
	}
	
	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		mesh.scale(1 / -h);
		mesh.scale(radius);
		return mesh;
	}
	
}
