package jade;

import Components.SpriteRenderer;
import Components.SpriteSheet;
import Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Shader;
import renderer.Texture;


public class Level_Editor_Scene extends Scene {

    private GameObject obj1;
    private SpriteRenderer obj1SpriteRenderer;
    private SpriteSheet sprites;
    private Shader defaultShader;
    private Texture testTex;

    GameObject testObj;

    public Level_Editor_Scene(){}

    @Override
    public void init(){

        loadResources();

        this.camera = new Camera(new Vector2f());

        if(levelLoaded){
            return;
        }

        sprites = AssetPool.getSpriteSheet("assets/images/yoda life lessons.png");


        obj1 = new GameObject("Object 1",
                new Transform(new Vector2f(200,200),new Vector2f(256,300))
                ,2);

        obj1SpriteRenderer = new SpriteRenderer();
        obj1SpriteRenderer.setColor(new Vector4f(1,0,0,1));
        obj1.addComponent(obj1SpriteRenderer);
        activeGameObject = obj1;
        this.addGameObjectToScene(obj1);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/yoda life lessons.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/yoda life lessons.png"),
                                16, 16, 26, 0));
    }

    @Override
    public void update(float deltaTime) {

        for(GameObject g: this.gameObjects){
            g.update(deltaTime);
        }

        //calls renderer render function every update call
        this.renderer.render();
    }


}
