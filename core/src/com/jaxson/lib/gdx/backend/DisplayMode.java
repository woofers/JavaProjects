package com.jaxson.lib.gdx.backend;

import com.badlogic.gdx.math.Vector2;
import com.jaxson.lib.util.Printer;

public class DisplayMode
{
    private static class MaxDisplay
            extends com.badlogic.gdx.Graphics.DisplayMode
    {
        private static final int MAX = Integer.MAX_VALUE;

        MaxDisplay()
        {
            super(MAX, MAX, MAX, MAX);
        }
    }

    private static class MinDisplay
            extends com.badlogic.gdx.Graphics.DisplayMode
    {
        MinDisplay()
        {
            super(0, 0, 0, 0);
        }
    }

    private static final int BPP = 32;
    private static final int REFRESH_RATE = 60;
    private static final boolean FULLSCREEN = false;

    public static final DisplayMode WORST
            = new DisplayMode(new MinDisplay(), true);
    public static final DisplayMode BEST
            = new DisplayMode(new MaxDisplay(), true);

    private int width;
    private int height;
    private int refreshRate;
    private int bitsPerPixel;
    private boolean fullscreen;

    public DisplayMode(com.badlogic.gdx.Graphics.DisplayMode displayMode)
    {
        this(displayMode, FULLSCREEN);
    }

    public DisplayMode(com.badlogic.gdx.Graphics.DisplayMode displayMode,
            boolean fullscreen)
    {
        this(displayMode.width,
                displayMode.height,
                displayMode.refreshRate,
                displayMode.bitsPerPixel,
                fullscreen);
    }

    public DisplayMode(int width, int height)
    {
        this(width, height, REFRESH_RATE);
    }

    public DisplayMode(int width, int height, int refreshRate)
    {
        this(width, height, refreshRate, BPP);
    }

    public DisplayMode(int width,
            int height,
            int refreshRate,
            boolean fullscreen)
    {
        this(width, height, refreshRate, BPP, fullscreen);
    }

    public DisplayMode(int width, int height, int refreshRate, int bitsPerPixel)
    {
        this(width, height, refreshRate, bitsPerPixel, FULLSCREEN);
    }

    public DisplayMode(int width,
            int height,
            int refreshRate,
            int bitsPerPixel,
            boolean fullscreen)
    {
        this.width = width;
        this.height = height;
        this.refreshRate = refreshRate;
        this.bitsPerPixel = bitsPerPixel;
        this.fullscreen = fullscreen;
    }

    public int area()
    {
        return width() * height();
    }

    public float aspectRatio()
    {
        return (float) width() / (float) height();
    }

    public int bitsPerPixel()
    {
        return bitsPerPixel;
    }

    public Vector2 center()
    {
        return size().scl(0.5f);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof DisplayMode)) return false;
        DisplayMode display = (DisplayMode) other;
        return width() == display.width()
                && height() == display.height()
                && refreshRate() == display.refreshRate()
                && bitsPerPixel() == display.bitsPerPixel()
                && fullscreen() == display.fullscreen();
    }

    public boolean fullscreen()
    {
        return fullscreen;
    }

    public int height()
    {
        return height;
    }

    public int refreshRate()
    {
        return refreshRate;
    }

    public Vector2 size()
    {
        return new Vector2(width(), height());
    }

    public com.badlogic.gdx.Graphics.DisplayMode toBestDisplayMode(
            com.badlogic.gdx.Graphics.DisplayMode[] displayModes)
    {
        com.badlogic.gdx.Graphics.DisplayMode bestMode = new MinDisplay();
        com.badlogic.gdx.Graphics.DisplayMode worstMode = new MaxDisplay();
        for (com.badlogic.gdx.Graphics.DisplayMode mode: displayModes)
        {
            if (mode.height >= bestMode.height && mode.height <= height())
            {
                if (mode.width >= bestMode.width && mode.width <= width())
                {
                    if (mode.width == bestMode.width
                            && mode.height == bestMode.height)
                    {
                        if (mode.refreshRate > bestMode.refreshRate
                                && mode.refreshRate <= refreshRate())
                        {
                            bestMode = mode;
                        }
                    }
                    else if (mode.refreshRate <= refreshRate())
                    {
                        bestMode = mode;
                    }
                }
            }

            if (worstMode.width > mode.width) worstMode = mode;
        }
        return bestMode.width != 0 ? bestMode : worstMode;
    }

    @Override
    public String toString()
    {
        return new Printer(getClass(),
                new Printer.Label("Width", width()),
                new Printer.Label("Height", height()),
                new Printer.Label("Refresh Rate", refreshRate()),
                new Printer.Label("Bits Per Pixel", bitsPerPixel()),
                new Printer.Label("Aspect Ratio", aspectRatio()),
                new Printer.Label("Fullscreen", fullscreen())).toString();
    }

    public int width()
    {
        return width;
    }
}
