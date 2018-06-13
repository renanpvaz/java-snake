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
import java.util.Random;

public class Game extends JPanel {
    private Timer timer;
    private Snake snake;
    private Point cherry;
    private int points = 0;
    private boolean gameOver = false;
    
    private static int DELAY = 50;
    private static double CHERRY_SPAWN_CHANCE = .2;
    
    private int fps;

    public Game() {
        init();
    }
    
    private void init() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        snake = new Snake();
        
        timer = new Timer();
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
        
        if (Math.random() <= CHERRY_SPAWN_CHANCE && cherry == null) {
            spawnCherry();
        }
        
        if (cherry != null && snake.getHead().intersects(cherry)) {
            snake.addTail();
            cherry = null;
            points++;
        }
        
        checkForGameOver();
    }
    
    private void checkForGameOver() {
        Point head = snake.getHead();
        boolean hitBoundary = head.getX() <= 0 
            || head.getX() + 30 >= 800 
            || head.getY() <= 0 
            || head.getY() + 30 >= 600;
            
        boolean ateItself = false;
        
        for(Point t : snake.getTail()) {
            ateItself = ateItself || head.equals(t);
        }
        
        gameOver = hitBoundary || ateItself;
    }
     
    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Courier", Font.PLAIN, 12)); 
        
        Point p = snake.getHead();
        
        g2d.drawString("SNAKE", 10, 14);
        g2d.drawString("Points: " + points, 700, 14);
        
        g2d.fillRect(p.getX(), p.getY(), 10, 10);
        
        for(Point t : snake.getTail()) {
            g2d.fillRect(t.getX(), t.getY(), 10, 10);
        }
        
        if (cherry != null) {
            g2d.setColor(Color.RED);
            g2d.fillOval(cherry.getX(), cherry.getY(), 10, 10);
        }
        
        if (gameOver) {
            g2d.setFont(new Font("Courier", Font.PLAIN, 30)); 
            g2d.drawString("GAME OVER", 300, 300);
        }         
    } 
    
    public void spawnCherry() {
        cherry = new Point((new Random()).nextInt(800), (new Random()).nextInt(600));
    }
    
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT: snake.turn(Direction.LEFT); break;    
                case KeyEvent.VK_RIGHT: snake.turn(Direction.RIGHT); break;
                case KeyEvent.VK_UP: snake.turn(Direction.UP); break;  
                case KeyEvent.VK_DOWN: snake.turn(Direction.DOWN); break;        
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