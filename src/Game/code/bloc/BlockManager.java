package Game.code.bloc;
import KiryuEngine.KiryuPhysics.Vector3;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.Random;

public class BlockManager {

    //liste contenant la liste des blocs de la structure
    private final ArrayList<Block> blocks = new ArrayList<>();

    //generateur de nombre aletoire
    private final Random random = new Random();

    //genere une structure aleatoire d'une position donnée
    public void generateRandomStructure(
        float centerX,
        float groundY
    ) {
        //supprime les bloc de l'ancienne structure
        blocks.clear();

        int structureType = random.nextInt(3);

        switch (structureType) {

            case 0: //uno bloco
                generateSingleBlock(centerX, groundY);
                break;

            case 1: //rectangle
                generateRectangle(centerX, groundY);
                break;

            case 2: //pyramide
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

        //pour faire quel genre de rectangle on fait
        int columns = random.nextInt(3) + 2; // 2-4
        int rows = random.nextInt(2) + 2;    // 2-3

        //calcul la position x du premier bloc pour center la structure
        float startX =
            centerX - ((columns - 1) * Block.SIZE) / 2;

        //parcour chaque rangee de la structure
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

        //choisit aleatoirement si c pyramide de 3 ou 4
        int rows = random.nextInt(2) + 3;
        // 3 or 4 rows

        //parcourir chaque rangée de la pyramide 
        for (int row = 0; row < rows; row++) {

            //calcul le nombre de blocs dans cette rangée / quand on monte -1 bloc
            int blocksInRow = rows - row;

            //calcul largeur totale de la rangée
            float rowWidth =
                blocksInRow * Block.SIZE;

                //calcule la position du premier bloc pour centrer la rangée
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


    //dessine tous les blocs actuellement présent
    public void draw(PApplet sketch) {

        for (Block block : blocks) {
            block.draw(sketch);
        }
    }

    //retourne la liste de tous les blocs 
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    //set up collision simple 
    public boolean checkProjectileCollision(Vector3 projectilePosition) {

        float projectileSize = 50;

        //parcour tous les blocs de la structure
        //la liste est parcourue a l'envers parce qu'un des blocs peut etre delete
        for (int i = blocks.size() - 1; i >= 0; i--) {

            Block block = blocks.get(i);
            Vector3 blockPosition = block.getPosition();

            boolean collisionX =
                Math.abs(projectilePosition.x - blockPosition.x)
                < (projectileSize / 2 + Block.SIZE / 2);

            boolean collisionY =
                Math.abs(projectilePosition.y - blockPosition.y)
                < (projectileSize / 2 + Block.SIZE / 2);

            if (collisionX && collisionY) {

                // Destroy the block
                blocks.remove(i);

                return true;
            }
        }

        return false;
    }
}