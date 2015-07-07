package main.java.MyGame;

import main.java.MyGame.Game.STATE;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

class Menu extends MouseAdapter{

    private Handler handler;
    private static Random r = new SecureRandom();
    private HUD hud;

    private boolean muted = false;

    private boolean playing = false;
    private Clip clip = null;

    public Menu(Handler handler, HUD hud){
        this.handler = handler;
        this.hud = hud;
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        // Mute button 10, 400, 32, 432
        if(mouseOver(mx, my,10, 400, 32, 432)){
            muted = !muted;
            if(muted){
                mute();
            }
        }
        //play button
        if(mouseOver(mx, my,210,150,200,64) && Game.gameState == STATE.Menu){
            startGame();
        }
        //Help Button
        if(mouseOver(mx, my,210,250,200,64) && Game.gameState == STATE.Menu){
            Game.gameState = STATE.Help;
        }
        //back button for help
        if(Game.gameState == STATE.Help && mouseOver(mx, my,210,350,200,64)){
            Game.gameState = STATE.Menu;
            return;
        }
        //Try Again Button
        if(Game.gameState == STATE.End && mouseOver(mx, my,210,350,200,64)){
            startGame();
        }
        //Quit Button
        if(mouseOver(mx, my,210,350,200,64) && Game.gameState == STATE.Menu){
            System.exit(1);
        }
    }
    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mx, int my,int x, int y, int width, int height) {
        return mx > x && mx < x + width && my > y && my < y + height;
    }

    public void tick(){

    }
    public void mute(){
        if(playing) {
            clip.stop();
            clip.close();
        }
    }

    private static String[] file = {"Music/Killing_Time.wav","Music/Latin_Industries.wav","Music/MTA.wav","Music/Rhinoceros.wav","Music/Ropocalypse_2.wav","Music/Severe_Tire_Damage.wav"};

    private void startGame(){
        handler.object.clear();
        Game.gameState = STATE.Game;
        //Music
        if(!muted){
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(Menu.class.getResource(file[r.nextInt(5 + 1)]));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            assert clip != null;
            clip.open(audioIn);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        clip.loop(-1);
        clip.start();
        playing =true;
        }
        //Reset Level + Hud and start game
        hud.setLevel(1);
        hud.setScore(0);
        handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
        handler.clearEnemies();
        handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemy, handler));
    }

    private static int countLines(String filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        }
    }
    public void render(Graphics g){
        try {
            BufferedImage image;
            if(muted){
                image = ImageIO.read(Menu.class.getResourceAsStream("image/white-mute-32.png"));
            }else{
                image = ImageIO.read(Menu.class.getResourceAsStream("image/white-volume-up-32.png"));
            }
            g.setColor(Color.white);
            g.drawImage(image, 10, 400, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Game.gameState == STATE.Menu){
            Font fnt = new Font("Segoe UI", Font.PLAIN, 50);
            Font fnt2 = new Font("Segoe UI", Font.PLAIN, 30);

            g.setFont(fnt);
            g.setColor(Color.white);
            g.drawString("Menu", 240, 70);

            g.setFont(fnt2);
            g.drawRect(210, 150, 200, 64);
            g.drawString("Play", 270, 190);

            g.drawRect(210, 250, 200, 64);
            g.drawString("Help", 270, 290);

            g.drawRect(210,350,200,64);
            g.drawString("Quit",270, 390);
        }else if(Game.gameState == STATE.Help){
            Font fnt = new Font("Segoe UI", Font.PLAIN, 50);
            Font fnt2 = new Font("Segoe UI", Font.PLAIN, 30);
            Font fnt3 = new Font("Segoe UI", Font.PLAIN, 20);

            g.setFont(fnt);
            g.setColor(Color.white);
            g.drawString("Help", 220, 70);


            g.setFont(fnt3);
            g.drawString("Use WASD to move", Game.WIDTH /2 - 96,200);


            g.setFont(fnt2);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Back", 270, 390);
        }else if(Game.gameState == STATE.End){
            Font fnt = new Font("Segoe UI", Font.PLAIN, 50);
            Font fnt2 = new Font("Segoe UI", Font.PLAIN, 30);
            Font fnt3 = new Font("Segoe UI", Font.PLAIN, 20);

            g.setFont(fnt);
            g.setColor(Color.white);
            g.drawString("Game Over", 180, 70);

            //Get Highscore
            File f = new File("score.txt");

            Scanner s = null;
            try {
                s = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert s != null;
            int[] array;

            int lines;
            try {
                lines = countLines("score.txt");
                array = new int[lines];
            } catch (IOException e) {
                array = new int[0];
                lines = 0;
                e.printStackTrace();
            }

            for (int i = 0; i < array.length; i++) {
                array[i] = s.nextInt();
            }

            int highscore;
            boolean newHigh;
            if(lines != 0) {
                highscore = array[array.length - 1];
            }else{
                highscore = hud.getScore();
            }
            newHigh = highscore == hud.getScore();

            g.setFont(fnt3);
            if(newHigh) {
                g.drawString("New Highscore!", 175, 150);
            }
            else{
                g.drawString("Highscore: " + highscore,175,150);
            }

            g.setFont(fnt3);
            g.drawString("You lost with a score with: " + hud.getScore(),175,200);

            g.setFont(fnt3);
            g.drawString("And you got to level: " + hud.getLevel(),175,250);

            g.setFont(fnt2);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Try Again", 245, 390);
        }

    }

}
