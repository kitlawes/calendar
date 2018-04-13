import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;

public class Calendar extends JFrame {

    public static void main(String[] args) {
        new Calendar();
    }

    public Calendar() {

        setSize(100 * 7 + 17, 400 + 40);
        setLocation(3100, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(null);

        JTextArea jTextArea = new JTextArea();
        jTextArea.setText("APRIL");
        jTextArea.setSize(100 * 7 + 1, 50 + 1);
        jTextArea.setLocation(0, 0);
        jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(jTextArea);

        for (int i = 0; i < 7; i++) {
                jTextArea = new JTextArea();
                jTextArea.setText(DayOfWeek.of(i + 1).toString());
                jTextArea.setSize(100 + 1, 50 + 1);
                jTextArea.setLocation(100 * i, 50);
                jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(jTextArea);
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                jTextArea = new JTextArea();
                int date = i + j * 7 - 5;
                jTextArea.setText(date >= 1 && date <= 30 ? String.valueOf(date) : "");
                jTextArea.setSize(100 + 1, 50 + 1);
                jTextArea.setLocation(100 * i, 100 + 50 * j);
                jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(jTextArea);
            }
        }

        setVisible(true);

    }

}
