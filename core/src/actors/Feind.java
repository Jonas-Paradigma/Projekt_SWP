package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Feind  extends Spielobjekt {

    public Rectangle boundary;
    private double geschwindigkeit = 2;
    private double beschleunigung = 0.001;

    private int screenX = 0;
    public Feind(int x, int y, String image, int screenX ) {
        super(x, y, new Texture(Gdx.files.internal(image)));
        boundary = new Rectangle();
        this.setBoundary();
        this.screenX = screenX;
    }

    public double getGeschwindigkeit() {
        return geschwindigkeit;
    }

    public void setGeschwindigkeit(double geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }

    public double getBeschleunigung() {

        return beschleunigung;
    }

    public void setBeschleunigung(double beschleunigung) {
        this.beschleunigung = beschleunigung;
    }

    public void update(float delta) {
        geschwindigkeit += beschleunigung;
        this.setY((int)(this.getY()- geschwindigkeit));

        if (this.getY() < 0) {
            this.setY(Gdx.graphics.getHeight() + this.getHeight());
        }
        this.setBoundary();
    }

    private void setBoundary() {
        boundary.set(this.getX(),this.getY(),this.getWidth(), this.getHeight());
    }

    @Override
    public void draw(Batch b, float parentAlpha) {
        this.getImage().draw(b);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        this.update(dt);
    }
}
