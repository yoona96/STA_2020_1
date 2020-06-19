import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DDayView extends DefaultLayout {

    final static int D_DATE_WIDTH = 330;
    final static int D_DATE_HEIGHT = 100;
    final static int D_DAY_WIDTH = 250;
    final static int D_DAY_HEIGHT = 60;

    JPanel datePanel;
    JPanel d_dayPanel;
    JPanel borderPanel;

    public DDayView(System system) {
        super(system);

        datePanel = new JPanel();
        datePanel.setLayout(null);
        datePanel.setBounds(335, 190, D_DATE_WIDTH, D_DATE_HEIGHT);
        datePanel.setVisible(true);

        displaySegment(335, 190, D_DATE_WIDTH, D_DATE_HEIGHT, "  NONE");

        d_dayPanel = new JPanel();
        d_dayPanel.setLayout(null);
        d_dayPanel.setBounds(375, 310, D_DAY_WIDTH, D_DAY_HEIGHT);
        d_dayPanel.setVisible(true);

        displaySegment(375, 310, D_DAY_WIDTH, D_DAY_HEIGHT, "000");

        borderPanel = new JPanel();
        borderPanel.setVisible(false);
        borderPanel.setBorder(new LineBorder(Color.GRAY, 5));
        borderPanel.setBounds(335 - 5, 190 - 5,
                (D_DATE_WIDTH + 10) / 3, D_DATE_HEIGHT + 10);

        add(datePanel, Integer.valueOf(1));
        add(d_dayPanel, Integer.valueOf(1));
        add(borderPanel, Integer.valueOf(2));
    }

    public void setYear(String str) {
        displaySegment(335, 190, D_DATE_WIDTH / 3, D_DATE_HEIGHT, str, layer++);
    }

    public void setMonth(String str) {
        displaySegment(335 + D_DATE_WIDTH / 3, 190, D_DATE_WIDTH / 3, D_DATE_HEIGHT, str, layer++);
    }

    public void setDate(String str) {
        displaySegment(335 + D_DATE_WIDTH / 3 * 2, 190, D_DATE_WIDTH / 3, D_DATE_HEIGHT, str, layer++);
    }

    public void setDday(String str) {
        displaySegment(375, 310, D_DAY_WIDTH, D_DAY_HEIGHT, str, layer++);
    }


}
