package net.xirion.MyGame;

import java.awt.*;

public class EnemyBoss extends GameObject{
    private Handler handler;

    private int timer = 80;
    private int timer2 = 50;

    public EnemyBoss(int x, int y, ID id, Handler handler){
        super(x,y,id);
        this.handler = handler;
        velX = 0;
        velY = 2;
    }
    public Rectangle getBounds(){
        return new Rectangle((int) x,(int) y, 96, 96);
    }
    public void tick(){
        x += velX;
        y += velY;

        if(timer <= 0){ velY = 0; timer2--;}
        else timer--;

        if(timer2 <=0){
            if(velX == 0) velX = 3;
        }

        //if(y <= 0 || y >= Game.HEIGHT - 32) velY *= -1;
        if(x <= 0 || x >= Game.WIDTH - 96) velX *= -1;


        //handler.addObject(new Trail((int)x,(int) y, ID.Trail, Color.red, 96, 96, 0.02f,handler));
    }
    public void render(Graphics g){
        g.setColor(Color.red);
        g.fillRect((int)x,(int)y,96,96);
    }
}