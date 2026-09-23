package KiryuEngine.KiryuRendering;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;
import processing.core.PImage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import static processing.core.PConstants.ARGB;
import static processing.core.PConstants.CENTER;

public class Animation implements Renderable{

  private final String animationPath;
  private final ArrayList<PImage> animationSequence;
  private int currentFrame = 0;
  private final boolean bIsLooping;
  private boolean bIsFinished;
  private boolean bIsPlaying;
  private int ticksPerAnimationFrame = 1;
  private int tickCounter = 0;
  public float height;
  public float width;


  public Animation(
      String animationPath,
      float width,
      float height,
      boolean looping,
      int tickRate
  ) {
    this.animationPath = animationPath;
    this.width = width;
    this.height = height;
    this.animationSequence = new ArrayList<>();
    bIsLooping = looping;
    bIsFinished = true;
    bIsPlaying = false;
    ticksPerAnimationFrame = tickRate;
  }

  public void playAnimation(){
    if (bIsFinished && !bIsPlaying) {
      currentFrame = 0;
      tickCounter = 0;
      bIsFinished = false;
    }
  }

  public boolean isFinished() {
    return bIsFinished;
  }
  public boolean isLooping() {
    return bIsLooping;
  }


  @Override
  public void draw(
      PApplet sketch,
      Vector3 position
  ) {

    if (bIsFinished) {
      return;
    }

    bIsPlaying = true;

      sketch.imageMode(CENTER);
      sketch.image(animationSequence.get(currentFrame),
          (float) position.x,
          (float) position.y,
          this.width,
          this.height
      );
    tickCounter++;
    if (tickCounter >= ticksPerAnimationFrame) {
      tickCounter = 0;
      currentFrame++;

      if (currentFrame >= animationSequence.size() ) {
        if (!bIsLooping) {
          bIsFinished = true;
          bIsPlaying = false;
        }
        currentFrame = 0;
      }
    }
  }

  @Override
  public void draw(
      PApplet sketch,
      Vector3 position,
      Vector3 direction
  ) {
    if (bIsFinished) {
      return;
    }

    bIsPlaying = true;

    sketch.pushMatrix();
    sketch.translate((float)position.x,(float)position.y);
    float angle = PApplet.atan2((float)direction.y,(float)direction.x);
    sketch.rotate(angle);
    sketch.imageMode(CENTER);
    sketch.image(animationSequence.get(currentFrame),
        0,
        0,
        this.width,
        this.height
    );
    sketch.popMatrix();
    tickCounter++;
    if (tickCounter >= ticksPerAnimationFrame) {
      tickCounter = 0;
      currentFrame++;

      if (currentFrame >= animationSequence.size() ) {
        if (!bIsLooping) {
          bIsFinished = true;
          bIsPlaying = false;
        }
        currentFrame = 0;
      }
    }
  }

  @Override
  public void draw(
      PApplet sketch,
      Vector3 position,
      float angle
  ) {
    if (bIsFinished) {
      return;
    }

    bIsPlaying = true;

    sketch.pushMatrix();
    sketch.translate((float)position.x,(float)position.y);
    sketch.rotate(angle);
    sketch.imageMode(CENTER);
    sketch.image(animationSequence.get(currentFrame),
        0,
        0,
        this.width,
        this.height
    );
    sketch.popMatrix();

    tickCounter++;
    if (tickCounter >= ticksPerAnimationFrame) {
      tickCounter = 0;
      currentFrame++;

      if (currentFrame >= animationSequence.size() ) {
        if (!bIsLooping) {
          bIsFinished = true;
          bIsPlaying = false;
        }
        currentFrame = 0;
      }
    }
  }

  @Override
  public void draw(
      PApplet sketch,
      Vector3 position,
      float width,
      float height
  ) {
    if (bIsFinished) {
      return;
    }

    bIsPlaying = true;

    sketch.imageMode(CENTER);

    sketch.image(
        animationSequence.get(currentFrame),
        (float)position.x,
        (float)position.y,
        width,
        height
    );
    tickCounter++;
    if (tickCounter >= ticksPerAnimationFrame) {
      tickCounter = 0;
      currentFrame++;

      if (currentFrame >= animationSequence.size() ) {
        if (!bIsLooping) {
          bIsFinished = true;
          bIsPlaying = false;
        }
        currentFrame = 0;
      }
    }
  }

  @Override
  public void draw(
      PApplet sketch,
      Vector3 position,
      Vector3 direction,
      float width,
      float height
  ) {
    if (bIsFinished) {
      return;
    }

    bIsPlaying = true;

    sketch.pushMatrix();
    sketch.translate((float)position.x,(float)position.y);
    float angle = PApplet.atan2((float)direction.x,(float)direction.y) - sketch.HALF_PI;
    sketch.rotate(angle);
    sketch.imageMode(CENTER);
    sketch.image(animationSequence.get(currentFrame),
        (float)position.x,
        (float)position.y,
        width,
        height
    );
    sketch.popMatrix();
    tickCounter++;
    if (tickCounter >= ticksPerAnimationFrame) {
      tickCounter = 0;
      currentFrame++;

      if (currentFrame >= animationSequence.size() ) {
        if (!bIsLooping) {
          bIsFinished = true;
          bIsPlaying = false;
        }
        currentFrame = 0;
      }
    }
  }

  @Override
  public void loadRenderableImage(PApplet sketch) {
    loadRenderableImage(sketch,"todo");
  }

  @Override
  public void loadRenderableImage(
      PApplet sketch,
      String filename
  ) {

    try {
      File file = new File("src/Game/data/" + filename);
      ImageInputStream is = ImageIO.createImageInputStream(file);
      ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
      reader.setInput(is, false, false);

      int numImages = reader.getNumImages(true);

      BufferedImage masterCanvas = null;
      Graphics2D g = null;

      for (int i = 0; i < numImages; i++) {
        BufferedImage frameImg = reader.read(i);

        if (masterCanvas == null) {
          masterCanvas = new BufferedImage(frameImg.getWidth(), frameImg.getHeight(), BufferedImage.TYPE_INT_ARGB);
          g = masterCanvas.createGraphics();
        }


        g.drawImage(frameImg, 0, 0, null);


        PImage img = new PImage(masterCanvas.getWidth(), masterCanvas.getHeight(), ARGB);
        masterCanvas.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
        img.updatePixels();

        animationSequence.add(img);
      }

      if (g != null) g.dispose();
      reader.dispose();

    } catch (Exception e) {
      System.err.println("Error loading GIF: " + e.getMessage());
    }

  }

  public void setTicksPerAnimationFrame(int ticksPerAnimationFrame) {
    this.ticksPerAnimationFrame = ticksPerAnimationFrame;
  }

}
