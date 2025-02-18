#type vertex
#version 330

layout(location=0) in vec3 aPos; //anything starting with 'a' is an attribute
layout(location=1) in vec4 aCol;
layout(location=2) in vec2 aTexCords;
layout(location=3) in float aTexID;

uniform mat4 projectionMatrix, viewMatrix;

out vec4 fCol; //anything starting with 'f' is going to the fragment shader
out vec2 fTexCords;
out float fTexID;

void main()
{
    fCol = aCol;
    fTexCords = aTexCords;
    fTexID = aTexID;
    gl_Position = projectionMatrix * viewMatrix * vec4(aPos, 1.0);
}


#type fragment
#version 330 //not mine though the tutorial's

in vec4 fCol;
in vec2 fTexCords;
in float fTexID;
out vec4 Col;

uniform sampler2D texMatrix[8];

void main()
{
    float noise = fract(sin(dot(fCol.xy, vec2(12.9898,78.233))) * 43758.5453);

    if(fTexID>=0){
        Col = fCol * texture(texMatrix[int(fTexID)], fTexCords);
    }
    else{
        Col = fCol;
    }
}