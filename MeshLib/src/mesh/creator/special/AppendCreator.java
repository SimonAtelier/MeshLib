package mesh.creator.special;

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

		for (int i = 0; i < meshes.length; i++) {
			Mesh3D m = meshes[i];
			mesh.addVertices(m.getVertices());
			mesh.addFaces(meshes[i].getFaces());
			for (Face3D f : meshes[i].getFaces()) {
				for (int j = 0; j < f.indices.length; j++) {
					f.indices[j] += n;
				}
			}
			n += m.getVertexCount();
		}

		return mesh;
	}
	
}
