import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * @author Yoonseop Shin
 */
public class System extends Function {

    // 6�� �� 4�� �ν��Ͻ��� ��������.
    // �˶�, �˶�Ŀ������ �׻� �� �� ���Եǰų� ���Ե��� �ʾƾ� �Ѵ�.
    final static int TIMED_OUT = 600_000;
    final String BLANK_SIX = "      ";
    final String DEFAULT_VALUE = "000000";
    public GUI GUI;
    public TimeKeeping timeKeeping;
    public Stopwatch stopwatch;
    public Timer timer;
    public D_day d_day;
    public Alarm alarm;
    public AlarmCustom alarmCustom;
    public Buzzer buzzer;
    public Border border;
    private int functionNumIdx;
    private int[] functionNum;
    private int selectedFid;
    private int status; // ��Ʈ����ŷ: 0b00 0b01 0b10 0b11
    private Thread checkTimeOut;
    private long lastOperateTime;
    private int[] cacheValue;

    public int getSelectedFid() {
        return selectedFid;
    }

    public System() {
        functionNum = new int[4];
        Arrays.fill(functionNum, 0);
        functionNum[0] = 1;
        functionNum[1] = 2;
        functionNum[2] = 3;
        functionNum[3] = 4;
        status = 0b00;
        type = 1;
        selectedFid = 1;
        functionNumIdx = 0;

        timeKeeping = new TimeKeeping(this);
        stopwatch = new Stopwatch(this);
        timer = new Timer(this);
        d_day = new D_day(this);
        alarm = null;
        alarmCustom = null;

        buzzer = new Buzzer();
        border = new Border(this);
        cacheValue = new int[4];
        Arrays.fill(cacheValue, -1);

        lastOperateTime = java.lang.System.currentTimeMillis();
    }

    public int getStatus() { return this.status; }

    public void setFunctionNum(int[] functionNum) {
        this.functionNum = functionNum;
    }

    public void startCheckTimeOut() {
        checkTimeOut = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (java.lang.System.currentTimeMillis() - lastOperateTime >= TIMED_OUT) {
                        lastOperateTime = java.lang.System.currentTimeMillis();
                        switch (selectedFid) {
                            case 1:
                                if (timeKeeping.getMode() == 0 && mode == 1) {
                                    this.cancel();
                                    GUI.setView(GUI.timekeepingView);
                                } else if (timeKeeping.getMode() == 1 && mode == 0) {
                                    timeKeeping.cancel();
                                    GUI.timekeepingView.borderPanel.setVisible(false);
                                }
                                break;
                            case 2:
                                if (stopwatch.getMode() == 2) {
                                    stopwatch.cancel();
                                    GUI.stopwatchView.borderPanel.setVisible(false);
                                }
                                break;
                            case 3:
                                if (timer.getMode() == 1) {
                                    timer.cancel();
                                    GUI.timerView.borderPanel.setVisible(false);
                                    String tmp = timer.getTimer().getCurrentTime();
                                    StringTokenizer st = new StringTokenizer(tmp, " ");
                                    GUI.timerView.setHour(String.format("%02d", Integer.parseInt(st.nextToken())));
                                    GUI.timerView.setMinute(String.format("%02d", Integer.parseInt(st.nextToken())));
                                    GUI.timerView.setSecond(String.format("%02d", Integer.parseInt(st.nextToken())));
                                }
                                break;
                            case 4:
                                if (d_day.getMode() == 1) {
                                    d_day.cancel();
                                    GUI.d_dayView.borderPanel.setVisible(false);
                                    if (d_day.getD_day() == -1) {
                                        GUI.d_dayView.setYear("  ");
                                        GUI.d_dayView.setMonth("NO");
                                        GUI.d_dayView.setDate("NE");
                                    } else {
                                        String curDate = d_day.getD_dayDate().getCurrentDate();
                                        StringTokenizer st = new StringTokenizer(curDate, " ");
                                        GUI.d_dayView.setYear(String.format("%02d", Integer.parseInt(st.nextToken()) % 100));
                                        GUI.d_dayView.setMonth(String.format("%02d", Integer.parseInt(st.nextToken())));
                                        GUI.d_dayView.setDate(String.format("%02d", Integer.parseInt(st.nextToken())));
                                    }
                                }
                                break;
                            case 5:
                                if (alarm.getMode() != 0) {
                                    alarm.cancel();
                                    GUI.alarmView.borderPanel.setVisible(false);
                                }
                                break;
                            case 6:
                                if (alarmCustom.getMode() != 0) {
                                    alarmCustom.cancel();
                                    GUI.alarmCustomView.borderPanel.setVisible(false);
                                }
                                break;
                            default:
                                break;
                        }
                        GUI.setView(GUI.timekeepingView);
                        functionNumIdx = 0;
                        selectedFid = 1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace(java.lang.System.out);
                    Thread.currentThread().interrupt();
                }
            }
        });
        checkTimeOut.start();
    }

    public void modeBtnLongPressed() {
        lastOperateTime = java.lang.System.currentTimeMillis();
        if (updateStatus() > -1)
            return;
        switch (selectedFid) {
            case 1:
                if (timeKeeping.getMode() == 0 && mode == 1) {
                    this.cancel();
                    GUI.setView(GUI.timekeepingView);
                } else if (timeKeeping.getMode() == 1 && mode == 0) {
                    timeKeeping.cancel();
                    GUI.timekeepingView.borderPanel.setVisible(false);
                }
                break;
            case 2:
                if (stopwatch.getMode() == 2) {
                    stopwatch.cancel();
                    GUI.stopwatchView.borderPanel.setVisible(false);
                }
                break;
            case 3:
                if (timer.getMode() == 1) {
                    timer.cancel();
                    GUI.timerView.borderPanel.setVisible(false);
                    String tmp = timer.getTimer().getCurrentTime();
                    StringTokenizer st = new StringTokenizer(tmp, " ");
                    GUI.timerView.setHour(String.format("%02d", Integer.parseInt(st.nextToken())));
                    GUI.timerView.setMinute(String.format("%02d", Integer.parseInt(st.nextToken())));
                    GUI.timerView.setSecond(String.format("%02d", Integer.parseInt(st.nextToken())));
                }
                break;
            case 4: // dday
                if (d_day.getMode() == 1) {
                    d_day.cancel();
                    GUI.d_dayView.borderPanel.setVisible(false);
                    if (d_day.getD_day() == -1) {
                        GUI.d_dayView.setYear("  ");
                        GUI.d_dayView.setMonth("NO");
                        GUI.d_dayView.setDate("NE");
                    } else {
                        String curDate = d_day.getD_dayDate().getCurrentDate();
                        StringTokenizer st = new StringTokenizer(curDate, " ");
                        GUI.d_dayView.setYear(String.format("%02d", Integer.parseInt(st.nextToken()) % 100));
                        GUI.d_dayView.setMonth(String.format("%02d", Integer.parseInt(st.nextToken())));
                        GUI.d_dayView.setDate(String.format("%02d", Integer.parseInt(st.nextToken())));
                    }
                }
                break;
            case 5: // alarm
                if (alarm.getMode() != 0) {
                    alarm.cancel();
                    GUI.alarmView.borderPanel.setVisible(false);
                }
                break;
            case 6:
                if (alarmCustom.getMode() != 0) {
                    alarmCustom.cancel();
                    GUI.alarmCustomView.borderPanel.setVisible(false);
                }
                break;
            default:
                break;
        }
    }

    public void selectBtnLongPressed() {
        lastOperateTime = java.lang.System.currentTimeMillis();
        if (updateStatus() > -1)
            return;
        switch (selectedFid) {
            case 1: // timekeeping
                if (timeKeeping.getMode() == 0 && mode == 0) {
                    requestFunctionSettingMode();

                    GUI.setView(GUI.functionSelectingView);
                    GUI.functionSelectingView.setdDay("   ");
                    GUI.functionSelectingView.setDate(BLANK_SIX);
                    GUI.functionSelectingView.setAlarmNum(" ");
                    GUI.functionSelectingView.setCurTime2("  ");
                    GUI.functionSelectingView.setDayofweek("   ");

                    String str = "";
                    for (int i = 0; i < 4; i++) {
                        str += String.valueOf(cacheValue[i]);
                    }
                    GUI.functionSelectingView.setCurTime1(str);
                    GUI.functionSelectingView.setBorderPanel(0);
                    GUI.functionSelectingView.borderPanel.setVisible(true);
                }
                break;
            case 2: // ��ž��ġ
                if (stopwatch.getMode() == 0) {
                    stopwatch.requestRecordCheckMode();
                    if(stopwatch.getMode() == 2)
                        GUI.stopwatchView.borderPanel.setVisible(true);
                }
                break;
            case 4: // d-day
                if (d_day.getMode() == 0) {
                    d_day.requestDeleteDday();

                    GUI.d_dayView.setYear("  ");
                    GUI.d_dayView.setMonth("NO");
                    GUI.d_dayView.setDate("NE");
                    GUI.d_dayView.setDday("000");
                }
                break;
            case 5:// alarm
                if (alarm.getMode() == 0) {
                    if (alarm.getSize() > 0) {
                        GUI.alarmView.borderPanel.setVisible(true);
                        GUI.alarmView.setBorderPanel(0);
                        alarm.requestAlarmSelectMode();
                    }
                }
                break;
            case 6:
                if (alarm.getMode() == 0) {
                    if (alarm.getSize() > 0) {
                        GUI.alarmCustomView.borderPanel.setVisible(true);
                        GUI.alarmCustomView.setBorderPanel(0);
                        alarmCustom.requestAlarmSelectMode();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void requestFunctionSettingMode() {
        changeMode(-1);
    }

    public void startBtnPressed() {
        lastOperateTime = java.lang.System.currentTimeMillis();
        if (updateStatus() > -1)
            return;
        switch (selectedFid) {
            case 1: // timekeeping���� ����ð� �����ϴ� ��
                if (timeKeeping.getMode() == 0 && mode == 0) {
                    return;
                } else if (timeKeeping.getMode() == 1 && mode == 0) {
                    timeKeeping.changeValue(1);
                    int[] timeSettingValue = timeKeeping.getTimeSettingValue();
                    GUI.timekeepingView.setHour(String.format("%02d", timeSettingValue[0]));
                    GUI.timekeepingView.setMinute(String.format("%02d", timeSettingValue[1]));
                    GUI.timekeepingView.setCurTime2(String.format("%02d", timeSettingValue[2]));
                    GUI.timekeepingView.setDate(String.format("%02d", timeSettingValue[3]).substring(2, 4)
                            + String.format("%02d", timeSettingValue[4])
                            + String.format("%02d", timeSettingValue[5]));
                } else if (timeKeeping.getMode() == 0 && mode == 1) {
                    changeValue(1);

                    String str = "1";
                    for (int i = 1; i < 4; i++) {
                        str += String.valueOf(cacheValue[i]);
                    }
                    GUI.functionSelectingView.setCurTime1(str);
                }
                break;
            case 2: // stopwatch
                if (stopwatch.getMode() == 0) {
                    stopwatch.requestStartStopwatch();
                } else if (stopwatch.getMode() == 1) {
                    stopwatch.requestPauseStopwatch();
                } else {
                    stopwatch.movePointer(1);
                    String[] tmp = stopwatch.getStopwatchRecord();

                    int curRecordPointer = stopwatch.getRecordPointer();

                    String[] str = new String[3];
                    if (tmp[0 + curRecordPointer] == null)
                        str[0] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[0 + curRecordPointer], " ");
                        str[0] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }
                    if (tmp[1 + curRecordPointer] == null)
                        str[1] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[1 + curRecordPointer], " ");
                        str[1] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));

                    }
                    if (tmp[2 + curRecordPointer] == null)
                        str[2] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[2 + curRecordPointer], " ");
                        str[2] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }

                    GUI.stopwatchView.setStopwatchList(str[0] + str[1] + str[2]);
                }
                break;
            case 3: // timer
                if (timer.getMode() == 0) {     // Ÿ�̸Ӱ� �⺻ ȭ���� ��
                    timer.requestStartTimer();
                } else if (timer.getMode() == 1) {  // Ÿ�̸� ���� ���
                    timer.changeValue(1);
                    int[] tsv = timer.getTimeSettingValue();
                    GUI.timerView.setHour(String.format("%02d", tsv[0]));
                    GUI.timerView.setMinute(String.format("%02d", tsv[1]));
                    GUI.timerView.setSecond(String.format("%02d", tsv[2]));
                } else {    // 타이머 실행중일 때
                    timer.requestPauseTimer();
                }

                break;
            case 4: // d-day
                if (d_day.getMode() == 0) {
                    // �ƹ��͵� ����.
                } else {
                    d_day.changeValue(1);
                    int[] curDate = d_day.getDateSettingValue();
                    GUI.d_dayView.setYear(String.format("%02d", curDate[0] % 100));
                    GUI.d_dayView.setMonth(String.format("%02d", curDate[1]));
                    GUI.d_dayView.setDate(String.format("%02d", curDate[2]));
                }
                break;
            case 5: // alarm
                if (alarm.getMode() == 0)   // �⺻��
                    return;
                    // GUI�� �ݿ��ؾ� ��.
                else if (alarm.getMode() == 1) { // �˶� ����
                    alarm.changeValue(1);
                    type = alarm.getType();
                    int[] alarmSettingValue = alarm.getAlarmSettingValue();
                    String str1 = "00", str2 = "00", str3 = "00";
                    if (alarmSettingValue[0] < 10) {
                        str1 = "0" + String.format("%1s", alarmSettingValue[0]);
                    } else {
                        str1 = String.format("%02d", alarmSettingValue[0]);
                    }
                    if (alarmSettingValue[1] < 10) {
                        str2 = "0" + String.format("%1s", alarmSettingValue[1]);
                    } else {
                        str2 = String.format("%02d", alarmSettingValue[1]);
                    }
                    if (alarmSettingValue[2] < 10) {
                        str3 = "0" + String.format("%1s", alarmSettingValue[2]);
                    } else {
                        str3 = String.format("%02d", alarmSettingValue[2]);
                    }

                    GUI.alarmView.setAlarm(str1 + str2 + str3);

                } else if (alarm.getMode() == 2) // ������ ����
                {
                    alarm.changeValue2(1);
                    int alarmPointer = alarm.getAlarmPointer();
                    int[] segmentPointer = alarm.getSegmentPointer();
                    AlarmData[] alarmList = alarm.getAlarmList();

                    if (alarmPointer >= segmentPointer[1]) {    // ���� ��
                        // ������������ �ش��ϴ� ������ ����ָ� ��.
                        String str = "";
                        for (int i = segmentPointer[0]; i <= segmentPointer[1]; i++) {
                            StringTokenizer st = new StringTokenizer(alarmList[i].getTime().getCurrentTime(), " ");
                            while (st.hasMoreTokens()) {
                                int tmp = Integer.parseInt(st.nextToken());
                                str += String.format("%02d", tmp);
                            }
                        }
                        for (int i = 0; i < (3 - (segmentPointer[1] - segmentPointer[0] + 1)) * 6; i++) {
                            str += " ";
                        }
                        GUI.alarmView.setAlarmList(str);   // 18�ڸ�
                    }

                    GUI.alarmView.setBorderPanel(alarmPointer - segmentPointer[0]);
                }
                break;
            case 6: // alarm custom
                if (alarmCustom.getMode() == 0) // �⺻���
                {

                } else if (alarmCustom.getMode() == 1) { // �˶� ����Ʈ ��ȸ ���
                    alarmCustom.changeValue2(1);

                    int alarmPointer = alarm.getAlarmPointer();
                    int[] segmentPointer = alarm.getSegmentPointer();
                    AlarmData[] alarmList = alarm.getAlarmList();

                    if (alarmPointer >= segmentPointer[1]) {    // ���� ��
                        // ������������ �ش��ϴ� ������ ����ָ� ��.
                        String str = "";
                        for (int i = segmentPointer[0]; i <= segmentPointer[1]; i++) {
                            StringTokenizer st = new StringTokenizer(alarmList[i].getTime().getCurrentTime(), " ");
                            while (st.hasMoreTokens()) {
                                int tmp = Integer.parseInt(st.nextToken());
                                str += String.format("%02d", tmp);
                            }
                        }
                        for (int i = 0; i < (3 - (segmentPointer[1] - segmentPointer[0] + 1)) * 6; i++) {
                            str += " ";
                        }
                        GUI.alarmCustomView.setAlarmList(str);   // 18�ڸ�
                    }

                    GUI.alarmCustomView.setBorderPanel(alarmPointer - segmentPointer[0]);


                } else if (alarmCustom.getMode() == 2) // �˶� ���͹�, ���� ����
                {
                    alarmCustom.changeValue(1);
                    int[] csvArr = alarmCustom.getCustomSettingValue();
                    GUI.alarmCustomView.setAlarmInterval(String.valueOf(csvArr[1]));
                    GUI.alarmCustomView.setAlarmVolume(String.valueOf(csvArr[2]));
                }

                break;
            default:
                break;
        }
    }

    public void resetBtnPressed() {
        lastOperateTime = java.lang.System.currentTimeMillis();
        if (updateStatus() > -1)
            return;
        switch (selectedFid) {
            case 1: // timekeeping���� ����ð� �����ϴ� ��
                if (timeKeeping.getMode() == 0 && mode == 0)
                    return;
                    // GUI�� �ݿ��ؾ� ��.
                else if (timeKeeping.getMode() == 1 && mode == 0) {
                    timeKeeping.changeValue(-1);
                    int type = timeKeeping.getType();

                    int[] timeSettingValue = timeKeeping.getTimeSettingValue();
                    GUI.timekeepingView.setHour(String.format("%02d", timeSettingValue[0]));
                    GUI.timekeepingView.setMinute(String.format("%02d", timeSettingValue[1]));
                    GUI.timekeepingView.setCurTime2(String.format("%02d", timeSettingValue[2]));
                    GUI.timekeepingView.setDate(String.format("%02d", timeSettingValue[3]).substring(2, 4)
                            + String.format("%02d", timeSettingValue[4])
                            + String.format("%02d", timeSettingValue[5]));
                } else if (timeKeeping.getMode() == 0 && mode == 1) {
                    changeValue(-1);

                    String str = "1";
                    for (int i = 1; i < 4; i++) {
                        str += String.valueOf(cacheValue[i]);
                    }
                    GUI.functionSelectingView.setCurTime1(str);
                }

                break;
            case 2: // stopwatch
                if (stopwatch.getMode() == 2) {
                    stopwatch.movePointer(-1);
                    String[] tmp = stopwatch.getStopwatchRecord();
                    int curRecordPointer = stopwatch.getRecordPointer();
                    String[] str = new String[3];

                    if (tmp[0 + curRecordPointer] == null)
                        str[0] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[0 + curRecordPointer], " ");
                        str[0] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }
                    if (tmp[1 + curRecordPointer] == null)
                        str[1] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[1 + curRecordPointer], " ");
                        str[1] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));

                    }
                    if (tmp[2 + curRecordPointer] == null)
                        str[2] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[2 + curRecordPointer], " ");
                        str[2] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }

                    GUI.stopwatchView.setStopwatchList(str[0] + str[1] + str[2]);
                } else {
                    stopwatch.requestResetStopwatch();
                    GUI.stopwatchView.setStopwatch(DEFAULT_VALUE);
                    GUI.stopwatchView.setStopwatchList("  NONE  NONE  NONE");
                }
                break;
            case 3: // timer
                if (timer.getMode() == 1) {
                    timer.changeValue(-1);
                    int[] tsv = timer.getTimeSettingValue();
                    GUI.timerView.setHour(String.format("%02d", tsv[0]));
                    GUI.timerView.setMinute(String.format("%02d", tsv[1]));
                    GUI.timerView.setSecond(String.format("%02d", tsv[2]));
                } else {
                    timer.requestResetTimer();
                    GUI.timerView.setHour("00");
                    GUI.timerView.setMinute("00");
                    GUI.timerView.setSecond("00");
                }
                break;
            case 4: // d-day
                if (d_day.getMode() == 0) {
                    // �ƹ��͵� ����.
                } else {
                    d_day.changeValue(-1);
                    int[] curDate = d_day.getDateSettingValue();
                    GUI.d_dayView.setYear(String.format("%02d", curDate[0] % 100));
                    GUI.d_dayView.setMonth(String.format("%02d", curDate[1]));
                    GUI.d_dayView.setDate(String.format("%02d", curDate[2]));
                }
                break;
            case 5: // alarm
                if (alarm.getMode() == 0)
                    return;
                    // GUI�� �ݿ��ؾ� ��.
                else if (alarm.getMode() == 1) { // �˶� ����
                    alarm.changeValue(-1);
                    type = alarm.getType();
                    int[] alarmSettingValue = alarm.getAlarmSettingValue();
                    String str1 = "00", str2 = "00", str3 = "00";
                    if (alarmSettingValue[0] < 10) {
                        str1 = "0" + String.format("%1s", alarmSettingValue[0]);
                    } else {
                        str1 = String.format("%02d", alarmSettingValue[0]);
                    }
                    if (alarmSettingValue[1] < 10) {
                        str2 = "0" + String.format("%1s", alarmSettingValue[1]);
                    } else {
                        str2 = String.format("%02d", alarmSettingValue[1]);
                    }
                    if (alarmSettingValue[2] < 10) {
                        str3 = "0" + String.format("%1s", alarmSettingValue[2]);
                    } else {
                        str3 = String.format("%02d", alarmSettingValue[2]);
                    }

                    GUI.alarmView.setAlarm(str1 + str2 + str3);
                } else if (alarm.getMode() == 2) // ������ ����
                {
                    alarm.changeValue2(-1);
                    int alarmPointer = alarm.getAlarmPointer();
                    int[] segmentPointer = alarm.getSegmentPointer();
                    AlarmData[] alarmList = alarm.getAlarmList();

                    if (alarmPointer <= segmentPointer[0]) {    // ���� ��
                        String str = "";
                        for (int i = segmentPointer[0]; i <= segmentPointer[1]; i++) {
                            StringTokenizer st = new StringTokenizer(alarmList[i].getTime().getCurrentTime(), " ");
                            while (st.hasMoreTokens()) {
                                int tmp = Integer.parseInt(st.nextToken());
                                str += String.format("%02d", tmp);
                            }
                        }
                        for (int i = 0; i < (3 - (segmentPointer[1] - segmentPointer[0] + 1)) * 6; i++) {
                            str += " ";
                        }
                        GUI.alarmView.setAlarmList(str);   // 18�ڸ�
                    }

                    GUI.alarmView.setBorderPanel(alarmPointer - segmentPointer[0]);
                }
                break;
            case 6: // alarm custom
                if (alarmCustom.getMode() == 0) // �˶� ������
                {
                    alarm.movePointer(-1);
                }
                // GUI�� �ݿ��ؾ� ��.
                else if (alarmCustom.getMode() == 1) {
                    alarmCustom.changeValue2(-1);
                    int alarmPointer = alarm.getAlarmPointer();
                    int[] segmentPointer = alarm.getSegmentPointer();
                    AlarmData[] alarmList = alarm.getAlarmList();

                    if (alarmPointer <= segmentPointer[0]) {    // ���� ��
                        String str = "";
                        for (int i = segmentPointer[0]; i <= segmentPointer[1]; i++) {
                            StringTokenizer st = new StringTokenizer(alarmList[i].getTime().getCurrentTime(), " ");
                            while (st.hasMoreTokens()) {
                                int tmp = Integer.parseInt(st.nextToken());
                                str += String.format("%02d", tmp);
                            }
                        }
                        for (int i = 0; i < (3 - (segmentPointer[1] - segmentPointer[0] + 1)) * 6; i++) {
                            str += " ";
                        }
                        GUI.alarmCustomView.setAlarmList(str);   // 18�ڸ�
                    }

                    GUI.alarmCustomView.setBorderPanel(alarmPointer - segmentPointer[0]);
                } else if (alarmCustom.getMode() == 2) { // �˶� ����, ���� ����
                    alarmCustom.changeValue(-1);
                    int[] csvArr = alarmCustom.getCustomSettingValue();
                    GUI.alarmCustomView.setAlarmInterval(String.valueOf(csvArr[1]));
                    GUI.alarmCustomView.setAlarmVolume(String.valueOf(csvArr[2]));
                }
                break;
            default:
                break;
        }

    }

    public void selectBtnPressed() {
        lastOperateTime = java.lang.System.currentTimeMillis();
        if (updateStatus() > -1)
            return;
        switch (selectedFid) {
            case 1: // timekeeping���� ����ð� �����ϴ� ��
                if (timeKeeping.getMode() == 0 && mode == 0) {
                    timeKeeping.requestTimeSettingMode();
                    GUI.timekeepingView.borderPanel.setVisible(true);
                    GUI.timekeepingView.borderPanel.setBounds(
                            GUI.timekeepingView.curTimePanel1.getX() - 5,
                            GUI.timekeepingView.curTimePanel1.getY() - 5,
                            (GUI.timekeepingView.curTimePanel1.getWidth() + 10) / 2,
                            GUI.timekeepingView.curTimePanel1.getHeight() + 10
                    );
                } else if (timeKeeping.getMode() == 1 && mode == 0) {
                    timeKeeping.changeType();
                    int type = timeKeeping.getType();
                    GUI.timekeepingView.borderPanel.setVisible(true);
                    if (type == 0) {
                        GUI.timekeepingView.borderPanel.setBounds(
                                GUI.timekeepingView.curTimePanel1.getX() - 5,
                                GUI.timekeepingView.curTimePanel1.getY() - 5,
                                (GUI.timekeepingView.curTimePanel1.getWidth() + 10) / 2,
                                GUI.timekeepingView.curTimePanel1.getHeight() + 10
                        );
                    } else if (type == 1) {
                        GUI.timekeepingView.borderPanel.setBounds(
                                GUI.timekeepingView.curTimePanel1.getX() - 5 +
                                        (GUI.timekeepingView.curTimePanel1.getWidth() + 10) / 2,
                                GUI.timekeepingView.curTimePanel1.getY() - 5,
                                (GUI.timekeepingView.curTimePanel1.getWidth() + 10) / 2,
                                GUI.timekeepingView.curTimePanel1.getHeight() + 10
                        );
                    } else if (type == 2) {
                        GUI.timekeepingView.borderPanel.setBounds(
                                GUI.timekeepingView.curTimePanel2.getX() - 5,
                                GUI.timekeepingView.curTimePanel2.getY() - 5,
                                GUI.timekeepingView.curTimePanel2.getWidth() + 10,
                                GUI.timekeepingView.curTimePanel2.getHeight() + 10
                        );
                    } else if (type == 3) {
                        GUI.timekeepingView.borderPanel.setBounds(
                                GUI.timekeepingView.datePanel.getX() - 5,
                                GUI.timekeepingView.datePanel.getY() - 5,
                                (GUI.timekeepingView.datePanel.getWidth() + 10) / 3,
                                GUI.timekeepingView.datePanel.getHeight() + 10
                        );
                    } else if (type == 4) {
                        GUI.timekeepingView.borderPanel.setBounds(
                                GUI.timekeepingView.datePanel.getX() - 5
                                        + (GUI.timekeepingView.datePanel.getWidth() + 10) / 3,
                                GUI.timekeepingView.datePanel.getY() - 5,
                                (GUI.timekeepingView.datePanel.getWidth() + 10) / 3,
                                GUI.timekeepingView.datePanel.getHeight() + 10
                        );
                    } else if (type == 5) {
                        GUI.timekeepingView.borderPanel.setBounds(
                                GUI.timekeepingView.datePanel.getX() - 5
                                        + 2 * (GUI.timekeepingView.datePanel.getWidth() + 10) / 3,
                                GUI.timekeepingView.datePanel.getY() - 5,
                                (GUI.timekeepingView.datePanel.getWidth() + 10) / 3,
                                GUI.timekeepingView.datePanel.getHeight() + 10
                        );
                    }
                } else if (timeKeeping.getMode() == 0 && mode == 1) {
                    changeType();
                    GUI.functionSelectingView.setBorderPanel(type - 1);
                }
                break;
            case 2: // stopwatch
                if (stopwatch.getMode() == 0 || stopwatch.getMode() == 1) {
                    stopwatch.requestSaveRecord();
                    String[] tmp = stopwatch.getStopwatchRecord();

                    String[] str = new String[3];
                    if (tmp[0] == null)
                        str[0] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[0], " ");
                        str[0] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }
                    if (tmp[1] == null)
                        str[1] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[1], " ");
                        str[1] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));

                    }
                    if (tmp[2] == null)
                        str[2] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[2], " ");
                        str[2] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }

                    GUI.stopwatchView.setStopwatchList(str[0] + str[1] + str[2]);
                } else {

                }
                break;
            case 3: // timer
                if (timer.getMode() == 0) {
                    timer.requestTimerSettingMode();
                    int[] tmp = timer.getTimeSettingValue();
                    GUI.timerView.setHour(String.format("%02d", tmp[0]));
                    GUI.timerView.setMinute(String.format("%02d", tmp[1]));
                    GUI.timerView.setSecond(String.format("%02d", tmp[2]));
                    GUI.timerView.borderPanel.setVisible(true);

                    int w = GUI.timerView.borderPanel.getWidth();
                    int h = GUI.timerView.borderPanel.getHeight();
                    int x = GUI.timerView.borderPanel.getX() % w + 2 * w;
                    int y = GUI.timerView.borderPanel.getY();
                    GUI.timerView.borderPanel.setBounds(x, y, w, h);
                }
                else if (timer.getMode() == 1){
                    timer.changeType();
                    int timerType = timer.getType();
                    int w = GUI.timerView.borderPanel.getWidth();
                    int h = GUI.timerView.borderPanel.getHeight();
                    int x = GUI.timerView.borderPanel.getX() % w + 2 * w;
                    int y = GUI.timerView.borderPanel.getY();

                    if (timerType == 0) {
                        GUI.timerView.borderPanel.setBounds(x, y, w, h);
                    } else if (timerType == 1) {
                        GUI.timerView.borderPanel.setBounds(x + w, y, w, h);
                    } else if (timerType == 2) {
                        GUI.timerView.borderPanel.setBounds(x + 2 * w, y, w, h);
                    }
                }
                else { }

                break;
            case 4: // d-day
                if (d_day.getMode() == 0) {
                    d_day.requestDdaySettingMode();
                    GUI.d_dayView.borderPanel.setVisible(true);

                    int w = GUI.d_dayView.borderPanel.getWidth();
                    int h = GUI.d_dayView.borderPanel.getHeight();
                    int x = GUI.d_dayView.borderPanel.getX() % w + 2 * w;
                    int y = GUI.d_dayView.borderPanel.getY();
                    GUI.d_dayView.borderPanel.setBounds(x, y, w, h);

                    String curDate = d_day.getD_dayDate().getCurrentDate();
                    StringTokenizer st = new StringTokenizer(curDate, " ");
                    GUI.d_dayView.setYear(String.format("%02d", Integer.parseInt(st.nextToken()) % 100));
                    GUI.d_dayView.setMonth(String.format("%02d", Integer.parseInt(st.nextToken())));
                    GUI.d_dayView.setDate(String.format("%02d", Integer.parseInt(st.nextToken())));
                } else {
                    d_day.changeType();
                    int d_dayType = d_day.getType();
                    int w = GUI.d_dayView.borderPanel.getWidth();
                    int h = GUI.d_dayView.borderPanel.getHeight();
                    int x = GUI.d_dayView.borderPanel.getX() % w + 2 * w;
                    int y = GUI.d_dayView.borderPanel.getY();
                    if (d_dayType == 0) {
                        GUI.d_dayView.borderPanel.setBounds(x, y, w, h);
                    } else if (d_dayType == 1) {
                        GUI.d_dayView.borderPanel.setBounds(x + w, y, w, h);
                    } else if (d_dayType == 2) {
                        GUI.d_dayView.borderPanel.setBounds(x + 2 * w, y, w, h);
                    }
                }
                break;
            case 5: // alarm
                if (alarm.getMode() == 0) {
                    alarm.requestAlarmSettingMode();

                    GUI.alarmView.borderPanel.setVisible(true);
                    int w = 300 / 3;
                    int h = 45;
                    int x = 350;
                    int y = 180;
                    GUI.alarmView.borderPanel.setBounds(x, y, w, h);
                } else if (alarm.getMode() == 1) {
                    alarm.changeType();

                    int alarmType = alarm.getType();
                    int w = GUI.alarmView.borderPanel.getWidth();
                    int h = GUI.alarmView.borderPanel.getHeight();
                    int x = 350;
                    int y = 180;
                    if (alarmType == 0) {
                        GUI.alarmView.borderPanel.setBounds(x, y, w, h);
                    } else if (alarmType == 1) {
                        GUI.alarmView.borderPanel.setBounds(x + w, y, w, h);
                    } else if (alarmType == 2) {
                        GUI.alarmView.borderPanel.setBounds(x + 2 * w, y, w, h);
                    }

                } else { // �˶� ����Ʈ Ȯ�θ��
                    alarm.requestDeleteAlarm();

                    int alarmPointer = alarm.getAlarmPointer();
                    AlarmData[] tmp = alarm.getAlarmList();

                    GUI.alarmView.borderPanel.setVisible(false);
                    GUI.alarmView.setAlarm(BLANK_SIX);

                    String[] str = new String[3];
                    if (tmp[0 + alarmPointer] == null)
                        str[0] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[0 + alarmPointer].getTime().getCurrentTime(), " ");
                        str[0] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }
                    if (tmp[1 + alarmPointer] == null)
                        str[1] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[1 + alarmPointer].getTime().getCurrentTime(), " ");
                        str[1] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));

                    }
                    if (tmp[2 + alarmPointer] == null)
                        str[2] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[2 + alarmPointer].getTime().getCurrentTime(), " ");
                        str[2] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }

                    GUI.alarmView.setAlarmList(str[0] + str[1] + str[2]);
                    GUI.alarmCustomView.setAlarmList(str[0] + str[1] + str[2]);
                }
                break;
            case 6: // alarm custom
                alarmCustom.changeType();
                if (alarmCustom.getMode() == 1) { // �˶� ����Ʈ ��ȸ ���
                    alarmCustom.requestIntervalSettingMode();
                    GUI.alarmCustomView.borderPanel.setBounds(
                        550, 165, GUI.alarmCustomView.ALARM_WIDTH, GUI.alarmCustomView.ALARM_HEIGHT
                    );
                    GUI.alarmCustomView.setAlarmInterval(Integer.toString(alarmCustom.getCustomSettingValue()[1]));
                    GUI.alarmCustomView.setAlarmVolume(Integer.toString(alarmCustom.getCustomSettingValue()[2]));
                } else if (alarmCustom.getMode() == 2) { // ����, ���͹� ���� ������
                    int acType = alarmCustom.getType();

                    if (acType == 2) {  // ����
                        alarmCustom.requestVolumeSettingMode();
                        GUI.alarmCustomView.borderPanel.setBounds(
                                430, 165, GUI.alarmCustomView.ALARM_WIDTH, GUI.alarmCustomView.ALARM_HEIGHT
                        );
                        GUI.alarmCustomView.setAlarmVolume(Integer.toString(alarmCustom.getCustomSettingValue()[2]));
                    } else if (acType == 1) { // ���͹�
                        alarmCustom.requestIntervalSettingMode();
                        GUI.alarmCustomView.borderPanel.setBounds(
                                550, 165, GUI.alarmCustomView.ALARM_WIDTH, GUI.alarmCustomView.ALARM_HEIGHT
                        );
                        GUI.alarmCustomView.setAlarmInterval(Integer.toString(alarmCustom.getCustomSettingValue()[1]));
                    }
                }
                break;
            default:
                break;
        }

    }

    public void modeBtnPressed() {
        lastOperateTime = java.lang.System.currentTimeMillis();
        if (updateStatus() > -1)
            return;
        switch (selectedFid) {
            case 1: // timekeeping���� ����ð� �����ϴ� ��
                if (timeKeeping.getMode() == 0 && mode == 0) {
                    nextFunction();
                } else if (timeKeeping.getMode() == 0 && mode == 1) {
                    // 5, 6�� ��Ʈ
                    setFunction();

                    changeMode(-1);
                    GUI.setView(GUI.timekeepingView);
                } else {
                    // ������ �ϸ� d-day�� �ٽ� ����ϸ� ��.
                    timeKeeping.requestSave();
                    try {
                        GUI.timekeepingView.borderPanel.setVisible(false);
                        d_day.setDate(d_day.getD_dayDate());

                        GUI.d_dayView.borderPanel.setVisible(false);
                        String curDate = d_day.getD_dayDate().getCurrentDate();
                        StringTokenizer st = new StringTokenizer(curDate, " ");

                        if (d_day.getD_day() == -1) {
                            GUI.d_dayView.setYear("00");
                            GUI.d_dayView.setMonth("NO");
                            GUI.d_dayView.setDate("NE");
                        } else {
                            GUI.d_dayView.setYear(String.format("%02d", Integer.parseInt(st.nextToken()) % 100));
                            GUI.d_dayView.setMonth(String.format("%02d", Integer.parseInt(st.nextToken())));
                            GUI.d_dayView.setDate(String.format("%02d", Integer.parseInt(st.nextToken())));
                        }
                        if (d_day.getD_day() > 999)
                            GUI.d_dayView.setDday("999");
                        else if (d_day.getD_day() == -1) {
                            GUI.d_dayView.setDday("000");
                        } else
                            GUI.d_dayView.setDday(String.format("%03d", d_day.getD_day()));
                    } catch (NullPointerException e) {
                    }
                }

                break;
            case 2: // stopwatch
                if (stopwatch.getMode() == 0) {
                    nextFunction();
                }

                break;
            case 3: // timer
                if (timer.getMode() == 0) {
                    nextFunction();
                }
                else if (timer.getMode() == 1) {
                    timer.requestSave();
                    GUI.timerView.borderPanel.setVisible(false);
                }
                else { nextFunction(); }

                break;
            case 4: // d-day
                if (d_day.getMode() == 0) {
                    GUI.d_dayView.borderPanel.setVisible(false);
                    String curDate = d_day.getD_dayDate().getCurrentDate();
                    StringTokenizer st = new StringTokenizer(curDate, " ");

                    if (d_day.getD_day() == -1) {
                        GUI.d_dayView.setYear("00");
                        GUI.d_dayView.setMonth("NO");
                        GUI.d_dayView.setDate("NE");
                    } else {
                        GUI.d_dayView.setYear(String.format("%02d", Integer.parseInt(st.nextToken()) % 100));
                        GUI.d_dayView.setMonth(String.format("%02d", Integer.parseInt(st.nextToken())));
                        GUI.d_dayView.setDate(String.format("%02d", Integer.parseInt(st.nextToken())));
                    }
                    if (d_day.getD_day() > 999)
                        GUI.d_dayView.setDday("999");
                    else if (d_day.getD_day() == -1)
                        GUI.d_dayView.setDday("000");
                    else
                        GUI.d_dayView.setDday(String.format("%03d", d_day.getD_day()));
                    nextFunction();
                } else {    // mode 1
                    d_day.requestSave();
                    GUI.d_dayView.borderPanel.setVisible(false);
                    String curDate = d_day.getD_dayDate().getCurrentDate();
                    StringTokenizer st = new StringTokenizer(curDate, " ");
                    GUI.d_dayView.setYear(String.format("%02d", Integer.parseInt(st.nextToken()) % 100));
                    GUI.d_dayView.setMonth(String.format("%02d", Integer.parseInt(st.nextToken())));
                    GUI.d_dayView.setDate(String.format("%02d", Integer.parseInt(st.nextToken())));
                    if (d_day.getD_day() > 999)
                        GUI.d_dayView.setDday("999");
                    else if (d_day.getD_day() == -1) {
                        GUI.d_dayView.setYear("00");
                        GUI.d_dayView.setMonth("NO");
                        GUI.d_dayView.setDate("NE");
                        GUI.d_dayView.setDday("000");
                    }
                    else
                        GUI.d_dayView.setDday(String.format("%03d", d_day.getD_day()));

                }

                break;
            case 5: // alarm
                if (alarm.getMode() == 0) {
                    nextFunction();
                } else if (alarm.getMode() == 1) {
                    alarm.requestSave();
                    timeKeeping.setAlarmCnt(alarm.getSize());
                    int alarmPointer = alarm.getAlarmPointer();
                    AlarmData[] tmp = alarm.getAlarmList();

                    GUI.alarmView.borderPanel.setVisible(false);
                    GUI.alarmView.setAlarm(BLANK_SIX);

                    String[] str = new String[3];
                    if (tmp[0 + alarmPointer] == null)
                        str[0] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[0 + alarmPointer].getTime().getCurrentTime(), " ");
                        str[0] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }
                    if (tmp[1 + alarmPointer] == null)
                        str[1] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[1 + alarmPointer].getTime().getCurrentTime(), " ");
                        str[1] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));

                    }
                    if (tmp[2 + alarmPointer] == null)
                        str[2] = BLANK_SIX;
                    else {
                        StringTokenizer st = new StringTokenizer(tmp[2 + alarmPointer].getTime().getCurrentTime(), " ");
                        str[2] = String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken()));
                    }

                    GUI.alarmView.setAlarmList(str[0] + str[1] + str[2]);
                    GUI.alarmCustomView.setAlarmList(str[0] + str[1] + str[2]);
                }
                break;
            case 6: // alarm custom
                if (alarmCustom.getMode() == 0) {
                    nextFunction();
                } else if (alarmCustom.getMode() == 1) {
                    //alarmCustom.requestAlarmVolumeMode();
                } else if (alarmCustom.getMode() == 2) {
                    alarmCustom.requestSave();
                    GUI.alarmCustomView.borderPanel.setVisible(false);
                    GUI.alarmCustomView.setAlarmVolume(" ");
                    GUI.alarmCustomView.setAlarmInterval(" ");
                }

                break;
            default:
                break;
        }
    }

    public void cancel() {
        changeMode(-1);
        type = 1;
    }

    public void changeMode(int _mode) {
        mode ^= 1;

        if (mode == 0) {
            Arrays.fill(cacheValue, -1);
        } else {
            for (int i = 0; i < 4; i++) {
                cacheValue[i] = functionNum[i];
            }
        }
        type = 1;
    }

    public void changeType() {
        type = (type + 1) % 3 + 1;
    }

    @Override
    protected int getMode() {
        return this.mode;
    }

    public void changeValue(int diff) {
        cacheValue[type] += diff;

        if (cacheValue[type] > 6)
            cacheValue[type] = 6;
        if (cacheValue[type] < 2)
            cacheValue[type] = 2;
    }

    public void setFunction() {
        HashSet<Integer> hs = new HashSet<>();
        for (int i = 1; i < 4; i++) {
            hs.add(cacheValue[i]);
        }
        if (hs.size() == 3) {
            if ((hs.contains(5) && hs.contains(6)) || (!hs.contains(5) && !hs.contains(6))) {   // ����
                functionNum[1] = cacheValue[1];
                functionNum[2] = cacheValue[2];
                functionNum[3] = cacheValue[3];

                for (int i : functionNum) {
                    if (i == Stopwatch.FID) {
                        if (stopwatch == null)
                            stopwatch = new Stopwatch(this);
                    } else if (i == Timer.FID) {
                        if (timer == null)
                            timer = new Timer(this);
                    } else if (i == D_day.FID) {
                        if (d_day == null)
                            d_day = new D_day(this);
                    } else if (i == Alarm.FID) {
                        if (alarm == null)
                            alarm = new Alarm(this);
                    } else if (i == AlarmCustom.FID) {
                        if (alarmCustom == null)
                            alarmCustom = new AlarmCustom(this);
                    }
                }

                boolean flag = false;
                for (int i = 1; i < 4; i++) {
                    if (Stopwatch.FID == functionNum[i]) {
                        flag = true;
                    }
                }
                if (!flag)
                    stopwatch = null;

                flag = false;
                for (int i = 1; i < 4; i++) {
                    if (Timer.FID == functionNum[i]) {
                        flag = true;
                    }
                }
                if (!flag)
                    timer = null;

                flag = false;
                for (int i = 1; i < 4; i++) {
                    if (D_day.FID == functionNum[i]) {
                        flag = true;
                    }
                }
                if (!flag)
                    d_day = null;

                flag = false;
                for (int i = 1; i < 4; i++) {
                    if (Alarm.FID == functionNum[i]) {
                        flag = true;
                    }
                }
                if (!flag)
                    alarm = null;

                flag = false;
                for (int i = 1; i < 4; i++) {
                    if (alarmCustom.FID == functionNum[i]) {
                        flag = true;
                    }
                }
                if (!flag)
                    alarmCustom = null;
            }
        }
    }

    public void drawTotalBorder() {
        GUI.timekeepingView.setClockDisplay(true);
        GUI.stopwatchView.setClockDisplay(true);
        GUI.timerView.setClockDisplay(true);
        GUI.d_dayView.setClockDisplay(true);
        GUI.alarmView.setClockDisplay(true);
        GUI.alarmCustomView.setClockDisplay(true);
    }

    public void removeTotalBorder() {
        GUI.timekeepingView.setClockDisplay(false);
        GUI.stopwatchView.setClockDisplay(false);
        GUI.timerView.setClockDisplay(false);
        GUI.d_dayView.setClockDisplay(false);
        GUI.alarmView.setClockDisplay(false);
        GUI.alarmCustomView.setClockDisplay(false);
    }

    public void startBorder() {
        border.startBorder();
        status |= 2;
        if (GUI != null)
            drawTotalBorder();
    }

    public void stopBorder() {
        border.stopBorder();
        if (GUI != null)
            removeTotalBorder();
    }

    public void beepBuzzer(int interval, int volume) {
        status |= 1;
        buzzer.beepBuzzer(interval, volume);
    }

    public void stopBuzzer() {
        try {
            buzzer.stopBuzzer();
            buzzer.getBeepThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace(java.lang.System.out);
            Thread.currentThread().interrupt();
        }
    }

    public int updateStatus() {
        if (status == 0b11) {
            status = 0b01;
            stopBorder();
            return 2;
        } else if (status == 0b10) {
            status = 0b00;
            stopBorder();
            return 1;
        } else if (status == 0b01) {
            status = 0b00;
            stopBuzzer();
            return 0;
        }
        return -1;
    }

    public int getFunctionNumIdx() {
        return this.functionNumIdx;
    }

    public int[] getFunctionNum() {
        return functionNum;
    }

    public void nextFunction() {
        functionNumIdx = (functionNumIdx + 1) % 4;
        selectedFid = functionNum[functionNumIdx];

        if (GUI != null) {
            switch (selectedFid) {
                case 1:
                    GUI.setView(GUI.timekeepingView);
                    break;
                case 2:
                    GUI.setView(GUI.stopwatchView);
                    break;
                case 3:
                    GUI.setView(GUI.timerView);
                    break;
                case 4:
                    GUI.setView(GUI.d_dayView);
                    break;
                case 5:
                    GUI.setView(GUI.alarmView);
                    break;
                case 6:
                    GUI.setView(GUI.alarmCustomView);
                    break;
                default:
                    break;
            }
        }
    }
}