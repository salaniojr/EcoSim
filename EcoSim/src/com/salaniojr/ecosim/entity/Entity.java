package com.salaniojr.ecosim.entity;

import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.salaniojr.ecosim.entity.state.HuntState;
import com.salaniojr.ecosim.entity.state.IdleState;
import com.salaniojr.ecosim.entity.state.State;
import com.salaniojr.ecosim.screen.play.ServiceLocator;

public abstract class Entity {
	
	private static int current_id = 0;

	protected int id;
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

	private boolean moving;

	public Entity(String texturePath, AnimalType type) {
		id = current_id++;
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
		
		updateNeighbors();
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
	}

	private void checkActions() {
		TiledMap map = ServiceLocator.locate();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		Boolean kill = (Boolean) cell.getTile().getProperties().get("kill");
		
		if (kill != null) {
			die();
		}
	}

	private void die() {
		dead = true;
		TiledMap map = ServiceLocator.locate();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		cell.getTile().getProperties().remove("contains");
		cell.getTile().getProperties().remove("kill");
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
		TiledMap map = ServiceLocator.locate();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		cell.getTile().getProperties().remove("contains");
		cell.getTile().getProperties().put("contains", type);
	}

	protected int getYInCellCoord() {
		return (int) ((getY() == 0? 1 : getY() / sprite.getHeight()) - (getY() == 0? 1 : 0));
	}

	protected int getXinCellCoord() {
		return (int) ((getX() == 0? 1 : getX() / sprite.getWidth()) - (getX() == 0? 1 : 0));
	}
	
	protected int getYInCellCoord(float y) {
		return (int) ((y == 0? 1 : y / sprite.getHeight()) - (y == 0? 1 : 0));
	}

	protected int getXinCellCoord(float x) {
		return (int) ((x == 0? 1 : x / sprite.getWidth()) - (x == 0? 1 : 0));
	}

	private void updateHunger(float delta) {
		hungerTime += delta;

		if (hungerTime >= hungerIncreaseTime) {
			hunger++;
			hungerTime = 0;

			System.out.println("hunger update [" + type + "] : " + hunger);
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

	public void idle() {
		state = new IdleState(this);
	}

	public void move() {
		if (moving) {
			return;
		}
		
		TiledMap map = ServiceLocator.locate();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);
		
		Cell cell = null;
		Vector2 newPosition = null;
		
		while (cell == null) {
			newPosition = chooseNextMovePosition();
			cell = mapLayer.getCell(getXinCellCoord(newPosition.x), getYInCellCoord(newPosition.y));
			
			if (cell != null) {
				MapProperties properties = cell.getTile().getProperties();
				if (properties.containsKey("contains")) {
					cell = null;
				}
			}
		}
		
		Cell myCell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		myCell.getTile().getProperties().clear();
		
		moveTo(newPosition);
	}

	private void moveTo(Vector2 newPosition) {
		moving = true;
		
//		Tween.set(this, EntityTweenAccessor.MOVEX).target(getX()).start(ServiceLocator.locateTweenManager());
		Tween.to(this, EntityTweenAccessor.MOVEX, 0.75f).target(newPosition.x).start(ServiceLocator.locateTweenManager());
		
//		Tween.set(this, EntityTweenAccessor.MOVEY).target(getY()).start(ServiceLocator.locateTweenManager());
		Tween.to(this, EntityTweenAccessor.MOVEY, 0.75f).target(newPosition.y).start(ServiceLocator.locateTweenManager()).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if (type == TweenCallback.COMPLETE) {
					moving = false;
				}
			}
		});
	}

	private Vector2 chooseNextMovePosition() {
		Random random = new Random();
		int index = random.nextInt(8);
		
		return neighbors[index];
	}

	protected void moveToFoodNeighbor(int neighborIndex) {
		moveTo(neighbors[neighborIndex]);
	}
}