package mesh.modifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;

public class SmoothModifier implements IMeshModifier {

	private float factor;
	private List<Vector3f> smoothedVertices;
	private HashMap<Vector3f, List<Vector3f>> map;

	public SmoothModifier() {
		this.factor = 0.5f;
		this.smoothedVertices = new ArrayList<>();
		this.map = new HashMap<Vector3f, List<Vector3f>>();
	}

	protected Vector3f getSmoothedVertex(Vector3f v) {
		Vector3f a = new Vector3f(v);
		List<Vector3f> list = map.get(v);
		
		for (Vector3f v0 : list) {
			a.addLocal(v0);
		}
		
		a.divideLocal(list.size());
		return a.mult(factor).add(v.mult(1f - factor));
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		for (Face3D face : mesh.getFaces()) {
			int n = face.indices.length;
			for (int i = 0; i < face.indices.length; i++) {
				Vector3f edgeStartPoint = mesh.getVertexAt(face.indices[i]);
				Vector3f edgeEndPoint = mesh.getVertexAt(face.indices[(i + 1) % n]);
				List<Vector3f> list = map.get(edgeStartPoint);
				if (list == null) {
					list = new ArrayList<Vector3f>();
					map.put(edgeStartPoint, list);
				}
				list.add(edgeEndPoint);
			}
		}

		for (Vector3f v : mesh.getVertices()) {
			smoothedVertices.add(getSmoothedVertex(v));
		}

		mesh.clearVertices();
		mesh.addVertices(smoothedVertices);

		map.clear();
		smoothedVertices.clear();

		return mesh;
	}

}
