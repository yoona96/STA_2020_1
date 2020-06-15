import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class TimeKeepingTest {
    System system = new System();

    public TimeKeepingTest() {
        int[] alarmFuction = {1, 2, 5, 6};
        system.setFunctionNum(alarmFuction);
        system.alarm = new Alarm(system);
        system.alarmCustom = new AlarmCustom(system);
    }

    @Test
    public void setTimeTest() {
        TimeKeeping timekeeping = system.timeKeeping;

        Time time = new Time(1);
        time.setTime(21, 55, 30);
        Date date = new Date();
        date.setDate(2020, 6, 9);

        timekeeping.setCurDate(date);
        timekeeping.setCurTime(time);
        timekeeping.getCurTime().startTime();

        timekeeping.requestTimeSettingMode();
        for (int i = 0; i < 5; i++) {
            timekeeping.changeValue(2);
            timekeeping.changeType();
        }

        timekeeping.changeValue(2);

        timekeeping.requestSave();

        assert (time.toString().equals("23 57 32"));
        assert (date.getCurrentDate().equals("2022 8 11"));
        assert (timekeeping.getDayOfTheWeek() == 5);

        //경계값 넘어가는지 확인
        timekeeping.requestTimeSettingMode();
        for (int i = 0; i < 5; i++) {
            timekeeping.changeValue(5000);
            timekeeping.changeType();
        }
        timekeeping.changeValue(5000);
        timekeeping.requestSave();
        assert (time.toString().equals("23 59 59"));
        assert (date.getCurrentDate().equals("2099 12 31"));

        //조작한 월에 따라서 일이 넘어가지 않는지(월에 맞지 않는 일) 확인, 예: 2월
        timekeeping.requestTimeSettingMode();
        for (int i=0; i<3; i++)
            timekeeping.changeType();
        timekeeping.changeValue(0);
        timekeeping.changeType();
        timekeeping.changeValue(-10);
        timekeeping.changeType();
        timekeeping.changeValue(30);
        timekeeping.requestSave();
        assert(date.getCurrentDate().equals("2099 2 28"));
    }

    // DisplayTimeSet는 system에서 확인

    @Test
    public void setDisplayTest() {
        system.nextFunction();
        assert (system.getSelectedFid() == 2);

        // 알람 동기화 되는지
        system.alarm.requestAlarmSettingMode(); // mode = 1;

        // 순서대로 시, 분, 초 입력
        system.alarm.changeValue(2); // 시
        system.alarm.changeType();
        system.alarm.changeValue(2); // 분
        system.alarm.changeType();
        system.alarm.changeValue(2); // 초

        //request Save
        system.alarm.requestSave();

        system.timeKeeping.setAlarmCnt(system.alarm.getSize());
        assert (system.timeKeeping.getAlarmCnt() == 1);

        // d_day 동기화 되는지
        system.d_day.requestDdaySettingMode();

        String curDate = system.d_day.getD_dayDate().getCurrentDate();
        String splited[] = curDate.split(" ");

        StringTokenizer st = new StringTokenizer(curDate, " ");
        Calendar curDateCal = Calendar.getInstance();
        curDateCal.set(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()));

        // 1년 뒤, 2개월 뒤, 3일 뒤로 저장
        system.d_day.changeValue(1);
        system.d_day.changeType();
        system.d_day.changeValue(2);
        system.d_day.changeType();
        system.d_day.changeValue(3);
        system.d_day.requestSave();

        Date date = system.d_day.getD_dayDate();
        int dday = system.d_day.getD_day();

        Calendar d_dayDateCal = Calendar.getInstance();
        d_dayDateCal.set(date.getYear(), date.getMonth()-1, date.getDay());

        assert(dday == system.d_day.getD_day());
    }
}