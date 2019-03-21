package mesh.wip;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class Mesh3DUtil {
	
	// TODO implement sort method
	// http://stackoverflow.com/questions/6989100/sort-points-in-clockwise-order
	public static void flipDirection(Mesh3D mesh, Face3D face) {
		int[] copy = Arrays.copyOf(face.indices, face.indices.length);
		for (int i = 0; i < face.indices.length; i++) {
			face.indices[i] = copy[face.indices.length - 1 - i];
		}
	}

	public static void flipDirection(Mesh3D mesh) {
		for (Face3D face : mesh.faces) {
			flipDirection(mesh, face);
		}
	}

	public static void bridge(Mesh3D mesh, Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3) {
		int idx0 = mesh.vertices.indexOf(v0);
		int idx1 = mesh.vertices.indexOf(v1);
		int idx2 = mesh.vertices.indexOf(v2);
		int idx3 = mesh.vertices.indexOf(v3);
		Face3D face = new Face3D(idx0, idx1, idx3, idx2);
		// FIXME sort points in clockwise order
		// SortFacepoints sort = new SortFacepoints();
		// sort.sort(mesh, face);
		//
		mesh.faces.add(face);
	}

	public static void bridge(Mesh3D mesh, Face3D f0, Face3D f1) {
		Face3D f2 = new Face3D(f0.indices[0], f0.indices[1], f1.indices[1], f1.indices[0]);
		Face3D f3 = new Face3D(f0.indices[1], f0.indices[2], f1.indices[2], f1.indices[1]);
		Face3D f4 = new Face3D(f0.indices[2], f0.indices[3], f1.indices[3], f1.indices[2]);
		Face3D f5 = new Face3D(f0.indices[3], f0.indices[0], f1.indices[0], f1.indices[3]);
		mesh.faces.add(f2);
		mesh.faces.add(f3);
		mesh.faces.add(f4);
		mesh.faces.add(f5);
	}

	public static void bridge(Mesh3D target, Mesh3D m0, Mesh3D m1) {
		// if m0.vertices.length != m1.vertices.length
		int vertices = m0.getVertexCount();
		for (int i = 0; i < vertices; i++) {
			Mesh3DUtil.bridge(target, m1.getVertexAt(i), m1.getVertexAt((i + 1) % vertices), m0.getVertexAt(i),
					m0.getVertexAt((i + 1) % vertices));
		}
	}

	public static void extrudeFace(Mesh3D mesh, Face3D f, float scale, float amount) {
		int n = f.indices.length;
		int idx = mesh.vertices.size();
		Vector3f normal = calculateFaceNormal(mesh, f);
		Vector3f center = calculateFaceCenter(mesh, f);

		normal.multLocal(amount);

		for (int i = 0; i < n; i++) {
			Vector3f v0 = mesh.vertices.get(f.indices[i]);
			Vector3f v1 = new Vector3f(v0).subtract(center).mult(scale).add(center);

			v1.addLocal(normal);
			mesh.vertices.add(v1);
		}

		for (int i = 0; i < n; i++) {
			Face3D f0 = new Face3D(f.indices[i], f.indices[(i + 1) % n], idx + ((i + 1) % n), idx + i);
			mesh.add(f0);
		}

		for (int i = 0; i < n; i++) {
			f.indices[i] = idx + i;
		}
	}

	public static Vector3f calculateFaceNormal(Mesh3D mesh, int index) {
		Face3D f = mesh.faces.get(index);
		return calculateFaceNormal(mesh, f);
	}

	public static Vector3f calculateFaceNormal(Mesh3D mesh, Face3D f) {
		// https://www.opengl.org/wiki/Calculating_a_Surface_Normal
		Vector3f normal = new Vector3f();
		for (int i = 0; i < f.indices.length; i++) {
			Vector3f current = mesh.vertices.get(f.indices[i]);
			Vector3f next = mesh.vertices.get(f.indices[(i + 1) % f.indices.length]);
			normal.x += (current.y - next.y) * (current.z + next.z);
			normal.y += (current.z - next.z) * (current.x + next.x);
			normal.z += (current.x - next.x) * (current.y + next.y);
		}
		return normal.normalize();
	}

	public static Mesh3D append(Collection<Mesh3D> meshes) {
		return append(meshes.toArray(new Mesh3D[meshes.size()]));
	}

	public static Mesh3D append(Mesh3D... meshes) {
		// FIXME copy vertices and faces
		int n = 0;
		Mesh3D mesh = new Mesh3D();
		List<Vector3f> vertices = mesh.vertices;
		List<Face3D> faces = mesh.faces;

		for (int i = 0; i < meshes.length; i++) {
			Mesh3D m = meshes[i];
			vertices.addAll(m.vertices);
			faces.addAll(meshes[i].faces);
			for (Face3D f : meshes[i].faces) {
				for (int j = 0; j < f.indices.length; j++) {
					f.indices[j] += n;
				}
			}
			n += m.getVertexCount();
		}

		return mesh;
	}

	public static Vector3f calculateFaceCenter(Mesh3D mesh, Face3D f) {
		Vector3f center = new Vector3f();
		for (int i = 0; i < f.indices.length; i++) {
			center.addLocal(mesh.vertices.get(f.indices[i]));
		}
		return center.divideLocal(f.indices.length);
	}

}
