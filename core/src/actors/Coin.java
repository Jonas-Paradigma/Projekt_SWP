package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Coin {
    private Texture texture;
    private Vector2 position;

    public Coin(int x, int y, Texture texture) {
        this.texture = texture;
        this.position = new Vector2(x, y);
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void moveWithBackground(float backgroundScrollSpeed) {
        position.x -= backgroundScrollSpeed;
    }

    public Rectangle getBoundary() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void setRandomPosition() {
        Random random = new Random();
        position.x = random.nextInt((int) (2048 - texture.getWidth())); // Anpassen je nach Breite des Hintergrunds
        position.y = random.nextInt((int) (Gdx.graphics.getHeight() - texture.getHeight()));
    }

    public void act(float delta) {
        // Hier können zusätzliche Aktionen für die Münzen implementiert werden
    }

    public void dispose() {
        texture.dispose();
    }
}
