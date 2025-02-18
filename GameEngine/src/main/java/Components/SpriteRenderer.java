package Components;

import jade.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Sprite sprite;


    public SpriteRenderer(Vector4f col){
        this.color = col;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite spr){
        this.sprite = spr;
        this.color = new Vector4f(1,1,1,1);
    }

    @Override
    public void start(){

    }

    @Override
    public void update(float deltaTime) {


    }

    public Vector4f getColor(){
        return this.color;
    }

    public Texture getTexture(){return this.sprite.getTexture();}

    public Vector2f[] getTexCords(){return this.sprite.getTexCords();}
}
