package com.salaniojr.ecosim.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.salaniojr.ecosim.screen.play.ServiceLocator;

public class Plant extends Entity {

	private Vector2 position;
	private TextureRegion[][] regions;

	public Plant(Vector2 position, TextureRegion[][] regions) {
		super(AnimalType.PLANT);
		this.position = position;
		this.regions = regions;
	}

	@Override
	public void update(float delta) {

	}

	protected int getYInCellCoord(float y) {
		return (int) (y / 16);
	}

	protected int getXinCellCoord(float x) {
		return (int) (x / 16);
	}

	@Override
	protected void die() {
		TiledMapTileLayer mapLayer = ServiceLocator.locateMap();

		Cell cell = mapLayer.getCell(getXinCellCoord(position.x), getYInCellCoord(position.y));

		StaticTiledMapTile mapTile = new StaticTiledMapTile(regions[0][0]);
		cell.setTile(mapTile);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {

	}

	@Override
	public void searchFoodNearby() {

	}

}
