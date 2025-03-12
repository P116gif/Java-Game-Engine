package Components;

import org.joml.Vector2f;
import renderer.Texture;

public class Sprite {

    private Texture texture = null;
    private Vector2f[] texCords = {
            new Vector2f(1,1),
            new Vector2f(1, 0),
            new Vector2f(0,0),
            new Vector2f(0,1)

    };

    private float width, height;

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCords() {
        return texCords;
    }

    public void setTexCords(Vector2f[] texCords) {
        this.texCords = texCords;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexId(){

        return texture == null? -1 : texture.getId();
    }
}
