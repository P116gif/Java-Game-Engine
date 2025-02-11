package Components;

import jade.Component;

public class SpriteRenderer extends Component {

    @Override
    public void start(){
        System.out.println("I am starting");
    }

    @Override
    public void update(float deltaTime) {
        System.out.println("I am updating");

    }
}
