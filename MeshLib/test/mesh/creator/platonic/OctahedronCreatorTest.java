package mesh.creator.platonic;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import util.MeshTestUtil;

public class OctahedronCreatorTest {

	OctahedronCreator creator = new OctahedronCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void creatorImplementsMeshCreatorInterface() {
		Assert.assertTrue(creator instanceof IMeshCreator);
	}
	
	@Test
	public void creatorCreatedNewMeshInstanceWithEachCallOfCreate() {
		OctahedronCreator creator = new OctahedronCreator();
		Mesh3D previous = creator.create();
		for (int i = 0; i < 20; i++) {
			Mesh3D mesh = creator.create();
			Assert.assertTrue(mesh != previous);
			previous = mesh;
		}
	}
	
	@Test
	public void createdMeshHasEightFaces() {
		Assert.assertEquals(8, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshHasTwelveEdges() {
		Assert.assertEquals(12, mesh.createEdges().size());
	}
	
	@Test
	public void createdMeshHasSixVertices() {
		Assert.assertEquals(6, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshFulFillsEulersCharacteristic() {
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void createdMeshHasUniqueVertices() {
		MeshTestUtil.meshHasUniqueVertices(mesh);
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
	public void setRadiusToValueGetRadiusReturnsValue() {
		float radius = 134.789f;
		OctahedronCreator creator = new OctahedronCreator();
		creator.setRadius(radius);
		Assert.assertEquals(radius, creator.getRadius(), 0);
	}
	
	@Test
	public void constructCreatorWithRadiusGetRadiusReturnsExpectedValue() {
		float radius = 456.333f;
		OctahedronCreator creator = new OctahedronCreator(radius);
		Assert.assertEquals(radius, creator.getRadius(), 0);
	}
	
	@Test
	public void distanceFromEachVertexToCenterIsEqualToRadiusPassedToTheConstructor() {
		float radius = 675.456f;
		OctahedronCreator creator = new OctahedronCreator(radius);
		Mesh3D mesh = creator.create();
		for (Vector3f v : mesh.getVertices()) {
			float distance = new Vector3f().distance(v);
			Assert.assertEquals(radius, distance, 0);
		}
	}
	
	@Test
	public void distanceFromEachVertexToCenterIsEqualToRadiusSet() {
		float radius = 12345.12345f;
		OctahedronCreator creator = new OctahedronCreator();
		creator.setRadius(radius);
		Mesh3D mesh = creator.create();
		for (Vector3f v : mesh.getVertices()) {
			float distance = new Vector3f().distance(v);
			Assert.assertEquals(radius, distance, 0);
		}
	}
	
	@Test
	public void createdMeshHasEightTriangularFaces() {
		Assert.assertEquals(8, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
}
