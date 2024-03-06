package com.mygdx.game;

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

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();
		background = new Texture("images/Background1.png"); // Lade das Hintergrundbild
		playerTexture = new Texture("images/0.png"); // Lade das Spielerbild
		playerPosition = new Vector2(w / 2 - playerTexture.getWidth() / 2, h / 2 - playerTexture.getHeight() / 2); // Setze die Anfangsposition des Spielers in die Mitte des Bildschirms
		isPlayerFlying = false;
		backgroundScrollSpeed = 2; // Hintergrundscrollgeschwindigkeit

		// Setze die Kamera auf die Bildschirmgröße
		camera.setToOrtho(false, w, h);
	}

	@Override
	public void render() {
		// Clear screen
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Bewege den Hintergrund
		backgroundScrollSpeed += Gdx.graphics.getDeltaTime(); // Aktualisiere die Scrollgeschwindigkeit
		float backgroundOffsetX = backgroundScrollSpeed * 100; // Berechne den Hintergrundversatz basierend auf der Geschwindigkeit
		backgroundOffsetX %= background.getWidth(); // Stelle sicher, dass der Hintergrund wiederholt wird

		// Bewege den Spieler, wenn er fliegt
		if (isPlayerFlying) {
			playerPosition.y += 2; // Beispielbewegung nach oben
		}

		// Fokussiere die Kamera auf den Spieler
		camera.position.set(playerPosition.x + playerTexture.getWidth() / 2, playerPosition.y + playerTexture.getHeight() / 2, 0);
		camera.update();

		// Zeichne das Hintergrundbild
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// Zeichne den Hintergrund mehrmals nebeneinander, um eine nahtlose Wiederholung zu erzeugen
		for (int i = 0; i < 2; i++) {
			batch.draw(background, i * background.getWidth() - backgroundOffsetX, 0);
		}
		batch.draw(playerTexture, playerPosition.x, playerPosition.y);
		batch.end();

		// Überprüfe Benutzerinteraktion
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (!isPlayerFlying) {
				isPlayerFlying = true; // Starte das Fliegen, wenn die Leertaste gedrückt wird
			}
		}

		// Stoppe das Fliegen, wenn der Spieler den oberen Bildschirmrand erreicht
		if (playerPosition.y >= Gdx.graphics.getHeight() - playerTexture.getHeight()) {
			isPlayerFlying = false;
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		playerTexture.dispose();
	}
}
