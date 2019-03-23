package mesh.wip;

import math.Vector3f;
import mesh.Mesh3D;
import mesh.math.Bounds3;

public class Mesh3DOperations {

	public static Bounds3 getBounds(Mesh3D mesh) {
		Vector3f min = new Vector3f();
		Vector3f max = new Vector3f();
		Bounds3 bounds = new Bounds3();
		for (Vector3f v : mesh.vertices) {
			min.x = v.x < min.x ? v.x : min.x;
			min.y = v.y < min.y ? v.y : min.y;
			min.z = v.z < min.z ? v.z : min.z;
			max.x = v.x > max.x ? v.x : max.x;
			max.y = v.y > max.y ? v.y : max.y;
			max.z = v.z > max.z ? v.z : max.z;
		}
		bounds.setMinMax(min, max);
		return bounds;
	}
	
}
