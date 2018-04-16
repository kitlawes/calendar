import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
import java.util.GregorianCalendar;

public class Calendar extends JFrame {

    static JComboBox jComboBox;
    static JTextArea[][] jTextAreas;

    public static void main(String[] args) {
        new Calendar();
    }

    public Calendar() {

        setSize(50 * 7 + 17, 200 + 40);
        setLocation(3450, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(null);

        jComboBox = new JComboBox();
        for (int i = 0; i < 12; i++) {
            jComboBox.addItem(Month.of(i + 1));
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        jComboBox.setSelectedIndex(gregorianCalendar.get(GregorianCalendar.MONTH));
        JComboBoxItemListener jComboBoxItemListener = new JComboBoxItemListener();
        jComboBox.addItemListener(jComboBoxItemListener);
        jComboBox.setSize(50 * 7 + 1, 25 + 1);
        jComboBox.setLocation(0, 0);
        jComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jComboBox.setFont(jComboBox.getFont().deriveFont(7f));
        add(jComboBox);

        for (int i = 0; i < 7; i++) {
            JTextArea jTextArea = new JTextArea();
            jTextArea.setText(DayOfWeek.of(i + 1).toString());
            jTextArea.setSize(50 + 1, 25 + 1);
            jTextArea.setLocation(50 * i, 25);
            jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
            add(jTextArea);
        }

        jTextAreas = new JTextArea[6][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                JTextArea jTextArea = new JTextArea();
                jTextArea.setSize(50 + 1, 25 + 1);
                jTextArea.setLocation(50 * i, 50 + 25 * j);
                jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
                add(jTextArea);
                jTextAreas[j][i] = jTextArea;
            }
        }
        updateJTextAreas();

        setVisible(true);

    }

    static void updateJTextAreas() {
        if (jTextAreas != null) {
            YearMonth yearMonth = YearMonth.of(2018, jComboBox.getSelectedIndex() + 1);
            int lengthOfMonth = yearMonth.lengthOfMonth();
            GregorianCalendar gregorianCalendar = new GregorianCalendar(2018, jComboBox.getSelectedIndex(), 1);
            int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
            System.out.println(offset);
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    int date = i + j * 7 - offset;
                    jTextAreas[j][i].setText(date >= 1 && date <= lengthOfMonth ? String.valueOf(date) : "");
                }
            }
        }
    }

}
