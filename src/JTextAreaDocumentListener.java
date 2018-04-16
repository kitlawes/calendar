import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class JTextAreaDocumentListener implements DocumentListener {

    @Override
    public void insertUpdate(DocumentEvent e) {
        Calendar.saveCalendarBoxesContents();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        Calendar.saveCalendarBoxesContents();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        Calendar.saveCalendarBoxesContents();
    }

}
