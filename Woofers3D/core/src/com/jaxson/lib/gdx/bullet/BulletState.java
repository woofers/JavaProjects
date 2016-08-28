package com.jaxson.lib.gdx.bullet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.jaxson.lib.gdx.backend.Game;
import com.jaxson.lib.gdx.bullet.simulation.PhysicsWorld;
import com.jaxson.lib.gdx.bullet.simulation.bodies.Floor;
import com.jaxson.lib.gdx.bullet.simulation.bodies.types.PlayerBody;
import com.jaxson.lib.gdx.bullet.simulation.bodies.types.RigidBody;
import com.jaxson.lib.gdx.bullet.simulation.bodies.types.SoftBody;
import com.jaxson.lib.gdx.graphics.views.TargetCamera;
import com.jaxson.lib.gdx.io.GdxFile;
import com.jaxson.lib.gdx.states.State;

public abstract class BulletState extends State
{
	private static final boolean SHADOWS = true;

	private PhysicsWorld world;

	public BulletState(Game gameManager)
	{
		super(gameManager);
		this.world = new PhysicsWorld();
		setWorldSize();
		setShadows(SHADOWS);
	}

	public void applyPhysics(Floor entity)
	{
		world.add(entity);
	}

	public void applyPhysics(Floor entity, boolean hasMovement)
	{
		world.add(entity, hasMovement);
	}

	public void applyPhysics(PlayerBody entity)
	{
		world.add(entity);
	}

	public void applyPhysics(RigidBody entity)
	{
		world.add(entity);
	}

	public void applyPhysics(SoftBody entity)
	{
		world.add(entity);
	}

	public void applyPhysics(TargetCamera camera)
	{
		world.add(camera);
	}

	@Override
	public void dispose()
	{
		world.dispose();
		super.dispose();
	}

	public PhysicsWorld getPhysicsWorld()
	{
		return world;
	}

	public Vector3 getWorldSize()
	{
		return getPhysicsWorld().getWorldSize();
	}

	public void load(GdxFile file)
	{
		load(file, true);
	}

	public void load(GdxFile file, boolean applyPhysics)
	{
		for (RigidBody entity: getPhysicsWorld().load(file))
		{
			add(entity);
			if (applyPhysics) applyPhysics(entity);
		}
	}

	public void removePhysics(PlayerBody entity)
	{
		world.remove(entity);
	}

	public void removePhysics(RigidBody entity)
	{
		world.remove(entity);
	}

	public void removePhysics(SoftBody entity)
	{
		world.remove(entity);
	}

	@Override
	public void render(SpriteBatch spriteBatch, ModelBatch modelBatch)
	{
		super.render(spriteBatch, modelBatch);
		world.render(spriteBatch, modelBatch, getView());
	}

	public void setShadows(boolean shadows)
	{
		getEnvironment().setShawdows(shadows);
	}

	public void setWorldSize()
	{
		setWorldSize(getWorldSize());
	}

	private void setWorldSize(Vector3 worldSize)
	{
		getEnvironment().setWorldSize(worldSize);
	}

	@Override
	public void update(float dt)
	{
		super.update(dt);
		world.update(dt);
	}
}
