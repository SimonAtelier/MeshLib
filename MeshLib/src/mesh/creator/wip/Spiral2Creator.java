package mesh.creator.wip;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class Spiral2Creator implements IMeshCreator {

	@Override
	public Mesh3D create() {

		Mesh3D mesh = new Mesh3D();

		int n = 100;

		float r = 2f;
		float r1 = 5;

		float a = 0.1f;

		float angle = 0;
		float step = Mathf.TWO_PI / ((r1 - r) * 10);

		for (int i = 0; i < n; i++) {
			float x = r * Mathf.cos(angle);
			float y = r * Mathf.sin(angle);
			float z = 0;

			float x1 = r1 * Mathf.cos(angle);
			float y1 = r1 * Mathf.sin(angle);
			float z1 = 0;

			Vector3f v = new Vector3f(x, y, z);
			mesh.add(v);

			Vector3f v1 = new Vector3f(x1, y1, z1);
			mesh.add(v1);

			angle += step;

			r += a;
			r1 += a;
		}

		for (int i = 0; i < n * 2 - 3; i += 2) {
			Face3D f = new Face3D(i, i + 1, i + 3, i + 2);
			mesh.add(f);
		}

		return mesh;

	}

}
