package mesh.modifier.subdivision;

import java.util.ArrayList;

import math.GeometryUtil;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.wip.Mesh3DUtil;

/**
 * Divides the face (polygon) from its center to the middle of each edge. This
 * modifier works for faces with n vertices. The resulting mesh consists of
 * quads.
 * 
 * <pre>
 * o-------------------o      o---------o---------o
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      o---------o---------o
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      |         .         |
 * |                   |      |         .         |
 * o-------------------o      o---------o---------o  
 * 
 *           o                          o
 *          / \                        / \
 *         /   \                      /   \
 *        /     \                    /     \
 *       /       \	                /       \
 *      /         \	               o.       .o
 *     /           \              /   .   .   \
 *    /             \            /      o      \
 *   /               \          /       .       \
 *  /                 \        /        .        \
 * o-------------------o      o---------o---------o
 * </pre>
 * 
 * @version 0.1, 20 June 2016
 */
public class TessellationEdgeModifier implements IMeshModifier {

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		int nextIndex = mesh.vertices.size();
		ArrayList<Face3D> toAdd = new ArrayList<>();

		for (Face3D f : mesh.faces) {
			int n = f.indices.length;
			int[] idxs = new int[f.indices.length + 1];
			Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, f);
			mesh.vertices.add(center);
			idxs[0] = nextIndex;
			nextIndex++;

			// Create edge points
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v0 = mesh.vertices.get(f.indices[i % n]);
				Vector3f v1 = mesh.vertices.get(f.indices[(i + 1) % n]);
				Vector3f ep = GeometryUtil.getMidpoint(v0, v1);
				int idx = mesh.vertices.indexOf(ep);
				if (idx > -1) {
					idxs[i + 1] = idx;
				} else {
					mesh.vertices.add(ep);
					idxs[i + 1] = nextIndex;
					nextIndex++;
				}
			}

			// Create faces
			for (int i = 0; i < f.indices.length; i++) {
				Face3D f0 = new Face3D(f.indices[i], idxs[i + 1], idxs[0],
						idxs[i == 0 ? f.indices.length : i]);
				toAdd.add(f0);
			}

		}

		mesh.faces.clear();
		mesh.faces.addAll(toAdd);

		return mesh;
	}

}
