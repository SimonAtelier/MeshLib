package mesh.catalan;

import org.junit.Assert;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.catalan.TriakisTetrahedronCreator;
import util.MeshTestUtil;

public class TriakisTetrahedronCreatorTest {
	
	TriakisTetrahedronCreator creator = new TriakisTetrahedronCreator();
	Mesh3D mesh = creator.create();

	@Test
	public void creatorImplementsMeshCreatorInterface() {
		Assert.assertTrue(creator instanceof IMeshCreator);
	}
	
	@Test
	public void creatorCreatedNewMeshInstanceWithEachCallOfCreate() {
		TriakisTetrahedronCreator creator = new TriakisTetrahedronCreator();
		Mesh3D previous = creator.create();
		for (int i = 0; i < 20; i++) {
			Mesh3D mesh = creator.create();
			Assert.assertTrue(mesh != previous);
			previous = mesh;
		}
	}
	
	@Test
	public void createdMeshHasTwelveFaces() {
		Assert.assertEquals(12, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshHasTwelveTriangularFaces() {
		Assert.assertEquals(12, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void createdMeshHasEighteenEdges() {
		Assert.assertEquals(18, mesh.createEdges().size());
	}
	
	@Test
	public void createdMeshHasEightVertices() {
		Assert.assertEquals(8, mesh.getVertexCount());
	}
	
	@Test
	public void eachEdgeOfTheCreatedMeshIsIncidentToOnlyOneOrTwoFaces() {
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh);
	}
	
	@Test
	public void createdMeshFulFillsEulersCharacteristic() {
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void createdMeshhHasUniqueVertices() {
		MeshTestUtil.meshHasUniqueVertices(mesh);
	}
	
}
