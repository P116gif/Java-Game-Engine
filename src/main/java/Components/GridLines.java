package Components;

import Util.Settings;
import jade.Camera;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;

public class GridLines extends Component{

    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        int firstX = ((int) (cameraPos.x / Settings.gridWidth) - 1) * Settings.gridHeight;
        int firstY = ((int) (cameraPos.y / Settings.gridHeight) - 1) * Settings.gridHeight;

        int numVtLines = (int) (projectionSize.x * camera.getZoom() / Settings.gridWidth) + 2;
        int numHzLines = (int) (projectionSize.y * camera.getZoom() / Settings.gridHeight) + 2;

        int height = (int) (projectionSize.y * camera.getZoom()) + Settings.gridHeight * 2;
        int width = (int) (projectionSize.x * camera.getZoom()) + Settings.gridWidth * 2;

        int maxLines = Math.max(numVtLines, numHzLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);

        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (Settings.gridWidth * i);
            int y = firstY + (Settings.gridHeight * i);

            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}
