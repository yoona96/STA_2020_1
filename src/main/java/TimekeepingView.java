import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TimekeepingView extends DefaultLayout {

    final static int ALARM_WIDTH = 50;
    final static int ALARM_HEIGHT = 50;
    final static int ALARM_SIZE_WIDTH = 25;
    final static int ALARM_SIZE_HEIGHT = 25;
    final static String ALARM_IMG_NAME = "alarmlayout.jpg";
    final static int D_DAY_WIDTH = 235;
    final static int D_DAY_HEIGHT = 50;
    final static int CUR_TIME1_WIDTH = 270;
    final static int CUR_TIME1_HEIGHT = 65;
    final static int CUR_TIME2_WIDTH = 100;
    final static int CUR_TIME2_HEIGHT = 50;
    final static int DATE_WIDTH = 250;
    final static int DATE_HEIGHT = 50;
    final static int DAYOFWEEK_WIDTH = 120;
    final static int DAYOFWEEK_HEIGHT = 50;

    JPanel d_dayPanel;          // D-day
    JPanel curTimePanel1;       // 시/분
    JPanel curTimePanel2;       // 초
    JPanel datePanel;           // 날짜
    JPanel dayofweekPanel;      // 요일
    JPanel alarmPanel;
    JPanel alarmNumPanel;
    ImageIcon alarmImage;
    JLabel alarmLabel;
    JPanel borderPanel;

    public TimekeepingView(System system) {
        super(system);

        alarmImage = new ImageIcon(getClass().getResource(ALARM_IMG_NAME));
        alarmImage = new ImageIcon(alarmImage.getImage()
                .getScaledInstance(ALARM_WIDTH, ALARM_HEIGHT, Image.SCALE_SMOOTH));
        alarmLabel = new JLabel(alarmImage);
        alarmLabel.setBounds(0, 0, ALARM_WIDTH, ALARM_HEIGHT);

        alarmPanel = new JPanel();
        alarmPanel.setLayout(null);
        alarmPanel.setBounds(335, 175, ALARM_WIDTH, ALARM_HEIGHT);
        alarmPanel.add(alarmLabel);
        alarmPanel.setVisible(true);

        alarmNumPanel = new JPanel();
        alarmNumPanel.setLayout(null);
        alarmNumPanel.setBounds(385, 200, ALARM_SIZE_WIDTH, ALARM_SIZE_HEIGHT);
        alarmNumPanel.setVisible(true);

        displaySegment(385, 200, ALARM_SIZE_WIDTH, ALARM_SIZE_HEIGHT, " ");

        d_dayPanel = new JPanel();
        d_dayPanel.setLayout(null);
        d_dayPanel.setBounds(420, 175, D_DAY_WIDTH, D_DAY_HEIGHT);
        d_dayPanel.setVisible(true);

        displaySegment(420, 175, D_DAY_WIDTH, D_DAY_HEIGHT, "   ");

        curTimePanel1 = new JPanel();
        curTimePanel1.setLayout(null);
        curTimePanel1.setBounds(310, 235, CUR_TIME1_WIDTH, CUR_TIME1_HEIGHT);
        curTimePanel1.setVisible(true);

        displaySegment(310, 235, CUR_TIME1_WIDTH, CUR_TIME1_HEIGHT, "    ");

        curTimePanel2 = new JPanel();
        curTimePanel2.setLayout(null);
        curTimePanel2.setBounds(600, 250, CUR_TIME2_WIDTH, CUR_TIME2_HEIGHT);
        curTimePanel2.setVisible(true);

        displaySegment(600, 250, CUR_TIME2_WIDTH, CUR_TIME2_HEIGHT, "  ");

        datePanel = new JPanel();
        datePanel.setLayout(null);
        datePanel.setBounds(310, 310, DATE_WIDTH, DATE_HEIGHT);
        datePanel.setVisible(true);

        displaySegment(310, 310, DATE_WIDTH, DATE_HEIGHT, "      ");

        dayofweekPanel = new JPanel();
        dayofweekPanel.setLayout(null);
        dayofweekPanel.setBounds(570, 310, DAYOFWEEK_WIDTH, DAYOFWEEK_HEIGHT);
        dayofweekPanel.setVisible(true);

        displaySegment(570, 310, DAYOFWEEK_WIDTH, DAYOFWEEK_HEIGHT, "   ");

        borderPanel = new JPanel();
        borderPanel.setVisible(false);
        borderPanel.setBorder(new LineBorder(Color.GRAY, 5));
        borderPanel.setBounds(curTimePanel1.getX() - 5, curTimePanel1.getY() - 5,
                (curTimePanel1.getWidth() + 10) / 2, curTimePanel1.getHeight() + 10);

        add(alarmNumPanel, new Integer(1));
        add(datePanel, new Integer(1));
        add(d_dayPanel, new Integer(1));
        add(curTimePanel1, new Integer(1));
        add(curTimePanel2, new Integer(1));
        add(alarmPanel, new Integer(1));
        add(dayofweekPanel, new Integer(1));
        add(borderPanel, new Integer(2));
    }

    public void setAlarmNum(String str) {
        if (str.equals("10")) {
            add(new SegmentDisplay(385 + PADDING, 200 + PADDING,
                    ALARM_SIZE_WIDTH - 2 * PADDING, ALARM_SIZE_HEIGHT - 2 * PADDING, 't')
                    , new Integer(layer));
        }
        else
            displaySegment(385, 200, ALARM_SIZE_WIDTH, ALARM_SIZE_HEIGHT, str, layer++);
    }

    public void setdDay(String str) { displaySegment(420, 175, D_DAY_WIDTH, D_DAY_HEIGHT, str, layer++); }

    public void setCurTime1(String str) {
        displaySegment(310, 235, CUR_TIME1_WIDTH, CUR_TIME1_HEIGHT, str, layer++);
    }

    public void setCurTime2(String str) {
        displaySegment(600, 250, CUR_TIME2_WIDTH, CUR_TIME2_HEIGHT, str, layer++);
    }

    public void setDate(String str) {
        displaySegment(310, 310, DATE_WIDTH, DATE_HEIGHT, str, layer++);
    }

    public void setDayofweek(String str) { displaySegment(570, 310, DAYOFWEEK_WIDTH, DAYOFWEEK_HEIGHT, str, layer++); }

    public void setHour(String str) {
        displaySegment(310, 235, CUR_TIME1_WIDTH / 2, CUR_TIME1_HEIGHT, str, layer++);
    }

    public void setMinute(String str) {
        displaySegment(310 + CUR_TIME1_WIDTH / 2, 235, CUR_TIME1_WIDTH / 2, CUR_TIME1_HEIGHT, str, layer++);
    }

    public void setBorderPanel(int idx) {
        int w = CUR_TIME1_WIDTH / 4 + 10, h = CUR_TIME1_HEIGHT + 10;
        int x = 310 - 5 + CUR_TIME1_WIDTH / 4, y = 235 - 5;
        borderPanel.setBounds(x + CUR_TIME1_WIDTH / 4 * idx, y, w, h);
    }
}
