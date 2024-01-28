#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;   // takes value of vertex
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;

void main() {
    vec2 uv = v_texCoords;
    vec4 col = texture2D(u_texture, v_texCoords) * v_color;
    float a = abs(sin(u_time));
    vec3 temp = col.rgb + a * 0.4;
    gl_FragColor = vec4(temp, col.a);
}