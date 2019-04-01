package mesh.creator.primitives;

import org.junit.Assert;
import org.junit.Test;

import mesh.Mesh3D;
import mesh.creator.FillType;
import util.MeshTestUtil;

public class SegmentedCylinderCreatorTest {

	SegmentedCylinderCreator creator = new SegmentedCylinderCreator();
	Mesh3D mesh = creator.create();
	
	@Test
	public void getBottomRadiusReturnsOneByDefault() {
		Assert.assertEquals(1, creator.getBottomRadius(), 0);
	}
	
	@Test
	public void getTopRadiusReturnsOneByDefault() {
		Assert.assertEquals(1, creator.getTopRadius(), 0);
	}
	
	@Test
	public void getHeightReturnsTwoByDefault() {
		Assert.assertEquals(2, creator.getHeight(), 0);
	}
	
	@Test
	public void getHeightSegments() {
		Assert.assertEquals(1, creator.getHeightSegments(), 0);
	}
	
	@Test
	public void getRotationSegmentsReturnsThirtyTwoByDefault() {
		Assert.assertEquals(32, creator.getRotationSegments());
	}
	
	@Test
	public void createdMeshHasSixtyNineFacesByDefault() {
		Assert.assertEquals(96, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshHasSixtyFourVerticesByDefault() {
		Assert.assertEquals(66, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshHasSixtyFourTriangularFacesByDefault() {
		Assert.assertEquals(64, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void createdMeshHasThirtyTwoQuadFacesByDefault() {
		Assert.assertEquals(32, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
	@Test
	public void defaultCapFillTypeIsTriangleFan() {
		Assert.assertEquals(FillType.TRIANGLE_FAN, creator.getCapFillType());
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
	public void createdMeshHasUniqueVertices() {
		MeshTestUtil.meshHasUniqueVertices(mesh);
	}
	
	@Test
	public void createdMeshWithCapFillTypeNGonContainsTwoFacesWithAVertexCountOfThirtyTwo() {
		creator.setCapFillType(FillType.N_GON);
		mesh = creator.create();
		Assert.assertEquals(2, mesh.getNumberOfFacesWithVertexCountOfN(32));
	}
	
	@Test
	public void createdMeshWithCapFillTypeNGonContainsThirtyTwoQuadFaces() {
		creator.setCapFillType(FillType.N_GON);
		mesh = creator.create();
		Assert.assertEquals(32, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
	@Test
	public void createdMeshWithCapFillTypeNGonContainsThirtyFourFaces() {
		creator.setCapFillType(FillType.N_GON);
		mesh = creator.create();
		Assert.assertEquals(34, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshWithCapFillTypeContainsSixtyFourVertices() {
		creator.setCapFillType(FillType.N_GON);
		mesh = creator.create();
		Assert.assertEquals(64, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshWithCapFillTypeNothingContainsSixtyFourVertices() {
		creator.setCapFillType(FillType.NOTHING);
		mesh = creator.create();
		Assert.assertEquals(64, mesh.getVertexCount());
	}
	
	@Test
	public void createdMeshWithCapFillTypeNothingContainsThirtyTwoFaces() {
		creator.setCapFillType(FillType.NOTHING);
		mesh = creator.create();
		Assert.assertEquals(32, mesh.getFaceCount());
	}
	
	@Test
	public void createdMeshWithCapFillTypeNothingContainsThirtyTwoQuadFaces() {
		creator.setCapFillType(FillType.NOTHING);
		mesh = creator.create();
		Assert.assertEquals(32, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
	@Test
	public void isCapTopReturnsTrueByDefault() {
		Assert.assertEquals(true, creator.isCapTop());
	}
	
	@Test
	public void isCapBottomReturnsTrueByDefault() {
		Assert.assertEquals(true, creator.isCapBottom());
	}
	
	@Test
	public void setHeightToValueGetHeightReturnsValue() {
		float height = 4;
		creator.setHeight(height);
		Assert.assertEquals(height, creator.getHeight(), 0);
	}
	
	@Test
	public void setBottomRadiusToValueGetBottomRadiusReturnsValue() {
		float bottomRadius = 5;
		creator.setBottomRadius(bottomRadius);
		Assert.assertEquals(bottomRadius, creator.getBottomRadius(), 0);
	}
	
	@Test
	public void setTopRadiusToValueGetTopRadiusReturnsValue() {
		float topRadius = 5;
		creator.setTopRadius(topRadius);
		Assert.assertEquals(topRadius, creator.getTopRadius(), 0);
	}
	
	@Test
	public void setRotationSegmentsToValueGetRotationSegmentsReturnsValue() {
		int rotationSegments = 40;
		creator.setRotationSegments(rotationSegments);
		Assert.assertEquals(rotationSegments, creator.getRotationSegments());
	}
	
	@Test
	public void setHeightSegmentsToValueGetHeightSegmentsReturnsValue() {
		int heightSegments = 67;
		creator.setHeightSegments(heightSegments);
		Assert.assertEquals(heightSegments, creator.getHeightSegments());
	}
	
	@Test
	public void setCapTopToFalseIsCapTopReturnsFalse() {
		creator.setCapTop(false);
		Assert.assertEquals(false, creator.isCapTop());
	}
	
	@Test
	public void setCapTopToFalseCreatesMeshWithThirtyTwoQuadFaces() {
		creator.setCapTop(false);
		mesh = creator.create();
		Assert.assertEquals(32, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
	@Test
	public void setCapTopToFalseCreatesMeshWithThirtyTwoTriangularFaces() {
		creator.setCapTop(false);
		mesh = creator.create();
		Assert.assertEquals(32, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	@Test
	public void setCapTopToFalseCreatesMeshWithSixtyFourFaces() {
		creator.setCapTop(false);
		mesh = creator.create();
		Assert.assertEquals(64, mesh.getFaceCount());
	}
	
	@Test
	public void setCapBottomToFalseIsCapBottomReturnsFalse() {
		creator.setCapBottom(false);
		Assert.assertEquals(false, creator.isCapBottom());
	}
	
	@Test
	public void setCapBottomToFalseCreatesMeshWithSixtyFourFaces() {
		creator.setCapBottom(false);
		mesh = creator.create();
		Assert.assertEquals(64, mesh.getFaceCount());
	}
	
	@Test
	public void setCapTopAndCapBottomToFalseCreatesMeshWithThirtyTwoQuadFaces() {
		creator.setCapTop(false);
		creator.setCapBottom(false);
		Assert.assertEquals(32, mesh.getNumberOfFacesWithVertexCountOfN(4));
	}
	
	@Test
	public void setTopRadiusToZeroCreatesMeshWithSixtyFourTriangularFaces() {
		creator.setTopRadius(0);
		mesh = creator.create();
		Assert.assertEquals(64, mesh.getNumberOfFacesWithVertexCountOfN(3));
	}
	
	@Test
	public void setTopAndBottomRadiusToZeroCreatesMeshWithZeroVertices() {
		creator.setTopRadius(0);
		creator.setBottomRadius(0);
		mesh = creator.create();
		Assert.assertEquals(0, mesh.getVertexCount());
	}
	
	@Test
	public void setTopAndBottomRadiusToZeroCreatesMeshWithZeroFaces() {
		creator.setTopRadius(0);
		creator.setBottomRadius(0);
		mesh = creator.create();
		Assert.assertEquals(0, mesh.getFaceCount());
	}
	
}
