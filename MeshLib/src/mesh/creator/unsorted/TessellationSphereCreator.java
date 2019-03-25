package mesh.creator.unsorted;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.IcoSphereCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.SpherifyModifier;
import mesh.modifier.subdivision.TessellationEdgeModifier;
import mesh.wip.Mesh3DUtil;

public class TessellationSphereCreator implements IMeshCreator {

	private float radius;
	private float thickness;
	private float scaleExtrude;
	private int subdivisions;
	private Mesh3D mesh;
	
	public TessellationSphereCreator() {
		this.radius = 6f;
		this.thickness = 0.1f;
		this.scaleExtrude = 0.8f;
		this.subdivisions = 1;
	}
	
	private void createHoles() {
		List<Face3D> faces = mesh.getFaces();
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, scaleExtrude, 0.0f);
		}
		mesh.removeFaces(faces);
 	}
	
	private void tessellate() {
		new TessellationEdgeModifier().modify(mesh);
		new TessellationEdgeModifier().modify(mesh);
	}
	
	private void pushToSphere() {
		new SpherifyModifier(radius).modify(mesh);
	}
	
	private void solidify() {
		new SolidifyModifier(thickness).modify(mesh);
	}
	
	private void createIcoSphere() {
		mesh = new IcoSphereCreator(radius, subdivisions).create();
	}
	
	@Override
	public Mesh3D create() {
		createIcoSphere();
		tessellate();
		createHoles();
		pushToSphere();
		solidify();
		return mesh;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public float getScaleExtrude() {
		return scaleExtrude;
	}

	public void setScaleExtrude(float scaleExtrude) {
		this.scaleExtrude = scaleExtrude;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}
