package mesh.creator.unsorted;

import java.util.HashSet;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.GridCreator;
import mesh.creator.special.AppendCreator;

public class SegmentedCubeCreator implements IMeshCreator {

	private int segments;
	private float size;
	private float creationSize;
	private Mesh3D mesh;

	public SegmentedCubeCreator() {
		this.segments = 10;
		this.size = 1f;
		this.creationSize = (float) segments;
	}

	public SegmentedCubeCreator(int segments, float size) {
		this.segments = segments;
		this.size = size;
		this.creationSize = (float) segments;
	}

	private void createTop() {
		Mesh3D top = new GridCreator(segments, segments, creationSize).create();
		top.translateY(-creationSize);
		mesh = new AppendCreator(mesh, top).create();
	}

	private void createBottom() {
		Mesh3D bottom = new GridCreator(segments, segments, creationSize)
				.create();
		bottom.rotateX(Mathf.toRadians(180));
		bottom.translateY(creationSize);
		mesh = new AppendCreator(mesh, bottom).create();
	}

	private void createFront() {
		Mesh3D front = new GridCreator(segments, segments, creationSize)
				.create();
		front.rotateX(Mathf.HALF_PI);
		front.translateZ(-creationSize);
		mesh = new AppendCreator(mesh, front).create();
	}

	private void createBack() {
		Mesh3D front = new GridCreator(segments, segments, creationSize)
				.create();
		front.rotateX(-Mathf.HALF_PI);
		front.translateZ(creationSize);
		mesh = new AppendCreator(mesh, front).create();
	}

	private void createLeft() {
		Mesh3D front = new GridCreator(segments, segments, creationSize)
				.create();
		front.rotateZ(-Mathf.HALF_PI);
		front.translateX(-creationSize);
		mesh = new AppendCreator(mesh, front).create();
	}

	private void createRight() {
		Mesh3D front = new GridCreator(segments, segments, creationSize)
				.create();
		front.rotateZ(Mathf.HALF_PI);
		front.translateX(creationSize);
		mesh = new AppendCreator(mesh, front).create();
	}

	private void roundVertices() {
		for (int i = 0; i < mesh.getVertexCount(); i++) {
			Vector3f v = mesh.getVertexAt(i);
			v.set(Mathf.round(v.x), Mathf.round(v.y), Mathf.round(v.z));
		}
	}

	private void removeDoubles() {
		Mesh3D m = new Mesh3D();
		HashSet<Vector3f> vertexSet = new HashSet<Vector3f>();
		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				vertexSet.add(v);
			}
		}
		m.vertices.addAll(vertexSet);
		for (Face3D f : mesh.faces) {
			for (int i = 0; i < f.indices.length; i++) {
				Vector3f v = mesh.getVertexAt(f.indices[i]);
				int index = m.indexOf(v);
				f.indices[i] = index;
			}
			m.add(f);
		}
		this.mesh = m;
	}

	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		createTop();
		createBottom();
		createFront();
		createBack();
		createLeft();
		createRight();
		roundVertices();
		removeDoubles();
		mesh.scale(1.0f / creationSize, 1.0f / creationSize,
				1.0f / creationSize);
		mesh.scale(size);
		return mesh;
	}

}
