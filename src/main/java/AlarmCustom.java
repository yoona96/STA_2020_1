import java.util.Arrays;

public class AlarmCustom extends Function {

    public final static int FID = 6;
    private int ALARM_TOP_LIMIT;
    private final int ALARM_BOTTOM_LIMIT = 0;
    private final int INTERVAL_TOP_LIMIT = 2;
    private final int INTERVAL_BOTTOM_LIMIT = 0;
    private final int VOLUME_TOP_LIMIT = 4;
    private final int VOLUME_BOTTOM_LIMIT = 0;
    private int[] customSettingValue;
    private System system;
    private int type;

    public AlarmCustom(System system) {
        fid = 6;
        mode = 0;
        this.system = system;
        customSettingValue = new int[3];
        Arrays.fill(customSettingValue, -1);
        type = 0;
    }

    public void requestAlarmSelectMode() {
        if(system.alarm.getSize() == 0)
        {
            return;
        }
        else {
            int[] tmp = {0, Math.min(2, system.alarm.getSize() - 1)};
            system.alarm.setSegmentPointer(tmp);
            changeMode(1);
        }
    }

    public Alarm getAlarm() {
        return this.system.alarm;
    }

    public void requestIntervalSettingMode() {
        changeMode(2);
    }

    public void requestVolumeSettingMode() {
        changeMode(2);
    }

    public void setCustom() {
        AlarmData[] alarmList = system.alarm.getAlarmList();
        int alarmPointer = system.alarm.getAlarmPointer();
        alarmList[alarmPointer].setInterval(customSettingValue[1]);
        alarmList[alarmPointer].setVolume(customSettingValue[2]);
    }

    @Override
    protected void cancel() {
        changeMode(0);
    }

    public void changeMode(int mode) {
        if (mode == 1){
            if ((ALARM_TOP_LIMIT = system.alarm.getSize()) == 0) {
                this.mode = 0;
                return;
            }
            customSettingValue[0] = 0;
        } else if (mode == 2)
        {
            if (this.mode == 1) {   // 선택된 알람에서 옴.
                type = 1;
                AlarmData[] tmp = system.alarm.getAlarmList();
                int alarmPointer = system.alarm.getAlarmPointer();
                customSettingValue[1] = tmp[alarmPointer].getInterval();
                customSettingValue[2] = tmp[alarmPointer].getVolume();
            }
        }
        else {
            Arrays.fill(customSettingValue, -1);
            system.alarm.setAlarmPointer(0);
            int[] tmp = {0, Math.max(system.alarm.getSize(), 2)};
            system.alarm.setSegmentPointer(tmp);
            type = 0;
        }
        this.mode = mode;
    }

    public void requestSave() {
        type = 0;
        setCustom();
        changeMode(0);
    }

    public int getType() {
        return this.type;
    }

    public void changeValue2(int diff) {
        int alarmPointer = system.alarm.getAlarmPointer();
        int[] segmentPointer = system.alarm.getSegmentPointer();
        alarmPointer += diff;

        if (alarmPointer > system.alarm.getSize() - 1)
            alarmPointer = system.alarm.getSize() - 1;
        if (alarmPointer < 0)
            alarmPointer = 0;

        if (alarmPointer > segmentPointer[1]) {
            segmentPointer[0]++;
            segmentPointer[1]++;
        }
        else if (alarmPointer < segmentPointer[0]) {
            segmentPointer[0]--;
            segmentPointer[1]--;
        }

        system.alarm.setSegmentPointer(segmentPointer);
        system.alarm.setAlarmPointer(alarmPointer);
    }

    public void changeValue(int diff) {
        customSettingValue[type] += diff;

        switch (type) {
            case 0:
                if (customSettingValue[type] > ALARM_TOP_LIMIT)
                    customSettingValue[type] = ALARM_TOP_LIMIT;
                if (customSettingValue[type] < ALARM_BOTTOM_LIMIT)
                    customSettingValue[type] = ALARM_BOTTOM_LIMIT;
                break;
            case 1:
                if (customSettingValue[type] > INTERVAL_TOP_LIMIT)
                    customSettingValue[type] = INTERVAL_TOP_LIMIT;
                if (customSettingValue[type] < INTERVAL_BOTTOM_LIMIT)
                    customSettingValue[type] = INTERVAL_BOTTOM_LIMIT;
                break;
            case 2:
                if (customSettingValue[type] > VOLUME_TOP_LIMIT)
                    customSettingValue[type] = VOLUME_TOP_LIMIT;
                if (customSettingValue[type] < VOLUME_BOTTOM_LIMIT)
                    customSettingValue[type] = VOLUME_BOTTOM_LIMIT;
                break;
        }

    }

    public void changeType() {
        if (type == 1)
            type = 2;
        else if (type == 0 || type == 2)
            type = 1;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) { this.mode = mode; }

    public int[] getCustomSettingValue() {
        return customSettingValue;
    }

    public void setCustomSettingValue(int[] customSettingValue) {
        this.customSettingValue = customSettingValue;
    }
}
