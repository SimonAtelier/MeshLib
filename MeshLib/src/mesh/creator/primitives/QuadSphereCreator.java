package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.wip.Mesh3DUtil;

public class QuadSphereCreator implements IMeshCreator {

	private float radius;
	private int subdivisions;
	
	public QuadSphereCreator() {
		this.subdivisions = 3;
		this.radius = 1.0f;
	}
	
	public QuadSphereCreator(float radius, int subdivisions) {
		this.radius = radius;
		this.subdivisions = subdivisions;
	}

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new CubeCreator().create();
		for (int i = 0; i < subdivisions; i++) {
			Mesh3DUtil.subdivide(mesh);
		}
		Mesh3DUtil.scale(mesh, radius);
		Mesh3DUtil.pushToSphere(mesh, radius);
		return mesh;
	}
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}
	
}
