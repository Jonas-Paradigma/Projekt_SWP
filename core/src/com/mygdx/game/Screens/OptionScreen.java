package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.ScreenGame;
import helper.imageHelper;

public class OptionScreen implements Screen {

    private Stage stage;
    private Game game;
    private Texture backgroundImage;
    private SpriteBatch batch;

    public OptionScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch(); // Erstellen einer Instanz von SpriteBatch

        imageHelper ih = new imageHelper();

        // Hintergrundbild laden
        backgroundImage = new Texture("images/optionsscreen.png");

        // Music On Button
        Texture musicOnTexture = ih.changeImgSize(255, 100, "images/music on.png"); // Größe des Bildes ändern
        ImageButton.ImageButtonStyle musicOnStyle = new ImageButton.ImageButtonStyle();
        musicOnStyle.imageUp = new TextureRegionDrawable(new TextureRegion(musicOnTexture));
        ImageButton musicOnButton = new ImageButton(musicOnStyle);
        musicOnButton.setPosition(460, 180);
        musicOnButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Musik aktivieren
                ScreenGame.enableSound = true;
                // Musik im GameScreen2 aktivieren
                if (game.getScreen() instanceof GameScreen2) {
                    ((GameScreen2) game.getScreen()).enableSound(true);
                }
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(musicOnButton);


        // Music Off Button
        Texture musicOffTexture = ih.changeImgSize(255, 100, "images/music off.png"); // Größe des Bildes ändern
        ImageButton.ImageButtonStyle musicOffStyle = new ImageButton.ImageButtonStyle();
        musicOffStyle.imageUp = new TextureRegionDrawable(new TextureRegion(musicOffTexture));
        ImageButton musicOffButton = new ImageButton(musicOffStyle);
        musicOffButton.setPosition(780, 180);
        musicOffButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Musik deaktivieren
                ScreenGame.enableSound = false;
                // Musik im GameScreen2 deaktivieren
                if (game.getScreen() instanceof GameScreen2) {
                    ((GameScreen2) game.getScreen()).enableSound(false);
                }
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(musicOffButton);


        // Back Button
        Texture backTexture = ih.changeImgSize(255, 100, "images/back.png"); // Größe des Bildes ändern
        ImageButton.ImageButtonStyle backStyle = new ImageButton.ImageButtonStyle();
        backStyle.imageUp = new TextureRegionDrawable(new TextureRegion(backTexture));
        ImageButton backButton = new ImageButton(backStyle);
        backButton.setPosition(610, 60);
        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(backButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Hintergrundbild zeichnen
        batch.begin();
        batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        stage.dispose();
        backgroundImage.dispose();
        batch.dispose(); // SpriteBatch aufräumen
    }
}
