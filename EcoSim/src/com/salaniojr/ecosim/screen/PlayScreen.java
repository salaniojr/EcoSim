package com.salaniojr.ecosim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.salaniojr.ecosim.entity.Carnivore;

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
		carnivore.update(delta);
		
		if (carnivore.getY() > Gdx.graphics.getHeight()-24) {
			carnivore.setY(Gdx.graphics.getHeight()-24);
		}
		
		if (carnivore.getY() < 0) {
			carnivore.setY(0);
		}
		
		if (carnivore.getX() > Gdx.graphics.getWidth()-carnivore.getHeight()) {
			carnivore.setX(Gdx.graphics.getWidth() - carnivore.getWidth());
		}
		
		if (carnivore.getX() < 0) {
			carnivore.setX(0);
		}
		
		System.out.println(carnivore.getX());

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
		camera.position.y = 284;
		
		carnivore = new Carnivore();
		Gdx.input.setInputProcessor(carnivore);
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
