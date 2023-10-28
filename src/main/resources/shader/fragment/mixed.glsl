#version 330 core

out vec4 color;

in vec2 uv;
in vec4 tint;

uniform sampler2D sampler;

void main() {

    color = mix(texture(sampler, uv), vec4(tint.rgb, 1), tint.a);

}