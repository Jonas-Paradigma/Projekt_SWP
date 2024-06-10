package com.mygdx.game.Screens;

import actors.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import helper.imageHelper;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;

public class GameScreen2 implements Screen {
    private Game game;
    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture background;
    private Texture playerTexture;
    private Vector2 playerPosition;
    private boolean isPlayerFlying;
    private float backgroundScrollSpeed;
    private float playerVerticalVelocity;
    private ArrayList<Coin> coinlist;
    private TextureAtlas walkingAtlas;
    private TextureAtlas flyingAtlas;
    private TextureAtlas standAtlas;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> flyingAnimation;
    private Animation<TextureRegion> standAnimation;
    private float elapsedTime = 0.1f;
    private boolean initialized;
    private int screenHeight;
    private boolean gameStarted;

    private Player player;
    BitmapFont font;
    private Sound soundEffect;
    private AssetManager assetManager;
    private Music music;
    private Music music2;
    private Feind feind;
    private boolean soundEnabled = true;
    private boolean isPaused = false;
    private Texture feindTexture;
    private ArrayList<Feind> feindList; // List to store enemies

    // Neue Felder für Feind-Spawn und Texturen
    private float spawnTimer;
    private float spawnInterval;
    private Texture[] feindTextures;

    // Neue Felder für Raketen-Spawn
    private ArrayList<Rockets> rocketList; // Liste zur Speicherung von Raketen
    private float rocketSpawnTimer = 0;
    private float rocketSpawnInterval = 3.0f; // Intervall für Raketen-Spawn
    private float totalElapsedTime = 0; // Gesamtzeit seit Spielbeginn
    private CoinList coinList; // Liste der Münzen
    private int coinshitt = 0;


    //Konstruktor
    public GameScreen2(Game aGame, boolean enableSound) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        this.soundEnabled = enableSound;
        initStage();

    }

    public void initStage() {
        imageHelper ih = new imageHelper();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        screenHeight = Gdx.graphics.getHeight();

        // Musik
        music = Gdx.audio.newMusic(Gdx.files.internal("music/background.mp3"));
        music.setLooping(true);
        if (soundEnabled) {
            music.play();
        } else {
            music.pause();
        }

        background = new Texture("images/Background_new.png");
        camera = new OrthographicCamera(w, h);
        viewport = new FitViewport(w, h, camera);
        batch = new SpriteBatch();

        backgroundScrollSpeed = 2;
        gameStarted = false;

        playerTexture = new Texture("images/0.png");

        font = new BitmapFont(Gdx.files.internal("fonts/coins_20.fnt"), false);

        camera.update();

        player = new Player(w / 2 - playerTexture.getWidth() / 2, 0, playerTexture);
        feindTexture = ih.changeImgSize(130, 40, "images/zappy.png");

        // Initialisiere Feind-Texturen
        feindTextures = new Texture[]{
                ih.changeImgSize(130, 40, "images/zappy.png"),
                ih.changeImgSize(60, 120, "images/vertikal_zappy.png"),
        };

        // Initial spawn interval and timer
        spawnInterval = 3.0f; // Spawn an enemy every 2 seconds
        spawnTimer = 0;

        // Initialisiere Feind-Liste
        feindList = new ArrayList<>();

        // Initialisiere Raketen-Liste
        rocketList = new ArrayList<>();

        // Initialisiere Coin-Liste
        coinList = new CoinList(5, 1); // Beispiel: 25 Münzen, Richtung 1 (oder was auch immer die Richtung bedeutet)

        assetManager = new AssetManager();
        assetManager.load("Sounds/Coin.mp3", Sound.class);
        assetManager.finishLoading();
        soundEffect = assetManager.get("Sounds/Coin.mp3", Sound.class);
    }


    @Override
    public void show() {
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }


    // Add this field to your class to track the last rocket spawn time
    private boolean rocketsSpawned = false;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); // Weißer Hintergrund
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isPaused) {
            backgroundScrollSpeed += Gdx.graphics.getDeltaTime();
            float backgroundOffsetX = backgroundScrollSpeed * 200;
            backgroundOffsetX %= background.getWidth();

            camera.position.set(750, 150, 0);
            camera.zoom = 0.6f;
            camera.update();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            for (int i = 0; i < 2; i++) {
                batch.draw(background, i * background.getWidth() - backgroundOffsetX, 0);
            }

            batch.end();

            batch.begin();

            coinList.collide(player, soundEffect, () -> coinshitt++);

            // Zeige die Anzahl eingesammelter Münzen an
            font.draw(batch, "Coins: " + coinshitt, 320, 280);

            // liste mit coins
            coinList.render(batch);
            System.out.println("Player" + player.getX());
            // Zeige die Distanz an
            int distance = player.getDistanceTraveled();
            font.draw(batch, "Distanz: " + distance + "m", 320, 255);

            // Render und aktualisiere den Spieler
            batch.draw(player.getCurrentFrame(), player.getX(), player.getY());
            player.update(Gdx.graphics.getDeltaTime());

            elapsedTime += Gdx.graphics.getDeltaTime();

            batch.end();

            totalElapsedTime += delta; // Gesamtzeit aktualisieren

            // Update the spawn timer and spawn new enemies if needed
            spawnTimer += delta;
            if (spawnTimer >= spawnInterval) {
                spawnNewEnemy();
                spawnTimer = 0;
            }

            // Update and render enemies
            batch.begin();
            if (feindTextures != null) {
                for (Feind feind : feindList) {
                    feind.update(delta);
                    feind.draw(batch);

                    if (player.collideRectangle(feind.getBoundary())) {
                        System.out.println("Collision");
                        game.setScreen(new TitleScreen(game));
                        music.stop();
                    }
                }
            }
            batch.end();

            // Raketen erst nach 15 Sekunden spawnen und dann alle 10 Sekunden
            if (totalElapsedTime >= 4) {
                rocketSpawnTimer += delta;

                if (rocketSpawnTimer >= rocketSpawnInterval) {
                    spawnNewRocket();
                    rocketSpawnTimer = 0;
                }

                // Neue Raketen nach jedem 15 Sekunden Intervall hinzufügen
                if ((int)(totalElapsedTime / 15) > (int)((totalElapsedTime - delta) / 15)) {
                    spawnRocketAtPosition(17);
                    spawnRocketAtPosition(250);
                    rocketsSpawned = true;
                } else {
                    rocketsSpawned = false;
                }

                // Update and render rockets
                batch.begin();
                for (Rockets rocket : rocketList) {
                    rocket.update(delta);
                    rocket.draw(batch);

                    if (player.collideRectangle(rocket.getBoundary())) {
                        game.setScreen(new TitleScreen(game));
                        music.stop();
                    }
                }
                batch.end();
            }

            // Draw the player's hitbox
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            // Draw hitboxes for coins
            for (Coin coin : coinList.getCoins()) {
                Rectangle boundary = coin.getBoundary();
                shapeRenderer.rect(boundary.x, boundary.y, boundary.width, boundary.height);
            }

            // Draw hitbox for player
            Rectangle playerBoundary = player.getCurrentFrameBoundary();
            shapeRenderer.rect(playerBoundary.x, playerBoundary.y, playerBoundary.width, playerBoundary.height);

            shapeRenderer.end();
        }

        // Prüft Eingabe auf ESC-Taste
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            if (isPaused) {
                pauseGame();
                game.setScreen(new PauseScreen(game, this)); // Hier wird der PauseScreen aufgerufen
            } else {
                resumeGame();
            }
        }
    }




    // Method to spawn new enemies
    private void spawnNewEnemy() {
        float initialX = Gdx.graphics.getWidth();
        float y = MathUtils.random(17, 250 - feindTextures[0].getHeight());
        Texture randomFeindTexture = feindTextures[MathUtils.random(feindTextures.length - 1)];
        Feind feind = new Feind(initialX, y, randomFeindTexture);
        feindList.add(feind);
    }

    private void spawnNewRocket() {
        float initialX = Gdx.graphics.getWidth();
        float y = MathUtils.random(17, 250 - 20);
        Rockets rocket = new Rockets(initialX, y, new Texture("animations/rakete.png"));
        rocketList.add(rocket);
    }

    private void spawnRocketAtPosition(float y) {
        float initialX = Gdx.graphics.getWidth();
        Rockets rocket = new Rockets(initialX, y, new Texture("animations/rakete.png"));
        rocketList.add(rocket);
    }


    public void pauseGame() {
        isPaused = true;
        music.pause();
        Gdx.app.log("GameScreen2", "Spiel pausiert");
    }

    public void resumeGame() {
        Gdx.app.log("GameScreen2", "Spiel fortgesetzt");
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        pauseGame();
    }

    @Override
    public void resume() {
        resumeGame();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        music.dispose();
        batch.dispose();
        background.dispose();
        soundEffect.dispose();
        for (Texture texture : feindTextures) {
            texture.dispose();
        }
    }
}