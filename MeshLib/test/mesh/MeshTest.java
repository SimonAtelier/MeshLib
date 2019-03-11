package mesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import math.Vector3;

public class MeshTest {

	@Test(expected = NoSuchElementException.class)
	public void addFaceWithNullIndicesDoesNotAddAFace() {
		Mesh mesh = new Mesh();
		try {
			mesh.addFace(null);
		} catch (Exception e) {
		}
		mesh.getFaceAt(0);
	}

	@Test
	public void addFaceWithNullIndicesDoesNotIncrementFaceCount() {
		Mesh mesh = new Mesh();
		try {
			mesh.addFace(null);
		} catch (Exception e) {
		}
		Assert.assertEquals(0, mesh.getFaceCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void addFaceWithNullIndicesThrowsIllegalArgumentException() {
		Mesh mesh = new Mesh();
		mesh.addFace(null);
	}

	@Test
	public void getVertexCountReturnsZeroByDefault() {
		Mesh mesh = new Mesh();
		Assert.assertEquals(0, mesh.getVertexCount());
	}

	@Test
	public void getFaceCountReturnsZeroByDefault() {
		Mesh mesh = new Mesh();
		Assert.assertEquals(0, mesh.getFaceCount());
	}

	@Test
	public void getVertexCountReturnsOneAfterAddingOneVertex() {
		Mesh mesh = new Mesh();
		mesh.addVertex(0, 0, 0);
		Assert.assertEquals(1, mesh.getVertexCount());
	}

	@Test
	public void getFaceCountReturnsOneAfterAddingOneFace() {
		Mesh mesh = new Mesh();
		mesh.addFace(0, 0, 0);
		Assert.assertEquals(1, mesh.getFaceCount());
	}

	@Test
	public void getVertexCountAfterAddingRandomAmountOfVertices() {
		Mesh mesh = new Mesh();
		int random = (int) (Math.random() * 100);
		for (int i = 0; i < random; i++) {
			mesh.addVertex(0, 0, 0);
			Assert.assertEquals(i + 1, mesh.getVertexCount());
		}
	}

	@Test
	public void getFaceCountAfterAddingRandomAmountOfFaces() {
		Mesh mesh = new Mesh();
		int random = (int) (Math.random() * 100);
		for (int i = 0; i < random; i++) {
			mesh.addFace(0, 0, 0);
			Assert.assertEquals(i + 1, mesh.getFaceCount());
		}
	}

	@Test(expected = NoSuchElementException.class)
	public void getVertexAtIndexZeroThrowsNoSuchElementException() {
		Mesh mesh = new Mesh();
		mesh.getVertexAt(0);
	}

	@Test
	public void getVertexAtIndexZeroReturnsNoneNullObjectAfterAddingOneVertex() {
		Mesh mesh = new Mesh();
		mesh.addVertex(0, 0, 0);
		Assert.assertNotNull(mesh.getVertexAt(0));
	}

	@Test
	public void getVertexAtIndexZeroReturnsNoneNullObjectOfTypeVector3AfterAddingOneVertex() {
		Mesh mesh = new Mesh();
		mesh.addVertex(0, 0, 0);
		Vector3 vector3 = mesh.getVertexAt(0);
		Assert.assertNotNull(vector3);
	}

	@Test
	public void addVertexReturnsVectorWithGivenValues() {
		float randomX = (float) (Math.random() * Integer.MAX_VALUE);
		float randomY = (float) (Math.random() * Integer.MAX_VALUE);
		float randomZ = (float) (Math.random() * Integer.MAX_VALUE);

		Mesh mesh = new Mesh();
		mesh.addVertex(randomX, randomY, randomZ);

		Vector3 vector3 = mesh.getVertexAt(0);
		Assert.assertEquals(randomX, vector3.getX(), 0);
		Assert.assertEquals(randomY, vector3.getY(), 0);
		Assert.assertEquals(randomZ, vector3.getZ(), 0);
	}

	@Test
	public void addRandomAmountOfVerticesToTestGetVertexAt() {
		int randomAmount = (int) (Math.random() * 100);
		Mesh mesh = new Mesh();

		List<Vector3> vertices = new ArrayList<Vector3>();

		for (int i = 0; i < randomAmount; i++) {
			float randomX = (float) (Math.random() * Integer.MAX_VALUE);
			float randomY = (float) (Math.random() * Integer.MAX_VALUE);
			float randomZ = (float) (Math.random() * Integer.MAX_VALUE);
			mesh.addVertex(randomX, randomY, randomZ);
			vertices.add(new Vector3(randomX, randomY, randomZ));
		}

		for (int i = 0; i < vertices.size(); i++) {
			Vector3 expected = vertices.get(i);
			Vector3 actual = mesh.getVertexAt(i);
			Assert.assertEquals(expected.getX(), actual.getX(), 0);
			Assert.assertEquals(expected.getY(), actual.getY(), 0);
			Assert.assertEquals(expected.getZ(), actual.getZ(), 0);
		}
	}

	@Test(expected = NoSuchElementException.class)
	public void getFaceAtIndexZeroThrowsNoSuchElementException() {
		Mesh mesh = new Mesh();
		mesh.getFaceAt(0);
	}

	@Test
	public void getFaceAtIndexZeroReturnsNoneNullObjectAfterAddingOneFace() {
		Mesh mesh = new Mesh();
		mesh.addVertex(0, 0, 0);
		mesh.addFace(0, 0, 0);
		Assert.assertNotNull(mesh.getFaceAt(0));
	}

	@Test
	public void getFaceAtIndexZeroReturnsNoneNullObjectOfTypeFaceAfterAddingOneFace() {
		Mesh mesh = new Mesh();
		mesh.addFace(0, 0, 0);
		Face face = mesh.getFaceAt(0);
		Assert.assertNotNull(face);
	}

	@Test
	public void addFaceWithIndicesGetFaceAtIndexZeroReturnsFaceWithIndices() {
		int[] indices = new int[] { 1, 3, 4 };
		Mesh mesh = new Mesh();
		mesh.addFace(indices);
		Face face = mesh.getFaceAt(0);
		Assert.assertArrayEquals(indices, face.getIndices());
	}

	@Test
	public void addFaceWithRandomIndices() {
		int[] indices = new int[3];
		indices[0] = (int) (Math.random() * Integer.MAX_VALUE);
		indices[1] = (int) (Math.random() * Integer.MAX_VALUE);
		indices[2] = (int) (Math.random() * Integer.MAX_VALUE);

		Mesh mesh = new Mesh();
		mesh.addFace(indices);
		Face face = mesh.getFaceAt(0);
		Assert.assertArrayEquals(indices, face.getIndices());
	}

	@Test
	public void addRandomAmountOfFacesWithRandomIndices() {
		int randomAmount = (int) (Math.random() * 100);

		Mesh mesh = new Mesh();
		List<Face> expected = new ArrayList<Face>();

		for (int i = 0; i < randomAmount; i++) {
			int[] indices = new int[3];
			indices[0] = (int) (Math.random() * Integer.MAX_VALUE);
			indices[1] = (int) (Math.random() * Integer.MAX_VALUE);
			indices[2] = (int) (Math.random() * Integer.MAX_VALUE);
			Face face = new Face(indices);
			mesh.addFace(indices);
			expected.add(face);
		}

		for (int i = 0; i < expected.size(); i++) {
			Face expectedFace = expected.get(i);
			Face actualFace = mesh.getFaceAt(i);
			Assert.assertArrayEquals(expectedFace.getIndices(), actualFace.getIndices());
		}
	}
	
	@Test
	public void createEdgesReturnsNotNullObject() {
		Mesh mesh = new Mesh();
		Assert.assertNotNull(mesh.calculateEdges());
	}
	
	@Test
	public void createEdgesReturnsEmptyCollectionByDefault() {
		Mesh mesh = new Mesh();
		Collection<Edge> edges = mesh.calculateEdges();
		Assert.assertTrue(edges.isEmpty());
	}
	
	@Test
	public void createEdgesReturnsCollectionWithFourVerticesAfterAddingQauadFace() {
		Mesh mesh = new Mesh();
		mesh.addFace(0, 1, 2, 3);
		Collection<Edge> edges = mesh.calculateEdges();
		Assert.assertEquals(4, edges.size());
	}
	
}
