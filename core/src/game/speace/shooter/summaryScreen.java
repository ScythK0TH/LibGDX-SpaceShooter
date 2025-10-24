package game.speace.shooter;

import com.badlogic.gdx.Gdx;
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

public class summaryScreen implements Screen {
    private static final int BANNER_WIDTH = 320;
    private static final int BANNER_HEIGHT = 100;
    private SpriteBatch batch;
    private Stage stage;
    private String time;
    private final Main game;
    private int score, highscore;
    private Texture gameOverBannner;
    private BitmapFont font12;
    public HighScore highScoreManager;

    public summaryScreen(final Main game, int score, GameTimer gameTimer) {
        this.game = game;
        this.score = score;
        this.time = gameTimer.getFormattedTime();
        this.highScoreManager = new HighScore();
        this.highscore = HighScore.getHighScore();
        
        stage = new Stage(game.screenPort);
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();

        if (score > highscore) {
            HighScore.saveHighScore(score);
            this.highscore = score;
        }

        gameOverBannner = new Texture(Gdx.files.internal("over.png"));

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("SpaceGrotesk.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 25;
        font12 = fontGenerator.generateFont(fontParameter);
    }

    @Override
    public void show() {
        // Nothing to do here
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(gameOverBannner, Gdx.graphics.getWidth() / 2 - BANNER_WIDTH / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - 15, BANNER_WIDTH, BANNER_HEIGHT);

        GlyphLayout scoreLayout = new GlyphLayout(font12, "Score: " + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(font12, "Highscore: " + highscore, Color.WHITE, 0, Align.left, false);
        GlyphLayout playTimeLayout = new GlyphLayout(font12, "Play Time: " + this.time, Color.WHITE, 0, Align.left, false);

        font12.draw(batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - 15 * 4);
        font12.draw(batch, highscoreLayout, Gdx.graphics.getWidth() / 2 - highscoreLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - scoreLayout.height - 15 * 7);
        font12.draw(batch, playTimeLayout, Gdx.graphics.getWidth() / 2 - playTimeLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - highscoreLayout.height - 15 * 12);

        GlyphLayout goToOverScreen = new GlyphLayout(font12, "Next");
        float goToOverScreenX = Gdx.graphics.getWidth() / 2 - goToOverScreen.width / 2;
        float goToOverScreenY = Gdx.graphics.getHeight() / 3 - goToOverScreen.height / 3;
        

        float touchX = Gdx.input.getX(), touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
        if (Gdx.input.isTouched()) {
            // Checks if hovering over try again button
            if (touchX >= goToOverScreenX && touchX < goToOverScreenX + goToOverScreen.width && touchY >= goToOverScreenY - goToOverScreen.height && touchY < goToOverScreenY) {
                this.dispose();
                game.gotoOverScreen();
                MenuScreen.meteorite_S1.stop();
                GameScreen.bosslaserbeamloop.stop();
                GameScreen.chargelaserloop.stop();
                GameScreen.bosslaserbeam.stop();
                return;
            }
        }

        font12.draw(batch, goToOverScreen, goToOverScreenX, goToOverScreenY);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.screenPort.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Nothing to do here
    }

    @Override
    public void resume() {
        // Nothing to do here
    }

    @Override
    public void hide() {
        // Nothing to do here
    }

    @Override
    public void dispose() {
        batch.dispose();
        font12.dispose();
		stage.dispose();		
        gameOverBannner.dispose();
    }
}
