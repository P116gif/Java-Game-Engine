package jade;

import Util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.texture;

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
            //positions                 //color                     //texture XY coordinates
            200.5f, 0.5f, 0.0f,           1.0f,0.0f,0.0f,1.0f,      1,0,    //bottom right == 0
            200.5f, 100.5f, 0.0f,         1.0f,1.0f,0.0f,1.0f,      1,1,    //top right  == 1
            100.5f, 0.5f, 0.0f,           0.0f,1.0f,0.0f,1.0f,      0,0,    //bottom left == 2
            100.5f, 100.5f, 0.0f,          0.0f,1.0f,1.0f,1.0f,      0,1     //top left == 3
    };

    //MUST BE IN COUNTERCLOCKWISE ORDER
    //an element buffer holds the order in which openGL will connect the vertices
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

    private Shader defaultShader;
    private texture testTex;

    public Level_Editor_Scene(){}

    @Override
    public void init(){

        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compileAndLink();
        this.testTex = new texture("C:\\Users\\parij\\Pictures\\Genshin\\1cf9739e7a28662b3d73f515350b4c7d.jpg");


        //generate the vertex array object
        //this will contain the information about how to travel the vertex buffer
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create and upload vertex buffer object
        //this is the object that holds all the data about the vertex
        vboID = glGenBuffers();
        //giving array buffer the number vboID
        glBindBuffer(GL_ARRAY_BUFFER,vboID);

        //copies x,y,z values from the vertex buffer to GPU memory
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
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        //assigning for position                                                stride
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        //assigning for color
        //basically tells gpu that colors start at index 1 (in glsl file), they are float type, to go to next color you need to travel vertexsizebytes long and that the start of the first color is at positionsize*floatsize
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize*Float.BYTES);
        glEnableVertexAttribArray(1);

        //assigning UV/XY coordinated for texture
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);

        //we don't need the buffer object anymore
        glBindBuffer(GL_ARRAY_BUFFER,0);

    }




    @Override
    public void update(float deltaTime) {

        camera.position.x -= deltaTime * 50.f;

        defaultShader.use();

        //upload texture to shader
        //slot 0 means we want to upload the textureId at slot 0
        defaultShader.uploadTexture("texSampler", 0);
        glActiveTexture(GL_TEXTURE0);
        testTex.bind();

        defaultShader.uploadMat4f("ProjectionMatrix", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("viewMatrix", camera.getViewMatrix());
        defaultShader.uploadFloat("time", Time.getTime());



        //bind VAO that we're using
        glBindVertexArray(vaoID);

        //enable vertex attribute pointers
        //glEnableVertexAttribArray(0);
        //glEnableVertexAttribArray(1);

        //checks what is bound to the vertex array and then draws using info from that
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind after drawing
        //glDisableVertexAttribArray(0);
        ///glDisableVertexAttribArray(1);
        //glBindVertexArray(0);

        defaultShader.detach();

    }


}
