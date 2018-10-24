
public class BilateralFilter
{
	private int kernelSize;
	private float sigmap;
	private float sigmai;
	private float[][] pKernel;
	private ColorComparator ccomp;

	public BilateralFilter(ColorComparator ccomp, int kernelSize, float sigmap, float sigmai)
	{
		this.ccomp = ccomp;
		this.kernelSize = kernelSize;
		this.sigmap = sigmap;
		this.sigmai = sigmai;

		pKernel = make2DKernel(kernelSize, sigmap);
	}

	public void filter(final Color[][] dest, final Color[][] src)
	{
		int h = dest.length;
		int w = dest[0].length;
		int kernHalf = kernelSize / 2;
		
		for(int y = 0; y < h; ++y)
		{
			for(int x = 0; x < w; ++x)
			{
				float correction = 0.0f;
				Color out = dest[y][x];
				out.reset();
				
				
				
				for(int ky = 0; ky < kernelSize; ++ky)
				{
					int imgKy = y + ky - kernHalf;
					if(imgKy < 0) continue;
					if(imgKy >= h) break;
					
					for(int kx = 0; kx < kernelSize; ++kx)
					{
						int imgKx = x + kx - kernHalf;
						if(imgKx < 0) continue;
						if(imgKx >= w) break;
						
						float pkVal = pKernel[ky][kx];
						float ikVal = gaussianKernel(ccomp.compare(src[y][x], src[imgKy][imgKx]), sigmai);
						Color other = src[imgKy][imgKx];
						
						float factor = pkVal * ikVal;
						correction += factor;
						
						out.add(other, factor);
					}
				}
				
				correction = 1.0f / correction;
				out.r *= correction;
				out.g *= correction;
				out.b *= correction;
			}
		}
	}

	public void filter(Color[][] colors)
	{
		int h = colors.length;
		int w = colors[0].length;

		Color[][] copy = new Color[h][w];

		for(int y = 0; y < h; ++y)
		{
			for(int x = 0; x < w; ++x)
			{
				copy[y][x] = new Color();
			}
		}

		this.filter(copy, colors);

		for(int y = 0; y < h; ++y)
		{
			for(int x = 0; x < w; ++x)
			{
				colors[y][x] = copy[y][x];
			}
		}
	}

	private static float gaussianKernel(float x, float sigma)
	{
		return (1.0f / ((float)Math.sqrt(2.0 * Math.PI) * sigma)) * (float)Math.exp(-(x * x) / (2.0f * sigma * sigma));
	}
	
	private static float[][] make2DKernel(int size, float sigma)
	{
		if((size & 1) == 0)
		{
			++size;
		}
		
		if(size <= 0)
		{
			size = 1;
		}
		
		float[][] out = new float[size][size];

		int mid = size / 2;
		
		final float twoSigmaSqInv = 1.0f / (-2.0f * sigma * sigma);
		final float piSigInv = (1.0f / ((float)Math.PI)) * (-twoSigmaSqInv);
		for(int y = 0; y < size; ++y)
		{
			for(int x = 0; x < size; ++x)
			{
				float fx = x - mid;
				float fy = y - mid;
		
				float val = piSigInv * (float)Math.exp((fx * fx + fy * fy) * twoSigmaSqInv);
				out[y][x] = val;
			}
		}

		float sum = 0.0f;
		for(int y = 0; y < size; ++y)
		{
			for(int x = 0; x < size; ++x)
			{
				sum += out[y][x];
			}
		}
		
		sum = 1.0f / sum;
		for(int y = 0; y < size; ++y)
		{
			for(int x = 0; x < size; ++x)
			{
				out[y][x] *= sum;
			}
		}
		
		/*
		System.out.println();
		System.out.println("kernel:");
		for(int y = 0; y < size; ++y)
		{
			for(int x = 0; x < size; ++x)
			{
				System.out.print(out[y][x] + " ");
			}
			System.out.println();
		}
		*/
		return out;
	}
}
