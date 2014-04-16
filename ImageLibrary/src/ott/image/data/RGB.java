package ott.image.data;

public class RGB
{
	public int r;
	public int g;
	public int b;
	
	public RGB()
	{
	}
	
	public RGB(int value)
	{
		r = (value & 0xFF0000) >> 16;
		g = (value & 0xFF00) >> 8;
		b = (value & 0xFF);	
	}
	
	public RGB(int r,int g,int b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void divide(int divisor)
	{
		this.r /= divisor;
		this.g /= divisor;
		this.b /= divisor;
	}
	
	public void multiply(int multiplier)
	{
		this.r *= multiplier;
		this.g *= multiplier;
		this.b *= multiplier;
	}
	
	public void add(RGB addend)
	{
		this.r += addend.r;
		this.g += addend.g;
		this.b += addend.b;
	}
	
	public int max()
	{
		return Math.max(r, Math.max(g, b));
	}
	
	public int min()
	{
		return Math.min(r, Math.min(g, b));
	}
	
	public int sum()
	{
		return r + g + b;
	}
	
	public int average()
	{
		return sum() / 3;
	}
	
	public void removeNegatives()
	{
		r = (r < 0) ? 0 : r;
		g = (g < 0) ? 0 : g;
		b = (b < 0) ? 0 : b;
	}
	
	public int toInt()
	{
		int value = 0;
		value |= r << 16;
		value |= g << 8;
		value |= b;
		return value;
	}
}
