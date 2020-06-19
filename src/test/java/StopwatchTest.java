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
            e.printStackTrace(java.lang.System.out);
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

        stopwatch.requestStartStopwatch();
        try {
            Thread.sleep(3100);
            stopwatch.requestPauseStopwatch();
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }
        time = stopwatch.getStopwatch();
        timeStr = time.getCurrentTime();
        splitedTime = timeStr.split(" ");
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

        stopwatch.requestRecordCheckMode();
        assertTrue(stopwatch.getMode() == 0); // 기록 없을 때 기록 확인 모드로 전환되지 않는지 확인

        stopwatch.requestStartStopwatch();

        // 기록 3개 저장
        for(int i=0; i<3; ++i) {
            try {
                Thread.sleep(1100);
                stopwatch.requestSaveRecord();
            } catch (InterruptedException e) {
                e.printStackTrace(java.lang.System.out);
            }
        }

        // 기록 3개 찍혔는지 확인
        String records[] = stopwatch.getStopwatchRecord();
        int cnt=0;
        for(int i=0; i<10; ++i) {
            if(records[i] == null) break;
            ++cnt;
        }
        assertTrue(cnt == 3);

        stopwatch.requestRecordCheckMode();
        assertTrue(stopwatch.getRecordPointer() == 0); // requestRecordCheckMode시 pointer가 0으로 초기화 되는지 확인

        stopwatch.movePointer(-1);
        assertTrue(stopwatch.getRecordPointer() == 0); // 0 아래로 내려가는지 확인

        stopwatch.movePointer(1);
        assertTrue(stopwatch.getRecordPointer() == 1);

        stopwatch.movePointer(1);
        assertTrue(stopwatch.getRecordPointer() == 2);

        stopwatch.movePointer(1);
        java.lang.System.out.println(stopwatch.getRecordPointer());
        assertTrue(stopwatch.getRecordPointer() == 2); // 기록이 3개이므로, pointer가 2 초과하여 증가하지 않는지 확인
    }
}