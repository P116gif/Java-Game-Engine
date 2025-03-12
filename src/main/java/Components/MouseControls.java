package Components;

import Util.settings;
import jade.GameObject;
import jade.Mouse_Listener;
import jade.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component{

    GameObject holdingThisObject = null;

    public void pickUpObject(GameObject obj1){

        holdingThisObject = obj1;
        Window.getScene().addGameObjectToScene(holdingThisObject);
    }

    public void place(){
        this.holdingThisObject = null;
    }

    @Override
    public void update(float dt){

        if(holdingThisObject != null){

            //make the transform of the object the same as the transform of the mouse
            //subtract 16 to keep it int the center of the mouse
            //to snap it to a grid corner, we integer divide it by grid width then
            //multiply it by grid width to get grid corner position
                                                                                                                                    //offset
            holdingThisObject.transform.position.x = (int)(Mouse_Listener.getOrthoX() / settings.gridWidth) * settings.gridWidth + (32 * 10);
            holdingThisObject.transform.position.y = (int)(Mouse_Listener.getOrthoY() / settings.gridHeight) * settings.gridHeight - 64;


            if(Mouse_Listener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){

                place();
            }
        }
    }
}
