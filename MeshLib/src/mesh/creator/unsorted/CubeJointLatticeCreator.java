package mesh.creator.unsorted;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.creator.special.AppendCreator;
import mesh.wip.BridgeFaces;
import mesh.wip.FaceExtrude;

public class CubeJointLatticeCreator implements IMeshCreator {

	private int subdivisionsX;
	private int subdivisionsY;
	private float tileSizeX;
	private float tileSizeY;
	private float jointSize;
	private float scaleX;
	private float scaleY;
	
	public CubeJointLatticeCreator() {
		this.subdivisionsX = 10;
		this.subdivisionsY = 10;
		this.tileSizeX = 0.1f;
		this.tileSizeY = 0.1f;
		this.jointSize = 0.01f;
		this.scaleX = 0.5f;
		this.scaleY = 0.5f;
	}
	
	public CubeJointLatticeCreator(int subdivisionsX, int subdivisionsY,
			float tileSizeX, float tileSizeY, float jointSize, float scaleX,
			float scaleY) {
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsY = subdivisionsY;
		this.tileSizeX = tileSizeX;
		this.tileSizeY = tileSizeY;
		this.jointSize = jointSize;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();
		Mesh3D[][] cubes = new Mesh3D[subdivisionsY + 1][subdivisionsX + 1];
		
		// Create joints
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[0].length; j++) {
				cubes[i][j] = new CubeCreator(jointSize).create();
				cubes[i][j].translate(j * tileSizeX, i * tileSizeY, 0);
				mesh = new AppendCreator(mesh, cubes[i][j]).create();
			}
		}

		// Connect joints
		for (int i = 0; i < cubes.length; i++) {
			for (int j = 0; j < cubes[0].length; j++) {

				if ((j + 1) < cubes[0].length) {
					Face3D f0 = cubes[i][j].getFaceAt(2); // right
					Face3D f1 = cubes[i][j + 1].getFaceAt(4); // left
					FaceExtrude.extrudeFace(mesh, f0, scaleX, 0.0f);
					FaceExtrude.extrudeFace(mesh, f1, scaleX, 0.0f);
					f1.flipDirection();
					BridgeFaces.bridge(mesh, f0, f1);
					mesh.removeFace(f0);
					mesh.removeFace(f1);
				}

				if ((i + 1) < cubes.length) {
					Face3D f2 = cubes[i][j].getFaceAt(1); // bottom
					Face3D f3 = cubes[i + 1][j].getFaceAt(0); // top
					FaceExtrude.extrudeFace(mesh, f2, scaleY, 0.0f);
					FaceExtrude.extrudeFace(mesh, f3, scaleY, 0.0f);
					f3.flipDirection();
					BridgeFaces.bridge(mesh, f2, f3);
					mesh.removeFace(f2);
					mesh.removeFace(f3);
				}
			}
		}

		mesh.translate(-subdivisionsX * tileSizeX / 2f, -subdivisionsY * tileSizeY / 2f, 0);

		return mesh;
	}

	public int getSubdivisionsX() {
		return subdivisionsX;
	}

	public void setSubdivisionsX(int subdivisionsX) {
		this.subdivisionsX = subdivisionsX;
	}

	public int getSubdivisionsY() {
		return subdivisionsY;
	}

	public void setSubdivisionsY(int subdivisionsY) {
		this.subdivisionsY = subdivisionsY;
	}

	public float getTileSizeX() {
		return tileSizeX;
	}

	public void setTileSizeX(float tileSizeX) {
		this.tileSizeX = tileSizeX;
	}

	public float getTileSizeY() {
		return tileSizeY;
	}

	public void setTileSizeY(float tileSizeY) {
		this.tileSizeY = tileSizeY;
	}

	public float getJointSize() {
		return jointSize;
	}

	public void setJointSize(float jointSize) {
		this.jointSize = jointSize;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
}
