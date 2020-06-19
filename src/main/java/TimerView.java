import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TimerView extends DefaultLayout {
    final static int TIMER_WIDTH = 330;
    final static int TIMER_HEIGHT = 170;

    JPanel timerPanel;
    JPanel borderPanel;

    public TimerView(System system) {
        super(system);

        timerPanel = new JPanel();
        timerPanel.setLayout(null);
        timerPanel.setBounds(335, 190, TIMER_WIDTH, TIMER_HEIGHT);
        timerPanel.setVisible(true);

        displaySegment(335, 190, TIMER_WIDTH, TIMER_HEIGHT, "000000");

        borderPanel = new JPanel();
        borderPanel.setVisible(false);
        borderPanel.setBorder(new LineBorder(Color.GRAY, 5));
        borderPanel.setBounds(timerPanel.getX() - 5, timerPanel.getY() - 5,
                (timerPanel.getWidth() + 10) / 3, timerPanel.getHeight() + 10);

        add(timerPanel, Integer.valueOf(1));
        add(borderPanel, Integer.valueOf(2));
    }

    public void setHour(String str) {
        displaySegment(335, 190, TIMER_WIDTH / 3, TIMER_HEIGHT, str, layer++); }
    public void setMinute(String str) {
        displaySegment(335 + TIMER_WIDTH / 3, 190, TIMER_WIDTH / 3, TIMER_HEIGHT, str, layer++); }
    public void setSecond(String str) {
        displaySegment(335 + 2 * TIMER_WIDTH / 3, 190, TIMER_WIDTH / 3, TIMER_HEIGHT, str, layer++); }
}
