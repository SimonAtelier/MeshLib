package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class CubeCreator implements IMeshCreator {

	private float radius;
	private Mesh3D mesh;

	public CubeCreator() {
		this(1);
	}

	public CubeCreator(float radius) {
		this.radius = radius;
	}
	
	private void createVertices() {
		mesh.addVertex(radius, -radius, -radius);
		mesh.addVertex(radius, -radius, radius);
		mesh.addVertex(-radius, -radius, radius);
		mesh.addVertex(-radius, -radius, -radius);
		mesh.addVertex(radius, radius, -radius);
		mesh.addVertex(radius, radius, radius);
		mesh.addVertex(-radius, radius, radius);
		mesh.addVertex(-radius, radius, -radius);
	}
	
	private void createFaces() {
		mesh.addFace(3, 0, 1, 2);
		mesh.addFace(6, 5, 4, 7);
		mesh.addFace(1, 0, 4, 5);
		mesh.addFace(1, 5, 6, 2);
		mesh.addFace(6, 7, 3, 2);
		mesh.addFace(3, 7, 4, 0);
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createVertices();
		createFaces();
		return mesh;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

}
