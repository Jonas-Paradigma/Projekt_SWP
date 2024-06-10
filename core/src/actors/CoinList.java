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
        generateCoins();  // Aufruf der Münzengenerierung beim Erstellen der Instanz
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
            coinlist.add(new Coin(startX+(28*col), y, ih.changeImgSize(16, 16, "images/coin.png"), 4));
        }
/*
        for (int row = 0; row < numRows; row++) {
            int y = startY + row * (coinHeight + ySpacing);
            int x = startX;

            for (int col = 0; col < coinsPerRow; col++) {
                Coin coin = new Coin(x, y, ih.changeImgSize(16, 16, "images/coin.png"), backgroundScrollSpeed);
                // Überprüfung auf Überlappung mit vorhandenen Münzen
                boolean overlapping = false;
                for (Coin existingCoin : coinlist) {
                    if (Intersector.overlaps(coin.getBoundary(), existingCoin.getBoundary())) {
                        overlapping = true;
                        break;
                    }
                }
                // Münze nur hinzufügen, wenn keine Überlappung besteht
                if (!overlapping) {
                    coinlist.add(coin);
                }
                x += coinWidth + xSpacing;
            }

            startY += coinHeight + ySpacing + rowXSpacing;
        }

 */
    }

    public void render(SpriteBatch batch){
        // Render und aktualisiere die Münzen
        boolean start = false;
        for (Coin coin : coinlist) {
            coin.moveWithBackground();
            coin.draw(batch);
            System.out.println("X-wert" + coin.getX());
            if (coin.getX() + coin.getWidth() < 0) {
                //
                start = true;
            }
        }
        if (start) {
            coinlist.clear();
            generateCoins();
        }
    }
    public void collide(Player player, Sound soundEffect, Runnable onCoinCollected) {
        this.player = player;
        this.soundEffect = soundEffect;

        for (Coin coin : coinlist) {
            if (player.collideRectangle(coin.getBoundary())) {
                coin.setPosition(Gdx.graphics.getWidth(), coin.getY());
                onCoinCollected.run(); // Callback, um die Anzahl der gesammelten Münzen zu erhöhen
                soundEffect.play();
            }
        }
    }


}
