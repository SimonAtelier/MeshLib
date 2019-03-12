package mesh;

import org.junit.Test;

import mesh.exception.NumberOfIndicesMustBeGreaterOrEqualToThree;

public class FaceTest {

	@Test (expected = NumberOfIndicesMustBeGreaterOrEqualToThree.class)
	public void faceWithNodeIndicesIsNotValid() {
		int[] indices = new int[0];
		new Face(indices);
	}
	
	@Test (expected = NumberOfIndicesMustBeGreaterOrEqualToThree.class)
	public void faceWithOneIndexIsNotValid() {
		int random = (int) (Math.random() * Integer.MAX_VALUE);
		new Face(random);
	}
	
	@Test (expected = NumberOfIndicesMustBeGreaterOrEqualToThree.class)
	public void faceWithTwoIndicesIsNotValid() {
		int randomA = (int) (Math.random() * Integer.MAX_VALUE);
		int randomB = (int) (Math.random() * Integer.MAX_VALUE);
		int[] indices = new int[] {randomA, randomB};
		new Face(indices);
	}
	
}
