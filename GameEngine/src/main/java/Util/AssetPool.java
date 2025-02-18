package Util;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import Components.Sprite;
import Components.SpriteSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import renderer.Shader;
import renderer.Texture;

public class AssetPool {

    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(AssetPool.class);


    public static void addSpriteSheet(String filename, SpriteSheet spriteSheet){

        File file = new File(filename);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){

            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String filename){
        File file = new File(filename);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            logger.error("Tried to access sprite sheet but found that it was not added to AssetPool ", filename);
            assert false:"";
        }

        return AssetPool.spriteSheets.get(file.getAbsolutePath());
    }


    public static Shader getShader(String resourceName){

        File file = new File(resourceName);

        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return AssetPool.shaders.get(file.getAbsolutePath());
        }
        else{
            Shader shader = new Shader(resourceName);
            shader.compileAndLink();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName){

        File file = new File(resourceName);

        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        }
        else{
            Texture texture = new Texture(resourceName);
            AssetPool.textures.put(resourceName, texture);
            return texture;
        }
    }

}
