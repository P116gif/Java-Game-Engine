package Scenes;

import Components.Component;
import imgui.ImGui;
import jade.Camera;
import jade.GameObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import renderer.Renderer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;
    private static final Logger logger = LoggerFactory.getLogger(GameObject.class);

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

    public void sceneImgui(){

        if(activeGameObject!=null){

            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();

    }

    public void imgui(){}

    public void saveAsFile(){

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new forComponents())
                .registerTypeAdapter(GameObject.class, new forGameObject())
                .create();

        try{

            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        }
        catch(IOException e){
            logger.error("IO Exception occured when writing into level.txt", e);
            System.exit(-1);
        }
    }

    public void load(){

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new forComponents())
                .registerTypeAdapter(GameObject.class, new forGameObject())
                .create();

        String infile = "";


        try{
            infile = new String(Files.readAllBytes(Paths.get("level.txt")));
        }
        catch(IOException e){

            logger.error("Error while reading level.txt",e );
        }

        if(!infile.equalsIgnoreCase("")){

            int maxGOId = -1;
            int maxCompId = -1;


            GameObject[] objects = gson.fromJson(infile, GameObject[].class);

            for(GameObject go: objects){
                addGameObjectToScene(go);

                for(Component c: go.getComponentList()){

                    if(c.getUid() > maxCompId)
                        maxCompId = c.getUid();
                }

                if(go.getUid() > maxGOId)
                    maxGOId = go.getUid();
            }


            GameObject.init(++maxGOId);
            Component.init(++maxCompId);

            this.levelLoaded = true;

        }
    }
}
