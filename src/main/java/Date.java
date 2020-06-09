import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

/**
 * @author Yoonseop Shin
 */
public class Date {

    // 년, 월의 상위, 하위 한계값
    public final int YEAR_TOP_LIMIT = 2099;
    public final int YEAR_BOTTON_LIMIT = 2020;
    public final int MONTH_TOP_LIMIT = 12;
    public final int MONTH_BOTTON_LIMIT = 1;
    // 인덱스 1 ~ 12가 월에 대응하는 일 수. 인덱스 0는 TimeSettingMode일 때 순환적인 처리를 위해 '일'의 최솟값 1을 넣어놨다.
    public final static int numOfDays[] = {1,31,28,31,30,31,30,31,31,30,31,30,31};

    private int year;
    private int month;
    private int day;
    private Object lock; // 쓰레드 race condition 방지 위한 lock

    public void setDate(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
    }

    public Date() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd");
        String curTime = format.format(java.lang.System.currentTimeMillis());

        String splited[] = curTime.split(" ");

        year = Integer.parseInt(splited[0]);
        month = Integer.parseInt(splited[1]);
        day = Integer.parseInt(splited[2]);
        lock = new Object();
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public String getCurrentDate() {
        String currentDate;
        synchronized(lock) {
            currentDate = Integer.toString(year) + " " + Integer.toString(month) + " " + Integer.toString(day);
        }
        return currentDate;
    }

    public void deleteDday(String str) {
        StringTokenizer st = new StringTokenizer(str, " ");
        synchronized(lock) {
            year = Integer.parseInt(st.nextToken());
            month = Integer.parseInt(st.nextToken());
            day = Integer.parseInt(st.nextToken());
        }
    }

    public void raiseDate() {
        if(++day == numOfDays[month]) { day = 0; ++month; }
        if(month > 12) { month = 0; ++year; }
    }
}