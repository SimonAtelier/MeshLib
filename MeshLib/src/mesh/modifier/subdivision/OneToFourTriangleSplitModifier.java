package mesh.modifier.subdivision;

import java.util.ArrayList;
import java.util.List;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;

/**
 * Subdivides a triangle into four new triangles based on the edges of the
 * original triangle. This modifier works for triangles only. Faces with
 * vertices > 3 are ignored by this modifier. If the mesh contains such
 * polygons the use of this modifier can have unexpected results.
 * 
 * <pre>
 *     o                 o
 *    / \               / \ 
 *   /   \    ---->    o---o
 *  /     \           / \ / \
 * o-------o         o---o---o
 * </pre>
 * 
 * @version 0.1, 22 June 2016
 */
public class OneToFourTriangleSplitModifier implements IMeshModifier {

	private List<Face3D> toAdd;
	private List<Face3D> toRemove;
	private Mesh3D mesh;

	public OneToFourTriangleSplitModifier() {
		super();
		toAdd = new ArrayList<>();
		toRemove = new ArrayList<>();
	}

	private void updateFaces() {
		mesh.removeFaces(toRemove);
		mesh.addFaces(toAdd);
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		this.mesh = mesh;
		int nextIndex = mesh.getVertexCount();
		toAdd.clear();
		toRemove.clear();

		for (Face3D f : mesh.getFaces()) {
			// Ignore faces with vertices > 3
			if (f.indices.length > 3)
				continue;

			int n = f.indices.length;
			int[] idxs = new int[f.indices.length + 1];

			// Create edge points
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v0 = mesh.getVertexAt(f.indices[i % n]);
				Vector3f v1 = mesh.getVertexAt(f.indices[(i + 1) % n]);
				Vector3f ep = GeometryUtil.getMidpoint(v0, v1);
				int idx = mesh.indexOf(ep);
				if (idx > -1) {
					idxs[i] = idx;
				} else {
					mesh.add(ep);
					idxs[i] = nextIndex;
					nextIndex++;
				}
			}

			// Create faces
			toAdd.add(new Face3D(idxs[0], idxs[1], idxs[2]));
			toAdd.add(new Face3D(f.indices[0], idxs[0], idxs[2]));
			toAdd.add(new Face3D(f.indices[1], idxs[1], idxs[0]));
			toAdd.add(new Face3D(f.indices[2], idxs[2], idxs[1]));

			toRemove.add(f);
		}

		updateFaces();

		return mesh;
	}

}
