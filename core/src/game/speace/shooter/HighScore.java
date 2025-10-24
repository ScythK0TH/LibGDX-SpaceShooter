package game.speace.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class HighScore {
    private static final String PREFS_NAME = "spaceshootergame";
    private static final String HIGH_SCORE_KEY = "highscore";
    private static Preferences prefs;

    public HighScore() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    public static void saveHighScore(int score) {
        prefs.putInteger(HIGH_SCORE_KEY, score);
        prefs.flush(); // Save changes
    }

    public static int getHighScore() {
        return prefs.getInteger(HIGH_SCORE_KEY, 0); // Default high score is 0
    }
    
    public static void resetHighScore() {
        prefs.putInteger(HIGH_SCORE_KEY, 0);
        prefs.flush();
    }
}
