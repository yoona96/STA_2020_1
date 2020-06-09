import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {
    System system = new System();

    @Test
    public void setTimerTest() {
        Timer timer = system.timer;

        timer.requestTimerSettingMode();

        for(int i = 0; i<2; i++) {
            timer.changeValue(i+1);
            timer.changeType();
        }
        timer.changeValue(3);
        timer.requestSave();

        Time time = timer.getTimer();

        String timeStr = time.getCurrentTime();
        String splitedTime[] = timeStr.split(" ");

        assert(splitedTime[0].equals("1"));
        assert(splitedTime[1].equals("2"));
        assert(splitedTime[2].equals("3"));
    }

    @Test
    public void startTimerTest() {
        Timer timer = system.timer;
        timer.requestTimerSettingMode();

        timer.changeType();
        timer.changeType();
        timer.changeValue(5); // Timer를 5초로 Setting
        timer.requestSave();

        timer.requestStartTimer();

        try {
            Thread.sleep(3100); // 3초가 흐른게 되네요
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }

        Time time = timer.getTimer();

        String timeStr = time.getCurrentTime();
        String splitedTime[] = timeStr.split(" ");

        assert(splitedTime[2].equals("2"));

    }

    @Test
    public void beepTimerTest() {
        Timer timer = system.timer;
        timer.requestTimerSettingMode();

        timer.changeType();
        timer.changeType();
        timer.changeValue(3); // Timer를 3초로 Setting
        timer.requestSave();

        timer.requestStartTimer();

        try {
            Thread.sleep(3100); // 3초가 흐른게 되네요
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }

        assertTrue(timer.system.buzzer.isBuzzerState());
    }

    @Test
    public void resetTimerTest() {
        Timer timer = system.timer;

        timer.requestTimerSettingMode();

        timer.changeType();
        timer.changeType();
        timer.changeValue(5); // Timer를 5초로 Setting
        timer.requestSave();

        timer.requestStartTimer();

        Time time = timer.getTimer();

        String timeStr = time.getCurrentTime();
        String splitedTime[] = timeStr.split(" ");

        assert(splitedTime[2].equals("5"));

        timer.requestResetTimer();

        time = timer.getTimer();

        timeStr = time.getCurrentTime();
        splitedTime = timeStr.split(" ");

        assert(splitedTime[0].equals("0"));
        assert(splitedTime[1].equals("0"));
        assert(splitedTime[2].equals("0"));
    }

    @Test
    public void pauseTimerTest() {
        Timer timer = system.timer;

        timer.requestTimerSettingMode();

        timer.changeType();
        timer.changeType();
        timer.changeValue(5); // Timer를 5초로 Setting
        timer.requestSave();

        timer.requestStartTimer();

        try {
            Thread.sleep(1100); // 1초가 흐른게 되네요
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }

        timer.requestPauseTimer();

        try {
            Thread.sleep(1100); // 1초가 흐른게 되네요
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }

        Time time = timer.getTimer();

        String timeStr = time.getCurrentTime();
        String splitedTime[] = timeStr.split(" ");

        assert(splitedTime[2].equals("4"));
    }

    @Test
    public void stopTimerBuzzer() {
        Timer timer = system.timer;

        timer.system.buzzer.beepBuzzer(1, 1);
        timer.system.buzzer.stopBuzzer();

        assertFalse(timer.system.buzzer.isBuzzerState());
    }
}