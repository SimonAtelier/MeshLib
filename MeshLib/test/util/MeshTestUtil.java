package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;

import math.Vector3;
import mesh.Edge;
import mesh.Face;
import mesh.Mesh;

public class MeshTestUtil {

	public static void meshFulFillsEulersCharacteristic(Mesh mesh) {
		int faceCount = mesh.getFaceCount();
		int vertexCount = mesh.getVertexCount();
		int edgeCount = mesh.calculateEdges().size();
		Assert.assertEquals(2, faceCount + vertexCount - edgeCount);
	}
	
	public static void eachEdgeIsIncidentToOnlyOneOrTwoFaces(Mesh mesh, int expectedEdgeCountIncludingPairs) {
		HashMap<Edge, List<Face>> map = new HashMap<Edge, List<Face>>();
		
		for (int index = 0; index < mesh.getFaceCount(); index++) {
			Face face = mesh.getFaceAt(index);
			int[] indices = face.getIndices();
			for (int vertexIndex = 0; vertexIndex < indices.length; vertexIndex++) {
				int fromIndex = indices[vertexIndex % indices.length];
				int toIndex = indices[(vertexIndex + 1) % indices.length];
				Edge edge = new Edge(fromIndex, toIndex);
				List<Face> faces;
				if (!map.containsKey(edge)) {
					faces = new ArrayList<Face>();
					map.put(edge, faces);
				} else {
					faces = map.get(edge);
				}
				faces.add(face);
			}
		}
		
		Assert.assertEquals(expectedEdgeCountIncludingPairs, map.size());
		
		for (List<Face> faceList : map.values()) {
			Assert.assertTrue(faceList.size() == 1 || faceList.size() == 2);
		}
	}
	
	public static void allEdgesHaveEqualLength(Mesh mesh) {
		HashSet<Float> lengths = new HashSet<Float>();
		Collection<Edge> edges = mesh.calculateEdges();
		for (Edge edge : edges) {
			Vector3 fromVertex = mesh.getVertexAt(edge.getFromIndex());
			Vector3 toVertex = mesh.getVertexAt(edge.getToIndex());
			float edgeLength = fromVertex.subtract(toVertex).length();
			lengths.add(edgeLength);
		}
		Assert.assertEquals(1, lengths.size());
	}
	
}
