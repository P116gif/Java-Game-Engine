package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramId;

    private String vertexSource, fragmentSource, filePath;

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

            e.printStackTrace();
            assert false: "Error could not open file for shader. '" + filepath + "'";
        }
    }

    public void compileAndLink(){

        int vertexID, fragmentID;
        //compile and link the shaders

        //first load and compile vertex
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        //pass the shader source code to gl
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        //check and see if there were errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if(success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: 'defaultShader.glsl'\n\tVertex Shader Compilation Failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false:"";
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
            System.out.println("Error: '"+ filePath + "'\n\tFragment Shader Compilation Failed");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
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
            System.out.println("Error: '" + filePath + "' \n\tShader Program Compilation Failed");
            System.out.println(glGetProgramInfoLog(shaderProgramId, len));
            assert false:"";
        }

    }

    public void use(){
        //bind shader program
        glUseProgram(shaderProgramId);
    }

    public void detach(){
        glUseProgram(0);
    }
}
