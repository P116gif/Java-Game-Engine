package renderer;

import Components.SpriteRenderer;
import Util.AssetPool;
import jade.Window;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch> {


    private int zIndex;

    private List<Texture> textures;
    private int[] texSlots = {0,1,2,3,4,5,6,7};
    //vertex
    //===============
    //Position                          Color                           texCords            texID
    //float,float                       float,float,float,float         float, float        float

    private final int posSize = 2;
    private final int colorSize = 4;
    private final int texCordsSize = 2;
    private final int texIDSize = 1;

    //offsets need to be in bytes
    private final int posOffset = 0;
    private final int colorOffset = posOffset + posSize* Float.BYTES;
    private final int texCordsOffset = colorOffset + colorSize* Float.BYTES;
    private final int texIDOffset = texCordsOffset + texCordsSize* Float.BYTES;

    //size of one vertex
    private final int vertexSize = 9;
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


    public RenderBatch(int size, int z){

        //to know how many you want to render at once
        this.maxBatchSize = size;
        this.zIndex = z;
        shader = AssetPool.getShader("assets/shaders/default.glsl");

        this.sprites = new SpriteRenderer[maxBatchSize];

        //how many float we will need for our vertices
        //each sprite will have 4 vertices and each vertex has 6 floats
        vertices = new float[maxBatchSize * 4 * vertexSize];

        this.numSprites = 0;
        this.hasSpace = true;
        this.textures = new ArrayList<>();
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

        glVertexAttribPointer(2, texCordsSize, GL_FLOAT, false, vertexSizeBytes, texCordsOffset);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, texIDSize, GL_FLOAT, false, vertexSizeBytes, texIDOffset);
        glEnableVertexAttribArray(3);

    }


    public void render(){

        // for every site in the sprite array,
        //if the sprite is dirty, then reload the vertex properties
        //and rebuffer the data to be drawn on the screen

        boolean rebufferData = false;
        for(int i = 0; i < numSprites; i++){

            if(sprites[i].isDirty()){
                loadVertexProperties(i);
                sprites[i].setClean();
                rebufferData = true;
            }
        }

        //for now, we shall rebuffer all the data
        //god save my laptop >> god saved my laptop, nothing happened until this if was added

        if(rebufferData){
            glBindBuffer(GL_ARRAY_BUFFER,vboID);
            glBufferSubData(GL_ARRAY_BUFFER,0, vertices);
        }

        //use shaders
        shader.use();
        shader.uploadMat4f("projectionMatrix", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("viewMatrix", Window.getScene().camera().getViewMatrix());

        //bind textures
        for(int i = 0; i < textures.size(); i++){
            //System.out.println("Activated slot: " + i);
            glActiveTexture(GL_TEXTURE0+i);
            textures.get(i).bind();
        }

        shader.uploadIntArray("texMatrix", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        //UNBIND EVERYTHING MWAHAHAHAHA
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (Texture texture : textures) {
            texture.unbind();
        }

        shader.detach();
    }
    public void addSprites(SpriteRenderer spr){

        //get index and add rendered object
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        Texture tex = spr.getTexture();

        if(tex!=null){
            if(!textures.contains(tex)){
                //System.out.println("Added: " + tex);
                textures.add(tex);
            }

        }
        //add properties to the local vertices array
        loadVertexProperties(index);

        if(numSprites>=this.maxBatchSize){
            this.hasSpace = false;
        }
    }

    private void loadVertexProperties(int index){

        SpriteRenderer spr = this.sprites[index];

        //find offset within array (there are 4 vertices per sprite)
        int offset = index * 4 * vertexSize;

        Vector4f color = spr.getColor();
        Vector2f[] texCords = spr.getTexCords();

        //assign zero if there is no texture pack attached
        int texID = 0;

        if(spr.getTexture()!=null){
            //System.out.println("The texture added is: " + spr.getTexture());
            for(int i = 0; i < textures.size(); i++){
                if(textures.get(i).equals(spr.getTexture())){
                    //System.out.println("Got texture " + textures.get(i));
                    texID = i;
                    break;
                }
            }
        }
        //System.out.println("The tex ID is: " + texID);

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

            //load position from transform of the sprite
            vertices[offset] = spr.gameObject.transform.position.x + (xAdd * spr.gameObject.transform.scale.x);
            vertices[offset+1] = spr.gameObject.transform.position.y + (yAdd * spr.gameObject.transform.scale.y);

            //load color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            //load tex coordinates
            vertices[offset+6] = texCords[i].x;
            vertices[offset+7] = texCords[i].y;

            //load texture id
            vertices[offset+8] = texID;

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

    public boolean hasTextureRoom(){
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture t){
        return this.textures.contains(t);
    }

    public int getzIndex() {
        return zIndex;
    }

    //to compare batches based on their z index
    @Override
    public int compareTo(@NotNull RenderBatch o) {
        return Integer.compare(this.zIndex, o.getzIndex());
    }
}
