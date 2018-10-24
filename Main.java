import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Reading in.png");
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File("in.png"));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		ColorComparator comp = new DistColorComp();
		int kernelsize = 10;
		float sigmap = 4f;
		float sigmai = 0.08f;
		int times = 4;
		BilateralFilter filter = new BilateralFilter(comp , kernelsize, sigmap, sigmai);

		Color[][] colors = Color.convert(image);

		System.out.println("applying bilateral filter " + times + " times");
		for(int i = 0; i < times; ++i)
		{
			long time = System.currentTimeMillis();
			filter.filter(colors);
			time = System.currentTimeMillis() - time;
			System.out.println(time+"ms");
		}
		BufferedImage out = Color.convert(colors);

		System.out.println("Writing out.png");
		try
		{
			ImageIO.write(out, "png", new File("out.png"));
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
