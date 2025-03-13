package renderer;

import Util.AssetPool;
import Util.JMath;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {

    private static final int maxLines = 500;
    private static List<Line2D> lines = new ArrayList<>();

    //6 floats per vertex, 2 vertices per line

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

        //remove deadlines
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
        glDrawArrays(GL_LINES, 0, lines.size());

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

        DebugDraw.lines.add(new Line2D(from, to, color, lifetime));
    }

    //========================
    //ADD BOX 2D
    //========================

    public static void addBox2D(Vector2f center, Vector2f dimensions){

        addBox2D(center, dimensions, new Vector3f(0,1,0), 3, 0.0f);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, Vector3f color){

        addBox2D(center, dimensions, color, 3, 0.0f);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, Vector3f color, float lifetime){

        addBox2D(center, dimensions, color, lifetime, 0.0f);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, Vector3f color, float lifetime,
                                float rotation){

        //bottom left corner
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        //top right corner
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        //all vertices
        Vector2f[] vertices = {
                new Vector2f(min),
                new Vector2f(min.x, max.y),
                new Vector2f(max),
                new Vector2f(max.x, min.y)
        };

        if(rotation!=0.0f){

            for(Vector2f vert: vertices){

                JMath.rotate(vert, rotation, center);
            }
        }

        addLine2D(vertices[0], vertices[1],color, lifetime);
        addLine2D(vertices[0], vertices[3],color, lifetime);
        addLine2D(vertices[1], vertices[2],color, lifetime);
        addLine2D(vertices[2], vertices[3],color, lifetime);
        System.out.println("Inside draw box 2D.");
    }

    //========================
    //ADD CIRCLE 2D
    //========================
    public static void addCircle2D(Vector2f center, float radius){

        addCircle2D(center, radius, new Vector3f(1,0,1), 3);
    }

    public static void addCircle2D(Vector2f center, float radius, Vector3f color){

        addCircle2D(center, radius, color, 3);
    }

    public static void addCircle2D(Vector2f center, float radius, Vector3f color, float lifetime){

        Vector2f[] points = new Vector2f[20];

        float increment = 360.f / points.length;
        float currentAngle = 0;

        for(int i = 0; i < points.length; i++){

            Vector2f temp = new Vector2f(radius, 0);
            JMath.rotate(temp, currentAngle, new Vector2f());
            points[i] = new Vector2f(temp).add(center);

            if( i > 0){
                addLine2D(points[i-1], points[i], color, lifetime);
            }

            currentAngle+=increment;
        }

        addLine2D(points[points.length - 1], points[0], color, lifetime);
    }
}
