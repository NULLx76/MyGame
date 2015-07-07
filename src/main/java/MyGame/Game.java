package main.java.MyGame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game extends Canvas implements Runnable {
    public static final int WIDTH = 640, HEIGHT = WIDTH / 12 * 9;
    private static final long serialVersionUID = 5755600125683007216L;
    private Thread thread;
    private boolean running = false;
    private Random r = new SecureRandom();
    private static Handler handler;
    private HUD hud;
    private Spawn spawner;
    private Menu menu;

    public enum STATE {
      Menu,
      Help,
      Game,
      End
    }

    public static STATE gameState = STATE.Menu;

    private Game(){
        handler = new Handler();
        hud = new HUD();
        menu = new Menu(handler,hud);
        this.addKeyListener(new KeyInput(handler));
        this.addMouseListener(menu);

        new Window(WIDTH,HEIGHT,"MyGame by victorheld", this);

        spawner = new Spawn(handler, hud);
        Random r = new SecureRandom();

        if(gameState != STATE.Game){
            handler.object.clear();
            for(int i =0; i< 10; i++){
                handler.addObject(new MenuParticle(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.MenuParticle, handler));
            }
        }
    }
    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    private synchronized void stop(){
        try{
            thread.join();
            running = false;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void run(){
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                try {
                    tick();
                } catch (FileNotFoundException | UnsupportedEncodingException e) {e.printStackTrace();}
                delta--;
            }
            if(running)
                render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                //System.out.println("FPS: " + frames);
                hud.setFps(frames);
                frames = 0;
            }
        }
        stop();
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
                    if (c[i] == '\n') ++count;
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        }
    }

    private void tick() throws FileNotFoundException, UnsupportedEncodingException {

        handler.tick();

        if(gameState == STATE.Game){
            hud.tick();
            spawner.tick();

            File f = new File("score.txt");

            if(HUD.HEALTH <= 0){
                menu.mute();

                //Scoreboard
                String score = String.valueOf(hud.getScore());

                try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                    out.println(score);
                }catch (IOException e) {
                    e.printStackTrace();
                }

                Scanner s = null;
                try {
                    s = new Scanner(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                assert s != null;
                int[] array;

                try {
                    array = new int[countLines("score.txt")];
                } catch (IOException e) {
                    array = new int[0];
                    e.printStackTrace();
                }

                for (int i = 0; i < array.length; i++) {
                    array[i] = s.nextInt();
                }
                Arrays.sort(array);

                PrintWriter writer = new PrintWriter("score.txt", "UTF-8");
                for (int anArray : array) {
                    writer.println(anArray);
                }
                writer.close();

                gameState = STATE.End;
                HUD.HEALTH=100;
                handler.clearEnemies();
                for(int i =0; i< 10; i++) {
                    handler.addObject(new MenuParticle(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.MenuParticle, handler));
                }
            }
        }else if(gameState == STATE.Menu || gameState == STATE.End){
            menu.tick();
        }
    }
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D)g;

        //Anti aliasing
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        handler.render(g);

        if(gameState == STATE.Game){
            hud.render(g);
        }else if(gameState == STATE.Menu || gameState == STATE.Help  || gameState == STATE.End){
            menu.render(g);
        }

        g.dispose();
        bs.show();
    }
    public static float clamp(float var, float min, float max){
        if(var >= max)
            return max;
        else if(var < min)
            return min;
        else
            return var;
    }
    public static void main(String args[]){
        new Game();
    }
}