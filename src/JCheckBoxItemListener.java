import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JCheckBoxItemListener implements ItemListener {
    @Override
    public void itemStateChanged(ItemEvent event) {
        Calendar.setWhetherComponentsAreEnabled();
    }
}
