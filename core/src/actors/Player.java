package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    private Rectangle boundary;
    private float x;
    private float y;
    private float width;
    private float height;

    public Player(float x, float y, float width, float height, Texture image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        boundary = new Rectangle(x, y, width, height);
    }

    public void update(float delta) {
        // Hier könnten Sie die Position des Spielers aktualisieren
    }

    public boolean collidesWith(Rectangle shape) {
        return Intersector.overlaps(this.boundary, shape);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setX(float x) {
        this.x = x;
        updateBoundary();
    }

    public void setY(float y) {
        this.y = y;
        updateBoundary();
    }

    public void setWidth(float width) {
        this.width = width;
        updateBoundary();
    }

    public void setHeight(float height) {
        this.height = height;
        updateBoundary();
    }

    private void updateBoundary() {
        boundary.set(x, y, width, height);
    }
}
