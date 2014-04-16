package ott.image.data;


public enum Direction 
{
	ANGLE_0(1, 0), 
	ANGLE_45(1, -1), 
	ANGLE_90(0, 1), 
	ANGLE_135(1, 1);
	
	private final Point forward;

	private Direction(int h, int v)
	{
		forward = new Point(h, v);
	}

	public Point backwardLine()
	{
		return new Point(forward.y, -forward.x);
	}

	public Point forwardLine()
	{
		return new Point(-forward.y, forward.x);
	}
	
	public Point forwardGradient()
	{
		return new Point(forward);
	}
	
	public Point backwardGradient()
	{
		return new Point(-forward.x, -forward.y);
	}

	public static Direction determineDirection(double rad)
	{
		double deg = Math.toDegrees(rad);
		Direction result;
		if (-22.5 < deg && deg <= 22.5)
			result = ANGLE_0;
		else if (22.5 < deg && deg <= 67.5)
			result = ANGLE_45;
		else if (67.5 < deg && deg <= 90 || -67.5 >= deg && deg >= -90)
			result = ANGLE_90;
		else if (-22.5 >= deg && deg > -67.5)
			result = ANGLE_135;
		else
			throw new IllegalArgumentException("Angle must be within -90 and 90 degrees");
		return result;
	}
}