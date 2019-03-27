package mesh.creator.creative;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.unsorted.SegmentedCubeCreator;
import mesh.modifier.SolidifyModifier;
import mesh.modifier.subdivision.CatmullClarkModifier;
import mesh.wip.Mesh3DUtil;

/**
 * Inspired by John Malcolm 
 * https://www.youtube.com/watch?v=8z0Cw3JuGvQ
 * https://www.youtube.com/watch?v=E-a_rBHIf1A
 */
public class PortedCubeCreator implements IMeshCreator {

	private int subdivisions = 3;
	private float size = 1f;
	private float thickness = 0.1f;
	private boolean removeCorners;
	private Mesh3D mesh;
	
	public PortedCubeCreator() {
		this.subdivisions = 3;
		this.size = 1f;
		this.thickness = 0.1f;
		this.removeCorners = true;
	}

	private void removeCornerFaces() {
		HashSet<Face3D> toRemove = new HashSet<Face3D>();
		Mesh3D cube = new CubeCreator(3f).create();
		for (Face3D face : mesh.getFaces()) {
			for (int i = 0; i < face.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(face.indices[i]);
				if (cube.containsVertex(v)) {
					toRemove.add(face);
				}
			}
		}
		mesh.removeFaces(toRemove);
	}

	private void extrudeCenterFaces() {
		List<Face3D> toExtrude = new ArrayList<>();
		for (Face3D face : mesh.getFaces()) {
			Vector3f center = mesh.calculateFaceCenter(face)
					.divideLocal(3f);
			if (center.length() == 1) {
				toExtrude.add(face);
			}
		}
		for (Face3D f : toExtrude) {
			Mesh3DUtil.extrudeFace(mesh, f, 1.0f, -2.0f);
			mesh.removeFace(f);
		}
	}

	private void removeDoubles() {
		Mesh3D m = new Mesh3D();
		HashSet<Vector3f> vertexSet = new HashSet<Vector3f>();
		for (Face3D f : mesh.getFaces()) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				vertexSet.add(v);
			}
		}
		m.addVertices(vertexSet);
		for (Face3D f : mesh.getFaces()) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				int index = m.indexOf(v);
				f.indices[i] = index;
			}
			m.add(f);
		}
		this.mesh = m;
	}

	@Override
	public Mesh3D create() {
		mesh = new SegmentedCubeCreator(3, 3).create();
		if (removeCorners)
			removeCornerFaces();
		extrudeCenterFaces();
		removeDoubles();
		mesh.scale(Mathf.ONE_THIRD);
		mesh.scale(size);
		new SolidifyModifier(thickness).modify(mesh);
		new CatmullClarkModifier(subdivisions).modify(mesh);
		return mesh;
	}

}
