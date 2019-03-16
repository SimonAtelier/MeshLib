package mesh.creator.special;

import org.junit.Assert;
import org.junit.Test;

import mesh.Mesh3D;
import util.MeshTestUtil;

public class AntiprismCreatorTest {

	AntiprismCreator creator = new AntiprismCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void createdMeshHasTwoFacesWithSixVerticesByDefault() {
		Assert.assertEquals(2, mesh.getNumberOfFacesWithVertexCountOfN(6));
	}
	
	@Test
	public void createdMeshhHasTwelveTriangleFacesByDefault() {
		Assert.assertEquals(12, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void eachEdgeOfTheCreatedMeshIsIncidentToOnlyOneOrTwoFaces() {
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh);
	}
	
	@Test
	public void createdMeshFulFillsEulersCharacteristic() {
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void createdMeshHasTwoFacesWithVertexCountOfN() {
		int n = 22;
		AntiprismCreator creator = new AntiprismCreator();
		creator.setN(n);
		Mesh3D mesh = creator.create();
		Assert.assertEquals(2, mesh.getNumberOfFacesWithVertexCountOfN(n));
	}
	
	@Test
	public void createdMeshHasAnAmountOfTriangleFacesEqualToTwoTimesN() {
		int n = 111;
		AntiprismCreator creator = new AntiprismCreator();
		creator.setN(n);
		Mesh3D mesh = creator.create();
		Assert.assertEquals(n + n, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void createdMeshHasEightTrianglesFacesWithThreeAsN() {
		int n = 3;
		AntiprismCreator creator = new AntiprismCreator();
		creator.setN(n);
		Mesh3D mesh = creator.create();
		Assert.assertEquals(8, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void createdMeshKeepsEulersCharacteristicWithChangedN() {
		int n = 2;
		AntiprismCreator creator = new AntiprismCreator();
		creator.setN(n);
		Mesh3D mesh = creator.create();
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}
	
	@Test
	public void eachEdgeOfTheCreatedMeshIsIncidentToOnlyOneOrTwoFacesWithChangedN() {
		int n = 2;
		AntiprismCreator creator = new AntiprismCreator();
		creator.setN(n);
		Mesh3D mesh = creator.create();
		MeshTestUtil.eachEdgeIsIncidentToOnlyOneOrTwoFaces(mesh);
	}
	
}
