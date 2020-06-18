import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AlarmView extends DefaultLayout {
    final static int ALARM_WIDTH = 300;
    final static int ALARM_HEIGHT = 45;
    final static int ALARM_LIST_HEIGHT = 135;
    final static int DISPLAY_AMT = 3;

    JPanel curAlarmPanel;
    JPanel alarmListPanel;
    JPanel borderPanel;

    public AlarmView(System system) {
        super(system);

        curAlarmPanel = new JPanel();
        curAlarmPanel.setLayout(null);
        curAlarmPanel.setBounds(350, 180, ALARM_WIDTH, ALARM_HEIGHT);
        curAlarmPanel.setVisible(true);

        displaySegment(350, 180, ALARM_WIDTH, ALARM_HEIGHT, "000000");

        alarmListPanel = new JPanel();
        alarmListPanel.setLayout(null);
        alarmListPanel.setBounds(350, 240, ALARM_WIDTH, ALARM_LIST_HEIGHT);
        alarmListPanel.setVisible(true);

        displaySegment(350, 240, ALARM_WIDTH, ALARM_LIST_HEIGHT / DISPLAY_AMT, "  NONE");
        displaySegment(350, 240 + ALARM_LIST_HEIGHT / DISPLAY_AMT, ALARM_WIDTH
                , ALARM_LIST_HEIGHT / DISPLAY_AMT, "  NONE");
        displaySegment(350, 240 + 2 * ALARM_LIST_HEIGHT / DISPLAY_AMT, ALARM_WIDTH
                , ALARM_LIST_HEIGHT / DISPLAY_AMT, "  NONE");

        borderPanel = new JPanel();
        borderPanel.setVisible(false);
        borderPanel.setBorder(new LineBorder(Color.GRAY, 5));
        borderPanel.setBounds(350 - 5, 180 - 5,
                (ALARM_WIDTH + 10) / 3, ALARM_HEIGHT + 10);

        add(alarmListPanel, new Integer(1));
        add(curAlarmPanel, new Integer(1));
        add(borderPanel, new Integer(2));
    }

    public void setBorderPanel(int idx) {
        int x = 350 - 5;
        int y = 240 - 5;
        int w = ALARM_WIDTH + 10;
        int h = ALARM_HEIGHT + 10;
        borderPanel.setBounds(x, y + ALARM_HEIGHT * idx, w, h);
    }

    public void setAlarm(String str) {
        displaySegment(350, 180, ALARM_WIDTH, ALARM_HEIGHT, str, layer++);
    }

    public void setAlarmList(String str) {
        if (str.substring(0, 6).equals("      "))
            displaySegment(350, 240, ALARM_WIDTH, ALARM_HEIGHT, "  NONE", layer++);
        else
            displaySegment(350, 240, ALARM_WIDTH, ALARM_HEIGHT,
                    str.substring(0, 6), layer++);

        if (str.substring(6, 12).equals("      "))
            displaySegment(350, 240 + ALARM_HEIGHT, ALARM_WIDTH, ALARM_HEIGHT, "  NONE", layer++);
        else
            displaySegment(350, 240 + ALARM_HEIGHT, ALARM_WIDTH, ALARM_HEIGHT,
                    str.substring(6, 12), layer++);

        if (str.substring(12, 18).equals("      "))
            displaySegment(350, 240 + ALARM_HEIGHT * 2, ALARM_WIDTH, ALARM_HEIGHT, "  NONE", layer++);
        else
            displaySegment(350, 240 + ALARM_HEIGHT * 2, ALARM_WIDTH, ALARM_HEIGHT,
                    str.substring(12, 18), layer++);
    }

}
