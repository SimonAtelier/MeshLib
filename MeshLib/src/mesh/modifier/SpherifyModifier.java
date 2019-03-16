package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.wip.Mesh3DUtil;

public class SpherifyModifier implements IMeshModifier {

	private float radius;
	private Vector3f center;
	
	public SpherifyModifier() {
		this(1f, new Vector3f());
	}
	
	public SpherifyModifier(float radius) {
		this(radius, new Vector3f());
	}
	
	public SpherifyModifier(float radius, Vector3f center) {
		this.radius = radius;
		this.center = center;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setCenter(Vector3f center) {
		this.center.set(center);
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		Mesh3DUtil.pushToSphere(mesh, center, radius);
		return mesh;
	}
	
}
