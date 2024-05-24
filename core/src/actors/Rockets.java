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
    private Texture texture;
    private Rectangle boundary;
    private final float speed = 4; // Fixed speed

    private TextureAtlas Raketenatlas;
    private Animation<TextureRegion> Raketenanimation;

    public Rockets(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.boundary = new Rectangle(x, y, texture.getWidth(), texture.getHeight());





        // Atlas für die Raktenanimation
        Raketenatlas = new TextureAtlas(Gdx.files.internal("animations/laufen.atlas"));
        Array<TextureAtlas.AtlasRegion> walkingFrames = Raketenatlas.findRegions("mainwalk");
        Raketenanimation = new Animation<>(0.09f, walkingFrames, Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        x -= speed * delta * 60; // Multiply by 60 to simulate pixels per second
        boundary.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public Rectangle getBoundary() {
        return boundary;
    }
}
