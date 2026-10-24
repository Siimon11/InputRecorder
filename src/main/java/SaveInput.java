import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveInput implements Runnable {
    public final int FPS = 60;
    public Thread thread;
    public BufferedWriter bufferedWriter = null;
    private InputListener inputListener;

    public SaveInput(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    public void recordInputs(String fileName) {
        assert createFile(fileName);
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("src/saveFiles/" + fileName + ".txt", true));
            bufferedWriter.write(String.valueOf(FPS));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startThread();
    }

    public boolean createFile(String fileName) {
        try {
            File file = new File("src/saveFiles/" + fileName + ".txt");
            if(!file.createNewFile()) {
                System.out.println("File:" + fileName + " already exists.");
                return true;
            } else {
                System.out.println("File was created successfully.");
                return true;
            }
        } catch (IOException e) {
            System.out.println("Failed to create a file.");
            e.printStackTrace();
        }
        return false;
    }

    public void update() {
        try {
            bufferedWriter.write(MouseInfo.getPointerInfo().getLocation().x +
                    "," + MouseInfo.getPointerInfo().getLocation().y + inputListener.getActionString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputListener.resetActionString();
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
    public void startThread() {
        thread = new Thread(this);
        thread.start();
    }
}
