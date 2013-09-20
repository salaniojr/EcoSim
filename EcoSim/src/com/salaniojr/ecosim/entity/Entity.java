package com.salaniojr.ecosim.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.salaniojr.ecosim.entity.state.HuntState;
import com.salaniojr.ecosim.entity.state.IdleState;
import com.salaniojr.ecosim.entity.state.State;
import com.salaniojr.ecosim.screen.play.MapLocator;

public abstract class Entity {

	protected Sprite sprite;
	protected int hunger;
	public static final int HUNGER_MAX = 8;
	public static final int HUNGER_MIN = 0;
	protected State state;
	private float hungerIncreaseTime = 2f;
	private float hungerTime = 0;

	protected Vector2[] neighbors;

	protected AnimalType type;
	private boolean dead;

	public Entity(String texturePath, AnimalType type) {
		sprite = new Sprite(new Texture(Gdx.files.internal(texturePath)));
		state = new IdleState(this);

		this.type = type;
		this.neighbors = new Vector2[8];

		initNeighbors();

		hunger = HUNGER_MIN;
	}

	private void initNeighbors() {
		for (int i = 0; i < neighbors.length; i++) {
			neighbors[i] = new Vector2();
		}
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public void update(float delta) {
		checkActions();
		
		if (dead) {
			return;
		}
		
		updateHunger(delta);
		state.update(delta);

		updateNeighbors();

		updateWorldInfo();

		if (sprite.getY() > Gdx.graphics.getHeight() - sprite.getHeight()) {
			sprite.setY(Gdx.graphics.getHeight() - sprite.getHeight());
		}

		if (sprite.getY() < 0) {
			sprite.setY(0);
		}

		if (sprite.getX() > Gdx.graphics.getWidth() - sprite.getHeight()) {
			sprite.setX(Gdx.graphics.getWidth() - sprite.getWidth());
		}

		if (sprite.getX() < 0) {
			sprite.setX(0);
		}
	}

	private void checkActions() {
		TiledMap map = MapLocator.locate();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		Cell cell = mapLayer.getCell((int) (getX() == 0? 1 : getX() / sprite.getWidth() - (getX() == 0? -1 : 0)), (int) (getY() == 0? 1 : getY() / sprite.getHeight() - (getY() == 0? -1 : 0)));
		Boolean kill = (Boolean) cell.getTile().getProperties().get("kill");
		
		if (kill != null) {
			die();
		}
	}

	private void die() {
		dead = true;
		TiledMap map = MapLocator.locate();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		Cell cell = mapLayer.getCell((int) ((getX() == 0? 1 : getX() / sprite.getWidth()) - (getX() == 0? -1 : 0)), (int) ((getY() == 0? 1 : getY() / sprite.getHeight()) - (getY() == 0? -1 : 0)));
		cell.getTile().getProperties().remove("contains");
	}

	private void updateNeighbors() {
		float size = sprite.getHeight();
		neighbors[0].set(getX() - size, getY() + size);
		neighbors[1].set(getX(), getY() + size);
		neighbors[2].set(getX() + size, getY() + size);
		neighbors[3].set(getX() - size, getY());
		neighbors[4].set(getX() + size, getY());
		neighbors[5].set(getX() - size, getY() - size);
		neighbors[6].set(getX(), getY() - size);
		neighbors[7].set(getX() + size, getY() - size);
	}

	private void updateWorldInfo() {
		TiledMap map = MapLocator.locate();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		Cell cell = mapLayer.getCell((int) ((getX() == 0? 1 : getX() / sprite.getWidth()) - (getX() == 0? -1 : 0)), (int) ((getY() == 0? 1 : getY() / sprite.getHeight()) - (getY() == 0? -1 : 0)));
		cell.getTile().getProperties().remove("contains");
		cell.getTile().getProperties().put("contains", type);
	}

	private void updateHunger(float delta) {
		hungerTime += delta;

		if (hungerTime >= hungerIncreaseTime) {
			hunger++;
			hungerTime = 0;

			System.out.println("hunger update: " + hunger);
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		if (dead) {
			return;
		}
		sprite.draw(spriteBatch);
	}

	public boolean isHungry() {
		boolean hungry = false;

		if (hunger >= 6) {
			hungry = true;
		}

		return hungry;
	}

	public void hunt() {
		state = new HuntState(this);
	}

	public abstract void searchFoodNearby();

	public void setPosition(float x, float y) {
		sprite.setPosition(x, y);
	}
}