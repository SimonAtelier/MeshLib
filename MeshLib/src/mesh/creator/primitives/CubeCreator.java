package mesh.creator.primitives;

import mesh.Mesh;
import mesh.creator.IMeshCreator;

public class CubeCreator implements IMeshCreator {

	private float radius;
	private Mesh mesh;
	
	public CubeCreator() {
		setRadius(1);
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
	
	private void createNewMesh() {
		mesh = new Mesh();
	}
	
	@Override
	public Mesh create() {
		createNewMesh();
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
