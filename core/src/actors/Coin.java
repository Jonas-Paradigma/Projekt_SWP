package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Coin extends Spielobjekt {
    private int pixel;
    private Rectangle boundary;
    private ArrayList<Coin> fList;

    public Coin(int x, int y, Texture image, int pixel, ArrayList<Coin> fList) {
        super(x, y, image);
        this.pixel = pixel;
        this.fList = fList;
        boundary = new Rectangle();
        setRandomPosition();
        setBoundary();
    }





    public void update(float delta) {
        super.update(delta);
        setBoundary();
    }

    public boolean collideRectangle(Rectangle shape) {
        return Intersector.overlaps(boundary, shape);
    }

    public ArrayList<Coin> getfList() {
        return fList;
    }

    public void setfList(ArrayList<Coin> fList) {
        this.fList = fList;
    }

    public void setRandomPosition() {
        Random r = new Random();
        boolean collision = true;
        int ry = 0;
        int rx = 0;
        while (collision) {
            int minY = 200; // Änderung: Die minimale Y-Position auf 200 setzen
            int maxY = (int) (Gdx.graphics.getHeight() - getHeight()); // Maximale Y-Position basierend auf der Bildschirmhöhe und der Höhe der Münze berechnen
            int maxW = (int) (Gdx.graphics.getWidth() - getWidth());
            rx = r.nextInt(maxW + 1 - 0) + 0;
            ry = r.nextInt(2000 + 1 - minY) + minY;

            Rectangle rect = new Rectangle(rx, ry, getWidth(), getHeight());
            collision = false;
            for (Coin f : fList) {
                if (Intersector.overlaps(rect, f.getBoundary())) {
                    collision = true;
                    break;
                }
            }
        }
        setY(ry);
        setX(rx);
    }

    public Rectangle getBoundary() {
        return boundary;
    }

    private void setBoundary() {
        boundary.set(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
    }
}
