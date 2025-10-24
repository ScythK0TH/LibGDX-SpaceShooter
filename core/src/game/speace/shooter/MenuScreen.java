package game.speace.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MenuScreen implements Screen {

	  final Main game;
	  private Skin mySkin;
	  private Stage stage;
	  private Texture backgroundTexture;
	  private TextureRegion backgroundRegion;
	  public static Sound meteorite_S1 = Gdx.audio.newSound(Gdx.files.internal("soundmenu.ogg"));
	  

    public MenuScreen(final Main game) {
        this.game = game;
        
        mySkin = new Skin(Gdx.files.internal(GameConstants.skin));
        stage = new Stage(game.screenPort);
        Gdx.input.setInputProcessor(stage);
        
         
        long bg1 = meteorite_S1.play(1f);
        meteorite_S1.setVolume(bg1, 0.2f);
  
        
        // Load the background texture
        backgroundTexture = new Texture(Gdx.files.internal("space.png"));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundRegion = new TextureRegion(backgroundTexture);

//        Label gameTitle = new Label("GAME MENU", mySkin, "big");
//        gameTitle.setSize(GameConstants.col_width * 2, GameConstants.row_height * 2);
//        gameTitle.setPosition(GameConstants.centerX - gameTitle.getWidth() / 2, GameConstants.centerY + GameConstants.row_height);
//        gameTitle.setAlignment(Align.center);

        Button startBtn = new ImageButton(mySkin, "start");
        startBtn.setSize(GameConstants.col_width * 4, GameConstants.row_height);
        startBtn.setPosition(GameConstants.centerX - startBtn.getWidth() / 2, GameConstants.centerY);
        startBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.gotoGameScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Button exitBtn = new ImageButton(mySkin, "exit");
        exitBtn.setSize(GameConstants.col_width * 4, GameConstants.row_height);
        exitBtn.setPosition(GameConstants.centerX - exitBtn.getWidth() / 2, startBtn.getY() - GameConstants.row_height - 15);
        exitBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            	Gdx.app.exit();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

//        stage.addActor(gameTitle);
        stage.addActor(startBtn);
        stage.addActor(exitBtn);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);  // Set the clear color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Draw the background image
        game.batch.begin();
        game.batch.draw(backgroundRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Render the stage
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.screenPort.update(width, height);
        stage.getViewport().update(width, height, true);

    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        mySkin.dispose();
        stage.dispose();
    }
}
