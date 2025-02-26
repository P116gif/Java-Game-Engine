package jade;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;


public class ImGuiLayer{

    public final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    public final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private long glfwWindow;
    private boolean showText = false;

    public ImGuiLayer(long windowPtr){
        this.glfwWindow = windowPtr;
        init();
    }

    private void init() {
        ImGui.createContext();

        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init("#version 330");
    }

    public void run(){
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        imgui();
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void imgui() {
        ImGui.begin("New window");

        if(ImGui.button("Click me")){
             showText = true;
        }

        if(showText){
            ImGui.text("Some random text");
            ImGui.sameLine();
            if(ImGui.button("Stop text")){
                showText = false;
            }
        }
        ImGui.end();
    }

    public void destroy(){

        imGuiGlfw.shutdown();
        imGuiGl3.shutdown();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
    }


}
