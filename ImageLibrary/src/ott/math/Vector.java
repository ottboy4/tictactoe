package ott.math;

public class Vector
{
	public float x;
	public float y;
	
	public Vector()
	{
	}
	// Vector2 constructor
	public Vector(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float length()
	{
		return (float) Math.sqrt(dot(this));
	}
	public float lengthSquared()
	{
		return (float) dot(this);
	}
	public Vector normalized()
	{
		float length = length();
		float x = this.x / length;
		float y = this.y / length;
		return new Vector(x, y);
	}
	public float dot(Vector that)
	{
		return this.x * that.x + this.y * that.y;
	}

	public Vector perpCW()
	{
		return new Vector(y, -x);
	}
	public Vector perpCCW()
	{
		return new Vector(-y, x);
	}
	
	public static Vector add(Vector left, Vector right)
	{
		float x = left.x + right.x;
		float y = left.y + right.y;
		return new Vector(x,y);
	}
	
	public static Vector subtract(Vector left, Vector right)
	{
		float x = left.x - right.x;
		float y = left.y - right.y;
		return new Vector(x,y);
	}
	
	public static Vector multiply(Vector left, Vector right)
	{
		float x = left.x * right.x;
		float y = left.y * right.y;
		return new Vector(x,y);
	}
	
	public static Vector divide(Vector left, Vector right)
	{
		float x = left.x / right.x;
		float y = left.y / right.y;
		return new Vector(x,y);
	}
}
