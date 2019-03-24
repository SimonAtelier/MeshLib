package mesh.creator.special;

import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class AppendCreator implements IMeshCreator {
	
	private Mesh3D[] meshes;
	
	public AppendCreator(Mesh3D... meshes) {
		this.meshes = meshes;
	}

	@Override
	public Mesh3D create() {
		return append();
	}
	
	private Mesh3D append() {
		// FIXME copy vertices and faces
		int n = 0;
		Mesh3D mesh = new Mesh3D();
		List<Vector3f> vertices = mesh.vertices;
		List<Face3D> faces = mesh.faces;

		for (int i = 0; i < meshes.length; i++) {
			Mesh3D m = meshes[i];
			vertices.addAll(m.vertices);
			faces.addAll(meshes[i].faces);
			for (Face3D f : meshes[i].faces) {
				for (int j = 0; j < f.indices.length; j++) {
					f.indices[j] += n;
				}
			}
			n += m.getVertexCount();
		}

		return mesh;
	}
	
}
