import org.junit.Test;

import static org.junit.Assert.*;

public class StopwatchTest {
    System system = new System();

    @Test
    public void startStopwatchTest() {
        Stopwatch stopwatch = system.stopwatch;

        stopwatch.requestStartStopwatch();

        try {
            Thread.sleep(3100);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        Time time = stopwatch.getStopwatch();

        String timeStr = time.getCurrentTime();
        String splitedTime[] = timeStr.split(" ");

        assert(splitedTime[2].equals("3"));
    }

    @Test
    public void pauseStopwatchTest() {
        Stopwatch stopwatch = system.stopwatch;
        stopwatch.requestStartStopwatch();
        try {
            Thread.sleep(3100);
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }
        stopwatch.requestPauseStopwatch();
        try {
            Thread.sleep(2100);
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }
        Time time = stopwatch.getStopwatch();
        String timeStr = time.getCurrentTime();
        String splitedTime[] = timeStr.split(" ");
        assert(splitedTime[2].equals("3"));
    }

    @Test
    public void resetStopwatchTest() {
        Stopwatch stopwatch = system.stopwatch;
        stopwatch.requestStartStopwatch();
        try {
            Thread.sleep(3100);
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }
        Time time = stopwatch.getStopwatch();
        String timeStr = time.getCurrentTime();
        String splitedTime[] = timeStr.split(" ");
        assert(splitedTime[2].equals("3"));
        stopwatch.requestResetStopwatch(); //reset
        timeStr = time.getCurrentTime();
        splitedTime = timeStr.split(" ");
        assert(splitedTime[2].equals("0"));
    }

    @Test
    public void recordStopwatchTest() {
        Stopwatch stopwatch = system.stopwatch;
        stopwatch.requestStartStopwatch();
        for(int i=0; i<5; i++) {
            try {
                Thread.sleep(1100);
            } catch(InterruptedException e) {
                java.lang.System.out.println(e.getMessage());
            }
            stopwatch.requestSaveRecord();
        }
        String[] rec = stopwatch.getStopwatchRecord();
        for(int i=0; i<5; i++)
            assert(rec[i].equals("0 0 " + (i+1)));
    }

    @Test
    public void controlStopwatchRecord() {
        Stopwatch stopwatch = system.stopwatch;

        stopwatch.movePointer(1);
        assert(stopwatch.getRecordPointer()==1);
    }
}