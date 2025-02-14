//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package jade;

import java.util.Objects;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Window {

    private final int width = 1920;
    private final int height = 1080;
    private final String title = "Game Engine";
    private long glfwWindow;
    private static Window window = null;

    public float r,b,g,a;

    private static Scene currentScene = null;

    private Window() {
        r=1;
        b=1;
        g=1;
        a=1;
    }

    public static void changeScene(int newScene){
        switch (newScene)
        {
            case 0:
                currentScene = new Level_Editor_Scene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new Level_Scene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false: "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public void run() {
        System.out.println("Hello lwjgl" + Version.getVersion() + "!");
        this.init();
        this.loop();
        Callbacks.glfwFreeCallbacks(this.glfwWindow);
        GLFW.glfwDestroyWindow(this.glfwWindow);
        GLFW.glfwTerminate();
        ((GLFWErrorCallback)Objects.requireNonNull(GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)null))).free();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialise GLFW window.");
        } else {
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(131076, 0);
            GLFW.glfwWindowHint(131075, 1);
            GLFW.glfwWindowHint(131080, 1);
            this.glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0L, 0L);
            if (this.glfwWindow == 0L) {
                throw new RuntimeException("Failed to create GLFW window.");
            } else {
                GLFW.glfwSetCursorPosCallback(this.glfwWindow, Mouse_Listener::mousePosCallBack);
                GLFW.glfwSetMouseButtonCallback(this.glfwWindow, Mouse_Listener::mouseButtonCallBack);
                GLFW.glfwSetScrollCallback(this.glfwWindow, Mouse_Listener::mouseScrollCallBack);
                GLFW.glfwSetKeyCallback(this.glfwWindow, Key_Listener::keyCallBack);
                GLFW.glfwMakeContextCurrent(this.glfwWindow);
                GLFW.glfwSwapInterval(1);
                GLFW.glfwShowWindow(this.glfwWindow);
                GL.createCapabilities();

                Window.changeScene(0);
            }
        }
    }

    public void loop() {

        float beginTime = (float)glfwGetTime();
        float endTime;
        float dt = -1.0f;

        //this is the looping
        //checks if the user hit the close button or alt+f4 or similar closing keys
        while(!GLFW.glfwWindowShouldClose(this.glfwWindow)) {

            //processes events in the event queue and returns immediately
            GLFW.glfwPollEvents();

            GL11.glClearColor(r, g, b, a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if(dt >= 0) currentScene.update(dt);

            GLFW.glfwSwapBuffers(this.glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

    }
}
