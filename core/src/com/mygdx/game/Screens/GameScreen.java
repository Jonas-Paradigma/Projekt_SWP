package com.mygdx.game.Screens;


import actors.Feind;
import actors.GameActor;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.ScreenGame;


/**
 * Created by julienvillegas on 17/01/2017.
 */
public class GameScreen implements Screen {

    private Stage stage;
    private Game game;

    GameActor gameActor;
    GameActor background;
    Feind feind;
    Boolean gameEnd = false;

    public GameScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        initStage();


    }


    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!gameEnd){
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) gameActor.move(0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) gameActor.move(1);
            //if (Gdx.input.isKeyPressed(Input.Keys.UP)) gameActor.setY(gameActor.getY()+1);
            //if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) gameActor.setY(gameActor.getY()-1);
            stage.act();
            feind.act(delta);



            if (gameActor.collideCircle(feind.boundary) && !gameEnd) {

                System.out.println("Kollision");
                gameEnd = true;
                this.showButtons();
            }

        }
        stage.draw();
        // Trennung von Shaperender und Batchrender
        //gameActor.drawShape(delta);
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
    }

    public void showButtons() {

        stage.clear();

        Label title = new Label("You died!!!", ScreenGame.gameSkin,"big-black");
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight()*2/3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        TextButton backButton = new TextButton("Back",ScreenGame.gameSkin);
        backButton.setWidth(Gdx.graphics.getWidth()/2);
        backButton.setPosition(Gdx.graphics.getWidth()/2-backButton.getWidth()/2,Gdx.graphics.getHeight()/2-backButton.getHeight()/2);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(backButton);

        TextButton newButton = new TextButton("New Game",ScreenGame.gameSkin);
        newButton.setWidth(Gdx.graphics.getWidth()/1.5f);
        newButton.setPosition(Gdx.graphics.getWidth()/2-backButton.getWidth()/2,Gdx.graphics.getHeight()/4-backButton.getHeight()/2);
        newButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                stage.clear();
                initStage();
                gameEnd =false;
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(newButton);
    }

    private void initStage(){

        //gameActor = new GameActor(0,0,"images/sma.png");
        //background = new GameActor(0,0,"images/background_800.jpg");
        //feind = new Feind(500,300,"images/wi_rund_20.png",Gdx.graphics.getHeight());

        //stage.addActor(background);
        //stage.addActor(gameActor);
        //stage.addActor(feind);
    }
}
