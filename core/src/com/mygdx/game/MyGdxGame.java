package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture background;
	private Texture playerTexture;
	private Vector2 playerPosition;
	private boolean isPlayerRunning;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();
		background = new Texture("images/Background1.png"); // Lade das Hintergrundbild
		playerTexture = new Texture("images/0.png"); // Lade das Spielerbild
		playerPosition = new Vector2(w / 2, h / 2); // Setze die Anfangsposition des Spielers in die Mitte des Bildschirms
		isPlayerRunning = false;

		// Setze die Kamera auf die Bildschirmgröße
		camera.setToOrtho(false, w, h);
	}

	@Override
	public void render() {
		// Clear screen
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Bewege den Spieler, wenn er läuft
		if (isPlayerRunning) {
			playerPosition.x += 2; // Beispielbewegung nach rechts
		}

		// Fokussiere die Kamera auf den Spieler
		camera.position.set(playerPosition.x + playerTexture.getWidth() / 2, playerPosition.y + playerTexture.getHeight() / 2, 0);
		camera.update();

		// Zeichne das Hintergrundbild
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(playerTexture, playerPosition.x, playerPosition.y);
		batch.end();

		// Überprüfe Benutzerinteraktion
		if (Gdx.input.justTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos); // Umrechnung von Bildschirmkoordinaten zu Spielkoordinaten
			// Hier kannst du deine Logik für Benutzerinteraktion implementieren
			isPlayerRunning = true; // Starte die Spielerbewegung, wenn der Bildschirm berührt wird
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		background.dispose();
		playerTexture.dispose();
	}
}
