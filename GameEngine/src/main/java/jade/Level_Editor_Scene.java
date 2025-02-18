package jade;

import Components.Sprite;
import Components.SpriteRenderer;
import Components.SpriteSheet;
import Util.AssetPool;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Shader;
import renderer.Texture;


public class Level_Editor_Scene extends Scene {

    private Shader defaultShader;
    private Texture testTex;

    GameObject testObj;

    public Level_Editor_Scene(){}

    @Override
    public void init(){

        loadResources();

        this.camera = new Camera(new Vector2f());

        SpriteSheet sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        GameObject go = new GameObject("Object 1", new Transform(new Vector2f(500,200),new Vector2f(500,256)));
        go.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(go);

        GameObject go2 = new GameObject("Object 2", new Transform(new Vector2f(200,200),new Vector2f(256,300)));
        go2.addComponent(new SpriteRenderer(sprites.getSprite(11)));
        System.out.println(go2 + " Texture: " + AssetPool.getTexture("C:\\Users\\parij\\Pictures\\Genshin\\80608eb29afc989d4d692eacabab613b.jpg"));
        this.addGameObjectToScene(go2);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                                16, 16, 26, 0));
    }


    int count = 0;

    @Override
    public void update(float deltaTime) {

        for(GameObject go: this.gameObjects){
            go.update(deltaTime);
        }

        this.renderer.render();
    }


}
