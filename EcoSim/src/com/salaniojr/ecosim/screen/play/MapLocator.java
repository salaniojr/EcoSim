package com.salaniojr.ecosim.screen.play;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class MapLocator {
	private static TiledMap map;
	
	public static void provide(TiledMap map) {
		MapLocator.map = map;
	}
	
	public static TiledMap locate() {
		return map;
	}
}
