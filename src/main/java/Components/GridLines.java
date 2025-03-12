package Components;

import Util.settings;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.debugDraw;

public class GridLines extends Component{

    @Override
    public void update(float dt){

        Vector2f cameraPos = Window.getScene().camera().position;
        Vector2f projectionSize = Window.getScene().camera().getProjectionSize();

        int firstX = ((int)(cameraPos.x / settings.gridWidth) - 1) * settings.gridWidth;
        int firstY = ((int)(cameraPos.y / settings.gridHeight) - 1) * settings.gridHeight;

        int numVerticalLines = (int)(projectionSize.x / settings.gridWidth) + 2;
        int numHorizontalLines = (int)(projectionSize.y / settings.gridHeight) + 2;

        int height = (int)projectionSize.y + settings.gridHeight * 2;
        int width = (int)projectionSize.x + settings.gridWidth * 2;

        int maxLines = Math.max(numHorizontalLines, numVerticalLines);
        Vector3f clr = new Vector3f(0.2f, 0.2f, 0.2f);

        for(int i = 0; i < maxLines; i++){

            int x = firstX + (settings.gridWidth * i);
            int y = firstY + (settings.gridHeight * i);

            if(i < numVerticalLines){

                debugDraw.addLine2D(new Vector2f(x,firstY), new Vector2f(x, firstY + height), clr);
            }

            if(i < numHorizontalLines){

                debugDraw.addLine2D(new Vector2f(firstX,y), new Vector2f(firstX + width, y), clr);
            }
        }
    //TODO: MAKE GRID LINES IN BACKGROUND, NOT FOREGROUND
    }

}
