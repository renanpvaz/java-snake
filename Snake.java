import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import java.util.*;

public class Snake {
    private Direction direction;
    private Point head;
    private Point changedDirectionAt;
    private ArrayList<Point> tail;
    
    public Snake() {
        this.head = new Point(200, 150);
        this.changedDirectionAt = new Point(0, 0);
        
        this.direction = Direction.RIGHT;
        this.tail = new ArrayList<Point>();
        
        this.tail.add(new Point(0, 0));
        this.tail.add(new Point(0, 0));
        this.tail.add(new Point(0, 0));    
    }

    public void move() {
        ArrayList newTail = new ArrayList<Point>();
        
        for (int i = 0, size = tail.size(); i < size; i++) {
            Point current = tail.get(i);
            Point previous = i == 0 ? head : tail.get(i - 1);

            newTail.add(new Point(previous.getX(), previous.getY()));
        }
        
        this.tail = newTail;
        
        this.head.move(this.direction, 20);
    }
    
    public void addTail() {
        this.tail.add(new Point(head));
    }
        
    public int getOffsetX() {
        switch(this.direction) {
            case RIGHT: return -8;      
            case LEFT: return 8;        
        }
        
        return 0;
    }
    
    public int getOffsetY() {
        switch(this.direction) {
            case UP: return 10;      
            case DOWN: return -10;        
        }
        
        return 0;
    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT: direction = Direction.LEFT; break;    
            case KeyEvent.VK_RIGHT: direction = Direction.RIGHT; break;
            case KeyEvent.VK_UP: direction = Direction.UP; break;  
            case KeyEvent.VK_DOWN: direction = Direction.DOWN; break;        
        }
        
        this.changedDirectionAt = new Point(this.head);
    }
    
    public ArrayList<Point> getTail() {
        return this.tail;
    }
    
    public Point getHead() {
        return this.head;
    }
    
    public String toString() {
        return "head = " + head +
            "\n tail = [ " + tail.get(0) + "\n," + tail.get(1) + "\n," + tail.get(2) + "\n]"; 
    }
}
