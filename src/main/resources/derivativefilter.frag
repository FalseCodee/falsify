#version 330 core

out vec4 color;

in vec2 v_TexCoord;
in vec2 v_OneTexel;

uniform sampler2D u_Texture;

void main()
{
    vec4 originalColor = texture2D(u_Texture, v_TexCoord);
    vec4 originalColorAbove = texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - v_OneTexel.y));

    color = vec4((originalColor.xyz - originalColorAbove.xyz) / 2 + 0.5, 1.0);
}
