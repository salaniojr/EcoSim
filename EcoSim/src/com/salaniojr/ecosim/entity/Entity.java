package com.salaniojr.ecosim.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.salaniojr.ecosim.entity.state.BreedState;
import com.salaniojr.ecosim.entity.state.HuntState;
import com.salaniojr.ecosim.entity.state.IdleState;
import com.salaniojr.ecosim.entity.state.State;
import com.salaniojr.ecosim.screen.play.ServiceLocator;

public abstract class Entity {

	private static int current_id = 0;

	public static final int HUNGER_MAX = 8;
	public static final int HUNGER_MIN = 0;

	protected int id;
	protected Sprite sprite;
	protected State state;

	protected int hunger;
	protected float hungerIncreaseTime = 8f;
	protected float hungerTime = 0;

	protected int breedHunger;
	protected float breedHungerIncreaseTime = 8f;
	protected float breedHungerTime = 0;

	protected Vector2[] neighbors;

	protected AnimalType type;

	protected boolean moving;
	protected boolean dead;
	protected boolean dying;

	private BitmapFont hungerText;

	private boolean breeding;

	public Entity(String texturePath, AnimalType type, Vector2 position) {
		id = current_id++;
		sprite = new Sprite(new Texture(Gdx.files.internal(texturePath)));
		sprite.setPosition(position.x, position.y);
		state = new IdleState(this);

		this.type = type;
		this.neighbors = new Vector2[8];

		initNeighbors();

		hungerText = ServiceLocator.locateFont(12);
		hungerText.setColor(Color.BLUE);

		hunger = new Random().nextInt(HUNGER_MAX - 1);
		breedHunger = new Random().nextInt(HUNGER_MAX - 1);
	}

	public Entity(AnimalType animalType) {
		this.type = animalType;
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

	public float getAlpha() {
		return sprite.getColor().a;
	}

	public void setAlpha(float value) {
		sprite.setColor(sprite.getColor().r, sprite.getColor().g, sprite.getColor().b, value);
		hungerText.setColor(hungerText.getColor().r, hungerText.getColor().g, hungerText.getColor().b, value);
	}

	public void update(float delta) {
		float mod = getX() % 16;

		assert mod == 0;

		updateHunger(delta);

		if (hunger == HUNGER_MAX && !dying) {
			die();
		}

		if (dead) {
			return;
		}

		state.update(delta);
	}

	protected void die() {
		dying = true;
		TiledMapTileLayer mapLayer = ServiceLocator.locateMap();

		Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		cell.getTile().getProperties().clear();

		animateDeath();
	}

	private void animateDeath() {
		Tween.to(this, EntityTweenAccessor.FADE, 2f).target(0).start(ServiceLocator.locateTweenManager())
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if (type == TweenCallback.COMPLETE) {
							dead = true;
						}
					}
				});
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
		return (int) (getY() / sprite.getHeight());
	}

	protected int getXinCellCoord() {
		return (int) (getX() / sprite.getWidth());
	}

	protected int getYInCellCoord(float y) {
		return (int) (y / sprite.getHeight());
	}

	protected int getXinCellCoord(float x) {
		return (int) (x / sprite.getWidth());
	}

	private void updateHunger(float delta) {
		hungerTime += delta;
		breedHungerTime += delta;

		if (hungerTime >= hungerIncreaseTime) {
			hunger++;
			hungerTime = 0;
		}

		if (breedHungerTime >= breedHungerIncreaseTime) {
			breedHunger++;
			breedHungerTime = 0;
		}
	}

	public void draw(SpriteBatch spriteBatch) {
		if (dead) {
			return;
		}
		sprite.draw(spriteBatch);

		if (hunger >= HUNGER_MAX - 1) {
			hungerText.setColor(Color.MAGENTA.r, Color.MAGENTA.g, Color.MAGENTA.b, hungerText.getColor().a);
		} else {
			hungerText.setColor(Color.BLUE.r, Color.BLUE.g, Color.BLUE.b, hungerText.getColor().a);
		}

		hungerText.draw(spriteBatch, hunger + "", getX() + 7, getY() + 11);
	}

	public boolean isHungry() {
		boolean hungry = false;

		if (hunger >= 6) {
			hungry = true;
		}

		return hungry;
	}

	public boolean isOnFire() {
		boolean heat = false;

		if (breedHunger >= 6 && !isHungry()) {
			heat = true;
		}

		return heat;
	}

	public void idle() {
		state = new IdleState(this);
	}

	public void hunt() {
		state = new HuntState(this);
	}

	public void breed() {
		state = new BreedState(this);
	}

	public abstract void searchFoodNearby();

	public void setPosition(float x, float y) {
		sprite.setPosition(x, y);
		updateNeighbors();
	}

	public void move() {
		if (moving || dying) {
			return;
		}

		Vector2 newPosition = chooseNextMovePosition();

		moveTo(newPosition);
	}

	protected void moveTo(Vector2 newPosition) {
		TiledMapTileLayer mapLayer = ServiceLocator.locateMap();
		Cell currentCell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
		Cell futureCell = mapLayer.getCell(getXinCellCoord(newPosition.x), getYInCellCoord(newPosition.y));

		if (futureCell.getTile().getProperties().containsKey("willcontain")
				|| futureCell.getTile().getProperties().containsKey("contains")) {
			return;
		}

		moving = true;

		currentCell.getTile().getProperties().clear();
		futureCell.getTile().getProperties().put("willcontain", this);

		Random random = new Random();
		float velocity = (random.nextInt(2) + 1) * 0.5f;

		Tween.to(this, EntityTweenAccessor.MOVEXY, velocity).target(newPosition.x, newPosition.y)
				.start(ServiceLocator.locateTweenManager()).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int eventType, BaseTween<?> source) {
						if (eventType == TweenCallback.COMPLETE) {
							TiledMapTileLayer mapLayer = ServiceLocator.locateMap();
							Cell cell = mapLayer.getCell(getXinCellCoord(), getYInCellCoord());
							cell.getTile().getProperties().clear();
							cell.getTile().getProperties().put("contains", Entity.this);

							moving = false;
							updateNeighbors();
						}
					}

				});
	}

	private Vector2 chooseNextMovePosition() {
		List<Vector2> possiblePositions = new ArrayList<Vector2>();

		TiledMapTileLayer mapLayer = ServiceLocator.locateMap();

		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i].x < 0 || neighbors[i].x > Gdx.graphics.getWidth() - sprite.getWidth()
					|| neighbors[i].y > Gdx.graphics.getHeight() - sprite.getHeight() || neighbors[i].y < 0) {
				continue;
			}

			Cell cell = mapLayer.getCell(getXinCellCoord(neighbors[i].x), getYInCellCoord(neighbors[i].y));

			if (neighbors[i].x == 0 && neighbors[i].y == 0) {
				System.out.println();
			}

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

	public boolean isMoving() {
		return moving;
	}

	public AnimalType getType() {
		return type;
	}

	protected void restoreHunger() {
		hunger = HUNGER_MIN;
		hungerTime = 0;
	}

	public void searchPartnerNearby() {
		TiledMapTileLayer mapLayer = ServiceLocator.locateMap();

		Entity animal = null;
		Vector2 spacePosition = null;
		for (int i = 0; i < neighbors.length; i++) {
			Cell cell = mapLayer.getCell((int) (neighbors[i].x / sprite.getWidth()), (int) (neighbors[i].y / sprite.getHeight()));

			if (cell == null) {
				continue;
			}
			
			Object object = cell.getTile().getProperties().get("contains");

			if (animal == null && object != null) {
				animal = (Entity) object;

				if (!animal.getType().equals(type) || !animal.isOnFire()) {
					continue;
				}
			}
			
			if (spacePosition == null) {
				if (object == null) {
					spacePosition = new Vector2(neighbors[i]);
				}
			}
			
			if (animal != null && spacePosition != null) {
				break;
			}
		}

		if (animal != null && spacePosition != null) {
			startBreeding();
			breedHunger = HUNGER_MIN;
			animal.startBreeding();
		} else {
			move();
		}
	}

	protected void startBreeding() {
		breeding = true;
		Tween.to(this, EntityTweenAccessor.MOVEX, 2f);
	}

	public boolean isBreeding() {
		return breeding;
	}

}