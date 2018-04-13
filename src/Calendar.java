import javax.swing.*;

public class Calendar extends JFrame {

    public static void main(String[] args) {
        new Calendar();
    }

    public Calendar() {
        setSize(500, 500);
        setLocation(100, 100);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

}
