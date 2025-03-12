package jade;

import Scenes.Scene;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;


public class ImGuiLayer {


    public void initImGui(){
        ImGui.createContext();
        final ImFontAtlas fontAtlas = ImGui.getIO().getFonts();
        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets\\fonts\\agency.TTF", 20, fontConfig);

    }

    public void imGui(Scene currentScene){

        ImGui.newFrame();
        currentScene.sceneImgui();
        ImGui.render();
    }
}
