package mesh.wip;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class Mesh3DUtil {
	
	@Deprecated
	public static void flipDirection(Mesh3D mesh, Face3D face) {
		int[] copy = Arrays.copyOf(face.indices, face.indices.length);
		for (int i = 0; i < face.indices.length; i++) {
			face.indices[i] = copy[face.indices.length - 1 - i];
		}
	}

	@Deprecated
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

	@Deprecated
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

	public static void extrudeFace(Mesh3D mesh, Face3D f, float scale, float amount) {
		int n = f.indices.length;
		int idx = mesh.vertices.size();
		Vector3f normal = mesh.calculateFaceNormal(f);
		Vector3f center = mesh.calculateFaceCenter(f);

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

}
