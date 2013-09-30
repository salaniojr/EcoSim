package com.salaniojr.ecosim.screen;

import com.salaniojr.ecosim.screen.play.ServiceLocator;

import aurelienribon.tweenengine.Tween;

public class LoadProgress {
	private float percent = 0;
	private int totalWork;
	private String message;
	private Tween tweenCallback;
	
	public LoadProgress() {
		this.message = "game";
	}

	public LoadProgress(Tween tweenCallback) {
		this.tweenCallback = tweenCallback;
	}

	public void setTotalWork(String message, int totalWork) {
		this.percent = 0;
		this.message = message;
		this.totalWork = totalWork;
	}
	
	public void progress(int amount) {
		float progressPercent = amount * 100 / totalWork;
		percent += progressPercent;
	}
	
	public float getProgress() {
		return percent;
	}
	
	public String getMessage() {
		return String.format("Loading %s %.2f", message, percent);
	}

	public void done() {
		tweenCallback.start(ServiceLocator.locateTweenManager());
	}
}
