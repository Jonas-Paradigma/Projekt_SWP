package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Feind {
    private float x, y;
    private Texture texture;
    private Rectangle boundary;
    private final float speed = 4; // Fixed speed

    public Feind(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.boundary = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
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
