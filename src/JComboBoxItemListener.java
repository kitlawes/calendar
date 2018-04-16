import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JComboBoxItemListener implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            Calendar.updateJTextAreas();
        }
    }

}
