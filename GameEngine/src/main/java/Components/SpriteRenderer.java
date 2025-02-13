package Components;

import jade.Component;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;


    public SpriteRenderer(Vector4f col){

        this.color = col;

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
}
