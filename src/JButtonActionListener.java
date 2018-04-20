import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "CLEAR MONTH":
                Calendar.clearCalendarBoxes("MONTH");
                break;
            case "CLEAR CALENDAR":
                Calendar.clearCalendarBoxes("ALL");
                break;
        }
    }
}
