package mesh.creator.primitives;

import mesh.Mesh;
import mesh.creator.IMeshCreator;

public class CubeCreator implements IMeshCreator {

	@Override
	public Mesh create() {
		Mesh mesh = new Mesh();		
		float radius = 1;
		
		mesh.addVertex(radius, -radius, -radius);
		mesh.addVertex(radius, -radius, radius);
		mesh.addVertex(-radius, -radius, radius);
		mesh.addVertex(-radius, -radius, -radius);
		mesh.addVertex(radius, radius, -radius);
		mesh.addVertex(radius, radius, radius);
		mesh.addVertex(-radius, radius, radius);
		mesh.addVertex(-radius, radius, -radius);
		
		mesh.addFace(3, 0, 1, 2);
		mesh.addFace(6, 5, 4, 7);
		mesh.addFace(1, 0, 4, 5);
		mesh.addFace(1, 5, 6, 2);
		mesh.addFace(6, 7, 3, 2);
		mesh.addFace(3, 7, 4, 0);
		
		return mesh;
	}

}
