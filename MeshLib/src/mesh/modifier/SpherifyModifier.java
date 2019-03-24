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
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Vector3f vertex = mesh.getVertexAt(i);
			Vector3f v0 = new Vector3f(vertex.x - origin.x, vertex.y - origin.y, vertex.z - origin.z).normalizeLocal();
			vertex.set(v0.mult(radius).add(origin));
		}	
		return mesh;
	}
	
}
