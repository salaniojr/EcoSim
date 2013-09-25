package com.salaniojr.ecosim;

import com.badlogic.gdx.Game;
import com.salaniojr.ecosim.screen.play.PlayScreen;

public class EcoSim extends Game {
	
	@Override
	public void create() {		
		setScreen(new PlayScreen());
		
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
