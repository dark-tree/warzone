#version 330 core

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 tex;

uniform vec2 scale;

out vec2 uv;

void main() {

    // set Z to -1 in 2D mode
    // the camera in OpenGL faces towards -Z
    gl_Position = vec4(pos.x * scale.x, pos.y * scale.y, -1.0, 1.0);

    uv = tex;

}