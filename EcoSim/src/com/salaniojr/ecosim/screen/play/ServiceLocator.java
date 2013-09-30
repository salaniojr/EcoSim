package com.salaniojr.ecosim.screen.play;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class ServiceLocator {
	private static TiledMapTileLayer map;
	private static TweenManager tweenManager;

	public static void provide(TiledMap map) {
		ServiceLocator.map = (TiledMapTileLayer) map.getLayers().get(0);
	}

	public static TiledMapTileLayer locateMap() {
		assert map != null;
		return map;
	}

	public static TweenManager locateTweenManager() {
		if (tweenManager == null) {
			tweenManager = new TweenManager();
		}

		return tweenManager;
	}

	public static BitmapFont locateFont(String fontName, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/" + fontName));// ""));
		BitmapFont font = generator.generateFont(size); // font size in pixels
		generator.dispose();

		return font;
	}
}
