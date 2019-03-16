package mesh.wip;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class FaceSelection {

	private Mesh3D mesh;
	private HashSet<Face3D> faceSet;

	public FaceSelection(Mesh3D mesh) {
		this.mesh = mesh;
		this.faceSet = new HashSet<>();
	}

	public void selectTriangles() {
		selectByVertexCount(3);
	}

	public void selectQuads() {
		selectByVertexCount(4);
	}

	public void selectByVertexCount(int n) {
		for (Face3D face : mesh.faces) {
			if (face.indices.length == n) {
				faceSet.add(face);
			}
		}
	}

	/**
	 * Selects all faces that have a similar normal as the provided one. This is a
	 * way to select faces that have the same orientation (angle).
	 * 
	 * @param normal
	 * @param threshold
	 */
	public void selectSimilarNormal(Vector3f normal, float threshold) {
		for (Face3D face : mesh.faces) {
			Vector3f normal0 = Mesh3DUtil.calculateFaceNormal(mesh, face);
			Vector3f delta = normal0.subtract(normal).abs();
			// FIXME
			if (delta.x <= threshold && delta.y <= threshold && delta.z <= threshold) {
				faceSet.add(face);
			}
		}
	}

	/**
	 * Selects all faces that have a similar perimeter as the provided one.
	 * 
	 * @param face
	 * @param threshold
	 */
	public void selectSimilarPerimeter(Face3D face, float threshold) {
		float perimeter0 = perimeter(face);
		for (Face3D f : mesh.faces) {
			if (Mathf.abs(perimeter(f) - perimeter0) <= threshold) {
				faceSet.add(f);
			}
		}
	}

	// Move to the util class?
	private float perimeter(Face3D face) {
		float perimeter = 0;
		for (int i = 0; i < face.indices.length - 2; i++) {
			i++;
			Vector3f v0 = mesh.getVertexAt(face.indices[i]);
			Vector3f v1 = mesh.getVertexAt(face.indices[i + 1]);
			perimeter += v0.distance(v1);
		}
		return perimeter;
	}

	public void selectN(int n) {
		for (int i = 0; i < mesh.faces.size(); i++) {
			// if (i % n == 0) {
			Face3D face = mesh.getFaceAt(i);
			if (face.indices[0] % n == 0)
				faceSet.add(face);
			// }
		}
	}

	// Test
	public void selectWithYLassThan(float y) {
		for (Face3D face : mesh.faces) {
			Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, face);
			if (center.y <= y) {
				faceSet.add(face);
			}
		}
	}

	public void selectRegion(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
		for (Face3D f : mesh.faces) {
			int n = f.indices.length;
			boolean add = true;
			for (int i = 0; i < n; i++) {
				Vector3f v = mesh.getVertexAt(i);
				add &= (v.x >= minX && v.x <= maxX && v.y >= minY && v.y <= maxY && v.z >= minZ && v.z <= maxZ);
			}
			if (add) {
				faceSet.add(f);
			}
		}
	}

	public void removeRegion(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
		for (Face3D f : mesh.faces) {
			int n = f.indices.length;
			boolean remove = true;
			for (int i = 0; i < n; i++) {
				Vector3f v = mesh.getVertexAt(i);
				remove &= (v.x >= minX && v.x <= maxX && v.y >= minY && v.y <= maxY && v.z >= minZ && v.z <= maxZ);
			}
			if (remove) {
				faceSet.remove(f);
			}
		}
	}

	public void selectLeftFaces() {
		for (Face3D f : mesh.faces) {
			Vector3f v = Mesh3DUtil.calculateFaceNormal(mesh, f);
			Vector3f v0 = new Vector3f(Mathf.round(v.x), Mathf.round(v.y), Mathf.round(v.z));
			if (v0.x == -1) {
				faceSet.add(f);
			}
		}
	}

	public void selectRightFaces() {
		for (Face3D f : mesh.faces) {
			Vector3f v = Mesh3DUtil.calculateFaceNormal(mesh, f);
			Vector3f v0 = new Vector3f(Mathf.round(v.x), Mathf.round(v.y), Mathf.round(v.z));
			if (v0.x == 1) {
				faceSet.add(f);
			}
		}
	}

	public void selectTopFaces() {
		for (Face3D f : mesh.faces) {
			Vector3f v = Mesh3DUtil.calculateFaceNormal(mesh, f);
			Vector3f v0 = new Vector3f(Mathf.round(v.x), Mathf.round(v.y), Mathf.round(v.z));
			if (v0.y == -1) {
				faceSet.add(f);
			}
		}
	}

	public void selectBottomFaces() {
		for (Face3D f : mesh.faces) {
			Vector3f v = Mesh3DUtil.calculateFaceNormal(mesh, f);
			Vector3f v0 = new Vector3f(Mathf.round(v.x), Mathf.round(v.y), Mathf.round(v.z));
			if (v0.y == 1) {
				faceSet.add(f);
			}
		}
	}

	public void selectFrontFaces() {
		for (Face3D f : mesh.faces) {
			Vector3f v = Mesh3DUtil.calculateFaceNormal(mesh, f);
			Vector3f v0 = new Vector3f(Mathf.round(v.x), Mathf.round(v.y), Mathf.round(v.z));
			if (v0.z == 1) {
				faceSet.add(f);
			}
		}
	}

	public void selectBackFaces() {
		for (Face3D f : mesh.faces) {
			Vector3f v = Mesh3DUtil.calculateFaceNormal(mesh, f);
			Vector3f v0 = new Vector3f(Mathf.round(v.x), Mathf.round(v.y), Mathf.round(v.z));
			if (v0.z == -1) {
				faceSet.add(f);
			}
		}
	}

	public void invert() {
		HashSet<Face3D> faceSet = new HashSet<>();
		faceSet.addAll(mesh.faces);
		faceSet.removeAll(this.faceSet);
		this.faceSet.clear();
		this.faceSet.addAll(faceSet);
	}

	public FaceSelection getInvertedSelection() {
		FaceSelection selection = new FaceSelection(mesh);
		HashSet<Face3D> faceSet = new HashSet<>();
		faceSet.addAll(mesh.faces);
		faceSet.removeAll(this.faceSet);
		selection.faceSet = faceSet;
		return selection;
	}

	public void selectOuter() {
		for (Face3D face : mesh.faces) {
			Vector3f normal = Mesh3DUtil.calculateFaceNormal(mesh, face);
			Vector3f v = mesh.getVertexAt(face.indices[0]);
			if (normal.dot(v) > 0) {
				faceSet.add(face);
			}
		}
	}

	public void selectInner() {
		for (Face3D face : mesh.faces) {
			Vector3f normal = Mesh3DUtil.calculateFaceNormal(mesh, face);
			Vector3f v = mesh.getVertexAt(face.indices[0]);
			if (normal.dot(v) < 0) {
				faceSet.add(face);
			}
		}
	}

	public void selectRandom() {
		int random;
		for (Face3D f : mesh.faces) {
			random = Mathf.random(0, 2);
			if (random == 0) {
				faceSet.add(f);
			}
		}
	}

	public void selectFromTo(int from, int to) {
		faceSet.addAll(mesh.getFaces(from, to));
	}

	public void selectFaceAt(int index) {
		faceSet.add(mesh.getFaceAt(index));
	}

	public void selectAll() {
		faceSet.addAll(mesh.getFaces(0, mesh.getFaceCount()));
	}

	public void add(Face3D face) {
		if (face != null)
			faceSet.add(face);
	}

	public void addAll(FaceSelection selection) {
		faceSet.addAll(selection.faceSet);
	}

	public void remove(Face3D face) {
		if (face != null)
			faceSet.remove(face);
	}

	public void removeAll(FaceSelection selection) {
		faceSet.removeAll(selection.faceSet);
	}

	public void clear() {
		faceSet.clear();
	}

	public Iterator<Face3D> getIterator() {
		return faceSet.iterator();
	}

	public Collection<Face3D> getFaces() {
		return faceSet;
	}

	public Mesh3D getMesh() {
		return mesh;
	}

}
