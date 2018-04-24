import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JComboBoxItemListener implements ItemListener {
    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource().equals(Calendar.viewJComboBox)
                || itemEvent.getSource().equals(Calendar.yearJComboBox)) {
            Calendar.updateViewComponents();
        }
        Calendar.updateCopyComponents();
        Calendar.updateCalendarBoxes();
        Calendar.applyComponentsColours();
    }
}
