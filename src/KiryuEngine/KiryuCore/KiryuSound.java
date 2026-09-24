package KiryuEngine.KiryuCore;

import processing.core.PApplet;
import processing.sound.SoundFile;

import java.io.File;

public class KiryuSound {

  private SoundFile soundFile;
  private float volume = 1.0f;

  public KiryuSound(PApplet sketch, String filename) {
    try {

      File file = new File("src/Game/data/" + filename);

      if (file.exists()) {
        soundFile = new SoundFile(sketch, file.getAbsolutePath());
      } else {
        System.err.println("CRITICAL ERROR: Sound file not found at " + file.getAbsolutePath());
      }
    } catch (Exception e) {
      System.err.println("Error initializing sound: " + e.getMessage());
    }
  }


  public void setVolume(float newVolume) {

    this.volume = Math.max(0.0f, newVolume);


    if (soundFile != null && soundFile.isPlaying()) {
      soundFile.amp(this.volume);
    }
  }

  public float getVolume() {
    return this.volume;
  }


  public void play() {
    if (soundFile != null) {
      soundFile.play();
    }
  }


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
