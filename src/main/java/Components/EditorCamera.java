package Components;

import jade.Camera;
import jade.Key_Listener;
import jade.Mouse_Listener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_DECIMAL;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class EditorCamera extends Component{

    private float dragDebounce = .032f;

    private final Camera levelEditorCamera;
    private Vector2f clickOrigin;

    private float lerpTime = 0.0f;
    private float dragSensitivity = 30f;
    double scrollSensitivity = 0.1f;


    private boolean reset = false;

    public EditorCamera(Camera levelEditorCamera){
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();

    }

    @Override
    public void update(float dt){

        if(Mouse_Listener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && dragDebounce > 0){
            this.clickOrigin = new Vector2f(Mouse_Listener.getOrthoX(), Mouse_Listener.getOrthoY());
            dragDebounce-=dt;
        }
        else if(Mouse_Listener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){

            //change between where we first clicked and currently where we are
            Vector2f delta = new Vector2f(Mouse_Listener.getOrthoX(), Mouse_Listener.getOrthoY())
                    .sub(this.clickOrigin);

            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));

            //lerping between clickOrigin and the current mouse position
            this.clickOrigin.lerp(new Vector2f(Mouse_Listener.getOrthoX(), Mouse_Listener.getOrthoY())
                    , dt);


        }else{
            dragDebounce = 0.032f;
        }

        if(Mouse_Listener.getScrollY() != 0.0f){

            //exponential function to increase zoom the farther out you go
            //meaning the more you zoom, the faster you zoom
            float addValue = (float)Math.pow(Math.abs(Mouse_Listener.getScrollY() * scrollSensitivity)
                    , 1 / levelEditorCamera.getZoom());

            //to get sign of direction in which we are zooming
            addValue *= (float) -Math.signum(Mouse_Listener.getScrollY());
            levelEditorCamera.addZoom(addValue);
        }

        if(Key_Listener.isKeyPressed(GLFW_KEY_KP_DECIMAL)){
            reset = true;
        }

        if(reset){

           //to reset position
            levelEditorCamera.position.lerp(new Vector2f(0,0), lerpTime);
            this.lerpTime+=0.1f*dt;


            //to reset zoom
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() +
                    ((1.0f-levelEditorCamera.getZoom()) * lerpTime));

            //to clamp when it gets close enough
            if(Math.abs(levelEditorCamera.position.x) <= 5.0f && Math.abs(levelEditorCamera.position.y) <= 5){
                levelEditorCamera.position.set(0f,0f);
                this.levelEditorCamera.setZoom(1.0f);
                reset = false;
                this.lerpTime = 0.0f;
            }
        }
    }
}
