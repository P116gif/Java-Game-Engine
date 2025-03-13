package renderer;

import jade.GameObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {

    private static final Logger logger = LoggerFactory.getLogger(FrameBuffer.class);

    private int fboID = 0;
    private Texture texture = null;

    public FrameBuffer(int width, int height){

        //generate framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        //create texture to render the data to and attach it to our frame buffer
        this.texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,this.texture.getId(), 0);

        //create render buffer which stores depth data
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);

        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);


        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){

            logger.error("Error frame buffer is not complete in {}" ,this.getClass());
            System.exit(-1);
        }

        //unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFboID() {
        return fboID;
    }

    public int getTextureID() {
        return texture.getId();
    }

}
