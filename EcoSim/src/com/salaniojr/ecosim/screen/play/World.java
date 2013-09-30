package com.salaniojr.ecosim.screen.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.salaniojr.ecosim.entity.Carnivore;
import com.salaniojr.ecosim.entity.Entity;
import com.salaniojr.ecosim.entity.EntityTweenAccessor;
import com.salaniojr.ecosim.entity.Herbivore;
import com.salaniojr.ecosim.entity.Plant;

public class World {

	private Texture mapTexture;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	private List<Entity> entities;
	private List<Vector2> availPositions;

	public TiledMap getMap() {
		return map;
	}

	public SpriteBatch getTileRendererSpriteBatch() {
		return renderer.getSpriteBatch();
	}

	public void init() {
		entities = new ArrayList<Entity>();
		availPositions = new ArrayList<Vector2>();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		setupTileMap(w, h);
		setupEntities();

		renderer = new OrthogonalTiledMapRenderer(map);
	}

	private void setupTileMap(float w, float h) {
		mapTexture = new Texture(Gdx.files.internal("data/ecosim_tiles.png"));
		TextureRegion[][] splitTiles = TextureRegion.split(mapTexture, 16, 16);

		map = new TiledMap();
		MapLayers layers = map.getLayers();

		TiledMapTileLayer layer = new TiledMapTileLayer((int) w, (int) h, 16, 16);

		Vector2 position = null;
		Random random = new Random();
		
		for (int x = 0; x < (int) w / 16; x++) {
			for (int y = 0; y < (int) h / 16; y++) {
				position = new Vector2(x * 16, y * 16);
				availPositions.add(position);

				Cell cell = new Cell();

				int tx = random.nextInt(2);

				StaticTiledMapTile staticTiledMapTile = null;
				if (tx == 1) { // tall grass
					int probability = random.nextInt(2);

					if (probability == 0) {
						Plant plant = new Plant(position, splitTiles);
						entities.add(plant);
						availPositions.remove(position);

						staticTiledMapTile = new StaticTiledMapTile(splitTiles[0][tx]);
						staticTiledMapTile.getProperties().put("contains", plant);
					} else {
						staticTiledMapTile = new StaticTiledMapTile(splitTiles[0][0]);
					}
				} else {
					staticTiledMapTile = new StaticTiledMapTile(splitTiles[0][tx]);
				}

				cell.setTile(staticTiledMapTile);

				layer.setCell(x, y, cell);
			}
		}

		layers.add(layer);

		ServiceLocator.provide(map);
	}

	private void setupEntities() {
		Tween.registerAccessor(Entity.class, new EntityTweenAccessor());

		int maxPositionIndex = availPositions.size();
		Entity entity = null;
		Random random = new Random();
		
		for (int i = 0; i < 150; i++) {
			int animalType = random.nextInt(2);

			int positionIndex = random.nextInt(maxPositionIndex);

			if (animalType == 0) {
				entity = new Carnivore(availPositions.remove(positionIndex));
			} else {
				entity = new Herbivore(availPositions.remove(positionIndex));
			}

			entities.add(entity);
			maxPositionIndex--;
		}
	}

	public void update(float delta) {
		for (Entity entity : entities) {
			entity.update(delta);
		}
	}

	public void updateRenderer(OrthographicCamera camera) {
		renderer.setView(camera);
		renderer.render();
	}

	public void draw(OrthographicCamera camera, SpriteBatch spriteBatch) {
		drawEntities(renderer.getSpriteBatch());
	}

	private void drawEntities(SpriteBatch spriteBatch) {
		for (Entity entity : entities) {
			entity.draw(spriteBatch);
		}
	}

	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

}
