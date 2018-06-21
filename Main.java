import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main extends JFrame {
    public Main() {
        initUI();
    }

    private void initUI() {
        add(new Game());

        setTitle("Snake 1.1");
        setSize(800, 600);

        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}
