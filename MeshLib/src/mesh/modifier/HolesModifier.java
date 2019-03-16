package mesh.modifier;

import java.util.List;

import mesh.Face3D;
import mesh.Mesh3D;
import mesh.wip.Mesh3DUtil;

public class HolesModifier implements IMeshModifier {

	private float scaleExtrude;
	
	public HolesModifier() {
		this.scaleExtrude = 0.5f;
	}
	
	public HolesModifier(float scaleExtrude) {
		this.scaleExtrude = scaleExtrude;
	}

	@Override
	public Mesh3D modify(Mesh3D mesh) {
		List<Face3D> faces = mesh.getFaces(0, mesh.getFaceCount());
		for (Face3D face : faces) {
			Mesh3DUtil.extrudeFace(mesh, face, scaleExtrude, 0.0f);
		}
		mesh.faces.removeAll(faces);
		return mesh;
	}

	public float getScaleExtrude() {
		return scaleExtrude;
	}

	public void setScaleExtrude(float scaleExtrude) {
		this.scaleExtrude = scaleExtrude;
	}
	
}
