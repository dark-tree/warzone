#version 330 core

out vec4 color;

in vec2 uv;
in vec4 tint;

void main() {
    color = tint.rgba;
}