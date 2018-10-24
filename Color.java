import java.awt.image.BufferedImage;

public class Color
{
    public float r;
    public float g;
    public float b;

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public Color() {
    }
    
    public Color copy()
    {
        return new Color(r, g, b);
    }
	
	public void reset()
	{
		r = g = b = 0.0f;
	}
	
	public void add(Color other, float factor)
	{
		r += other.r * factor;
		g += other.g * factor;
		b += other.b * factor;
	}
	
	public static Color[][] convert(BufferedImage image)
	{
		int w = image.getWidth();
		int h = image.getHeight();
		
		Color[][] out = new Color[h][w];
		
		float inv255 = 1.0f / 255.0f;
		for(int y = 0; y < h; ++y)
		{
			for(int x = 0; x < w; ++x)
			{
				java.awt.Color color = new java.awt.Color(image.getRGB(x, y));
				Color c = new Color(color.getRed() * inv255, color.getGreen() * inv255, color.getBlue() * inv255);
				out[y][x] = c;
			}
		}
		
		return out;
	}
	
	public static BufferedImage convert(Color[][] colors)
	{
		int h = colors.length;
		int w = colors[0].length;
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		for(int y = 0; y < h; ++y)
		{
			for(int x = 0; x < w; ++x)
			{
				Color c = colors[y][x];
				java.awt.Color color = new java.awt.Color(clamp(c.r), clamp(c.g), clamp(c.b));
				image.setRGB(x, y, color.getRGB());
			}
		}
		
		return image;
	}
	
	private static float clamp(float x)
	{
		return x < 0.0f ? 0.0f : (x > 1.0f ? 1.0f : x);
	}
}
