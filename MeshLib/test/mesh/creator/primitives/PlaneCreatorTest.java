package mesh.creator.primitives;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3;
import mesh.Edge;
import mesh.Mesh;
import util.MeshTestUtil;

public class PlaneCreatorTest {

	@Test
	public void createdMeshIsNotNull() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		Assert.assertNotNull(mesh);
	}
	
	@Test
	public void createdMeshHasFourVertices() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		Assert.assertEquals(4, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshHasOneFace() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		Assert.assertEquals(1, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshHasFourEdges() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		Assert.assertEquals(4, mesh.calculateEdges().size());
	}
	
	@Test
	public void eachEdgeOfTheCreatedMeshIsIncidentToOnlyOneOrTwoFaces() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh, 4);
	}
	
	@Test
	public void createdMeshHasFourDifferentVertices() {
		HashSet<Vector3> vertices = new HashSet<Vector3>();
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Vector3 vertex = mesh.getVertexAt(i);
			vertices.add(vertex);
		}
		Assert.assertEquals(vertices.size(), 4);
	}
	
	@Test
	public void eachVertexHasAnZeroYComponent() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Vector3 vertex = mesh.getVertexAt(i);
			Assert.assertEquals(0, vertex.getY(), 0);
		}
	}
	
	@Test
	public void eachVertexOfTheCreatedMeshHasAnXComponentWhereTheAbsoluteValueIsOne() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Vector3 vertex = mesh.getVertexAt(i);
			Assert.assertEquals(1, Math.abs(vertex.getX()), 0);
		}
	}
	
	@Test
	public void eachVertexOfTheCreatedMeshHasAnZComponentWhereTheAbsoluteValueIsOne() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Vector3 vertex = mesh.getVertexAt(i);
			Assert.assertEquals(1, Math.abs(vertex.getZ()), 0);
		}
	}
	
	@Test
	public void allEdgesOfTheCreatedMeshHaveEqualLength() {
		PlaneCreator creator = new PlaneCreator();
		Mesh mesh = creator.create();
		MeshTestUtil.allEdgesHaveEqualLength(mesh);
	}
	
	@Test
	public void creatorCreatesNewMeshWithEveryCreateCall() {
		int random = (int) (Math.random() * 30);
		PlaneCreator creator = new PlaneCreator();
		Mesh last = creator.create();
		for (int i = 0; i < random; i++) {
			Mesh mesh = creator.create();
			Assert.assertTrue(last != mesh);
			last = mesh;
		}
	}
	
	@Test
	public void getRadiusReturnsOneByDefault() {
		PlaneCreator creator = new PlaneCreator();
		Assert.assertEquals(1, creator.getRadius(), 0);
	}
	
	@Test
	public void ifSetRadiusToRandomValueThenGetRadiusReturnsThisValue() {
		float random = (float) (Math.random() * Integer.MAX_VALUE);
		PlaneCreator creator = new PlaneCreator();
		creator.setRadius(random);
		Assert.assertEquals(random, creator.getRadius(), 0);
	}
	
	@Test
	public void theEdgeLengthIsTwiceAsLargeAsTheSettedRadius() {
		float random = (float) (Math.random() * Integer.MAX_VALUE);
		PlaneCreator creator = new PlaneCreator();
		creator.setRadius(random);
		Mesh mesh = creator.create();
		Collection<Edge> edges = mesh.calculateEdges();
		for (Edge edge : edges) {
			Vector3 from = mesh.getVertexAt(edge.getFromIndex());
			Vector3 to = mesh.getVertexAt(edge.getToIndex());
			float length = from.subtract(to).length();
			Assert.assertEquals(random * 2, length, 0);
		}
	}

}
