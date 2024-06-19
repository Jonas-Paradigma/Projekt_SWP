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


    private float elapsedTime = 0.001f;
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

    //Sounds und music
    private Sound playerdie;
    private Sound playerrocket;


    private float timeSinceDeath = 0;
    private boolean goToTitleScreen = false;

    private boolean playerDiedByZappy = false;
    private boolean playerDiedByRocket = false;





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

        //Sound
        playerdie = Gdx.audio.newSound(Gdx.files.internal("sounds/playerdie.mp3"));
        playerrocket = Gdx.audio.newSound(Gdx.files.internal("sounds/playerrocket.mp3"));


        //Bilder
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

        feindTextures = new Texture[]{
                ih.changeImgSize(130, 40, "images/zappy.png"),
                ih.changeImgSize(45, 110, "images/vertikal_zappy.png"),
        };


        spawnInterval = 2.5f; // Der Feind wird alle 4.5 Sekunden eingefügt
        spawnTimer = 0;

        feindList = new ArrayList<>();
        rocketList = new ArrayList<>();
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


    private boolean rocketsSpawned = false;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1); // Weißer Hintergrund
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (!isPaused) {
            // Hier beginnt der Batch für das Rendering

            backgroundScrollSpeed += Gdx.graphics.getDeltaTime();
            float backgroundOffsetX = backgroundScrollSpeed * 200;
            backgroundOffsetX %= background.getWidth();

            camera.position.set(750, 150, 0);
            camera.zoom = 0.6f;
            camera.update();

            batch.setProjectionMatrix(camera.combined);

            for (int i = 0; i < 2; i++) {
                batch.draw(background, i * background.getWidth() - backgroundOffsetX, 0);
            }

            coinList.collide(player, soundEffect, () -> coinshitt++);

            // Zeige die Anzahl eingesammelter Münzen an
            font.draw(batch, "Coins: " + coinshitt, 320, 280);

            coinList.render(batch);
            System.out.println("Player" + player.getX());

            // Zeige die Distanz an
            int distance = player.getDistanceTraveled();
            font.draw(batch, "Distanz: " + distance + "m", 320, 255);

            batch.draw(player.getCurrentFrame(), player.getX(), player.getY());
            player.update(Gdx.graphics.getDeltaTime());

            elapsedTime += Gdx.graphics.getDeltaTime();

            totalElapsedTime += delta;

            spawnTimer += delta;
            if (spawnTimer >= spawnInterval) {
                spawnNewEnemy();
                spawnTimer = 0;
            }

            if (feindTextures != null) {
                for (Feind feind : feindList) {
                    feind.update(delta);
                    feind.draw(batch);

                    if (!playerDiedByZappy && player.collideRectangle(feind.getBoundary())) {
                        music.stop();
                        playerdie.play();
                        player.zappydie();
                        playerDiedByZappy = true;
                        this.timeSinceDeath = 0;
                    }

                }

            }

            // Raketen erst nach 15 Sekunden spawnen
            if (totalElapsedTime >= 4) {
                rocketSpawnTimer += delta;

                if (rocketSpawnTimer >= rocketSpawnInterval) {
                    spawnNewRocket();
                    rocketSpawnTimer = 0;
                }

                // Neue Raketen nach jeden 15 Sekunden
                if ((int)(totalElapsedTime / 15) > (int)((totalElapsedTime - delta) / 15)) {
                    spawnRocketAtPosition(17);
                    spawnRocketAtPosition(250);
                    rocketsSpawned = true;
                } else {
                    rocketsSpawned = false;
                }

                for (Rockets rocket : rocketList) {
                    rocket.update(delta);
                    rocket.draw(batch);

                    if (!playerDiedByRocket && player.collideRectangle(rocket.getBoundary())) {
                        player.rocketdie2();
                        music.stop();
                        playerrocket.play();
                        playerDiedByRocket = true;
                        this.timeSinceDeath = 0;
                    }

                }


                if (!player.isAlivezappy() || !player.isAliverocket()) {
                    timeSinceDeath += delta;

                    if (!goToTitleScreen && timeSinceDeath >= 4) {
                        goToTitleScreen = true;
                        game.setScreen(new TitleScreen(game));
                    }
                }
            }

        }

        batch.end();

        // Prüfen auf Eingabe und Pause
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