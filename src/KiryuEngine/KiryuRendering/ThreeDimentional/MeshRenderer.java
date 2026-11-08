package KiryuEngine.KiryuRendering.ThreeDimentional;

import KiryuEngine.KiryuMath.Quaternion;
import KiryuEngine.KiryuMath.Vector3;
import processing.core.PApplet;
import processing.core.PShape;
import java.util.ArrayList;

public class MeshRenderer {
  private final PApplet sketch;
  private final ArrayList<Mesh> loadedMesh;


  public MeshRenderer(PApplet sketch){
    this.sketch = sketch;
    this.loadedMesh =new ArrayList<>() {
    };
  }

  public boolean addMesh(Mesh mesh){
    if (loadMesh(mesh)) {
      return loadedMesh.add(mesh);
    }
    return false;
  }

  private boolean loadMesh(Mesh mesh){
    if(loadedMesh.contains(mesh)) return false;

    PShape model = this.sketch.loadShape(mesh.getFilename());
    if (model != null) {
      mesh.setModel(model);
      return true;
    }
    return false;
  }

  public boolean removeMesh(Mesh mesh){
    return loadedMesh.remove(mesh);
  }

  private void drawMesh(Mesh mesh, Vector3 position, Quaternion rotation){

  }
}
