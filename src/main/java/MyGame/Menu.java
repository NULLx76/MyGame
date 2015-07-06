package main.java.MyGame;

import main.java.MyGame.Game.STATE;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.SecureRandom;
import java.util.Random;

public class Menu extends MouseAdapter{

    private Handler handler;
    private Random r = new SecureRandom();
    private HUD hud;

    public Menu(Handler handler, HUD hud){
        this.handler = handler;
        this.hud = hud;
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();


        //play button
        if(mouseOver(mx, my,210,150,200,64) && Game.gameState == STATE.Menu){
            Game.gameState = STATE.Game;
            handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
            handler.clearEnemies();
            handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemy, handler));
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
            Game.gameState = STATE.Game;
            hud.setLevel(1);
            hud.setScore(0);
            handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
            handler.clearEnemies();
            handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemy, handler));
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

    public void render(Graphics g){
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

            g.setFont(fnt3);
            g.drawString("You lost with a score with: " + hud.getScore(),175,200);

            g.setFont(fnt3);
            g.drawString("And you got to level: " + hud.getLevel(),175,250);

            g.setFont(fnt2);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Try Again",245, 390);
        }

    }

}
