import javax.swing.*;

public class GUI extends JFrame {

    final static int FRAME_WIDTH = 1000;
    final static int FRAME_HEIGHT = 550;

    protected System system;
    public TimekeepingView timekeepingView;
    public StopwatchView stopwatchView;
    public TimerView timerView;
    public DDayView d_dayView;
    public AlarmView alarmView;
    public AlarmCustomView alarmCustomView;
    public TimekeepingView functionSelectingView;

    public GUI(System system) {
        setTitle("Alarm Customizing Clock System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);

        this.system = system;
        timekeepingView = new TimekeepingView(system);
        functionSelectingView = new TimekeepingView(system);
        stopwatchView = new StopwatchView(system);
        timerView = new TimerView(system);
        d_dayView = new DDayView(system);
        alarmView = new AlarmView(system);
        alarmCustomView = new AlarmCustomView(system);

        setView(timekeepingView);
    }


    /**
     * set View
     * @param view: JLayeredPane
     */
    public void setView(JLayeredPane view) {
        setContentPane(view);
    }

}
