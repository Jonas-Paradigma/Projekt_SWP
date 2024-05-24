package com.mygdx.game.Screens;

import actors.Coin;
import actors.NPCs;
import actors.Player;
import actors.Feind;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import helper.imageHelper;

import java.util.ArrayList;

public class GameScreen2 implements Screen {
    private Game game;
    private Stage stage;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture background;
    private Texture playerTexture;
    private Texture npcTexture;
    private Texture feindTexture;

    private float backgroundScrollSpeed;
    private ArrayList<Coin> cList;

    private ArrayList<NPCs> npcList;
    private ArrayList<Feind> feindList; // List to store enemies
    private float elapsedTime = 0.1f;
    private boolean initialized;
    private int screenHeight;
    private boolean gameStarted;

    private Player player;
    private NPCs npc;
    BitmapFont font;
    private Sound soundEffect;
    private AssetManager assetManager;
    private Music music;

    // New fields for enemy spawning
    private float spawnTimer;
    private float spawnInterval;

    public GameScreen2(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        initStage();
    }

    public void initStage() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/background.mp3"));
        music.setLooping(true);
        music.play();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        screenHeight = Gdx.graphics.getHeight();
        background = new Texture("images/Background_new.png");
        camera = new OrthographicCamera(w, h);
        viewport = new FitViewport(w, h, camera);
        batch = new SpriteBatch();

        backgroundScrollSpeed = 2;
        gameStarted = false;

        imageHelper ih = new imageHelper();

        playerTexture = new Texture("images/0.png");
        //npcTexture = new Texture("animations/npclinks.png");
        feindTexture = ih.changeImgSize(130, 40, "images/zapp.png");

        font = new BitmapFont(Gdx.files.internal("fonts/coins_20.fnt"), false);

        camera.update();

        player = new Player(w / 2 - playerTexture.getWidth() / 2, 0, playerTexture);

        //npc = new NPCs((int) (w / 2 - npcTexture.getWidth() / 2), 10, npcTexture);

        // Initialize enemy list
        feindList = new ArrayList<>();

        // Initial spawn interval and timer
        spawnInterval = 2.0f; // Spawn an enemy every 2 seconds
        spawnTimer = 0;


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
                cList.add(coin);
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

    int coinshitt = 0;

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

        // Update the spawn timer and spawn new enemies if needed
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnNewEnemy();
            spawnTimer = 0; // Reset timer
        }

        // Update and render enemies
        batch.begin();
        for (Feind feind : feindList) {
            feind.update(delta);
            feind.draw(batch);

            if (player.collideRectangle(feind.getBoundary())) {
                System.out.println("Collision");
                Gdx.app.exit(); // Spiel schließen
            }
        }
        batch.end();

    }

    // Method to spawn new enemies
    private void spawnNewEnemy() {
        float initialX = Gdx.graphics.getWidth();
        float y = MathUtils.random(17, 250 - feindTexture.getHeight());
        Feind feind = new Feind(initialX, y, feindTexture);
        feindList.add(feind);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
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
    }
}
