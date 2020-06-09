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
        timekeeping.requestTimeSettingMode();
        String time = timekeeping.getCurTime().getCurrentTime();
        String date = timekeeping.getCurDate().getCurrentDate();
        int dayOfTheWeek;
        for (int i = 0; i < 5; i++) {
            timekeeping.changeValue(2);
            timekeeping.changeType();
        }
        timekeeping.changeValue(2);
        timekeeping.requestSave();

        String splitedTime[] = time.split(" ");
        int splitedTimeInt[] = new int[3];
        for (int i = 0; i < 3; i++)
            splitedTimeInt[i] = Integer.parseInt(splitedTime[i]) + 2;
        String splitedDate[] = date.split(" ");
        int splitedDateInt[] = new int[3];
        for (int i = 0; i < 3; i++)
            splitedDateInt[i] = Integer.parseInt(splitedDate[i]) + 2;

        time = splitedTimeInt[0] + " " + splitedTimeInt[1] + " " + splitedTimeInt[2];
        date = splitedDateInt[0] + " " + splitedDateInt[1] + " " + splitedDateInt[2];

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
        try {
            java.util.Date date1 = dateFormat.parse(date);
            calendar.setTime(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);

        assert (timekeeping.getCurTime().getCurrentTime().equals(time));
        assert (timekeeping.getCurDate().getCurrentDate().equals(date));
        assert (timekeeping.getDayOfTheWeek() == dayOfTheWeek);
    }

    // DisplayTimeSet는 system에서 확인

    @Test
    public void setDisplayTest() {
        system.nextFunction();
        assert (system.getSelectedFid() == 2);
    }
}