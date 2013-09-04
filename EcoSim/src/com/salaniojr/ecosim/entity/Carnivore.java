package com.salaniojr.ecosim.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Carnivore implements InputProcessor {
	private Sprite sprite;
	
	public Carnivore() {
		sprite = new Sprite(new Texture(Gdx.files.internal("data/carnivore.png")));
	}
	
	public float getX() {
		return sprite.getX();
	}
	
	public float getY() {
		return sprite.getY();
	}
	
	public void draw(SpriteBatch spriteBatch) {
		sprite.draw(spriteBatch);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (Keys.LEFT == keycode) {
			sprite.setPosition(sprite.getX() - sprite.getWidth(), sprite.getY());
		}
		
		if (Keys.RIGHT == keycode) {
			sprite.setPosition(sprite.getX() + sprite.getWidth(), sprite.getY());
		}
		
		if (Keys.UP == keycode) {
			sprite.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight());
		}
		
		if (Keys.DOWN == keycode) {
			sprite.setPosition(sprite.getX(), sprite.getY() - sprite.getHeight());
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void update(float delta) {
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public void setY(float f) {
		sprite.setPosition(sprite.getX(), f);
	}

	public void setX(float i) {
		sprite.setPosition(i, sprite.getY());
	}

	public float getHeight() {
		return sprite.getHeight();
	}
}
