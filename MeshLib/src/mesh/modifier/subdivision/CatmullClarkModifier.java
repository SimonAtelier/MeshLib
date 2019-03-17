package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class CatmullClarkModifier implements IMeshModifier {

	private int subdivisions;
	private int originalVertexCount;
	private Mesh3D meshToSubdivide;
	
	private HashMap<Edge3D, Vector3f> mapEdgesToFacePoints;
	private HashMap<Edge3D, Integer> mapEdgesToEdgePointIndicies;
	private HashMap<Integer, Integer> mapVertexIndexToNumberOfOutgoingEdges;
	private HashMap<Integer, List<Vector3f>> mapOriginalVerticesToFacePoints;
	private HashMap<Integer, List<Vector3f>> mapVerticesToEdgePoints;

	public CatmullClarkModifier() {
		this(1);
	}

	public CatmullClarkModifier(int subdivisions) {
		this.subdivisions = subdivisions;
		this.mapEdgesToFacePoints = new HashMap<Edge3D, Vector3f>();
		this.mapEdgesToEdgePointIndicies = new HashMap<Edge3D, Integer>();
		this.mapVertexIndexToNumberOfOutgoingEdges = new HashMap<Integer, Integer>();
		this.mapOriginalVerticesToFacePoints = new HashMap<Integer, List<Vector3f>>();
		this.mapVerticesToEdgePoints = new HashMap<Integer, List<Vector3f>>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMeshToSubdivide(mesh);
		for (int i = 0; i < subdivisions; i++) {
			clearMaps();
			setOriginalVertexCount(mesh.getVertexCount());
			subdivideMesh();
			processEdgePoints();
			smoothVertices();
		}
		return mesh;
	}

	private void clearMaps() {
		mapEdgesToFacePoints.clear();
		mapEdgesToEdgePointIndicies.clear();
		mapVertexIndexToNumberOfOutgoingEdges.clear();
		mapOriginalVerticesToFacePoints.clear();
		mapVerticesToEdgePoints.clear();
	}

	private Vector3f getFacePointsAverage(int index) {
		Vector3f average = new Vector3f();
		List<Vector3f> facePoints = mapOriginalVerticesToFacePoints.get(index);
		for (Vector3f facePoint : facePoints) {
			average.addLocal(facePoint);
		}
		return average.mult(1f / (float) facePoints.size());
	}

	private Vector3f getEdgePointAverage(int index) {
		Vector3f average = new Vector3f();
		List<Vector3f> edgePoints = mapVerticesToEdgePoints.get(index);
		for (Vector3f edgePoint : edgePoints) {
			average.addLocal(edgePoint);
		}
		return average.mult(1f / (float) edgePoints.size());
	}

	private void countEdgesOutgoingFromAVertex(Edge3D[] edges) {
		for (Edge3D edge : edges) {
			Integer outgoingEdges = mapVertexIndexToNumberOfOutgoingEdges.get(edge.getFromIndex());
			if (outgoingEdges == null) {
				outgoingEdges = 0;
			}
			outgoingEdges++;
			mapVertexIndexToNumberOfOutgoingEdges.put(edge.getFromIndex(), outgoingEdges);
		}
	}

	private void smoothVertices() {
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

	private void processEdgePoints() {
		for (Edge3D edge : mapEdgesToEdgePointIndicies.keySet()) {
			int index = mapEdgesToEdgePointIndicies.get(edge);
			Vector3f v0 = meshToSubdivide.vertices.get(edge.getFromIndex());
			Vector3f v1 = meshToSubdivide.vertices.get(edge.getToIndex());
			Vector3f fp0 = mapEdgesToFacePoints.get(edge);
			Vector3f fp1 = mapEdgesToFacePoints.get(new Edge3D(edge.getToIndex(), edge.getFromIndex()));
			if (v0 != null && v1 != null && fp0 != null && fp1 != null) {
				Vector3f ep = v0.add(v1).add(fp0).add(fp1).mult(0.25f);
				meshToSubdivide.vertices.get(index).set(ep);
			}
		}
	}

	private void subdivideMesh() {
		int index = meshToSubdivide.getVertexCount();
		ArrayList<Face3D> facesToAdd = new ArrayList<Face3D>();
		
		ArrayList<Face3D> facesToRemove = new ArrayList<Face3D>();
		facesToRemove.addAll(meshToSubdivide.getFaces(0, meshToSubdivide.getFaceCount()));
		
		// for each original face
		for (Face3D face : meshToSubdivide.faces) {
			index = processFace(index, facesToAdd, face);
		}

		// remove old faces from the mesh
		meshToSubdivide.faces.removeAll(facesToRemove);
		// add all new faces to the mesh
		meshToSubdivide.faces.addAll(facesToAdd);
	}

	private int processFace(int index, ArrayList<Face3D> facesToAdd, Face3D face) {
		int n = face.indices.length;
		int[] idxs = new int[n + 1];
		Edge3D[] edges = new Edge3D[n];
		Vector3f[] vertices = new Vector3f[n];
		Vector3f[] edgePoints = new Vector3f[n];

		// face point F = average of all points defining the face (center)
		Vector3f facePoint = new Vector3f();
		
		for (int i = 0; i < n; i++) {
			Vector3f v = meshToSubdivide.vertices.get(face.indices[i]);
			facePoint.addLocal(v);
			vertices[i] = v;
		}
		
		facePoint.divideLocal(face.indices.length);
		// store face point
		meshToSubdivide.vertices.add(facePoint);
		// face point index
		idxs[0] = index;
		index++;

		// edges of the face
		for (int i = 0; i < n; i++) {
			Edge3D p = new Edge3D(face.indices[i % n], face.indices[(i + 1) % n]);
			edges[i] = p;
			// map edges to face point
			mapEdgesToFacePoints.put(p, facePoint);
			// midpoints (edge points)
			edgePoints[i] = GeometryUtil.getMidpoint(vertices[i % n], vertices[(i + 1) % n]);
		}

		countEdgesOutgoingFromAVertex(edges);

		for (int i = 0; i < n; i++) {
			// adjacent edge already processed?
			Edge3D edge = new Edge3D(edges[i].getToIndex(), edges[i].getFromIndex());
			Integer edgePointIndex = mapEdgesToEdgePointIndicies.get(edge);
			if (edgePointIndex == null) {
				meshToSubdivide.vertices.add(edgePoints[i]); // next index + 1
				idxs[i + 1] = index;
				mapEdgesToEdgePointIndicies.put(edges[i], index);
				index++;
			} else {
				idxs[i + 1] = edgePointIndex;
				mapEdgesToEdgePointIndicies.put(edges[i], edgePointIndex);
			}
		}

		for (int i = 0; i < n; i++) {
			int vIndex = face.indices[i];
			List<Vector3f> facepoints = mapOriginalVerticesToFacePoints.get(vIndex);
			List<Vector3f> edgePoints2 = mapVerticesToEdgePoints.get(vIndex);

			// create new faces
			Face3D f0 = new Face3D(face.indices[i], idxs[i + 1], idxs[0], idxs[i == 0 ? face.indices.length : i]);
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
			facepoints.add(facePoint);
			// map vertices to edge point
			edgePoints2.add(new Vector3f(edgePoints[i]));
		}
		return index;
	}
	
	private void setMeshToSubdivide(Mesh3D meshToSubdivide) {
		this.meshToSubdivide = meshToSubdivide;
	}
	
	private void setOriginalVertexCount(int originalVertexCount) {
		this.originalVertexCount = originalVertexCount;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}
