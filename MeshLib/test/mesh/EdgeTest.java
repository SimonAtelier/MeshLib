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

}
