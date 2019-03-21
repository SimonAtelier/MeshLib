package mesh.modifier.face;

import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.wip.FaceSelection;
import mesh.wip.Mesh3DUtil;

public class CenterSplitFaceModifier {

	private Mesh3D mesh;
	private FaceSelection selection;
	private List<Face3D> newFaces;

	public CenterSplitFaceModifier() {
		newFaces = new ArrayList<Face3D>();
	}
	
	public void modify(FaceSelection selection) {
		clearNewlyCreatedFaces();
		setMesh(selection.getMesh());
		setSelection(selection);
		centerSplitSelectedFaces();
		addNewlyCreatedFaces();
	}
	
	private void centerSplitSelectedFaces() {
		for (Face3D face : selection.getFaces()) {
			centerSplit(face);
		}
	}

	private void clearNewlyCreatedFaces() {
		newFaces.clear();
	}
	
	private void addNewlyCreatedFaces() {
		mesh.faces.addAll(newFaces);
	}

	private void centerSplit(Face3D face) {
		int nextIndex = mesh.getVertexCount();
		int indicesLength = face.indices.length;
		Vector3f center = Mesh3DUtil.calculateFaceCenter(mesh, face);
		mesh.add(center);
		for (int i = 0; i < face.indices.length; i++) {
			int index0 = face.indices[i % indicesLength];
			int index1 =  face.indices[(i + 1) % indicesLength];
			int index3 = nextIndex;
			newFaces.add(new Face3D(index0, index1, index3));
		}
		mesh.faces.remove(face);
	}
	
	private void setMesh(Mesh3D mesh) {
		this.mesh = mesh;
	}
	
	private void setSelection(FaceSelection selection) {
		this.selection = selection;
	}

}
