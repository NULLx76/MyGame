package main.java.MyGame;

import main.java.MyGame.Game.STATE;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Menu extends MouseAdapter{

    private Game game;
    private Handler handler;
    private Random r = new Random();
    private HUD hud;

    public Menu(Game game,Handler handler,HUD hud){
        this.game = game;
        this.handler = handler;
        this.hud = hud;
    }



    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();


        //play button
        if(mouseOver(mx, my,210,150,200,64) && game.gameState == STATE.Menu){
            game.gameState = STATE.Game;
            handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
            handler.clearEnemies();
            handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemy, handler));
        }
        //Help Button
        if(mouseOver(mx, my,210,250,200,64) && game.gameState == STATE.Menu){
            game.gameState = STATE.Help;
        }
        //back button for help
        if(game.gameState == STATE.Help && mouseOver(mx, my,210,350,200,64)){
            game.gameState = STATE.Menu;
            return;
        }
        //Try Again Button
        if(game.gameState == STATE.End && mouseOver(mx, my,210,350,200,64)){
            game.gameState = STATE.Game;
            hud.setLevel(1);
            hud.setScore(0);
            handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler));
            handler.clearEnemies();
            handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemy, handler));
        }
        //Quit Button
        if(mouseOver(mx, my,210,350,200,64) && game.gameState == STATE.Menu){
            System.exit(1);
        }
    }
    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mx, int my,int x, int y, int width, int height){
        if(mx > x && mx < x + width ){
            if(my >y && my < y +height){
                return true;
            }else return false;
        }else return false;
    }

    public void tick(){

    }

    public void render(Graphics g){
        if(game.gameState == STATE.Menu){
            Font fnt = new Font("Segoe UI", 1, 50);
            Font fnt2 = new Font("Segoe UI", 1, 30);

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
        }else if(game.gameState == STATE.Help){
            Font fnt = new Font("Segoe UI", 1, 50);
            Font fnt2 = new Font("Segoe UI", 1, 30);
            Font fnt3 = new Font("Segoe UI", 1, 20);

            g.setFont(fnt);
            g.setColor(Color.white);
            g.drawString("Help", 240, 70);

            g.setFont(fnt3);
            g.drawString("USE [WASD] keys to move the player and dodge enemies!",50,200);

            g.setFont(fnt2);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Back",270, 390);
        }else if(game.gameState == STATE.End){
            Font fnt = new Font("Segoe UI", 1, 50);
            Font fnt2 = new Font("Segoe UI", 1, 30);
            Font fnt3 = new Font("Segoe UI", 1, 20);

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
