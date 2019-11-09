package com.jumpdontdie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {

	private AssetManager assetManeger;

	@Override
	public void create () {
		//setScreen(new MainGameScreen(this));
		//setScreen(new Box2DScreen(this));
		assetManeger = new AssetManager();
		assetManeger.load("floor.png", Texture.class);
		assetManeger.load("overfloor.png", Texture.class);
		assetManeger.load("spike.png", Texture.class);
		assetManeger.load("player.png", Texture.class);
		assetManeger.finishLoading();

		setScreen(new GameScreen(this));
	}

	public AssetManager getAssetManeger(){
		return assetManeger;
	}
}
