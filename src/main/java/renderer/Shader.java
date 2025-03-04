package renderer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Shader {

    private int shaderProgramId;
    private boolean beingUsed = false;

    private String vertexSource, fragmentSource, filePath;
    private static final Logger logger = LoggerFactory.getLogger(Shader.class);


    public Shader(String filepath) {

        this.filePath = filepath;

        //NOTE
        //this entire thing is just splitting the shader files into two (for vertex and fragment)
        //this can be completely skipped by simply making two separate files
        //one for vertex, and one for fragment
        try{

            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )[a-zA-Z]+"); //match any #type XXXX and split the string there

            int index = source.indexOf("#type") + 6; //index of next word after #type
            int eol = source.indexOf("\r\n", index); //takes us to end of the word after #type
            String firstPattern = source.substring(index,eol).trim();

            //second #type
            index = source.indexOf("#type",eol)+6; //
            //\r moves cursor to the beginning of the line, \n moves it to the next line
            eol = source.indexOf("\r\n",index); //basically moving it to the beginning of the next line
            String secondPattern = source.substring(index,eol).trim();

            if(firstPattern.equalsIgnoreCase("vertex")){
                vertexSource = splitString[1];
            }
            else if(firstPattern.equalsIgnoreCase("fragment")){
                fragmentSource = splitString[1];
            }else{
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if(secondPattern.equalsIgnoreCase("vertex")){
                vertexSource = splitString[2];
            }
            else if(secondPattern.equalsIgnoreCase("fragment")){
                fragmentSource = splitString[2];
            }else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

        }catch(IOException e){

            logger.error("Error: Could not open file for shader", e);
            assert false: "Error could not open file for shader. '" + filepath + "'";
        }
    }

    public void compileAndLink(){

        int vertexID, fragmentID;
        //compile and link the shaders

        //first load and compile vertex
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        if(vertexID == 0)
        {
            logger.error("Failed to create vertex shader: {} (glCreateShader returned 0)", vertexID);
            assert false: "Trial worked";
        }

        //pass the shader source code to gl
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);


        //check and see if there were errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if(success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            logger.error("Vertex Shader Compilation Failed for '{}'", filePath);
            logger.error("Shader Compilation Log:\n{}", glGetShaderInfoLog(vertexID, len));
            assert false : "Vertex Shader Compilation Failed";

        }

        //first load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //pass the shader source code to gl
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        //check and see if there were errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);

        if(success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            logger.error("Error: '{}' \n\tFragment Shader Compilation Failed", filePath);
            logger.error(glGetShaderInfoLog(fragmentID, len));
            assert false:"";
        }

        //======================================
        //link shaders and check for errors
        //=======================================
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId,vertexID);
        glAttachShader(shaderProgramId, fragmentID);
        glLinkProgram(shaderProgramId);

        //check for linking errors
        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            logger.error("Error: '{}' \n\tShader Program Compilation Failed", filePath);
            logger.error(glGetProgramInfoLog(shaderProgramId, len));
            assert false:"";
        }

    }

    public void use(){
        //bind shader program
        if(!beingUsed)
        {
            glUseProgram(shaderProgramId);
            beingUsed = true;
        }
    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varname, Matrix4f mat4){

        int varLocation = glGetUniformLocation(shaderProgramId,varname);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16); //4x4 matrix
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);

    }

    public void uploadVec4f(String varname, Vector4f vec4f){
        int varLocation = glGetUniformLocation(shaderProgramId,varname);
        use();
        glUniform4f(varLocation, vec4f.x, vec4f.y,vec4f.z,vec4f.w);
    }

    public void uploadFloat(String varname, float val){
        int varLocation = glGetUniformLocation(shaderProgramId,varname);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varname, int val){
        int varLocation = glGetUniformLocation(shaderProgramId,varname);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot){
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array){
        int varLocation = glGetUniformLocation(shaderProgramId,varName);
        use();
        glUniform1iv(varLocation, array);
    }


}
