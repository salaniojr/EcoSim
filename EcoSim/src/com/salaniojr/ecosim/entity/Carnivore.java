package com.salaniojr.ecosim.entity;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.salaniojr.ecosim.screen.play.ServiceLocator;

public class Carnivore extends Entity {
	private static final String CARNIVORE_TEXTURE_PATH = "data/carnivore.png";

	public Carnivore(Vector2 position) {
		super(CARNIVORE_TEXTURE_PATH, AnimalType.CARNIVORE, position);
	}

	@Override
	public void searchFoodNearby() {
		TiledMapTileLayer mapLayer = ServiceLocator.locateMap();

		Vector2 huntPosition = null;
		Entity animal = null;
		for (int i = 0; i < neighbors.length; i++) {
			Cell cell = mapLayer.getCell((int) (neighbors[i].x / sprite.getWidth()), (int) (neighbors[i].y / sprite.getHeight()));

			// is cell out of world bounds?
			if (cell == null) {
				continue;
			}

			animal = (Entity) cell.getTile().getProperties().get("contains");

			if (animal != null) {
				if (animal.getType().equals(AnimalType.HERBIVORE)) {
					huntPosition = neighbors[i];
					break;
				}
			}
		}

		if (huntPosition != null) {
			idle();
			restoreHunger();
			animal.die();

			moveTo(huntPosition);
		} else {
			move();
		}
	}
}
