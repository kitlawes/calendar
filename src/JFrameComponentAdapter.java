import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class JFrameComponentAdapter extends ComponentAdapter {

    public void componentResized(ComponentEvent evt) {
        Calendar.setComponentsSizeAndLocation();
    }

}
