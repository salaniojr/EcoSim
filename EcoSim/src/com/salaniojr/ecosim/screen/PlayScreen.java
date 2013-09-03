package com.salaniojr.ecosim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.salaniojr.ecosim.Carnivore;

public class PlayScreen implements Screen {
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private Carnivore carnivore;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);
		
//		mapLayer.setVisible(false);
		
		renderer.setView(camera);
		renderer.render();
		
		renderer.getSpriteBatch().begin();
		carnivore.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("data/map.tmx");
		
		renderer = new OrthogonalTiledMapRenderer(map);
		
		camera = new OrthographicCamera();
		camera.position.x = Gdx.graphics.getWidth()/2;
		camera.position.y = Gdx.graphics.getHeight()/2;
		
		Texture texture = new Texture(Gdx.files.internal("data/carnivore.png"));
		carnivore = new Carnivore();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

}
