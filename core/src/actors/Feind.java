package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Feind {
    private float x, y;
    private Texture texture;
    private Rectangle boundary;
    private final float speed = 4; // fixed speed

    private float elapsedTime = 0.1f;

    public Feind(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.boundary = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float delta) {
        elapsedTime += delta;
        x -= speed * delta * 60;
        boundary.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, this.x, this.y);
    }

    public Rectangle getBoundary() {
        float hitboxScaleX = 0.6f; //Hitbox auf Breite ändern
        float hitboxScaleY = 0.6f; //Hitbox auf Höhe ändern

        float hitboxWidth = boundary.width * hitboxScaleX;
        float hitboxHeight = boundary.height * hitboxScaleY;

        float hitboxX = boundary.x + (boundary.width - hitboxWidth) / 2;
        float hitboxY = boundary.y + (boundary.height - hitboxHeight) / 2;

        return new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);

    }
}
