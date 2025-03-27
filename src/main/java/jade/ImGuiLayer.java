package jade;

import Editor.GameViewWindow;
import Editor.PropertiesWindow;
import Scenes.Level_Editor_Scene;
import Scenes.Scene;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import renderer.PickingTexture;

import java.util.logging.Level;


public class ImGuiLayer {

    private PropertiesWindow propertiesWindow;

    public void initImGui(){

        ImGui.createContext();
        final ImFontAtlas fontAtlas = ImGui.getIO().getFonts();
        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets\\fonts\\agency.TTF", 20, fontConfig);

        //enable docking
        final ImGuiIO io = ImGui.getIO();
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
    }

    public void imGui(Scene currentScene){

        ImGui.newFrame();
        setupDockSpace();
        currentScene.imgui();
        GameViewWindow.imgui();
        propertiesWindow.update(currentScene);
        propertiesWindow.imgui();
        ImGui.end();
        ImGui.render();
    }

    private void setupDockSpace() {

        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        //always start at top left corner of screen
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                        ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                        ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("DocSpace demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        //Dockspace
        //without it, the windows will take up the whole screen when docking and won't let other windows
        //show up
        ImGui.dockSpace(ImGui.getID("DockSpace"));

    }

    public void setPropertiesWindow(PickingTexture pT) {

        this.propertiesWindow = new PropertiesWindow(pT);
    }
}
