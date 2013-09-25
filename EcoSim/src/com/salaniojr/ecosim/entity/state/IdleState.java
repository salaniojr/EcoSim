package com.salaniojr.ecosim.entity.state;

import java.util.Random;

import com.salaniojr.ecosim.entity.Entity;

public class IdleState implements State {
	private Entity entity;
	private boolean stay;
	private float stayAmount;

	public IdleState(Entity carnivore) {
		this.entity = carnivore;
	}

	@Override
	public void update(float delta) {
		if (entity.isHungry()) {
			entity.hunt();
		} else {
			if (stay) {
				stayAmount -= delta;
				if (stayAmount <= 0) {
					stay = false;
					stayAmount = 0;
				}
				
				return;
			}
			
			Random random = new Random();
			int moveOrNot = random.nextInt(2);
			
			if (moveOrNot == 1) {
				entity.move();
			} else {
				stayAmount = new Random().nextInt(5) + 1;
				stay = true;
			}
		}
	}
}
