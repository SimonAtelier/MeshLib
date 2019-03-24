package mesh.catalan;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.catalan.RhombicDodecahedronCreator;
import mesh.creator.primitives.CubeCreator;
import util.MeshTestUtil;

public class RhombicDodecahedronCreatorTest {

	RhombicDodecahedronCreator creator = new RhombicDodecahedronCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void creatorImplementsMeshCreatorInterface() {
		Assert.assertTrue(creator instanceof IMeshCreator);
	}
	
	@Test
	public void createdMeshHasTwelveFaces() {
		Assert.assertEquals(12, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshHasFourteenVertices() {
		Assert.assertEquals(14, mesh.getVertexCount());
	}
	
	@Test
	public void cratedMeshContainsTheVerticesOfTheCube() {
		CubeCreator creator = new CubeCreator();
		Mesh3D mesh = creator.create();
		for (Vector3f v : mesh.getVertices()) {
			Assert.assertTrue(this.mesh.getVertices().contains(v));
		}
	}
	
	@Test
	public void createdMeshHasTwentyFourEdges() {
		Assert.assertEquals(24, mesh.createEdges().size());
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
	public void createdMeshHasTwelveFacesQuadFaces() {
		Assert.assertEquals(12, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
}
