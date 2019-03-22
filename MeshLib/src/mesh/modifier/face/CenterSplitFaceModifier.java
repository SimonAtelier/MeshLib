package mesh.modifier.face;

import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import mesh.Face3D;
import mesh.Mesh3D;
import mesh.modifier.IMeshModifier;
import mesh.wip.FaceSelection;

public class CenterSplitFaceModifier implements IMeshModifier, FaceSelectionModifier {

	private Mesh3D mesh;
	private FaceSelection selection;
	private List<Face3D> newFaces;

	public CenterSplitFaceModifier() {
		newFaces = new ArrayList<Face3D>();
	}
	
	@Override
	public void modify(FaceSelection selection) {
		setMesh(selection.getMesh());
		setSelection(selection);
		split();
	}
	
	@Override
	public Mesh3D modify(Mesh3D mesh) {
		FaceSelection selection = new FaceSelection(mesh);
		selection.selectAll();
		setMesh(mesh);
		setSelection(selection);
		split();
		return mesh;
	}
	
	private void split() {
		clearNewlyCreatedFaces();
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
		Vector3f center = mesh.calculateFaceCenter(face);
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
