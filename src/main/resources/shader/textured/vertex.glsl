#version 330 core

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 tex;
layout (location = 2) in vec4 rgb;

uniform vec2 scale;
uniform vec2 offset;

out vec2 uv;
out vec4 tint;

void main() {

    // set Z to -1 in 2D mode
    // the camera in OpenGL faces towards -Z
    gl_Position = vec4((offset.x + pos.x) * scale.x, (offset.y + pos.y) * scale.y, -1.0, 1.0);

    uv = tex;
    tint = rgb;

}