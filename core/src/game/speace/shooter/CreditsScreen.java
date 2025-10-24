package game.speace.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;



public class CreditsScreen implements Screen {

    final Main game;
    private Skin mySkin;
    private Stage stage;
	private Texture backgroundTexture;
	private TextureRegion backgroundRegion;

    public CreditsScreen(final Main game){
        this.game = game;
        
        // Load the background texture
        backgroundTexture = new Texture(Gdx.files.internal("space.jpg"));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundRegion = new TextureRegion(backgroundTexture);
        
        mySkin = new Skin(Gdx.files.internal(GameConstants.skin));
        stage = new Stage(game.screenPort);
        Gdx.input.setInputProcessor(stage);


        Label gameTitle = new Label("By Jan Oscar",mySkin,"big");
        gameTitle.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,GameConstants.centerY + GameConstants.row_height);
        gameTitle.setAlignment(Align.center);
        
        Label gameTitle1 = new Label("66102010145",mySkin,"big");
        gameTitle1.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle1.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,gameTitle.getY() - GameConstants.row_height -7);
        gameTitle1.setAlignment(Align.center);
        
        Label gameTitle2 = new Label("66102010150",mySkin,"big");
        gameTitle2.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle2.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,gameTitle1.getY() - GameConstants.row_height -7);
        gameTitle2.setAlignment(Align.center);
        
        Label gameTitle3 = new Label("66102010153",mySkin,"big");
        gameTitle3.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle3.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,gameTitle2.getY() - GameConstants.row_height -7);
        gameTitle3.setAlignment(Align.center);

        Label gameTitle4 = new Label("66102010239",mySkin,"big");
        gameTitle4.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle4.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,gameTitle3.getY() - GameConstants.row_height -7);
        gameTitle4.setAlignment(Align.center);
        
        Label gameTitle5 = new Label("66102010571",mySkin,"big");
        gameTitle5.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle5.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,gameTitle4.getY() - GameConstants.row_height -7);
        gameTitle5.setAlignment(Align.center);
        
       
        Button homeBtn = new TextButton("HOME",mySkin,"small");
        homeBtn.setSize(GameConstants.col_width,GameConstants.row_height);
        homeBtn.setPosition(0,GameConstants.screenHeight - homeBtn.getHeight());
        homeBtn.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.gotoMenuScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(homeBtn);
        stage.addActor(gameTitle);
        stage.addActor(gameTitle1);
        stage.addActor(gameTitle2);
        stage.addActor(gameTitle3);
        stage.addActor(gameTitle4);
        stage.addActor(gameTitle5);
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
        game.screenPort.update(width,height);

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
    public void dispose(){
        mySkin.dispose();
        stage.dispose();
    }
}