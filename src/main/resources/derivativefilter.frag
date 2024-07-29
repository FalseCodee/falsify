#version 330 core

out vec4 color;

in vec2 v_TexCoord;
in vec2 v_OneTexel;

uniform sampler2D u_Texture;

void main()
{
    vec4 originalColor = texture2D(u_Texture, v_TexCoord);
    color = vec4(originalColor.xyz, 1.0);
}
