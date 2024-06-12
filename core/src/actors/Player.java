package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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

    private float hitboxOffsetX = 30;
    private float hitboxOffsetY = 10;
    private float hitboxWidth = 20;
    private float hitboxHeight = 20;

    private Sound playerrun;
    private Sound playerfly;
    private long lastPlayTime = 0;


    public Player(float x, float y, Texture image) {
        this.x = x;
        this.y = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.playerTexture = image;
        boundary = new Rectangle(x, y, (float) (width/7), height/5);

        // Atlas für Laufanimation
        walkingAtlas = new TextureAtlas(Gdx.files.internal("animations/laufen.atlas"));
        Array<TextureAtlas.AtlasRegion> walkingFrames = walkingAtlas.findRegions("mainwalk");
        walkingAnimation = new Animation<>(0.09f, walkingFrames, Animation.PlayMode.LOOP);

        // Atlas für Fluganimation
        flyingAtlas = new TextureAtlas(Gdx.files.internal("animations/mainfly.atlas"));
        Array<TextureAtlas.AtlasRegion> flyingFrames = flyingAtlas.findRegions("mainfly");
        flyingAnimation = new Animation<>(0.09f, flyingFrames, Animation.PlayMode.LOOP);

        // Atlas für Stand-Animation
        standAtlas = new TextureAtlas(Gdx.files.internal("animations/maindown.atlas"));
        Array<TextureAtlas.AtlasRegion> standFrames = standAtlas.findRegions("maindown");
        standAnimation = new Animation<>(0.09f, standFrames, Animation.PlayMode.LOOP);

        playerTexture = new Texture("images/0.png");
        // Startposition am Boden
        isPlayerFlying = false;
        playerVerticalVelocity = 0;

        playerrun = Gdx.audio.newSound(Gdx.files.internal("Sounds/playerrun.mp3"));
        playerfly = Gdx.audio.newSound(Gdx.files.internal("sounds/jetpack_firelp.wav"));
    }


    public boolean collideRectangle(Rectangle bshape) {
        if (Intersector.overlaps(this.boundary, bshape)) {
            return true;
        } else {
            return false;
        }
    }


    public void update(float delta) {
        elapsedTime += delta;

        this.y += playerVerticalVelocity * Gdx.graphics.getDeltaTime();

        // Spieleranimationen
        if (this.y <= 17) {
            isPlayerFlying = false;
            currentFrame = walkingAnimation.getKeyFrame(elapsedTime, true);

            // Aktuelle Zeit in Millisekunden abrufen
            long currentTime = System.currentTimeMillis();

            // Überprüfen, ob seit dem letzten Abspielen des Sounds mindestens 100 Millisekunden vergangen sind
            if (currentTime - lastPlayTime >= 200) {
                playerrun.play();
                lastPlayTime = currentTime; // Timer aktualisieren
            }
        }

        //Zurückgelegte Distanz
        distanceTraveled += Math.abs(9) * Gdx.graphics.getDeltaTime();


        if (isPlayerFlying && !Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            currentFrame = standAnimation.getKeyFrame(elapsedTime, true);
            playerfly.stop();
        } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            currentFrame = flyingAnimation.getKeyFrame(elapsedTime, true);
            isPlayerFlying = true;
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastPlayTime >= 150) {
                playerfly.play();
                lastPlayTime = currentTime; // Timer aktualisieren
            }
        } else {
            currentFrame = walkingAnimation.getKeyFrame(elapsedTime, true);
        }

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

    public Rectangle getCurrentFrameBoundary() {
        TextureRegion currentFrame = getCurrentFrame();
        return new Rectangle(getX() + hitboxOffsetX, getY() + hitboxOffsetY, hitboxWidth, hitboxHeight);
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
