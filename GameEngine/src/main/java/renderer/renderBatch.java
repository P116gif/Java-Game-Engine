package renderer;

import Components.SpriteRenderer;
import jade.Window;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class renderBatch {

    //vertex
    //===============
    //Position                          Color
    //float,float                       float,float,float,float

    private final int posSize = 2;
    private final int colorSize = 4;

    //offsets need to be in bytes
    private final int posOffset = 0;
    private final int colorOffset = posOffset + posSize* Float.BYTES;

    //size of one vertex
    private final int vertexSize = 6;
    private final int vertexSizeBytes = vertexSize * Float.BYTES;

    //sprite container, number of sprites
    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasSpace;

    //one giant array for all vertices
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;


    public renderBatch(int size){

        //to know how many you want to render at once
        this.maxBatchSize = size;

        shader = new Shader("assets/shaders/default.glsl");
        shader.compileAndLink();

        this.sprites = new SpriteRenderer[maxBatchSize];

        //how many float we will need for our vertices
        //each sprite will have 4 vertices and each vertex has 6 floats
        vertices = new float[maxBatchSize * 4 * vertexSize];

        this.numSprites = 0;
        this.hasSpace = true;
    }

    public void start(){

        //generate and bind a vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        //since the indices will never change, they can be static drawing
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //enable the buffer attribute pointers that tell the buffer where our vertex attributes
        //are in the array
        glVertexAttribPointer(0, posSize, GL_FLOAT, false, vertexSizeBytes, posOffset);
        glEnableVertexAttribArray(0);

        //enable color array
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, colorOffset);
        glEnableVertexAttribArray(1);

    }

    public void addSprites(SpriteRenderer spr){

        //get index and add rendered object
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        //add properties to the local vertices array
        loadVertexProperties(index);

        if(numSprites>=this.maxBatchSize){
            this.hasSpace = false;
        }
    }

    public void render(){
        //for now, we shall rebuffer all the data
        //god save my laptop

        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferSubData(GL_ARRAY_BUFFER,0, vertices);

        //use shaders
        shader.use();
        shader.uploadMat4f("projectionMatrix", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("viewMatrix", Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        //UNBIND EVERYTHING MWAHAHAHAHA
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(int index){

        SpriteRenderer spr = this.sprites[index];

        //find offset within array (there are 4 vertices per sprite)
        int offset = index * 4 * vertexSize;

        Vector4f color = spr.getColor();

        //add vertices with appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for(int i = 0; i < 4; i++){
            if(i==1){
                yAdd = 0.0f;
            }
            else if(i==2){
                xAdd = 0.0f;
            }
            else if(i==3){
                yAdd = 1.0f;
            }

            //load position
            vertices[offset] = spr.gameObject.transform.position.x + (xAdd * spr.gameObject.transform.scale.x);
            vertices[offset+1] = spr.gameObject.transform.position.y + (yAdd * spr.gameObject.transform.scale.y);

            //load color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            offset+=vertexSize;

        }
    }
    private int[] generateIndices() {

        //6 indices per square since we have 3 indices per triangle
        int[] elements = new int[6 * maxBatchSize];

        for(int i = 0; i < maxBatchSize; i++){

            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {

        //each individual quad contains 6 vertices
        int offsetArrayIndex = 6 * index;

        //the difference from vertex 0 of first square to vertex 0 of second square is 4
        int offset = 4 * index;

        //Triangle 1 - 0,2,3
        /*
            3   2
            1   0
         */

        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        //Triangle 2 - 3,1,0
        elements[offsetArrayIndex+3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;

    }


    public boolean hasSpace() {
        return this.hasSpace;
    }
}
