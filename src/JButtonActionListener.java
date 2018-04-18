import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Calendar.clearCalendarBoxes();
    }
}
