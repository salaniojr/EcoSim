package com.salaniojr.ecosim.entity;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.salaniojr.ecosim.screen.play.ServiceLocator;

public class Herbivore extends Entity {
	private static final String HERBIVORE_TEXTURE_PATH = "data/herbivore.png";

	public Herbivore(Vector2 position) {
		super(HERBIVORE_TEXTURE_PATH, AnimalType.HERBIVORE, position);
	}

	@Override
	public void searchFoodNearby() {
		TiledMapTileLayer mapLayer = ServiceLocator.locateMap();
		
		Vector2 huntPosition = null;
		Entity animal = null;
		for (int i = 0; i < neighbors.length; i++) {
			Cell cell = mapLayer.getCell((int) (neighbors[i].x / sprite.getWidth()), (int) (neighbors[i].y / sprite.getHeight()));

			if (cell == null) {
				continue;
			}

			animal = (Entity) cell.getTile().getProperties().get("contains");

			if (animal != null) {
				if (animal.getType().equals(AnimalType.PLANT)) {
					huntPosition = neighbors[i];
					break;
				}
			} 
		}
		
		if (huntPosition != null) {
			idle();
			hunger = HUNGER_MIN;
			animal.die();

			moveTo(huntPosition);
		} else {
			move();
		}
	}

}
