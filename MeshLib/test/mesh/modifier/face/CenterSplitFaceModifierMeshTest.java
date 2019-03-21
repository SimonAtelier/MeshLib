package mesh.modifier.face;

import org.junit.Assert;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import util.MeshTestUtil;

public class CenterSplitFaceModifierMeshTest {

	Mesh3D mesh = new CubeCreator().create();
	CenterSplitFaceModifier modifier = new CenterSplitFaceModifier();
	
	@Test
	public void centerSplitCubeOneTimeCreatesMeshWithTwentyFourTriangularFaces() {
		modifier.modify(mesh);
		Assert.assertEquals(24, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void centerSplitCubeOneTimeCreatesMeshWithFourteenVertices() {
		modifier.modify(mesh);
		Assert.assertEquals(14, mesh.getVertexCount());
	}
	
	@Test
	public void centerSlitCubeTwoTimesCreatedMeshWithSeventyTwoTriangularFaces() {
		modifier.modify(mesh);
		modifier.modify(mesh);
		Assert.assertEquals(72, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void centerSplitCubeOneTimeCreatesMeshWithThirtyEightVertices() {
		modifier.modify(mesh);
		modifier.modify(mesh);
		Assert.assertEquals(38, mesh.getVertexCount());
	}
	
	@Test
	public void centerSplittedCubeFulFillsEulersCharacteristic() {
		modifier.modify(mesh);
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void eachEdgeOfTheCenterSplittedCubeIsIncidentToOnlyOneOrTwoFaces() {
		modifier.modify(mesh);
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh);
	}
	
	@Test
	public void centerSplittedCubeHasUniqueVertices() {
		modifier.modify(mesh);
		MeshTestUtil.meshHasUniqueVertices(mesh);
	}
	
}
