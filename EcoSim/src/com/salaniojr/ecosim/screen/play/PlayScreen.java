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
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.salaniojr.ecosim.entity.Carnivore;
import com.salaniojr.ecosim.entity.Entity;
import com.salaniojr.ecosim.entity.EntityTweenAccessor;
import com.salaniojr.ecosim.entity.Herbivore;

public class PlayScreen implements Screen {

	private BitmapFont font;

	private Texture tiles;

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private List<Entity> entities;
	
	private TweenManager tweenManager;
	
	@Override
	public void show() {
		
		tweenManager = ServiceLocator.locateTweenManager();
		
		setFonts();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		setupEntities();
		setupTileMap(w, h);
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
		
		TiledMap map = ServiceLocator.locate();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);
		
		int contCount = 0;
		for (int i = 0; i < mapLayer.getWidth(); i++) {
			for (int j = 0; j < mapLayer.getHeight(); j++) {
				Cell cell = mapLayer.getCell(i, j);
				MapProperties properties = cell.getTile().getProperties();
				if (properties.containsKey("contains")) {
					contCount++;
				}
			}
		}
		
		System.out.println("Amount: " + contCount);
		
	}

	private void drawHud(SpriteBatch spriteBatch) {
		font.draw(renderer.getSpriteBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
	}

	private void setupEntities() {
		Tween.registerAccessor(Entity.class, new EntityTweenAccessor());
		
		entities = new ArrayList<Entity>();
		
		Entity carn1 = new Carnivore();
		carn1.setPosition(1 * 16, 1 * 16);
		Entity carn2 = new Carnivore();
		carn2.setPosition(5 * 16, 1 * 16);
		Entity carn3 = new Carnivore();
		carn3.setPosition(1 * 16, 8 * 16);
		Entity carn4 = new Carnivore();
		carn4.setPosition(22 * 16, 20 * 16);
		Entity carn5 = new Carnivore();
		carn5.setPosition(10 * 16, 15 * 16);
		Entity carn6 = new Carnivore();
		carn6.setPosition(20 * 16, 18 * 16);
		Entity carn7 = new Carnivore();
		carn7.setPosition(25 * 16, 8 * 16);
		
		Entity herb1 = new Herbivore();
		Entity herb2 = new Herbivore();
		herb2.setPosition(14 * 16, 17 * 16);
		Entity herb3 = new Herbivore();
		herb3.setPosition(8 * 16, 9 * 16);
		Entity herb4 = new Herbivore();
		herb4.setPosition(2 * 16, 13 * 16);
		Entity herb5 = new Herbivore();
		herb5.setPosition(27 * 16, 25 * 16);
		Entity herb6 = new Herbivore();
		herb6.setPosition(18 * 16, 19 * 16);
		Entity herb7 = new Herbivore();
		herb7.setPosition(12 * 16, 10 * 16);
		
		entities.add(carn1);
		entities.add(carn2);
		entities.add(carn3);
		entities.add(carn4);
		entities.add(carn5);
		entities.add(carn6);
		entities.add(carn7);
		
		entities.add(herb1);
		entities.add(herb2);
		entities.add(herb3);
		entities.add(herb4);
		entities.add(herb5);
		entities.add(herb6);
		entities.add(herb7);
		
		
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

		for (int x = 0; x < (int) w; x++) {
			for (int y = 0; y < (int) h; y++) {
				Cell cell = new Cell();
				
				Random random = new Random();
				int tx = random.nextInt(2);
				
				StaticTiledMapTile staticTiledMapTile = new StaticTiledMapTile(splitTiles[0][tx]);
				cell.setTile(staticTiledMapTile);
				layer.setCell(x, y, cell);
			}
		}
		
		layers.add(layer);
		
		ServiceLocator.provide(map);
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
