// Adapted from:
// <a href="http://callumhay.blogspot.com/2010/09/gaussian-blur-shader-glsl.html" target="_blank" rel="nofollow">http://callumhay.blogspot.com/2010/09/gaussian-blur-shader-glsl.html</a>
 
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif
 
#define PROCESSING_TEXTURE_SHADER
 
uniform sampler2D texture;
uniform float mult;
uniform float max;
 
varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform vec2 resolution;

void main(void) {
  float x = 1.0 / resolution.x;
  float y = 1.0 / resolution.y;
  
  
  vec4 horizEdge = vec4( 0.0 );
  horizEdge -= texture2D( texture, vec2( vertTexCoord.x - x, vertTexCoord.y - y ) ) * 1.0;
  horizEdge -= texture2D( texture, vec2( vertTexCoord.x - x, vertTexCoord.y     ) ) * 2.0;
  horizEdge -= texture2D( texture, vec2( vertTexCoord.x - x, vertTexCoord.y + y ) ) * 1.0;
  horizEdge += texture2D( texture, vec2( vertTexCoord.x + x, vertTexCoord.y - y ) ) * 1.0;
  horizEdge += texture2D( texture, vec2( vertTexCoord.x + x, vertTexCoord.y     ) ) * 2.0;
  horizEdge += texture2D( texture, vec2( vertTexCoord.x + x, vertTexCoord.y + y ) ) * 1.0;
  
  vec4 vertEdge = vec4( 0.0 );
  vertEdge -= texture2D( texture, vec2( vertTexCoord.x - x, vertTexCoord.y - y ) ) * 1.0;
  vertEdge -= texture2D( texture, vec2( vertTexCoord.x    , vertTexCoord.y - y ) ) * 2.0;
  vertEdge -= texture2D( texture, vec2( vertTexCoord.x + x, vertTexCoord.y - y ) ) * 1.0;
  vertEdge += texture2D( texture, vec2( vertTexCoord.x - x, vertTexCoord.y + y ) ) * 1.0;
  vertEdge += texture2D( texture, vec2( vertTexCoord.x    , vertTexCoord.y + y ) ) * 2.0;
  vertEdge += texture2D( texture, vec2( vertTexCoord.x + x, vertTexCoord.y + y ) ) * 1.0;
  
  vec3 edge = sqrt((horizEdge.rgb * horizEdge.rgb) + (vertEdge.rgb * vertEdge.rgb));
  
  float col = (edge.x+edge.y+edge.z)/3;
  
  gl_FragColor = vec4(1.0 - (col*mult),1.0 - (col*mult),1.0 - (col*mult),1.0);
  
}