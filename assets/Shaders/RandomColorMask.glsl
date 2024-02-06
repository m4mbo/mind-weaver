#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;   // takes value of vertex
varying vec2 v_texCoords;
uniform float r;
uniform float g;
uniform float b;
uniform sampler2D u_texture;

void main() {
    vec4 col = texture2D(u_texture, v_texCoords) * v_color;

    col.r *= r;
    col.g *= g;
    col.b *= b;

    gl_FragColor = col;
}
