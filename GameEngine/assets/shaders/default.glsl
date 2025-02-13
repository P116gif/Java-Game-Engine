#type vertex
#version 330

layout(location=0) in vec3 aPos; //anything starting with 'a' is an attribute
layout(location=1) in vec4 aCol;

uniform mat4 projectionMatrix, viewMatrix;

out vec4 fCol; //anything starting with 'f' is going to the fragment shader

void main()
{
    fCol = aCol;
    gl_Position = projectionMatrix * viewMatrix * vec4(aPos, 1.0);
}


#type fragment
#version 330 //not mine though the tutorial's

in vec4 fCol;
out vec4 Col;

void main()
{
    float noise = fract(sin(dot(fCol.xy, vec2(12.9898,78.233))) * 43758.5453);
    Col = fCol;
}