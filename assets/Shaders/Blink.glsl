#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;   // takes value of vertex
uniform float u_time;

void main() {
    float a = abs(sin(u_time));
    gl_FragColor = v_col * a;
}