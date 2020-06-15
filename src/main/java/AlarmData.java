
/**
 * @author Yoonseop Shin
 */
public class AlarmData {

    private int interval;
    private int volume;
    private Time alarmTime;

    public void setVolume(int volume) {
        this.volume = volume;
    }
    public int getVolume() {
        return volume;
    }
    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public AlarmData() {
        alarmTime = new Time(2); // 증가도 감소도 아니라 의미 없게 2로 지정.
        interval = 1;
        volume = 2;
        alarmTime.setTime(-1,-1,-1);
    }

    public Time getTime(){
        return this.alarmTime;
    }

    public void setAlarmTime(Time settingTime) {
        this.alarmTime = settingTime;
    }
}