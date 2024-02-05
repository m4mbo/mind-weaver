#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;   // takes value of vertex
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    vec4 col = texture2D(u_texture, v_texCoords) * v_color;

    if (col.rgb == vec3(42.0 / 255.0, 35.0 / 255.0, 83.0 / 255.0)) {
        col = vec4(67.0 / 255.0, 12.0 / 255.0, 81.0 / 255.0, 1.0);
    }

    gl_FragColor = col;
}