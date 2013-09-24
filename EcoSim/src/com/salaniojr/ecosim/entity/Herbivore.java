package com.salaniojr.ecosim.entity;

public class Herbivore extends Entity {
	private static final String HERBIVORE_TEXTURE_PATH = "data/herbivore.png";

	public Herbivore() {
		super(HERBIVORE_TEXTURE_PATH, AnimalType.HERBIVORE);
	}

	@Override
	public void searchFoodNearby() {
		move();
	}

}
