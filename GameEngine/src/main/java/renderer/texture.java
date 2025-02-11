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

public class texture {

    //simply for debugging
    private String filepath;
    private int textureId;

    private static Logger logger = LoggerFactory.getLogger(texture.class);

    public texture(String filepath){

        this.filepath = filepath;

        //generate texture on gpu
        textureId = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
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
}
