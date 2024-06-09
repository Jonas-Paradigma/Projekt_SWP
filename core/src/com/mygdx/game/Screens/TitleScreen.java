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

public class TitleScreen implements Screen {

    private Stage stage;
    private Game game;
    private Texture backgroundImage;
    private SpriteBatch batch;

    public TitleScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();

        backgroundImage = new Texture("images/start.png");

        imageHelper ih = new imageHelper();

        //Play Button
        Texture startTexture = ih.changeImgSize(255, 100, "images/play.png");
        ImageButton.ImageButtonStyle startStyle = new ImageButton.ImageButtonStyle();
        startStyle.imageUp = new TextureRegionDrawable(new TextureRegion(startTexture));
        ImageButton startButton = new ImageButton(startStyle);
        startButton.setPosition(460, 180);
        startButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ((ScreenGame) game).stopMenuMusic();
                ((ScreenGame) game).playBackgroundMusic();
                game.setScreen(new GameScreen2(game, ScreenGame.enableSound));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(startButton);

        //Options Button
        Texture optionTexture = ih.changeImgSize(255, 100, "images/options.png");
        ImageButton.ImageButtonStyle optionStyle = new ImageButton.ImageButtonStyle();
        optionStyle.imageUp = new TextureRegionDrawable(new TextureRegion(optionTexture));
        ImageButton optionButton = new ImageButton(optionStyle);
        optionButton.setPosition(780, 180);
        optionButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new OptionScreen(game));
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(optionButton);

        // Exit Button
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
        ((ScreenGame) game).playMenuMusic();
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
        Gdx.input.setInputProcessor(null); // Eingabeprozessor entfernen
        stage.clear(); // Bühne aufräumen
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundImage.dispose();
        batch.dispose();
    }
}
