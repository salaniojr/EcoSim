package com.salaniojr.ecosim.entity;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.salaniojr.ecosim.screen.play.ServiceLocator;

public class Carnivore extends Entity {
	private static final String CARNIVORE_TEXTURE_PATH = "data/carnivore.png";
	
	public Carnivore() {
		super(CARNIVORE_TEXTURE_PATH, AnimalType.CARNIVORE);
	}

	@Override
	public void searchFoodNearby() {
		TiledMap map = ServiceLocator.locateMap();
		TiledMapTileLayer mapLayer = (TiledMapTileLayer) map.getLayers().get(0);

		for (int i = 0; i < neighbors.length; i++) {
			Cell cell = mapLayer.getCell((int) (neighbors[i].x / sprite.getWidth()), (int) (neighbors[i].y / sprite.getHeight()));

			if (cell == null) {
				continue;
			}

			AnimalType animalType = (AnimalType) cell.getTile().getProperties().get("contains");

			if (animalType != null) {
				if (animalType.equals(AnimalType.HERBIVORE)) {
					hunger = HUNGER_MIN;
					cell.getTile().getProperties().put("kill", true);
					
					moveToFoodNeighbor(i);
				} else {
				}
			} else {
				move();
			}
		}
	}
}
