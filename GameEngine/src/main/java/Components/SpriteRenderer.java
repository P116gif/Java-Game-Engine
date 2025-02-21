package Components;

import jade.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;
import jade.Transform;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Sprite sprite;

    // to create movement and animations
    private Transform lastTransform;
    private boolean dirty = false;


    public SpriteRenderer(Vector4f col){
        this.color = col;
        this.sprite = new Sprite(null);
        this.dirty = true;
    }

    public SpriteRenderer(Sprite spr){
        this.sprite = spr;
        this.color = new Vector4f(1,1,1,1);
        this.dirty = true;
    }

    @Override
    public void start(){

        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float deltaTime) {

        //if transform changes, then make dirty true
        if(!(this.lastTransform.equals(this.gameObject.transform))){
            this.gameObject.transform.copy(lastTransform);
            dirty = true;
        }
    }

    public Vector4f getColor(){
        return this.color;
    }

    public Texture getTexture(){return this.sprite.getTexture();}

    public Vector2f[] getTexCords(){return this.sprite.getTexCords();}

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.dirty = true;

    }

    //if color is made different, make dirty true
    public void setColor(Vector4f color){
        if(!this.color.equals(color)){
            this.dirty = true;
            this.color.set(color);
        }

    }

    public boolean isDirty() {
        return dirty;
    }

    public void setClean(){
        this.dirty = false;
    }
}
