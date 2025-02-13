package jade;

import Components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Shader;
import renderer.texture;



public class Level_Editor_Scene extends Scene {

    private Shader defaultShader;
    private texture testTex;

    GameObject testObj;

    public Level_Editor_Scene(){}

    @Override
    public void init(){

        this.camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (600.0f- xOffset * 2.0f);
        float totalHeight = (300f - yOffset * 2f);
        float sizeX = totalWidth/100f;
        float sizeY = totalHeight/100f;

        for(int x = 0; x < 100; x++){
            for(int y = 0; y < 100; y++){
                float xPos = xOffset  + (x*sizeX);
                float yPos = yOffset + (y*sizeY);
                                                                                        //position                  //scale
                GameObject go = new GameObject("Obj " + x + " " + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos/totalWidth, yPos/totalHeight, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }


    }


    int count = 0;

    @Override
    public void update(float deltaTime) {

        System.out.println("FPS: " + 1/deltaTime);
        for(GameObject go: this.gameObjects){
            go.update(deltaTime);
        }

        this.renderer.render();
    }


}
