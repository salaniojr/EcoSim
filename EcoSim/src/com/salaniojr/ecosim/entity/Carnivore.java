package com.salaniojr.ecosim.entity;

import com.badlogic.gdx.math.Vector2;

public class Carnivore extends Entity {
	private static final String CARNIVORE_TEXTURE_PATH = "data/carnivore.png";

	public Carnivore(Vector2 position) {
		super(CARNIVORE_TEXTURE_PATH, AnimalType.CARNIVORE, position, AnimalType.HERBIVORE);
	}
}
