package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

public class LoopSubdivisionModifier implements IMeshModifier {

	private static final float ONE_EIGHTS = 1f / 8f;
	private static final float THREE_EIGHTS = 3f / 8f;
	private static final float FIVE_EIGHTS = 5f / 8f;
		
	private int subdivisions;
	private int originalVertexCount;
	private int nextVertexIndex;
	private Face3D currentFace;
	private Mesh3D mesh;
	private List<Face3D> newFaces;
	private List<Vector3f> smoothedVertices;
	private HashMap<Edge3D, Integer> mapEdgeToEdgePointIndex;
	private HashMap<Edge3D, Edge3D> mapEdgeToNextEdge;
	private HashMap<Integer, List<Edge3D>> mapVertexIndexToOutgoingEdges;

	public LoopSubdivisionModifier() {
		this(1);
	}
	
	public LoopSubdivisionModifier(int subdivisions) {
		setSubdivisions(subdivisions);
		newFaces = new ArrayList<Face3D>();
		smoothedVertices = new ArrayList<Vector3f>();
		mapEdgeToEdgePointIndex = new HashMap<Edge3D, Integer>();
		mapVertexIndexToOutgoingEdges = new HashMap<Integer, List<Edge3D>>();
		mapEdgeToNextEdge = new HashMap<Edge3D, Edge3D>();
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		setMesh(mesh);
		for (int i = 0; i < getSubdivisions(); i++) {
			processOneSubdivisionIteration();
		}
		return mesh;
	}
	
	private void processOneSubdivisionIteration() {
		clear();
		setOriginalVertexCountFromMesh();
		setNextVertexIndexFromMesh();
		processFaces();
		removeOldFacesFromMesh();
		addNewFacesToMesh();
		smooth();
	}

	private void smooth() {
		smoothEdgePoints();
		smoothVertexPoints();
	}
	
	private void smoothVertexPoints() {
		calculateSmoothVertexPoints();
		applySmoothedVertices();
	}

	private void calculateSmoothVertexPoints() {
		for (int i = 0; i < getOriginalVertexCount(); i++) {
			Vector3f vertex = mesh.getVertexAt(i);
			List<Edge3D> edges = mapVertexIndexToOutgoingEdges.get(i);
			float n = edges.size();
			float scalar = 3f / (8 * n);
			Vector3f sum = new Vector3f();
			for (Edge3D edge : edges) {
				Vector3f to = mesh.getVertexAt(edge.getToIndex());
				sum.addLocal(to);
			}
			sum.multLocal(scalar);
			smoothedVertices.add(vertex.mult(FIVE_EIGHTS).add(sum));
		}
		applySmoothedVertices();
	}
	
	private void applySmoothedVertices() {
		for (int i = 0; i < getOriginalVertexCount(); i++) {
			mesh.getVertexAt(i).set(smoothedVertices.get(i));
		}
	}

	private void smoothEdgePoints() {
		for (Edge3D edge : getEdges()) {
			Integer edgePointIndex = mapEdgeToEdgePointIndex.get(edge);
			Vector3f edgePoint = getVertexAt(edgePointIndex);
			Vector3f from = getVertexAt(edge.getFromIndex());
			Vector3f to = getVertexAt(edge.getToIndex());
			Vector3f a = from.add(to).mult(THREE_EIGHTS);
			Vector3f b = getVertexAt(mapEdgeToNextEdge.get(edge).getToIndex());
			Vector3f c = getVertexAt(mapEdgeToNextEdge.get(edge.createPair()).getToIndex());
			Vector3f d = b.add(c).mult(ONE_EIGHTS);
			edgePoint.set(a.add(d));
		}
	}

	private void processFaces() {
		for (Face3D face : mesh.getFaces()) {
			if (face.indices.length == 3) {
				currentFace = face;
				processCurrentFace();
			}
		}
	}

	private void processCurrentFace() {
		int[] edgePointIndices = new int[3];
		for (int i = 0; i < 3; i++) {
			int fromIndex = currentFace.indices[i];
			int toIndex = currentFace.indices[(i + 1) % currentFace.indices.length];
			int nextIndex = currentFace.indices[(i + 2) % currentFace.indices.length];
			Edge3D edge = new Edge3D(fromIndex, toIndex);
			Edge3D nextEdge = new Edge3D(toIndex, nextIndex);
			Vector3f edgePoint = createEdgePoint(edge);
			int edgePointIndex = addEdgePoint(edge, edgePoint);
			edgePointIndices[i] = edgePointIndex;
			mapVertexIndexToEdgePointOfOutgoingEdge(fromIndex, edge);
			mapEdgeToNextEdge(edge, nextEdge);
		}
		createNewFaces(edgePointIndices);
	}

	private void createNewFaces(int[] edgePointIndices) {
		int[] indices = createIndicesForCurrentFaceSplit(edgePointIndices);
		addNewFace(new Face3D(indices[0], indices[3], indices[5]));
		addNewFace(new Face3D(indices[3], indices[1], indices[4]));
		addNewFace(new Face3D(indices[5], indices[4], indices[2]));
		addNewFace(new Face3D(indices[5], indices[3], indices[4]));
	}

	private int[] createIndicesForCurrentFaceSplit(int[] edgePointIndices) {
		int[] indices = new int[6];
		indices[0] = currentFace.indices[0];
		indices[1] = currentFace.indices[1];
		indices[2] = currentFace.indices[2];
		indices[3] = edgePointIndices[0];
		indices[4] = edgePointIndices[1];
		indices[5] = edgePointIndices[2];
		return indices;
	}

	private int addEdgePoint(Edge3D edge, Vector3f edgePoint) {
		Edge3D pair = edge.createPair();
		Integer edgePointIndex = mapEdgeToEdgePointIndex.get(pair);
		if (edgePointIndex == null) {
			mesh.add(edgePoint);
			edgePointIndex = nextVertexIndex;
			mapEdgeToEdgePointIndex.put(edge, edgePointIndex);
			nextVertexIndex++;
		}
		return edgePointIndex;
	}

	private Vector3f createEdgePoint(Edge3D edge) {
		Vector3f from = getVertexAt(edge.getFromIndex());
		Vector3f to = getVertexAt(edge.getToIndex());
		Vector3f edgePoint = from.add(to).mult(0.5f);
		return edgePoint;
	}
	
	private void mapEdgeToNextEdge(Edge3D edge, Edge3D nextEdge) {
		mapEdgeToNextEdge.put(edge, nextEdge);
	}

	private void mapVertexIndexToEdgePointOfOutgoingEdge(int vertexIndex, Edge3D edge) {
		List<Edge3D> edgeList = mapVertexIndexToOutgoingEdges.get(vertexIndex);
		if (edgeList == null) {
			edgeList = new ArrayList<Edge3D>();
			mapVertexIndexToOutgoingEdges.put(vertexIndex, edgeList);
		}
		edgeList.add(edge);
	}

	private void removeOldFacesFromMesh() {
		mesh.clearFaces();
	}

	private void addNewFacesToMesh() {
		mesh.addFaces(newFaces);
	}

	private void addNewFace(Face3D face) {
		newFaces.add(face);
	}

	private void clearFaceList() {
		newFaces.clear();
	}

	private void clearVertexList() {
		smoothedVertices.clear();
	}

	private void clearMappings() {
		mapEdgeToEdgePointIndex.clear();
		mapVertexIndexToOutgoingEdges.clear();
		mapEdgeToNextEdge.clear();
	}

	private void clear() {
		clearFaceList();
		clearMappings();
		clearVertexList();
	}

	private Vector3f getVertexAt(int index) {
		return mesh.getVertexAt(index);
	}

	private Set<Edge3D> getEdges() {
		return mapEdgeToEdgePointIndex.keySet();
	}

	private int getOriginalVertexCount() {
		return originalVertexCount;
	}

	private void setOriginalVertexCountFromMesh() {
		originalVertexCount = mesh.getVertexCount();
	}

	private void setNextVertexIndexFromMesh() {
		nextVertexIndex = mesh.getVertexCount();
	}

	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}

}
