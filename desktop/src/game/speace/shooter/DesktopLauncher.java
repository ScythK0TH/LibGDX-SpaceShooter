package game.speace.shooter;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		DisplayMode disp = Lwjgl3ApplicationConfiguration.getDisplayMode();
		config.setForegroundFPS(disp.refreshRate);
		config.setTitle("ShooterGame");
		config.setWindowedMode(700, 1000);
		new Lwjgl3Application(new Main(), config);
	}
}
