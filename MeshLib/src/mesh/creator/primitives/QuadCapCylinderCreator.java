package mesh.creator.primitives;

import java.util.ArrayList;
import java.util.List;

import math.Mathf;
import math.Vector3f;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.special.AppendCreator;
import mesh.modifier.FlipFacesModifier;
import mesh.modifier.SpherifyModifier;
import mesh.wip.Mesh3DUtil;

public class QuadCapCylinderCreator implements IMeshCreator {

	private int vertices;
	private int segments;
	private int capRows;
	private int capCols;
	private float radius;
	private float height;
	private Mesh3D mesh;

	public QuadCapCylinderCreator() {
		this(32, 1, 1, 2);
	}
	
	public QuadCapCylinderCreator(int vertices, int segments, float radius, float height) {
		this.vertices = vertices;
		this.segments = segments;
		this.radius = radius;
		this.height = height;
		capRows = vertices / 4;
		capCols = (vertices + 2) / 4;
		validateParameters();
	}
	
	protected void validateParameters() {
		if (vertices % 2 == 1)
			throw new IllegalArgumentException("The number of vertices must be even.");
		if (vertices < 4)
			throw new IllegalArgumentException("The number of vertices must be greater or equal to 4.");
	}
	
	private List<Vector3f> getBorderVerticesFromGrid(Mesh3D grid) {
		float valueX = Mathf.abs(grid.getVertexAt(0).x);
		float valueZ = Mathf.abs(grid.getVertexAt(0).z);
		
		List<Vector3f> vertices0 = new ArrayList<Vector3f>();
		List<Vector3f> vertices1 = new ArrayList<Vector3f>();
		List<Vector3f> vertices2 = new ArrayList<Vector3f>();
		List<Vector3f> vertices3 = new ArrayList<Vector3f>();

		for (int i = 0; i < grid.getVertexCount(); i++) {
			Vector3f v = grid.getVertexAt(i);
			if (v.z == -valueZ) {
				vertices0.add(v);
			}
			if (v.x == valueX) {
				vertices1.add(v);
			}
			if (v.z == valueZ) {
				vertices2.add(v);
			}
			if (v.x == -valueX) {
				vertices3.add(v);
			}
		}

		vertices1.remove(0);
		vertices2.remove(vertices2.size() - 1);
		vertices0.addAll(vertices1);

		for (int i = vertices2.size() - 1; i > 0; i--) {
			vertices0.add(vertices2.get(i));
		}

		for (int i = vertices3.size() - 1; i > 0; i--) {
			vertices0.add(vertices3.get(i));
		}
		
		return vertices0;
	}
	
	private void flatten(Mesh3D mesh, float y) {
		for (Vector3f v : mesh.getVertices()) {
			v.setY(y);
		}
	}
	
	private Mesh3D createCap() {
		float a = Mathf.TWO_PI / vertices;
		Mesh3D mesh = new CircleCreator(vertices, radius, -height / 2).create();
		Mesh3D grid = new GridCreator(capCols, capRows, 1).create().translateY(-1);
		
		mesh.rotateY(Mathf.HALF_PI + (capCols % 2 == 1 ? -a / 2f : 0));
		
		List<Vector3f> gridTopBorderVertices = getBorderVerticesFromGrid(grid);
		
		new SpherifyModifier(radius).modify(grid);

		flatten(grid, -height / 2f);
		mesh = new AppendCreator(mesh, grid).create();
		
		int idx = ((capCols / 2) + 1) - (capCols % 2 == 1 ? 0 : 1);
		for (int i = 0; i < gridTopBorderVertices.size(); i++) {
			Vector3f v0 = mesh.getVertexAt(i);
			Vector3f v1 = mesh.getVertexAt((i + 1) % vertices);
			Vector3f v3 = gridTopBorderVertices.get((idx + i + 1) % gridTopBorderVertices.size());
			Vector3f v2 = gridTopBorderVertices.get((idx + i) % gridTopBorderVertices.size());
			Mesh3DUtil.bridge(mesh, v0, v1, v2, v3);
		}
		
		return mesh;
	}

	@Override
	public Mesh3D create() {
		
		mesh = new Mesh3D();
		
		Mesh3D top = createCap();
		
		Mesh3D bottom = top.copy();
		bottom.translateY(height);
		new FlipFacesModifier().modify(bottom);

		List<Mesh3D> meshes = new ArrayList<Mesh3D>();
		
		meshes.add(top);
		mesh = new AppendCreator(mesh, top).create();
		
		float segmentHeight = height / segments;
		for (int i = 0; i < segments - 1; i++) {
			float a = Mathf.TWO_PI / vertices;
			Mesh3D circle = new CircleCreator(vertices, radius, segmentHeight + i * segmentHeight - height / 2f).create();
			circle.rotateY(Mathf.HALF_PI + (capCols % 2 == 1 ? -a / 2f : 0));
			meshes.add(circle);
			mesh = new AppendCreator(mesh, circle).create();
		}
		
		meshes.add(bottom);
		mesh = new AppendCreator(mesh, bottom).create();
		
		for (int j = 0; j < meshes.size() - 1; j++) {
			Mesh3D mesh0 = meshes.get(j);
			Mesh3D mesh1 = meshes.get(j + 1);
			for (int i = 0; i < vertices; i++) {
				Vector3f v0 = mesh0.getVertexAt(i);
				Vector3f v1 = mesh1.getVertexAt(i);
				Vector3f v2 = mesh0.getVertexAt((i + 1) % vertices);
				Vector3f v3 = mesh1.getVertexAt((i + 1) % vertices);
				Mesh3DUtil.bridge(mesh, v0, v1, v2, v3);
			}
		}
	
		return mesh;
	}

}
