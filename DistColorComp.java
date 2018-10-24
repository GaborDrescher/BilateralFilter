
public class DistColorComp implements ColorComparator
{
	@Override
	public float compare(Color a, Color b)
	{
		float dr = a.r - b.r;
		float dg = a.g - b.g;
		float db = a.b - b.b;
		
		float sq = dr*dr + dg*dg + db*db;
		return (float)Math.sqrt(sq);
		//return sq;
	}
	
}
