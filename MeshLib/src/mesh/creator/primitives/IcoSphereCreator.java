package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.platonic.IcosahedronCreator;
import mesh.modifier.SpherifyModifier;
import mesh.modifier.subdivision.OneToFourTriangleSplitModifier;

public class IcoSphereCreator implements IMeshCreator {

	private float radius;
	private int subdivisions;
	private Mesh3D mesh;

	public IcoSphereCreator() {
		this(1, 0);
	}

	public IcoSphereCreator(float radius, int subdivisions) {
		this.radius = radius;
		this.subdivisions = subdivisions;
	}
	
	private void createIcosahedron() {
		mesh = new IcosahedronCreator().create();
	}
	
	private void subdivideIcosahedron() {
		OneToFourTriangleSplitModifier modifier = new OneToFourTriangleSplitModifier();
		for (int i = 0; i < subdivisions; i++) {
			modifier.modify(mesh);
		}
	}
	
	private void spherifyIcosahedron() {
		new SpherifyModifier(radius).modify(mesh);
	}

	@Override
	public Mesh3D create() {
		createIcosahedron();
		subdivideIcosahedron();
		spherifyIcosahedron();
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
