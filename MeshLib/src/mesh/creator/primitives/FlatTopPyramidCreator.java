package mesh.creator.primitives;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class FlatTopPyramidCreator implements IMeshCreator {

	private float bottomRadius;
	private float topRadius;
	private float height;
	private Mesh3D mesh;
	
	public FlatTopPyramidCreator() {
		bottomRadius = 1.0f;
		topRadius = 0.5f;
		height = 2.0f;
	}
	
	public FlatTopPyramidCreator(float bottomRadius, float topRadius) {
		this.bottomRadius = bottomRadius;
		this.topRadius = topRadius;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		createVertices();
		createFaces();
		return mesh;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private void createFaces() {
		mesh.addFace(3, 0, 1, 2);
		mesh.addFace(6, 5, 4, 7);
		mesh.addFace(1, 0, 4, 5);
		mesh.addFace(1, 5, 6, 2);
		mesh.addFace(6, 7, 3, 2);
		mesh.addFace(3, 7, 4, 0);
	}

	private void createVertices() {
		float halfHeight = height * 0.5f;
		
		mesh.addVertex(topRadius, -halfHeight, -topRadius);
		mesh.addVertex(topRadius, -halfHeight, topRadius);
		mesh.addVertex(-topRadius, -halfHeight, topRadius);
		mesh.addVertex(-topRadius, -halfHeight, -topRadius);
		mesh.addVertex(bottomRadius, halfHeight, -bottomRadius);
		mesh.addVertex(bottomRadius, halfHeight, bottomRadius);
		mesh.addVertex(-bottomRadius, halfHeight, bottomRadius);
		mesh.addVertex(-bottomRadius, halfHeight, -bottomRadius);
	}
	
	public float getBottomRadius() {
		return bottomRadius;
	}

	public void setBottomRadius(float bottomRadius) {
		this.bottomRadius = bottomRadius;
	}

	public float getTopRadius() {
		return topRadius;
	}

	public void setTopRadius(float topRadius) {
		this.topRadius = topRadius;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}
