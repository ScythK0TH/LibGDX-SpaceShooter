package game.speace.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;

public class GameOverScreen implements Screen {
    
	private static final int BANNER_WIDTH = 320;
	private static final int BANNER_HEIGHT = 100;
	private SpriteBatch batch;
//	SpaceShooterGame game;
	private Stage stage;

	GameScreen gamescreen;
    int score, highscore;
    final Main game;
    private Texture gameOverBannner;
	private FreeTypeFontGenerator fontGenerator;
	private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    
	public GameOverScreen(final Main game) {
		this.game = game;
		stage = new Stage(game.screenPort);
        Gdx.input.setInputProcessor(stage);

//    	this.score = score;
    	batch = new SpriteBatch();
    	Preferences prefs = Gdx.app.getPreferences("Speace Game");
    	this.highscore = prefs.getInteger("Highh Score",0);
    	
    	if (score > highscore) {
			prefs.putInteger("highscore", score);
			prefs.flush();
		}
    	
//    	gameOverBannner = new Texture(Gdx.files.internal("over.png"));
    	fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceGrotesk.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		//สร้างฟอนด์
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceGrotesk.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 25;
        //เก็บฟอนด์ไว้ในตัวแปร
        BitmapFont font12 = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//draw GAMEOVER.png
//		batch.draw(gameOverBannner, Gdx.graphics.getWidth()/2 - BANNER_WIDTH / 2 , Gdx.graphics.getHeight()  - BANNER_HEIGHT - 15 , BANNER_WIDTH,BANNER_HEIGHT);
					
		GlyphLayout tryAgainLayout = new GlyphLayout(font12, "Try Again");
		GlyphLayout mainMenuLayout = new GlyphLayout(font12, "Main Menu");

		float tryAgainX = Gdx.graphics.getWidth() / 2 - tryAgainLayout.width /2;
		float tryAgainY = Gdx.graphics.getHeight() / 2 - tryAgainLayout.height / 2;
		float mainMenuX = Gdx.graphics.getWidth() / 2 - mainMenuLayout.width /2;
		float mainMenuY = Gdx.graphics.getHeight() / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;
		
		float touchX = Gdx.input.getX() , touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

		if(Gdx.input.isTouched()) {
			//Checks if hovering over try again button
			if (touchX >= tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY >= tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
				this.dispose();
				batch.end();
				game.setScreen(new GameScreen(game));
                MenuScreen.meteorite_S1.play(0.5f);
				return;
				
			}
			
			//Checks if hovering over main menu button
			if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY){
				this.dispose();
				game.setScreen(new MenuScreen(game));
				return;
			}
			
		}
		tryAgainLayout.setText(font12, "Try Again", Color.WHITE, 0, Align.left, false);
		mainMenuLayout.setText(font12, "Main Menu", Color.WHITE, 0, Align.left, false);
		font12.draw(batch, tryAgainLayout , tryAgainX ,tryAgainY);
		font12.draw(batch, mainMenuLayout , mainMenuX ,mainMenuY);
		batch.end();
	}

	    @Override
	    public void resize(int width, int height) {
	        game.screenPort.update(width, height);
	        stage.getViewport().update(width, height, true);

	    }

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
        batch.dispose();
		stage.dispose();		
	}
	

}
