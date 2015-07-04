package net.xirion.MyGame;

import java.awt.*;

/**
 * Created by Victor on 4/7/2015.
 */
public class BasicEnemy extends GameObject {

    public BasicEnemy(int x, int y, ID id) {
        super(x, y, id);

        velX = 5;
        velY = 5;
    }

    public void tick() {
        x+= velX;
        y+= velY;

        if(y <= 0 || y >= Game.HEIGHT -32) velY *=-1;

        if(x <= 0 || x >= Game.WIDTH -16) velX *=-1;

    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x,y,16,16);

    }

    public Rectangle getBounds() {
        return new Rectangle(x,y,32,32);
    }
}
