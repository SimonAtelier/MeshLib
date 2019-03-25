package mesh.modifier;

import math.Vector3f;
import mesh.Mesh3D;

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
		Vector3f origin = new Vector3f(center);
		for (Vector3f v : mesh.getVertices()) {
			Vector3f v0 = new Vector3f(v.x - origin.x, v.y - origin.y, v.z - origin.z).normalizeLocal();
			v.set(v0.mult(radius).add(origin));
		}
		return mesh;
	}
	
}
