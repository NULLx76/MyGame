package net.xirion.MyGame;

import java.awt.*;

public class Player extends GameObject{

    Handler handler;

    public Player(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;

    }

    public void tick() {
        x+= velX;
        y+= velY;

        x = Game.clamp(x, 0, Game.WIDTH - 37);
        y = Game.clamp(y, 0, Game.HEIGHT - 60);

        handler.addObject(new Trail(x,y,ID.Trail,Color.BLUE,32,32,0.05f,handler));

        collision();

    }

    private void collision(){
        for(int i =0; i < handler.object.size(); i++ ){
            GameObject tempObject = handler.object.get(i);

            if(tempObject.getId() == ID.BasicEnemy){
                if(getBounds().intersects(tempObject.getBounds())){
                    //collision code
                    HUD.HEALTH -= 2;
                }
            }
        }
    }

    public void render(Graphics g) {
       g.setColor(Color.BLUE);
        g.fillRect(x,y,32,32);
    }

    public Rectangle getBounds() {
        return new Rectangle(x,y,32,32);
    }


}
