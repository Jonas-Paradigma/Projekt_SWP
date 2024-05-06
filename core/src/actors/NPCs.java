package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class NPCs extends Spielobjekt {
    private Rectangle boundary;
    private float x;
    private float y;
    private float width;
    private float height;

    private TextureAtlas walkingnpc;
    private Animation<TextureRegion> walkingAnimation;

    private Texture npcTexture;

    float w = Gdx.graphics.getWidth();
    private SpriteBatch batch;


    public NPCs(int x, int y, Texture image) {
        super(x, y, image);
        this.x = x;
        this.y = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.npcTexture = image;
        boundary = new Rectangle(x, y, width, height);
    }


}
