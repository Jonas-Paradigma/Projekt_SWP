package actors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;



public class Game implements KeyListener {
    JFrame frame;
    JLabel player;

    public boolean grounded = false;
    public int velocity = 0;
    public int finalY = 0;
    public boolean spaceHeld = false;


    Action spaceAction;

    Game() {

        frame = new JFrame("Nicholas Seow-Xi Crouse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(null);

        frame.setFocusable(true);
        frame.addKeyListener(this);

        player = new JLabel();
        player.setBackground(Color.red);
        player.setBounds(10, 1800, 50, 50);
        player.setOpaque(true);

        frame.add(player);
        frame.setVisible(true);

        Timer flightTime = new Timer();
        TimerTask flight = new TimerTask() {

            public void run() {
                if(velocity > -5) {
                    velocity -= 1;
                }
                else{
                    velocity = -5;
                }
                if (finalY < -61) {
                    finalY = finalY + velocity;
                }
                else{
                    finalY = -61;
                }
                player.setLocation(player.getX(), player.getY() + finalY);
                if (player.getY() <= 0){
                    cancel();
                    velocity = 0;
                    finalY = 0;
                    player.setLocation(player.getX(), 0);

                }
            }
        };


        Timer gravityTime = new Timer();
        TimerTask gravity = new TimerTask() {


            //creates a timer run method that simulates the falling gravity when not grounded
            public void run() {
                if(velocity < 5){
                    velocity += 1;
                }
                else{
                    velocity = 5;
                }
                //creates the variable the tells where the player is located
                if (finalY < 61) {
                    finalY = finalY + velocity;
                }
                else{
                    finalY = 61;
                }
                player.setLocation(player.getX(), player.getY() + finalY);
                if (player.getY() >= 1000){
                    cancel();
                    velocity = 0;
                    finalY = 0;
                    player.setLocation(player.getX(), 990);
                }

            }
        };

        if (spaceHeld == false ) {
            gravityTime.scheduleAtFixedRate(gravity, 0, 33);
        }
        if (spaceHeld == true ) {
            flightTime.scheduleAtFixedRate(flight, 0 ,33);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            spaceHeld = true;
//debug
            System.out.println(spaceHeld);
        }
    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            spaceHeld = false;
//debug
            System.out.println(spaceHeld);
        }
    }
}