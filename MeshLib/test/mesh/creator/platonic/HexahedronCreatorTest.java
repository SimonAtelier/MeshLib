package mesh.creator.platonic;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3f;
import mesh.Edge3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import util.MeshTestUtil;

public class HexahedronCreatorTest {

	HexahedronCreator creator = new HexahedronCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void creatorImplementsMeshCreatorInterface() {
		Assert.assertTrue(creator instanceof IMeshCreator);
	}
	
	@Test
	public void creatorCreatedNewMeshInstanceWithEachCallOfCreate() {
		HexahedronCreator creator = new HexahedronCreator();
		Mesh3D previous = creator.create();
		for (int i = 0; i < 20; i++) {
			Mesh3D mesh = creator.create();
			Assert.assertTrue(mesh != previous);
			previous = mesh;
		}
	}
	
	@Test
	public void createdMeshHasSixSquareFaces() {
		Assert.assertEquals(6, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
	@Test
	public void createdMeshHasUniqueVertices() {
		MeshTestUtil.meshHasUniqueVertices(mesh);
	}
	
	@Test
	public void createdMeshHasSixFaces() {
		Assert.assertEquals(6, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshHasTwelveEdges() {
		Assert.assertEquals(12, mesh.createEdges().size());
	}
	
	@Test
	public void createdMeshHasEightVertices() {
		Assert.assertEquals(8, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshFulFillsEulersCharacteristic() {
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void allEdgesOfTheCreatedMeshHaveEqualLength() {
		MeshTestUtil.allEdgesHaveEqualLength(mesh);
	}
	
	@Test
	public void eachEdgeOfTheCreatedMeshIsIncidentToOnlyOneOrTwoFaces() {
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh);
	}
	
	@Test
	public void getRadiusReturnsOneByDefault() {
		Assert.assertEquals(1, creator.getRadius(), 0);
	}
	
	@Test
	public void setRadiusToValueGetRadiusReturnsValue() {
		float radius = 10.2f;
		creator.setRadius(radius);
		Assert.assertEquals(radius, creator.getRadius(), 0);
	}
	
	@Test
	public void theEdgeLengthOfEachEdgeIsTwiceAsLargeAsTheGivenRadius() {
		float radius = 33.33f;
		Mesh3D mesh;
		Collection<Edge3D> edges;
		HexahedronCreator creator = new HexahedronCreator();
		creator.setRadius(radius);
		mesh = creator.create();
		edges = mesh.createEdges();
		for (Edge3D edge : edges) {
			Vector3f from = mesh.getVertexAt(edge.getFromIndex());
			Vector3f to = mesh.getVertexAt(edge.getToIndex());
			float length = from.subtract(to).length();
			Assert.assertEquals(radius * 2, length, 0);
		}
	}
	
}
