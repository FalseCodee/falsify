#version 330 core

out vec4 color;

in vec2 v_TexCoord;
in vec2 v_OneTexel;

uniform sampler2D u_Texture;
uniform float u_Radius;
uniform vec2 u_Size;
uniform vec2 u_Pos;


void main()
{
    vec4 Color;
    for( float i=0.0;i<1.0;i+=1.0/float(u_Radius) )
    {
        Color += texture2D( u_Texture, v_TexCoord+(0.5-u_Pos)*i/u_Radius);
    }
    Color /= float(u_Radius);
    color =  Color;
}

