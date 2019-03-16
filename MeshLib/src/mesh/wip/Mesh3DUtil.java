package mesh.wip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import math.GeometryUtil;
import math.Mathf;
import math.Matrix3f;
import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.primitives.BoxCreator;
import mesh.math.Bounds3;

public class Mesh3DUtil {

//	public static Collection<Edge3D> getEdges(Mesh3D mesh) {
//		HashSet<Edge3D> edges = new HashSet<Edge3D>();
//		for (Face3D f : mesh.faces) {
//			for (int i = 0; i <= f.indices.length; i++) {
//				int fromIndex = f.indices[i % f.indices.length];
//				int toIndex = f.indices[(i + 1) % f.indices.length];
//				Edge3D edge = new Edge3D(fromIndex, toIndex);
//				Edge3D pair = new Edge3D(toIndex, fromIndex);
//				if (!edges.contains(pair))
//					edges.add(edge);
//			}
//		}
//		return new ArrayList<Edge3D>(edges);
//	}

	public static Mesh3D toMesh(Bounds3 bounds) {
		Mesh3D mesh = new BoxCreator(bounds.getWidth(), bounds.getHeight(), bounds.getDepth()).create();
		mesh.translate(bounds.getCenter());
		return mesh;
	}

	/**
	 * FIXME
	 * 
	 * @param mesh
	 * @param vertices
	 * @param segments
	 * @param height
	 * @param close
	 * @return
	 */
	public static Mesh3D extrude(Mesh3D mesh, List<Vector3f> vertices, int segments, float height, boolean close) {
		float halfHeight = height / 2f;
		float segmentHeight = height / segments;
		int vertexCount = vertices.size();

		float h = 0;

		for (int i = 0; i <= segments; i++) {
			for (int j = 0; j < vertexCount; j++) {
				Vector3f v0 = new Vector3f(vertices.get(j)).subtractLocal(0, h, 0);
				mesh.vertices.add(v0);
				if (i < segments) {
					int idx0 = Mathf.toOneDimensionalIndex(i, j, vertexCount);
					int idx1 = Mathf.toOneDimensionalIndex(i, (j + 1) % vertexCount, vertexCount);
					int idx3 = Mathf.toOneDimensionalIndex((i + 1) % (segments + 1), j, vertexCount);
					int idx2 = Mathf.toOneDimensionalIndex((i + 1) % (segments + 1), (j + 1) % vertexCount,
							vertexCount);
					if (j == vertexCount - 1 && !close)
						continue;
					if (i % 2 == 0)
						mesh.add(new Face3D(idx0, idx1, idx2, idx3));
				}
			}
			if (i % 2 == 1)
				// h += 0.01f;
				h += 0;
			else
				h += 0.1f;
		}

		return mesh;
	}

	public static List<Face3D> centerSplit(Mesh3D mesh, Face3D f) {
		int index = mesh.getVertexCount();
		int n = f.indices.length;
		List<Face3D> toAdd = new ArrayList<Face3D>();
		Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, f);
		mesh.add(center);
		for (int i = 0; i < f.indices.length; i++) {
			Face3D f1 = new Face3D(f.indices[i % n], f.indices[(i + 1) % n], index);
			toAdd.add(f1);
		}
		mesh.faces.addAll(toAdd);
		mesh.faces.remove(f);
		return toAdd;
	}

	public static void removeDoubles(Mesh3D mesh) {
		Mesh3D m = new Mesh3D();
		HashSet<Vector3f> vertexSet = new HashSet<Vector3f>();

		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				vertexSet.add(v);
			}
		}

		m.vertices.addAll(vertexSet);

		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				int index = m.vertices.indexOf(v);
				f.indices[i] = index;
			}
			m.add(f);
		}

		mesh.vertices.clear();
		mesh.faces.clear();

		mesh.vertices.addAll(m.vertices);
		mesh.faces.addAll(m.faces);
	}

	public static Mesh3D tessellateByCenter(Mesh3D mesh, Face3D f) {
		int nextIndex = mesh.vertices.size();
		ArrayList<Face3D> toAdd = new ArrayList<>();

		int n = f.indices.length;
		Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, f);
		mesh.add(center);

		for (int i = 0; i < f.indices.length; i++) {
			Face3D f1 = new Face3D(f.indices[i % n], f.indices[(i + 1) % n], nextIndex);
			toAdd.add(f1);
		}

		nextIndex++;

		mesh.faces.remove(f);
		mesh.faces.addAll(toAdd);

		return mesh;
	}

	public static void rotateFaceX(Mesh3D mesh, Face3D face, float a) {
		Matrix3f m = new Matrix3f(1, 0, 0, 0, Mathf.cos(a), -Mathf.sin(a), 0, Mathf.sin(a), Mathf.cos(a));

		for (int i = 0; i < face.indices.length; i++) {
			Vector3f v = mesh.vertices.get(face.indices[i]);
			Vector3f v0 = v.mult(m);
			v.set(v.x, v0.y, v0.z);
		}
	}

	public static void rotateFaceY(Mesh3D mesh, Face3D face, float a) {
		Matrix3f m = new Matrix3f(Mathf.cos(a), 0, Mathf.sin(a), 0, 1, 0, -Mathf.sin(a), 0, Mathf.cos(a));

		for (int i = 0; i < face.indices.length; i++) {
			Vector3f v = mesh.vertices.get(face.indices[i]);
			Vector3f v0 = v.mult(m);
			v.set(v0.x, v.y, v0.z);
		}
	}

	public static void rotateFaceZ(Mesh3D mesh, Face3D face, float a) {
		Matrix3f m = new Matrix3f(Mathf.cos(a), -Mathf.sin(a), 0, Mathf.sin(a), Mathf.cos(a), 0, 0, 0, 1);

		for (int i = 0; i < face.indices.length; i++) {
			Vector3f v = mesh.vertices.get(face.indices[i]);
			Vector3f v0 = v.mult(m);
			v.set(v0.x, v0.y, v.z);
		}
	}

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

	/**
	 * 
	 * @param mesh
	 * @param v0
	 * @param v1
	 * @param v2
	 * @param v3
	 */
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

	/**
	 * 
	 * @param mesh
	 * @param f0
	 * @param f1
	 */
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

	// public static Vector3f innkreis(Mesh3D mesh, Face3D face) {
	// http://www.mathe-online.at/lernpfade/Schwerpunkte/?kapitel=3&navig=l
	// Vector3f va = mesh.getVertexAt(face.indices[2]);
	// Vector3f vb = mesh.getVertexAt(face.indices[1]);
	// Vector3f vc = mesh.getVertexAt(face.indices[0]);
	//
	// float a = vb.subtract(vc).length();
	// float b = vc.subtract(va).length();
	// float c = va.subtract(vb).length();
	// float u = a + b + c;
	//
	// Vector3f va0 = va.mult(a);
	// Vector3f vb0 = vb.mult(b);
	// Vector3f vc0 = vc.mult(c);
	//
	// Vector3f result = va0.add(vb0).add(vc0);
	// result.divideLocal(u);
	//
	//// mesh.add(result);
	//
	// return result;
	// }

	/**
	 * 
	 * @param mesh
	 * @param f
	 * @param scale
	 * @param amount
	 */
	public static void extrudeFace(Mesh3D mesh, Face3D f, float scale, float amount) {
		// FIXED: Works now for faces with vertices.length > 4 too
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

	/**
	 * 
	 * @param mesh
	 * @param f
	 * @param thickness
	 * @param amount
	 */
	public static void insetFace(Mesh3D mesh, Face3D f, float thickness) {
		// FIXED: Works now for faces with vertices.length > 4 too
		int n = f.indices.length;
		int idx = mesh.vertices.size();
		Vector3f normal = calculateFaceNormal(mesh, f);
		Vector3f center = calculateFaceCenter(mesh, f);

		List<Vector3f> verts = new ArrayList<Vector3f>();

		for (int i = 0; i < n; i++) {
			Vector3f v0 = mesh.vertices.get(f.indices[i]);
			Vector3f v1 = mesh.vertices.get(f.indices[(i + 1) % f.indices.length]);

			// Vector3f v2 = new Vector3f(v0).subtract(center).mult(scale)
			// .add(center);

			Vector3f edgeN = v1.add(v0).normalize();
			// Vector3f mid = v0.add(v1).mult(0.5f);
			// System.out.println(i);
			// System.out.println(mid);

			float distance = v1.subtract(v0).length();
			float a = 1 / distance * thickness;

			Vector3f v4 = v1.subtract(v0).mult(a).add(v0);
			Vector3f v5 = v1.add(v1.subtract(v0).mult(-a));

			// mesh.add(new Vector3f(v2));
			// mesh.add(new Vector3f(v3));

			verts.add(v4);
			verts.add(v5);
		}

		for (int i = 1; i < verts.size(); i += 2) {
			int a = verts.size() - 2 + i;
			Vector3f v0 = verts.get(a % verts.size());
			Vector3f v1 = verts.get((a + 1) % verts.size());
			Vector3f v = v1.add(v0).mult(0.5f);
			mesh.add(v);
		}

		// mesh.vertices.addAll(verts);

		for (int i = 0; i < n; i++) {
			Face3D f0 = new Face3D(f.indices[i], f.indices[(i + 1) % n], idx + ((i + 1) % n), idx + i);
			mesh.add(f0);
		}

		for (int i = 0; i < n; i++) {
			f.indices[i] = idx + i;
		}
	}

	/**
	 * 
	 * @param mesh
	 * @param index
	 * @return
	 */
	public static Vector3f calculateFaceNormal(Mesh3D mesh, int index) {
		Face3D f = mesh.faces.get(index);
		return calculateFaceNormal(mesh, f);
	}

	/**
	 * 
	 * @param mesh
	 * @param f
	 * @return
	 */
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

	public static Vector3f calculateFaceNormal(Vector3f... vertices) {
		// https://www.opengl.org/wiki/Calculating_a_Surface_Normal
		Vector3f normal = new Vector3f();
		for (int i = 0; i < vertices.length; i++) {
			Vector3f current = vertices[i];
			Vector3f next = vertices[(i + 1) % vertices.length];
			normal.x += (current.y - next.y) * (current.z + next.z);
			normal.y += (current.z - next.z) * (current.x + next.x);
			normal.z += (current.x - next.x) * (current.y + next.y);
		}
		return normal.normalize();
	}

	/**
	 * 
	 * @param mesh
	 */
	public static void subdivide(Mesh3D mesh) {
		List<Face3D> toAdd = new ArrayList<>();
		HashMap<Vector3f, Integer> map = new HashMap<>();
		int nextIndex = mesh.vertices.size();

		for (Face3D f : mesh.faces) {
			Vector3f v0 = mesh.vertices.get(f.indices[0]);
			Vector3f v1 = mesh.vertices.get(f.indices[1]);
			Vector3f v2 = mesh.vertices.get(f.indices[2]);
			Vector3f v3 = mesh.vertices.get(f.indices[3]);

			Vector3f cp = v0.add(v1).add(v2).add(v3).mult(0.25f);

			Vector3f v4 = GeometryUtil.getMidpoint(v0, v1);
			Vector3f v5 = GeometryUtil.getMidpoint(v1, v2);
			Vector3f v6 = GeometryUtil.getMidpoint(v2, v3);
			Vector3f v7 = GeometryUtil.getMidpoint(v3, v0);

			int[] idxs = new int[5];
			Vector3f[] ePts = new Vector3f[] { v4, v5, v6, v7 };

			mesh.vertices.add(cp);
			idxs[0] = nextIndex;
			nextIndex++;

			for (int i = 0; i < ePts.length; i++) {
				Integer idx = map.get(ePts[i]);
				if (idx == null) {
					map.put(ePts[i], nextIndex);
					mesh.vertices.add(ePts[i]);
					idxs[i + 1] = nextIndex;
					nextIndex++;
				} else {
					idxs[i + 1] = idx;
				}
			}

			Face3D f0 = new Face3D(f.indices[0], idxs[1], idxs[0], idxs[4]);
			Face3D f1 = new Face3D(idxs[1], f.indices[1], idxs[2], idxs[0]);
			Face3D f2 = new Face3D(idxs[0], idxs[2], f.indices[2], idxs[3]);
			Face3D f3 = new Face3D(idxs[4], idxs[0], idxs[3], f.indices[3]);

			toAdd.add(f0);
			toAdd.add(f1);
			toAdd.add(f2);
			toAdd.add(f3);
		}

		mesh.faces.clear();
		mesh.faces.addAll(toAdd);
	}

	public static Mesh3D append(Collection<Mesh3D> meshes) {
		return append(meshes.toArray(new Mesh3D[meshes.size()]));
	}

	/**
	 * 
	 * @param meshes
	 * @return
	 */
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
					// f.indices[0] += n;
					// f.indices[1] += n;
					// f.indices[2] += n;
					// f.indices[3] += n;
				}
			}
			n += m.getVertexCount();
		}

		return mesh;
	}

	/**
	 * 
	 * @param mesh
	 * @param t
	 */
	public static void translate(Mesh3D mesh, Vector3f t) {
		for (Vector3f v : mesh.vertices) {
			v.addLocal(t);
		}
	}

	/**
	 * 
	 * 
	 * @param mesh
	 * @param f
	 * @return
	 * @version 0.2, Works now for faces with n vertices.
	 */
	public static Vector3f calculateFaceCenter(Mesh3D mesh, Face3D f) {
		Vector3f center = new Vector3f();
		for (int i = 0; i < f.indices.length; i++) {
			center.addLocal(mesh.vertices.get(f.indices[i]));
		}
		return center.divideLocal(f.indices.length);
	}

	/**
	 * Works for faces with n vertices.
	 * 
	 * @param mesh
	 * @param index
	 * @return
	 * @version 0.2, Works now for faces with n vertices.
	 */
	public static Vector3f calculateFaceCenter(Mesh3D mesh, int index) {
		Face3D f = mesh.faces.get(index);
		return calculateFaceCenter(mesh, f);
	}

	/**
	 * 
	 * @param mesh
	 * @param f
	 * @param index
	 * @param scale
	 * @version 0.2, Works now for faces with n vertices.
	 */
	public static void scaleFace(Mesh3D mesh, Face3D f, float scale) {
		Vector3f center = calculateFaceCenter(mesh, f);
		for (int i = 0; i < f.indices.length; i++) {
			Vector3f v = mesh.vertices.get(f.indices[i]);
			v.subtractLocal(center).multLocal(scale).addLocal(center);
		}
	}

	/**
	 * 
	 * @param mesh
	 * @param index
	 * @param scale
	 * @version 0.2, Works now for faces with n vertices.
	 */
	public static void scaleFaceAt(Mesh3D mesh, int index, float scale) {
		Face3D f = mesh.faces.get(index);
		scaleFace(mesh, f, scale);
	}

	/**
	 * 
	 * @param mesh
	 * @param radius
	 * @return
	 */
	public static Mesh3D pushToSphere(Mesh3D mesh, float radius) {
		return pushToSphere(mesh, new Vector3f(), radius);
	}

	public static Mesh3D pushToSphere(Mesh3D mesh, Vector3f center, float radius) {
		Vector3f origin = new Vector3f(center);
		for (Vector3f v : mesh.vertices) {
			Vector3f v0 = new Vector3f(v.x - origin.x, v.y - origin.y, v.z - origin.z).normalizeLocal();
			v.set(v0.mult(radius).add(origin));
		}
		return mesh;
	}

	/**
	 * 
	 * @param mesh
	 * @param face
	 * @param v
	 * @return
	 */
	public static Mesh3D translateFace(Mesh3D mesh, Face3D face, Vector3f v) {
		for (int i = 0; i < face.indices.length; i++) {
			Vector3f v0 = mesh.vertices.get(face.indices[i]);
			v0.addLocal(v);
		}
		return mesh;
	}

	/**
	 * 
	 * @param mesh
	 * @param scale
	 */
	public static void scale(Mesh3D mesh, float scale) {
		for (Vector3f v : mesh.vertices) {
			v.multLocal(scale);
		}
	}

	/**
	 * 
	 * @param mesh
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void scale(Mesh3D mesh, float x, float y, float z) {
		Vector3f scale = new Vector3f(x, y, z);
		scale(mesh, scale);
	}

	/**
	 * 
	 * @param mesh
	 * @param scale
	 */
	public static void scale(Mesh3D mesh, Vector3f scale) {
		for (Vector3f v : mesh.vertices) {
			v.multLocal(scale);
		}
	}

	/**
	 * 
	 * @param mesh
	 * @param index
	 */
	public static void removeFaceAt(Mesh3D mesh, int index) {
		mesh.faces.remove(index);
	}

	// /**
	// * Will also remove faces.
	// *
	// * @param mesh
	// * @param index
	// */
	// public static void removeVertexAt(Mesh3D mesh, int index) {
	// List<Face3D> toRemove = new ArrayList<>();
	// mesh.vertices.remove(index);
	// for (Face3D f : mesh.faces) {
	// if (f.containsIndex(index)) {
	// toRemove.add(f);
	// }
	// }
	// mesh.faces.removeAll(toRemove);
	// }

	/**
	 * 
	 * @param mesh
	 * @param v
	 * @return
	 */
	public static int indexOf(Mesh3D mesh, Vector3f v) {
		return mesh.vertices.indexOf(v);
	}

	/**
	 * 
	 * @param mesh
	 * @param from
	 * @param to
	 * @return
	 */
	public static List<Face3D> getFaces(Mesh3D mesh, int from, int to) {
		return new ArrayList<>(mesh.faces.subList(from, to));
	}

	/**
	 * 
	 * @param mesh
	 * @param from
	 * @param to
	 * @return
	 */
	public static List<Vector3f> getVertices(Mesh3D mesh, int from, int to) {
		return new ArrayList<>(mesh.vertices.subList(from, to));
	}

	/**
	 * 
	 * @param mesh
	 * @param s
	 * @return a handle to the scaled copy of the provided mesh
	 */
	public static Mesh3D scaleCopy(Mesh3D mesh, Vector3f s) {
		Mesh3D copy = new Mesh3D();
		List<Vector3f> vertices = copy.vertices;
		List<Face3D> faces = copy.faces;
		// Copy and translate vertices
		for (Vector3f v : mesh.vertices) {
			vertices.add(new Vector3f(v).multLocal(s));
		}
		// Copy faces
		for (Face3D f : mesh.faces) {
			faces.add(new Face3D(f));
		}
		return copy;
	}

}