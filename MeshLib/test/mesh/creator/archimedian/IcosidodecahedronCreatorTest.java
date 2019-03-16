package mesh.creator.archimedian;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import mesh.Edge3D;
import mesh.Mesh3D;
import util.MeshTestUtil;

public class IcosidodecahedronCreatorTest {
	
	IcosidodecahedronCreator creator = new IcosidodecahedronCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void createdMeshHasThrityTwoFaces() {
		Assert.assertEquals(32, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshHasSixtyEdges() {
		Collection<Edge3D> edges = mesh.createEdges();
		Assert.assertEquals(60, edges.size());
	}
	
	@Test
	public void createdMeshHasThirtyVertices() {
		Assert.assertEquals(30, mesh.getVertexCount());
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
	public void createdMeshHasTwentyTriangleFaces() {
		Assert.assertEquals(20, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void createdMeshHasTwelvePentagonalFaces() {
		Assert.assertEquals(12, mesh.getNumberOfFacesWithVertexCountOfN(5));
	}
	
}
