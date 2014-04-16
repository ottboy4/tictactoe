package ott.image.data;

public class Kernel
{
	private int width;
	private int height;
	private int[] data;
	
	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
	public int getHalfWidth()
	{
		return width / 2;
	}
	
	public int getHalfHeight()
	{
		return height / 2;
	}

	public int[] getData()
	{
		return data;
	}
	
	public Kernel(int width, int height, int[] data)
	{
		this.width = width;
		this.height = height;
		if (width * height > data.length)
			throw new IllegalArgumentException("Int array is smaller than the width * height");
		this.data = new int[width * height];
		System.arraycopy(data, 0, this.data, 0, width * height);
	}
}
