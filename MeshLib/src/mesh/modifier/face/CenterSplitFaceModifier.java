package mesh.modifier.face;

import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.wip.FaceSelection;
import mesh.wip.Mesh3DUtil;

public class CenterSplitFaceModifier {

	public void modify(FaceSelection selection) {
		for (Face3D face : selection.getFaces()) {
			centerSplit(selection.getMesh(), face);
		}
	}
	
	private void centerSplit(Mesh3D mesh, Face3D f) {
		int index = mesh.getVertexCount();
		int n = f.indices.length;
		List<Face3D> toAdd = new ArrayList<Face3D>();
		Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, f);
		mesh.add(center);
		for (int i = 0; i < f.indices.length; i++) {
			Face3D f1 = new Face3D(f.indices[i % n], f.indices[(i + 1) % n], index);
			toAdd.add(f1);
		}
		mesh.faces.addAll(toAdd);
		mesh.faces.remove(f);
	}
	
}
