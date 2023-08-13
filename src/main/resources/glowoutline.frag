#version 330 core

out vec4 color;

in vec2 v_TexCoord;
in vec2 v_OneTexel;

uniform sampler2D u_Texture;
uniform float u_Radius;
uniform vec2 u_Direction;
uniform vec2 u_Size;

const int Quality = 8;
const int Directions = 16;
const float Pi = 6.28318530718;//pi * 2

void main()
{
    vec2 radius = u_Radius/u_Size.xy;
    vec4 Color = texture2D( u_Texture, v_TexCoord);
    int checks = 0;
    if(Color.a != 0) color = vec4(0,0,0,0);
    else {
        for (float d = 0.0;d < Pi; d += Pi / float(Directions))
        {
            for (float i = 1.0 / float(Quality);i <= 1.0; i += 1.0 / float(Quality))
            {
                vec4 texColor = texture2D(u_Texture, v_TexCoord + vec2(cos(d), sin(d)) * radius * i);
                if (texColor.a != 0) {
                    Color += texColor;
                    checks++;
                }
            }
        }
        Color.xyz /= checks;
        Color.a /= ((float(Quality) * float(Directions)) - checks);
        color = Color;
    }
}