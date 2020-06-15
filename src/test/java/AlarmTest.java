

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class AlarmTest  {

    System system = new System();

   /* public AlarmTest() {
        int[] alarmFuction = {1, 2, 5, 6};
        system.setFunctionNum(alarmFuction);
        system.alarm = new Alarm(system);
        system.alarmCustom = new AlarmCustom(system);
    }
*/
    @Test
    public void SetAlarmTest() {
        Alarm alarm = new Alarm(system);

        alarm.requestAlarmSettingMode(); // mode = 1;
        assert(alarm.getMode() == 1);

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
        assert(alarm.getAlarmList()[0].getTime().getCurrentTime().equals(time.getCurrentTime()));


        alarm.requestAlarmSettingMode(); // mode = 1;
        assert(alarm.getMode() == 1);

        // ### 시, 분, 초 경계값 확인
        // 순서대로 시, 분, 초 입력
        alarm.changeValue(-60); // 시
        alarm.changeType();
        alarm.changeValue(-60); // 분
        alarm.changeType();
        alarm.changeValue(-60); // 초


        //request Save
        alarm.requestSave(); // 0 0 0 저장

        // 순서대로 시, 분, 초 입력



        for (int i = 0 ; i < 3 ; i++)
            alarmSettingValue[i] = 0;


        Time time1 = new Time(2);
        time1.setTime(alarmSettingValue[0], alarmSettingValue[1], alarmSettingValue[2]);

        // 경계값으로 저장되었는지 확인
        assert(alarm.getAlarmList()[1].getTime().getCurrentTime().equals(time1.getCurrentTime()));

        //
        alarm.requestAlarmSettingMode(); // mode = 1;
        assert(alarm.getMode() == 1);

        // 순서대로 시, 분, 초 입력
        alarm.changeValue(60); // 시
        alarm.changeType();
        alarm.changeValue(60); // 분
        alarm.changeType();
        alarm.changeValue(60); // 초


        //request Save
        alarm.requestSave(); // 0 0 0 저장

        // 순서대로 시, 분, 초 입력
        alarmSettingValue[0] = 23;
        for (int i = 1 ; i < 3 ; i++)
            alarmSettingValue[i] = 59;

        Time time2 = new Time(2);
        time2.setTime(alarmSettingValue[0], alarmSettingValue[1], alarmSettingValue[2]);

        // 경계값으로 저장되었는지 확인
        assert(alarm.getAlarmList()[2].getTime().getCurrentTime().equals(time2.getCurrentTime()));

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

        assert(alarm.getAlarmList()[1].getTime().equals(time3)); // "3 3 3"
        assert(alarm.getAlarmList()[0].getTime().equals(time2));
    }

    @Test
    public void BeepAlarmTest() {
        Alarm alarm = new Alarm(system);
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
        java.lang.System.out.println(alarm.getAlarmList()[0].getTime().getCurrentTime());

        // 현재 시각과 알람 시간이 같으면
        if(timeKeeping.getCurTime().getCurrentTime().equals(alarm.getAlarmList()[0].getTime().getCurrentTime()))
        {
            system.beepBuzzer(alarm.getAlarmList()[0].getInterval(), alarm.getAlarmList()[0].getVolume()); // 버저 울리기.
            assertEquals(1, system.getStatus() & 1);
            // buzzer에서 interval과 volume을 get할 방법이 없음. -> getter로 신규 함수 추가해야함.
            assertEquals(1000, system.buzzer.getInterval());
            assertEquals(1.0, system.buzzer.getVolume());
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

        Alarm alarm = new Alarm(system);


        // size == 0
        alarm.requestAlarmSelectMode();
        assert(alarm.getMode() == 0); // 알람이 없어 모드가 전환 안되는지 확인

        alarm.movePointer(1);
        assertEquals(0, alarm.getAlarmPointer()); // 바로 리턴되서 변동 없음.

        // 중복 테스트
        Time time = new Time(2);
        time.setTime(1,1,1);
        alarm.addTimeToAlarmList(time);
        Time time2 = new Time(2);
        time2.setTime(1,1,1);
        alarm.addTimeToAlarmList(time2);
        assertEquals(1, alarm.getSize()); // 중복되면 바로 리턴되서 1에서 변동 없음.

        // ## size > 0

        alarm.requestAlarmSelectMode();
        assert(alarm.getMode() == 2); // 알람이 생기면 모드가 전환되는지 확인

        assertEquals(0, alarm.getAlarmPointer()); // 초기 포인터 = 0
        alarm.movePointer(1); // 포인터 +1
        assertEquals(0, alarm.getAlarmPointer()); // 알람 개수 : 1개  - 포인터 최대 0까지라 변동 없는지 확인
        alarm.movePointer(-1); // 포인터 -1
        assertEquals(0, alarm.getAlarmPointer()); // 알람 개수 : 1개  - 포인터 최소 0까지라 변동 없는지 확인

        // size = 2
        time2.setTime(2,2,2);
        alarm.addTimeToAlarmList(time2);
        java.lang.System.out.println(alarm.getSize());

        assertEquals(0, alarm.getAlarmPointer()); // 초기 포인터 = 0
        alarm.movePointer(1); // 포인터 +1
        assertEquals(1, alarm.getAlarmPointer()); // 알람 개수 : 2개  - 포인터 최대 1까지. 되는 지확인
        alarm.movePointer(1); // 포인터 +1
        assertEquals(1, alarm.getAlarmPointer()); // 알람 개수 : 2개  - 포인터 최대 1까지. 경계값 걸리는지

        alarm.movePointer(-1); // 포인터 -1
        assertEquals(0, alarm.getAlarmPointer()); // 알람 개수 : 2개  - 포인터 최소 0까지지. 포인터 -1 되는지 확인
        alarm.movePointer(-1); // 포인터 -1
        assertEquals(0, alarm.getAlarmPointer()); // 알람 개수 : 2개  - 포인터 최소 0까지라 변동 없는지 확인

        // size = 3
        Time time3 = new Time(2);
        time3.setTime(3,3,3);
        alarm.addTimeToAlarmList(time3);
        java.lang.System.out.println(alarm.getSize());

        assertEquals(0, alarm.getAlarmPointer()); // 초기 포인터 = 0
        alarm.movePointer(1); // 포인터 +1
        assertEquals(1, alarm.getAlarmPointer()); // 알람 개수 : 3개  - 포인터 최대 2까지. 포인터 +1 되는 지확인
        alarm.movePointer(1); // 포인터 +1
        assertEquals(2, alarm.getAlarmPointer()); // 알람 개수 : 3개  - 포인터 최대 2까지. 포인터 +1 되는 지확인
        alarm.movePointer(1); // 포인터 +1
        assertEquals(2, alarm.getAlarmPointer()); // 알람 개수 : 3개  - 포인터 최대 2까지. 경계값 걸리는지

        alarm.movePointer(-1); // 포인터 -1
        assertEquals(1, alarm.getAlarmPointer()); // 알람 개수 : 3개  - 포인터 최소 0까지지. 포인터 -1 되는지 확인
        alarm.movePointer(-1); // 포인터 -1
        assertEquals(0, alarm.getAlarmPointer()); // 알람 개수 : 3개  - 포인터 최소 0까지지. 포인터 -1 되는지 확인
        alarm.movePointer(-1); // 포인터 -1
        assertEquals(0, alarm.getAlarmPointer()); // 알람 개수 : 3개  - 포인터 최소 0까지라 변동 없는지 확인


        //size = 4
        Time time4 = new Time(2);
        time4.setTime(4,4,4);
        alarm.addTimeToAlarmList(time4);
        java.lang.System.out.println(alarm.getSize());

        assertEquals(0, alarm.getAlarmPointer()); // 초기 포인터 = 0
        alarm.movePointer(1); // 포인터 +1
        assertEquals(1, alarm.getAlarmPointer()); // 알람 개수 : 4개  - 포인터 최대 3까지. 포인터 +1 되는 지확인
        alarm.movePointer(1); // 포인터 +1
        assertEquals(2, alarm.getAlarmPointer()); // 알람 개수 : 4개  - 포인터 최대 3까지. 포인터 +1 되는 지확인
        alarm.movePointer(1); // 포인터 +1
        assertEquals(3, alarm.getAlarmPointer()); // 알람 개수 : 4개  - 포인터 최대 3까지. 포인터 +1 되는 지확인
        alarm.movePointer(1); // 포인터 +1
        assertEquals(3, alarm.getAlarmPointer()); // 알람 개수 : 4개  - 포인터 최대 3까지. 경계값 걸리는지

        alarm.movePointer(-1); // 포인터 -1
        assertEquals(2, alarm.getAlarmPointer()); // 알람 개수 : 4개  - 포인터 최소 0까지지. 포인터 -1 되는지 확인
        alarm.movePointer(-1); // 포인터 -1
        assertEquals(1, alarm.getAlarmPointer()); // 알람 개수 : 4개  - 포인터 최소 0까지지. 포인터 -1 되는지 확인
        alarm.movePointer(-1); // 포인터 -1
        assertEquals(0, alarm.getAlarmPointer()); // 알람 개수 : 4개  - 포인터 최소 0까지지. 포인터 -1 되는지 확인
        alarm.movePointer(-1); // 포인터 -1
        assertEquals(0, alarm.getAlarmPointer()); // 알람 개수 : 4개  - 포인터 최소 0까지라 변동 없는지 확인



        // size == 5
        Time time5 = new Time(2);
        time5.setTime(5,5,5);
        alarm.addTimeToAlarmList(time5);
        java.lang.System.out.println(alarm.getSize());

        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(4, alarm.getAlarmPointer()); // 경계값 확인
        alarm.movePointer(-1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer()); // 경계값 걸리는지


        // size == 6
        Time time6 = new Time(2);
        time6.setTime(6,6,6);
        alarm.addTimeToAlarmList(time6);
        java.lang.System.out.println(alarm.getSize());

        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(5, alarm.getAlarmPointer()); // 경게값 걸리는지
        alarm.movePointer(-1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer()); // 경계값 걸리는지

        // size == 7
        Time time7 = new Time(2);
        time7.setTime(7,7,7);
        alarm.addTimeToAlarmList(time7);
        java.lang.System.out.println(alarm.getSize());

        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(6, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(6, alarm.getAlarmPointer()); // 경계값 걸리는지

        alarm.movePointer(-1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer()); // 경계값 걸리는지

        // size == 8
        Time time8 = new Time(2);
        time8.setTime(8,8,8);
        alarm.addTimeToAlarmList(time8);
        java.lang.System.out.println(alarm.getSize());

        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(6, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(7, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(7, alarm.getAlarmPointer()); // 경계값 걸리는지
        alarm.movePointer(-1);
        assertEquals(6, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer()); // 경계값 걸리는지


        // size == 9
        Time time9 = new Time(2);
        time9.setTime(9,9,9);
        alarm.addTimeToAlarmList(time9);
        java.lang.System.out.println(alarm.getSize());

        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(6, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(7, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(8, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(8, alarm.getAlarmPointer()); // 경계값 걸리는지
        alarm.movePointer(-1);
        assertEquals(7, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(6, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer()); // 경계값 걸리는지


        // size == 10
        Time time10 = new Time(2);
        time10.setTime(10,10,10);
        alarm.addTimeToAlarmList(time10);

        java.lang.System.out.println(alarm.getSize());
        
        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(6, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(7, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(8, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(9, alarm.getAlarmPointer());
        alarm.movePointer(1);
        assertEquals(9, alarm.getAlarmPointer()); // 경계값 걸리는지
        alarm.movePointer(-1);
        assertEquals(8, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(7, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(6, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(5, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(4, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(3, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(2, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(1, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer());
        alarm.movePointer(-1);
        assertEquals(0, alarm.getAlarmPointer()); // 경계값 걸리는지
    }
}
