package mesh.creator.wip;

import java.util.ArrayList;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CircleCreator;
import mesh.modifier.SolidifyModifier;
import mesh.wip.Mesh3DUtil;

public class PlateCreator implements IMeshCreator {
	
	private int vertices = 48;
	private int innerSegments = 4;
	private float radius0 = 3.0f;
	private float radius1 = 4.0f;
	private float innerSegmentHeight = 0.2f;
	private float outerSegmentHeight = 0.3f;
	private float thickness = 0.1f;
	private Mesh3D mesh;
	private List<Mesh3D> innerCircles = new ArrayList<Mesh3D>();	
	
	private void createInnerVertices() {
		float segmentRadius = radius0 / innerSegments; 
		for (int i = 0; i < innerSegments; i++) {
			Mesh3D circle = new CircleCreator(vertices, (i + 1) * segmentRadius).create();
			mesh = Mesh3DUtil.append(mesh, circle);
			innerCircles.add(circle);
		}
	}
	
	private void createInnerFaces() {
		createTriangleFan();
		for (int i = 0; i < innerCircles.size() - 1; i++) {
			Mesh3D circle0 = innerCircles.get(i);
			Mesh3D circle1 = innerCircles.get(i + 1);
			Mesh3DUtil.bridge(mesh, circle0, circle1);
		}
	}
	
	private void createTriangleFan() {
		int index = mesh.getVertexCount();
		mesh.vertices.add(new Vector3f(0, 0, 0));
		for (int i = 0; i < vertices; i++) {
			Face3D face = new Face3D(i % vertices, (i + 1) % vertices, index);
			mesh.faces.add(face);
		}
	}
	
	private void createOuter() {
		Mesh3D c = new CircleCreator(vertices, radius0, innerSegmentHeight).create();
		Mesh3D c1 = new CircleCreator(vertices, radius1, outerSegmentHeight).create();
		mesh = Mesh3DUtil.append(mesh, c, c1);
		Mesh3DUtil.bridge(mesh, innerCircles.get(innerCircles.size() - 1), c);
		Mesh3DUtil.bridge(mesh, c, c1);
	}
	
	@Override
	public Mesh3D create() {
		mesh = new Mesh3D();
		
		createInnerVertices();
		createInnerFaces();
		createOuter();
		
		mesh.rotateX(Mathf.PI);
		
//		new HolesModifier().modify(mesh);
			
		new SolidifyModifier(thickness).modify(mesh);
		
//		new CatmullClarkModifier(2).modify(mesh);
		
		return mesh;
	}

}
