
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class AlarmCustomTest {
    System system = new System();

    public AlarmCustomTest() {
        int[] alarmFuction = {1, 2, 5, 6};
        system.setFunctionNum(alarmFuction);
        system.alarm = new Alarm(system);
        system.alarmCustom = new AlarmCustom(system);
    }

    @Test
    public void ControlAlarmListTest() {
        // alarm 리스트 설정
        Time time = new Time(2);
        time.setTime(1,2,3);
        system.alarm.addTimeToAlarmList(time);

        Time time2 = new Time(2);
        time2.setTime(1,2,4);
        system.alarm.addTimeToAlarmList(time2);

        Time time3 = new Time(2);
        time3.setTime(1,2,5);
        system.alarm.addTimeToAlarmList(time3);

        Time time4 = new Time(2);
        time4.setTime(1,2,6);
        system.alarm.addTimeToAlarmList(time4);


        system.alarmCustom.requestAlarmSelectMode();
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        assertEquals(0, system.alarmCustom.getAlarm().getAlarmPointer());

        java.lang.System.out.println("size: " + system.alarm.getSize());


        // 알람 리스트 잘 확인 되는지 확인.
        system.alarmCustom.changeValue2(1);
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        java.lang.System.out.println(system.alarmCustom.getAlarm().getSegmentPointer());
        assertEquals(1, system.alarmCustom.getAlarm().getAlarmPointer()); // 0에서 포인터가 하나 증가하는 지 확인

        system.alarmCustom.changeValue2(1);
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        java.lang.System.out.println(system.alarmCustom.getAlarm().getSegmentPointer());
        assertEquals(2, system.alarmCustom.getAlarm().getAlarmPointer()); // 1에서 포인터가 하나 증가하는 지 확인

        system.alarmCustom.changeValue2(1);
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        java.lang.System.out.println(system.alarmCustom.getAlarm().getSegmentPointer());
        assertEquals(3, system.alarmCustom.getAlarm().getAlarmPointer()); // 2에서 포인터가 하나 증가하는 지 확인

        system.alarmCustom.changeValue2(1);
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        java.lang.System.out.println(system.alarmCustom.getAlarm().getSegmentPointer());
        assertEquals(3, system.alarmCustom.getAlarm().getAlarmPointer()); // 3에서 포인터가 하나 증가하는 지 확인 (마지막이라 안 올라감)


        system.alarmCustom.changeValue2(-1);
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        java.lang.System.out.println(system.alarmCustom.getAlarm().getSegmentPointer());
        assertEquals(2, system.alarmCustom.getAlarm().getAlarmPointer()); // 3에서 포인터가 하나 감소하는 지 확인

        system.alarmCustom.changeValue2(-1);
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        java.lang.System.out.println(system.alarmCustom.getAlarm().getSegmentPointer());
        assertEquals(1, system.alarmCustom.getAlarm().getAlarmPointer()); // 2에서 포인터가 하나 감소하는 지 확인

        system.alarmCustom.changeValue2(-1);
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        java.lang.System.out.println(system.alarmCustom.getAlarm().getSegmentPointer());
        assertEquals(0, system.alarmCustom.getAlarm().getAlarmPointer()); // 1에서 포인터가 하나 감소하는 지 확인

        system.alarmCustom.changeValue2(-1);
        java.lang.System.out.println(system.alarmCustom.getAlarm().getAlarmPointer());
        java.lang.System.out.println(system.alarmCustom.getAlarm().getSegmentPointer());
        assertEquals(0, system.alarmCustom.getAlarm().getAlarmPointer()); // 0에서 포인터가 하나 감소하는 지 확인 (0 미만으로 안 내려감)
    }

    @Test
    public void SetAlarmIntervalTest() {

        AlarmCustom alarmCustom = system.alarmCustom;

        // alarm 리스트 설정
        Time time = new Time(2);
        time.setTime(1,2,3);
        system.alarm.addTimeToAlarmList(time);


        alarmCustom.requestAlarmSelectMode(); // 알람 선택 모드
        alarmCustom.getAlarm().movePointer(1); // 포인터 +1

        alarmCustom.requestIntervalSettingMode(); // 알람 인터벌 설정 모드
        alarmCustom.changeType(); // type  0: 기본 -> 1 : 인터벌

        assertEquals(1, alarmCustom.getType());
        assertEquals(2, alarmCustom.getCustomSettingValue()[1]); // 초기값 2인지 확인

        //인터벌이 범위 내에서 잘 바뀌는 지 확인.
        alarmCustom.changeValue(1);
        assertEquals(3, alarmCustom.getCustomSettingValue()[1]); // 1이 올라갔는지 확인

        alarmCustom.changeValue(1);
        assertEquals(3, alarmCustom.getCustomSettingValue()[1]); // 한계값 3에 막혀 안 올라가는지 확인

        alarmCustom.changeValue(1);
        assertEquals(3, alarmCustom.getCustomSettingValue()[1]); // 또 한계값 3에 막혀 안 올라가는지 확인

        alarmCustom.changeValue(-1);
        assertEquals(2, alarmCustom.getCustomSettingValue()[1]); // 3에서 값이 1 내려가는지 확인

        alarmCustom.changeValue(-1);
        assertEquals(1, alarmCustom.getCustomSettingValue()[1]); // 2에서 값이 1 내려가는지 확인

        alarmCustom.changeValue(-1);
        assertEquals(1, alarmCustom.getCustomSettingValue()[1]); // 한계값 1에 막혀 값이 안 내려가는지 확인

        alarmCustom.changeValue(-1);
        assertEquals(1, alarmCustom.getCustomSettingValue()[1]); // 또 한계값 1에 막혀 값이 안 내려가는지 확인

        alarmCustom.changeValue(1);
        assertEquals(2, alarmCustom.getCustomSettingValue()[1]); // 1에서 값이 1 올라가는지 확인

        alarmCustom.changeValue(1);
        assertEquals(3, alarmCustom.getCustomSettingValue()[1]); // 2에서 값이 1 올라가는지 확인


    }

    @Test
    public void SetAlarmVolumeTest() {
        AlarmCustom alarmCustom = system.alarmCustom;

        Time time = new Time(2);
        time.setTime(1,1,2);
        system.alarm.addTimeToAlarmList(time);

        alarmCustom.requestAlarmSelectMode(); // 알람 선택 모드
        alarmCustom.getAlarm().movePointer(1); // 포인터 +1

        alarmCustom.requestIntervalSettingMode(); // 알람 인터벌 설정 모드
        alarmCustom.changeType(); // type  0: 기본 -> 1 : 인터벌
        alarmCustom.changeValue(1);

        alarmCustom.requestVolumeSettingMode(); // 알람 볼륨 설정 모드
        alarmCustom.changeType(); // type  1: 인터벌 -> 2 : 볼륨


        assertEquals(2, alarmCustom.getCustomSettingValue()[2]); // 초기 볼륨값 2인지 확인


        // 범위에 맞게 볼륨값이 잘 바뀌는 지 확인
        alarmCustom.changeValue(1);
        assertEquals(3, alarmCustom.getCustomSettingValue()[2]); // 2에서 값이 1 올라가는지 확인

        alarmCustom.changeValue(1);
        assertEquals(4, alarmCustom.getCustomSettingValue()[2]); // 3에서 값이 1 올라가는지 확인

        alarmCustom.changeValue(1);
        assertEquals(4, alarmCustom.getCustomSettingValue()[2]); // 한계값 4에 막혀 값이 안 올라가는지 확인

        alarmCustom.changeValue(-1);
        assertEquals(3, alarmCustom.getCustomSettingValue()[2]); // 4에서 값이 1 내려가는지 확인

        alarmCustom.changeValue(-1);
        assertEquals(2, alarmCustom.getCustomSettingValue()[2]); // 3에서 값이 1 내려가는지 확인


        alarmCustom.changeValue(-1);
        assertEquals(1, alarmCustom.getCustomSettingValue()[2]); // 2에서 값이 1 내려가는지 확인

        alarmCustom.changeValue(-1);
        assertEquals(0, alarmCustom.getCustomSettingValue()[2]); // 1에서 값이 1 내려가는지 확인

        alarmCustom.changeValue(-1);
        assertEquals(0, alarmCustom.getCustomSettingValue()[2]); // 한계값 0에 막혀 값이 안 내려가는지 확인

        alarmCustom.changeValue(1);
        assertEquals(1, alarmCustom.getCustomSettingValue()[2]); // 0에서 값이 1 올라가는지 확인

        alarmCustom.changeValue(1);
        assertEquals(2, alarmCustom.getCustomSettingValue()[2]); // 1에서 값이 1 올라가는지 확인

        // 저장이 잘 되는지 확인
        alarmCustom.setCustom();

        java.lang.System.out.println(system.alarm.getAlarmList()[0].getInterval());
        java.lang.System.out.println(system.alarm.getAlarmList()[0].getVolume());
    }




}