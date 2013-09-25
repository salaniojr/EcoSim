package com.salaniojr.ecosim.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	private float hungerIncreaseTime = 8f;
	private float hungerTime = 0;

	protected Vector2[] neighbors;

	protected AnimalType type;
	private boolean dead;

	private boolean moving;

	private BitmapFont hungerText;

	public Entity(String texturePath, AnimalType type) {
		id = current_id++;
		sprite = new Sprite(new Texture(Gdx.files.internal(texturePath)));
		hungerText = new BitmapFont();
		hungerText.scale(0.01f);
		state = new IdleState(this);

		this.type = type;
		this.neighbors = new Vector2[8];

		initNeighbors();

		hunger = new Random().nextInt(HUNGER_MAX - 3);
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

		if (hunger == HUNGER_MAX) {
			die();
		}

		if (dead) {
			return;
		}

		updateHunger(delta);
		state.update(delta);
	}

	private void checkActions() {
		TiledMap map = ServiceLocator.locateMap();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		Boolean kill = (Boolean) cell.getTile().getProperties().get("kill");

		if (kill != null) {
			die();
		}
	}

	private void die() {
		dead = true;
		TiledMap map = ServiceLocator.locateMap();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		cell.getTile().getProperties().clear();
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

	protected int getYInCellCoord() {
		return (int) ((getY() == 0 ? 1 : getY() / sprite.getHeight()) - (getY() == 0 ? 1 : 0));
	}

	protected int getXinCellCoord() {
		return (int) ((getX() == 0 ? 1 : getX() / sprite.getWidth()) - (getX() == 0 ? 1 : 0));
	}

	protected int getYInCellCoord(float y) {
		return (int) ((y == 0 ? 1 : y / sprite.getHeight()) - (y == 0 ? 1 : 0));
	}

	protected int getXinCellCoord(float x) {
		return (int) ((x == 0 ? 1 : x / sprite.getWidth()) - (x == 0 ? 1 : 0));
	}

	private void updateHunger(float delta) {
		hungerTime += delta;

		if (hungerTime >= hungerIncreaseTime) {
			hunger++;
			hungerTime = 0;
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		if (dead) {
			return;
		}
		sprite.draw(spriteBatch);

		hungerText.draw(spriteBatch, "h: " + hunger, getX(), getY() + sprite.getHeight() / 2);
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
		updateNeighbors();
	}

	public void idle() {
		state = new IdleState(this);
	}

	public void move() {
		if (moving) {
			return;
		}

		Vector2 newPosition = chooseNextMovePosition();

		moveTo(newPosition);
	}

	private void moveTo(Vector2 newPosition) {
		TiledMap map = ServiceLocator.locateMap();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);
		Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		cell.getTile().getProperties().clear();

		moving = true;

		cell = mapLayer.getCell(getXinCellCoord(newPosition.x), getYInCellCoord(newPosition.y));
		cell.getTile().getProperties().put("willcontain", type);

		Random random = new Random();
		int velocity = random.nextInt(3) + 1;

		Tween.to(this, EntityTweenAccessor.MOVEXY, velocity).target(newPosition.x, newPosition.y)
				.start(ServiceLocator.locateTweenManager()).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int eventType, BaseTween<?> source) {
						if (eventType == TweenCallback.COMPLETE) {
							TiledMap map = ServiceLocator.locateMap();
							TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);
							Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
							cell.getTile().getProperties().clear();
							cell.getTile().getProperties().put("contains", type);

							moving = false;
							updateNeighbors();
						}
					}
				});
	}

	private Vector2 chooseNextMovePosition() {
		List<Vector2> possiblePositions = new ArrayList<Vector2>();

		TiledMap map = ServiceLocator.locateMap();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i].x < 0 || neighbors[i].x > Gdx.graphics.getWidth() - sprite.getWidth()
					|| neighbors[i].y > Gdx.graphics.getHeight() - sprite.getHeight() || neighbors[i].y < 0) {
				continue;
			}

			Cell cell = mapLayer.getCell(getXinCellCoord(neighbors[i].x), getYInCellCoord(neighbors[i].y));
			MapProperties properties = cell.getTile().getProperties();

			if (properties.containsKey("contains") || properties.containsKey("willcontain")) {
				continue;
			}

			possiblePositions.add(new Vector2(neighbors[i].x, neighbors[i].y));
		}

		if (possiblePositions.isEmpty()) {
			return new Vector2(getX(), getY());
		}

		Random random = new Random();
		int newPositionIndex = random.nextInt(possiblePositions.size());

		return possiblePositions.get(newPositionIndex);
	}

	protected void moveToFoodNeighbor(int neighborIndex) {
		moveTo(neighbors[neighborIndex]);
	}
}