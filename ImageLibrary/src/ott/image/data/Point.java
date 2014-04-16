package ott.image.data;

public class Point
{
	public int x;
	public int y;
	
	public Point()
	{
	}
	
	@Override
	public String toString()
	{
		return "Point [x=" + x + ", y=" + y + "]";
	}

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(Point other)
	{
		this(other.x, other.y);
	}

	public void translate(int dx, int dy)
	{
		x += dx;
		y += dy;
	}
	
	public void translate(Point other)
	{
		x += other.x;
		y += other.y;
	}
	
	public static int lowestY(Point ... points)
	{
		int lowest = points[0].y;
		for (int i = 1; i < points.length; i++)
		{
			if (points[i].y < lowest)
				lowest = points[i].y;
		}
		return lowest;
	}
	
	public static int lowestX(Point ... points)
	{
		int lowest = points[0].x;
		for (int i = 1; i < points.length; i++)
		{
			if (points[i].x < lowest)
				lowest = points[i].x;
		}
		return lowest;
	}
	
	public static int highestY(Point ... points)
	{
		int lowest = points[0].y;
		for (int i = 1; i < points.length; i++)
		{
			if (points[i].y > lowest)
				lowest = points[i].y;
		}
		return lowest;
	}
	
	public static int highestX(Point ... points)
	{
		int lowest = points[0].x;
		for (int i = 1; i < points.length; i++)
		{
			if (points[i].x > lowest)
				lowest = points[i].x;
		}
		return lowest;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
}
