package jade;

import Components.SpriteRenderer;
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

        this.camera = new Camera(new Vector2f());

        GameObject go = new GameObject("Object 1", new Transform(new Vector2f(800,800),new Vector2f(300,256)));
        go.addComponent(new SpriteRenderer(AssetPool.getTexture("C:\\Users\\parij\\Pictures\\Genshin\\1311796.png")));
        this.addGameObjectToScene(go);

        GameObject go2 = new GameObject("Object 2", new Transform(new Vector2f(200,200),new Vector2f(256,300)));
        go2.addComponent(new SpriteRenderer(AssetPool.getTexture("C:\\Users\\parij\\Pictures\\Genshin\\80608eb29afc989d4d692eacabab613b.jpg")));
        System.out.println(go2 + " Texture: " + AssetPool.getTexture("C:\\Users\\parij\\Pictures\\Genshin\\80608eb29afc989d4d692eacabab613b.jpg"));
        this.addGameObjectToScene(go2);

        GameObject go3 = new GameObject("Object 3", new Transform(new Vector2f(500,300),new Vector2f(300,256)));
        go3.addComponent(new SpriteRenderer(AssetPool.getTexture("C:\\Users\\parij\\Pictures\\Genshin\\Yae Miko Genshin Impact.jpg")));
        System.out.println(go2 + " Texture: " + AssetPool.getTexture("C:\\Users\\parij\\Pictures\\Genshin\\Yae Miko Genshin Impact.jpg"));
        this.addGameObjectToScene(go3);




        loadResources();

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
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
