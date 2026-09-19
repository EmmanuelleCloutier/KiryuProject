package Game.code.bloc;

import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class BlockManager {

    private final ArrayList<Block> blocks = new ArrayList<>();
    private final Random random = new Random();

    public void generateRandomStructure(
        float centerX,
        float groundY
    ) {

        blocks.clear();

        int structureType = random.nextInt(3);

        switch (structureType) {

            case 0:
                generateSingleBlock(centerX, groundY);
                break;

            case 1:
                generateRectangle(centerX, groundY);
                break;

            case 2:
                generatePyramid(centerX, groundY);
                break;
        }
    }


    private void generateSingleBlock(
        float centerX,
        float groundY
    ) {

        blocks.add(
            new Block(
                new Vector3(
                    centerX,
                    groundY - Block.SIZE / 2,
                    0
                )
            )
        );
    }


    private void generateRectangle(
        float centerX,
        float groundY
    ) {

        int columns = random.nextInt(3) + 2; // 2-4
        int rows = random.nextInt(2) + 2;    // 2-3

        float startX =
            centerX - ((columns - 1) * Block.SIZE) / 2;

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {

                double x =
                    startX + column * Block.SIZE;

                double y =
                    groundY
                    - Block.SIZE / 2
                    - row * Block.SIZE;

                blocks.add(
                    new Block(
                        new Vector3(x, y, 0)
                    )
                );
            }
        }
    }


    private void generatePyramid(
        float centerX,
        float groundY
    ) {

        int rows = random.nextInt(2) + 3;
        // 3 or 4 rows

        for (int row = 0; row < rows; row++) {

            int blocksInRow = rows - row;

            float rowWidth =
                blocksInRow * Block.SIZE;

            float startX =
                centerX
                - rowWidth / 2
                + Block.SIZE / 2;

            for (
                int column = 0;
                column < blocksInRow;
                column++
            ) {

                double x =
                    startX
                    + column * Block.SIZE;

                double y =
                    groundY
                    - Block.SIZE / 2
                    - row * Block.SIZE;

                blocks.add(
                    new Block(
                        new Vector3(x, y, 0)
                    )
                );
            }
        }
    }


    public void draw(PApplet sketch) {

        for (Block block : blocks) {
            block.draw(sketch);
        }
    }


    public ArrayList<Block> getBlocks() {
        return blocks;
    }
}