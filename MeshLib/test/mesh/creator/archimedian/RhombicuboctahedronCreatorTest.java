package mesh.creator.archimedian;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import mesh.Edge3D;
import mesh.Mesh3D;
import util.MeshTestUtil;

public class RhombicuboctahedronCreatorTest {

	RhombicuboctahedronCreator creator = new RhombicuboctahedronCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void createdMeshHasTwentySixFaces() {
		Assert.assertEquals(26, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshFourtyEightEdges() {
		Collection<Edge3D> edges = mesh.createEdges();
		Assert.assertEquals(48, edges.size());
	}
	
	@Test
	public void createdMeshHasTwentyFourVertices() {
		Assert.assertEquals(24, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshHasEdgesWithEqualLength() {
		MeshTestUtil.allEdgesHaveEqualLength(mesh);
	}
	
	@Test
	public void eachEdgeOfTheCreatedMeshIsIncidentToOnlyOneOrTwoFaces() {
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh);
	}

	@Test
	public void createdMeshFulfillsEulersCharacteristic() {
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void createdMeshHasEightTriangleFaces() {
		Assert.assertEquals(8, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void createdMeshHasEighteenSquareFaces() {
		Assert.assertEquals(18, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
}
