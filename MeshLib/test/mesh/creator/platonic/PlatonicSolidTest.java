package mesh.creator.platonic;

import org.junit.Assert;
import org.junit.Test;

public class PlatonicSolidTest {

	@Test
	public void tetrahedronHasFourFaces() {
		PlatonicSolid platonicSolid = PlatonicSolid.TETRAHEDRON;
		Assert.assertEquals(4, platonicSolid.getFaceCount());
	}
	
	@Test
	public void tetrahedronHasSixEdges() {
		PlatonicSolid platonicSolid = PlatonicSolid.TETRAHEDRON;
		Assert.assertEquals(6, platonicSolid.getEdgeCount());
	}
	
	@Test
	public void tetrahedronHasFourVertices() {
		PlatonicSolid platonicSolid = PlatonicSolid.TETRAHEDRON;
		Assert.assertEquals(4, platonicSolid.getVertexCount());
	}
	
	@Test
	public void tetrahedronHasExpectedName() {
		PlatonicSolid platonicSolid = PlatonicSolid.TETRAHEDRON;
		Assert.assertEquals("TETRAHEDRON", platonicSolid.getName());
	}
	
	@Test
	public void hexahedronHasSixFaces() {
		PlatonicSolid platonicSolid = PlatonicSolid.HEXAHEDRON;
		Assert.assertEquals(6, platonicSolid.getFaceCount());
	}
	
	@Test
	public void hexahedronHasTwelveEdges() {
		PlatonicSolid platonicSolid = PlatonicSolid.HEXAHEDRON;
		Assert.assertEquals(12, platonicSolid.getEdgeCount());
	}
	
	@Test
	public void hexahedronHasEightVertices() {
		PlatonicSolid platonicSolid = PlatonicSolid.HEXAHEDRON;
		Assert.assertEquals(8, platonicSolid.getVertexCount());
	}
	
	@Test
	public void hexahedronHasExpectedName() {
		PlatonicSolid platonicSolid = PlatonicSolid.HEXAHEDRON;
		Assert.assertEquals("HEXAHEDRON", platonicSolid.getName());
	}
	
	@Test
	public void octahedronHasEightFaces() {
		PlatonicSolid platonicSolid = PlatonicSolid.OCTAHEDRON;
		Assert.assertEquals(8, platonicSolid.getFaceCount());
	}
	
	@Test
	public void octahedronHasTwelveEdges() {
		PlatonicSolid platonicSolid = PlatonicSolid.OCTAHEDRON;
		Assert.assertEquals(12, platonicSolid.getEdgeCount());
	}
	
	@Test
	public void octahedronHasSixVertices() {
		PlatonicSolid platonicSolid = PlatonicSolid.OCTAHEDRON;
		Assert.assertEquals(6, platonicSolid.getVertexCount());
	}
	
	@Test
	public void octahedronHasExpectedName() {
		PlatonicSolid platonicSolid = PlatonicSolid.OCTAHEDRON;
		Assert.assertEquals("OCTAHEDRON", platonicSolid.getName());
	}
	
	@Test
	public void icosahedronHasTwentyFaces() {
		PlatonicSolid platonicSolid = PlatonicSolid.ICOSAHEDRON;
		Assert.assertEquals(20, platonicSolid.getFaceCount());
	}
	
	@Test
	public void icosahedronHasThirtyEdges() {
		PlatonicSolid platonicSolid = PlatonicSolid.ICOSAHEDRON;
		Assert.assertEquals(30, platonicSolid.getEdgeCount());
	}
	
	@Test
	public void icosahedronHasTwelveVertices() {
		PlatonicSolid platonicSolid = PlatonicSolid.ICOSAHEDRON;
		Assert.assertEquals(12, platonicSolid.getVertexCount());
	}
	
	@Test
	public void icosahedronHasExpectedName() {
		PlatonicSolid platonicSolid = PlatonicSolid.ICOSAHEDRON;
		Assert.assertEquals("ICOSAHEDRON", platonicSolid.getName());
	}
	
	@Test
	public void dodecahedronHasTwelveFaces() {
		PlatonicSolid platonicSolid = PlatonicSolid.DODECAHEDRON;
		Assert.assertEquals(12, platonicSolid.getFaceCount());
	}
	
	@Test
	public void dodecahedronHasThirtyEdges() {
		PlatonicSolid platonicSolid = PlatonicSolid.DODECAHEDRON;
		Assert.assertEquals(30, platonicSolid.getEdgeCount());
	}
	
	@Test
	public void dodecahedronHasTwentyVertices() {
		PlatonicSolid platonicSolid = PlatonicSolid.DODECAHEDRON;
		Assert.assertEquals(20, platonicSolid.getVertexCount());
	}
	
	@Test
	public void dodecahedronHasExpectedName() {
		PlatonicSolid platonicSolid = PlatonicSolid.DODECAHEDRON;
		Assert.assertEquals("DODECAHEDRON", platonicSolid.getName());
	}
	
}
