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
	
	private void processFace(Face3D face) {
		int[] indices = new int[face.indices.length + 1];
		Edge3D[] edges = new Edge3D[face.indices.length];
		
		Vector3f facePoint = calculateFaceCenter(face);
		
		indices[0] = nextVertexIndex;
		addFacePointToMesh(facePoint);

		
		Vector3f[] edgePoints = progessEdges(face, edges);
		
		
		incrementEdgesOutgoingFromAVertex(edges);
		
		for (int i = 0; i < face.indices.length; i++) {
			int vertexIndex = face.indices[i];
			mapVertexToFacePoint(vertexIndex, facePoint);
			mapVertexToEdgePoint(vertexIndex, edgePoints[i]);
			mapEdgeToFacePoint(edges[i], facePoint);
			indices[i + 1] = mapEdgeToEdgePointIndex(edges[i], edgePoints[i]);
		}

		createNewFaces(face, indices);
	}
	
	private Vector3f[] progessEdges(Face3D face, Edge3D[] edges) {
		int faceIndiciesLength = face.indices.length;
		Vector3f[] edgePoints = new Vector3f[faceIndiciesLength];
		for (int i = 0; i < faceIndiciesLength; i++) {
			int fromIndex = face.indices[i % faceIndiciesLength];
			int toIndex = face.indices[(i + 1) % faceIndiciesLength];
			Edge3D edge = new Edge3D(fromIndex, toIndex);
			edges[i] = edge;
			edgePoints[i] = calculateEdgeMidpoint(edge);
		}
		return edgePoints;
	}
	
	private boolean isAdjacentEdgeIsAlreadyProcessed(Edge3D edge) {
		Edge3D pair = createPair(edge);
		return mapEdgesToEdgePointIndicies.containsKey(pair);
	}

	private int mapEdgeToEdgePointIndex(Edge3D edge, Vector3f edgePoint) {
		Edge3D adjacentEdge = createPair(edge);
		Integer edgePointIndex = mapEdgesToEdgePointIndicies.get(adjacentEdge);
		
		if (edgePointIndex == null) {
			edgePointIndex = nextVertexIndex;
			addEdgePointPointToMesh(edgePoint);
		}
		
		mapEdgesToEdgePointIndicies.put(edge, edgePointIndex);
			
		return edgePointIndex;
	}
	
	private void createNewFaces(Face3D face, int[] idxs) {
		for (int index = 0; index < face.indices.length; index++) {
			int index0 = face.indices[index];
			int index1 = idxs[index + 1];
			int index2 = idxs[0];
			int index3 = idxs[index == 0 ? face.indices.length : index];
			Face3D newFace = new Face3D(index0, index1, index2, index3);
			newFacesToAdd.add(newFace);
		}
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
	
	private void addFacePointToMesh(Vector3f facePoint) {
		addVertex(facePoint);
	}
	
	private void addEdgePointPointToMesh(Vector3f edgePoint) {
		addVertex(edgePoint);
	}
	
	private void addVertex(Vector3f vertex) {
		meshToSubdivide.vertices.add(vertex);
		nextVertexIndex++;
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
