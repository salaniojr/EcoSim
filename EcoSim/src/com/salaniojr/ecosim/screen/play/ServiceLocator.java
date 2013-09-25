package com.salaniojr.ecosim.screen.play;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class ServiceLocator {
	private static TiledMap map;
	private static TweenManager tweenManager;

	public static void provide(TiledMap map) {
		ServiceLocator.map = map;
	}

	public static TiledMap locateMap() {
		assert map != null;
		return map;
	}

	public static TweenManager locateTweenManager() {
		if (tweenManager == null) {
			tweenManager = new TweenManager();
		}

		return tweenManager;
	}

	public static BitmapFont locateFont(int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/American Captain.ttf"));
		BitmapFont font = generator.generateFont(10); // font size in pixels
		generator.dispose();

		return font;
	}
}
