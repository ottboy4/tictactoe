package ott.image.data;

public class Line
{
	public int x1;
	public int x2;
	public int y1;
	public int y2;
	public int w;
	public int h;
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + h;
		result = prime * result + w;
		result = prime * result + x1;
		result = prime * result + x2;
		result = prime * result + y1;
		result = prime * result + y2;
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
		Line other = (Line) obj;
		if (h != other.h)
			return false;
		if (w != other.w)
			return false;
		if (x1 != other.x1)
			return false;
		if (x2 != other.x2)
			return false;
		if (y1 != other.y1)
			return false;
		if (y2 != other.y2)
			return false;
		return true;
	}
	
	public Line()
	{
	}

	public Line(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		int highX = Math.max(x1, x2);
		int lowX = Math.min(x1, x2);
		int highY = Math.max(y1, y2);
		int lowY = Math.min(y1, y2);
		w = highX - lowX;
		h = highY - lowY;
	}
	
	/**
	 * Builds a line using x and y start coordinates combined with the lines width and height
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public static Line buildLine(int x, int y, int w, int h)
	{
		return new Line(x, y, x + w, y + h);
	}

	@Override
	public String toString()
	{
		return "Line [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", w=" + w + ", h=" + h + "]";
	}
}
