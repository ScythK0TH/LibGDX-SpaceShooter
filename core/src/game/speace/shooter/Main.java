package game.speace.shooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends Game {

	public SpriteBatch batch;
	public Viewport screenPort;
	int score;
	long time;

	@Override
	public void create () {
		batch = new SpriteBatch();
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false);
		screenPort = new ScreenViewport();
		this.setScreen(new MenuScreen(this));
	}

	public void gotoMenuScreen(){
		MenuScreen menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}
	
	public void gotoOverScreen(){
		GameOverScreen overscreen = new GameOverScreen(this);
		setScreen(overscreen);
	}
	
	public void gotoSummaryScreen(int score, GameTimer gameTimer ){
		summaryScreen summaScreen = new summaryScreen(this, score, gameTimer);
		setScreen(summaScreen);
	}
	
    public void gotoWinScreen(int score, GameTimer gameTimer) {
    	winScreen winScreen = new winScreen(this,score,gameTimer);
    	setScreen(winScreen);
    }
	
//	public void gotoCreditsScreen(){
//		CreditsScreen creditScreen = new CreditsScreen(this);
//		setScreen(creditScreen);
//		
//	}

	public void gotoGameScreen(){
		GameScreen gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}