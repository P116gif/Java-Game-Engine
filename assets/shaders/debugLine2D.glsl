#type vertex
#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aCol;


out vec3 fCol;

uniform mat4 uProjection;
uniform mat4 uView;

void main(){

    fCol = aCol;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}


#type fragment
#version 330 core

in vec3 fCol;

out vec4 Col;

void main(){

    Col = vec4(fCol, 1);
}