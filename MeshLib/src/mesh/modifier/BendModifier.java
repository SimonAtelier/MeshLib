package mesh.modifier;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;

public class BendModifier implements IMeshModifier {

	private float factor;

	public BendModifier(float factor) {
		this.factor = factor;
	}

	public void simpleDeformBend(float factor, Vector3f v) {
		float x = v.x, y = v.y, z = v.z;
		float theta, sinTheta, cosTheta;

		theta = x * factor;
		sinTheta = Mathf.sin(theta);
		cosTheta = Mathf.cos(theta);

		if (Mathf.abs(factor) > 1e-7f) {
			v.x = -(y - 1.0f / factor) * sinTheta;
			v.y = (y - 1.0f / factor) * cosTheta + 1.0f / factor;
			v.z = z;
		}
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Vector3f v = mesh.getVertexAt(i);
			simpleDeformBend(factor, v);
		}
		return mesh;
	}

	public float getFactor() {
		return factor;
	}

	public void setFactor(float factor) {
		this.factor = factor;
	}

}
