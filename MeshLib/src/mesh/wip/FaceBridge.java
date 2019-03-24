package mesh.wip;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class FaceBridge {

	@Deprecated
	public static void bridge(Mesh3D mesh, Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3) {
		int idx0 = mesh.indexOf(v0);
		int idx1 = mesh.indexOf(v1);
		int idx2 = mesh.indexOf(v2);
		int idx3 = mesh.indexOf(v3);
		Face3D face = new Face3D(idx0, idx1, idx3, idx2);
		mesh.faces.add(face);
	}

	@Deprecated
	public static void bridge(Mesh3D mesh, Face3D f0, Face3D f1) {
		Face3D f2 = new Face3D(f0.indices[0], f0.indices[1], f1.indices[1], f1.indices[0]);
		Face3D f3 = new Face3D(f0.indices[1], f0.indices[2], f1.indices[2], f1.indices[1]);
		Face3D f4 = new Face3D(f0.indices[2], f0.indices[3], f1.indices[3], f1.indices[2]);
		Face3D f5 = new Face3D(f0.indices[3], f0.indices[0], f1.indices[0], f1.indices[3]);
		mesh.faces.add(f2);
		mesh.faces.add(f3);
		mesh.faces.add(f4);
		mesh.faces.add(f5);
	}
	
}
