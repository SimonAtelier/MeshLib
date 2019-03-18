package mesh.creator.platonic;

import org.junit.Assert;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import util.MeshTestUtil;

public class IcosahedronCreatorTest {

	IcosahedronCreator creator = new IcosahedronCreator();
	Mesh3D mesh = creator.create();

	@Test
	public void creatorImplementsMeshCreatorInterface() {
		Assert.assertTrue(creator instanceof IMeshCreator);
	}
	
	@Test
	public void creatorCreatedNewMeshInstanceWithEachCallOfCreate() {
		IcosahedronCreator creator = new IcosahedronCreator();
		Mesh3D previous = creator.create();
		for (int i = 0; i < 20; i++) {
			Mesh3D mesh = creator.create();
			Assert.assertTrue(mesh != previous);
			previous = mesh;
		}
	}
	
	@Test
	public void createdMeshHasTwentyFaces() {
		Assert.assertEquals(20, mesh.getFaceCount());
	}
	
	@Test
	public void createdMesHasTwelveVertices() {
		Assert.assertEquals(12, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshHasThirtyEdges() {
		Assert.assertEquals(30, mesh.createEdges().size());		
	}
	
	@Test
	public void createdMeshFulFillsEulersCharacteristic() {
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void eachEdgeOfTheCreatedMeshIsIncidentToOnlyOneOrTwoFaces() {
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh);
	}
	
	@Test
	public void allEdgesOfTheCreatedMeshHaveEqualLength() {
		MeshTestUtil.allEdgesHaveEqualLength(mesh);
	}

}
