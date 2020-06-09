import org.junit.Test;

import java.util.Calendar;
import java.util.StringTokenizer;

import static org.junit.Assert.*;

public class D_dayTest {
    System system = new System();

    @Test
    public void setDdayTest() {
        D_day d_day = system.d_day;

        d_day.requestDdaySettingMode();

        String curDate = d_day.getD_dayDate().getCurrentDate();
        String splited[] = curDate.split(" ");

        StringTokenizer st = new StringTokenizer(curDate, " ");
        Calendar curDateCal = Calendar.getInstance();
        curDateCal.set(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()));

        //1년 뒤, 2개월 뒤, 3일 뒤로 저장
        d_day.changeValue(1);
        d_day.changeType();
        d_day.changeValue(2);
        d_day.changeType();
        d_day.changeValue(3);
        d_day.requestSave();

        Date date = d_day.getD_dayDate();
        int dday = d_day.getD_day();

        Calendar d_dayDateCal = Calendar.getInstance();
        d_dayDateCal.set(date.getYear(), date.getMonth()-1, date.getDay());

        assert(date.getYear() == (Integer.parseInt(splited[0]) + 1));
        assert(date.getMonth() == (Integer.parseInt(splited[1]) + 2));
        assert(date.getDay() == (Integer.parseInt(splited[2]) + 3));
        assert(dday == (d_dayDateCal.getTimeInMillis() - curDateCal.getTimeInMillis()) / (60*60*24*1000));
    }

    @Test
    public void borderDdayTest() {
        D_day d_day = system.d_day;

        Date date = new Date();
        date.setDate(2020, 6,9);
        d_day.setDate(date);
        java.lang.System.out.println(d_day.getD_day());

        system.timeKeeping.getCurTime().setTime(23, 59, 59);

        try {
            Thread.sleep(2000); // 1초가 흐른게 되네요
        } catch(InterruptedException e) {
            java.lang.System.out.println(e.getMessage());
        }

        assertTrue(system.border.getBorderState());
    }

    @Test
    public void stopDdayBorderTest() {
        D_day d_day = system.d_day;

        system.startBorder();
        assertTrue(system.border.getBorderState());

        system.stopBorder();
        assertFalse(system.border.getBorderState());
    }

    @Test
    public void deleteDdayTest() {
        D_day d_day = system.d_day;

        String curDate = d_day.getD_dayDate().getCurrentDate();
        String splited[] = curDate.split(" ");

        d_day.requestDdaySettingMode();

        //1년 뒤, 2개월 뒤, 3일 뒤로 저장
        d_day.changeValue(1);
        d_day.changeType();
        d_day.changeValue(2);
        d_day.changeType();
        d_day.changeValue(3);
        d_day.requestSave();

        d_day.requestDeleteDday();

        java.lang.System.out.println(d_day.getD_dayDate().getCurrentDate());

        assert(d_day.getD_dayDate().getYear() == Integer.parseInt(splited[0]));
        assert(d_day.getD_dayDate().getMonth() == Integer.parseInt(splited[1]));
        assert(d_day.getD_dayDate().getDay() == Integer.parseInt(splited[2]));
        assert(d_day.getD_day() == -1);
    }
}