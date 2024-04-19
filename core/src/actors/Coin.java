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



    public void draw(Batch b) {
        super.draw(b);
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
        int rx = 0;
        int ry = 0;
        while (collision) {
            int minY = 0; // Der sichtbare Bereich beginnt am unteren Rand des Bildschirms
            int maxY = Gdx.graphics.getHeight() - (int) getHeight(); // Die maximale y-Position ist die Bildschirmhöhe abzüglich der Münzhöhe
            int maxX = Gdx.graphics.getWidth() - (int) getWidth(); // Die maximale x-Position ist die Bildschirmbreite abzüglich der Münzbreite
            rx = r.nextInt(maxX + 1 - 0) + 0;
            ry = r.nextInt(maxY + 1 - minY) + minY;
            Rectangle rect = new Rectangle(rx, ry, getWidth(), getHeight());
            collision = false;
            for (Coin f : fList) {
                if (Intersector.overlaps(rect, f.getBoundary())) {
                    collision = true;
                    break;
                }
            }
        }
        setX(rx);
        setY(ry);
        setBoundary();
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
