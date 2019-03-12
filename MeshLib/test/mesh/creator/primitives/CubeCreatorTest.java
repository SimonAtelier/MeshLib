package mesh.creator.primitives;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3;
import mesh.Edge;
import mesh.Face;
import mesh.Mesh;
import mesh.creator.IMeshCreator;
import util.MeshTestUtil;

public class CubeCreatorTest {
	
	@Test
	public void everyEdgeOfTheCreatedMeshIsTwiceAsLargeAsSettedTheRadius() {
		float random = (float) (Math.random() * Integer.MAX_VALUE);
		CubeCreator creator = new CubeCreator();
		creator.setRadius(random);
		Mesh mesh = creator.create();
		Collection<Edge> edges = mesh.calculateEdges();
		for (Edge edge : edges) {
			Vector3 from = mesh.getVertexAt(edge.getFromIndex());
			Vector3 to = mesh.getVertexAt(edge.getToIndex());
			float edgeLength = from.subtract(to).length();
			Assert.assertEquals(2 * random, edgeLength, 0);
		}
	}
	
	@Test
	public void getRadiusReturnsValueAfterSetValue() {
		float random = (float) (Math.random() * Integer.MAX_VALUE);
		CubeCreator creator = new CubeCreator();
		creator.setRadius(random);
		Assert.assertEquals(random, creator.getRadius(), 0);
	}
	
	@Test
	public void getRadiusReturnsOneByDefault() {
		CubeCreator creator = new CubeCreator();
		Assert.assertEquals(1, creator.getRadius(), 0);
	}

	@Test
	public void creatorImplementsMeshCreatorInterface() {
		CubeCreator creator = new CubeCreator();
		Assert.assertTrue(creator instanceof IMeshCreator);
	}

	@Test
	public void createdMeshIsNotNull() {
		CubeCreator creator = new CubeCreator();
		Assert.assertNotNull(creator.create());
	}
	
	@Test
	public void createdMeshHasEightVertices() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		Assert.assertEquals(8, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshHasSixFaces() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		Assert.assertEquals(6, mesh.getFaceCount());
	}
	
	@Test
	public void everyFaceOfTheCreatedMeshHasFourIndices() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		for (int i = 0; i < mesh.getFaceCount(); i++) {
			Face face = mesh.getFaceAt(i);
			Assert.assertEquals(4, face.getIndices().length);
		}
	}
	
	@Test
	public void createdMeshHasTwelveEdges() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		Assert.assertEquals(12, mesh.calculateEdges().size());
	}
	
	@Test
	public void eachFaceOfTheCreatedMeshHasDifferentIndices() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		for (int i = 0; i < mesh.getFaceCount(); i++) {
			Face face = mesh.getFaceAt(i);
			int lastIndex = -1;
			for (int j = 0; j < face.getIndices().length; j++) {
				int index = face.getIndices()[j];
				Assert.assertTrue(lastIndex != index);
				lastIndex = index;
			}
		}
	}
	
	@Test
	public void theAbsoluteValueOfEachVertexComponentIsOneByDeafult() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		for (int index = 0; index < mesh.getVertexCount(); index++) {
			Vector3 vertex = mesh.getVertexAt(index);
			Assert.assertEquals(1, Math.abs(vertex.getX()), 0);
			Assert.assertEquals(1, Math.abs(vertex.getY()), 0);
			Assert.assertEquals(1, Math.abs(vertex.getZ()), 0);
		}
	}
	
	@Test
	public void theCreatedMeshHasEightDifferentVertices() {
		HashSet<Vector3> vertices = new HashSet<Vector3>();
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		for (int index = 0; index < mesh.getVertexCount(); index++) {
			Vector3 vertex = mesh.getVertexAt(index);
			vertices.add(vertex);
		}
		Assert.assertEquals(8, vertices.size());
	}
	
	@Test
	public void createdMeshFulFillsEulerCharacteristic() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void eachEdgeOfTheCreatedMeshIsIncidentToOnlyOneOrTwoFaces() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh, 24);
	}
	
	@Test
	public void allEdgesOfTheCreatedMeshHaveEqualLength() {
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		MeshTestUtil.allEdgesHaveEqualLength(mesh);
	}
	
	@Test
	public void creatorCreatesNewMeshAtEveryCreateCall() {
		int random = (int) (Math.random() * 50);
		CubeCreator creator = new CubeCreator();
		Mesh mesh = creator.create();
		Mesh meshOther;
		for (int i = 0; i < random; i++) {
			meshOther = creator.create();
			Assert.assertTrue(mesh != meshOther);
		}
	}
	
}
