package Components;

import org.joml.Vector2f;
import renderer.Texture;

public class Sprite {

    private Texture texture;
    private Vector2f[] texCords = {
            new Vector2f(1,1),
            new Vector2f(1, 0),
            new Vector2f(0,0),
            new Vector2f(0,1)

    };

    public Sprite(Texture tex){

        this.texture = tex;
    }

    public Sprite(Texture tex, Vector2f[] cords){
        this.texture = tex;
        this.texCords = cords;
    }


    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCords() {
        return texCords;
    }
}
