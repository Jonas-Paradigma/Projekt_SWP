package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Rockets {
    private float x, y;
    private Texture texture; // Texture for the rocket
    private Rectangle boundary;
    private float elapsedTime = 0.1f;
    private final float speed = 5.5f; // Fixed speed

    private TextureAtlas raketenAtlas;
    private Animation<TextureRegion> raketenAnimation;
    private TextureRegion currentFrame;


    public Rockets(float x, float y, Texture image) {
        this.x = x;
        this.y = y;
        this.texture = image;
        this.boundary = new Rectangle(x, y, texture.getWidth()/7, texture.getHeight()/17);


        raketenAtlas = new TextureAtlas(Gdx.files.internal("animations/rakete.atlas"));
        Array<TextureAtlas.AtlasRegion> rocketFrames = raketenAtlas.findRegions("rakete");
        raketenAnimation = new Animation<>(0.09f, rocketFrames, Animation.PlayMode.LOOP);

        currentFrame = raketenAnimation.getKeyFrame(elapsedTime, true);
    }


    public void update(float delta) {
        x -= speed * delta * 60;
        boundary.setPosition(x, y);
        elapsedTime += delta;
        currentFrame = raketenAnimation.getKeyFrame(elapsedTime, true);

    }

    public void draw(SpriteBatch batch) {
        batch.draw(currentFrame, x, y, currentFrame.getRegionWidth()/3, currentFrame.getRegionHeight()/3);
    }

    public Rectangle getBoundary() {

        float hitboxScaleX = 0.4f; //Hitbox auf Breite ändern
        float hitboxScaleY = 0.6f; //Hitbox auf Höhe ändern

        float hitboxWidth = boundary.width * hitboxScaleX;
        float hitboxHeight = boundary.height * hitboxScaleY;

        float hitboxX = boundary.x + (boundary.width - hitboxWidth) / 2;
        float hitboxY = boundary.y + (boundary.height - hitboxHeight) / 2;

        return new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
    }



    public float getY() {
        return y;
    }
}
