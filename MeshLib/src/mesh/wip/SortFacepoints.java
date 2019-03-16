package mesh.wip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class SortFacepoints implements Comparator<Integer> {

	private Vector3f center;
	private Vector3f normal;
	private Mesh3D mesh;

	public void sort(Mesh3D mesh, Face3D face) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < face.indices.length; i++) {
			list.add(face.indices[i]);
		}

		this.center = Mesh3DUtil.calculateFaceCenter(mesh, face);
		this.normal = Mesh3DUtil.calculateFaceNormal(mesh, face);
		this.mesh = mesh;

		Collections.sort(list, this);
		
		for (int i = 0; i < face.indices.length; i++) {
			face.indices[i] = list.get(i);
		}
	}

	@Override
	public int compare(Integer a, Integer b) {
		Vector3f v0 = mesh.vertices.get(a);
		Vector3f v1 = mesh.vertices.get(b);
		return compare(v0, v1);
	}
	
	protected int compare(Vector3f a, Vector3f b) {
		// http://stackoverflow.com/questions/14370636/sorting-a-list-of-3d-coplanar-points-to-be-clockwise-or-counterclockwise
		// You have the center C and the normal n. To determine whether point B
		// is clockwise or counterclockwise from point A, calculate dot(n,
		// cross(A-C, B-C)). If the result is positive, B is counterclockwise
		// from A; if it's negative, B is clockwise from A.
				
		float d = center.dot(a.subtract(center).cross(b.subtract(center)));
		
		if (d < 0)
			return 1;
		if (d > 0)
			return -1;

		return 0;
	}

}
