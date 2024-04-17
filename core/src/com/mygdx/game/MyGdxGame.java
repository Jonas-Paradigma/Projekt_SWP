package com.mygdx.game;

import actors.Coin;
import actors.Player;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import helper.imageHelper;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private FitViewport viewport;
	private SpriteBatch batch;
	private Texture background;
	private Texture playerTexture;
	private Vector2 playerPosition;
	private boolean isPlayerFlying;
	private float backgroundScrollSpeed;
	private float playerVerticalVelocity; // Geschwindigkeit des Spielers in vertikaler Richtung
	private int screenHeight;
	private boolean gameStarted;

	ArrayList<Coin> cList;



	private TextureAtlas atlas;
	private Animation<TextureRegion> animation;
	float elapsedTime = 0.1f;


	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		screenHeight = Gdx.graphics.getHeight();

		// Hintergrund laden
		background = new Texture("images/Background_new.png");
		// Hintergrundgröße für die Kamera einstellen
		//camera = new OrthographicCamera(background.getWidth(), background.getHeight());
		camera = new OrthographicCamera(w, h);
		//viewport = new FitViewport(background.getWidth(), background.getHeight(), camera);
		viewport = new FitViewport(w, h, camera);
		batch = new SpriteBatch();

		//Atlas laufanimation von dem Spieler
		atlas = new TextureAtlas(Gdx.files.internal("animations/laufen.atlas"));
		Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("mainwalk");
		animation = new Animation<>(0.09f, frames, Animation.PlayMode.LOOP);

		atlas = new TextureAtlas(Gdx.files.internal("animations/laufen.atlas"));

		playerTexture = new Texture("images/0.png");
		playerPosition = new Vector2(w / 2 - playerTexture.getWidth() / 2, 0); // Startposition am Boden
		isPlayerFlying = false;
		backgroundScrollSpeed = 2;
		playerVerticalVelocity = 0; // Initialgeschwindigkeit des Spielers in vertikaler Richtung
		gameStarted = false;


		camera.update();


		//Coins einfügen
		cList = new ArrayList<Coin>();
		imageHelper ih = new imageHelper();
		for (int i = 0; i < 5; i++) {
			int randomX = (int) (Math.random() * Gdx.graphics.getWidth());
			int randomY = (int) (Math.random() * Gdx.graphics.getHeight());
			cList.add(new Coin(randomX, randomY, ih.changeImgSize(150, 150, "images/coin.png"), 5, cList));
		}

	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		backgroundScrollSpeed += Gdx.graphics.getDeltaTime();
		float backgroundOffsetX = backgroundScrollSpeed * 200;
		backgroundOffsetX %= background.getWidth();

		playerPosition.y += playerVerticalVelocity * Gdx.graphics.getDeltaTime();

		camera.position.set(750, 150, 0);
		camera.zoom = 0.6f;
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		// Draw coins
		for (Coin coin : cList) {
			coin.draw(batch);
		}

		// Draw background
		for (int i = 0; i < 2; i++) {
			batch.draw(background, i * background.getWidth() - backgroundOffsetX, 0);
		}

		// Calculate next frame of player animation
		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);

		// Draw player animation
		batch.draw(currentFrame, playerPosition.x, playerPosition.y);

		batch.end();

		// Check boundaries
		if (playerPosition.y >= 240) {
			playerPosition.y = 240;
			playerVerticalVelocity = 0;
		}
		if (playerPosition.y <= 17) {
			playerPosition.y = 17;
			playerVerticalVelocity = 0;
		}

		// Change vertical velocity based on space key
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			playerVerticalVelocity += 500 * Gdx.graphics.getDeltaTime(); // Accelerate upwards
		} else {
			playerVerticalVelocity -= 500 * Gdx.graphics.getDeltaTime(); // Accelerate downwards
		}

		// Limit vertical velocity
		playerVerticalVelocity = Math.min(playerVerticalVelocity, 300); // Maximum speed upwards
		playerVerticalVelocity = Math.max(playerVerticalVelocity, -300); // Maximum speed downwards

		// Check for collision
		for (Coin c : cList) {
			if (Player.collideRectangle(c.getBoundary())) {
				c.setRandomPosition();
			}
		}

		// Update coins
		for (Coin coin : cList) {
			coin.act(Gdx.graphics.getDeltaTime());
		}
	}




	@Override
	public void resize(int width, int height){
		viewport.update(width, height);
	}
	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		playerTexture.dispose();
	}
}
