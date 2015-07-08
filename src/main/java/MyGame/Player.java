package main.java.MyGame;

import java.awt.*;
public class Player extends GameObject{
    private Handler handler;
    private HUD hud;
    public Player(int x,int y, ID id, Handler handler,HUD hud){
        super(x, y, id);
        this.handler = handler;
        this.hud = hud;
    }
    public Rectangle getBounds(){
        return new Rectangle((int) x,(int) y, 32, 32);
    }
    public void tick(){
        x += velX;
        y += velY;
        x = Game.clamp(x, 0, Game.WIDTH -37);
        y = Game.clamp(y, 0, Game.HEIGHT - 60);
        handler.addObject(new Trail(x, y, ID.Trail, Color.white, 32, 32, 0.05f,handler));
        collision();
    }
    private void collision(){
        for(int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ID.BasicEnemy || tempObject.getId() == ID.FastEnemy || tempObject.getId() == ID.SmartEnemy) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    hud.HEALTH -= 2;
                }
            }
            else if (tempObject.getId() == ID.EnemyBoss) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    hud.HEALTH = 0;
                }
            }
        }
    }
    public void render(Graphics g){
        g.setColor(Color.white);
        g.fillRect((int)x,(int)y,32,32);
    }
}