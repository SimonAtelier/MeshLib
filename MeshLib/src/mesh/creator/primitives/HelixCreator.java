package mesh.creator.primitives;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.modifier.face.CenterSplitFaceModifier;
import mesh.wip.FaceSelection;

public class HelixCreator implements IMeshCreator {

	private float majorRadius;
	private float minorRadius;
	private int majorSegments;
	private int minorSegments;
	private int turns;
	private float dy;
	private boolean capEnds;
	
	private Mesh3D mesh;

	public HelixCreator() {
		this.majorRadius = 1.0f;
		this.minorRadius = 0.25f;
		this.majorSegments = 48;
		this.minorSegments = 12;
		this.turns = 4;
		this.dy = 0.6f;
		this.capEnds = true;
	}

	@Override
	public Mesh3D create() {
		initializeMesh();
		Vector3f[] verts = createVertices();
		createFaces(mesh);
		mesh.add(verts);
		if (capEnds)
			createCaps(mesh);
		return mesh;
	}

	private void initializeMesh() {
		mesh = new Mesh3D();
	}

	private Vector3f[] createVertices() {
		float y0 = -(turns * dy) / 2f; // Apply offset to center the helix
		float stepY = dy / (float) majorSegments;
		Vector3f[] verts = new Vector3f[majorSegments * minorSegments * turns];

		for (int n = 0; n < turns; n++) {
			float majorAngle = 0;
			float majorStep = Mathf.TWO_PI / majorSegments;
			for (int j = 0; j < majorSegments; j++) {
				float minorAngle = 0;
				float minorStep = Mathf.TWO_PI / minorSegments;
				Vector3f v0 = new Vector3f(majorRadius * Mathf.cos(majorAngle), y0,
						majorRadius * Mathf.sin(majorAngle));
				for (int i = 0; i < minorSegments; i++) {
					Vector3f v1 = new Vector3f(minorRadius * Mathf.cos(minorAngle), minorRadius * Mathf.sin(minorAngle),
							0);
					// Rotate
					float a = Mathf.TWO_PI - majorAngle;
					float x2 = Mathf.cos(a) * v1.x + Mathf.sin(a) * v1.z;
					float z2 = -Mathf.sin(a) * v1.x + Mathf.cos(a) * v1.z;
					v1.set(x2, v1.y, z2);
					v1.addLocal(v0);
					minorAngle += minorStep;

					verts[n * (majorSegments * minorSegments) + (j * minorSegments + i)] = v1;
				}
				y0 += stepY;
				majorAngle += majorStep;
			}
		}
		return verts;
	}

	private void createCaps(Mesh3D mesh) {
		int n = mesh.getVertexCount() - 1;
		int m = minorSegments - 1;
		Face3D f0 = new Face3D(new int[minorSegments]);
		Face3D f1 = new Face3D(new int[minorSegments]);

		for (int i = 0; i < minorSegments; i++) {
			f0.setIndexAt(m - i, i);
			f1.setIndexAt(m - i, n - i);
		}

		mesh.add(f0);
		mesh.add(f1);
		
		CenterSplitFaceModifier modifier = new CenterSplitFaceModifier();
		FaceSelection selection = new FaceSelection(mesh);
		selection.add(f0);
		selection.add(f1);
		modifier.modify(selection);
	}

	private void createFaces(Mesh3D mesh) {
		int l = majorSegments * turns;
		for (int j = 0; j < majorSegments * turns - 1; j++) {
			for (int i = 0; i < minorSegments; i++) {
				int[] k = new int[] { j % l, (j + 1) % l, i % minorSegments, (i + 1) % minorSegments };
				int index0 = k[1] * minorSegments + k[2];
				int index1 = k[0] * minorSegments + k[2];
				int index2 = k[1] * minorSegments + k[3];
				int index3 = k[0] * minorSegments + k[3];
				Face3D f = new Face3D(index0, index1, index3, index2);
				mesh.add(f);
			}
		}
	}

	public float getMajorRadius() {
		return majorRadius;
	}

	public void setMajorRadius(float majorRadius) {
		this.majorRadius = majorRadius;
	}

	public float getMinorRadius() {
		return minorRadius;
	}

	public void setMinorRadius(float minorRadius) {
		this.minorRadius = minorRadius;
	}

	public int getMajorSegments() {
		return majorSegments;
	}

	public void setMajorSegments(int majorSegments) {
		this.majorSegments = majorSegments;
	}

	public int getMinorSegments() {
		return minorSegments;
	}

	public void setMinorSegments(int minorSegments) {
		this.minorSegments = minorSegments;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public boolean isCap() {
		return capEnds;
	}

	public void setCap(boolean cap) {
		this.capEnds = cap;
	}

}
