#version 330 core

out vec4 color;

in vec2 v_TexCoord;
in vec2 v_OneTexel;

uniform sampler2D u_Texture;
uniform float u_Radius;
uniform vec2 u_Size;
uniform float u_Scale;

void main()
{
    vec2 uv = v_TexCoord;
    vec2 halfpixel = v_OneTexel / 2.0;
    float offset = u_Radius * (u_Scale / u_Scale);

    vec4 sum = texture(u_Texture, uv) * 4.0;
    sum += texture(u_Texture, uv - halfpixel.xy * offset);
    sum += texture(u_Texture, uv + halfpixel.xy * offset);
    sum += texture(u_Texture, uv + vec2(halfpixel.x, -halfpixel.y) * offset);
    sum += texture(u_Texture, uv - vec2(halfpixel.x, -halfpixel.y) * offset);

    color = vec4(sum.xyz / 8.0, 1.0);
}