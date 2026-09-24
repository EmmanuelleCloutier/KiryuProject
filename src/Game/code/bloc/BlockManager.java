package Game.code.bloc;
import KiryuEngine.KiryuCore.KiryuSound;
import KiryuEngine.KiryuPhysics.Particle;
import KiryuEngine.KiryuPhysics.Vector3;
import KiryuEngine.KiryuRendering.Animation;
import KiryuEngine.KiryuRendering.Sprite;
import processing.core.PApplet;

import java.util.*;

public class BlockManager {

    //liste contenant la liste des blocs de la structure
    private ArrayList<Block> currentStructure = new ArrayList<>();
    private ArrayList<Block> nextStructure = new ArrayList<>();
    private HashMap<Animation, Vector3> destroyAnimationPositions = new HashMap<>();
    private boolean bIsNextStructureGenerated = false;
    private boolean bIsNextStructureGenerating = false;

    //generateur de nombre aletoire
    private final Random random = new Random();

    private final PApplet sketch;
    private final Sprite blockSprite;
    private final Animation destroyAnimation;
    private final KiryuSound destroySound;

    private final BlockRenderer blockRenderer;
    private final StructureGenerator structureGenerator;
    private volatile List<StructureGenerator.BlockData> generatedStructure;


    public BlockManager(PApplet sketch){
        this.sketch = sketch;
        this.blockRenderer = new BlockRenderer(sketch);
        this.structureGenerator = new StructureGenerator();

        blockSprite = new Sprite(
            "Game/data/brick_tillable.png",
            Block.SIZE,
            Block.SIZE
        );

        destroyAnimation = new Animation(
            "Game/data/destroy.gif",
            75,
            75,
            false,
            1
        );

        destroyAnimation.loadRenderableImage(
            sketch,
            "destroy.gif"
        );


        destroySound = new KiryuSound(sketch,"impact.wav");
    }

    public void loadNextStructure(){
        ArrayList<Block> temp = currentStructure;

        currentStructure = nextStructure;
        nextStructure = temp;

        bIsNextStructureGenerated = false;

    }


    public void updateStructureGeneration() {

        if (generatedStructure == null) {
            return;
        }

        nextStructure.clear();

        for (StructureGenerator.BlockData data : generatedStructure) {

            nextStructure.add(
                new Block(
                    new Vector3(
                        data.x(),
                        data.y(),
                        data.z()
                    ),
                    blockSprite,
                    destroyAnimation
                )
            );
        }

        generatedStructure = null;

        bIsNextStructureGenerating = false;
        bIsNextStructureGenerated = true;
    }

    //genere une structure aleatoire d'une position donnée
    public void generateRandomStructure(
        float centerX,
        float groundY
    ) {

        if (bIsNextStructureGenerating) {
            return;
        }

        // Already have a generated structure
        if (bIsNextStructureGenerated) {
            return;
        }

        bIsNextStructureGenerating = true;

        structureGenerator.generateRandomStructureAsync(
            centerX,
            groundY
        ).thenAccept(data -> {
            generatedStructure = data;
        });
    }


    //dessine tous les blocs actuellement présent
    public void drawBlocks() {
        if (currentStructure.isEmpty()) return;
        blockRenderer.drawBlockSprites(currentStructure);


        Iterator<Map.Entry<Animation, Vector3>> iterator =
            destroyAnimationPositions.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Animation, Vector3> entry = iterator.next();

            Animation animation = entry.getKey();
            Vector3 position = entry.getValue();

            animation.draw(this.sketch, position);

            if (animation.isFinished()) {
                iterator.remove();
            }
        }

    }

    //retourne la liste de tous les blocs 
    public ArrayList<Block> getCurrentStructure() {
        return currentStructure;
    }

    //set up collision simple 
    public boolean checkProjectileCollision(
        Particle particle,
        float particleWidth,
        float particleHeight
    ) {
        if (currentStructure.isEmpty()) return false;

        Vector3 projectilePosition = particle.getPosition();

        //parcour tous les blocs de la structure
        //la liste est parcourue a l'envers parce qu'un des blocs peut etre delete
        for (int i = currentStructure.size() - 1; i >= 0; i--) {

            Block block = currentStructure.get(i);
            Vector3 blockPosition = block.getPosition();

            boolean collisionX =
                Math.abs(projectilePosition.x - blockPosition.x)
                < (particleWidth / 2 + Block.SIZE / 2);

            boolean collisionY =
                Math.abs(projectilePosition.y - blockPosition.y)
                < (particleHeight / 2 + Block.SIZE / 2);

            if (collisionX && collisionY) {
                destroyAnimationPositions.put(block.getDestroyAnimation(),blockPosition);

                block.getDestroyAnimation().playAnimation();

                destroySound.play();
                destroySound.setVolume(0.25f);
                // Destroy the block
                currentStructure.remove(i);


                return true;
            }
        }

        return false;
    }

    public boolean areAllBlocksDestroyed() {
    return currentStructure.isEmpty();
    }
}