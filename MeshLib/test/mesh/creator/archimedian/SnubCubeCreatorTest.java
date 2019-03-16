package mesh.creator.archimedian;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import mesh.Edge3D;
import mesh.Mesh3D;
import util.MeshTestUtil;

public class SnubCubeCreatorTest {

	SnubCubeCreator creator = new SnubCubeCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void createdMeshHasThirtyEightFaces() {
		Assert.assertEquals(38, mesh.getFaceCount());
	}

	@Test
	public void createdMeshHasSixtyEdges() {
		Collection<Edge3D> edges = mesh.createEdges();
		Assert.assertEquals(60, edges.size());
	}

	@Test
	public void cretedMeshHasTwentyFourVertices() {
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
	public void createdMeshHasSixQuadFaces() {
		Assert.assertEquals(6, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}

	@Test
	public void createdMeshHasThirtyTwoTriangleFaces() {
		Assert.assertEquals(32, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
}
