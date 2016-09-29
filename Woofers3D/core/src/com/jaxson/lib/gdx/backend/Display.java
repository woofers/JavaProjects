package com.jaxson.lib.gdx.backend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.BufferFormat;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jaxson.lib.gdx.GameConfig;
import com.jaxson.lib.gdx.graphics.DisplayOrientation;
import com.jaxson.lib.gdx.graphics.color.MyColor;
import com.jaxson.lib.gdx.graphics.g2d.Screenshot;
import com.jaxson.lib.gdx.graphics.views.View;
import com.jaxson.lib.gdx.input.Inputs;
import com.jaxson.lib.gdx.input.KeyCombination;
import com.jaxson.lib.gdx.input.Keyboard;
import com.jaxson.lib.gdx.input.KeyboardKey;
import com.jaxson.lib.gdx.input.Keys;
import com.jaxson.lib.gdx.input.Mouse;
import com.jaxson.lib.gdx.input.TouchScreen;
import com.jaxson.lib.gdx.util.GameObject;
import com.jaxson.lib.io.Jsonable;
import com.jaxson.lib.math.MyMath;

/**
 * A class that handles the display and rendering.
 * Contains the {@link Camera}, {@link Viewport}, {@link SpriteBatch},
 * {@link ModelBatch}.
 * @author Jaxson Van Doorn
 * @since 1.0
 */
public class Display extends GameObject
{
	private static final int CLEAR_MASK = GL20.GL_COLOR_BUFFER_BIT
										| GL20.GL_DEPTH_BUFFER_BIT;

	private static final int COVERAGE_SAMPLING_MASK
			= GL20.GL_COVERAGE_BUFFER_BIT_NV;

	private static final int EMPTY_MASK = GL20.GL_ZERO;
	private static final Color CLEAR_COLOR = Color.ROYAL;
	private static final int NATIVE_ROTATION_OFFSET = 90;

	private Game game;
	private View view;
	private boolean minimized;
	private boolean paused;
	private int lastWindowedWidth;
	private int lastWindowedHeight;

	private Keyboard keyboard;
	private Mouse mouse;
	private Keys fullscreenKey;
	private KeyboardKey pauseKey;
	private KeyboardKey screenshotKey;
	private TouchScreen touchScreen;

	/**
	 * Constructs the display.
	 * @param game Reference to the {@link Game}
	 */
	public Display(Game game)
	{
		this.game = game;
		this.view = new View(width(), height());
		this.lastWindowedWidth = defaultWidth();
		this.lastWindowedHeight = defaultHeight();

		this.keyboard = Inputs.keyboard();
		this.mouse = Inputs.mouse();
		this.touchScreen = Inputs.touchScreen();
		this.pauseKey = keyboard.key("Escape");
		this.screenshotKey = keyboard.key("F12");
		this.fullscreenKey
			= new Keys(keyboard.key("F11"),
					   new KeyCombination(
					   		new Keys(keyboard.key("L-Alt"),
									 keyboard.key("R-Alt")),
							keyboard.key("Enter")));

		setFullscreen(startsFullscreen());
	}

	/**
	 * Gets whether the {@link Game} supports fullscreen.
	 * @return {@link boolean} - Whether the {@link Game} supports fullscreen
	 */
	public boolean allowsFullscreen()
	{
		return config().allowsFullscreen();
	}

	/**
	 * Clears the screen with the {@link #clearMask()} and clear color.
	 */
	public void clearScreen()
	{
		clearScreen(CLEAR_COLOR);
		clearScreen(clearMask());
	}

	/**
	 * Clears the screen with a {@link Color}.
	 * @param color The {@link Color} to clear the screen
	 */
	public void clearScreen(Color color)
	{
		clearScreen(color.r, color.g, color.b, color.a);
	}

	/**
	 * Clears the screen with a RGB color that is fully opaque.
	 * @param r The red conponent of the color
	 * @param g The green conponent of the color
	 * @param b The blue conponent of the color
	 */
	public void clearScreen(float r, float g, float b)
	{
		clearScreen(r, g, b, MyColor.MAX_VALUE_FLOAT);
	}

	/**
	 * Clears the screen with a RGBA color.
	 * @param r The red conponent of the color
	 * @param g The green conponent of the color
	 * @param b The blue conponent of the color
	 * @param a The alpha conponent of the color
	 */
	public void clearScreen(float r, float g, float b, float a)
	{
		gl().glClearColor(r, g, b, a);
	}

	/**
	 * Clears the screen with a {@link int} mask.
	 * @param mask The clear mask
	 */
	public void clearScreen(int mask)
	{
		gl().glClear(mask);
	}

	@Override
	public void dispose()
	{
		view.dispose();
	}

	/**
	 * Gets the aspect ratio of the {@link Game}.
	 * @return {@link float} - The aspect ratio of the {@link Game}
	 */
	public float aspectRatio()
	{
		return (float) width() / (float) height();
	}

	/**
	 * Gets the {@link BufferFormat} of the {@link Display}.
	 * @return {@link BufferFormat} - The {@link BufferFormat} of the
	 * {@link Display}
	 */
	public BufferFormat bufferFormat()
	{
		return graphics().getBufferFormat();
	}

	/**
	 * Gets the center of the {@link Display}.
	 * @return {@link Vector2} - The center of the {@link Display}
	 */
	public Vector2 center()
	{
		return new Vector2(width() * MyMath.HALF, height() * MyMath.HALF);
	}

	/**
	 * Gets the clear mask of the {@link Display}.
	 * @return {@link int} - The clear mask of the {@link Display}
	 */
	public int clearMask()
	{
		return CLEAR_MASK | coverageSampling();
	}

	/**
	 * Gets the config of the {@link Game}.
	 * @return {@link GameConfig} - The config
	 */
	public GameConfig config()
	{
		return game.config();
	}

	/**
	 * Gets the coverage sampling mask.
	 * @return {@link int} - The coverage sampling mask
	 */
	public int coverageSampling()
	{
		return hasCoverageSampling() ? COVERAGE_SAMPLING_MASK : EMPTY_MASK;
	}

	/**
	 * Gets the aspect ratio of the {@link Display}.
	 * @return {@link float} - The aspect ratio of the {@link Display}
	 */
	public float defaultAspectRatio()
	{
		return (float) defaultWidth() / (float) defaultHeight();
	}

	/**
	 * Gets the default height of the {@link Display}.
	 * @return {@link float} - The default height of the {@link Display}
	 */
	public int defaultHeight()
	{
		return config().getHeight();
	}

	/**
	 * Gets the default width of the {@link Display}.
	 * @return {@link float} - The default width of the {@link Display}
	 */
	public int defaultWidth()
	{
		return config().getWidth();
	}

	/**
	 * Gets the {@link DisplayMode} of the {@link Display}.
	 * @return {@link float} - The {@link DisplayMode} of the {@link Display}
	 */
	public DisplayMode displayMode()
	{
		return graphics().getDesktopDisplayMode();
	}

	/**
	 * Gets all possible {@link DisplayMode}s of the {@link Display}.
	 * @return {@link float} - All possible {@link DisplayMode}s of the
	 * {@link Display}
	 */
	public DisplayMode[] displayModes()
	{
		return graphics().getDisplayModes();
	}

	/**
	 * Gets the frame rate.
	 * @return {@link int} - The frame rate
	 */
	public int fps()
	{
		return graphics().getFramesPerSecond();
	}

	/**
	 * Gets the best fullscreen {@link DisplayMode} of the {@link Display}.
	 * @return {@link DisplayMode} - The best fullscreen {@link DisplayMode} of
	 * the {@link Display}
	 */
	public DisplayMode fullscreenDisplayMode()
	{
		DisplayMode[] displayModes = displayModes();
		DisplayMode bestMode = displayModes[0];
		for (DisplayMode mode: displayModes)
		{
			if (bestMode.width < mode.width) bestMode = mode;
		}
		return bestMode;
	}

	/**
	 * Gets the OpenGLES reference.
	 * @return {@link int} - The OpenGLES reference
	 */
	public GL20 gl()
	{
		return Gdx.gl;
	}

	/**
	 * Gets the {@link Graphics} reference.
	 * @return {@link Graphics} - The {@link Graphics} reference
	 */
	public Graphics graphics()
	{
		return game.graphics();
	}

	/**
	 * Gets the height of the {@link Display} in pixels.
	 * @return {@link int} - The height of the {@link Display} in
	 * pixels
	 */
	public int height()
	{
		return graphics().getHeight();
	}

	/**
	 * Gets the {@link Input} reference.
	 * @return {@link Input} - The {@link Input} reference
	 */
	public Input input()
	{
		return game.input();
	}

	public int lastWindowedHeight()
	{
		return lastWindowedHeight;
	}

	public int lastWindowedWidth()
	{
		return lastWindowedWidth;
	}

	private Orientation nativeOrientation()
	{
		return input().getNativeOrientation();
	}

	private int nativeRotation()
	{
		return input().getRotation();
	}

	public DisplayOrientation orientation()
	{
		return new DisplayOrientation(rotation());
	}

	public int rotation()
	{
		int rotation = nativeRotation();
		if (nativeOrientation() == Orientation.Portrait) return rotation;
		return rotation + NATIVE_ROTATION_OFFSET;
	}

	public Jsonable<GameConfig> saveableConfig()
	{
		return game.saveableConfig();
	}

	/**
	 * Gets the {@link View} of the {@link Display}.
	 * @return {@link View} - The {@link View} of the {@link Display}
	 */
	public View view()
	{
		return view;
	}

	/**
	 * Gets the width of the {@link Display} in pixels.
	 * @return {@link int} - The starting of the {@link Display} in pixels
	 */
	public int width()
	{
		return graphics().getWidth();
	}

	/**
	 * Gets whether the {@link Display} uses coverage sampling.
	 * @return {@link boolean} - Whether the {@link Display} uses coverage
	 * sampling
	 * @see <a href="http://www.nvidia.com/object/coverage-sampled-aa.html">
	 * CoverageSampling</a>
	 */
	public boolean hasCoverageSampling()
	{
		return bufferFormat().coverageSampling;
	}

	/**
	 * Gets whether the {@link Display} is focused.
	 * @return {@link boolean} - Whether the {@link Display} is focused
	 */
	public boolean isFocused()
	{
		return !isMinimized() && mouse.isCatched();
	}

	/**
	 * Gets whether the {@link Display} using fullscreen.
	 * @return {@link boolean} - Whether the {@link Display} using fullscreen
	 */
	public boolean isFullscreen()
	{
		return graphics().isFullscreen();
	}

	public boolean isLandscape()
	{
		return orientation().isLandscape();
	}

	/**
	 * Gets whether the {@link Display} is minimized.
	 * @return {@link boolean} - Whether the {@link Display} is minimized
	 */
	public boolean isMinimized()
	{
		return minimized;
	}

	/**
	 * Gets whether the {@link Display} is paused.
	 * @return {@link boolean} - Whether the {@link Display} is paused
	 */
	public boolean isPaused()
	{
		return paused;
	}

	public boolean isPortrait()
	{
		return orientation().isPortrait();
	}

	public boolean isReverseLandscape()
	{
		return orientation().equals(DisplayOrientation.REVERSE_LANDSCAPE);
	}

	public boolean isReversePortrait()
	{
		return orientation().equals(DisplayOrientation.REVERSE_PORTRAIT);
	}

	public boolean isStandardLandscape()
	{
		return orientation().equals(DisplayOrientation.LANDSCAPE);
	}

	public boolean isStandardPortrait()
	{
		return orientation().equals(DisplayOrientation.PORTRAIT);
	}

	@Override
	public void pause()
	{
		if (!isPaused()) mouse.setCatched(minimized);
		minimized = true;
	}

	@Override
	public void render(View view)
	{
		clearScreen();
	}

	@Override
	public void resize(int width, int height)
	{
		view.resize(width, height);
	}

	@Override
	public void resume()
	{
		if (!isPaused()) mouse.setCatched(minimized);
		minimized = false;
	}

	/**
	 * Sets the {@link DisplayMode} of the {@link Display}.
	 * @param displayMode The {@link DisplayMode}
	 */
	public void setDisplayMode(DisplayMode displayMode)
	{
		graphics().setDisplayMode(displayMode);
		updateViewport();
	}

	/**
	 * Sets the {@link DisplayMode} of the {@link Display} by width and height.
	 * @param width The width
	 * @param height The height
	 */
	public void setDisplayMode(int width, int height)
	{
		graphics().setDisplayMode(width, height, false);
		updateViewport();
	}

	/**
	 * Sets whether the {@link Display} uses fullscreen.
	 * @param fullscreen Whether the {@link Display} using fullscreen
	 */
	public void setFullscreen(boolean fullscreen)
	{
		if (isFullscreen() == fullscreen) return;
		config().setFullscreenStartup(fullscreen);
		saveableConfig().save();
		if (fullscreen)
		{
			lastWindowedWidth = width();
			lastWindowedHeight = height();
			setDisplayMode(fullscreenDisplayMode());
		}
		else
		{
			setDisplayMode(lastWindowedWidth(), lastWindowedHeight());
		}
		Inputs.reset();
	}

	/**
	 * Sets whether the {@link Game} is paused.
	 * @param paused Whether the {@link Game} is paused
	 */
	public void setPaused(boolean paused)
	{
		this.paused = paused;
		mouse.setCatched(!isPaused());
	}

	/**
	 * Sets the title of the {@link Game} window.
	 * @param title The title of the {@link Game} window
	 */
	public void setTitle(String title)
	{
		graphics().setTitle(title);
		config().setTitle(title);
		saveableConfig().save();
	}

	/**
	 * Sets whether the {@link Game} uses vertical sync.
	 * @param vsync Whether the {@link Game} uses vertical sync
	 */
	public void setVsync(boolean vsync)
	{
		graphics().setVSync(vsync);
		config().setVsync(vsync);
		saveableConfig().save();
	}

	/**
	 * Gets whether the {@link Game} starts fullscreen.
	 * @return {@link boolean} - Whether the {@link Game} starts fullscreen
	 */
	public boolean startsFullscreen()
	{
		return config().startsFullscreen();
	}

	/**
	 * Toggles whether the {@link Display} is fullscreen.
	 */
	public void toggleFullscreen()
	{
		setFullscreen(!isFullscreen());
	}

	/**
	 * Toggles whether the {@link Display} is paused.
	 */
	public void togglePaused()
	{
		setPaused(!isPaused());
	}

	@Override
	public void update(float dt)
	{
		super.update(dt);
		if (!isPaused()) view().update();
	}

	@Override
	protected void input(float dt)
	{
		if (allowsFullscreen() && fullscreenKey.isDown()) toggleFullscreen();
		if (!mouse.isCatched() && !isPaused() && touchScreen.justTouched())
			mouse.setCatched(true);
		if (touchScreen.exists() && touchScreen.fingersToucing(3))
			togglePaused();
		if (keyboard.exists())
		{
			if (hasPauseScreen() && pauseKey.isPressed()) togglePaused();
			if (screenshotKey.isPressed()) new Screenshot().save().dispose();
		}
	}

	private void setViewport(int width, int height)
	{
		setViewport(0, 0, width, height);
	}

	private void setViewport(int x, int y, int width, int height)
	{
		gl().glViewport(x, y, width, height);
	}

	/**
	 * Updates the {@link Viewport}
	 */
	private void updateViewport()
	{
		setViewport(width(), height());
	}

	private boolean hasPauseScreen()
	{
		return game.currentState() != null
			&& game.currentState().hasPauseState();
	}
}
