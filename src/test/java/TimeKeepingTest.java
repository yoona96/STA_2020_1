import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeKeepingTest {
    System system = new System();

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
    }

    // DisplayTimeSet는 system에서 확인

    @Test
    public void setDisplayTest() {
        system.nextFunction();
        assert (system.getSelectedFid() == 2);
    }
}