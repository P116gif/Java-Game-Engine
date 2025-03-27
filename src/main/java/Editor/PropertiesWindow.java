package Editor;

import Scenes.Scene;
import imgui.ImGui;
import jade.GameObject;
import jade.Mouse_Listener;
import renderer.PickingTexture;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {

    protected GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pT){

        this.pickingTexture = pT;
    }

    public void imgui(){

        if(activeGameObject!=null){

            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public void update(Scene currentScene){

        if (Mouse_Listener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {

            int x = (int)Mouse_Listener.getScreenX();
            int y = (int)Mouse_Listener.getScreenY();

            int gameObjectID = pickingTexture.readPixel(x,y);

            activeGameObject = currentScene.getGameObject(gameObjectID);
        }

    }
}
