package mesh.creator.wip;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.special.ChainLinkCreator;
import mesh.wip.Mesh3DUtil;

public class ChainCreator2 implements IMeshCreator {

	private int links = 5;
	
	@Override
	public Mesh3D create() {
		Mesh3D mesh = new Mesh3D();
		ChainLinkCreator creator = new ChainLinkCreator();
		creator.setMajorRadius(0.5f);
		creator.setMajorSegments(6);
		creator.setMinorRadius(0.2f);
		
		float offset = creator.getCenterPieceSize();
		offset += creator.getMajorRadius() - creator.getMinorRadius();
		
		for (int i = 0; i < links; i++) {
			Mesh3D link = creator.create();
			link.translateZ(i * offset);
			if (i % 2 == 0) {
				link.rotateZ(Mathf.HALF_PI);
			}
			mesh = Mesh3DUtil.append(mesh, link);
		}
		
		mesh.translateZ(-creator.getCenterPieceSize() * links / 2f);
		
		return mesh;
	}

}
