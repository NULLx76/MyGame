package net.xirion.MyGame;

import java.awt.*;

public class Trail extends GameObject{

    private float alpha = 1;
    private Handler handler;
    private Color color;

    private int width;
    private int height;

    public Trail(int x, int y, ID id,Color color,int width,int height, Handler handler) {
        super(x, y, id);
        this.color = color;
        this.width = width;
        this.height = height;

    }

    public void tick() {

    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(makeTransparent(alpha));
        g.setColor(color);
        g.fillRect(x,y,width,height);
        g2d.setComposite(makeTransparent(1));

    }

    private AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }

    public Rectangle getBounds() {
        return null;
    }
}
