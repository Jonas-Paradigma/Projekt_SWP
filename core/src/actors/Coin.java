package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Coin {
    private Texture texture;
    private Vector2 position;
    private float speed;
    private Rectangle boundary;

    public Coin(int x, int y, Texture texture, float backgroundScrollSpeed) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.speed = backgroundScrollSpeed * 2;
        boundary = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }


    public boolean collideRectangle(Rectangle shape) {
        if(Intersector.overlaps(this.boundary, shape)){
            return true;
        }else {
            return false;
        }
    }


    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void moveWithBackground() {

        System.out.println(speed);
        position.x -= speed;
        this.boundary.x = position.x;
    }

    public Rectangle getBoundary() {
        return boundary;
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


    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return texture.getWidth();
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }


}