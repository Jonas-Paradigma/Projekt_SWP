package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import helper.imageHelper;

import java.util.ArrayList;
import java.util.Random;

public class CoinList {
    private ArrayList<Coin> coinlist;
    private int direction;
    private int anzahlCoins;
    private float backgroundScrollSpeed;
    private int coinWidth = 16;
    private int coinHeight = 16;

    private int coinsPerRow = 5;
    private int numRows = 1;

    private int ySpacing = 10;

    private int startX = 1750;
    private int startY = 50;


    private Player player;
    private int coinshitt = 0;
    private Sound soundEffect;

    public CoinList(int anzCoins, int direction) {
        this.coinlist = new ArrayList<>();
        this.direction = direction;
        this.anzahlCoins = anzCoins;
        generateCoins();
    }

    public ArrayList<Coin> getCoins() {
        return coinlist;
    }

    public void generateCoins() {
        backgroundScrollSpeed += Gdx.graphics.getDeltaTime();
        float backgroundOffsetX = backgroundScrollSpeed * 200;

        imageHelper ih = new imageHelper();
        Random r = new Random();
        // ZUfalls höhe y
        int y = startY + (r.nextInt(5) + 0)*(coinHeight + ySpacing);
        for (int col = 0; col < coinsPerRow; col++) {
            coinlist.add(new Coin(startX+(25*col), y, ih.changeImgSize(16, 16, "images/coin.png"), 4));
        }

    }

    public void render(SpriteBatch batch){
        // Render und aktualisiere die Münzen
        boolean start = false;
        for (Coin coin : coinlist) {
            coin.moveWithBackground();
            coin.draw(batch);
            System.out.println("X-wert" + coin.getX());
            if ((coin.getX() + coin.getWidth()) < 200) {
                //
                start = true;
            }
        }
        if (start || coinlist.size() < 1) {
            coinlist.clear();
            generateCoins();
        }
    }
    public void collide(Player player, Sound soundEffect, Runnable onCoinCollected) {
        this.player = player;
        this.soundEffect = soundEffect;

        for (int i= coinlist.size()-1; i>=0;i--) {
            if (player.collideRectangle(coinlist.get(i).getBoundary())) {
                //coin.setPosition(Gdx.graphics.getWidth(), coin.getY());
                coinlist.remove(i);
                onCoinCollected.run();
                soundEffect.play();
            }
        }
    }


}
