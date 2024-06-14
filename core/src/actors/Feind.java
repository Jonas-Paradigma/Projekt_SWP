package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Feind {
    private float x, y;
    private Texture texture;
    private Rectangle boundary;
    private Polygon poly;
    private final float speed = 4; // fixierte Geschwindigkeit
    private float elapsedTime = 0.1f;
    TextureRegion currentFrame = new TextureRegion();

    public Feind(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.boundary = new Rectangle(x, y,  texture.getWidth(), texture.getHeight());


    }

    public void update(float delta) {
        elapsedTime += delta;
        x -= speed * delta * 60;
        boundary.setPosition(x, y);
        //currentFrame = zappyanimation.getKeyFrame(elapsedTime, true);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture,this.x, this.y);
    }

    public Rectangle getBoundary() {
        return boundary;
    }
}