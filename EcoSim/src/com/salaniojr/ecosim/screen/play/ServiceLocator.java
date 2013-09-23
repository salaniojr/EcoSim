package com.salaniojr.ecosim.screen.play;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class ServiceLocator {
	private static TiledMap map;
	private static TweenManager tweenManager;
	
	public static void provide(TiledMap map) {
		ServiceLocator.map = map;
	}
	
	public static TiledMap locate() {
		return map;
	}
	
	public static TweenManager locateTweenManager() {
		if (tweenManager == null) {
			tweenManager = new TweenManager();
		}
		
		return tweenManager;
	}
}

