package mesh.creator.primitives;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3;
import mesh.Face;
import mesh.Mesh;
import mesh.creator.IMeshCreator;
import util.MeshTestUtil;

public class CubeCreatorTest {

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
	public void theAbsoluteValueOfEachVertexComponentIsOne() {
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
	
}
