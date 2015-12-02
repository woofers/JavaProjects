package com.jaxson.lib.gdx.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jaxson.lib.gdx.entities.Entity;
import com.jaxson.lib.gdx.sprites.Sprite;
import com.jaxson.lib.gdx.states.GameStateManager;
import com.jaxson.lib.gdx.util.MyInputProcessor;
import com.jaxson.lib.util.MyArrayList;

public abstract class State<C extends Camera>
{
	protected C camera;
	protected GameStateManager gameStateManager;
	protected InputProcessor input;
	protected MyArrayList<Entity> entities;
	protected MyArrayList<Sprite> sprites;
	protected Environment environment;

	public State(GameStateManager gameStateManager, C camera)
	{
		this.gameStateManager = gameStateManager;
		this.camera = camera;
		this.input = new MyInputProcessor();
		this.entities = new MyArrayList<Entity>();
		this.sprites = new MyArrayList<Sprite>();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		Gdx.input.setInputProcessor(input);
	}

	public void add(Entity entity)
	{
		entities.add(entity);
	}

	public void add(Sprite sprite)
	{
		sprites.add(sprite);
	}

	public void dispose()
	{
		for (Entity entity: entities)
		{
			entity.dispose();
		}
		for (Sprite sprite: sprites)
		{
			sprite.dispose();
		}
	}

	public C getCamera()
	{
		return camera;
	}

	public abstract void input();

	public void remove(Entity entity)
	{
		entities.remove(entity);
	}

	public void remove(Sprite sprite)
	{
		sprites.remove(sprite);
	}


	public void render(SpriteBatch spriteBatch, ModelBatch modelBatch)
	{
		render(spriteBatch);
		render(modelBatch);
	}

	public void render(ModelBatch modelBatch)
	{
		if (modelBatch == null && entities.isEmpty()) return;
		modelBatch.begin(camera);
		for (Entity entity: entities)
		{
			modelBatch.render(entity, environment);
		}
		modelBatch.end();
	}

	public void render(SpriteBatch spriteBatch)
	{
		if (spriteBatch == null && sprites.isEmpty()) return;
		Vector2 location;
		spriteBatch.begin();
		for (Sprite sprite: sprites)
		{
			location = sprite.getLocation();
			spriteBatch.draw(sprite, location.x, location.y);
		}
		spriteBatch.end();
	}

	public void setCamera(C camera)
	{
		this.camera = camera;
	}

	public void update(float dt)
	{
		input();
		camera.update();
		for (Entity entity: entities)
		{
			entity.update(dt);
		}
		for (Sprite sprite: sprites)
		{
			sprite.update(dt);
		}
	}
}
