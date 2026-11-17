package KiryuEngine.KiryuRendering.ThreeDimentional;

import processing.core.PShape;

import java.awt.*;
import java.util.Objects;

public class Mesh {

  private PShape model;
  private Color color;
  private final String filename;

  public Mesh(String filename) {

   this.filename = filename;
  }

  public PShape getModel() {
    return model;
  }

  public String getFilename() {
    return filename;
  }


  public void setModel(PShape model) {
    this.model = model;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }


  @Override
  public boolean equals(Object o){
    if (this == o) return true;

    if (o==null || getClass() != o.getClass())return false;

    Mesh mesh = (Mesh) o;

    return this.filename.equals(mesh.filename);
  }

  @Override
  public int hashCode(){
    return Objects.hash(this.filename);
  }
}
