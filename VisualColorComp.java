
public class VisualColorComp implements ColorComparator
{
	@Override
	public float compare(Color a, Color b)
	{
		float sa = 0.0f;
		sa += a.r * 0.212671f;
		sa += a.g * 0.715160f;
		sa += a.b * 0.072169f;
		
		float sb = 0.0f;
		sb += b.r * 0.212671f;
		sb += b.g * 0.715160f;
		sb += b.b * 0.072169f;
		
		float diff = sa - sb;
		
		return Math.abs(diff);
		//return diff * diff;
	}
	
}
