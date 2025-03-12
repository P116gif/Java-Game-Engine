package jade;

import Components.Sprite;
import Components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, int id, float sizeX, float sizeY){

        GameObject block = new GameObject("Sprite_Object_Gen " + id,
                new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}
