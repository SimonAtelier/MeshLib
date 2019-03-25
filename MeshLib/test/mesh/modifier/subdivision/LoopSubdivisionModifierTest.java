package mesh.modifier.subdivision;

import org.junit.Assert;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.platonic.IcosahedronCreator;
import util.MeshTestUtil;

public class LoopSubdivisionModifierTest {

	Mesh3D mesh = new IcosahedronCreator().create();
	LoopSubdivisionModifier modifier = new LoopSubdivisionModifier();
	
	@Test
	public void subdivideIcosahedronOneTimeCreatesMeshWithEightyTriangularFaces() {
		modifier.modify(mesh);
		Assert.assertEquals(80, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void subdivideIcosahedronOneTimeCreatesMeshWithExpectedVertexCount() {
		int edgeCount = mesh.createEdges().size();
		int oldVertexCount = mesh.getVertexCount();
		modifier.modify(mesh);
		Assert.assertEquals(edgeCount + oldVertexCount, mesh.getVertexCount());
	}

	@Test
	public void subdivideIcosahedronTwoTimesCreatesMeshWithThreeHundredAndTwentyTriangularFaces() {
		modifier.modify(mesh);
		modifier.modify(mesh);
		Assert.assertEquals(320, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void subdivideIcosahedronTwoTimesCreatesMeshWithExpectedVertexCount() {
		modifier.modify(mesh);
		int edgeCount = mesh.createEdges().size();
		int oldVertexCount = mesh.getVertexCount();
		modifier.modify(mesh);
		Assert.assertEquals(edgeCount + oldVertexCount, mesh.getVertexCount());
	}
	
	@Test
	public void eachEdgeOfTheMofifiedMeshIsIncidentToOnlyOneOrTwoFaces() {
		modifier.modify(mesh);
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh);
	}
	
	@Test
	public void modifiedMeshFulFillsEulersCharacteristic() {
		modifier.modify(mesh);
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void modifiedMeshHasUniqueVertices() {
		modifier.modify(mesh);
		MeshTestUtil.meshHasUniqueVertices(mesh);
	}
	
}
