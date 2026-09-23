package Game.code.bloc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StructureGenerator {

  public record BlockData(double x, double y, double z){}

  private final ExecutorService executor =
      Executors.newSingleThreadExecutor();

  private final Random random = new Random();

  public CompletableFuture<List<BlockData>> generateRandomStructureAsync(
      float centerX,
      float groundY
  ) {

    return CompletableFuture.supplyAsync(
        () -> generateRandomStructure(centerX, groundY),
        executor
    );
  }

  private List<BlockData> generateRandomStructure(
      float centerX,
      float groundY
  ) {
    List<BlockData> blocks = new ArrayList<>();

    int structureType = random.nextInt(3);

    switch (structureType) {
      case 0 -> generateSingleBlock(
          blocks,
          centerX,
          groundY
      );
      case 1 -> generateRectangle(
          blocks,
          centerX,
          groundY
      );
      case 2 -> generatePyramid(
          blocks,
          centerX,
          groundY
      );
    }

    return blocks;
  }

  private void generateSingleBlock(
      List<BlockData> blocks,
      float centerX,
      float groundY
  ) {
    blocks.add(
        new BlockData(
            centerX,
            groundY - Block.SIZE / 2,
            0
        )
    );
  }

  private void generateRectangle(
      List<BlockData> blocks,
      float centerX,
      float groundY
  ) {
    int columns = random.nextInt(3) + 2;
    int rows = random.nextInt(2) + 2;

    float startX =
        centerX - ((columns - 1) * Block.SIZE) / 2;

    for (int row = 0; row < rows; row++) {

      for (int column = 0; column < columns; column++) {

        float x =
            startX + column * Block.SIZE;

        float y =
            groundY
                - Block.SIZE / 2
                - row * Block.SIZE;

        blocks.add(
            new BlockData(x, y, 0)
        );
      }
    }
  }

  private void generatePyramid(
      List<BlockData> blocks,
      float centerX,
      float groundY
  ) {
    int rows = random.nextInt(2) + 3;

    for (int row = 0; row < rows; row++) {

      int blocksInRow = rows - row;

      float rowWidth =
          blocksInRow * Block.SIZE;

      float startX =
          centerX
              - rowWidth / 2
              + Block.SIZE / 2;

      for (int column = 0;
           column < blocksInRow;
           column++) {

        float x =
            startX
                + column * Block.SIZE;

        float y =
            groundY
                - Block.SIZE / 2
                - row * Block.SIZE;

        blocks.add(
            new BlockData(x, y, 0)
        );
      }
    }
  }
}