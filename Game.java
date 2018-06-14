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
    private boolean gameOver = false;
    private boolean started = false;
    private BufferedImage image;

    private static int DELAY = 50;
    private static double CHERRY_SPAWN_CHANCE = .2;

    private int fps;

    public Game() {
        try {
            image = ImageIO.read(new File("cherry.png"));
        } catch (IOException e) {
        }

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        snake = new Snake(400, 250);
        timer = new Timer();
        repaint();
    }

    private void start() {
        started = true;
        timer.schedule(new GameLoop(), 0, DELAY);
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

    private void checkForGameOver() {
        Point head = snake.getHead();
        boolean hitBoundary = head.getX() <= 0
            || head.getX() >= 800
            || head.getY() <= 0
            || head.getY() >= 570;

        boolean ateItself = false;

        for(Point t : snake.getTail()) {
            ateItself = ateItself || head.equals(t);
        }

        gameOver = hitBoundary || ateItself ;
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Courier", Font.PLAIN, 12));

        if (started) {
            Point p = snake.getHead();

            if (cherry != null) g2d.drawString("cherry = "+ cherry.toString(), 10, 14);

            g2d.drawString("snake = "+ snake.getHead().toString(), 10, 24);

            g2d.drawString("Points: " + points, 700, 14);

            g2d.setColor(new Color(74, 245, 14));
            g2d.fillRect(p.getX(), p.getY(), 10, 10);

            for(int i = 0, size = snake.getTail().size(); i < size; i++) {
                Point t = snake.getTail().get(i);

                g2d.fillRect(t.getX(), t.getY(), 10, 10);
            }

            if (cherry != null) {
                g2d.drawImage(image, cherry.getX(), cherry.getY(), 60, 60, null);
            }

            if (gameOver) {
                g2d.setFont(new Font("Courier", Font.PLAIN, 30));
                g2d.drawString("GAME OVER", 300, 300);
            }
        } else {
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

        }
    }

    public void spawnCherry() {
        cherry = new Point((new Random()).nextInt(780),
            (new Random()).nextInt(560));
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (started) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: snake.turn(Direction.LEFT); break;
                    case KeyEvent.VK_RIGHT: snake.turn(Direction.RIGHT); break;
                    case KeyEvent.VK_UP: snake.turn(Direction.UP); break;
                    case KeyEvent.VK_DOWN: snake.turn(Direction.DOWN); break;
                }
            } else {
                start();
            }
        }
    }

    private class GameLoop extends java.util.TimerTask {
        public void run() {
            update();
            repaint();

            if (gameOver) {
                timer.cancel();
            }
        }
    }
}
