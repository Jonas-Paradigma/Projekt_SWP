package helper;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class imageHelper {
    public Texture changeImgSize(int newWidth, int newHeight, String filename) {
        Pixmap pixmap200 = new Pixmap(Gdx.files.internal(filename));
        Pixmap pixmap100 = new Pixmap(newWidth, newHeight, pixmap200.getFormat());
        pixmap100.drawPixmap(pixmap200,
                0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
                0, 0, pixmap100.getWidth(), pixmap100.getHeight()
        );
        Texture t = new Texture(pixmap100);
        pixmap200.dispose();
        pixmap100.dispose();
        return t;
    }
}