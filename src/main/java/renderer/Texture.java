package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Texture {

    //simply for debugging
    private String filepath;
    private int textureId;
    private int width, height;

    private static Logger logger = LoggerFactory.getLogger(Texture.class);

    public Texture(){
    }

    public Texture(int width, int height) {
        //ONLY FOR FRAME BUFFER FOR NOW
        this.filepath = "Generated";

        //generate texture on GPU
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0,
                GL_RGB, GL_UNSIGNED_BYTE, 0);

        //created space withing this texture ID
    }

    public void init(String filepath){

        this.filepath = filepath;

        //generate texture on gpu
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        //set the texture parameters
        //if uv coordinates greater than texture width and height, repeat the image

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //when stretching the image, pixelate the image instead of blur
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        //when shrinking an image, pixelate don't blue
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //load the image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if(image!=null){
            this.width = width.get(0);
            this.height = height.get(0);
            //uploads image to gpu with exact formatting
            if(channels.get(0)==3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }
            else if(channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
            else{
                logger.error("Number of channels in texture uploaded unknown: {}", channels.get(0));
                assert false:"";
            }
        }
        else{
            logger.error("Texture file could not load image from file: {}", filepath);
            assert false: "";
        }


        stbi_image_free(image);
    }



    public void bind(){

        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void unbind(){

        glBindTexture(GL_TEXTURE_2D,0);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getId() {

        return textureId;
    }

    public String getFilePath(){

        return this.filepath;
    }

    @Override
    public boolean equals(Object o){

        if(o == null) return false;

        if(!(o instanceof Texture)) return false;

        Texture otex = (Texture) o;

        return otex.getWidth() == this.width && otex.getHeight() == this.height
                && otex.getId() == this.textureId && otex.getFilePath().equals(this.filepath);
    }
}
