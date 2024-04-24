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
	private float playerVerticalVelocity;
	private ArrayList<Coin> cList;
	private TextureAtlas walkingAtlas;
	private TextureAtlas flyingAtlas;
	private TextureAtlas standAtlas;
	private Animation<TextureRegion> walkingAnimation;
	private Animation<TextureRegion> flyingAnimation;
	private Animation<TextureRegion> standAnimation;
	private float elapsedTime = 0.1f;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// Hintergrund laden
		background = new Texture("images/Background_new.png");
		camera = new OrthographicCamera(w, h);
		viewport = new FitViewport(w, h, camera);
		batch = new SpriteBatch();

		// Atlas für Laufanimation des Spielers laden
		walkingAtlas = new TextureAtlas(Gdx.files.internal("animations/laufen.atlas"));
		Array<TextureAtlas.AtlasRegion> walkingFrames = walkingAtlas.findRegions("mainwalk");
		walkingAnimation = new Animation<>(0.09f, walkingFrames, Animation.PlayMode.LOOP);

		// Atlas für Fluganimation des Spielers laden
		flyingAtlas = new TextureAtlas(Gdx.files.internal("animations/mainfly.atlas"));
		Array<TextureAtlas.AtlasRegion> flyingFrames = flyingAtlas.findRegions("mainfly");
		flyingAnimation = new Animation<>(0.09f, flyingFrames, Animation.PlayMode.LOOP);

		// Atlas für Stand-Animation des Spielers laden
		standAtlas = new TextureAtlas(Gdx.files.internal("animations/maindown.atlas"));
		Array<TextureAtlas.AtlasRegion> standFrames = standAtlas.findRegions("maindown");
		standAnimation = new Animation<>(0.09f, standFrames, Animation.PlayMode.LOOP);

		playerTexture = new Texture("images/0.png");
		playerPosition = new Vector2(w / 2 - playerTexture.getWidth() / 2, 0);
		isPlayerFlying = false;
		backgroundScrollSpeed = 2;
		playerVerticalVelocity = 0;

		camera.update();

		// Münzen erstellen
		cList = new ArrayList<>();
		imageHelper ih = new imageHelper();

		// Erstelle Münzen an verschiedenen Stellen des Hintergrunds
		for (int i = 0; i < 100; i++) {
			int randomX = (int) (Math.random() * (background.getWidth() - 14)); // 14 ist die Breite der Münze
			int randomY = (int) (Math.random() * (background.getHeight() - 14)); // 14 ist die Höhe der Münze
			Texture coinTexture = ih.changeImgSize(14, 14, "images/coin.png");
			Coin coin = new Coin(randomX, randomY, coinTexture);
			cList.add(coin);
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

		// Hintergrund zeichnen
		for (int i = 0; i < 2; i++) {
			batch.draw(background, i * background.getWidth() - backgroundOffsetX, 0);
		}

		// Münzen zeichnen und mit dem Hintergrund bewegen
		for (Coin coin : cList) {
			coin.moveWithBackground(backgroundScrollSpeed);
			coin.draw(batch);
		}


		elapsedTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame;

		float lowerBackgroundBoundary = 17;
		if (playerPosition.y <= lowerBackgroundBoundary) {
			isPlayerFlying = false;
		}

		if (isPlayerFlying && !Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			currentFrame = standAnimation.getKeyFrame(elapsedTime, true);
		} else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			currentFrame = flyingAnimation.getKeyFrame(elapsedTime, true);
			isPlayerFlying = true;
		} else {
			currentFrame = walkingAnimation.getKeyFrame(elapsedTime, true);
		}
		batch.draw(currentFrame, playerPosition.x, playerPosition.y);

		batch.end();

		if (playerPosition.y >= 240) {
			playerPosition.y = 240;
			playerVerticalVelocity = 0;
		}
		if (playerPosition.y <= 17) {
			playerPosition.y = 17;
			playerVerticalVelocity = 0;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			playerVerticalVelocity += 500 * Gdx.graphics.getDeltaTime();
		} else {
			playerVerticalVelocity -= 500 * Gdx.graphics.getDeltaTime();
		}

		playerVerticalVelocity = Math.min(playerVerticalVelocity, 300);
		playerVerticalVelocity = Math.max(playerVerticalVelocity, -300);

		for (Coin c : cList) {
			if (Player.collideRectangle(c.getBoundary())) {
				c.setRandomPosition();
			}
			c.act(Gdx.graphics.getDeltaTime());
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		playerTexture.dispose();
		walkingAtlas.dispose();
		flyingAtlas.dispose();
	}
}