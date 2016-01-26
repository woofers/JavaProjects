package com.jaxson.lib.gdx.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.jaxson.lib.gdx.graphics.MyColor;
import com.jaxson.lib.util.MyMath;

public class GdxMath extends MyMath
{
	public static final float DEGREES_TO_RADIANS = MathUtils.degreesToRadians;
	public static final float RADIANS_TO_DEGREES = MathUtils.radiansToDegrees;

	private GdxMath()
	{

	}

	public static Vector3 absVector(Vector3 vector)
	{
		return vector.set(abs(vector.x), abs(vector.y), abs(vector.z));
	}

	public static Vector3 divideVector(Vector3 vector, float scalar)
	{
		return vector.scl(reciprocal(scalar));
	}

	public static Vector3 divideVector(Vector3 vector, Vector3 vector2)
	{
		return vector.set(vector.x / vector2.x, vector.y / vector2.y, vector.z / vector2.z);
	}

	public static Color randColor()
	{
		return randColor(MyColor.MIN_VALUE_INT, MyColor.MAX_VALUE_INT);
	}

	public static Color randColor(Color minColor, Color maxColor)
	{
		return new MyColor(randFloat(minColor.r, maxColor.r), randFloat(minColor.g, maxColor.g),
				randFloat(minColor.b, maxColor.b));
	}

	public static Color randColor(int minRGB, int maxRGB)
	{
		return randColor(minRGB, maxRGB, minRGB, maxRGB, minRGB, maxRGB);
	}

	public static Color randColor(int minR, int maxR, int minG, int maxG, int minB, int maxB)
	{
		return new MyColor(randInt(minR, maxR), randInt(minG, maxG), randInt(minB, maxB));
	}

	public static Vector3 randVector3(float min, float max)
	{
		return new Vector3(randFloat(min, max), randFloat(min, max), randFloat(min, max));
	}

	public static Vector3 reciprocalVector(Vector3 vector)
	{
		return vector.set(reciprocal(vector.x), reciprocal(vector.y), reciprocal(vector.z));
	}
}
