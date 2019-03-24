package mesh.wip;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class FaceExtrude {
	
	public static void extrudeFace(Mesh3D mesh, Face3D f, float scale, float amount) {
		int n = f.indices.length;
		int idx = mesh.getVertexCount();
		Vector3f normal = mesh.calculateFaceNormal(f);
		Vector3f center = mesh.calculateFaceCenter(f);

		normal.multLocal(amount);

		for (int i = 0; i < n; i++) {
			Vector3f v0 = mesh.getVertexAt(f.indices[i]);
			Vector3f v1 = new Vector3f(v0).subtract(center).mult(scale).add(center);

			v1.addLocal(normal);
			mesh.add(v1);
		}

		for (int i = 0; i < n; i++) {
			Face3D f0 = new Face3D(f.indices[i], f.indices[(i + 1) % n], idx + ((i + 1) % n), idx + i);
			mesh.add(f0);
		}

		for (int i = 0; i < n; i++) {
			f.indices[i] = idx + i;
		}
	}
	
}
