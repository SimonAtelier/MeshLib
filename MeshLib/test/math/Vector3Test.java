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
	public void subtractVectorReturnsNewVector() {
		Vector3 vectorA = new Vector3(0, 0, 0);
		Vector3 vectorB = new Vector3(0, 0, 0);
		Vector3 vectorC = vectorA.subtract(vectorB);
		Assert.assertFalse(vectorA == vectorC);
	}

	@Test
	public void subtractOtherVectorResultsInNewVectorWithSubtractedValues() {
		float randomAx = (float) (Math.random() * Integer.MAX_VALUE);
		float randomAy = (float) (Math.random() * Integer.MAX_VALUE);
		float randomAz = (float) (Math.random() * Integer.MAX_VALUE);

		float randomBx = (float) (Math.random() * Integer.MAX_VALUE);
		float randomBy = (float) (Math.random() * Integer.MAX_VALUE);
		float randomBz = (float) (Math.random() * Integer.MAX_VALUE);

		Vector3 vectorA = new Vector3(randomAx, randomAy, randomAz);
		Vector3 vectorB = new Vector3(randomBx, randomBy, randomBz);

		Vector3 vectorC = vectorA.subtract(vectorB);

		Assert.assertEquals(randomAx - randomBx, vectorC.getX(), 0);
		Assert.assertEquals(randomAy - randomBy, vectorC.getY(), 0);
		Assert.assertEquals(randomAz - randomBz, vectorC.getZ(), 0);
	}

	@Test
	public void unitVectorX() {
		Vector3 vector3 = new Vector3(1, 0, 0);
		Assert.assertEquals(1, vector3.length(), 0);
	}

	@Test
	public void unitVectorY() {
		Vector3 vector3 = new Vector3(0, 1, 0);
		Assert.assertEquals(1, vector3.length(), 0);
	}

	@Test
	public void unitVectorZ() {
		Vector3 vector3 = new Vector3(0, 0, 1);
		Assert.assertEquals(1, vector3.length(), 0);
	}

	@Test
	public void lengthWithTestValuesA() {
		float x = 6.5205402E8f;
		float y = 1.57953382E9f;
		float z = 2.3534346E7f;

		float lengthSquared = (x * x) + (y * y) + (z * z);
		Vector3 vector3 = new Vector3(x, y, z);

		float a = (float) Math.round(lengthSquared * 100000000.0f) / 100000000.0f;
		float b = vector3.length() * vector3.length();
		b = (float) Math.round(b * 100000000.0f) / 100000000.0f;
		
		Assert.assertEquals(a, b, 0);
	}

	@Test
	public void length() {
		float x = (float) (Math.random() * Integer.MAX_VALUE);
		float y = (float) (Math.random() * Integer.MAX_VALUE);
		float z = (float) (Math.random() * Integer.MAX_VALUE);

		float lengthSquared = (x * x) + (y * y) + (z * z);
		Vector3 vector3 = new Vector3(x, y, z);

		float a = (float) Math.round(lengthSquared * 100000000.0f) / 100000000.0f;
		float b = vector3.length() * vector3.length();
		b = (float) Math.round(b * 100000000.0f) / 100000000.0f;

		Assert.assertEquals(a, b, 0);
	}
	
	@Test
	public void addComponentsResultsInNewVectorWithAddedComponents() {
		float x1 = (float) (Math.random() * Integer.MAX_VALUE);
		float y1 = (float) (Math.random() * Integer.MAX_VALUE);
		float z1 = (float) (Math.random() * Integer.MAX_VALUE);
		float x2 = (float) (Math.random() * Integer.MAX_VALUE);
		float y2 = (float) (Math.random() * Integer.MAX_VALUE);
		float z2 = (float) (Math.random() * Integer.MAX_VALUE);
		
		Vector3 vectorA = new Vector3(x1, y1, z1);
		Vector3 result = vectorA.add(x2, y2, z2);
		
		Assert.assertTrue(vectorA != result);
		Assert.assertEquals(x1 + x2, result.getX(), 0);
		Assert.assertEquals(y1 + y2, result.getY(), 0);
		Assert.assertEquals(z1 + z2, result.getZ(), 0);
	}
	
	@Test
	public void normalizeDividesEachComponentByTheLengthOfTheVector() {
		float x = (float) (Math.random() * Integer.MAX_VALUE);
		float y = (float) (Math.random() * Integer.MAX_VALUE);
		float z = (float) (Math.random() * Integer.MAX_VALUE);
		Vector3 vector3 = new Vector3(x, y, z);
		Vector3 result = vector3.normalize();
		float length = vector3.length();
		Assert.assertEquals(x / length, result.getX(), 0);
		Assert.assertEquals(y / length, result.getY(), 0);
		Assert.assertEquals(z / length, result.getZ(), 0);
	}
	
	@Test
	public void zeroVectorHasTheLengthZero() {
		Vector3 vector3 = new Vector3(0, 0, 0);
		float length = vector3.length();
		Assert.assertEquals(0, length, 0);
	}
	
	@Test (expected = ArithmeticException.class)
	public void divideVectorByZero() {
		Vector3 vector3 = new Vector3(1, 1, 1);
		vector3.divide(0);
	}
	
	@Test
	public void normalizeAVectorWithLengthZeroTheComponentsAreDividedByOne() {
		Vector3 vector3 = new Vector3(0, 0, 0);
		Vector3 result = vector3.normalize();
		Assert.assertEquals(0, result.getX(), 0);
		Assert.assertEquals(0, result.getY(), 0);
		Assert.assertEquals(0, result.getZ(), 0);
	}
	
	@Test
	public void normalizeReturnsNewVectorInstance() {
		Vector3 vector3 = new Vector3(0, 0, 0);
		Vector3 result = vector3.normalize();
		Assert.assertTrue(vector3 != result);
	}
	
	@Test
	public void normalizeLeavesTheOriginalVectorUntouched() {
		float x = (float) (Math.random() * Integer.MAX_VALUE);
		float y = (float) (Math.random() * Integer.MAX_VALUE);
		float z = (float) (Math.random() * Integer.MAX_VALUE);
		Vector3 original = new Vector3(x, y, z);
		original.normalize();
		Assert.assertEquals(x, original.getX(), 0);
		Assert.assertEquals(y, original.getY(), 0);
		Assert.assertEquals(z, original.getZ(), 0);
	}
	
	@Test
	public void normalizedVectorHasTheLengthOne() {
		float x = (float) (Math.random() * Integer.MAX_VALUE);
		float y = (float) (Math.random() * Integer.MAX_VALUE);
		float z = (float) (Math.random() * Integer.MAX_VALUE);
		Vector3 original = new Vector3(x, y, z);
		Vector3 result = original.normalize();
		Assert.assertEquals(1, result.length(), 0);
	}
	
}
