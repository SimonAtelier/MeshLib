package mesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import math.Vector3;

public class Mesh {

	private int faceCount;
	private int vertexCount;
	private List<Vector3> vertices = new ArrayList<Vector3>();
	private List<Face> faces = new ArrayList<Face>();
	
	public Collection<Edge> calculateEdges() {
		HashSet<Edge> edges = new HashSet<Edge>();
		for (Face face : faces) {
			int[] indices = face.getIndices();
			for (int i = 0; i < indices.length; i++) {
				int fromIndex = indices[i % indices.length];
				int toIndex = indices[(i + 1) % indices.length];
				Edge edge = new Edge(fromIndex, toIndex);
				Edge pair = new Edge(toIndex, fromIndex);
				if (!edges.contains(pair))
					edges.add(edge);
			}
		}
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
	}
	
	private void addFace(Face face) {
		faces.add(face);
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
