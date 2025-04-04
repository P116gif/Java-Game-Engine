package renderer;

import Util.AssetPool;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class debugDraw {

    private static int maxLines = 500;
    private static List<Line2D> lines = new ArrayList<>();

    //6 floats per vertex, 2 vertes per line

    private static float[] vertexArray = new float[maxLines * 12];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID, vboID;

    private static boolean started = false;

    public static void start(){

        //generate the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create vbo and buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //enable the vertex array attributes
        glVertexAttribPointer(0,3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(1.0f);
    }

    public static void beginFrame(){

        if(!started){
            start();
            started = true;
        }

        //remove dead lines
        for(int i = 0; i < lines.size(); i++){
            if(lines.get(i).beginFrame() <= 0){
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw(){

        if(lines.isEmpty()) return;

        int index = 0;

        for(Line2D line: lines){

            for(int i = 0; i < 2; i++){

                Vector2f position = i == 0? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                //load position into float array
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = 20.0f;

                //load color into array
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index+=6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);                                               //index - 6?
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 12));

        //use our shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        //bind vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 12 );

        //disable
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        //unbind shader
        shader.detach();
    }

    //=========================
    //ADD Line 2D method
    //=========================
    public static void addLine2D(Vector2f from, Vector2f to){

        addLine2D(from, to, new Vector3f(0,1,0),120);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color){

        addLine2D(from, to, color,120);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, float lifetime){

        if(lines.size() >= maxLines) return;

        debugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

}
