package mesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import math.Vector3;

public class Mesh {

	private int faceCount;
	private int vertexCount;
	private List<Vector3> vertices = new ArrayList<Vector3>();
	private List<Face> faces = new ArrayList<Face>();
	private Collection<Edge> edges = new ArrayList<Edge>();
	
	public Collection<Edge> calculateEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		edges.addAll(this.edges);
		return edges;
	}
	
	public void addVertex(float x, float y, float z) {
		vertexCount++;
		vertices.add(new Vector3(x, y, z));
	}
	
	public void addFace(int... indices) {
		if (indices == null)
			throw new IllegalArgumentException();
		
		faceCount++;
		addFace(new Face(indices));
		
		for (int i = 0; i < 4; i++) {
			addEdge(new Edge());
		}
	}
	
	private void addFace(Face face) {
		faces.add(face);
	}
	
	private void addEdge(Edge edge) {
		edges.add(edge);
	}
	
	public Vector3 getVertexAt(int index) {
		if (getVertexCount() > 0)
			return vertices.get(index);
		throw new NoSuchElementException();
	}
	
	public Face getFaceAt(int index) {
		if (getFaceCount() > 0)
			return faces.get(index);
		throw new NoSuchElementException();
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getFaceCount() {
		return faceCount;
	}

}
