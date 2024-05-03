package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player {

    private Rectangle boundary;
    private float x;
    private float y;
    private float width;
    private float height;

    private TextureAtlas walkingAtlas;
    private TextureAtlas flyingAtlas;
    private TextureAtlas standAtlas;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> flyingAnimation;
    private Animation<TextureRegion> standAnimation;

    private Texture playerTexture;
    private boolean isPlayerFlying;

    private float playerVerticalVelocity; // Geschwindigkeit des Spielers in vertikaler Richtung

    private float elapsedTime = 0.1f;
    float w = Gdx.graphics.getWidth();
    private SpriteBatch batch;

    private float distanceTraveled = 0;

    Texture randomTex = new Texture("animations/laufen.png");

    TextureRegion currentFrame = new TextureRegion(randomTex);


    public Player(float x, float y, Texture image) {
        this.x = x;
        this.y = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.playerTexture = image;
        boundary = new Rectangle(x, y, width, height);

        // Atlas für Laufanimation des Spielers laden
        walkingAtlas = new TextureAtlas(Gdx.files.internal("animations/laufen.atlas"));
        Array<TextureAtlas.AtlasRegion> walkingFrames = walkingAtlas.findRegions("mainwalk");
        walkingAnimation = new Animation<>(0.09f, walkingFrames, Animation.PlayMode.LOOP);

        // Atlas für Fluganimation des Spielers laden
        flyingAtlas = new TextureAtlas(Gdx.files.internal("animations/mainfly.atlas"));
        Array<TextureAtlas.AtlasRegion> flyingFrames = flyingAtlas.findRegions("mainfly");
        flyingAnimation = new Animation<>(0.09f, flyingFrames, Animation.PlayMode.LOOP);

        // Atlas für Stand-Animation des Spielers laden
        standAtlas = new TextureAtlas(Gdx.files.internal("animations/maindown.atlas"));
        Array<TextureAtlas.AtlasRegion> standFrames = standAtlas.findRegions("maindown");
        standAnimation = new Animation<>(0.09f, standFrames, Animation.PlayMode.LOOP);

        playerTexture = new Texture("images/0.png");
        // Startposition am Boden
        isPlayerFlying = false;
        playerVerticalVelocity = 0; // Initialgeschwindigkeit des Spielers in vertikaler Richtung


    }

    public boolean collideRectangle(Rectangle bshape) {
        if (Intersector.overlaps(this.boundary, bshape)) {
            return true;
        } else {
            return false;
        }
    }


    public void update(float delta) {
        // Zeit aktualisieren
        elapsedTime += delta;

        // Hier könnten Sie die Position des Spielers aktualisieren
        this.y += playerVerticalVelocity * Gdx.graphics.getDeltaTime();

        // Spieleranimationen aktualisieren
        if (this.y <= 17) {
            isPlayerFlying = false;
            currentFrame = walkingAnimation.getKeyFrame(elapsedTime, true);
            //System.out.println("Boden");
        }

        //Zurückgelegte Distanz
        distanceTraveled += Math.abs(9) * Gdx.graphics.getDeltaTime();


        if (isPlayerFlying && !Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            currentFrame = standAnimation.getKeyFrame(elapsedTime, true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            currentFrame = flyingAnimation.getKeyFrame(elapsedTime, true);
            isPlayerFlying = true;
        } else {
            currentFrame = walkingAnimation.getKeyFrame(elapsedTime, true);
        }

        // Vertikale Grenzen überprüfen und Spielerbewegung aktualisieren
        if (this.y >= 240) {
            this.y = 240;
            playerVerticalVelocity = 0;
        }
        if (this.y <= 17) {
            this.y = 17;
            playerVerticalVelocity = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            playerVerticalVelocity += 500 * Gdx.graphics.getDeltaTime(); // Beschleunigen nach oben
        } else {
            playerVerticalVelocity -= 500 * Gdx.graphics.getDeltaTime(); // Beschleunigen nach unten
        }
        playerVerticalVelocity = Math.min(playerVerticalVelocity, 300);
        playerVerticalVelocity = Math.max(playerVerticalVelocity, -300);

        updateBoundary();
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


    public void act(float delta) {

    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public int getDistanceTraveled() {
        return (int) distanceTraveled;
    }

}
