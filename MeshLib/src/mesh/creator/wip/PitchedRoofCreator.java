package mesh.creator.wip;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;

public class PitchedRoofCreator implements IMeshCreator {

	private float width;
	private float height;
	private float depth;
	private float thickness;
	private Mesh3D mesh;
	
	public PitchedRoofCreator() {
		width = 4;
		height = 4;
		depth = 6;
		thickness = 0.1f;
	}
	
	private void createVertices() {
		float widthHalf = width * 0.5f;
		float heightHalf = height * 0.5f;
		float depthHalf = depth * 0.5f;
		
		mesh.addVertex(-widthHalf, heightHalf, -depthHalf);
		mesh.addVertex(widthHalf, heightHalf, -depthHalf);
		mesh.addVertex(widthHalf, heightHalf, depthHalf);
		mesh.addVertex(-widthHalf, heightHalf, depthHalf);
		
		mesh.addVertex(0, -heightHalf, -depthHalf);
		mesh.addVertex(0, -heightHalf, depthHalf);
		
		mesh.addVertex(0, -heightHalf + thickness, -depthHalf);
		mesh.addVertex(0, -heightHalf + thickness, depthHalf);
		
		mesh.addFace(5, 4, 1, 2);
		mesh.addFace(4, 5, 3, 0);
//		mesh.addFace(5, 2, 3);
//		mesh.addFace(4, 0, 1);
		

	}
	
	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		
		createVertices();
		
		return mesh;
	}

}
