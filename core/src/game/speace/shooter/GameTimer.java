package game.speace.shooter;

import com.badlogic.gdx.utils.Timer;

public class GameTimer {
    private float timeElapsed; // Time elapsed in seconds
    private Timer.Task timerTask;

    public GameTimer() {
        timeElapsed = 0;
    }

    public void start() {
        timerTask = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                timeElapsed++;
            }
        }, 1, 1); // Schedule to run every second
    }

    public void stop() {
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    public void reset() {
        stop();
        timeElapsed = 0;
    }

    public String getFormattedTime() {
        int minutes = (int) (timeElapsed / 60);
        int seconds = (int) (timeElapsed % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    public float getTimeElapsed() {
        return timeElapsed;
    }
}
