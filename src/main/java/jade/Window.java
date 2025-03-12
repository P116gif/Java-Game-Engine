//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package jade;

import java.util.Objects;

import Scenes.Level_Editor_Scene;
import Scenes.Level_Scene;
import Scenes.Scene;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import renderer.debugDraw;

public class Window {

    private final int width = 1920;
    private final int height = 1080;
    private final String title = "Game Engine";
    private long glfwWindow;
    private static Window window = null;
    private static final Logger logger = LoggerFactory.getLogger(Window.class);
    public float r,b,g,a;

    //imgui
    protected ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    protected ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private String glslVersion = null;
    private ImGuiLayer imGuiLayer = null;

    private static Scene currentScene = null;

    private Window(ImGuiLayer imGuiLayer) {
        r=1;
        b=1;
        g=1;
        a=1;
        this.imGuiLayer = imGuiLayer;
    }

    public static void changeScene(int newScene){
        switch (newScene)
        {
            case 0:
                currentScene = new Level_Editor_Scene();
                break;
            case 1:
                currentScene = new Level_Scene();
                break;
            default:
                assert false: "Unknown scene '" + newScene + "'";
                break;
        }

        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window(new ImGuiLayer());
        }

        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }


    public void run() {
        System.out.println("Hello lwjgl " + Version.getVersion() + "!");

        this.init();

        imGuiLayer.initImGui();
        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);

        this.loop();

        Callbacks.glfwFreeCallbacks(this.glfwWindow);
        GLFW.glfwDestroyWindow(this.glfwWindow);
        GLFW.glfwTerminate();
        ((GLFWErrorCallback)Objects.requireNonNull(GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)null))).free();
    }

    public void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
           logger.error("Unable to initialise GLFW window.");
           System.exit(-1);
        }

        //imgui things
        glslVersion = "#version 330";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        //glfw
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(131076, 0);
        GLFW.glfwWindowHint(131075, 1);
        GLFW.glfwWindowHint(131080, 1);

        //creating thw window pointer
        this.glfwWindow = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0L, 0L);

        if (this.glfwWindow == 0L) {
            logger.error("Failed to create GLFW window.");
            System.exit(-1);
        }

        GLFW.glfwSetCursorPosCallback(this.glfwWindow, Mouse_Listener::mousePosCallBack);
        GLFW.glfwSetMouseButtonCallback(this.glfwWindow, Mouse_Listener::mouseButtonCallBack);
        GLFW.glfwSetScrollCallback(this.glfwWindow, Mouse_Listener::mouseScrollCallBack);
        GLFW.glfwSetKeyCallback(this.glfwWindow, Key_Listener::keyCallBack);

        //show the window
        GLFW.glfwMakeContextCurrent(this.glfwWindow);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(this.glfwWindow);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        // new color = sourceColor(source alpha) * destinationColor(1-sourceAlpha)
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        Window.changeScene(0);
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

            debugDraw.beginFrame();

            GL11.glClearColor(r, g, b, a);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            //imgui
            imGuiGlfw.newFrame();
            imGuiGl3.newFrame();
            imGuiLayer.imGui(currentScene);
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupCurrentContext = GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupCurrentContext);
            }

            //while window has not closed, keep calling update function
            if(dt >= 0){

                debugDraw.draw();
                currentScene.update(dt);

            }
            GLFW.glfwSwapBuffers(this.glfwWindow);
            GLFW.glfwPollEvents();

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;

        }

        currentScene.saveAsFile();
    }
}
