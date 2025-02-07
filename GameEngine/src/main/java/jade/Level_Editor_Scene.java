package jade;

import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Level_Editor_Scene extends Scene {

    private String vertexShaderSource = "#version 330 core\n" +
            "\n" +
            "layout(location=0) in vec3 aPos; \n" +
            "layout(location=1) in vec4 aCol;\n" +
            "\n" +
            "out vec4 fCol;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fCol = aCol;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    private String fragmentShaderSource="#version 330 core\n" +
            "\n" +
            "in vec4 fCol;\n" +
            "\n" +
            "out vec4 Col;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    Col = fCol;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram; //identifiers so that gpu knows what cpu is sending it

    private float[] vertexArray = {
            //positions                 //color
            0.5f, -0.5f, 0.0f,          1.0f,0.0f,0.0f,1.0f, //bottom right == 0
            0.5f, 0.5f, 0.0f,           1.0f,1.0f,0.0f,1.0f, //top right  == 1
            -0.5f, -0.5f, 0.0f,         0.0f,1.0f,0.0f,1.0f, //bottom left == 2
            -0.5f, 0.5f, 0.0f,          0.0f,1.0f,1.0f,1.0f //top left == 3
    };

    //MUST BE IN COUNTERCLOCKWISE ORDER
    private int[] elementArray = {

            /*
                x       x

                x       x
             */

            0,1,3, //bottom right, top right and then top left form a  triangle
            0,3,2  //bottom right, top left and bottom left form a triangle


    };

    //vertex array object, vertex buffer object, element buffer object
    private int vaoID, vboID, eboID;

    private Shader defaultShader = new Shader("assets/shaders/default.glsl");

    public Level_Editor_Scene(){}

    @Override
    public void init(){

        defaultShader.compileAndLink();

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer  of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create the vbo and upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        //create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        //EBO
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //add the vertex attribute pointers, ie, which attribute comes first, which next etx
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        //assigning for position
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        //assigning for color
        //basically tells gpu that colors start at index 1 (in glsl file), they are float type, to go to next color you need to travel vertexsizebytes long and that the start of the first color is at positionsize*floatsize
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize*floatSizeBytes);
        glEnableVertexAttribArray(1);

    }




    @Override
    public void update(float deltaTime) {

        defaultShader.use();

        //bind VAO that we're using
        glBindVertexArray(vaoID);

        //enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind after drawing
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        defaultShader.detach();

    }


}
