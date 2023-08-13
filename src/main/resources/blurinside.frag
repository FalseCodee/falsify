#version 330 core

out vec4 color;

in vec2 v_TexCoord;
in vec2 v_OneTexel;

uniform sampler2D u_Texture;
uniform sampler2D u_Inside;
uniform float u_Radius;
uniform vec2 u_Direction;
uniform vec2 u_Size;

const int Quality = 8;
const int Directions = 16;
const float Pi = 6.28318530718;//pi * 2

void main()
{
    vec4 originalColor = texture2D(u_Inside, v_TexCoord);
    if(originalColor.a < 0.8 && originalColor.a > 0.2) {
        vec2 radius = u_Radius / u_Size.xy;
        vec4 Color = texture2D(u_Texture, v_TexCoord);
        for (float d = 0.0;d < Pi; d += Pi / float(Directions))
        {
            for (float i = 1.0 / float(Quality);i <= 1.0; i += 1.0 / float(Quality))
            {
                Color += texture2D(u_Texture, v_TexCoord + vec2(cos(d), sin(d)) * radius * i);
            }
        }
        Color /= float(Quality) * float(Directions) + 1.0;
        if(originalColor.xyz == 0) originalColor.xyz += 1-originalColor.a;
        color = vec4(Color.xyz * (originalColor.xyz), 1.0);
    } else {
        color = originalColor;
    }
}
