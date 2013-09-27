package com.salaniojr.ecosim.entity.state;

import com.salaniojr.ecosim.entity.Entity;

public class BreedState implements State {
	
	private Entity entity;

	public BreedState(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void update(float delta) {
		if (entity.isMoving() || entity.isBreeding()) {
			return;
		}
		
		entity.searchPartnerNearby();
	}

}
