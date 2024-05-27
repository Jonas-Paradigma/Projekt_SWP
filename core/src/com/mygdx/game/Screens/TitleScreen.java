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

public class TitleScreen implements Screen {

    private Stage stage;
    private Game game;
    private Texture backgroundImage;
    private SpriteBatch batch;
    private int maxwidth = 1000;

    public TitleScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch(); // Erstellen einer Instanz von SpriteBatch

        // Hintergrundbild laden
        backgroundImage = new Texture("images/start.png");

        imageHelper ih = new imageHelper();

        //Play Button
        Texture startTexture = ih.changeImgSize(255, 100, "images/play.png"); // Größe des Bildes ändern
        ImageButton.ImageButtonStyle startStyle = new ImageButton.ImageButtonStyle();
        startStyle.imageUp = new TextureRegionDrawable(new TextureRegion(startTexture));
        ImageButton startButton = new ImageButton(startStyle);
        startButton.setPosition(460, 180);
        startButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen2(game,true)); // Wechsle zu GameScreen2, wenn der Button losgelassen wird
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(startButton);

        //Options Button
        Texture optionTexture = ih.changeImgSize(255, 100, "images/options.png"); // Größe des Bildes ändern
        ImageButton.ImageButtonStyle optionStyle = new ImageButton.ImageButtonStyle();
        optionStyle.imageUp = new TextureRegionDrawable(new TextureRegion(optionTexture));
        ImageButton optionButton = new ImageButton(optionStyle);
        optionButton.setPosition(780, 180);
        optionButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new OptionScreen(game)); // Wechsle zu OptionScreen, wenn der Button losgelassen wird
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(optionButton);

        //Exit Button
        Texture exitTexture = ih.changeImgSize(255, 100, "images/exit.png"); // Größe des Bildes ändern
        ImageButton.ImageButtonStyle exitStyle = new ImageButton.ImageButtonStyle();
        exitStyle.imageUp = new TextureRegionDrawable(new TextureRegion(exitTexture));
        ImageButton exitButton = new ImageButton(exitStyle);
        exitButton.setPosition(610, 60);
        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit(); // Spiel schließen, wenn der Button losgelassen wird
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(exitButton);
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
        // Entferne die Buttons aus der Bühne und setze den InputProcessor zurück
        Gdx.input.setInputProcessor(null);
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundImage.dispose();
        batch.dispose(); // SpriteBatch aufräumen
    }
}
