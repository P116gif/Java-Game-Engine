#type vertex
#version 330

layout(location=0) in vec3 aPos; //anything starting with 'a' is an attribute
layout(location=1) in vec4 aCol;
layout(location=2) in vec2 aTexCords;

uniform mat4 ProjectionMatrix, viewMatrix;

out vec4 fCol; //anything starting with 'f' is going to the fragment shader
out vec2 fTexCords;

void main()
{
    fCol = aCol;
    fTexCords = aTexCords;
    gl_Position = ProjectionMatrix * viewMatrix * vec4(aPos, 1.0);
}


#type fragment
#version 330 //not mine though the tutorial's

in vec4 fCol;
in vec2 fTexCords;

uniform float time;
uniform sampler2D texSampler;

out vec4 Col;

void main()
{
    float noise = fract(sin(dot(fCol.xy, vec2(12.9898,78.233))) * 43758.5453);
    Col = texture(texSampler, fTexCords);
}