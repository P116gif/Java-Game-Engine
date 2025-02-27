package jade;

import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){}

    public void init(){

    }

    public void start(){
        for(GameObject g: gameObjects){
            g.start();
            this.renderer.add(g);
        }

        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go){
        System.out.println("Added game object: " + go);
        if(!isRunning){
            gameObjects.add(go);
        }
        else{
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float deltaTime);

    public Camera camera(){

        return this.camera;
    }
}
