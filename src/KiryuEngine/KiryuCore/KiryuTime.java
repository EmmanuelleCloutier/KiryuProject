package KiryuEngine.KiryuCore;

public class KiryuTime {
  private static long startTime;
  private static long lastTime;

  public static float deltaTime = 0.0f;
  public static float timeScale = 1.0f; // 1.0 = normal, 0.0 = paused
  private static float smoothDelta = 0.016f;
  private static double unitCoef = 1 / 1_000_000_000.0;

  private static boolean paused = false;

  /**
   * initialization of the time class
   */
  public static void init() {
    startTime = System.nanoTime();
    lastTime = System.nanoTime();
  }

  /**
   * updates the deltaTime between each frames
   */
  public static void update() {
    long currentTime = System.nanoTime();
    float rawDeltaTime = (float)((currentTime - lastTime) * unitCoef);
    lastTime = currentTime;

    deltaTime = rawDeltaTime * timeScale;

    smoothDelta = (smoothDelta * 0.9f) + (rawDeltaTime * 0.1f);
  }

  /**
   * @return time elapsed between each frame scaled with timeScale
   * principal deltaTime that needs to be used in most of the cases
   */
  public static float getDeltaTime() {
    return deltaTime;
  }

  /**
   * @return  time elapsed between each frame (basically rawDeltaTime)
   * useful for ui animations
   */
  public static float getUnscaledDeltaTime() {
    return (float)((System.nanoTime() - lastTime) * unitCoef);
  }

  /**
   * @return time elapsed since the initialization of the time class
   */
  public static float getTotalTime() {
    return (float)((System.nanoTime() - startTime) * unitCoef);
  }

  /**
   * @return average elapsed time between previous frame and current frame
   */
  public static float getSmoothDeltaTime() {
    return smoothDelta * timeScale;
  }

  /**
   *
   * @return frame rate at which the app runs
   */
  public static float getFPS() {
    return smoothDelta > 0 ? 1.0f / smoothDelta : 0.0f;
  }

  /**
   * sets the timeScale to 0, effectively freezing anything that
   * acts around deltaTime
   */
  public static void togglePause() {
    paused = !paused;
    timeScale = paused ? 0.0f : 1.0f;
  }

  /**
   * @return whether pause is on or off
   */
  public static boolean isPaused() {
    return paused;
  }

}
