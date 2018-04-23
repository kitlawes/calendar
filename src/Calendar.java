import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
import java.util.GregorianCalendar;
import java.util.Random;

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
    static JButton copyDayJButton;
    static JComboBox dayToCopyJComboBox;
    static JCheckBox copiesJCheckBox;
    static JLabel copiesJLabel;
    static JTextField copiesJTextField;
    static JCheckBox weeksIntervalJCheckBox;
    static JLabel weeksIntervalJLabel;
    static JTextField weeksIntervalJTextField;
    static JButton defaultColoursJButton;
    static JButton randomColoursJButton;
    static final String filename = "calendar_boxes_contents.ser";
    static boolean calendarBoxesContentsLocked;
    static Color componentsColour1;
    static Color componentsColour2;
    static Color componentsColour3;
    static Color componentsColour4;

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

        copyDayJButton = new JButton();
        copyDayJButton.setText("COPY DAY");
        copyDayJButton.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        copyDayJButton.setFont(copyDayJButton.getFont().deriveFont(7f));
        copyDayJButton.addActionListener(jButtonActionListener);
        calendar.add(copyDayJButton);

        dayToCopyJComboBox = new JComboBox();
        YearMonth yearMonth = YearMonth.of((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex() + 1);
        int lengthOfMonth = yearMonth.lengthOfMonth();
        for (int i = 0; i < lengthOfMonth; i++) {
            dayToCopyJComboBox.addItem(i + 1);
        }
        dayToCopyJComboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        dayToCopyJComboBox.setFont(dayToCopyJComboBox.getFont().deriveFont(7f));
        calendar.add(dayToCopyJComboBox);

        copiesJCheckBox = new JCheckBox();
        copiesJCheckBox.setSelected(false);
        JCheckBoxItemListener jCheckBoxItemListener = new JCheckBoxItemListener();
        copiesJCheckBox.addItemListener(jCheckBoxItemListener);
        calendar.add(copiesJCheckBox);

        copiesJLabel = new JLabel();
        copiesJLabel.setText("COPIES:");
        copiesJLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        copiesJLabel.setFont(copiesJLabel.getFont().deriveFont(7f));
        copiesJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        copiesJLabel.setEnabled(false);
        copiesJLabel.setOpaque(true);
        calendar.add(copiesJLabel);

        copiesJTextField = new JTextField();
        copiesJTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        copiesJTextField.setFont(copiesJTextField.getFont().deriveFont(7f));
        copiesJTextField.setEnabled(false);
        calendar.add(copiesJTextField);

        weeksIntervalJCheckBox = new JCheckBox();
        weeksIntervalJCheckBox.setSelected(false);
        weeksIntervalJCheckBox.addItemListener(jCheckBoxItemListener);
        calendar.add(weeksIntervalJCheckBox);

        weeksIntervalJLabel = new JLabel();
        weeksIntervalJLabel.setText("WEEKS INTERVAL:");
        weeksIntervalJLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        weeksIntervalJLabel.setFont(weeksIntervalJLabel.getFont().deriveFont(7f));
        weeksIntervalJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        weeksIntervalJLabel.setEnabled(false);
        weeksIntervalJLabel.setOpaque(true);
        calendar.add(weeksIntervalJLabel);

        weeksIntervalJTextField = new JTextField();
        weeksIntervalJTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        weeksIntervalJTextField.setFont(weeksIntervalJTextField.getFont().deriveFont(7f));
        weeksIntervalJTextField.setEnabled(false);
        calendar.add(weeksIntervalJTextField);

        defaultColoursJButton = new JButton();
        defaultColoursJButton.setText("DEFAULT COLOURS");
        defaultColoursJButton.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        defaultColoursJButton.setFont(defaultColoursJButton.getFont().deriveFont(7f));
        defaultColoursJButton.addActionListener(jButtonActionListener);
        calendar.add(defaultColoursJButton);

        randomColoursJButton = new JButton();
        randomColoursJButton.setText("RANDOM COLOURS");
        randomColoursJButton.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        randomColoursJButton.setFont(randomColoursJButton.getFont().deriveFont(7f));
        randomColoursJButton.addActionListener(jButtonActionListener);
        calendar.add(randomColoursJButton);

        setComponentsSizeAndLocation();
        setComponentsColours(false);
        applyComponentsColours();

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
                    yearMonth = YearMonth.of(2008 + i, j + 1);
                    lengthOfMonth = yearMonth.lengthOfMonth();
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
                    calendarBoxesJTextAreas[j][i][1].setEditable(true);
                } else {
                    calendarBoxesJTextAreas[j][i][1].setEditable(false);
                }
            }
        }
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

        double componentWidth = (double) (calendar.getSize().width - 17) / 42;
        double componentHeight = (double) (calendar.getSize().height - 40) / 12;

        monthJComboBox.setSize((int) Math.round(componentWidth * 21), (int) Math.round(componentHeight));
        monthJComboBox.setLocation(0, 0);
        yearJComboBox.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 21), (int) Math.round(componentHeight));
        yearJComboBox.setLocation((int) Math.round(componentWidth * 21), 0);

        for (int i = 0; i < 7; i++) {
            headerJTextAreas[i].setSize((int) Math.round(componentWidth * 6 * (i + 1)) - (int) Math.round(componentWidth * 6 * i),
                    (int) Math.round(componentHeight * 2) - (int) Math.round(componentHeight));
            headerJTextAreas[i].setLocation((int) Math.round(componentWidth * 6 * i), (int) Math.round(componentHeight));
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                calendarBoxesJTextAreas[j][i][0].setSize((int) Math.round(componentWidth * 6 * (i + 1)) - (int) Math.round(componentWidth * 6 * i),
                        (int) Math.round(componentHeight * 2 + componentHeight * j + componentHeight / 2) - (int) Math.round(componentHeight * 2 + componentHeight * j));
                calendarBoxesJTextAreas[j][i][1].setSize((int) Math.round(componentWidth * 6 * (i + 1)) - (int) Math.round(componentWidth * 6 * i),
                        (int) Math.round(componentHeight * 2 + componentHeight * (j + 1)) - (int) Math.round(componentHeight * 2 + componentHeight * j + componentHeight / 2));
                calendarBoxesJTextAreas[j][i][0].setLocation((int) Math.round(componentWidth * 6 * i), (int) Math.round(componentHeight * 2 + componentHeight * j));
                calendarBoxesJTextAreas[j][i][1].setLocation((int) Math.round(componentWidth * 6 * i), (int) Math.round(componentHeight * 2 + componentHeight * j + componentHeight / 2));
            }
        }

        clearMonthJButton.setSize((int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 9) - (int) Math.round(componentHeight * 8));
        clearMonthJButton.setLocation(0, (int) Math.round(componentHeight * 8));
        clearYearJButton.setSize((int) Math.round(componentWidth * 28) - (int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 9) - (int) Math.round(componentHeight * 8));
        clearYearJButton.setLocation((int) Math.round(componentWidth * 14), (int) Math.round(componentHeight * 8));
        clearCalendarJButton.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 28),
                (int) Math.round(componentHeight * 9) - (int) Math.round(componentHeight * 8));
        clearCalendarJButton.setLocation((int) Math.round(componentWidth * 28), (int) Math.round(componentHeight * 8));

        copyDayJButton.setSize((int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 10) - (int) Math.round(componentHeight * 9));
        copyDayJButton.setLocation(0, (int) Math.round(componentHeight * 9));
        dayToCopyJComboBox.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 10) - (int) Math.round(componentHeight * 9));
        dayToCopyJComboBox.setLocation((int) Math.round(componentWidth * 21), (int) Math.round(componentHeight * 9));

        copiesJCheckBox.setSize(21,
                (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10) - 2);
        copiesJCheckBox.setLocation(1, (int) Math.round(componentHeight * 10) + 1);
        copiesJLabel.setSize((int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10));
        copiesJLabel.setLocation(0, (int) Math.round(componentHeight * 10));
        copiesJTextField.setSize((int) Math.round(componentWidth * 21) - (int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10));
        copiesJTextField.setLocation((int) Math.round(componentWidth * 14), (int) Math.round(componentHeight * 10));

        weeksIntervalJCheckBox.setSize(21,
                (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10) - 2);
        weeksIntervalJCheckBox.setLocation((int) Math.round(componentWidth * 21) + 1, (int) Math.round(componentHeight * 10) + 1);
        weeksIntervalJLabel.setSize((int) Math.round(componentWidth * 35) - (int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10));
        weeksIntervalJLabel.setLocation((int) Math.round(componentWidth * 21), (int) Math.round(componentHeight * 10));
        weeksIntervalJTextField.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 35),
                (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10));
        weeksIntervalJTextField.setLocation((int) Math.round(componentWidth * 35), (int) Math.round(componentHeight * 10));

        defaultColoursJButton.setSize((int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 12) - (int) Math.round(componentHeight * 11));
        defaultColoursJButton.setLocation(0, (int) Math.round(componentHeight * 11));
        randomColoursJButton.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 12) - (int) Math.round(componentHeight * 11));
        randomColoursJButton.setLocation((int) Math.round(componentWidth * 21), (int) Math.round(componentHeight * 11));

    }

    static void clearCalendarBoxes(String periodToClear) {
        clearCalendarBoxesContents(periodToClear);
        updateCalendarBoxes();
        saveCalendarBoxesContents();
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

    static void copyCalendarBox() {
        copyCalendarBoxContents();
        updateCalendarBoxes();
        saveCalendarBoxesContents();
    }

    static void copyCalendarBoxContents() {

        GregorianCalendar gregorianCalendar = new GregorianCalendar(
                (int) yearJComboBox.getSelectedItem(),
                monthJComboBox.getSelectedIndex(),
                (int) dayToCopyJComboBox.getSelectedItem());
        gregorianCalendar.setMinimalDaysInFirstWeek(1);
        int dayToCopyFrom = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7;
        int weekToCopyFrom = gregorianCalendar.get(GregorianCalendar.WEEK_OF_MONTH) - 1;
        int monthToCopyFrom = gregorianCalendar.get(GregorianCalendar.MONTH);
        int yearToCopyFrom = yearJComboBox.getSelectedIndex();

        Integer copies = null;
        try {
            copies = Integer.parseInt(copiesJTextField.getText());
        } catch(NumberFormatException e) {
        } catch(NullPointerException e) {
        }
        Integer weeksInterval = null;
        try {
            weeksInterval = Integer.parseInt(weeksIntervalJTextField.getText());
        } catch(NumberFormatException e) {
        } catch(NullPointerException e) {
        }

        YEAR_LOOP:
        for (int i = yearToCopyFrom; i < 21; i++) {
            int earliestMonthToCopyFrom = i == yearToCopyFrom ? monthToCopyFrom : 0;
            for (int j = earliestMonthToCopyFrom; j < 12; j++) {
                int earliestWeekToCopyFrom = i == yearToCopyFrom && j == earliestMonthToCopyFrom ? weekToCopyFrom + 1 : 0;
                for (int k = earliestWeekToCopyFrom; k < 6; k++) {
                    YearMonth yearMonth = YearMonth.of(2008 + i, j + 1);
                    int lengthOfMonth = yearMonth.lengthOfMonth();
                    gregorianCalendar = new GregorianCalendar(2008 + i, j, 1);
                    int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
                    int date = dayToCopyFrom + k * 7 - offset;
                    if (date >= 1 && date <= lengthOfMonth) {

                        if (weeksIntervalJCheckBox.isSelected() && weeksInterval != null) {
                            weeksInterval--;
                            if (weeksInterval >= 1) {
                                continue;
                            }
                            weeksInterval = Integer.parseInt(weeksIntervalJTextField.getText());
                        }

                        if (copiesJCheckBox.isSelected() && copies != null) {
                            if (copies <= 0) {
                                break YEAR_LOOP;
                            }
                            copies--;
                        }

                        calendarBoxesContents[i][j][k][dayToCopyFrom][1]
                                = calendarBoxesContents[yearToCopyFrom][monthToCopyFrom][weekToCopyFrom][dayToCopyFrom][1];

                    }
                }
            }
        }

    }

    static void setWhetherComponentsAreEnabled() {
        copiesJLabel.setEnabled(copiesJCheckBox.isSelected());
        copiesJTextField.setEnabled(copiesJCheckBox.isSelected());
        weeksIntervalJLabel.setEnabled(weeksIntervalJCheckBox.isSelected());
        weeksIntervalJTextField.setEnabled(weeksIntervalJCheckBox.isSelected());
        copiesJCheckBox.revalidate();
        weeksIntervalJCheckBox.revalidate();
    }

    static void setComponentsColours(boolean randomColours) {
        int redAdjustment = 0;
        int greenAdjustment = 0;
        int blueAdjustment = 0;
        if (randomColours) {
            Random random = new Random();
            redAdjustment = random.nextInt(101) - 50;
            greenAdjustment = random.nextInt(101) - 50;
            blueAdjustment = random.nextInt(101) - 50;
        }
        componentsColour1 = new Color(128 + redAdjustment, 128 + greenAdjustment, 128 + blueAdjustment);
        componentsColour2 = new Color(Math.min(255, 192 + redAdjustment), Math.min(255, 192 + greenAdjustment), Math.min(255, 192 + blueAdjustment));
        componentsColour3 = new Color(Math.min(255, 238 + redAdjustment), Math.min(255, 238 + greenAdjustment), Math.min(255, 238 + blueAdjustment));
        componentsColour4 = new Color(Math.min(255, 255 + redAdjustment), Math.min(255, 255 + greenAdjustment), Math.min(255, 255 + blueAdjustment));
    }

    static void applyComponentsColours() {

        monthJComboBox.setBackground(componentsColour3);
        yearJComboBox.setBackground(componentsColour3);
        for (int i = 0; i < 7; i++) {
            headerJTextAreas[i].setBackground(componentsColour2);
        }
        YearMonth yearMonth = YearMonth.of((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex() + 1);
        int lengthOfMonth = yearMonth.lengthOfMonth();
        GregorianCalendar gregorianCalendar = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex(), 1);
        int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                int date = i + j * 7 - offset;
                if (date >= 1 && date <= lengthOfMonth) {
                    calendarBoxesJTextAreas[j][i][0].setBackground(componentsColour4);
                    calendarBoxesJTextAreas[j][i][1].setBackground(componentsColour4);
                } else {
                    calendarBoxesJTextAreas[j][i][0].setBackground(componentsColour1);
                    calendarBoxesJTextAreas[j][i][1].setBackground(componentsColour1);
                }
            }
        }
        dayToCopyJComboBox.setBackground(componentsColour3);
        copiesJCheckBox.setBackground(componentsColour3);
        copiesJLabel.setBackground(componentsColour3);
        copiesJTextField.setBackground(componentsColour4);
        weeksIntervalJCheckBox.setBackground(componentsColour3);
        weeksIntervalJLabel.setBackground(componentsColour3);
        weeksIntervalJTextField.setBackground(componentsColour4);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(8, 31, calendar.getSize().width - 17, calendar.getSize().height - 40);
    }

}
