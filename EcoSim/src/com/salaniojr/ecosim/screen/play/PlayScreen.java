package com.salaniojr.ecosim.screen.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

public class PlayScreen implements Screen {

	private BitmapFont font;

	private Texture tiles;

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private List<Entity> entities;

	private TweenManager tweenManager;
	private List<Vector2> availPositions;

	@Override
	public void show() {
		entities = new ArrayList<Entity>();
		availPositions = new ArrayList<Vector2>();

		tweenManager = ServiceLocator.locateTweenManager();

		setFonts();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		setupTileMap(w, h);
		setupEntities();
		setupCamera(w, h);

		renderer = new OrthogonalTiledMapRenderer(map);
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	private void update(float delta) {
		tweenManager.update(delta);

		for (Entity entity : entities) {
			entity.update(delta);
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.setView(camera);
		renderer.render();
		renderer.getSpriteBatch().begin();

		drawEntities(renderer.getSpriteBatch());
		drawHud(renderer.getSpriteBatch());

		renderer.getSpriteBatch().end();
	}

	private void drawEntities(SpriteBatch spriteBatch) {
		for (Entity entity : entities) {
			entity.draw(spriteBatch);
		}
	}

	private void drawHud(SpriteBatch spriteBatch) {
		font.draw(renderer.getSpriteBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
	}

	private void setupEntities() {
		Tween.registerAccessor(Entity.class, new EntityTweenAccessor());

		int maxPositionIndex = availPositions.size();
		Entity entity = null;
		Random random = new Random();
		for (int i = 0; i < 2; i++) {
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

	private void setupCamera(float w, float h) {
		camera = new OrthographicCamera();

		camera.position.x = w / 2;
		camera.position.y = h / 2;
	}

	private void setupTileMap(float w, float h) {
		tiles = new Texture(Gdx.files.internal("data/ecosim_tiles.png"));

		TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16);

		map = new TiledMap();
		MapLayers layers = map.getLayers();

		TiledMapTileLayer layer = new TiledMapTileLayer((int) w, (int) h, 16, 16);

		Vector2 position = null;
		Random random = new Random();

		position = new Vector2(2 * 16, 1 * 16);
		availPositions.add(position);
		position = new Vector2(1 * 16, 1 * 16);
		availPositions.add(position);

		for (int x = 0; x < (int) w / 16; x++) {
			for (int y = 0; y < (int) h / 16; y++) {

				// availPositions.add(position);

				Cell cell = new Cell();

				// int tx = random.nextInt(2);
				int tx = 0;

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

		
		addPlant(splitTiles, layer, position, -1, -1);
		addPlant(splitTiles, layer, position, -1, 0);
		addPlant(splitTiles, layer, position, -1, 1);
		addPlant(splitTiles, layer, position, 1, -1);
		
		addPlant(splitTiles, layer, position, 1, 1);
		addPlant(splitTiles, layer, position, 0, 1);
		addPlant(splitTiles, layer, position, 0, -1);
		addPlant(splitTiles, layer, position, -1, 2);
		addPlant(splitTiles, layer, position, 0, 2);
		addPlant(splitTiles, layer, position, 1, 2);
		addPlant(splitTiles, layer, position, 2, 2);
		addPlant(splitTiles, layer, position, 2, 1);
		addPlant(splitTiles, layer, position, 2, 0);
		addPlant(splitTiles, layer, position, 2, -1);
		addPlant(splitTiles, layer, position, 3, 0);
		addPlant(splitTiles, layer, position, 3, -1);
		addPlant(splitTiles, layer, position, 3, 1);
		addPlant(splitTiles, layer, position, 3, 2);
		addPlant(splitTiles, layer, position, 3, 3);
		addPlant(splitTiles, layer, position, 2, 3);
		addPlant(splitTiles, layer, position, 1, 3);
		addPlant(splitTiles, layer, position, 0, 3);
		addPlant(splitTiles, layer, position, -1, 3);

		layers.add(layer);
		
		
		
		
		
		ServiceLocator.provide(map);
	}

	private void addPlant(TextureRegion[][] splitTiles, TiledMapTileLayer layer, Vector2 position, int xCellIndex, int yCellIndex) {
		Cell cell = layer.getCell((int) (position.x / 16 + xCellIndex), (int) (position.y / 16 + yCellIndex));
		StaticTiledMapTile staticTiledMapTile = new StaticTiledMapTile(splitTiles[0][1]);
		cell.setTile(staticTiledMapTile);
		Vector2 newPos = new Vector2(position.x + (16 * xCellIndex), position.y + (16 * yCellIndex));
		Plant plant = new Plant(newPos, splitTiles);
		staticTiledMapTile.getProperties().put("contains", plant);
	}

	private void setFonts() {
		font = new BitmapFont();
		font.setColor(Color.BLUE);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
}
