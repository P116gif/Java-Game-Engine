package Scenes;

import Components.*;
import Util.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Shader;
import renderer.Texture;
import renderer.debugDraw;


public class Level_Editor_Scene extends Scene {

    private GameObject obj1;
    private SpriteRenderer obj1SpriteRenderer;
    private SpriteSheet sprites;

    private GameObject levelEditorStuff = new GameObject("levelEditor", new Transform(new Vector2f()), 0);

    public Level_Editor_Scene(){}

    @Override
    public void init(){

        //LEVEL EDITOR BASICS
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new MouseControls());

        loadResources();

        //CAMERA
        this.camera = new Camera(new Vector2f(0,0));

        if(levelLoaded){
            sprites = AssetPool.getSpriteSheet("assets/spritesheets/decorationsAndBlocks.png");
            activeGameObject = gameObjects.get(0);
            return;
        }


//        obj1 = new GameObject("Yoda",
//                new Transform(new Vector2f(200,200),new Vector2f(256,300))
//                ,2);
//
//        obj1SpriteRenderer = new SpriteRenderer();
//        Sprite sprite = new Sprite();
//        sprite.setTexture(AssetPool.getTexture("assets/images/yoda_life_lessons.png"));
//        obj1SpriteRenderer.setSprite(sprite);
//        obj1SpriteRenderer.setColor(new Vector4f(1,0,0,1));
//        obj1.addComponent(obj1SpriteRenderer);
//        activeGameObject = obj1;
//        this.addGameObjectToScene(obj1);
//
       sprites = AssetPool.getSpriteSheet("assets/spritesheets/decorationsAndBlocks.png");

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/spritesheets/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));


    }

    @Override
    public void update(float deltaTime) {

        levelEditorStuff.update(deltaTime);

        for(GameObject g: this.gameObjects){
            g.update(deltaTime);
        }

        //calls renderer render function every update call
        this.renderer.render();
    }

    @Override
    public void imgui(){

        //EXPLANATION
        /*
            What we are doing in IMGUI is that we are making the window for
            a bunch of sprites from a spritesheet. We are cutting them and then placing them inside
            a imgui window from which they can then be clicked on and dragged out
         */
        ImGui.begin("DragNDrop");

        //get positions and store it inside the created variable
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);


        //x position of right end of window
        float windowX2 = windowPos.x + windowSize.x;

        //for every sprite in the spritesheet, we make one button for it
        for(int i = 0; i < sprites.size(); i++){

            Sprite sprite = sprites.getSprite(i);
            //times 2 is just for size
            //if you make it 1 or 8 the buttons sizes will dec/inc
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCords = sprite.getTexCords();

            //                 individual sprite id, tex id, ",         ",
            //we make the button
            if(ImGui.imageButton(Integer.toString(i),id, spriteWidth, spriteHeight, texCords[2].x, texCords[0].y, texCords[0].x, texCords[2].y)){

                GameObject object = Prefabs.generateSpriteObject(sprite, i, spriteWidth, spriteHeight);

                //Attach this to the mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickUpObject(object);
            }

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);

            float lastButtonX2  = lastButtonPos.x;
            float currentButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if(i + 1 < sprites.size() && currentButtonX2 < windowX2){
                //if there is space (condition 2), then place it on the same line
                ImGui.sameLine();
            }

        }

        ImGui.end();
    }


}
