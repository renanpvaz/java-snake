import java.awt.EventQueue;
import javax.swing.JFrame;

public class BoardMain extends JFrame {
    public BoardMain() {    
        initUI();
    }
    
    private void initUI() {
        add(new Board());

        setTitle("Moving sprite");
        setSize(800, 600);
        
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            BoardMain ex = new BoardMain();
            ex.setVisible(true);
        });
    }
}