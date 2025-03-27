package Editor;

import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import imgui.ImGui;
import jade.Mouse_Listener;
import jade.Window;
import org.joml.Vector2f;

public class GameViewWindow {

    public static void imgui(){

        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeforViewport();
        ImVec2 windowPos = getCenteredPosforViewport(windowSize);

        //draw the next thing at the given position
        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topleft = new ImVec2();
        ImGui.getCursorScreenPos(topleft);

        topleft.x -= ImGui.getScrollX();
        topleft.y -= ImGui.getScrollY();

        int textureID = Window.getFrameBuffer().getTextureID();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0,1, 1, 0);

        Mouse_Listener.setGameViewportPos(new Vector2f(topleft.x, topleft.y));
        Mouse_Listener.setGameViewportSize(new Vector2f(windowSize.x, windowSize.y));
        ImGui.end();
    }

    private static ImVec2 getCenteredPosforViewport(ImVec2 aspectSize) {

        ImVec2 windowSize = new ImVec2();

        //wthout scrolling, how big is the inner window?
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        //find position of top left corner of the box we show our game inside
        //do this by subtracting half of aspect size from the center of the screen
        float viewportX = (windowSize.x / 2.0f) - aspectSize.x/2.0f;
        float viewportY = (windowSize.y / 2.0f) - aspectSize.y/2.0f;

        return new ImVec2(viewportX + ImGui.getCursorPosX(),
                viewportY + ImGui.getCursorPosY());

    }

    private static ImVec2 getLargestSizeforViewport() {

        ImVec2 windowSize = new ImVec2();

        //wthout scrolling, how big is the inner window?
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight= aspectWidth / Window.getTargetAspectRatio();

        if(aspectHeight > windowSize.y){

            //switch to pillarbox mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }


}
