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
			processOneSubdivisionIteration();
		}
		return mesh;
	}
	
	private void processOneSubdivisionIteration() {
		clearMaps();
		clearFaceLists();
		setOriginalVertexCountFromCurrentMesh();
		subdivideMesh();
		smoothEdgePoints();
		smoothOriginalVertices();
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

	private void incrementEdgesOutgoingFromAVertex(Edge3D edge) {
		Integer outgoingEdges = mapVertexIndexToNumberOfOutgoingEdges.get(edge.getFromIndex());
		if (outgoingEdges == null)
			outgoingEdges = 0;
		outgoingEdges++;
		mapVertexIndexToNumberOfOutgoingEdges.put(edge.getFromIndex(), outgoingEdges);
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

	private void smoothEdgePoints() {
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

	private void processFace(Face3D face) {
		int faceIndicesLength = face.indices.length;
		int[] indices = new int[faceIndicesLength + 1];
		Vector3f facePoint = calculateFaceCenter(face);

		indices[0] = addFacePointToMesh(facePoint);
		
		for (int index = 0; index < faceIndicesLength; index++) {
			int vertexIndex = face.indices[index];
			Edge3D edge = createEdge(face, index);
			Vector3f edgePoint = calculateEdgeMidpoint(edge);
			int edgePointIndex= addEdgePoint(edge, edgePoint);
			incrementEdgesOutgoingFromAVertex(edge);
			mapEdgeToEdgePointIndex(edge, edgePointIndex);
			mapVertexToFacePoint(vertexIndex, facePoint);
			mapVertexToEdgePoint(vertexIndex, edgePoint);
			mapEdgeToFacePoint(edge, facePoint);
			indices[index + 1] = edgePointIndex;
		}

		createNewFaces(face, indices);
	}

	private Edge3D createEdge(Face3D face, int index) {
		int fromIndex = face.indices[index];
		int toIndex = face.indices[(index + 1) % face.indices.length];
		Edge3D edge = new Edge3D(fromIndex, toIndex);
		return edge;
	}
	
	private void createNewFaces(Face3D face, int[] indices) {
		for (int index = 0; index < face.indices.length; index++) {
			int index0 = face.indices[index];
			int index1 = indices[index + 1];
			int facePointIndex = indices[0];
			int index3 = indices[index == 0 ? face.indices.length : index];
			Face3D newFace = new Face3D(index0, index1, facePointIndex, index3);
			newFacesToAdd.add(newFace);
		}
	}

	private int addEdgePoint(Edge3D edge, Vector3f edgePoint) {
		Edge3D adjacentEdge = createPair(edge);
		Integer edgePointIndex = mapEdgesToEdgePointIndicies.get(adjacentEdge);

		if (edgePointIndex == null) {
			edgePointIndex = addVertex(edgePoint);
		}

		return edgePointIndex;
	}
	
	private void mapEdgeToEdgePointIndex(Edge3D edge, int edgePointIndex) {
		mapEdgesToEdgePointIndicies.put(edge, edgePointIndex);
	}

	private void mapEdgeToFacePoint(Edge3D edge, Vector3f facePoint) {
		mapEdgesToFacePoints.put(edge, facePoint);
	}

	private void mapVertexToFacePoint(int vertexIndex, Vector3f facePoint) {
		List<Vector3f> facePoints = mapOriginalVerticesToFacePoints.get(vertexIndex);
		if (facePoints == null) {
			facePoints = new ArrayList<Vector3f>();
			mapOriginalVerticesToFacePoints.put(vertexIndex, facePoints);
		}
		facePoints.add(facePoint);
	}

	private void mapVertexToEdgePoint(int vertexIndex, Vector3f edgePoint) {
		List<Vector3f> edgePoints = mapVerticesToEdgePoints.get(vertexIndex);
		if (edgePoints == null) {
			edgePoints = new ArrayList<Vector3f>();
			mapVerticesToEdgePoints.put(vertexIndex, edgePoints);
		}
		edgePoints.add(edgePoint);
	}

	private Vector3f calculateFaceCenter(Face3D face) {
		int faceIndiciesLength = face.indices.length;
		Vector3f facePoint = new Vector3f();
		for (int i = 0; i < faceIndiciesLength; i++) {
			Vector3f vertex = getVertexAt(face.indices[i]);
			facePoint.addLocal(vertex);
		}
		return facePoint.divideLocal(faceIndiciesLength);
	}

	private Vector3f calculateEdgeMidpoint(Edge3D edge) {
		Vector3f fromVertex = getVertexAt(edge.getFromIndex());
		Vector3f toVertex = getVertexAt(edge.getToIndex());
		Vector3f midpoint = GeometryUtil.getMidpoint(fromVertex, toVertex);
		return midpoint;
	}

	private Edge3D createPair(Edge3D edge) {
		return new Edge3D(edge.getToIndex(), edge.getFromIndex());
	}

	private int addFacePointToMesh(Vector3f facePoint) {
		return addVertex(facePoint);
	}

	private int addVertex(Vector3f vertex) {
		int index = nextVertexIndex;
		meshToSubdivide.vertices.add(vertex);
		nextVertexIndex++;
		return index;
	}

	private void removeOldFacesFromMesh() {
		meshToSubdivide.faces.removeAll(oldFacesToRemove);
	}

	private void addNewFacesToMesh() {
		meshToSubdivide.faces.addAll(newFacesToAdd);
	}

	private Vector3f getVertexAt(int index) {
		return meshToSubdivide.getVertexAt(index);
	}

	private void setMeshToSubdivide(Mesh3D meshToSubdivide) {
		this.meshToSubdivide = meshToSubdivide;
	}

	private void setOriginalVertexCountFromCurrentMesh() {
		this.originalVertexCount = meshToSubdivide.getVertexCount();
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}
