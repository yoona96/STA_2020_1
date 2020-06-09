/**
 * @author Yoonseop Shin
 */
public class Border {

    private boolean borderState;

    public Border(System system) {

    }

    public boolean getBorderState() {
        return borderState;
    }

    public void stopBorder() {
        borderState = false;
    }

    public void startBorder() {
        borderState = true;
    }
}