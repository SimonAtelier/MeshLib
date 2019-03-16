package mesh.creator.archimedian;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import mesh.Edge3D;
import mesh.Mesh3D;
import util.MeshTestUtil;

public class CuboctahedronCreatorTest {

	CuboctahedronCreator creator = new CuboctahedronCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void createdMeshHasFourteenFaces() {
		Assert.assertEquals(14, mesh.getFaceCount());
	}

	@Test
	public void createdMeshHasTwentyFourEdges() {
		Collection<Edge3D> edges = mesh.createEdges();
		Assert.assertEquals(24, edges.size());
	}

	@Test
	public void cretedMeshHasTwelveVertices() {
		Assert.assertEquals(12, mesh.getVertexCount());
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
	public void createdMeshHasEightTriangleFaces() {
		Assert.assertEquals(8, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}

}
