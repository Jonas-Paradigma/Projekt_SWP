package com.mygdx.game.Screens;

import actors.Coin;
import actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import helper.imageHelper;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;


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




    //Lassen
    public GameScreen2(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        initStage();
    }


    
    public void initStage() {

        //Musik
        music = Gdx.audio.newMusic(Gdx.files.internal("music/background.mp3"));
        music.setLooping(true);
        music.play();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        screenHeight = Gdx.graphics.getHeight();
        // Hintergrund laden
        background = new Texture("images/Background_new.png");
        camera = new OrthographicCamera(w, h);
        viewport = new FitViewport(w, h, camera);
        batch = new SpriteBatch();

        backgroundScrollSpeed = 2;
        gameStarted = false;



        playerTexture = new Texture("images/0.png");

        font = new BitmapFont(Gdx.files.internal("fonts/coins_20.fnt"),  false);
        //font = new BitmapFont(Gdx.files.internal("fonts/calibri_green_30.fnt"), Gdx.files.internal("fonts/calibri_green_30.png"), false);

        // Kamera-Update
        camera.update();

        player = new Player(w / 2 - playerTexture.getWidth() / 2, 0,  playerTexture);
        // Münzen erstellen
        cList = new ArrayList<>();
        imageHelper ih = new imageHelper();
        for (int i = 0; i < 7; i++) {
            int randomX = (int) (Math.random() * Gdx.graphics.getWidth());

            // Generiere eine zufällige y-Position zwischen 200 und 50
            int randomY = (int) (Math.random() * (250 - 50 + 1)) + 50;

            cList.add(new Coin(randomX, randomY, ih.changeImgSize(16, 16, "images/coin.png"), backgroundScrollSpeed));

            assetManager = new AssetManager();
            assetManager.load("Sounds/Coin.mp3", Sound.class);
            assetManager.finishLoading();

            soundEffect = assetManager.get("Sounds/Coin.mp3", Sound.class);
        }

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

        // Hintergrund zeichnen
        for (int i = 0; i < 2; i++) {
            batch.draw(background, i * background.getWidth() - backgroundOffsetX, 0);
        }

        batch.end(); // Ende des Zeichnens des Hintergrunds

        //Münzen zeichnen und bewegen
        batch.begin(); // Batch wieder öffnen für das Zeichnen der Münzen
        for (Coin coin : cList) {
            coin.moveWithBackground();
            coin.draw(batch);
            font.draw(batch, "Coins: "+coinshitt,320,275);
            int distance = player.getDistanceTraveled();
            font.draw(batch, "Distanz: " + distance + "m", 320, 250);


            if (player.collideRectangle(coin.getBoundary())){
                //System.out.println("collision");
                coin.setPosition(Gdx.graphics.getWidth(), coin.getY());
                coinshitt++;
                soundEffect.play();
            }
            // Überprüfen, ob die Münze außerhalb des sichtbaren Bereichs ist
            if (coin.getX() + coin.getWidth() < 0) {
                // Wenn ja, setze die Münze auf die rechte Seite des Bildschirms
                coin.setPosition(Gdx.graphics.getWidth(), coin.getY());
            }
        }


        // Spieler zeichnen
        batch.draw(player.getCurrentFrame(), player.getX(), player.getY());
        player.update(Gdx.graphics.getDeltaTime());

        elapsedTime += Gdx.graphics.getDeltaTime();

        batch.end(); // Ende des Zeichnens der Münzen und des Spielers
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