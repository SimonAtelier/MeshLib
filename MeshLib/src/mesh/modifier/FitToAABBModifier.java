package mesh.modifier;

import mesh.Mesh3D;
import mesh.math.Bounds3;
import mesh.wip.Mesh3DOperations;

public class FitToAABBModifier implements IMeshModifier {

	private float boundingBoxWidth;
	private float boundingBoxHeight;
	private float boundingBoxDepth;

	public FitToAABBModifier(float boundingBoxWidth, float boundingBoxHeight, float boundingBoxDepth) {
		this.boundingBoxWidth = boundingBoxWidth;
		this.boundingBoxHeight = boundingBoxHeight;
		this.boundingBoxDepth = boundingBoxDepth;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		Bounds3 bounds = Mesh3DOperations.getBounds(mesh);
		float scaleX = 1f / bounds.getWidth() * boundingBoxWidth;
		float scaleY = 1f / bounds.getHeight() * boundingBoxHeight;
		float scaleZ = 1f / bounds.getDepth() * boundingBoxDepth;
		mesh.scale(scaleX, scaleY, scaleZ);
		return mesh;
	}

}
