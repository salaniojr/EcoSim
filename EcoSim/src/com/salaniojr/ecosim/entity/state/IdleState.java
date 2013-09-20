package com.salaniojr.ecosim.entity.state;

import com.salaniojr.ecosim.entity.Entity;

public class IdleState implements State {
	private Entity carnivore;

	public IdleState(Entity carnivore) {
		this.carnivore = carnivore;
	}

	@Override
	public void update(float delta) {
		if (carnivore.isHungry()) {
			carnivore.hunt();
		}
	}
}
