package com.jaxson.lib.gdx.bullet;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.jaxson.lib.gdx.backend.GameManager;
import com.jaxson.lib.gdx.bullet.bodies.Floor;
import com.jaxson.lib.gdx.bullet.bodies.PlayerBody;
import com.jaxson.lib.gdx.bullet.bodies.RigidBody;
import com.jaxson.lib.gdx.bullet.bodies.SoftBody;
import com.jaxson.lib.gdx.graphics.cameras.TargetCamera;
import com.jaxson.lib.gdx.states.State;
import com.badlogic.gdx.math.Vector3;

public abstract class BulletState extends State
{
	private static final boolean SHADOWS = true;

	private PhysicsWorld world;

	public BulletState(GameManager gameManager)
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

	@Override
	public void dispose()
	{
		super.dispose();
	}

	public PhysicsWorld getPhysicsWorld()
	{
		return world;
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
		world.render(spriteBatch, modelBatch, getCamera());
	}

	@Override
	public void setCamera(Camera camera)
	{
		super.setCamera(camera);
		if (camera instanceof TargetCamera) getTargetCamera().setWorld(getPhysicsWorld());
	}

	@Override
	public void update(float dt)
	{
		super.update(dt);
		world.update(dt);
	}

	public Vector3 getWorldSize()
	{
		return getPhysicsWorld().getWorldSize();
	}

	public void setWorldSize()
	{
		setWorldSize(getWorldSize());
	}

	private void setWorldSize(Vector3 worldSize)
	{
		getEnvironment().setWorldSize(worldSize);
	}

	public void setShadows(boolean shadows)
	{
		getEnvironment().setShawdows(shadows);
	}
}