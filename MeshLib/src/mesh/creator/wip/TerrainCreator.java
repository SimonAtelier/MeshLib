package mesh.creator.wip;

import math.ImprovedNoise;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class TerrainCreator implements IMeshCreator {

	private float y;
	private float heightFactor;
	private int subdivisionsX;
	private int subdivisionsZ;
	private float tileSizeX;
	private float tileSizeZ;

	public TerrainCreator() {
		super();
		this.heightFactor = 1f;
		this.subdivisionsX = 10;
		this.subdivisionsZ = 10;
		this.tileSizeX = 0.1f;
		this.tileSizeZ = 0.1f;
	}

	public TerrainCreator(int subdivisionsX, int subdivisionsZ, float size) {
		super();
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsZ = subdivisionsZ;
		this.tileSizeX = (size + size) / subdivisionsX;
		this.tileSizeZ = (size + size) / subdivisionsZ;
	}

	public TerrainCreator(int subdivisionsX, int subdivisionsZ,
			float tileSizeX, float tileSizeZ) {
		super();
		this.subdivisionsX = subdivisionsX;
		this.subdivisionsZ = subdivisionsZ;
		this.tileSizeX = tileSizeX;
		this.tileSizeZ = tileSizeZ;
	}

	@Override
	public Mesh3D create() {
		float offsetX = (tileSizeX * subdivisionsX) / 2f;
		float offsetZ = (tileSizeZ * subdivisionsZ) / 2f;
		Mesh3D mesh = new Mesh3D();

		float x = 0;

		// Create vertices
		for (int i = 0; i < subdivisionsZ + 1; i++) {
			for (int j = 0; j < subdivisionsX + 1; j++) {
				Vector3f v = new Vector3f(j * tileSizeX, 0, i * tileSizeZ);
				v.subtractLocal(offsetX, (float)ImprovedNoise.noise(x, y, 0) * heightFactor, offsetZ);				
				mesh.add(v);
				x += 0.05f;
			}
			x = 0;
			y += 0.05f;
		}

		// Create faces
		for (int i = 0; i < subdivisionsZ; i++) {
			for (int j = 0; j < subdivisionsX; j++) {
				int index = i * (subdivisionsX + 1) + j;
				Face3D face0 = new Face3D(index, index + 1, index
						+ subdivisionsX + 1);
				Face3D face1 = new Face3D(index + 1, index + subdivisionsX + 2,
						index + subdivisionsX + 1);
				mesh.faces.add(face0);
				mesh.faces.add(face1);
			}
		}

		return mesh;
	}
	
	public void setNoiseY(float y) {
		this.y = y;
	}
	
	public float getMultiplierY() {
		return heightFactor;
	}

	public void setHeightFactor(float multiplierY) {
		this.heightFactor = multiplierY;
	}

	public int getSubdivisionsX() {
		return subdivisionsX;
	}

	public void setSubdivisionsX(int subdivisionsX) {
		this.subdivisionsX = subdivisionsX;
	}

	public int getSubdivisionsZ() {
		return subdivisionsZ;
	}

	public void setSubdivisionsZ(int subdivisionsZ) {
		this.subdivisionsZ = subdivisionsZ;
	}

	public float getTileSizeX() {
		return tileSizeX;
	}

	public void setTileSizeX(float tileSizeX) {
		this.tileSizeX = tileSizeX;
	}

	public float getTileSizeZ() {
		return tileSizeZ;
	}

	public void setTileSizeZ(float tileSizeZ) {
		this.tileSizeZ = tileSizeZ;
	}

}
