package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Coin extends Spielobjekt{
    private float speed;
    private Rectangle boundary;

    private TextureAtlas coinAtlas;
    private Animation<TextureRegion> coinAnimation;
    private float animationTime;
    private boolean playCoinAnimation;


    public Coin(int x, int y, Texture texture, float backgroundScrollSpeed) {
        super(x,y,texture);
        this.speed = backgroundScrollSpeed ;
        boundary = new Rectangle(x, y, texture.getWidth(), texture.getHeight());



        //coinAtlas = new TextureAtlas(Gdx.files.internal("animations/coinanimations.atlas"));
        //Array<TextureAtlas.AtlasRegion> coinFrames = coinAtlas.findRegions("coinanimation");
        //coinAnimation = new Animation<>(0.09f, coinFrames);

        //animationTime = 0;
        //playCoinAnimation = false;




    }


    public boolean collideRectangle(Rectangle shape) {
        if(Intersector.overlaps(this.boundary, shape)){
            return true;
        }else {
            return false;
        }
    }


    public void draw(Batch batch) {
        batch.draw(this.getImage(), this.getX(), this.getY());
    }

    public void moveWithBackground() {

        this.setX(this.getX()-speed);
        this.boundary.x = this.getX();
    }

    public Rectangle getBoundary() {
        return boundary;
    }

    public void setRandomPosition() {
        Random random = new Random();
        this.setX(random.nextInt((int) (2048 - this.getWidth()))); // Anpassen je nach Breite des Hintergrunds
        this.setY(random.nextInt((int) (Gdx.graphics.getHeight() - this.getHeight())));
    }

    public void act(float delta) {
        // Hier können zusätzliche Aktionen für die Münzen implementiert werden
        moveWithBackground();
    }


}