package mesh.creator.platonic;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import math.Mathf;
import math.Vector3f;
import mesh.Edge3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import util.MeshTestUtil;

public class TetrahedronCreatorTest {

	TetrahedronCreator creator = new TetrahedronCreator();
	Mesh3D mesh = creator.create();

	@Test
	public void creatorImplementsMeshCreatorInterface() {
		Assert.assertTrue(creator instanceof IMeshCreator);
	}

	@Test
	public void creatorCreatedNewMeshInstanceWithEachCallOfCreate() {
		TetrahedronCreator creator = new TetrahedronCreator();
		Mesh3D previous = creator.create();
		for (int i = 0; i < 20; i++) {
			Mesh3D mesh = creator.create();
			Assert.assertTrue(mesh != previous);
			previous = mesh;
		}
	}

	@Test
	public void createdMeshHasUniqueVertices() {
		MeshTestUtil.meshHasUniqueVertices(mesh);
	}

	@Test
	public void createdMeshHasFourFaces() {
		Assert.assertEquals(4, mesh.getFaceCount());
	}

	@Test
	public void createdMeshHasSixEdges() {
		Assert.assertEquals(6, mesh.createEdges().size());
	}

	@Test
	public void createdMeshHasFourVertices() {
		Assert.assertEquals(4, mesh.getVertexCount());
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
	public void createdMeshFulFillsEulersCharacteristic() {
		MeshTestUtil.meshFulFillsEulersCharacteristic(mesh);
	}

	@Test
	public void createMeshHasUniqueVertices() {
		MeshTestUtil.meshHasUniqueVertices(mesh);
	}

	@Test
	public void theDefaultLengthOfEachEdgeIsTwoTimesTheSquareRootOfTwo() {
		float expectedEdgeLength = 2 * Mathf.sqrt(2);
		Collection<Edge3D> edges = mesh.createEdges();
		for (Edge3D edge : edges) {
			Vector3f from = mesh.getVertexAt(edge.getFromIndex());
			Vector3f to = mesh.getVertexAt(edge.getToIndex());
			float length = from.subtract(to).length();
			Assert.assertEquals(expectedEdgeLength, length, 0);
		}
	}

	@Test
	public void theEdgeLengthDependsOnTheRadius() {
		float radius = 33.33f;
		float expectedEdgeLength = new Vector3f(radius, radius, radius).subtract(radius, -radius, -radius).length();
		Mesh3D mesh;
		Collection<Edge3D> edges;
		TetrahedronCreator creator = new TetrahedronCreator();
		creator.setRadius(radius);
		mesh = creator.create();
		edges = mesh.createEdges();
		for (Edge3D edge : edges) {
			Vector3f from = mesh.getVertexAt(edge.getFromIndex());
			Vector3f to = mesh.getVertexAt(edge.getToIndex());
			float length = from.subtract(to).length();
			Assert.assertEquals(expectedEdgeLength, length, 0);
		}
	}
	
	@Test
	public void setRadiusToValueGetRadiusReturnsValue() {
		float radius = 444.123f;
		TetrahedronCreator creator = new TetrahedronCreator();
		creator.setRadius(radius);
		Assert.assertEquals(radius, creator.getRadius(), 0);
	}

}
