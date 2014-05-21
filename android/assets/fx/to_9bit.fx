//Effect Dynamically converts texture to 9-bit color

sampler TextureSampler : register(s0);

float4 main(float4 color : COLOR0, float2 texcoord : TEXCOORD0) : COLOR0
{
	//Look up texture color
	float4 tex = tex2D(TextureSampler, texcoord);

	//converts it to 9-bit color
	float4 pixel = tex.rgba;
	pixel += float4(0.125, 0.125, 0.125, 0.125);
	int4 ipixel = pixel * float4(0.125, 0.125, 0.125, 1.0);
	//pixel = ipixel * 8;
	tex.rgb = pixel;

	return tex.rgba;
}

technique to_9bit
{
    pass Pass1
    {
        PixelShader = compile ps_2_0 main();
    }
}
