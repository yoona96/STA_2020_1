import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class TimeKeeping extends Function {

    final static int FID = 1;
    final static String[] DAY_OF_THE_WEEK = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    private final int TYPE_SIZE = 6;

    // 0 -> Default Mode, 1 -> TimeSettingMode
    private Time curTime;
    private Date curDate;
    private int d_day;
    private int alarmCnt;
    private int dayOfTheWeek; // 1 : 일요일 7 : 토요일

    // TimeSettingMode일 때, 사용자가 변화시키는 값을 임시 저장하는 배열
    private int timeSettingValue[] = {-1, -1, -1, -1, -1, -1};

    public TimeKeeping(System system) {
        d_day = -1;
        alarmCnt = 0;
        mode = 0;

        curDate = new Date();
        setDayOfTheWeek(); // curDate 초기화 후 호출해야함.

        curTime = new Time(1);

        curTime.setDateListener(() -> {
            curDate.raiseDate();
            setDayOfTheWeek();

            if(system.d_day.getD_day() != -1) {

                system.d_day.setD_day(system.d_day.getD_day() - 1);
                if(system.d_day.getD_dayDate().getCurrentDate().equals(curDate.getCurrentDate())) {
                    system.startBorder();
                    Date date = new Date();
                    date.setDate(0, 0, 0);
                    system.d_day.setDate(date);
                    if (system.GUI != null) {
                        system.GUI.d_dayView.setYear("  ");
                        system.GUI.d_dayView.setMonth("NO");
                        system.GUI.d_dayView.setDate("NE");
                        system.GUI.d_dayView.setDday("000");
                    }
                }
                else {
                    String curDate = system.d_day.getD_dayDate().getCurrentDate();
                    StringTokenizer st = new StringTokenizer(curDate, " ");
                    if (system.GUI != null) {
                        system.GUI.d_dayView.setYear(String.format("%02d", Integer.parseInt(st.nextToken()) % 100));
                        system.GUI.d_dayView.setMonth(String.format("%02d", Integer.parseInt(st.nextToken())));
                        system.GUI.d_dayView.setDate(String.format("%02d", Integer.parseInt(st.nextToken())));
                        system.GUI.d_dayView.setDday(String.format("%03d", system.d_day.getD_day()));
                    }
                }
            }
        });

        curTime.setSecondListener(() -> {
            if (mode == 0) {
                String str = curTime.getCurrentTime();
                StringTokenizer st = new StringTokenizer(str, " ");
                if (system.GUI != null) {
                    system.GUI.timekeepingView.setCurTime1(String.format("%02d", Integer.parseInt(st.nextToken()))
                            + String.format("%02d", Integer.parseInt(st.nextToken())));
                    system.GUI.timekeepingView.setCurTime2(String.format("%02d", Integer.parseInt(st.nextToken())));
                    system.GUI.timekeepingView.setDayofweek(DAY_OF_THE_WEEK[dayOfTheWeek - 1]);
                    str = curDate.getCurrentDate();
                    st = new StringTokenizer(str, " ");
                    system.GUI.timekeepingView.setDate(String.format("%02d", Integer.parseInt(st.nextToken().substring(2, 4)))
                            + String.format("%02d", Integer.parseInt(st.nextToken()))
                            + String.format("%02d", Integer.parseInt(st.nextToken())));
                }

                // D-day
                if (system.GUI != null) {
                    if (system.d_day != null) {
                        int dDay = system.d_day.getD_day();
                        if (dDay == -1)
                            system.GUI.timekeepingView.setdDay("000");
                        else {
                            if (dDay > 999)
                                system.GUI.timekeepingView.setdDay("999");
                            else
                                system.GUI.timekeepingView.setdDay(String.format("%03d", dDay));
                        }
                    }
                    else
                        system.GUI.timekeepingView.setdDay("000");
                }

                // Alarm
                if (system.alarm == null) {
                    if (system.GUI != null) {
                        system.GUI.timekeepingView.setAlarmNum(" ");
                    }
                    return;
                }

                int alarmNum = system.alarm.getSize();
                AlarmData[] alarmList = system.alarm.getAlarmList();
                for (int i = 0; i < alarmNum; i++) {
                    if (curTime.equals(alarmList[i].getTime())) { // 선택된 알람 인터벌이랑 볼륨
                        system.beepBuzzer(alarmList[i].getInterval(), alarmList[i].getVolume());
                        break;
                    }
                }

                if (system.GUI != null) {
                    if (system.alarm != null)
                        system.GUI.timekeepingView.setAlarmNum(String.valueOf(alarmNum));
                    else
                        system.GUI.timekeepingView.setAlarmNum(" ");
                }
            }
        });

        curTime.startTime();
        type = 0;
    }

    public int getAlarmCnt() {
        return alarmCnt;
    }

    public void setAlarmCnt(int cnt) {
        this.alarmCnt = cnt;
    }

    public void setCurTime(Time curTime) {
        this.curTime = curTime;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    @Override
    public void changeMode(int _mode) {
        mode ^= 1;

        // 현재 시, 분, 초, 년, 월, 일로 timeSettingValue 초기화
        if (mode == 1) {
            this.type = 0;

            String currentTime = curTime.getCurrentTime();
            String currentDate = curDate.getCurrentDate();

            String ymd[] = currentDate.split(" ");
            String hms[] = currentTime.split(" ");
            timeSettingValue[0] = Integer.parseInt(hms[0]);
            timeSettingValue[1] = Integer.parseInt(hms[1]);
            timeSettingValue[2] = Integer.parseInt(hms[2]);
            timeSettingValue[3] = Integer.parseInt(ymd[0]);
            timeSettingValue[4] = Integer.parseInt(ymd[1]);
            timeSettingValue[5] = Integer.parseInt(ymd[2]);
        } else {
            // timeSettingValue -1로 비활성화
            for (int i = 0; i < TYPE_SIZE; ++i)
                timeSettingValue[i] = -1;
        }
    }

    public void changeValue(int diff) {
        timeSettingValue[type] += diff;

        // 각 type 값 검사
        switch (type) {
            case 0:
                if (timeSettingValue[type] < curTime.TIME_BOTTOM_LIMIT)
                    timeSettingValue[type] = curTime.TIME_BOTTOM_LIMIT;
                else if (timeSettingValue[type] > curTime.HOUR_TOP_LIMIT)
                    timeSettingValue[type] = curTime.HOUR_TOP_LIMIT;
                break;
            case 1:
                if (timeSettingValue[type] < curTime.TIME_BOTTOM_LIMIT)
                    timeSettingValue[type] = curTime.TIME_BOTTOM_LIMIT;
                else if (timeSettingValue[type] > curTime.MINUTE_TOP_LIMIT)
                    timeSettingValue[type] = curTime.MINUTE_TOP_LIMIT;
                break;
            case 2:
                if (timeSettingValue[type] < curTime.TIME_BOTTOM_LIMIT)
                    timeSettingValue[type] = curTime.TIME_BOTTOM_LIMIT;
                else if (timeSettingValue[type] > curTime.SECOND_TOP_LIMIT)
                    timeSettingValue[type] = curTime.SECOND_TOP_LIMIT;
                break;
            case 3:
                if (timeSettingValue[type] < curDate.YEAR_BOTTON_LIMIT)
                    timeSettingValue[type] = curDate.YEAR_BOTTON_LIMIT;
                else if (timeSettingValue[type] > curDate.YEAR_TOP_LIMIT)
                    timeSettingValue[type] = curDate.YEAR_TOP_LIMIT;
                break;
            case 4:
                if (timeSettingValue[type] < curDate.MONTH_BOTTON_LIMIT)
                    timeSettingValue[type] = curDate.MONTH_BOTTON_LIMIT;
                else if (timeSettingValue[type] > curDate.MONTH_TOP_LIMIT)
                    timeSettingValue[type] = curDate.MONTH_TOP_LIMIT;
                break;
            case 5:
                if(timeSettingValue[4] == 2) {
                    int temp, year = timeSettingValue[3];
                    int tmpNumOfDay;
                    temp = year % 4;
                    if (temp == 0) {
                        temp = year % 100;
                        if (temp == 0) {
                            temp = year % 400;
                            if (temp == 0) {
                                tmpNumOfDay = 29;
                            }
                            else
                                tmpNumOfDay = 28;
                        }
                        else {
                            tmpNumOfDay = 29;
                        }
                    } else
                        tmpNumOfDay = 28;

                    if(timeSettingValue[type] < Date.numOfDays[0])
                        timeSettingValue[type] = Date.numOfDays[0];
                    else if(timeSettingValue[type] > tmpNumOfDay)
                        timeSettingValue[type] = tmpNumOfDay;
                }
                else {
                    if(timeSettingValue[type] < Date.numOfDays[0])
                        timeSettingValue[type] = Date.numOfDays[0];
                    else if(timeSettingValue[type] > Date.numOfDays[timeSettingValue[4]])
                        timeSettingValue[type] = Date.numOfDays[timeSettingValue[4]];
                }
                break;
        }
    }

    public void setDayOfTheWeek() {
        // 년,월,일에 맞는 요일 설정
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
        try {
            java.util.Date date = dateFormat.parse(curDate.getCurrentDate());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void changeType() {
            type = (type + 1) % TYPE_SIZE;
    }

    public void requestSave() {
        if (timeSettingValue[4] == 2) {
            int temp, year = timeSettingValue[3];
            int tmpNumOfDay;
            temp = year % 4;
            if (temp == 0) {
                temp = year % 100;
                if (temp == 0) {
                    temp = year % 400;
                    if (temp == 0) {
                        tmpNumOfDay = 29;
                    }
                    else
                        tmpNumOfDay = 28;
                }
                else {
                    tmpNumOfDay = 29;
                }
            } else
                tmpNumOfDay = 28;

            if (timeSettingValue[5] > tmpNumOfDay) {
                changeMode(-1);
                return;
            }
        }
        else if (timeSettingValue[5] > Date.numOfDays[timeSettingValue[4]]) {
            changeMode(-1);
            return;
        }

        curTime.pauseTime();
        try {
            curTime.getTimeThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        curTime.setTime(timeSettingValue[0], timeSettingValue[1], timeSettingValue[2]);
        curDate.setDate(timeSettingValue[3], timeSettingValue[4], timeSettingValue[5]);
        setDayOfTheWeek();

        curTime.startTime();
        changeMode(-1);
    }

    public void requestTimeSettingMode() {
        this.changeMode(-1);
    }

    public int[] getTimeSettingValue() {
        return timeSettingValue;
    }

    public int getType() {
        return type;
    }

    public int getMode() {
        return mode;
    }

    public Date getCurDate() { return curDate; }
    public Time getCurTime() { return curTime; }
    public int getD_day() { return d_day; }
    public int getDayOfTheWeek() { return dayOfTheWeek; }


    public void setD_day(int d_day) {
        this.d_day = d_day;
    }

    public void cancel() {
        changeMode(-1);
        type = 0;
    }
}