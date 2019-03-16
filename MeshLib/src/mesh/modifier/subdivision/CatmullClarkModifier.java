package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.Pair;
import mesh.modifier.IMeshModifier;

/**
 * An implementation of the Catmull-Clark subdivision surface. It was developed
 * in 1978 by Edwin Catmull and Jim Clark.
 * 
 * @version 0.3, 30 June 2016
 */
public class CatmullClarkModifier implements IMeshModifier {

	private int subdivisions;
	private int originalVertexCount;
	private Mesh3D meshToSubdivide;
	private HashMap<Pair, Vector3f> mapEdgesToFacePoints;
	private HashMap<Pair, Integer> mapEdgesToEdgePointIndicies;
	private HashMap<Integer, Integer> mapVertexIndexToNumberOfOutgoingEdges;
	private HashMap<Integer, List<Vector3f>> mapOriginalVerticesToFacePoints;
	private HashMap<Integer, List<Vector3f>> mapVerticesToEdgePoints;

	public CatmullClarkModifier() {
		this(1);
	}

	public CatmullClarkModifier(int subdivisions) {
		this.subdivisions = subdivisions;
		this.mapEdgesToFacePoints = new HashMap<Pair, Vector3f>();
		this.mapEdgesToEdgePointIndicies = new HashMap<Pair, Integer>();
		this.mapVertexIndexToNumberOfOutgoingEdges = new HashMap<Integer, Integer>();
		this.mapOriginalVerticesToFacePoints = new HashMap<Integer, List<Vector3f>>();
		this.mapVerticesToEdgePoints = new HashMap<Integer, List<Vector3f>>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		this.meshToSubdivide = mesh;
		for (int i = 0; i < subdivisions; i++) {
			originalVertexCount = mesh.getVertexCount();
			mapEdgesToFacePoints.clear();
			mapEdgesToEdgePointIndicies.clear();
			mapVertexIndexToNumberOfOutgoingEdges.clear();
			mapOriginalVerticesToFacePoints.clear();
			mapVerticesToEdgePoints.clear();
			subdivideMesh();
			processEdgePoints();
			smoothVertices();
		}
		return mesh;
	}

	protected Vector3f getFacePointsAverage(int index) {
		Vector3f v0 = new Vector3f();
		List<Vector3f> facePoints = mapOriginalVerticesToFacePoints.get(index);
		for (Vector3f v1 : facePoints) {
			v0.addLocal(v1);
		}
		return v0.mult(1f / (float) facePoints.size());
	}

	protected Vector3f getEdgePointAverage(int index) {
		Vector3f v0 = new Vector3f();
		List<Vector3f> edgePoints = mapVerticesToEdgePoints.get(index);
		for (Vector3f v1 : edgePoints) {
			v0.addLocal(v1);
		}
		return v0.mult(1f / (float) edgePoints.size());
	}

	protected void incrementN(Pair[] pairs) {
		for (Pair pair : pairs) {
			Integer n = mapVertexIndexToNumberOfOutgoingEdges.get(pair.a);
			if (n == null) {
				n = 0;
			}
			n += 1;
			mapVertexIndexToNumberOfOutgoingEdges.put(pair.a, n);
		}
	}

	protected void smoothVertices() {
		for (int i = 0; i < originalVertexCount; i++) {
			float n = (float) mapVertexIndexToNumberOfOutgoingEdges.get(i);
			Vector3f d = meshToSubdivide.vertices.get(i);
			Vector3f fpSum = getFacePointsAverage(i);
			Vector3f epSum = getEdgePointAverage(i);
			Vector3f v = fpSum.add(epSum.mult(2f).add(d.mult(n - 3)));
			v.divideLocal(n);
			d.set(v);
		}
	}

	protected void processEdgePoints() {
		for (Pair pair : mapEdgesToEdgePointIndicies.keySet()) {
			int index = mapEdgesToEdgePointIndicies.get(pair);
			Vector3f v0 = meshToSubdivide.vertices.get(pair.a);
			Vector3f v1 = meshToSubdivide.vertices.get(pair.b);
			Vector3f fp0 = mapEdgesToFacePoints.get(pair);
			Vector3f fp1 = mapEdgesToFacePoints.get(new Pair(pair.b, pair.a));
			if (v0 != null && v1 != null && fp0 != null && fp1 != null) {
				Vector3f ep = v0.add(v1).add(fp0).add(fp1).mult(0.25f);
				meshToSubdivide.vertices.get(index).set(ep);
			}
		}
	}

	protected void subdivideMesh() {
		int index = meshToSubdivide.getVertexCount();
		ArrayList<Face3D> facesToAdd = new ArrayList<Face3D>();
		ArrayList<Face3D> facesToRemove = new ArrayList<Face3D>();

		// for each original face
		for (Face3D f : meshToSubdivide.faces) {
			int n = f.indices.length;
			int[] idxs = new int[n + 1];
			Pair[] pairs = new Pair[n];
			Vector3f[] vertices = new Vector3f[n];
			Vector3f[] edgePoints = new Vector3f[n];

			// face point F = average of all points defining the face (center)
			Vector3f fp = new Vector3f();
			for (int i = 0; i < n; i++) {
				Vector3f v = meshToSubdivide.vertices.get(f.indices[i]);
				fp.addLocal(v);
				vertices[i] = v;
			}
			fp.divideLocal(f.indices.length);
			// store face point
			meshToSubdivide.vertices.add(fp);
			// face point index
			idxs[0] = index;
			index++;

			// edges of the face
			for (int i = 0; i < n; i++) {
				Pair p = new Pair(f.indices[i % n], f.indices[(i + 1) % n]);
				pairs[i] = p;
				// map edges to face point
				mapEdgesToFacePoints.put(p, fp);
				// midpoints (edge points)
				edgePoints[i] = GeometryUtil.getMidpoint(vertices[i % n], vertices[(i + 1) % n]);
			}

			// counting edges outgoing from a vertex
			incrementN(pairs);

			for (int i = 0; i < n; i++) {
				// adjacent edge already processed?
				Integer epIndex = mapEdgesToEdgePointIndicies.get(new Pair(pairs[i].b, pairs[i].a));
				if (epIndex == null) {
					meshToSubdivide.vertices.add(edgePoints[i]); // next index + 1
					idxs[i + 1] = index;
					mapEdgesToEdgePointIndicies.put(pairs[i], index);
					index++;
				} else {
					idxs[i + 1] = epIndex;
					mapEdgesToEdgePointIndicies.put(pairs[i], epIndex);
				}
			}

			for (int i = 0; i < n; i++) {
				int vIndex = f.indices[i];
				List<Vector3f> facepoints = mapOriginalVerticesToFacePoints.get(vIndex);
				List<Vector3f> edgePoints2 = mapVerticesToEdgePoints.get(vIndex);

				// create new faces
				Face3D f0 = new Face3D(f.indices[i], idxs[i + 1], idxs[0], idxs[i == 0 ? f.indices.length : i]);
				facesToAdd.add(f0);

				if (facepoints == null) {
					facepoints = new ArrayList<Vector3f>();
					mapOriginalVerticesToFacePoints.put(vIndex, facepoints);
				}

				if (edgePoints2 == null) {
					edgePoints2 = new ArrayList<Vector3f>();
					mapVerticesToEdgePoints.put(vIndex, edgePoints2);
				}

				// map vertices to face point
				facepoints.add(fp);
				// map vertices to edge point
				edgePoints2.add(new Vector3f(edgePoints[i]));
			}

			// remove old face
			facesToRemove.add(f);
		}

		// remove old faces from the mesh
		meshToSubdivide.faces.removeAll(facesToRemove);
		// add all new faces to the mesh
		meshToSubdivide.faces.addAll(facesToAdd);
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}
