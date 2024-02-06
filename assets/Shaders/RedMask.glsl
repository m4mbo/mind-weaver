#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;   // takes value of vertex
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    vec4 col = texture2D(u_texture, v_texCoords) * v_color;

    col.r += 0.5;

    gl_FragColor = col;
}