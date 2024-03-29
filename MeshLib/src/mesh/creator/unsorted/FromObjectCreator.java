package mesh.creator.unsorted;

import java.io.File;

import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.io.SimpleObjectReader;
import mesh.modifier.FlipFacesModifier;

public class FromObjectCreator implements IMeshCreator {

	private float scale;
	private boolean flipDirection;
	private String path;

	public FromObjectCreator(String path) {
		this(1.0f, true, path);
	}

	public FromObjectCreator(float scale, boolean flipDirection, String path) {
		this.scale = scale;
		this.flipDirection = true;
		this.path = path;
	}

	@Override
	public Mesh3D create() {
		File file = new File(path);
		SimpleObjectReader in = new SimpleObjectReader();
		Mesh3D mesh = in.read(file);
		mesh.scale(scale);
		if (flipDirection)
			new FlipFacesModifier().modify(mesh);
		return mesh;
	}

}
