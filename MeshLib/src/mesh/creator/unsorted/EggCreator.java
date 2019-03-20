package mesh.creator.unsorted;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.FlatTopPyramidCreator;
import mesh.modifier.subdivision.CatmullClarkModifier;

public class EggCreator implements IMeshCreator {

	private float size;
	private float topScale;
	private int subdivisions;
	private Mesh3D mesh;
	
	public EggCreator() {
		this.size = 1f;
		this.topScale = 0.5f;
		this.subdivisions = 3;
	}
	
	public EggCreator(float size, float topScale, int subdivisions) {
		this.size = size;
		this.topScale = topScale;
		this.subdivisions = subdivisions;
	}
	
	private void createFlatTopPyramid() {
		FlatTopPyramidCreator creator = new FlatTopPyramidCreator();
		creator.setBottomRadius(size);
		creator.setTopRadius(topScale);
		mesh = creator.create();
	}
	
	private void subdividesFlatTopPyramid() {
		new CatmullClarkModifier(subdivisions).modify(mesh);
	}

	@Override
	public Mesh3D create() {
		createFlatTopPyramid();
		subdividesFlatTopPyramid();
		return mesh;
	}
	
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getTopScale() {
		return topScale;
	}

	public void setTopScale(float topScale) {
		this.topScale = topScale;
	}

	public int getSubdivisions() {
		return subdivisions;
	}

	public void setSubdivisions(int subdivisions) {
		this.subdivisions = subdivisions;
	}
	
}
