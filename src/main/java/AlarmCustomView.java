import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AlarmCustomView extends DefaultLayout {
    final static int ALARM_LIST_WIDTH = 300;
    final static int ALARM_LIST_HEIGHT = 135;
    final static int ALARM_WIDTH = 60;
    final static int ALARM_HEIGHT = 60;
    final static String VOLUME_IMG_NAME = "soundimage.jpg";
    final static String INTERVAL_IMG_NAME = "intervalimage.jpg";
    final static int DISPLAY_AMT = 3;
    final String NONE = "  NONE";

    ImageIcon volumeImage;
    ImageIcon intervalImage;
    JLabel volumeLabel;
    JPanel volumeImgPanel;
    JPanel volumeControlPanel;
    JLabel intervalLabel;
    JPanel intervalImgPanel;
    JPanel intervalControlPanel;
    JPanel alarmListPanel;
    JPanel borderPanel;

    public AlarmCustomView(System system) {
        super(system);

        volumeImage = new ImageIcon(getClass().getResource(VOLUME_IMG_NAME));
        volumeImage = new ImageIcon(volumeImage.getImage()
                .getScaledInstance(ALARM_WIDTH, ALARM_HEIGHT, Image.SCALE_SMOOTH));
        volumeLabel = new JLabel(volumeImage);
        volumeLabel.setBounds(0, 0, ALARM_WIDTH, ALARM_HEIGHT);
        volumeLabel.setVisible(true);

        volumeImgPanel = new JPanel();
        volumeImgPanel.setLayout(null);
        volumeImgPanel.setVisible(true);
        volumeImgPanel.setBounds(370, 165, ALARM_WIDTH, ALARM_HEIGHT);
        volumeImgPanel.add(volumeLabel);

        volumeControlPanel = new JPanel();
        volumeControlPanel.setLayout(null);
        volumeControlPanel.setVisible(true);
        volumeControlPanel.setBounds(430, 165, ALARM_WIDTH, ALARM_HEIGHT);

        displaySegment(430, 165, ALARM_WIDTH, ALARM_HEIGHT, "0");

        intervalImage = new ImageIcon(getClass().getResource(INTERVAL_IMG_NAME));
        intervalImage = new ImageIcon(intervalImage.getImage()
                .getScaledInstance(ALARM_WIDTH, ALARM_HEIGHT, Image.SCALE_SMOOTH));
        intervalLabel = new JLabel(intervalImage);
        intervalLabel.setBounds(0, 0, ALARM_WIDTH, ALARM_HEIGHT);
        intervalLabel.setVisible(true);

        intervalImgPanel = new JPanel();
        intervalImgPanel.setLayout(null);
        intervalImgPanel.setVisible(true);
        intervalImgPanel.setBounds(490, 165, ALARM_WIDTH, ALARM_HEIGHT);
        intervalImgPanel.add(intervalLabel);

        intervalControlPanel = new JPanel();
        intervalControlPanel.setLayout(null);
        intervalControlPanel.setVisible(true);
        intervalControlPanel.setBounds(550, 165, ALARM_WIDTH, ALARM_HEIGHT);

        displaySegment(550, 165, ALARM_WIDTH, ALARM_HEIGHT, "0");

        alarmListPanel = new JPanel();
        alarmListPanel.setLayout(null);
        alarmListPanel.setBounds(350, 240, ALARM_LIST_WIDTH, ALARM_LIST_HEIGHT);
        alarmListPanel.setVisible(true);

        displaySegment(350, 240, ALARM_LIST_WIDTH, ALARM_LIST_HEIGHT / DISPLAY_AMT, NONE);
        displaySegment(350, 240 + ALARM_LIST_HEIGHT / DISPLAY_AMT, ALARM_LIST_WIDTH
                , ALARM_LIST_HEIGHT / DISPLAY_AMT, NONE);
        displaySegment(350, 240 + 2 * ALARM_LIST_HEIGHT / DISPLAY_AMT, ALARM_LIST_WIDTH
                , ALARM_LIST_HEIGHT / DISPLAY_AMT, NONE);

        borderPanel = new JPanel();
        borderPanel.setVisible(false);
        borderPanel.setBorder(new LineBorder(Color.GRAY, 5));
        borderPanel.setBounds(350 - 5, 240 - 5,
                ALARM_LIST_WIDTH + 10, ALARM_LIST_HEIGHT + 10);

        add(intervalControlPanel, new Integer(1));
        add(intervalImgPanel, new Integer(1));
        add(volumeControlPanel, new Integer(1));
        add(alarmListPanel, new Integer(1));
        add(volumeImgPanel, new Integer(1));
        add(borderPanel, new Integer(2));
    }
    public void setAlarmInterval(String str) {
        displaySegment(550, 165, ALARM_WIDTH, ALARM_HEIGHT, str, layer++);
    }

    public void setAlarmVolume(String str)
    {
        displaySegment(430, 165, ALARM_WIDTH, ALARM_HEIGHT, str, layer++);
    }

    public void setBorderPanel(int idx) {
        int x = 350 - 5;
        int y = 240 - 5;
        int w = ALARM_LIST_WIDTH + 10;
        int h = ALARM_LIST_HEIGHT / 3 + 10;
        borderPanel.setBounds(x, y + ALARM_LIST_HEIGHT / 3 * idx, w, h);
    }

    public void setAlarmList(String str) {
        if (str.substring(0, 6).equals("      "))
            displaySegment(350, 240, ALARM_LIST_WIDTH, ALARM_LIST_HEIGHT / 3, NONE, layer++);
        else
            displaySegment(350, 240, ALARM_LIST_WIDTH, ALARM_LIST_HEIGHT / 3,
                    str.substring(0, 6), layer++);

        if (str.substring(6, 12).equals("      "))
            displaySegment(350, 240 + ALARM_LIST_HEIGHT / 3, ALARM_LIST_WIDTH, ALARM_LIST_HEIGHT / 3, NONE, layer++);
        else
            displaySegment(350, 240 + ALARM_LIST_HEIGHT / 3, ALARM_LIST_WIDTH, ALARM_LIST_HEIGHT / 3,
                    str.substring(6, 12), layer++);

        if (str.substring(12, 18).equals("      "))
            displaySegment(350, 240 + ALARM_LIST_HEIGHT / 3 * 2, ALARM_LIST_WIDTH, ALARM_LIST_HEIGHT / 3, NONE, layer++);
        else
            displaySegment(350, 240 + ALARM_LIST_HEIGHT / 3 * 2, ALARM_LIST_WIDTH, ALARM_LIST_HEIGHT / 3,
                    str.substring(12, 18), layer++);
    }
}
