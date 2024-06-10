package com.mygdx.game.Screens;

import actors.Coin;
import actors.Feind;
import actors.Player;
import actors.Rockets;
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
    private ArrayList<Coin> cList;
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
    private Sound playerdie;
    private Sound playerrocket;
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

        //Musik
        music = Gdx.audio.newMusic(Gdx.files.internal("music/background.mp3"));
        music.setLooping(true);
        if (soundEnabled) {
            music.play();
        } else {
            music.pause();
        }

        //Sound
         playerdie = Gdx.audio.newSound(Gdx.files.internal("sounds/playerdie.mp3"));
         playerrocket = Gdx.audio.newSound(Gdx.files.internal("sounds/playerrocket.mp3"));


        //Bilder
        playerTexture = new Texture("images/0.png");
        background = new Texture("images/Background_new.png");

        camera = new OrthographicCamera(w, h);
        viewport = new FitViewport(w, h, camera);
        batch = new SpriteBatch();

        backgroundScrollSpeed = 2;
        gameStarted = false;

        font = new BitmapFont(Gdx.files.internal("fonts/coins_20.fnt"),  false);

        camera.update();

        player = new Player(w / 2 - playerTexture.getWidth() / 2, 0,  playerTexture);
        feindTexture = ih.changeImgSize(130, 40, "images/zappy.png");

        // Initialisiere Feind-Texturen
        feindTextures = new Texture[]{
                ih.changeImgSize(130, 40, "images/zappy.png"),
                ih.changeImgSize(130, 40, "images/zappy.png"),
                ih.changeImgSize(130, 40, "images/zappy.png"),
                ih.changeImgSize(130, 40, "images/zappy.png")
        };

        // Initial spawn interval and timer
        spawnInterval = 3.0f; // Spawn an enemy every 2 seconds
        spawnTimer = 0;

        // Initialisiere Feind-Liste
        feindList = new ArrayList<>();

        // Initialisiere Raketen-Liste
        rocketList = new ArrayList<>();

        //Initialisiere Coin-Liste
        cList = new ArrayList<>();

        int coinWidth = 16;
        int coinHeight = 16;

        int coinsPerRow = 5;
        int numRows = 5;

        int xSpacing = 10;
        int ySpacing = 10;

        int rowXSpacing = 100;

        int startX = 50;
        int startY = 50;

        for (int row = 0; row < numRows; row++) {
            int y = startY + row * (coinHeight + ySpacing);
            int x = startX;

            for (int col = 0; col < coinsPerRow; col++) {
                Coin coin = new Coin(x, y, ih.changeImgSize(16, 16, "images/coin.png"), backgroundScrollSpeed);
                // Überprüfung auf Überlappung mit vorhandenen Münzen
                boolean overlapping = false;
                for (Coin existingCoin : cList) {
                    if (Intersector.overlaps(coin.getBoundary(), existingCoin.getBoundary())) {
                        overlapping = true;
                        break;
                    }
                }
                // Münze nur hinzufügen, wenn keine Überlappung besteht
                if (!overlapping) {
                    cList.add(coin);
                }
                x += coinWidth + xSpacing;
            }

            startY += coinHeight + ySpacing + rowXSpacing;
        }

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

    int coinshitt = 0;

    @Override
    public void render(float v) {
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
            for (Coin coin : cList) {
                coin.moveWithBackground();
                coin.draw(batch);
                font.draw(batch, "Coins: " + coinshitt, 320, 280);
                int distance = player.getDistanceTraveled();
                font.draw(batch, "Distanz: " + distance + "m", 320, 255);

                if (player.collideRectangle(coin.getBoundary())) {
                    coin.setPosition(Gdx.graphics.getWidth(), coin.getY());
                    coinshitt++;
                    soundEffect.play();
                }

                if (coin.getX() + coin.getWidth() < 0) {
                    coin.setPosition(Gdx.graphics.getWidth(), coin.getY());
                }
            }

            batch.draw(player.getCurrentFrame(), player.getX(), player.getY());
            player.update(Gdx.graphics.getDeltaTime());

            elapsedTime += Gdx.graphics.getDeltaTime();

            batch.end();

            float delta = Gdx.graphics.getDeltaTime();
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
                        playerdie.play();
                        System.out.println("Collision");
                        music.stop();
                        game.setScreen(new TitleScreen(game));
                    }
                }
            }
            batch.end();

            // Raketen erst nach 15 Sekunden spawnen
            if (totalElapsedTime >= 4) {
                rocketSpawnTimer += delta;
                if (rocketSpawnTimer >= rocketSpawnInterval) {
                    spawnNewRocket();
                    rocketSpawnTimer = 0;
                }

                // Update and render rockets
                batch.begin();
                for (Rockets rocket : rocketList) {
                    rocket.update(delta);
                    rocket.draw(batch);

                    if (player.collideRectangle(rocket.getBoundary())) {
                        playerrocket.play();
                        music.stop();
                        game.setScreen(new TitleScreen(game));
                    }
                }
                batch.end();
            }

            // Draw the player's hitbox
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            // Draw hitboxes for coins
            for (Coin coin : cList) {
                Rectangle boundary = coin.getBoundary();
                shapeRenderer.rect(boundary.x, boundary.y, boundary.width, boundary.height);
            }

            // Draw hitbox for player
            Rectangle playerBoundary = player.getCurrentFrameBoundary();
            shapeRenderer.rect(playerBoundary.x, playerBoundary.y, playerBoundary.width, playerBoundary.height);

            shapeRenderer.end();
        }

        //Prüft Eingabe auf esc-Taste
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

    public void pauseGame() {
        isPaused = true;
        music.pause();
        Gdx.app.log("GameScreen2", "Spiel pausiert");
    }

    public void resumeGame() {
        Gdx.app.log("GameScreen2", "Spiel fortgesetzt");
        if (soundEnabled) {
            music.play();
        }
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
        playerdie.dispose();
        playerrocket.dispose();
        soundEffect.dispose();
        for (Texture texture : feindTextures) {
            texture.dispose();
        }
    }
}