import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Map;

public class Actions {
    public static Robot robot;

    private Map<String, Integer> keyMap = Map.ofEntries(
            Map.entry("Escape", KeyEvent.VK_ESCAPE),
            Map.entry("Back Slash", KeyEvent.VK_BACK_SLASH),
            Map.entry("Backspace", KeyEvent.VK_BACK_SPACE),
            Map.entry("Tab", KeyEvent.VK_TAB),
            Map.entry("Equals", KeyEvent.VK_EQUALS),
            Map.entry("Open Bracket", KeyEvent.VK_OPEN_BRACKET),
            Map.entry("Close Bracket", KeyEvent.VK_CLOSE_BRACKET),
            Map.entry("Semicolon", KeyEvent.VK_SEMICOLON),
            Map.entry("Enter", KeyEvent.VK_ENTER),
            Map.entry("Back Quote", KeyEvent.VK_BACK_QUOTE),
            Map.entry("Quote", KeyEvent.VK_QUOTE),
            Map.entry("Slash", KeyEvent.VK_SLASH),
            Map.entry("Caps Lock", KeyEvent.VK_CAPS_LOCK),
            Map.entry("Shift", KeyEvent.VK_SHIFT),
            Map.entry("Control", KeyEvent.VK_CONTROL),
            Map.entry("Meta", KeyEvent.VK_META),
            Map.entry("Alt", KeyEvent.VK_ALT),
            Map.entry("Space", KeyEvent.VK_SPACE),
            Map.entry("Print Screen", KeyEvent.VK_PRINTSCREEN),
            Map.entry("Comma", KeyEvent.VK_COMMA),
            Map.entry("Period", KeyEvent.VK_PERIOD),
            Map.entry("Minus", KeyEvent.VK_MINUS),
            Map.entry("Page Up", KeyEvent.VK_PAGE_UP),
            Map.entry("Up", KeyEvent.VK_UP),
            Map.entry("Page Down", KeyEvent.VK_PAGE_DOWN),
            Map.entry("Left", KeyEvent.VK_LEFT),
            Map.entry("Down", KeyEvent.VK_DOWN),
            Map.entry("Right", KeyEvent.VK_RIGHT)
    );

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public Actions() throws AWTException {
    }

    public static void leftClick(int x, int y, int milliSeconds) {
        moveTo(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(milliSeconds);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
    public static void type(char c) {
        int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(c);
        if(Character.isUpperCase(c)) {
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            robot.keyRelease(KeyEvent.VK_SHIFT);
        } else {
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
        }
    }
    public static void writeString(String string) {
        for(int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(c);
            if(Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else {
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
            }
        }
    }
    public static void moveTo(final int x2, final int y2) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        final int x1 = mouseLocation.x;
        final int y1 = mouseLocation.y;
        int pixelJUmp = 5;
        final double lineLength = Math.sqrt(Math.pow(x2 - x1, 2)+Math.pow(y2-y1, 2));
        double dt = 0;
        while (dt < lineLength) {
            dt += pixelJUmp;
            final double t = dt/lineLength;
            final int dx = (int)((1-t) * x1 + t * x2);
            final int dy = (int)((1-t) * y1 + t * y2);
            robot.mouseMove(dx, dy);
            robot.delay(1);
        }
        robot.mouseMove(x2, y2);
    }
    public static void rightClick(int milliSeconds) {
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.delay(milliSeconds);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
    }
    public void playActions(String actionString) {
        String[] strings = actionString.split(";");
        String[] mousePosition = strings[0].split(",");
        int mouseX = Integer.parseInt(mousePosition[0]);
        int mouseY = Integer.parseInt(mousePosition[1]);
        robot.mouseMove(mouseX, mouseY);
        for(int i = 1; i < strings.length; i++) {
            playAction(strings[i]);
        }
    }
    private void playAction(String action) {
        String[] splitAction = action.split("-");
        char c = splitAction[1].charAt(0);

        if(splitAction[0].equals("KeyPressed")) {
            if(splitAction[1].length() > 1) {
                if(keyMap.containsKey(splitAction[1])) robot.keyPress(keyMap.get(splitAction[1]));
                else System.out.println(splitAction[1]);
            } else {
                robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
            }
        } else if (splitAction[0].equals("KeyReleased")){
            if(splitAction[1].length() > 1) {
                if(keyMap.containsKey(splitAction[1])) robot.keyRelease(keyMap.get(splitAction[1]));
                else System.out.println(splitAction[1]);
            } else {
                robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
            }
        } else if (splitAction[0].equals("MousePressed")){
            robot.mousePress((int)(1024*Math.pow(2, Integer.parseInt(splitAction[1])-1)));
        } else if (splitAction[0].equals("MouseReleased")){
            robot.mouseRelease((int)(1024*Math.pow(2, Integer.parseInt(splitAction[1])-1)));
        } else {
            System.out.println("Error: Invalid action string: " + splitAction[0]);
        }
    }
}
