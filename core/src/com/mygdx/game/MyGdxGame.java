package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;





	private TextureAtlas atlas;
	private Animation<TextureRegion> animation;
	float elapsedTime = 0.1f;

	@Override
	public void create () {
		batch = new SpriteBatch();


		//Sprite Sheet animation
		atlas = new TextureAtlas(Gdx.files.internal(""));
		Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("maincharakter");
		animation = new Animation<>(0.01f, frames, Animation.PlayMode.LOOP);

		atlas = new TextureAtlas(Gdx.files.internal(""));



		img = new Texture("images/Background1.png");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
