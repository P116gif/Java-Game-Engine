package jade;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;


public class ImGuiLayer {

    boolean showtext = false;


    public void initImGui(){
        ImGui.createContext();
        final ImFontAtlas fontAtlas = ImGui.getIO().getFonts();
        final ImFontConfig fontConfig = new ImFontConfig();
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets\\fonts\\agency.TTF", 32, fontConfig);

    }

    public void imGui(){

        ImGui.begin("Please work.");

        ImGui.end();
    }
}
