import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
import java.util.GregorianCalendar;

public class Calendar extends JFrame {

    static Calendar calendar;
    static JComboBox monthJComboBox;
    static JComboBox yearJComboBox;
    static JTextArea[] headerJTextAreas;
    static JTextArea[][][] calendarBoxesJTextAreas;
    static String[][][][][] calendarBoxesContents;
    static JButton clearMonthJButton;
    static JButton clearYearJButton;
    static JButton clearCalendarJButton;
    static final String filename = "calendar_boxes_contents.ser";
    static boolean calendarBoxesContentsLocked;

    public static void main(String[] args) {
        calendar = new Calendar();
        setup(calendar);
    }

    static void setup(Calendar calendar) {

        calendar.setSize(50 * 7 + 17, 25 * 9 + 40);
        GraphicsDevice[] graphicsDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        int graphicsDevicesWidth = graphicsDevices[0].getDisplayMode().getWidth() + graphicsDevices[1].getDisplayMode().getWidth();
        int graphicsDevicesHeight = graphicsDevices[0].getDisplayMode().getHeight();
        calendar.setLocation(graphicsDevicesWidth - calendar.getWidth() - 10, graphicsDevicesHeight - calendar.getHeight() - 40);
        calendar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        calendar.setLayout(null);
        JFrameComponentAdapter jFrameComponentAdapter = new JFrameComponentAdapter();
        calendar.addComponentListener(jFrameComponentAdapter);

        monthJComboBox = new JComboBox();
        for (int i = 0; i < 12; i++) {
            monthJComboBox.addItem(Month.of(i + 1));
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        monthJComboBox.setSelectedIndex(gregorianCalendar.get(GregorianCalendar.MONTH));
        monthJComboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        monthJComboBox.setFont(monthJComboBox.getFont().deriveFont(7f));
        JComboBoxItemListener jComboBoxItemListener = new JComboBoxItemListener();
        monthJComboBox.addItemListener(jComboBoxItemListener);
        calendar.add(monthJComboBox);

        gregorianCalendar = new GregorianCalendar();
        yearJComboBox = new JComboBox();
        for (int i = 0; i < 21; i++) {
            yearJComboBox.addItem(gregorianCalendar.get(GregorianCalendar.YEAR) - 10 + i);
        }
        yearJComboBox.setSelectedIndex(10);
        yearJComboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        yearJComboBox.setFont(yearJComboBox.getFont().deriveFont(7f));
        yearJComboBox.addItemListener(jComboBoxItemListener);
        calendar.add(yearJComboBox);

        headerJTextAreas = new JTextArea[7];
        for (int i = 0; i < 7; i++) {
            JTextArea jTextArea = new JTextArea();
            jTextArea.setText(DayOfWeek.of(i + 1).toString());
            jTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
            jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
            jTextArea.setBackground(Color.LIGHT_GRAY);
            jTextArea.setEditable(false);
            calendar.add(jTextArea);
            headerJTextAreas[i] = jTextArea;
        }

        JTextAreaDocumentListener jTextAreaDocumentListener = new JTextAreaDocumentListener();
        calendarBoxesJTextAreas = new JTextArea[6][7][2];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                JTextArea jTextArea = new JTextArea();
                jTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
                jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
                calendar.add(jTextArea);
                calendarBoxesJTextAreas[j][i][0] = jTextArea;
                jTextArea = new JTextArea();
                jTextArea.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
                jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
                jTextArea.getDocument().addDocumentListener(jTextAreaDocumentListener);
                calendar.add(jTextArea);
                calendarBoxesJTextAreas[j][i][1] = jTextArea;
            }
        }

        clearMonthJButton = new JButton();
        clearMonthJButton.setText("CLEAR MONTH");
        clearMonthJButton.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        clearMonthJButton.setFont(clearMonthJButton.getFont().deriveFont(7f));
        JButtonActionListener jButtonActionListener = new JButtonActionListener();
        clearMonthJButton.addActionListener(jButtonActionListener);
        calendar.add(clearMonthJButton);

        clearYearJButton = new JButton();
        clearYearJButton.setText("CLEAR YEAR");
        clearYearJButton.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        clearYearJButton.setFont(clearYearJButton.getFont().deriveFont(7f));
        clearYearJButton.addActionListener(jButtonActionListener);
        calendar.add(clearYearJButton);

        clearCalendarJButton = new JButton();
        clearCalendarJButton.setText("CLEAR CALENDAR");
        clearCalendarJButton.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        clearCalendarJButton.setFont(clearCalendarJButton.getFont().deriveFont(7f));
        clearCalendarJButton.addActionListener(jButtonActionListener);
        calendar.add(clearCalendarJButton);

        setComponentsSizeAndLocation();

        File file = new File(filename);
        if (file.exists()) {

            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                calendarBoxesContents = (String[][][][][]) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {

            calendarBoxesContents = new String[21][12][6][7][2];
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
                                calendarBoxesContents[i][j][k][l][0] = String.valueOf(date);
                            } else {
                                calendarBoxesContents[i][j][k][l][0] = "";
                            }
                            calendarBoxesContents[i][j][k][l][1] = "";
                        }
                    }
                }
            }

        }

        updateCalendarBoxes();

        calendar.setVisible(true);

    }

    static void updateCalendarBoxes() {
        calendarBoxesContentsLocked = true;
        YearMonth yearMonth = YearMonth.of((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex() + 1);
        int lengthOfMonth = yearMonth.lengthOfMonth();
        GregorianCalendar gregorianCalendar = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex(), 1);
        int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                calendarBoxesJTextAreas[j][i][0].setText(calendarBoxesContents[yearJComboBox.getSelectedIndex()][monthJComboBox.getSelectedIndex()][j][i][0]);
                calendarBoxesJTextAreas[j][i][1].setText(calendarBoxesContents[yearJComboBox.getSelectedIndex()][monthJComboBox.getSelectedIndex()][j][i][1]);
                calendarBoxesJTextAreas[j][i][0].setEditable(false);
                int date = i + j * 7 - offset;
                if (date >= 1 && date <= lengthOfMonth) {
                    calendarBoxesJTextAreas[j][i][0].setBackground(Color.WHITE);
                    calendarBoxesJTextAreas[j][i][1].setBackground(Color.WHITE);
                    calendarBoxesJTextAreas[j][i][1].setEditable(true);
                } else {
                    calendarBoxesJTextAreas[j][i][0].setBackground(Color.GRAY);
                    calendarBoxesJTextAreas[j][i][1].setBackground(Color.GRAY);
                    calendarBoxesJTextAreas[j][i][1].setEditable(false);
                }
            }
        }
        calendar.repaint();
        calendarBoxesContentsLocked = false;
    }

    static void saveCalendarBoxesContents() {
        if (!calendarBoxesContentsLocked) {

            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    calendarBoxesContents[yearJComboBox.getSelectedIndex()][monthJComboBox.getSelectedIndex()][j][i][0] = calendarBoxesJTextAreas[j][i][0].getText();
                    calendarBoxesContents[yearJComboBox.getSelectedIndex()][monthJComboBox.getSelectedIndex()][j][i][1] = calendarBoxesJTextAreas[j][i][1].getText();
                }
            }

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename));
                objectOutputStream.writeObject(calendarBoxesContents);
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    static void setComponentsSizeAndLocation() {

        double componentWidth = (double) (calendar.getSize().width - 17) / 14;
        double componentHeight = (double) (calendar.getSize().height - 40) / 11;

        monthJComboBox.setSize((int) Math.round(componentWidth * 7), (int) Math.round(componentHeight));
        monthJComboBox.setLocation(0, 0);

        yearJComboBox.setSize((int) Math.round(componentWidth * 14) - (int) Math.round(componentWidth * 7), (int) Math.round(componentHeight));
        yearJComboBox.setLocation((int) Math.round(componentWidth * 7), 0);

        for (int i = 0; i < 7; i++) {
            headerJTextAreas[i].setSize((int) Math.round(componentWidth * 2 * (i + 1)) - (int) Math.round(componentWidth * 2 * i),
                    (int) Math.round(componentHeight * 2) - (int) Math.round(componentHeight));
            headerJTextAreas[i].setLocation((int) Math.round(componentWidth * 2 * i), (int) Math.round(componentHeight));
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                calendarBoxesJTextAreas[j][i][0].setSize((int) Math.round(componentWidth * 2 * (i + 1)) - (int) Math.round(componentWidth * 2 * i),
                        (int) Math.round(componentHeight * 2 + componentHeight * j + componentHeight / 2) - (int) Math.round(componentHeight * 2 + componentHeight * j));
                calendarBoxesJTextAreas[j][i][1].setSize((int) Math.round(componentWidth * 2 * (i + 1)) - (int) Math.round(componentWidth * 2 * i),
                        (int) Math.round(componentHeight * 2 + componentHeight * (j + 1)) - (int) Math.round(componentHeight * 2 + componentHeight * j + componentHeight / 2));
                calendarBoxesJTextAreas[j][i][0].setLocation((int) Math.round(componentWidth * 2 * i), (int) Math.round(componentHeight * 2 + componentHeight * j));
                calendarBoxesJTextAreas[j][i][1].setLocation((int) Math.round(componentWidth * 2 * i), (int) Math.round(componentHeight * 2 + componentHeight * j + componentHeight / 2));
            }
        }

        clearMonthJButton.setSize((int) Math.round(componentWidth * 14), (int) Math.round(componentHeight * 9) - (int) Math.round(componentHeight * 8));
        clearMonthJButton.setLocation(0, (int) Math.round(componentHeight * 8));

        clearYearJButton.setSize((int) Math.round(componentWidth * 14), (int) Math.round(componentHeight * 10) - (int) Math.round(componentHeight * 9));
        clearYearJButton.setLocation(0, (int) Math.round(componentHeight * 9));

        clearCalendarJButton.setSize((int) Math.round(componentWidth * 14), (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10));
        clearCalendarJButton.setLocation(0, (int) Math.round(componentHeight * 10));

    }

    static void clearCalendarBoxesContents(String periodToClear) {

        int earliestMonthToClear = 0;
        int latestMonthToClear = 0;
        int earliestYearToClear = 0;
        int latestYearToClear = 0;
        if (periodToClear.equals("MONTH")) {
            earliestMonthToClear = monthJComboBox.getSelectedIndex();
            latestMonthToClear = monthJComboBox.getSelectedIndex();
            earliestYearToClear = yearJComboBox.getSelectedIndex();
            latestYearToClear = yearJComboBox.getSelectedIndex();
        }
        if (periodToClear.equals("YEAR")) {
            earliestMonthToClear = 0;
            latestMonthToClear = 11;
            earliestYearToClear = yearJComboBox.getSelectedIndex();
            latestYearToClear = yearJComboBox.getSelectedIndex();
        }
        if (periodToClear.equals("ALL")) {
            earliestMonthToClear = 0;
            latestMonthToClear = 11;
            earliestYearToClear = 0;
            latestYearToClear = 20;
        }

        for (int i = earliestYearToClear; i <= latestYearToClear; i++) {
            for (int j = earliestMonthToClear; j <= latestMonthToClear; j++) {
                for (int k = 0; k < 6; k++) {
                    for (int l = 0; l < 7; l++) {
                        calendarBoxesContents[i][j][k][l][1] = "";
                    }
                }
            }
        }

    }

    static void clearCalendarBoxes(String periodToClear) {
        clearCalendarBoxesContents(periodToClear);
        updateCalendarBoxes();
        saveCalendarBoxesContents();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(8, 31, calendar.getSize().width - 17, calendar.getSize().height - 40);
    }

}
