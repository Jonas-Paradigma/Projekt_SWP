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
import helper.imageHelper;

public class PauseScreen implements Screen {

    private Stage stage;
    private Game game;
    private Texture backgroundImage;
    private SpriteBatch batch;
    private GameScreen2 previousScreen;

    public PauseScreen(Game aGame, GameScreen2 previousScreen) {
        game = aGame;
        this.previousScreen = previousScreen;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();

        imageHelper ih = new imageHelper();

        backgroundImage = new Texture("images/pausescreen.png");


        //resume button
        Texture resumeTexture = ih.changeImgSize(255, 100, "images/back.png");
        ImageButton.ImageButtonStyle resumeStyle = new ImageButton.ImageButtonStyle();
        resumeStyle.imageUp = new TextureRegionDrawable(new TextureRegion(resumeTexture));
        ImageButton resumeButton = new ImageButton(resumeStyle);
        resumeButton.setPosition(600, 220);
        resumeButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                previousScreen.resumeGame();
                game.setScreen(previousScreen);
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(resumeButton);

        //Menu Button
        Texture menuTexture = ih.changeImgSize(255, 100, "images/menu.png"); // Größe des Bildes ändern
        ImageButton.ImageButtonStyle menuStyle = new ImageButton.ImageButtonStyle();
        menuStyle.imageUp = new TextureRegionDrawable(new TextureRegion(menuTexture));
        ImageButton menuButton = new ImageButton(menuStyle);
        menuButton.setPosition(600, 90);
        menuButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game)); // Wechsle zu OptionScreen, wenn der Button losgelassen wird
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(menuButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        batch.dispose();
    }
}
