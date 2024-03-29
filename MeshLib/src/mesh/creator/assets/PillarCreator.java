package mesh.creator.assets;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.creator.FillType;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CylinderCreator;
import mesh.wip.FaceSelection;
import mesh.wip.FaceExtrude;

public class PillarCreator implements IMeshCreator {

	private int rotationSegments;
	private int topSegments;
	private int bottomSegments;
	private float topHeight;
	private float bottomHeight;
	private float centerHeight;
	private float radius;
	private Mesh3D mesh;
	private FaceSelection selection;
	
	public PillarCreator() {
		this(8, 3, 2, 1.0f, 2.0f, 5.0f, 1.0f);
	}
	
	public PillarCreator(int rotationSegments, int topSegments, int bottomSegments, float topHeight, float bottomHeight,
			float centerHeight, float radius) {
		this.rotationSegments = rotationSegments;
		this.topSegments = topSegments;
		this.bottomSegments = bottomSegments;
		this.topHeight = topHeight;
		this.bottomHeight = bottomHeight;
		this.centerHeight = centerHeight;
		this.radius = radius;
	}

	private float getBottomSegmentHeight() {
		return bottomHeight / bottomSegments;
	}
	
	private float getTopSegmentHeight() {
		return topHeight / topSegments;
	}
	
	private void createBaseCylinder() {
		CylinderCreator creator = new CylinderCreator();
		creator.setVertices(rotationSegments);
		creator.setBottomRadius(radius);
		creator.setTopRadius(radius);
		creator.setHeight(getBottomSegmentHeight());
		creator.setTopCapFillType(FillType.N_GON);
		creator.setBottomCapFillType(FillType.N_GON);
		mesh = creator.create();
		mesh.translateY(-getBottomSegmentHeight() * 0.5f);
	}
	
	private void initSelectionAndselectTopFace() {
		selection = new FaceSelection(mesh);
		selection.selectTopFaces();
	}
	
	private void createBottom() {
		for (Face3D face : selection.getFaces()) {
			for (int i = 0; i < bottomSegments - 1; i++) {
				FaceExtrude.extrudeFace(mesh, face, 0.9f, 0.0f);
				FaceExtrude.extrudeFace(mesh, face, 1.0f, getBottomSegmentHeight());
			}
		}
	}
	
	private void createCenter() {
		for (Face3D face : selection.getFaces()) {
			FaceExtrude.extrudeFace(mesh, face, 0.9f, 0.0f);
			FaceExtrude.extrudeFace(mesh, face, 1.0f, centerHeight);
		}
	}
	
	private void createTop() {
		for (Face3D face : selection.getFaces()) {
			for (int i = 0; i < topSegments; i++) {
				FaceExtrude.extrudeFace(mesh, face, 1.1f, 0.0f);
				FaceExtrude.extrudeFace(mesh, face, 1.0f, getTopSegmentHeight());
			}
		}
	}
	
	@Override
	public Mesh3D create() {
		createBaseCylinder();
		initSelectionAndselectTopFace();
		createBottom();
		createCenter();
		createTop();
		return mesh;
	}

	public int getRotationSegments() {
		return rotationSegments;
	}

	public void setRotationSegments(int rotationSegments) {
		this.rotationSegments = rotationSegments;
	}

	public int getTopSegments() {
		return topSegments;
	}

	public void setTopSegments(int topSegments) {
		this.topSegments = topSegments;
	}

	public int getBottomSegments() {
		return bottomSegments;
	}

	public void setBottomSegments(int bottomSegments) {
		this.bottomSegments = bottomSegments;
	}

	public float getTopHeight() {
		return topHeight;
	}

	public void setTopHeight(float topHeight) {
		this.topHeight = topHeight;
	}

	public float getBottomHeight() {
		return bottomHeight;
	}

	public void setBottomHeight(float bottomHeight) {
		this.bottomHeight = bottomHeight;
	}

	public float getCenterHeight() {
		return centerHeight;
	}

	public void setCenterHeight(float centerHeight) {
		this.centerHeight = centerHeight;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
}
