package com.salaniojr.ecosim.entity;

import aurelienribon.tweenengine.TweenAccessor;

public class EntityTweenAccessor implements TweenAccessor<Entity> {

	public static final int MOVEX = 0;
	public static final int MOVEY = 1;
	public static final int MOVEXY = 2;
	public static final int FADE = 3;

	@Override
	public int getValues(Entity target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case MOVEXY:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;
		case FADE:
			returnValues[0] = target.getAlpha();
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Entity target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case MOVEXY:
			target.setPosition(newValues[0], newValues[1]);
			break;
		case FADE:
			target.setAlpha(newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
