package mesh.creator.wip;

import java.util.ArrayList;
import java.util.List;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.TubeCreator;
import mesh.wip.Mesh3DUtil;

public class ChainCreator implements IMeshCreator {

	private int n;
	private int vertices;
	private List<Mesh3D> meshes;
	Mesh3D mesh;
	
	public ChainCreator() {
		super();
		this.n = 20;
		this.vertices = 64;
		this.meshes = new ArrayList<Mesh3D>();
	}
	
	private void createMeshes() {
		for (int i = 0; i < n; i++) {
			Mesh3D mesh = new TubeCreator(vertices, 1f, 0.9f, 1f, 0.9f, 0.2f).create();
			mesh.translateX(i);
			if (i % 2 == 0)
				mesh.rotateX(Mathf.toRadians(90));
			meshes.add(mesh);
			this.mesh = Mesh3DUtil.append(this.mesh, mesh);
		}
	}
	
	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createMeshes();
//		mesh.translateX(-n / 2f + 0.5f);
		return mesh;
	}
	
}
