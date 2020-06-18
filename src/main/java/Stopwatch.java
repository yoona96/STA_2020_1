import java.util.StringTokenizer;

public class Stopwatch extends Function {

    final static int FID = 2;
    public final int STOPWATCH_TOP_LIMIT = 7;
    public final int STOPWATCH_BOTTOM_LIMIT = 0;

    private final int TYPE_SIZE = 3;
    private int[] timeSettingValue = {-1, -1, -1};
    private Time stopwatch;
    private String[] stopwatchRecord;
    private int recordPointer;

    public Stopwatch(System system) {
        mode = 0;
        type = 0;
        stopwatchRecord = new String[10];
        for(int i=0; i<10; i++) {
            stopwatchRecord[i] = null;
        }
        stopwatch = new Time(2);
        stopwatch.setTime(0, 0, 0);
        stopwatch.setSecondListener(() -> {
                    if (system.GUI != null) {
                        String time = stopwatch.getCurrentTime();
                        StringTokenizer st = new StringTokenizer(time, " ");
                        system.GUI.stopwatchView.setStopwatch(String.format("%02d", Integer.parseInt(st.nextToken()))
                        + String.format("%02d", Integer.parseInt(st.nextToken())) +
                                String.format("%02d", Integer.parseInt(st.nextToken())));
                    }
                }
                );
        recordPointer = 0;
    }

    public Time getStopwatch() {
        return stopwatch;
    }

    public void requestStartStopwatch() {
        changeMode(1);
        stopwatch.startTime();
    }

    public void requestPauseStopwatch() {
        stopwatch.pauseTime();
        try {
            stopwatch.getTimeThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        changeMode(0);
    }

    public void requestResetStopwatch() {
        if (mode == 1)
            requestPauseStopwatch();
        stopwatch.clearTime();
        clearList();
    }

    public String[] getStopwatchRecord() {
        return stopwatchRecord;
    }

    public void clearList() {
        for(int i=0; i<10; i++) {
            stopwatchRecord[i] = null;
        }
    }

    public void requestSaveRecord() {
        String time = stopwatch.getCurrentTime();
        record(time);
    }

    public void record(String stopwatchTime) {
        for(int i=0; i<10; i++) {
            //stopwatch의 기록이 10개 미만일 때
            if(stopwatchRecord[i] == null) {
                stopwatchRecord[i] = stopwatchTime; //주석 수정해야합니다.
                break;
            }

            //stopwatch의 기록이 10개일 때
            if(i==9 && stopwatchRecord[i] != null) {
                for(int j=0; j<9; j++) {
                    stopwatchRecord[j] = stopwatchRecord[j+1];
                }
                stopwatchRecord[9] = stopwatchTime;
            }
        }
    }

    public void requestRecordCheckMode() {
        if(getSize() == 0) return;
        changeMode(2);
    }

    public int getRecordPointer() {
        return this.recordPointer;
    }

    public void movePointer(int diff) {
        recordPointer+=diff;

        if (recordPointer > 2 && stopwatchRecord[0 + recordPointer] == null) {
            recordPointer -= 1;
            return;
        }

        if(recordPointer < STOPWATCH_BOTTOM_LIMIT)
            recordPointer = STOPWATCH_BOTTOM_LIMIT;
        else if(recordPointer > STOPWATCH_TOP_LIMIT)
            recordPointer = STOPWATCH_TOP_LIMIT;
    }

    public void cancel() {
        changeMode(0);
        type = 0;
    }

    @Override
    public void changeMode(int mode) {
        this.mode = mode;

        if (this.mode == 0) {
            recordPointer = 0;
        }
    }

    public void changeValue(int diff) {
        timeSettingValue[type] += diff;
        switch(type) {
            case 0:
                if (timeSettingValue[type] < stopwatch.TIME_BOTTOM_LIMIT)
                    timeSettingValue[type] = stopwatch.HOUR_TOP_LIMIT;
                else if (timeSettingValue[type] > stopwatch.HOUR_TOP_LIMIT)
                    timeSettingValue[type] = stopwatch.TIME_BOTTOM_LIMIT;
                break;
            case 1:
                if (timeSettingValue[type] < stopwatch.TIME_BOTTOM_LIMIT)
                    timeSettingValue[type] = stopwatch.MINUTE_TOP_LIMIT;
                else if (timeSettingValue[type] > stopwatch.MINUTE_TOP_LIMIT)
                    timeSettingValue[type] = stopwatch.TIME_BOTTOM_LIMIT;
                break;
            case 2:
                if (timeSettingValue[type] < stopwatch.TIME_BOTTOM_LIMIT)
                    timeSettingValue[type] = stopwatch.SECOND_TOP_LIMIT;
                else if (timeSettingValue[type] > stopwatch.SECOND_TOP_LIMIT)
                    timeSettingValue[type] = stopwatch.TIME_BOTTOM_LIMIT;
                break;
        }
    }

    public void changeType() { type = (type + 1) % TYPE_SIZE; }

    public int getMode() {
        return this.mode;
    }

    public int getSize() {
        int count=0;
        for(int i=0; i<10; ++i) {
            if(stopwatchRecord[i] == null) break;
            count++;
        }
        return count;
    }
}