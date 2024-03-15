package com.mygdx.game;

import actors.Player;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MyGdxGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture background;
	private Texture playerTexture;
	private Vector2 playerPosition;
	private boolean isPlayerFlying;
	private float backgroundScrollSpeed;
	private float playerVerticalVelocity; // Geschwindigkeit des Spielers in vertikaler Richtung
	private int screenHeight;
	private boolean gameStarted;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		screenHeight = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();
		background = new Texture("images/Background1.png");
		playerTexture = new Texture("images/0.png");
		playerPosition = new Vector2(w / 2 - playerTexture.getWidth() / 2, 0); // Startposition am Boden
		isPlayerFlying = false;
		backgroundScrollSpeed = 2;
		playerVerticalVelocity = 0; // Initialgeschwindigkeit des Spielers in vertikaler Richtung
		gameStarted = false;

		camera.setToOrtho(false, w, h);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		backgroundScrollSpeed += Gdx.graphics.getDeltaTime();
		float backgroundOffsetX = backgroundScrollSpeed * 200;
		backgroundOffsetX %= background.getWidth();


		// Hier ändern wir die vertikale Position des Spielers basierend auf seiner vertikalen Geschwindigkeit
		playerPosition.y += playerVerticalVelocity * Gdx.graphics.getDeltaTime();

		camera.position.set(background.getWidth() / 2, screenHeight / 2, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < 2; i++) {
			batch.draw(background, i * background.getWidth() - backgroundOffsetX, 0);
		}
		batch.draw(playerTexture, playerPosition.x, playerPosition.y);

		// Grenze für den oberen Bildschirmrand
		if (playerPosition.y >= screenHeight - playerTexture.getHeight()) {
			playerPosition.y = screenHeight - playerTexture.getHeight();
			playerVerticalVelocity = 0; // Setze die vertikale Geschwindigkeit auf Null
		}
		// Grenze für den unteren Bildschirmrand
		if (playerPosition.y <= 0) {
			playerPosition.y = 0;
			playerVerticalVelocity = 0; // Setze die vertikale Geschwindigkeit auf Null
		}

		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			// Ändere die vertikale Geschwindigkeit exponentiell, wenn die Leertaste gedrückt wird
			playerVerticalVelocity += 500 * Gdx.graphics.getDeltaTime(); // Beschleunigung nach oben
		} else {
			// Reduziere die vertikale Geschwindigkeit exponentiell, wenn die Leertaste nicht gedrückt wird
			playerVerticalVelocity -= 500 * Gdx.graphics.getDeltaTime(); // Beschleunigung nach unten
		}

		// Begrenze die maximale Geschwindigkeit nach oben und unten
		playerVerticalVelocity = Math.min(playerVerticalVelocity, 300); // Maximale Geschwindigkeit nach oben
		playerVerticalVelocity = Math.max(playerVerticalVelocity, -300); // Maximale Geschwindigkeit nach unten
	}

	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		playerTexture.dispose();
	}
}
