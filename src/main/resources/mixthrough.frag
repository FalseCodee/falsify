#version 330 core

precision lowp float;

in vec2 uv;
out vec4 color;

uniform sampler2D uTextureBase;
uniform sampler2D uTextureShader;

void main() {
    vec4 base = texture(uTextureBase, uv);
    vec4 shader = texture(uTextureShader, uv);

    color = mix(base, shader, shader.w);
}