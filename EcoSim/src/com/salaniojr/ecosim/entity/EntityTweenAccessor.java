package com.salaniojr.ecosim.entity;

import aurelienribon.tweenengine.TweenAccessor;

public class EntityTweenAccessor implements TweenAccessor<Entity> {

	public static final int MOVEX = 0;
	public static final int MOVEY = 1;
	public static final int MOVEXY = 2;

	@Override
	public int getValues(Entity target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case MOVEX:
			returnValues[0] = target.getX();
			return 1;
		case MOVEY:
			returnValues[0] = target.getY();
			return 1;
		case MOVEXY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Entity target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case MOVEX:
			target.setPosition(newValues[0], target.getY());
			break;
		case MOVEY:
			target.setPosition(target.getX(), newValues[0]);
			break;
		case MOVEXY:
			target.setPosition(newValues[0], newValues[1]);
			break;
		default:
			assert false;
		}
	}

}
