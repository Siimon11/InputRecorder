import com.github.kwhat.jnativehook.GlobalScreen;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.event.InputEvent;

public class Main {
    static {
        OpenCV.loadLocally();
    }
    public static InputListener inputListener = new InputListener();
    public static SaveInput saveInput = new SaveInput(inputListener);
    public static PlayInput playInput = new PlayInput();
    public static void main(String[] args) throws InterruptedException {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        GlobalScreen.addNativeKeyListener(inputListener);
        GlobalScreen.addNativeMouseListener(inputListener);
        Mat image = Imgcodecs.imread("src/saveFiles/Kuva1.png");
        Mat template = Imgcodecs.imread("src/saveFiles/Template1.png");

        Mat result = new Mat();
        Imgproc.matchTemplate(image, template, result, Imgproc.TM_CCOEFF_NORMED);

        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double threshold = 0.9;
        if(mmr.maxVal >= threshold) {
            System.out.println("Template found at: " + mmr.maxLoc);
        } else {
            System.out.println("Template not found");
        }
    }
}