package mesh;

import org.junit.Assert;
import org.junit.Test;

public class EdgeTest {
	
	@Test
	public void getFromIndexReturnsMinusOneByDefault() {
		Edge edge = new Edge();
		Assert.assertEquals(-1, edge.getFromIndex());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setFromIndexToNegativeValueThrowsIllegalArgumentException() {
		Edge edge = new Edge();
		edge.setFromIndex(-1);
	}

	@Test
	public void setFromIndexToRandomPositiveValueThrowsNoException() {
		Edge edge = new Edge();
		int random = (int) (Math.random() * Integer.MAX_VALUE);
		edge.setFromIndex(random);
	}

	@Test
	public void ifSetFromIndexToRandomValueThenGetFromIndexReturnsThatValue() {
		Edge edge = new Edge();
		int random = (int) (Math.random() * Integer.MAX_VALUE);
		edge.setFromIndex(random);
		Assert.assertEquals(random, edge.getFromIndex());
	}

	@Test
	public void ifSetFromIndexToRandomNegativeValueThenGetFromIndexReturnsOldValue() {
		Edge edge = new Edge();
		int random = (int) (Math.random() * Integer.MAX_VALUE);
		int negativeRandom = random * -1;
		edge.setFromIndex(random);
		try {
			edge.setFromIndex(negativeRandom);
		} catch (Exception e) {
		}
		Assert.assertEquals(random, edge.getFromIndex());
	}

	@Test
	public void getToIndexReturnsMinusOneByDefault() {
		Edge edge = new Edge();
		Assert.assertEquals(-1, edge.getToIndex());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setToIndexToNegativeValueThrowsIllegalArgumentException() {
		Edge edge = new Edge();
		edge.setToIndex(-1);
	}

	@Test
	public void setToIndexToRandomPositiveValueThrowsNoException() {
		Edge edge = new Edge();
		int random = (int) (Math.random() * Integer.MAX_VALUE);
		edge.setToIndex(random);
	}

	@Test
	public void ifSetToIndexToRandomValueThenGetToIndexReturnsThatValue() {
		Edge edge = new Edge();
		int random = (int) (Math.random() * Integer.MAX_VALUE);
		edge.setToIndex(random);
		Assert.assertEquals(random, edge.getToIndex());
	}

	@Test
	public void ifSetToIndexToRandomNegativeValueThenGetToIndexReturnsOldValue() {
		Edge edge = new Edge();
		int random = (int) (Math.random() * Integer.MAX_VALUE);
		int negativeRandom = random * -1;
		edge.setToIndex(random);
		try {
			edge.setToIndex(negativeRandom);
		} catch (Exception e) {
		}
		Assert.assertEquals(random, edge.getToIndex());
	}
	
	@Test
	public void createEgdeWithParameters() {
		int randomFromIndex = (int) (Math.random() * Integer.MAX_VALUE);
		int randomToIndex = (int) (Math.random() * Integer.MAX_VALUE);
		Edge edge = new Edge(randomFromIndex, randomToIndex);
		Assert.assertEquals(randomFromIndex, edge.getFromIndex());
		Assert.assertEquals(randomToIndex, edge.getToIndex());
	}
	
	@Test
	public void edgeIsEqualToItself() {
		Edge edge = new Edge();
		Assert.assertTrue(edge.equals(edge));
	}
	
	@Test
	public void edgeIsNotEqualToNull() {
		Edge edge = new Edge();
		Assert.assertFalse(edge.equals(null));
	}
	
	@Test
	public void edgeIsNotEqualToObjectOfDifferentType() {
		Edge edge = new Edge();
		Assert.assertFalse(edge.equals(new Object()));
	}
	
	@Test
	public void edgesWithDifferentToIndicesAreNotEqual() {
		int randomFromIndex = (int) (Math.random() * Integer.MAX_VALUE);
		int randomToIndexA = (int) (Math.random() * Integer.MAX_VALUE);
		int randomToIndexB = (int) (Math.random() * Integer.MAX_VALUE);
		Edge edge1 = new Edge(randomFromIndex, randomToIndexA);
		Edge edge2 = new Edge(randomFromIndex, randomToIndexB);
		Assert.assertFalse(edge1.equals(edge2));
	}
	
	@Test
	public void edgesWithDifferentFromIndicesAreNotEqual() {
		int randomToIndex = (int) (Math.random() * Integer.MAX_VALUE);
		int randomFromIndexA = (int) (Math.random() * Integer.MAX_VALUE);
		int randomFromIndexB = (int) (Math.random() * Integer.MAX_VALUE);
		Edge edge1 = new Edge(randomFromIndexA, randomToIndex);
		Edge edge2 = new Edge(randomFromIndexB, randomToIndex);
		Assert.assertFalse(edge1.equals(edge2));
	}
	
	@Test
	public void testHashCode() {
		int randomToIndex = (int) (Math.random() * Integer.MAX_VALUE);
		int randomFromIndex = (int) (Math.random() * Integer.MAX_VALUE);
		Edge edge = new Edge(randomFromIndex, randomToIndex);
		final int prime = 31;
		int result = 1;
		result = prime * result + randomFromIndex;
		result = prime * result + randomToIndex;
		Assert.assertEquals(result, edge.hashCode());
	}
	
}
