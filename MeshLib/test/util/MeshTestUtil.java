package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Face3D;
import mesh.Mesh3D;

public class MeshTestUtil {
	
	public static void eachEdgeIsIncidentToOnlyOneOrTwoFaces(Mesh3D mesh) {
		HashMap<Edge3D, List<Face3D>> map = new HashMap<Edge3D, List<Face3D>>();
		
		for (Face3D face : mesh.faces) {
			for (int vertexIndex = 0; vertexIndex <= face.indices.length; vertexIndex++) {
				int fromIndex = face.indices[vertexIndex % face.indices.length];
				int toIndex = face.indices[(vertexIndex + 1) % face.indices.length];
				Edge3D edge = new Edge3D(fromIndex, toIndex);
				List<Face3D> faces;
				if (!map.containsKey(edge)) {
					faces = new ArrayList<Face3D>();
					map.put(edge, faces);
				} else {
					faces = map.get(edge);
				}
				faces.add(face);
			}
		}
		
		for (List<Face3D> faceList : map.values()) {
			Assert.assertTrue(faceList.size() == 1 || faceList.size() == 2);
		}
	}
			
	public static void allEdgesHaveEqualLength(Mesh3D mesh) {
		List<Float> lengths = new ArrayList<Float>();
		Collection<Edge3D> edges = mesh.createEdges();
		
		for (Edge3D edge : edges) {
			Vector3f fromVertex = mesh.getVertexAt(edge.getFromIndex());
			Vector3f toVertex = mesh.getVertexAt(edge.getToIndex());
			float edgeLength = fromVertex.subtract(toVertex).length();
			lengths.add(edgeLength);
		}
		
		float length = lengths.get(0);
		for (int i = 0; i < lengths.size(); i++) {
			float next = lengths.get(i);
			Assert.assertEquals(length, next, 0.000001f);
		}
		
	}
	
	public static void meshFulFillsEulersCharacteristic(Mesh3D mesh) {
		int faceCount = mesh.getFaceCount();
		int vertexCount = mesh.getVertexCount();
		int edgeCount = mesh.createEdges().size();
		Assert.assertEquals(2, faceCount + vertexCount - edgeCount);
	}

}
