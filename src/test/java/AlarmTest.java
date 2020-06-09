

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class AlarmTest  {

    System system = new System();

    public AlarmTest() {
        int[] alarmFuction = {1, 2, 5, 6};
        system.setFunctionNum(alarmFuction);
        system.alarm = new Alarm(system);
        system.alarmCustom = new AlarmCustom(system);
    }

    @Test
    public void SetAlarmTest() {
        Alarm alarm = system.alarm;

        alarm.requestAlarmSettingMode(); // mode = 1;

        // 순서대로 시, 분, 초 입력
        alarm.changeValue(2); // 시
        alarm.changeType();
        alarm.changeValue(2); // 분
        alarm.changeType();
        alarm.changeValue(2); // 초

        //request Save
        alarm.requestSave();

        // 테스트 비교용
        int alarmSettingValue[] = {-1,-1,-1};
        Time time = new Time(2);

        // 순서대로 시, 분, 초 입력
        for (int i = 0 ; i < 3 ; i++)
            alarmSettingValue[i] = 2;


        time.setTime(alarmSettingValue[0], alarmSettingValue[1], alarmSettingValue[2]);

        // 잘 저장 되었는 지 확인.
        assert(alarm.getAlarmList()[0].getAlarmTime().getCurrentTime().equals(time.getCurrentTime()));
    }


    @Test
    public void DeleteAlarmTest() {
        Alarm alarm = new Alarm(system);

        alarm.movePointer(1);
        int i = alarm.getAlarmPointer();
        assertEquals(0, i);

        Time time = new Time(2);
        time.setTime(1,1,1);
        alarm.addTimeToAlarmList(time);
        Time time2 = new Time(2);
        time2.setTime(2,2,2);
        alarm.addTimeToAlarmList(time2);
        Time time3 = new Time(2);
        time3.setTime(3,3,3);
        alarm.addTimeToAlarmList(time3);

        alarm.requestDeleteAlarm(); // " 2 2 2" 삭제

        assert(alarm.getAlarmList()[1].getAlarmTime().equals(time3)); // "3 3 3"
        assert(alarm.getAlarmList()[0].getAlarmTime().equals(time2));
    }

    @Test
    public void BeepAlarmTest() {
        Alarm alarm = system.alarm;
        TimeKeeping timeKeeping = system.timeKeeping;
        Buzzer buzzer = system.buzzer;

        Time time = new Time(2);
        time.setTime(23,59,59);

        alarm.addTimeToAlarmList(time);

        // 알람 커스텀된 간격, 볼륨 설정
        alarm.getAlarmList()[0].setInterval(2);
        alarm.getAlarmList()[0].setVolume(2);

        //현재 시간 임의 설정
        timeKeeping.requestTimeSettingMode();

        // 시간, 분, 초, 순으로 설정
        for(int i = 0 ; i < 5 ; i++) {
            timeKeeping.changeValue(60); // 알맞게 변경하세요
            timeKeeping.changeType();
        }

        timeKeeping.changeValue(60);
        timeKeeping.requestSave(); // "23 59 59"

        java.lang.System.out.println(timeKeeping.getCurTime().getCurrentTime());
        java.lang.System.out.println(alarm.getAlarmList()[0].getAlarmTime().getCurrentTime());

        // 현재 시각과 알람 시간이 같으면
        if(timeKeeping.getCurTime().getCurrentTime().equals(alarm.getAlarmList()[0].getAlarmTime().getCurrentTime()))
        {
            system.beepBuzzer(alarm.getAlarmList()[0].getInterval(), alarm.getAlarmList()[0].getVolume()); // 버저 울리기.
            assertEquals(1, system.getStatus() & 1);
            // buzzer에서 interval과 volume을 get할 방법이 없음. -> getter로 신규 함수 추가해야함.
            assertEquals(1000, system.buzzer.getInterval());
            assertEquals(0.07, system.buzzer.getVolume());
        }
    }

    @Test
    public void StopAlarmBuzzerTest() {
        Alarm alarm = system.alarm;
        system.beepBuzzer(1, 1);
        system.updateStatus();

        assertEquals(0, system.getStatus() & 0);
    }

    // DisplayAlarmList는 system에서 확인

    @Test
    public void ControlAlarmListTest() {

        Alarm alarm = system.alarm;

        // size == 0
        alarm.movePointer(1);
        assertEquals(0, alarm.getAlarmPointer()); // 바로 리턴되서 변동 없음.

        // 중복 테스트
        Time time = new Time(2);
        time.setTime(1,1,1);
        alarm.addTimeToAlarmList(time);
        Time time2 = new Time(2);
        time2.setTime(1,1,1);
        alarm.addTimeToAlarmList(time2);
        assertEquals(1, alarm.getSize()); // 바로 리턴되서 변동 없음.

        // size > 0
        time2.setTime(2,2,2);
        alarm.addTimeToAlarmList(time2);
        Time time3 = new Time(2);
        time3.setTime(3,3,3);
        alarm.addTimeToAlarmList(time3);
        Time time4 = new Time(2);
        time4.setTime(4,4,4);
        alarm.addTimeToAlarmList(time4);

        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer());
    }
}