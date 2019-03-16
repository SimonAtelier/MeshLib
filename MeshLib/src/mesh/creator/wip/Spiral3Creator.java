package mesh.creator.wip;

import java.util.ArrayList;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.wip.Mesh3DUtil;

public class Spiral3Creator implements IMeshCreator {

	@Override
	public Mesh3D create() {


		
		Mesh3D mesh = new Mesh3D();

		int n = 16;
		int m = 5;

		float r = 0.3f;

		float angle = 0;
		float step = Mathf.TWO_PI / n;

		
		float sR = 0f;
		

		
//		for (int j = 0; j < m; j++) {
			for (int i = 0; i < n; i++) {
				float x = Mathf.cos(angle);
				float y = Mathf.sin(angle);
				float z = 0;
				Vector3f v0 = new Vector3f(x, y, z);
				v0.multLocal(r);
				mesh.add(v0);
				mesh.add(new Vector3f(x, y, z).mult(0.5f));
				angle += step;
			}
//			angle = 0;
//			r += 0.1;
//		}

		for (int i = 0; i < n * 2 - 3; i += 2) {
			Face3D f = new Face3D(i, i + 1, i + 3, i + 2);
			mesh.add(f);
		}
		
		angle = 0;
		step = Mathf.TWO_PI / n;
		List<Vector3f> verts = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			sR += 0.05f;
			float x = Mathf.cos(angle);
			float y = Mathf.sin(angle);
			float z = 0;
			Vector3f v0 = new Vector3f(x, y, z);
			verts.add(v0.multLocal(sR));
			
			angle += step;
			
		}
		
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (int i = 0; i < faces.size(); i++) {
			Vector3f v = verts.get(i);
			Face3D f = faces.get(i);
//			Mesh3DUtil.extrudeFace(mesh, f, 0.5f, 0f);
			Mesh3DUtil.translateFace(mesh, f, v);
//			Mesh3DUtil.translateFace(mesh, f, v);
		}
		
		
		
		return mesh;
		
		
		

	}

}
