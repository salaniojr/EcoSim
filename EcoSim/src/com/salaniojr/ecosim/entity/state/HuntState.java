package com.salaniojr.ecosim.entity.state;

import com.salaniojr.ecosim.entity.Entity;

public class HuntState implements State {

	private Entity entity;

	public HuntState(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void update(float delta) {
		if (entity.isHungry()) {
			entity.searchFoodNearby();
		} else {
			entity.idle();
		}
			
		
		
		System.out.println("Now hunting!");
	}

}
