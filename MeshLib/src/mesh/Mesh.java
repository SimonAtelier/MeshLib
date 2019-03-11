package mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import math.Vector3;

public class Mesh {

	private int faceCount;
	private int vertexCount;
	private List<Vector3> vertices = new ArrayList<Vector3>();
	private List<Face> faces = new ArrayList<Face>();
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getFaceCount() {
		return faceCount;
	}
	
	public void addVertex(float x, float y, float z) {
		vertexCount++;
		vertices.add(new Vector3(x, y, z));
	}
	
	public void addFace(int... indices) {
		if (indices == null)
			throw new IllegalArgumentException();
		faceCount++;
		faces.add(new Face(indices));
	}
	
	public Vector3 getVertexAt(int index) {
		if (vertexCount > 0)
			return vertices.get(index);
		throw new NoSuchElementException();
	}
	
	public Face getFaceAt(int index) {
		if (faceCount > 0)
			return faces.get(index);
		throw new NoSuchElementException();
	}
	
}
