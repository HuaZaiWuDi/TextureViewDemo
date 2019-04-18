package laboratory.dxy.jack.com.jackupdate.util.timer;


import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Oden on 2016/6/16.
 */
public class MyTimer {
    private Timer timer = null;
    private TimerTask task = null;
    private MyTimerListener myTimerListener;
    private long period;

    public MyTimer(long period, MyTimerListener l) {
        this.period = period;
        this.myTimerListener = l;
    }

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    myTimerListener.enterTimer();
                }
            };
            timer.schedule(task, 0, period);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            task.cancel();
            timer.cancel();

            task = null;
            timer = null;
        }
    }

}
