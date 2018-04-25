import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Month;
import java.time.YearMonth;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class Calendar extends JFrame {

    static Calendar calendar;

    static JComboBox viewJComboBox;
    static JComboBox weekJComboBox;
    static JComboBox monthJComboBox;
    static JComboBox yearJComboBox;
    static JTextArea[] headerJTextAreas;
    static JTextArea[][][] monthViewCalendarBoxesJTextAreas;
    static JTextArea[][] weekViewCalendarBoxesJTextAreas;
    static JButton clearWeekJButton;
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

    static String[][][] calendarBoxesContents;
    static Color[] componentsColours;
    static final String calendarBoxesContentsFilename = "calendar_boxes_contents.ser";
    static final String componentsColoursFilename = "components_colours.ser";
    static boolean listenersActive;

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

        viewJComboBox = new JComboBox();
        viewJComboBox.addItem("MONTH VIEW");
        viewJComboBox.addItem("WEEK VIEW");
        viewJComboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        viewJComboBox.setFont(viewJComboBox.getFont().deriveFont(7f));
        JComboBoxItemListener jComboBoxItemListener = new JComboBoxItemListener();
        viewJComboBox.addItemListener(jComboBoxItemListener);
        calendar.add(viewJComboBox);

        monthJComboBox = new JComboBox();
        for (int i = 0; i < 12; i++) {
            monthJComboBox.addItem(Month.of(i + 1));
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        monthJComboBox.setSelectedIndex(gregorianCalendar.get(GregorianCalendar.MONTH));
        monthJComboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        monthJComboBox.setFont(monthJComboBox.getFont().deriveFont(7f));
        monthJComboBox.addItemListener(jComboBoxItemListener);
        calendar.add(monthJComboBox);

        weekJComboBox = new JComboBox();
        weekJComboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        weekJComboBox.setFont(weekJComboBox.getFont().deriveFont(7f));
        weekJComboBox.addItemListener(jComboBoxItemListener);
        calendar.add(weekJComboBox);

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
        monthViewCalendarBoxesJTextAreas = new JTextArea[6][7][2];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                JTextArea jTextArea = new JTextArea();
                jTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
                jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
                calendar.add(jTextArea);
                monthViewCalendarBoxesJTextAreas[j][i][0] = jTextArea;
                jTextArea = new JTextArea();
                jTextArea.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
                jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
                jTextArea.getDocument().addDocumentListener(jTextAreaDocumentListener);
                calendar.add(jTextArea);
                monthViewCalendarBoxesJTextAreas[j][i][1] = jTextArea;
            }
        }

        weekViewCalendarBoxesJTextAreas = new JTextArea[7][2];
        for (int i = 0; i < 7; i++) {
            JTextArea jTextArea = new JTextArea();
            jTextArea.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
            jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
            calendar.add(jTextArea);
            weekViewCalendarBoxesJTextAreas[i][0] = jTextArea;
            jTextArea = new JTextArea();
            jTextArea.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
            jTextArea.setFont(jTextArea.getFont().deriveFont(7f));
            jTextArea.getDocument().addDocumentListener(jTextAreaDocumentListener);
            calendar.add(jTextArea);
            weekViewCalendarBoxesJTextAreas[i][1] = jTextArea;
        }

        clearWeekJButton = new JButton();
        clearWeekJButton.setText("CLEAR WEEK");
        clearWeekJButton.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        clearWeekJButton.setFont(clearWeekJButton.getFont().deriveFont(7f));
        JButtonActionListener jButtonActionListener = new JButtonActionListener();
        clearWeekJButton.addActionListener(jButtonActionListener);
        calendar.add(clearWeekJButton);

        clearMonthJButton = new JButton();
        clearMonthJButton.setText("CLEAR MONTH");
        clearMonthJButton.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
        clearMonthJButton.setFont(clearMonthJButton.getFont().deriveFont(7f));
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

        listenersActive = true;
        updateViewComponents();
        updateCopyComponents();
        setComponentsSizeAndLocation();

        File file = new File(calendarBoxesContentsFilename);
        if (file.exists()) {

            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                calendarBoxesContents = (String[][][]) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {

            calendarBoxesContents = new String[21][12][31];
            for (int i = 0; i < 21; i++) {
                for (int j = 0; j < 12; j++) {
                    for (int k = 0; k < 31; k++) {
                        calendarBoxesContents[i][j][k] = "";
                    }
                }
            }

        }

        updateCalendarBoxes();

        file = new File(componentsColoursFilename);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                componentsColours = (Color[]) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            setComponentsColours(false);
        }
        applyComponentsColours();

        calendar.setVisible(true);

    }

    static void updateViewComponents() {

        if (listenersActive) {
            listenersActive = false;

            if (viewJComboBox.getSelectedItem().equals("MONTH VIEW")) {

                weekJComboBox.setVisible(false);
                monthJComboBox.setVisible(true);
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 6; j++) {
                        monthViewCalendarBoxesJTextAreas[j][i][0].setVisible(true);
                        monthViewCalendarBoxesJTextAreas[j][i][1].setVisible(true);
                    }
                }
                for (int i = 0; i < 7; i++) {
                    weekViewCalendarBoxesJTextAreas[i][0].setVisible(false);
                    weekViewCalendarBoxesJTextAreas[i][1].setVisible(false);
                }
                clearWeekJButton.setVisible(false);
                clearMonthJButton.setVisible(true);

            }

            if (viewJComboBox.getSelectedItem().equals("WEEK VIEW")) {

                weekJComboBox.removeAllItems();
                GregorianCalendar weekStart = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), 0, 1);
                GregorianCalendar weekEnd = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), 0, (8 - weekStart.get(GregorianCalendar.DAY_OF_WEEK)) % 7 + 1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                while (weekEnd.get(GregorianCalendar.YEAR) == (int) yearJComboBox.getSelectedItem()) {
                    weekJComboBox.addItem(simpleDateFormat.format(weekStart.getTime()) + " - " + simpleDateFormat.format(weekEnd.getTime()));
                    weekStart = (GregorianCalendar) weekEnd.clone();
                    weekStart.add(GregorianCalendar.DAY_OF_MONTH, 1);
                    weekEnd.add(GregorianCalendar.DAY_OF_MONTH, 7);
                }
                if (weekStart.get(GregorianCalendar.YEAR) == (int) yearJComboBox.getSelectedItem()) {
                    weekEnd = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), 11, 31);
                    weekJComboBox.addItem(simpleDateFormat.format(weekStart.getTime()) + " - " + simpleDateFormat.format(weekEnd.getTime()));
                }

                weekJComboBox.setVisible(true);
                monthJComboBox.setVisible(false);
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 6; j++) {
                        monthViewCalendarBoxesJTextAreas[j][i][0].setVisible(false);
                        monthViewCalendarBoxesJTextAreas[j][i][1].setVisible(false);
                    }
                }
                for (int i = 0; i < 7; i++) {
                    weekViewCalendarBoxesJTextAreas[i][0].setVisible(true);
                    weekViewCalendarBoxesJTextAreas[i][1].setVisible(true);
                }
                clearWeekJButton.setVisible(true);
                clearMonthJButton.setVisible(false);

            }

            listenersActive = true;
        }

    }

    static void updateCopyComponents() {

        if (listenersActive) {
            listenersActive = false;

            if (viewJComboBox.getSelectedItem().equals("MONTH VIEW")) {
                dayToCopyJComboBox.removeAllItems();
                YearMonth yearMonth = YearMonth.of((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex() + 1);
                int lengthOfMonth = yearMonth.lengthOfMonth();
                for (int i = 0; i < lengthOfMonth; i++) {
                    dayToCopyJComboBox.addItem(i + 1);
                }
            }

            if (viewJComboBox.getSelectedItem().equals("WEEK VIEW")) {
                dayToCopyJComboBox.removeAllItems();
                Date date = null;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = simpleDateFormat.parse(((String) weekJComboBox.getSelectedItem()).substring(0, 10));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GregorianCalendar weekStart = new GregorianCalendar();
                weekStart.setTime(date);
                try {
                    date = simpleDateFormat.parse(((String) weekJComboBox.getSelectedItem()).substring(13, 23));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GregorianCalendar weekEnd = new GregorianCalendar();
                weekEnd.setTime(date);
                dayToCopyJComboBox.addItem(weekStart.get(GregorianCalendar.DAY_OF_MONTH));
                while (!weekStart.equals(weekEnd)) {
                    weekStart.add(GregorianCalendar.DAY_OF_MONTH, 1);
                    dayToCopyJComboBox.addItem(weekStart.get(GregorianCalendar.DAY_OF_MONTH));
                }
            }

            listenersActive = true;
        }

    }

    static void updateCalendarBoxes() {

        if (listenersActive) {
            listenersActive = false;

            if (viewJComboBox.getSelectedItem().equals("MONTH VIEW")) {
                YearMonth yearMonth = YearMonth.of((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex() + 1);
                int lengthOfMonth = yearMonth.lengthOfMonth();
                GregorianCalendar gregorianCalendar = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex(), 1);
                int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 6; j++) {
                        int date = i + j * 7 - offset;
                        if (date >= 1 && date <= lengthOfMonth) {
                            monthViewCalendarBoxesJTextAreas[j][i][0].setText(String.valueOf(date));
                            monthViewCalendarBoxesJTextAreas[j][i][0].setEditable(false);
                            monthViewCalendarBoxesJTextAreas[j][i][1].setText(calendarBoxesContents[yearJComboBox.getSelectedIndex()][monthJComboBox.getSelectedIndex()][date - 1]);
                            monthViewCalendarBoxesJTextAreas[j][i][1].setEditable(true);
                        } else {
                            monthViewCalendarBoxesJTextAreas[j][i][0].setText("");
                            monthViewCalendarBoxesJTextAreas[j][i][0].setEditable(false);
                            monthViewCalendarBoxesJTextAreas[j][i][1].setText("");
                            monthViewCalendarBoxesJTextAreas[j][i][1].setEditable(false);
                        }
                    }
                }
            }

            if (viewJComboBox.getSelectedItem().equals("WEEK VIEW")) {
                Date date = null;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = simpleDateFormat.parse(((String) weekJComboBox.getSelectedItem()).substring(0, 10));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                int year = gregorianCalendar.get(GregorianCalendar.YEAR);
                gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -((gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7));
                for (int i = 0; i < 7; i++) {
                    if (gregorianCalendar.get(GregorianCalendar.YEAR) == year) {
                        weekViewCalendarBoxesJTextAreas[i][0].setText(String.valueOf(gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH)));
                        weekViewCalendarBoxesJTextAreas[i][0].setEditable(false);
                        weekViewCalendarBoxesJTextAreas[i][1].setText(calendarBoxesContents
                                [gregorianCalendar.get(GregorianCalendar.YEAR) - 2008]
                                [gregorianCalendar.get(GregorianCalendar.MONTH)]
                                [gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1]);
                        weekViewCalendarBoxesJTextAreas[i][1].setEditable(true);
                    } else {
                        weekViewCalendarBoxesJTextAreas[i][0].setText("");
                        weekViewCalendarBoxesJTextAreas[i][0].setEditable(false);
                        weekViewCalendarBoxesJTextAreas[i][1].setText("");
                        weekViewCalendarBoxesJTextAreas[i][1].setEditable(false);
                    }
                    gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
                }
            }

            listenersActive = true;
        }

    }

    static void saveCalendarBoxesContents() {

        if (listenersActive) {
            listenersActive = false;

            if (viewJComboBox.getSelectedItem().equals("MONTH VIEW")) {
                YearMonth yearMonth = YearMonth.of((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex() + 1);
                int lengthOfMonth = yearMonth.lengthOfMonth();
                GregorianCalendar gregorianCalendar = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex(), 1);
                int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 6; j++) {
                        int date = i + j * 7 - offset;
                        if (date >= 1 && date <= lengthOfMonth) {
                            calendarBoxesContents[yearJComboBox.getSelectedIndex()][monthJComboBox.getSelectedIndex()][date - 1] = monthViewCalendarBoxesJTextAreas[j][i][1].getText();
                        }
                    }
                }
            }

            if (viewJComboBox.getSelectedItem().equals("WEEK VIEW")) {
                Date date = null;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = simpleDateFormat.parse(((String) weekJComboBox.getSelectedItem()).substring(0, 10));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                int year = gregorianCalendar.get(GregorianCalendar.YEAR);
                gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -((gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7));
                for (int i = 0; i < 7; i++) {
                    if (gregorianCalendar.get(GregorianCalendar.YEAR) == year) {
                        calendarBoxesContents
                                [gregorianCalendar.get(GregorianCalendar.YEAR) - 2008]
                                [gregorianCalendar.get(GregorianCalendar.MONTH)]
                                [gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1]
                                = weekViewCalendarBoxesJTextAreas[i][1].getText();
                    }
                    gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
                }
            }

            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(calendarBoxesContentsFilename));
                objectOutputStream.writeObject(calendarBoxesContents);
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            listenersActive = true;
        }

    }

    static void setComponentsSizeAndLocation() {

        double componentWidth = (double) (calendar.getSize().width - 17) / 42;
        double componentHeight = (double) (calendar.getSize().height - 40) / 13;

        viewJComboBox.setSize((int) Math.round(componentWidth * 42), (int) Math.round(componentHeight));
        viewJComboBox.setLocation(0, 0);

        weekJComboBox.setSize((int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 2) - (int) Math.round(componentHeight));
        weekJComboBox.setLocation(0, (int) Math.round(componentHeight));
        monthJComboBox.setSize((int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 2) - (int) Math.round(componentHeight));
        monthJComboBox.setLocation(0, (int) Math.round(componentHeight));
        yearJComboBox.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 2) - (int) Math.round(componentHeight));
        yearJComboBox.setLocation((int) Math.round(componentWidth * 21), (int) Math.round(componentHeight));

        for (int i = 0; i < 7; i++) {
            headerJTextAreas[i].setSize((int) Math.round(componentWidth * 6 * (i + 1)) - (int) Math.round(componentWidth * 6 * i),
                    (int) Math.round(componentHeight * 3) - (int) Math.round(componentHeight * 2));
            headerJTextAreas[i].setLocation((int) Math.round(componentWidth * 6 * i), (int) Math.round(componentHeight * 2));
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                monthViewCalendarBoxesJTextAreas[j][i][0].setSize((int) Math.round(componentWidth * 6 * (i + 1)) - (int) Math.round(componentWidth * 6 * i),
                        (int) Math.round(componentHeight * 3 + componentHeight * j + componentHeight / 2) - (int) Math.round(componentHeight * 3 + componentHeight * j));
                monthViewCalendarBoxesJTextAreas[j][i][0].setLocation((int) Math.round(componentWidth * 6 * i), (int) Math.round(componentHeight * 3 + componentHeight * j));
                monthViewCalendarBoxesJTextAreas[j][i][1].setSize((int) Math.round(componentWidth * 6 * (i + 1)) - (int) Math.round(componentWidth * 6 * i),
                        (int) Math.round(componentHeight * 3 + componentHeight * (j + 1)) - (int) Math.round(componentHeight * 3 + componentHeight * j + componentHeight / 2));
                monthViewCalendarBoxesJTextAreas[j][i][1].setLocation((int) Math.round(componentWidth * 6 * i), (int) Math.round(componentHeight * 3 + componentHeight * j + componentHeight / 2));
            }
        }

        for (int i = 0; i < 7; i++) {
            weekViewCalendarBoxesJTextAreas[i][0].setSize((int) Math.round(componentWidth * 6 * (i + 1)) - (int) Math.round(componentWidth * 6 * i),
                    (int) Math.round(componentHeight * 3 + componentHeight / 2) - (int) Math.round(componentHeight * 3));
            weekViewCalendarBoxesJTextAreas[i][0].setLocation((int) Math.round(componentWidth * 6 * i), (int) Math.round(componentHeight * 3));
            weekViewCalendarBoxesJTextAreas[i][1].setSize((int) Math.round(componentWidth * 6 * (i + 1)) - (int) Math.round(componentWidth * 6 * i),
                    (int) Math.round(componentHeight * 3 + componentHeight * 6) - (int) Math.round(componentHeight * 3 + componentHeight / 2));
            weekViewCalendarBoxesJTextAreas[i][1].setLocation((int) Math.round(componentWidth * 6 * i), (int) Math.round(componentHeight * 3 + componentHeight / 2));
        }

        clearWeekJButton.setSize((int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 10) - (int) Math.round(componentHeight * 9));
        clearWeekJButton.setLocation(0, (int) Math.round(componentHeight * 9));
        clearMonthJButton.setSize((int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 10) - (int) Math.round(componentHeight * 9));
        clearMonthJButton.setLocation(0, (int) Math.round(componentHeight * 9));
        clearYearJButton.setSize((int) Math.round(componentWidth * 28) - (int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 10) - (int) Math.round(componentHeight * 9));
        clearYearJButton.setLocation((int) Math.round(componentWidth * 14), (int) Math.round(componentHeight * 9));
        clearCalendarJButton.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 28),
                (int) Math.round(componentHeight * 10) - (int) Math.round(componentHeight * 9));
        clearCalendarJButton.setLocation((int) Math.round(componentWidth * 28), (int) Math.round(componentHeight * 9));

        copyDayJButton.setSize((int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10));
        copyDayJButton.setLocation(0, (int) Math.round(componentHeight * 10));
        dayToCopyJComboBox.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 11) - (int) Math.round(componentHeight * 10));
        dayToCopyJComboBox.setLocation((int) Math.round(componentWidth * 21), (int) Math.round(componentHeight * 10));

        copiesJCheckBox.setSize(21,
                (int) Math.round(componentHeight * 12) - (int) Math.round(componentHeight * 11) - 2);
        copiesJCheckBox.setLocation(1, (int) Math.round(componentHeight * 11) + 1);
        copiesJLabel.setSize((int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 12) - (int) Math.round(componentHeight * 11));
        copiesJLabel.setLocation(0, (int) Math.round(componentHeight * 11));
        copiesJTextField.setSize((int) Math.round(componentWidth * 21) - (int) Math.round(componentWidth * 14),
                (int) Math.round(componentHeight * 12) - (int) Math.round(componentHeight * 11));
        copiesJTextField.setLocation((int) Math.round(componentWidth * 14), (int) Math.round(componentHeight * 11));

        weeksIntervalJCheckBox.setSize(21,
                (int) Math.round(componentHeight * 12) - (int) Math.round(componentHeight * 11) - 2);
        weeksIntervalJCheckBox.setLocation((int) Math.round(componentWidth * 21) + 1, (int) Math.round(componentHeight * 11) + 1);
        weeksIntervalJLabel.setSize((int) Math.round(componentWidth * 35) - (int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 12) - (int) Math.round(componentHeight * 11));
        weeksIntervalJLabel.setLocation((int) Math.round(componentWidth * 21), (int) Math.round(componentHeight * 11));
        weeksIntervalJTextField.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 35),
                (int) Math.round(componentHeight * 12) - (int) Math.round(componentHeight * 11));
        weeksIntervalJTextField.setLocation((int) Math.round(componentWidth * 35), (int) Math.round(componentHeight * 11));

        defaultColoursJButton.setSize((int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 13) - (int) Math.round(componentHeight * 12));
        defaultColoursJButton.setLocation(0, (int) Math.round(componentHeight * 12));
        randomColoursJButton.setSize((int) Math.round(componentWidth * 42) - (int) Math.round(componentWidth * 21),
                (int) Math.round(componentHeight * 13) - (int) Math.round(componentHeight * 12));
        randomColoursJButton.setLocation((int) Math.round(componentWidth * 21), (int) Math.round(componentHeight * 12));

    }

    static void clearCalendarBoxes(String periodToClear) {
        clearCalendarBoxesContents(periodToClear);
        updateCalendarBoxes();
        saveCalendarBoxesContents();
    }

    static void clearCalendarBoxesContents(String periodToClear) {

        if (periodToClear.equals("WEEK")) {

            Date date = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                date = simpleDateFormat.parse(((String) weekJComboBox.getSelectedItem()).substring(0, 10));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            GregorianCalendar weekStart = new GregorianCalendar();
            weekStart.setTime(date);
            try {
                date = simpleDateFormat.parse(((String) weekJComboBox.getSelectedItem()).substring(13, 23));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            GregorianCalendar weekEnd = new GregorianCalendar();
            weekEnd.setTime(date);
            calendarBoxesContents
                    [weekStart.get(GregorianCalendar.YEAR) - 2008]
                    [weekStart.get(GregorianCalendar.MONTH)]
                    [weekStart.get(GregorianCalendar.DAY_OF_MONTH) - 1] = "";
            while (!weekStart.equals(weekEnd)) {
                weekStart.add(GregorianCalendar.DAY_OF_MONTH, 1);
                calendarBoxesContents
                        [weekStart.get(GregorianCalendar.YEAR) - 2008]
                        [weekStart.get(GregorianCalendar.MONTH)]
                        [weekStart.get(GregorianCalendar.DAY_OF_MONTH) - 1] = "";
            }

        } else {

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
                    for (int k = 0; k < 31; k++) {
                        calendarBoxesContents[i][j][k] = "";
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

        Integer copies = null;
        try {
            copies = Integer.parseInt(copiesJTextField.getText());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }
        Integer weeksInterval = null;
        try {
            weeksInterval = Integer.parseInt(weeksIntervalJTextField.getText());
        } catch (NumberFormatException e) {
        } catch (NullPointerException e) {
        }

        GregorianCalendar originalGregorianCalendar = null;
        if (viewJComboBox.getSelectedItem().equals("MONTH VIEW")) {
            originalGregorianCalendar = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex(), (int) dayToCopyJComboBox.getSelectedItem());
        }
        if (viewJComboBox.getSelectedItem().equals("WEEK VIEW")) {
            Date date = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                date = simpleDateFormat.parse(((String) weekJComboBox.getSelectedItem()).substring(0, 10));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            originalGregorianCalendar = new GregorianCalendar();
            originalGregorianCalendar.setTime(date);
            while (originalGregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) != (int) dayToCopyJComboBox.getSelectedItem()) {
                originalGregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
            }
        }

        GregorianCalendar copyGregorianCalendar = (GregorianCalendar) originalGregorianCalendar.clone();
        if (weeksIntervalJCheckBox.isSelected() && weeksInterval != null) {
            copyGregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, 7 * weeksInterval);
        } else {
            copyGregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, 7);
        }
        while (copyGregorianCalendar.get(GregorianCalendar.YEAR) < 2008 + 20 + 1) {

            if (copiesJCheckBox.isSelected() && copies != null) {
                if (copies <= 0) {
                    break;
                }
                copies--;
            }

            calendarBoxesContents
                    [copyGregorianCalendar.get(GregorianCalendar.YEAR) - 2008]
                    [copyGregorianCalendar.get(GregorianCalendar.MONTH)]
                    [copyGregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1]
                    = calendarBoxesContents
                    [originalGregorianCalendar.get(GregorianCalendar.YEAR) - 2008]
                    [originalGregorianCalendar.get(GregorianCalendar.MONTH)]
                    [originalGregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH) - 1];

            if (weeksIntervalJCheckBox.isSelected() && weeksInterval != null) {
                copyGregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, 7 * weeksInterval);
            } else {
                copyGregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, 7);
            }

        }

    }

    static void setWhetherComponentsAreEnabled() {

        copiesJLabel.setEnabled(copiesJCheckBox.isSelected());
        copiesJTextField.setEnabled(copiesJCheckBox.isSelected());
        weeksIntervalJLabel.setEnabled(weeksIntervalJCheckBox.isSelected());
        weeksIntervalJTextField.setEnabled(weeksIntervalJCheckBox.isSelected());

        calendar.repaint();

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
        componentsColours = new Color[4];
        componentsColours[0] = new Color(128 + redAdjustment, 128 + greenAdjustment, 128 + blueAdjustment);
        componentsColours[1] = new Color(Math.min(255, 192 + redAdjustment), Math.min(255, 192 + greenAdjustment), Math.min(255, 192 + blueAdjustment));
        componentsColours[2] = new Color(Math.min(255, 238 + redAdjustment), Math.min(255, 238 + greenAdjustment), Math.min(255, 238 + blueAdjustment));
        componentsColours[3] = new Color(Math.min(255, 255 + redAdjustment), Math.min(255, 255 + greenAdjustment), Math.min(255, 255 + blueAdjustment));

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(componentsColoursFilename));
            objectOutputStream.writeObject(componentsColours);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void applyComponentsColours() {

        if (listenersActive) {
            listenersActive = false;

            viewJComboBox.setBackground(componentsColours[2]);
            weekJComboBox.setBackground(componentsColours[2]);
            monthJComboBox.setBackground(componentsColours[2]);
            yearJComboBox.setBackground(componentsColours[2]);
            for (int i = 0; i < 7; i++) {
                headerJTextAreas[i].setBackground(componentsColours[1]);
            }

            if (viewJComboBox.getSelectedItem().equals("MONTH VIEW")) {
                YearMonth yearMonth = YearMonth.of((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex() + 1);
                int lengthOfMonth = yearMonth.lengthOfMonth();
                GregorianCalendar gregorianCalendar = new GregorianCalendar((int) yearJComboBox.getSelectedItem(), monthJComboBox.getSelectedIndex(), 1);
                int offset = (gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7 - 1;
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 6; j++) {
                        int date = i + j * 7 - offset;
                        if (date >= 1 && date <= lengthOfMonth) {
                            monthViewCalendarBoxesJTextAreas[j][i][0].setBackground(componentsColours[3]);
                            monthViewCalendarBoxesJTextAreas[j][i][1].setBackground(componentsColours[3]);
                        } else {
                            monthViewCalendarBoxesJTextAreas[j][i][0].setBackground(componentsColours[0]);
                            monthViewCalendarBoxesJTextAreas[j][i][1].setBackground(componentsColours[0]);
                        }
                    }
                }
            }

            if (viewJComboBox.getSelectedItem().equals("WEEK VIEW")) {
                Date date = null;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    date = simpleDateFormat.parse(((String) weekJComboBox.getSelectedItem()).substring(0, 10));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                int year = gregorianCalendar.get(GregorianCalendar.YEAR);
                gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, -((gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK) + 5) % 7));
                for (int i = 0; i < 7; i++) {
                    if (gregorianCalendar.get(GregorianCalendar.YEAR) == year) {
                        weekViewCalendarBoxesJTextAreas[i][0].setBackground(componentsColours[3]);
                        weekViewCalendarBoxesJTextAreas[i][1].setBackground(componentsColours[3]);
                    } else {
                        weekViewCalendarBoxesJTextAreas[i][0].setBackground(componentsColours[0]);
                        weekViewCalendarBoxesJTextAreas[i][1].setBackground(componentsColours[0]);
                    }
                    gregorianCalendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
                }
            }

            dayToCopyJComboBox.setBackground(componentsColours[2]);
            copiesJCheckBox.setBackground(componentsColours[2]);
            copiesJLabel.setBackground(componentsColours[2]);
            copiesJTextField.setBackground(componentsColours[3]);
            weeksIntervalJCheckBox.setBackground(componentsColours[2]);
            weeksIntervalJLabel.setBackground(componentsColours[2]);
            weeksIntervalJTextField.setBackground(componentsColours[3]);

            calendar.repaint();

            listenersActive = true;
        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(8, 31, calendar.getSize().width - 17, calendar.getSize().height - 40);
    }

}
