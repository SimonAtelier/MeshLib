package mesh.modifier.face;

import org.junit.Assert;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.wip.FaceSelection;
import util.MeshTestUtil;

public class CenterSplitFaceModifierTest {

	Mesh3D mesh = new CubeCreator().create();
	FaceSelection selection = new FaceSelection(mesh);
	CenterSplitFaceModifier modifier = new CenterSplitFaceModifier();
	
	@Test
	public void centerSplitCubeOneTimeCreatesMeshWithTwentyFourTriangularFaces() {
		selection.selectAll();
		modifier.modify(selection);
		Assert.assertEquals(24, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void centerSplitCubeOneTimeCreatesMeshWithFourteenVertices() {
		selection.selectAll();
		new CenterSplitFaceModifier().modify(selection);
		Assert.assertEquals(14, mesh.getVertexCount());
	}
	
	@Test
	public void centerSlitCubeTwoTimesCreatedMeshWithSeventyTwoTriangularFaces() {
		selection.selectAll();
		modifier.modify(selection);
		selection.clear();
		selection.selectAll();
		modifier.modify(selection);
		Assert.assertEquals(72, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void centerSplitCubeOneTimeCreatesMeshWithThirtyEightVertices() {
		selection.selectAll();
		modifier.modify(selection);
		selection.clear();
		selection.selectAll();
		modifier.modify(selection);
		Assert.assertEquals(38, mesh.getVertexCount());
	}
	
	@Test
	public void centerSplittedCubeFulFillsEulersCharacteristic() {
		selection.selectAll();
		modifier.modify(selection);
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
}
