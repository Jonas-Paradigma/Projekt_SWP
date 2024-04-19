package actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Spielobjekt extends Actor {
    protected Sprite image;

    public Spielobjekt(int x, int y, Texture image) {
        super();
        this.setX(x);
        this.setY(y);
        this.image = new Sprite(image);
        this.setWidth(image.getWidth());
        this.setHeight(image.getHeight());
        this.image.setX(x);
        this.image.setY(y);
    }


    @Override
    public void act(float dt) {
        super.act(dt);
        this.image.setX(this.getX());
        this.image.setY(this.getY());
    }

    public Sprite getImage() {
        return this.image;
    }

    protected void update(float delta) {
    }

    public void draw(Batch b) {
        this.image.draw(b);
    }
}
