package mesh.creator.creative;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.TorusCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.wip.Mesh3DUtil;

public class TorusCageCreator implements IMeshCreator {

	private int subdivisions;
	private int majorSegments;
	private int minorSegments;
	private float thickness;
	private float majorRadius;
	private float minorRadius;
	private float extrude;
	private Mesh3D mesh;

	public TorusCageCreator() {
		this.subdivisions = 1;
		this.majorSegments = 24;
		this.minorSegments = 12;
		this.thickness = 0.05f;
		this.majorRadius = 1.0f;
		this.minorRadius = 0.5f;
		this.extrude = 0.8f;
	}

	private void createHoles() {
		List<Face3D> faces = mesh.getFaces();
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, extrude, 0f);
		}
		mesh.removeFaces(faces);
	}

	@Override
	public Mesh3D create() {
		mesh = new TorusCreator(majorRadius, minorRadius, majorSegments, minorSegments).create();
		createHoles();
		new SolidifyModifier(thickness).modify(mesh);
		new CatmullClarkModifier(subdivisions).modify(mesh);
		return mesh;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

	public int getMajorSegments() {
		return majorSegments;
	}

	public void setMajorSegments(int majorSegments) {
		this.majorSegments = majorSegments;
	}

	public int getMinorSegments() {
		return minorSegments;
	}

	public void setMinorSegments(int minorSegments) {
		this.minorSegments = minorSegments;
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public float getMajorRadius() {
		return majorRadius;
	}

	public void setMajorRadius(float majorRadius) {
		this.majorRadius = majorRadius;
	}

	public float getMinorRadius() {
		return minorRadius;
	}

	public void setMinorRadius(float minorRadius) {
		this.minorRadius = minorRadius;
	}

	public float getExtrude() {
		return extrude;
	}

	public void setExtrude(float extrude) {
		this.extrude = extrude;
	}

}
