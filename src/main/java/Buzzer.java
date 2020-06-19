import javax.sound.sampled.*;

public class Buzzer implements Runnable {
    public final static float SAMPLE_RATE = 8000f;

    public static void tone(int hz, int msecs, double vol) throws LineUnavailableException {
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i = 0; i < msecs * 8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
            sdl.write(buf, 0, 1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    public final int INTERVALS[] = {500, 750, 1000};

    public final double VOLUMES[] = {0, 0.5, 1.0, 2.0, 3.0};    // legend

    private int interval;
    private double volume;
    private Thread beepThread;
    private boolean buzzerState;
    private final Object lock = new Object();

    public int getInterval() { return this.interval; }

    public double getVolume() { return this.volume; }

    public boolean isBuzzerState() {
        return buzzerState;
    }

    public Buzzer() {
        interval = INTERVALS[1];
        volume = VOLUMES[2];
        buzzerState = false;
    }

    public void run() {
        while(true) {
            synchronized (lock) {
                if(!buzzerState) break;
            }
            try {
                Buzzer.tone(500, interval, volume);
            } catch (LineUnavailableException e) {
                e.printStackTrace(java.lang.System.out);
            }
        }
    }

    public void beepBuzzer(int interval, int volume) {
        this.interval = INTERVALS[interval];
        this.volume = VOLUMES[volume];

        boolean nowBuzzerState;
        synchronized (lock) {
            nowBuzzerState  = buzzerState;
        }
        if(!nowBuzzerState) {
            synchronized (lock) {
                buzzerState = true;
            }
            beepThread = new Thread(this);
            beepThread.start();
        }
    }

    public void stopBuzzer() {
        while (buzzerState) {
            synchronized (lock) {
                buzzerState = false;
            }
        }
        try {
            beepThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace(java.lang.System.out);
            Thread.currentThread().interrupt();
        }
        interval = INTERVALS[1];
        volume = VOLUMES[2];
    }

    public Thread getBeepThread() {
        return this.beepThread;
    }
}