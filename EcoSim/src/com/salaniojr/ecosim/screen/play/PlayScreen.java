package com.salaniojr.ecosim.screen.play;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.salaniojr.ecosim.screen.LoadProgress;

public class PlayScreen implements Screen {

	private SpriteBatch spriteBatch;
	private BitmapFont font;

	private OrthographicCamera camera;

	private TweenManager tweenManager;

	private World world;
	
	@Override
	public void show() {
		world = new World();
		world.init();

		spriteBatch = world.getTileRendererSpriteBatch();
		
		setFonts();
		setupCamera();
		
		tweenManager = ServiceLocator.locateTweenManager();
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	private void update(float delta) {
		tweenManager.update(delta);
		
		world.update(delta);
	}

	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.updateRenderer(camera);

		spriteBatch.begin();
		world.draw(camera, spriteBatch);
		drawHud(spriteBatch);
		spriteBatch.end();
	}

	private void drawHud(SpriteBatch spriteBatch) {
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
	}

	private void setupCamera() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();

		camera.position.x = w / 2;
		camera.position.y = h / 2;
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
		world.dispose();
		
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	public void loadData(LoadProgress progress) {
		
	}
}
