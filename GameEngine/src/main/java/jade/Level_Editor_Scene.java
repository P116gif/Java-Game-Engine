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

    private GameObject go;
    private SpriteSheet sprites;
    private Shader defaultShader;
    private Texture testTex;

    GameObject testObj;

    public Level_Editor_Scene(){}

    @Override
    public void init(){

        loadResources();

        this.camera = new Camera(new Vector2f());

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        go = new GameObject("Object 1",
                new Transform(new Vector2f(200,200),new Vector2f(500,256))
                ,4);
        go.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(go);

        GameObject go2 = new GameObject("Object 2",
                new Transform(new Vector2f(200,200),new Vector2f(256,300))
                ,2);
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


    int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float deltaTime) {

        spriteFlipTimeLeft-=deltaTime;
        if(spriteFlipTimeLeft <=0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if(spriteIndex>3){
                spriteIndex = 0;
            }
            go.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }
        go.transform.position.x+=20 * deltaTime ;

        for(GameObject g: this.gameObjects){
            g.update(deltaTime);
        }

        //calls renderer render function every update call
        this.renderer.render();
    }


}
