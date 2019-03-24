package mesh.wip;

import math.Vector3f;
import mesh.Mesh3D;

public class Morpher {

	private int count;
	private Mesh3D mesh;
	private Mesh3D source;
	private Mesh3D target;
	private boolean finished;

	public Morpher(Mesh3D source, Mesh3D target) {
		this.source = source;
		this.target = target;
		this.mesh = source.copy();
	}

	public void update() {
		if (finished)
			return;

		for (int i = 0; i < source.getVertexCount(); i++) {
			Vector3f v0 = mesh.getVertexAt(i);
			Vector3f target = this.target.getVertexAt(i);

			float speed = 1;
			Vector3f position = new Vector3f(v0);
			Vector3f tmp = new Vector3f(position);
			Vector3f velocity = new Vector3f();

			velocity.setX(position.getX() < target.getX() ? speed : -speed);
			velocity.setY(position.getY() < target.getY() ? speed : -speed);
			velocity.setZ(position.getZ() < target.getZ() ? speed : -speed);
			tmp.addLocal(velocity.mult(1));

			if (position.getX() <= target.getX() && tmp.getX() >= target.getX()) {
				tmp.setX(target.getX());
			}

			if (position.getX() >= target.getX() && tmp.getX() <= target.getX()) {
				tmp.setX(target.getX());
			}

			if (position.getY() <= target.getY() && tmp.getY() >= target.getY()) {
				tmp.setY(target.getY());
			}

			if (position.getY() >= target.getY() && tmp.getY() <= target.getY()) {
				tmp.setY(target.getY());
			}

			if (position.getZ() <= target.getZ() && tmp.getZ() >= target.getZ()) {
				tmp.setZ(target.getZ());
			}

			if (position.getZ() >= target.getZ() && tmp.getZ() <= target.getZ()) {
				tmp.setZ(target.getZ());
			}

			v0.set(tmp.getX(), tmp.getY(), tmp.getZ());

			if (tmp.equals(target)) {
				count++;
			}

			if (count == source.getVertexCount()) {
				finished = true;
			}
		}
		
		count = 0;
	}

	public Mesh3D getMesh() {
		return mesh;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
