import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;

public class InputListener implements NativeKeyListener, NativeMouseListener {
    private StringBuilder actionString = new StringBuilder();
    public InputListener() {

    }

    public synchronized String getActionString() {
        return actionString.toString();
    }


    public synchronized void resetActionString() {
        actionString.delete(0, actionString.length());
    }

    @Override
    public synchronized void nativeKeyPressed(NativeKeyEvent e) {
        actionString.append(";KeyPressed-").append(NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public synchronized void nativeKeyReleased(NativeKeyEvent e) {
        actionString.append(";KeyReleased-").append(NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public synchronized void nativeMousePressed(NativeMouseEvent e) {
        actionString.append(";MousePressed-").append(e.getButton());
    }
    @Override
    public synchronized void nativeMouseReleased(NativeMouseEvent e) {
        actionString.append(";MouseReleased-").append(e.getButton());
    }
}



