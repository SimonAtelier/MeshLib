package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.SpherifyModifier;
import mesh.modifier.subdivision.TessellationEdgeModifier;

public class QuadSphereCreator implements IMeshCreator {

	private float radius;
	private int subdivisions;
	private Mesh3D mesh;

	public QuadSphereCreator() {
		this(1, 3);
	}

	public QuadSphereCreator(float radius, int subdivisions) {
		this.radius = radius;
		this.subdivisions = subdivisions;
	}

	private void createCube() {
		mesh = new CubeCreator().create();
	}

	private void subdivideCube() {
		for (int i = 0; i < subdivisions; i++) {
			new TessellationEdgeModifier().modify(mesh);
		}
	}

	private void spherifyCube() {
		new SpherifyModifier(radius).modify(mesh);
	}

	@Override
	public Mesh3D create() {
		createCube();
		subdivideCube();
		spherifyCube();
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
