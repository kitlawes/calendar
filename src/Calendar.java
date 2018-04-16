import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
import java.util.GregorianCalendar;

public class Calendar extends JFrame {

    static JComboBox monthJComboBox;
    static JComboBox yearJComboBox;
    static JTextArea[][] jTextAreas;
    static String[][][][] calendarBoxesContents;
    static boolean calendarBoxesContentsLocked;

    public static void main(String[] args) {
        new Calendar();
    }

    public Calendar() {

        setSize(50 * 7 + 17, 200 + 40);
        setLocation(3450, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        monthJComboBox = new JComboBox();
        for (int i = 0; i < 12; i++) {
            monthJComboBox.addItem(Month.of(i + 1));
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        monthJComboBox.setSelectedIndex(gregorianCalendar.get(GregorianCalendar.MONTH));
        monthJComboBox.setSize(25 * 7 + 1, 25 + 1);
        monthJComboBox.setLocation(0, 0);
        monthJComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        monthJComboBox.setFont(monthJComboBox.getFont().deriveFont(7f));
        JComboBoxItemListener jComboBoxItemListener = new JComboBoxItemListener();
        monthJComboBox.addItemListener(jComboBoxItemListener);
        add(monthJComboBox);

        gregorianCalendar = new GregorianCalendar();
        yearJComboBox = new JComboBox();
        for (int i = 0; i < 21; i++) {
            yearJComboBox.addItem(gregorianCalendar.get(GregorianCalendar.YEAR) - 10 + i);
        }
        yearJComboBox.setSelectedIndex(10);
        yearJComboBox.setSize(25 * 7 + 1, 25 + 1);
        yearJComboBox.setLocation(25 * 7, 0);
        yearJComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        yearJComboBox.setFont(yearJComboBox.getFont().deriveFont(7f));
        yearJComboBox.addItemListener(jComboBoxItemListener);
        add(yearJComboBox);

        for (int i = 0; i < 7; i++) {
            JTextArea jTextArea = new JTextArea();
            jTextArea.setText(DayOfWeek.of(i + 1).toString());
            jTextArea.setSize(50 + 1, 25 + 1);
            jTextArea.setLocation(50 * i, 25);
            jTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
            jTextArea.setBackground(Color.LIGHT_GRAY);
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
                jTextArea.setBackground(Color.WHITE);
                JTextAreaDocumentListener jTextAreaDocumentListener = new JTextAreaDocumentListener();
                jTextArea.getDocument().addDocumentListener(jTextAreaDocumentListener);
                add(jTextArea);
                jTextAreas[j][i] = jTextArea;
            }
        }

        calendarBoxesContents = new String[21][12][6][7];
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 12; j++) {
                YearMonth yearMonth = YearMonth.of(2008 + i, j + 1);
                int lengthOfMonth = yearMonth.lengthOfMonth();
                gregorianCalendar = new GregorianCalendar(2008 + i, j, 1);
                int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
                for (int k = 0; k < 6; k++) {
                    for (int l = 0; l < 7; l++) {
                        int date = l + k * 7 - offset;
                        if (date >= 1 && date <= lengthOfMonth) {
                            calendarBoxesContents[i][j][k][l] = String.valueOf(date);
                        } else {
                            calendarBoxesContents[i][j][k][l] = "";
                        }
                    }
                }
            }
        }

        updateCalendarBoxes();

        setVisible(true);

    }

    static void updateCalendarBoxes() {
        calendarBoxesContentsLocked = true;
        if (jTextAreas != null) {
            YearMonth yearMonth = YearMonth.of((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex() + 1);
            int lengthOfMonth = yearMonth.lengthOfMonth();
            GregorianCalendar gregorianCalendar = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex(), 1);
            int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    jTextAreas[j][i].setText(calendarBoxesContents[yearJComboBox.getSelectedIndex()][monthJComboBox.getSelectedIndex()][j][i]);
                    int date = i + j * 7 - offset;
                    if (date >= 1 && date <= lengthOfMonth) {
                        jTextAreas[j][i].setBackground(Color.WHITE);
                    } else {
                        jTextAreas[j][i].setBackground(Color.GRAY);
                    }
                }
            }
        }
        calendarBoxesContentsLocked = false;
    }

    static void saveCalendarBoxesContents() {
        if (!calendarBoxesContentsLocked) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    calendarBoxesContents[yearJComboBox.getSelectedIndex()][monthJComboBox.getSelectedIndex()][j][i] = jTextAreas[j][i].getText();
                }
            }
        }
    }

}
