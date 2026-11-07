package KiryuEngine.KiryuRendering.ThreeDimentional;

import processing.core.PApplet;
import processing.core.PShape;


import java.util.HashSet;

public class MeshRenderer {
  private final PApplet sketch;
  private final HashSet<Mesh> loadedMesh;


  public MeshRenderer(PApplet sketch){
    this.sketch = sketch;
    this.loadedMesh = new HashSet<>();
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


}
