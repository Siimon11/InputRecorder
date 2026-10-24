import java.awt.*;
import java.io.*;

public class PlayInput implements Runnable{
    public int FPS;
    public Thread thread;
    public BufferedReader bufferedReader = null;
    private Actions actions;

    public PlayInput() {
        try {
            actions = new Actions();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void playInputs(String fileName) {
        assert(fileExists(fileName));
        try {
            bufferedReader = new BufferedReader(new FileReader("src/saveFiles/" + fileName + ".txt"));
            FPS = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        startThread();
    }
    private boolean fileExists(String fileName) {
        File file = new File("src/saveFiles/" + fileName + ".txt");
        return file.exists();
    }
    private void update() {
        try {
            String actionString = bufferedReader.readLine();
            if(actionString != null && !actionString.isEmpty()) {
                actions.playActions(actionString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (thread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
        }
    }
    private void startThread() {
        thread = new Thread(this);
        thread.start();
    }
}
