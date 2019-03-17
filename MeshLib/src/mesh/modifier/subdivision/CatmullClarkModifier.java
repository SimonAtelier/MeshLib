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

	private int nextVertexIndex;

	private int subdivisions;
	private int originalVertexCount;
	private Mesh3D meshToSubdivide;

	private HashMap<Edge3D, Vector3f> mapEdgesToFacePoints;
	private HashMap<Edge3D, Integer> mapEdgesToEdgePointIndicies;
	private HashMap<Integer, Integer> mapVertexIndexToNumberOfOutgoingEdges;
	private HashMap<Integer, List<Vector3f>> mapOriginalVerticesToFacePoints;
	private HashMap<Integer, List<Vector3f>> mapVerticesToEdgePoints;

	private ArrayList<Face3D> newFacesToAdd;
	private ArrayList<Face3D> oldFacesToRemove;

	public CatmullClarkModifier() {
		this(1);
	}

	public CatmullClarkModifier(int subdivisions) {
		this.subdivisions = subdivisions;
		mapEdgesToFacePoints = new HashMap<Edge3D, Vector3f>();
		mapEdgesToEdgePointIndicies = new HashMap<Edge3D, Integer>();
		mapVertexIndexToNumberOfOutgoingEdges = new HashMap<Integer, Integer>();
		mapOriginalVerticesToFacePoints = new HashMap<Integer, List<Vector3f>>();
		mapVerticesToEdgePoints = new HashMap<Integer, List<Vector3f>>();
		newFacesToAdd = new ArrayList<Face3D>();
		oldFacesToRemove = new ArrayList<Face3D>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMeshToSubdivide(mesh);
		for (int i = 0; i < subdivisions; i++) {
			clearMaps();
			clearFaceLists();
			setOriginalVertexCount(mesh.getVertexCount());
			subdivideMesh();
			processEdgePoints();
			smoothOriginalVertices();
		}
		return mesh;
	}

	private void subdivideMesh() {
		nextVertexIndex = meshToSubdivide.getVertexCount();
		oldFacesToRemove.addAll(meshToSubdivide.getFaces(0, meshToSubdivide.getFaceCount()));

		for (Face3D face : meshToSubdivide.faces) {
			processFace(face);
		}

		removeOldFacesFromMesh();
		addNewFacesToMesh();
	}

	private void removeOldFacesFromMesh() {
		meshToSubdivide.faces.removeAll(oldFacesToRemove);
	}

	private void addNewFacesToMesh() {
		meshToSubdivide.faces.addAll(newFacesToAdd);
	}

	private void clearMaps() {
		mapEdgesToFacePoints.clear();
		mapEdgesToEdgePointIndicies.clear();
		mapVertexIndexToNumberOfOutgoingEdges.clear();
		mapOriginalVerticesToFacePoints.clear();
		mapVerticesToEdgePoints.clear();
	}

	private void clearFaceLists() {
		newFacesToAdd.clear();
		oldFacesToRemove.clear();
	}

	private Vector3f calculateWeightedFacePointsAverage(int index) {
		Vector3f average = new Vector3f();
		List<Vector3f> facePoints = mapOriginalVerticesToFacePoints.get(index);
		for (Vector3f facePoint : facePoints) {
			average.addLocal(facePoint);
		}
		return average.mult(1f / (float) facePoints.size());
	}

	private Vector3f calculateWeightedEdgePointsAverage(int index) {
		Vector3f average = new Vector3f();
		List<Vector3f> edgePoints = mapVerticesToEdgePoints.get(index);
		for (Vector3f edgePoint : edgePoints) {
			average.addLocal(edgePoint);
		}
		return average.mult(1f / (float) edgePoints.size());
	}

	private void incrementEdgesOutgoingFromAVertex(Edge3D[] edges) {
		for (Edge3D edge : edges) {
			Integer outgoingEdges = mapVertexIndexToNumberOfOutgoingEdges.get(edge.getFromIndex());
			if (outgoingEdges == null)
				outgoingEdges = 0;
			outgoingEdges++;
			mapVertexIndexToNumberOfOutgoingEdges.put(edge.getFromIndex(), outgoingEdges);
		}
	}

	private void smoothOriginalVertices() {
		for (int i = 0; i < originalVertexCount; i++) {
			float n = (float) mapVertexIndexToNumberOfOutgoingEdges.get(i);
			Vector3f oldVertexValue = meshToSubdivide.vertices.get(i);
			Vector3f facePointsAverage = calculateWeightedFacePointsAverage(i);
			Vector3f edgePointsAverage = calculateWeightedEdgePointsAverage(i);
			Vector3f newVertexValue = facePointsAverage.add(edgePointsAverage.mult(2f).add(oldVertexValue.mult(n - 3)));
			newVertexValue.divideLocal(n);
			oldVertexValue.set(newVertexValue);
		}
	}

	private void processEdgePoints() {
		for (Edge3D edge : mapEdgesToEdgePointIndicies.keySet()) {
			int index = mapEdgesToEdgePointIndicies.get(edge);
			Edge3D pair = new Edge3D(edge.getToIndex(), edge.getFromIndex());
			Vector3f fromIndex = getVertexAt(edge.getFromIndex());
			Vector3f toIndex = getVertexAt(edge.getToIndex());
			Vector3f fp0 = mapEdgesToFacePoints.get(edge);
			Vector3f fp1 = mapEdgesToFacePoints.get(pair);
			if (fromIndex != null && toIndex != null && fp0 != null && fp1 != null) {
				Vector3f edgePoint = fromIndex.add(toIndex).add(fp0).add(fp1).mult(0.25f);
				getVertexAt(index).set(edgePoint);
			}
		}
	}
	
	private Vector3f calculateFacePoint(Face3D face) {
		Vector3f facePoint = new Vector3f();
		for (int i = 0; i < face.indices.length; i++) {
			Vector3f v = getVertexAt(face.indices[i]);
			facePoint.addLocal(v);
		}
		facePoint.divideLocal(face.indices.length);
		return facePoint;
	}

	private void processFace(Face3D face) {
		int faceIndexCount = face.indices.length;
		int[] idxs = new int[faceIndexCount + 1];
		Edge3D[] edges = new Edge3D[faceIndexCount];
		Vector3f[] edgePoints;

		Vector3f facePoint = calculateFacePoint(face);
		// store face point
		meshToSubdivide.vertices.add(facePoint);
		
		// face point index
		idxs[0] = nextVertexIndex;
		nextVertexIndex++;
		
		edgePoints = progessEdges(face, edges, facePoint);

		incrementEdgesOutgoingFromAVertex(edges);

		for (int i = 0; i < faceIndexCount; i++) {
			// adjacent edge already processed?
			Edge3D edge = new Edge3D(edges[i].getToIndex(), edges[i].getFromIndex());
			Integer edgePointIndex = mapEdgesToEdgePointIndicies.get(edge);
			if (edgePointIndex == null) {
				meshToSubdivide.vertices.add(edgePoints[i]); // next index + 1
				idxs[i + 1] = nextVertexIndex;
				mapEdgesToEdgePointIndicies.put(edges[i], nextVertexIndex);
				nextVertexIndex++;
			} else {
				idxs[i + 1] = edgePointIndex;
				mapEdgesToEdgePointIndicies.put(edges[i], edgePointIndex);
			}
		}

		for (int i = 0; i < faceIndexCount; i++) {
			int vIndex = face.indices[i];
			List<Vector3f> facepoints = mapOriginalVerticesToFacePoints.get(vIndex);
			List<Vector3f> edgePoints2 = mapVerticesToEdgePoints.get(vIndex);

			// create new faces
			Face3D f0 = new Face3D(face.indices[i], idxs[i + 1], idxs[0], idxs[i == 0 ? face.indices.length : i]);
			newFacesToAdd.add(f0);

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
	}

	private Vector3f[] progessEdges(Face3D face, Edge3D[] edges, Vector3f facePoint) {
		int faceIndexCount = face.indices.length;
		Vector3f[] edgePoints = new Vector3f[faceIndexCount];
		for (int i = 0; i < faceIndexCount; i++) {
			int fromIndex = face.indices[i % faceIndexCount];
			int toIndex = face.indices[(i + 1) % faceIndexCount];
			Edge3D edge = new Edge3D(fromIndex, toIndex);
			edges[i] = edge;
			// map edges to face point
			mapEdgesToFacePoints.put(edge, facePoint);
			edgePoints[i] = calculateMidpoint(edge);
		}
		return edgePoints;
	}
	
	private Vector3f calculateMidpoint(Edge3D edge) {
		Vector3f fromVertex = getVertexAt(edge.getFromIndex());
		Vector3f toVertex = getVertexAt(edge.getToIndex());
		Vector3f midpoint = GeometryUtil.getMidpoint(fromVertex, toVertex);
		return midpoint;
	}

	private Vector3f getVertexAt(int index) {
		return meshToSubdivide.getVertexAt(index);
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
