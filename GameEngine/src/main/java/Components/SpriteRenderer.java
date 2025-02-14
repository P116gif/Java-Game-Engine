package Components;

import jade.Component;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color;
    private Vector2f[] texCords = {
            new Vector2f(1,1),
            new Vector2f(1, 0),
            new Vector2f(0,0),
            new Vector2f(0,1)

    };
    private Texture texture;


    public SpriteRenderer(Vector4f col){
        this.texture = null;
        this.color = col;
    }

    public SpriteRenderer(Texture tex){
        this.texture = tex;
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

    public Texture getTexture(){return this.texture;}

    public Vector2f[] getTexCords(){return this.texCords;}
}
