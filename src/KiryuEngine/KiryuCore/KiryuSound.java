package KiryuEngine.KiryuCore;

import processing.core.PApplet;
import processing.sound.SoundFile;

import java.io.File;

public class KiryuSound {

  private SoundFile soundFile;

  public KiryuSound(PApplet sketch, String filename) {
    try {
      // Build the path to your audio file inside the data folder
      String path = sketch.sketchPath("src/Game/data/" + filename);

      // Make sure the file exists before loading to prevent crashes
      File file = new File(path);
      if (file.exists()) {
        soundFile = new SoundFile(sketch, path);
      } else {
        System.err.println("CRITICAL ERROR: Sound file not found at " + file.getAbsolutePath());
      }
    } catch (Exception e) {
      System.err.println("Error initializing sound: " + e.getMessage());
    }
  }

  // Play the sound once from the beginning (perfect for explosions!)
  public void play() {
    if (soundFile != null) {
      soundFile.play();
    }
  }

  // Play the sound on a continuous loop (perfect for background music)
  public void loop() {
    if (soundFile != null) {
      soundFile.loop();
    }
  }

  // Stop the sound instantly
  public void stop() {
    if (soundFile != null) {
      soundFile.stop();
    }
  }
}
