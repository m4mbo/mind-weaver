#ifdef GL_ES
precision mediump float;
#endif

#define period 20.
#define amplitude 0.4
#define threshold .2

varying vec4 v_color;   // takes value of vertex
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_time;
uniform vec2 u_resolution;

float compute_sine(float normalizedX) {
    float x = period * (normalizedX + u_time);
    return sin(x);
}

float compute_sine_prime(float normalizedX) {
    float x = period * normalizedX;
    return cos(x);
}

void main() {
    vec2 uv = v_texCoords;
    vec2 norm_uv = uv;

    float y = amplitude * compute_sine(norm_uv.x);
    float norm_y = (y+1.0) / 2.0;

    float dfx = compute_sine_prime(norm_uv.x);

    vec3 black = vec3(0);
    float distanceEstimate = abs(norm_y - norm_uv.y) / sqrt(1.0 + dfx*dfx);

    vec3 a = mix(vec3(1.0), black, smoothstep(0.0, threshold, distanceEstimate));

    vec4 col = texture2D(u_texture, v_texCoords) * v_color;

    vec3 temp = col.rgb * a;

    if (temp.r + temp.g + temp.b < 1.) {
        gl_FragColor = vec4(temp, 0.0);
    } else {
        gl_FragColor = vec4(temp, 1.0);
    }

}