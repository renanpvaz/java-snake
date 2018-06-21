import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.util.Timer;
import java.awt.Font;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Game extends JPanel {
    private Timer timer;
    private Snake snake;
    private Point cherry;
    private int points = 0;
    private int lastKeyPressed = 0;
    private int pKey = 0;
    private BufferedImage image;
    private GameStatus status;
    
    private static int WIDTH = 760;
    private static int HEIGHT = 520;
    private static int DELAY = 50;

    private int fps;

    public Game() {
        try {
            image = ImageIO.read(new File("cherry.png"));
        } catch (IOException e) {
        }

        addKeyListener(new KeyListener());
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        snake = new Snake(400, 250);
        status = GameStatus.NOT_STARTED;
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        render(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void update() {
        snake.move();

        if (cherry != null && snake.getHead().intersects(cherry, 20)) {
            snake.addTail();
            cherry = null;
            points++;
        }

        if (cherry == null) {
            spawnCherry();
        }

        checkForGameOver();
    }
    
    private void setStatus(GameStatus newStatus) {
        if (status != newStatus) {
            if (newStatus == GameStatus.RUNNING) {
                timer = new Timer();
                timer.schedule(new GameLoop(), 0, DELAY);
            } else if (newStatus != GameStatus.NOT_STARTED) {
                timer.cancel();
            }
        }
        
        status = newStatus;
    }
    
    private void togglePause() {
        setStatus(status == GameStatus.PAUSED ? GameStatus.RUNNING : GameStatus.PAUSED);
    }
    
    private void checkForGameOver() {
        Point head = snake.getHead();
        boolean hitBoundary = head.getX() <= 20
            || head.getX() >= WIDTH + 10
            || head.getY() <= 40
            || head.getY() >= HEIGHT + 30;

        boolean ateItself = false;

        for(Point t : snake.getTail()) {
            ateItself = ateItself || head.equals(t);
        }

        setStatus(hitBoundary || ateItself ? GameStatus.GAME_OVER : status);
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Courier", Font.PLAIN, 12));
        
        if (status == GameStatus.NOT_STARTED) {
          g2d.setFont(new Font("Courier", Font.PLAIN, 18));
          g2d.setColor(new Color(71, 128, 0));

          g2d.drawString("Press any key to begin", 250, 340);

          g2d.drawString("███████╗███╗   ██╗ █████╗ ██╗   ██╗███████╗", 150, 150);
          g2d.setColor(new Color(30, 109, 30));
          g2d.drawString("██╔════╝████╗  ██║██╔══██╗██║  ██╔╝██╔════╝", 150, 170);
          g2d.setColor(new Color(41, 103, 41));
          g2d.drawString("███████╗██╔██╗ ██║███████║██████╔╝ █████╗  ", 150, 190);
          g2d.setColor(new Color(45, 90, 45));
          g2d.drawString("╚════██║██║╚██╗██║██╔══██║██║  ██╗ ██╔══╝  ", 150, 210);
          g2d.setColor(new Color(49, 78, 49));
          g2d.drawString("███████║██║ ╚████║██║  ██║██║   ██╗███████╗", 150, 230);
          g2d.setColor(new Color(46, 66, 46));
          g2d.drawString("╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝   ╚═╝╚══════╝", 150, 250);

          return;
        }

        Point p = snake.getHead(); 

        g2d.drawString("Points: " + points, 20, 20);
        
        if (cherry != null) {
            g2d.drawImage(image, cherry.getX(), cherry.getY(), 60, 60, null);
        }   
        
        if (status == GameStatus.GAME_OVER) {
            g2d.setFont(new Font("Courier", Font.PLAIN, 30));
            g2d.drawString("GAME OVER", 300, 300);
        }
            
        if (status == GameStatus.PAUSED) {
            g2d.drawString("Paused", 600, 14);
        }
        
        g2d.setColor(new Color(74, 245, 14));
        g2d.fillRect(p.getX(), p.getY(), 10, 10);

        for(int i = 0, size = snake.getTail().size(); i < size; i++) {
            Point t = snake.getTail().get(i);

            g2d.fillRect(t.getX(), t.getY(), 10, 10);
        }
        
        g2d.setColor(new Color(71, 128, 0));
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(20, 40, WIDTH, HEIGHT);
    }

    public void spawnCherry() {
        cherry = new Point((new Random()).nextInt(WIDTH - 60) + 20,
            (new Random()).nextInt(HEIGHT - 60) + 20);
    }

    private class KeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
           
            if (status == GameStatus.RUNNING) {
                switch(key) {
                    case KeyEvent.VK_LEFT: snake.turn(Direction.LEFT); break;
                    case KeyEvent.VK_RIGHT: snake.turn(Direction.RIGHT); break;
                    case KeyEvent.VK_UP: snake.turn(Direction.UP); break;
                    case KeyEvent.VK_DOWN: snake.turn(Direction.DOWN); break;
                }
            }
            
            if (status == GameStatus.NOT_STARTED) {
                setStatus(GameStatus.RUNNING);
            }
            
            if (key == KeyEvent.VK_P) {
                togglePause();
            }
        }
    }

    private class GameLoop extends java.util.TimerTask {
        public void run() {
            update();
            repaint();
        }
    }
}
