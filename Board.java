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

public class Board extends JPanel {
    private Timer timer;
    private Snake snake;
    private final int DELAY = 200;
    private long lastFpsTime;
    
    private int fps;

    public Board() {
        initBoard();
    }
    
    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        snake = new Snake();
        
        timer = new Timer();
        timer.schedule(new LoopyStuff(), 0, 100);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        render(g);
        
        Toolkit.getDefaultToolkit().sync();
    }
     
    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Point p = snake.getHead();
        
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Courier", Font.PLAIN, 12)); 

        g2d.drawString(snake.toString(), 10, 10);
        
        g2d.drawString("@", p.getX(), p.getY());
        
        for(Point t : snake.getTail()) {
            g2d.drawString("O", t.getX(), t.getY());
        }
    } 

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            snake.keyReleased(e);
        }
    }
    
    private class LoopyStuff extends java.util.TimerTask
    {
        public void run() //this becomes the loop
        {
            snake.move();
            repaint();
    
        }
    }
}