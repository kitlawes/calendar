import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "CLEAR WEEK":
                Calendar.clearCalendarBoxes("WEEK");
                break;
            case "CLEAR MONTH":
                Calendar.clearCalendarBoxes("MONTH");
                break;
            case "CLEAR YEAR":
                Calendar.clearCalendarBoxes("YEAR");
                break;
            case "CLEAR CALENDAR":
                Calendar.clearCalendarBoxes("ALL");
                break;
            case "COPY DAY":
                Calendar.copyCalendarBox();
                break;
            case "DEFAULT COLOURS":
                Calendar.setComponentsColours(false);
                Calendar.applyComponentsColours();
                break;
            case "RANDOM COLOURS":
                Calendar.setComponentsColours(true);
                Calendar.applyComponentsColours();
                break;
        }
    }
}
