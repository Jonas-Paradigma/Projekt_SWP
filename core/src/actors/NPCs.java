package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class NPCs extends Spielobjekt {
    private Rectangle boundary;
    private float x;
    private float y;
    private float width;
    private float height;

    private TextureAtlas walkingnpc;
    private Animation<TextureRegion> walkingAnimation;

    public Texture npcTexture;
    private float elapsedTime = 0.1f;

    Texture randomTex = new Texture("animations/npclinks.png");

    TextureRegion currentFrame = new TextureRegion(randomTex);

    float w = Gdx.graphics.getWidth();
    private SpriteBatch batch;

    private float npcvelocity;
    private boolean isNpcFlying;

    public NPCs(int x, int y, Texture image) {
        super(x, y, image);
        this.x = x;
        this.y = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.npcTexture = image;
        boundary = new Rectangle(x, y, width, height);

        walkingnpc = new TextureAtlas(Gdx.files.internal("animations/npclinks.atlas"));
        Array<TextureAtlas.AtlasRegion> walkingFrames = walkingnpc.findRegions("npclinks");
        walkingAnimation = new Animation<>(0.09f, walkingFrames, Animation.PlayMode.LOOP);



        isNpcFlying = false;
        npcvelocity = 0;
    }




    public void update(float delta) {
        // Zeit aktualisieren
        elapsedTime += delta;

        // Hier könnten Sie die Position des Spielers aktualisieren
        this.y += npcvelocity * Gdx.graphics.getDeltaTime();

        // Spieleranimationen aktualisieren
        if (this.y <= 10) {
            isNpcFlying = false;
            currentFrame = walkingAnimation.getKeyFrame(elapsedTime, true);
            //System.out.println("Boden");
        }


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
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }



    public void act(float delta) {

    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }




}