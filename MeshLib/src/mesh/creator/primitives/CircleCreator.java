package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;

public class CircleCreator implements IMeshCreator {

	private int vertices;
	private float radius;
	private float centerY;
	private FillType fillType;
	private Mesh3D mesh;

	public CircleCreator() {
		this(32, 1);
	}

	public CircleCreator(int vertices, float radius) {
		this(vertices, radius, 0);
	}

	public CircleCreator(int vertices, float radius, float centerY) {
		this(vertices, radius, centerY, FillType.NOTHING);
	}

	public CircleCreator(int vertices, float radius, float centerY, FillType fillType) {
		this.vertices = vertices;
		this.radius = radius;
		this.centerY = centerY;
		this.fillType = fillType;
	}

	private void createVertices() {
		float currentAngle = 0;
		float segmentAngle = Mathf.TWO_PI / vertices;

		for (int i = 0; i < vertices; i++) {
			float x = radius * Mathf.cos(currentAngle);
			float z = radius * Mathf.sin(currentAngle);
			mesh.add(new Vector3f(x, centerY, z));
			currentAngle += segmentAngle;
		}

		if (fillType == FillType.TRIANGLE_FAN) {
			mesh.addVertex(0, centerY, 0);
		}
	}

	private void createFaces() {
		switch (fillType) {
		case NOTHING:
			break;
		case TRIANGLE_FAN:
			createTriangleFan();
			break;
		case N_GON:
			createNGon();
			break;
		default:
			break;
		}
	}

	private void createTriangleFan() {
		for (int i = 0; i < vertices; i++) {
			int idx0 = i % vertices;
			int idx1 = (i + 1) % vertices;
			mesh.addFace(idx0, idx1, vertices);
		}
	}
	
	private void createNGon() {
		int[] indices = new int[vertices];
		for (int i = 0; i < vertices; i++) {
			indices[i] = i;
		}
		mesh.addFace(indices);
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createVertices();
		createFaces();
		return mesh;
	}

	public int getVertices() {
		return vertices;
	}

	public void setVertices(int vertices) {
		this.vertices = vertices;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public FillType getFillType() {
		return fillType;
	}

	public void setFillType(FillType fillType) {
		this.fillType = fillType;
	}

}
