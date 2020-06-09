import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class StopwatchView extends DefaultLayout {
    final static int STOPWATCH_WIDTH = 300;
    final static int STOPWATCH_HEIGHT = 45;
    final static int STOPWATCH_LIST_HEIGHT = 135;
    final static int DISPLAY_AMT = 3;

    JPanel curStopwatchPanel;
    JPanel stopwatchListPanel;
    JPanel borderPanel;

    public StopwatchView(System system) {
        super(system);
        curStopwatchPanel = new JPanel();
        curStopwatchPanel.setLayout(null);
        curStopwatchPanel.setBounds(350, 180, STOPWATCH_WIDTH, STOPWATCH_HEIGHT);
        curStopwatchPanel.setVisible(true);

        displaySegment(350, 180, STOPWATCH_WIDTH, STOPWATCH_HEIGHT, "000000");

        stopwatchListPanel = new JPanel();
        stopwatchListPanel.setLayout(null);
        stopwatchListPanel.setBounds(350, 240, STOPWATCH_WIDTH, STOPWATCH_LIST_HEIGHT);
        stopwatchListPanel.setVisible(true);

        displaySegment(350, 240, STOPWATCH_WIDTH, STOPWATCH_LIST_HEIGHT / DISPLAY_AMT, "  NONE");
        displaySegment(350, 240 + STOPWATCH_LIST_HEIGHT / DISPLAY_AMT, STOPWATCH_WIDTH
                , STOPWATCH_LIST_HEIGHT / DISPLAY_AMT, "  NONE");
        displaySegment(350, 240 + 2 * STOPWATCH_LIST_HEIGHT / DISPLAY_AMT, STOPWATCH_WIDTH
                , STOPWATCH_LIST_HEIGHT / DISPLAY_AMT, "  NONE");

        borderPanel = new JPanel();
        borderPanel.setVisible(false);
        borderPanel.setBorder(new LineBorder(Color.GRAY, 5));
        borderPanel.setBounds(stopwatchListPanel.getX() - 5, stopwatchListPanel.getY() - 5,
                STOPWATCH_WIDTH + 10, STOPWATCH_LIST_HEIGHT + 10);

        add(stopwatchListPanel, new Integer(1));
        add(curStopwatchPanel, new Integer(1));
        add(borderPanel, new Integer(2));
    }

    public void setStopwatch(String str) { displaySegment(350, 180, STOPWATCH_WIDTH, STOPWATCH_HEIGHT, str, layer++); }

    public void setStopwatchList(String str) {
        if (str.substring(0, 6).equals("      "))
            displaySegment(350, 240, STOPWATCH_WIDTH, STOPWATCH_HEIGHT, "  NONE", layer++);
        else
            displaySegment(350, 240, STOPWATCH_WIDTH, STOPWATCH_HEIGHT,
                    str.substring(0, 6), layer++);

        if (str.substring(6, 12).equals("      "))
            displaySegment(350, 240 + STOPWATCH_HEIGHT, STOPWATCH_WIDTH, STOPWATCH_HEIGHT, "  NONE", layer++);
        else
            displaySegment(350, 240 + STOPWATCH_HEIGHT, STOPWATCH_WIDTH, STOPWATCH_HEIGHT,
                    str.substring(6, 12), layer++);

        if (str.substring(12, 18).equals("      "))
            displaySegment(350, 240 + STOPWATCH_HEIGHT * 2, STOPWATCH_WIDTH, STOPWATCH_HEIGHT, "  NONE", layer++);
        else
            displaySegment(350, 240 + STOPWATCH_HEIGHT * 2, STOPWATCH_WIDTH, STOPWATCH_HEIGHT,
                    str.substring(12, 18), layer++);
    }
}
