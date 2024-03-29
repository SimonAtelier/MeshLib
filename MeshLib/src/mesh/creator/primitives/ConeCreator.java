package mesh.creator.primitives;

import math.Mathf;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.face.CenterSplitFaceModifier;
import mesh.wip.FaceSelection;

public class ConeCreator implements IMeshCreator {

	private int rotationSegments;
	private int heightSegments;
	private float topRadius;
	private float bottomRadius;
	private float height;

	private Mesh3D mesh;

	public ConeCreator() {
		this(32, 10, 0, 1, 2);
	}

	public ConeCreator(int rotationSegments, int heightSegments, float topRadius, float bottomRadius, float height) {
		this.rotationSegments = rotationSegments;
		this.heightSegments = heightSegments;
		this.topRadius = topRadius;
		this.bottomRadius = bottomRadius;
		this.height = height;
	}

	private float getSegmentHeight() {
		return height / heightSegments;
	}

	private float getRadius(int i) {
		float radius = Mathf.map(i, 0, heightSegments, topRadius, bottomRadius);
		return radius;
	}

	private void createVertices() {
		int start = topRadius > 0 ? 0 : 1;
		int end = bottomRadius > 0 ? 0 : 1;
		float angle = Mathf.TWO_PI / rotationSegments;
		for (int i = start; i <= heightSegments - end; i++) {
			for (int j = 0; j < rotationSegments; j++) {
				float x = getRadius(i) * Mathf.cos(angle * j);
				float y = getSegmentHeight() * i - (height * 0.5f);
				float z = getRadius(i) * Mathf.sin(angle * j);
				mesh.addVertex(x, y, z);
			}
		}
	}

	private void addFace(int i, int j) {
		int idx0 = Mathf.toOneDimensionalIndex(i, j, rotationSegments);
		int idx1 = Mathf.toOneDimensionalIndex(i + 1, j, rotationSegments);
		int idx2 = Mathf.toOneDimensionalIndex(i + 1, (j + 1) % rotationSegments, rotationSegments);
		int idx3 = Mathf.toOneDimensionalIndex(i, (j + 1) % rotationSegments, rotationSegments);
		mesh.addFace(idx0, idx1, idx2, idx3);
	}

	private void createQuadFaces() {
		int end = topRadius > 0 ? 0 : 1;
		end += bottomRadius > 0 ? 0 : 1;
		for (int i = 0; i < heightSegments - end; i++) {
			for (int j = 0; j < rotationSegments; j++) {
				addFace(i, j);
			}
		}
	}

	private void createTopCap() {
		int[] indices = new int[rotationSegments];

		for (int i = 0; i < indices.length; i++)
			indices[i] = i;

		mesh.addFace(indices);
	}

	private void createBottomCap() {
		int index = mesh.getVertexCount() - 1;
		int[] indices = new int[rotationSegments];

		for (int i = 0; i < indices.length; i++)
			indices[i] = index - i;

		mesh.addFace(indices);
	}

	private void splitTop() {
		if (topRadius > 0)
			return;

		splitFace(mesh.getFaceCount() - 2, -getSegmentHeight());
	}

	private void splitBottom() {
		if (bottomRadius > 0)
			return;

		splitFace(mesh.getFaceCount() - 1, getSegmentHeight());
	}

	private void splitFace(int faceIndex, float offsetY) {
		int index = mesh.getVertexCount();
		Face3D face = mesh.getFaceAt(faceIndex);
		CenterSplitFaceModifier modifier = new CenterSplitFaceModifier();
		FaceSelection selection = new FaceSelection(mesh);
		selection.add(face);
		modifier.modify(selection);
		mesh.getVertexAt(index).addLocal(0, offsetY, 0);
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private boolean notNeedToCreateVerticesAndFaces() {
		return topAndBottomRadiusAreZero() || heightIsZero() || rotationSegmentsAreZero() || heightSegmentsAreZero();
	}

	private boolean topAndBottomRadiusAreZero() {
		return topRadius == 0 && bottomRadius == 0;
	}

	private boolean heightIsZero() {
		return height == 0;
	}

	private boolean rotationSegmentsAreZero() {
		return rotationSegments == 0;
	}

	private boolean heightSegmentsAreZero() {
		return heightSegments == 0;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();

		if (notNeedToCreateVerticesAndFaces())
			return mesh;

		createVertices();
		createQuadFaces();
		createTopCap();
		createBottomCap();
		splitTop();
		splitBottom();

		return mesh;
	}

	public int getRotationSegments() {
		return rotationSegments;
	}

	public void setRotationSegments(int rotationSegments) {
		this.rotationSegments = rotationSegments;
	}

	public int getHeightSegments() {
		return heightSegments;
	}

	public void setHeightSegments(int heightSegments) {
		this.heightSegments = heightSegments;
	}

	public float getTopRadius() {
		return topRadius;
	}

	public void setTopRadius(float topRadius) {
		this.topRadius = topRadius;
	}

	public float getBottomRadius() {
		return bottomRadius;
	}

	public void setBottomRadius(float bottomRadius) {
		this.bottomRadius = bottomRadius;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

}
