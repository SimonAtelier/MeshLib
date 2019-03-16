package mesh.creator.archimedian;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import mesh.Edge3D;
import mesh.Mesh3D;
import util.MeshTestUtil;

public class RhombicosidodecahedronCreatorTest {

	RhombicosidodecahedronCreator creator = new RhombicosidodecahedronCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void createdMeshHasSixtyTwoFaces() {
		Assert.assertEquals(62, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshHasOneHunderedAndTwentyEdges() {
		Collection<Edge3D> edges = mesh.createEdges();
		Assert.assertEquals(120, edges.size());
	}
	
	@Test
	public void createdMeshHasSixtyVertices() {
		Assert.assertEquals(60, mesh.getVertexCount());
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
	public void createdMeshHasThirtyQuadFaces() {
		Assert.assertEquals(30, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
	@Test
	public void createdMeshHasTwelvePentagonalFaces() {
		Assert.assertEquals(12, mesh.getNumberOfFacesWithVertexCountOfN(5));
	}
	
}
