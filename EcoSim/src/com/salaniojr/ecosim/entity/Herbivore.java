package com.salaniojr.ecosim.entity;

import com.badlogic.gdx.math.Vector2;

public class Herbivore extends Entity {
	private static final String HERBIVORE_TEXTURE_PATH = "data/herbivore.png";

	public Herbivore(Vector2 position) {
		super(HERBIVORE_TEXTURE_PATH, AnimalType.HERBIVORE, position, AnimalType.PLANT);
	}

}
