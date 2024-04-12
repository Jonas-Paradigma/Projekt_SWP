package com.mygdx.game;

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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


    public class MyGdxGame extends ApplicationAdapter {

        static public Skin gameSkin;
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



        private TextureAtlas atlas;
        private Animation<TextureRegion> animation;
        float elapsedTime = 0.1f;
        @Override
        public void create() {
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();
            screenHeight = Gdx.graphics.getHeight();

            camera = new OrthographicCamera(1, h / w);


            batch = new SpriteBatch();

            //Atlas laufanimation von dem Spieler
            atlas = new TextureAtlas(Gdx.files.internal("animations/laufen.atlas"));
            Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("mainwalk");
            animation = new Animation<>(0.09f, frames, Animation.PlayMode.LOOP);

            atlas = new TextureAtlas(Gdx.files.internal("animations/laufen.atlas"));

            background = new Texture("images/Background_new.png");
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

            camera.position.set(background.getWidth() / 2, screenHeight / 2, 0);
            camera.update();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // Zeichne den Hintergrund
            for (int i = 0; i < 2; i++) {
                batch.draw(background, i * background.getWidth() - backgroundOffsetX, 0);
            }

            // Berechne die nächste Frame der Animation
            elapsedTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);

            // Ändere die vertikale Geschwindigkeit basierend auf der Leertaste
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                playerVerticalVelocity = 300; // Setze die Geschwindigkeit nach oben
            } else {
                playerVerticalVelocity = -300; // Setze die Geschwindigkeit nach unten
            }

            // Begrenze die vertikale Geschwindigkeit nach oben und unten
            playerVerticalVelocity = Math.min(playerVerticalVelocity, 300); // Maximale Geschwindigkeit nach oben
            playerVerticalVelocity = Math.max(playerVerticalVelocity, -300); // Maximale Geschwindigkeit nach unten

            // Hier ändern wir die vertikale Position des Spielers basierend auf seiner vertikalen Geschwindigkeit
            playerPosition.y += playerVerticalVelocity * Gdx.graphics.getDeltaTime();

            // Zeichne die Animation des Spielers
            batch.draw(currentFrame, playerPosition.x, playerPosition.y);

            batch.end();

            // Grenzen für den oberen und unteren Bildschirmrand
            if (playerPosition.y >= screenHeight - currentFrame.getRegionHeight()) {
                playerPosition.y = screenHeight - currentFrame.getRegionHeight();
            }
            if (playerPosition.y <= 0) {
                playerPosition.y = 0;
            }
        }

    }
