package com.salaniojr.ecosim.screen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.salaniojr.ecosim.EcoSim;
import com.salaniojr.ecosim.screen.play.PlayScreen;
import com.salaniojr.ecosim.screen.play.ServiceLocator;

public class SplashScreen implements Screen {

	private SpriteBatch spriteBatch;
	private BitmapFont font;
	private TweenManager tweenManager;
	private OrthographicCamera camera;
	private Sprite background;
	private EcoSim ecoSim;

	public SplashScreen(EcoSim ecoSim) {
		this.ecoSim = ecoSim;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tweenManager.update(delta);

		spriteBatch.begin();
		background.draw(spriteBatch);
		drawHud(spriteBatch);
		spriteBatch.end();
	}

	private void drawHud(SpriteBatch spriteBatch) {
		font.draw(spriteBatch, "Eco Sim", Gdx.graphics.getWidth() / 2 - 50 - 160, Gdx.graphics.getHeight() / 2 + 50);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void show() {
		spriteBatch = new SpriteBatch();

		Texture texture = new Texture(Gdx.files.internal("data/splash.png"));
		background = new Sprite(texture);
		background.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0f);

		setFonts();
		setupCamera();

		tweenManager = ServiceLocator.locateTweenManager();
		Tween.registerAccessor(Sprite.class, new SplashTweenAccessor());
		Tween.registerAccessor(BitmapFont.class, new FontTweenAccessor());

		Tween.to(background, SplashTweenAccessor.SHOW, 3f).target(1f).start(ServiceLocator.locateTweenManager())
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if (type == TweenCallback.COMPLETE) {
							// start loading
							Tween.to(background, SplashTweenAccessor.FADE, 3f).target(0f)
									.start(ServiceLocator.locateTweenManager()).setCallback(new TweenCallback() {

										@Override
										public void onEvent(int type, BaseTween<?> source) {
											if (type == TweenCallback.COMPLETE) {
												ecoSim.setScreen(new PlayScreen());
											}
										}

									});
						}
					}
				});

		Tween.to(font, FontTweenAccessor.SHOW, 3f).target(1f).start(ServiceLocator.locateTweenManager())
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if (type == TweenCallback.COMPLETE) {
							Tween.to(font, FontTweenAccessor.SHOW, 3f).target(0f).start(ServiceLocator.locateTweenManager())
									.setCallback(new TweenCallback() {

										@Override
										public void onEvent(int type, BaseTween<?> source) {

										}

									});
						}
					}
				});
	}

	private void setupCamera() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();

		camera.position.x = w / 2;
		camera.position.y = h / 2;
	}

	private void setFonts() {
		font = ServiceLocator.locateFont("AndironOutlineNF.ttf", 100);
		font.setColor(0.76f, 0.77f, 0.51f, 0.0f);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		font.dispose();
		spriteBatch.dispose();
	}

}
