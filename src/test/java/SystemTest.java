import org.junit.Test;

public class SystemTest  {

    System system = new System();

    @Test
    public void ChangeScreenTest() {
        int[] functionNum = system.getFunctionNum();
        int functionNumIdx = system.getFunctionNumIdx();
        int selectedFid;

        functionNumIdx = (functionNumIdx + 1) % 4;
        selectedFid = functionNum[functionNumIdx];
        system.nextFunction();
        assert(selectedFid == system.getSelectedFid());
        functionNumIdx = (functionNumIdx + 1) % 4;
        selectedFid = functionNum[functionNumIdx];
        system.nextFunction();
        assert(selectedFid == system.getSelectedFid());
    }

    @Test
    public void TimeoutTest() {
        TimeKeeping timeKeeping = system.timeKeeping;
        Stopwatch stopwatch = system.stopwatch;
        Timer timer = system.timer;
        D_day d_day = system.d_day;
        Alarm alarm = null;
        AlarmCustom alarmCustom = null;

        // 타임 키핑
        timeKeeping.requestTimeSettingMode();
        system.startCheckTimeOut();
        // (임시로 10분을 10초로 바꾸고) 10초 정지 후 어떻게 되는 지 확인

        // 스톱워치
        java.lang.System.out.println(system.getSelectedFid());
        stopwatch.requestRecordCheckMode();
        system.startCheckTimeOut();
// (임시로 10분을 10초로 바꾸고) 10초 정지 후 어떻게 되는 지 확인
        java.lang.System.out.println(system.getMode());
        java.lang.System.out.println(system.getSelectedFid());

        // 타이머
        java.lang.System.out.println(system.getSelectedFid());
        timer.requestTimerSettingMode();
        system.startCheckTimeOut();
// (임시로 10분을 10초로 바꾸고) 10초 정지 후 어떻게 되는 지 확인
        java.lang.System.out.println(system.getMode());
        java.lang.System.out.println(system.getSelectedFid());

        // 디데이
        java.lang.System.out.println(system.getSelectedFid());
        d_day.requestDdaySettingMode();
        system.startCheckTimeOut();
// (임시로 10분을 10초로 바꾸고) 10초 정지 후 어떻게 되는 지 확인
        java.lang.System.out.println(system.getMode());

        // 알람
        int[] alarmFunction = {1, 3, 5, 6};
        system.setFunctionNum(alarmFunction);
        system.alarm = new Alarm(system);
        system.alarmCustom = new AlarmCustom(system);
        alarm = system.alarm;
        alarmCustom = system.alarmCustom;

        java.lang.System.out.println(system.getSelectedFid());
        alarm.requestAlarmSettingMode();
        system.startCheckTimeOut();
// (임시로 10분을 10초로 바꾸고) 10초 정지 후 어떻게 되는 지 확인
        java.lang.System.out.println(system.getMode());
        java.lang.System.out.println(system.getSelectedFid());

        // 알람 커스텀
        java.lang.System.out.println(system.getSelectedFid());
        alarmCustom.requestIntervalSettingMode();
        system.startCheckTimeOut();
// (임시로 10분을 10초로 바꾸고) 10초 정지 후 어떻게 되는 지 확인
        system.getMode();
        java.lang.System.out.println(system.getSelectedFid());
    }

    @Test
    public void CancelTest() {
        TimeKeeping timeKeeping = system.timeKeeping;
        Stopwatch stopwatch = system.stopwatch;
        Timer timer = system.timer;
        D_day d_day = system.d_day;
        Alarm alarm = null;
        AlarmCustom alarmCustom = null;

        //타임 키핑
        timeKeeping.requestTimeSettingMode();
        timeKeeping.cancel();
        assert(timeKeeping.getMode() == 0);

        // 스톱워치
        stopwatch.requestRecordCheckMode();
        stopwatch.cancel();
        assert(stopwatch.getMode() == 0);

        //타이머
        timer.requestTimerSettingMode();
        timer.cancel();
        assert(timer.getMode() == 0);

        //디데이
        d_day.requestDdaySettingMode();
        d_day.cancel();
        assert(d_day.getMode() == 0);

        int[] alarmFunction = {1, 3, 5, 6};
        system.setFunctionNum(alarmFunction);
        system.alarm = new Alarm(system);
        system.alarmCustom = new AlarmCustom(system);
        alarm = system.alarm;
        alarmCustom = system.alarmCustom;

        // 알람
        alarm.requestAlarmSettingMode();
        alarm.cancel();
        assert(alarm.getMode() == 0);

        // 알람 커스텀
        alarmCustom.requestIntervalSettingMode();
        alarmCustom.cancel();
        assert(alarmCustom.getMode() == 0);

        alarmCustom.requestVolumeSettingMode();
        alarmCustom.cancel();
        assert(alarmCustom.getMode() == 0);

        alarmCustom.requestAlarmSelectMode();
        alarmCustom.cancel();
        assert(alarmCustom.getMode() == 0);
    }
}