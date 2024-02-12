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

    float a = abs(sin(u_time * 2.5));

    float subtractAmount = 0.5;
    float finalAlpha = max(a - subtractAmount, 0.0);

    vec3 temp = col.rgb + finalAlpha;
    gl_FragColor = vec4(temp, col.a);
}