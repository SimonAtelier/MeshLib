package mesh.creator.primitives;

import mesh.Mesh;
import mesh.creator.MeshCreator;

public class PlaneCreator implements MeshCreator {

	private float radius;
	private Mesh mesh;
	
	public PlaneCreator() {
		setRadius(1);
	}
	
	private void createNewMesh() {
		mesh = new Mesh();
	}
	
	private void createVertices() {
		mesh.addVertex(-radius, 0, radius);
		mesh.addVertex(radius, 0, radius);
		mesh.addVertex(radius, 0, -radius);
		mesh.addVertex(-radius, 0, -radius);
	}
	
	private void createFace() {
		mesh.addFace(0, 1, 2, 3);
	}
	
	@Override
	public Mesh create() {
		createNewMesh();
		createVertices();
		createFace();
		return mesh;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}

}
