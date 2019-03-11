package math;

import org.junit.Assert;
import org.junit.Test;

public class Vector3Test {

	@Test
	public void vectorIsEqualToItself() {
		Vector3 vector3 = new Vector3(0, 0, 0);
		Assert.assertTrue(vector3.equals(vector3));
	}

	@Test
	public void vectorIsNotEqualToNull() {
		Vector3 vector3 = new Vector3(0, 0, 0);
		Assert.assertFalse(vector3.equals(null));
	}

	@Test
	public void vectorIsNotEqualToObjectOfOtherType() {
		Vector3 vector3 = new Vector3(0, 0, 0);
		Assert.assertFalse(vector3.equals(new Object()));
	}

	@Test
	public void vectorIsEqualToVectorWithSameComponents() {
		float random = (float) (Math.random() * Integer.MAX_VALUE);
		Vector3 vector3One = new Vector3(random, random, random);
		Vector3 vector3Two = new Vector3(random, random, random);
		Assert.assertTrue(vector3One.equals(vector3Two));
	}

	@Test
	public void vectorIsNotEqualToVectorWithDifferentXComponents() {
		float randomA = (float) (Math.random() * Integer.MAX_VALUE);
		float randomB = (float) (Math.random() * Integer.MAX_VALUE);
		Vector3 vector3One = new Vector3(randomA, randomA, randomA);
		Vector3 vector3Two = new Vector3(randomB, randomA, randomA);
		Assert.assertFalse(vector3One.equals(vector3Two));
	}

	@Test
	public void vectorIsNotEqualToVectorWithDifferentYComponents() {
		float randomA = (float) (Math.random() * Integer.MAX_VALUE);
		float randomB = (float) (Math.random() * Integer.MAX_VALUE);
		Vector3 vector3One = new Vector3(randomA, randomA, randomA);
		Vector3 vector3Two = new Vector3(randomA, randomB, randomA);
		Assert.assertFalse(vector3One.equals(vector3Two));
	}

	@Test
	public void vectorIsNotEqualToVectorWithDifferentZComponents() {
		float randomA = (float) (Math.random() * Integer.MAX_VALUE);
		float randomB = (float) (Math.random() * Integer.MAX_VALUE);
		Vector3 vector3One = new Vector3(randomA, randomA, randomA);
		Vector3 vector3Two = new Vector3(randomA, randomA, randomB);
		Assert.assertFalse(vector3One.equals(vector3Two));
	}

	@Test
	public void testHashCode() {
		float randomX = (float) (Math.random() * Integer.MAX_VALUE);
		float randomY = (float) (Math.random() * Integer.MAX_VALUE);
		float randomZ = (float) (Math.random() * Integer.MAX_VALUE);
		final int prime = 31;
		int result = 1;
		Vector3 vector3 = new Vector3(randomX, randomY, randomZ);
		result = prime * result + Float.floatToIntBits(randomX);
		result = prime * result + Float.floatToIntBits(randomY);
		result = prime * result + Float.floatToIntBits(randomZ);
		Assert.assertEquals(result, vector3.hashCode());
	}

	@Test
	public void subtractVectorReturnsVector() {
		Vector3 vectorA = new Vector3(0, 0, 0);
		Vector3 vectorB = new Vector3(0, 0, 0);
		Vector3 vectorC = vectorA.subtract(vectorB);
		Assert.assertTrue(vectorA == vectorC);
	}

	@Test
	public void subtractSubtractsInternally() {
		float randomAx = (float) (Math.random() * Integer.MAX_VALUE);
		float randomAy = (float) (Math.random() * Integer.MAX_VALUE);
		float randomAz = (float) (Math.random() * Integer.MAX_VALUE);
		
		float randomBx = (float) (Math.random() * Integer.MAX_VALUE);
		float randomBy = (float) (Math.random() * Integer.MAX_VALUE);
		float randomBz = (float) (Math.random() * Integer.MAX_VALUE);
		
		Vector3 vectorA = new Vector3(randomAx, randomAy, randomAz);
		Vector3 vectorB = new Vector3(randomBx, randomBy, randomBz);
		
		vectorA.subtract(vectorB);
		
		Assert.assertEquals(randomAx - randomBx, vectorA.getX(), 0);
		Assert.assertEquals(randomAy - randomBy, vectorA.getY(), 0);
		Assert.assertEquals(randomAz - randomBz, vectorA.getZ(), 0);
	}

}
