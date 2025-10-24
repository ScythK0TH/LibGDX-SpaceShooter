package game.speace.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

class GameScreen implements Screen {
//	long startTime = System.currentTimeMillis();
	
    final Main game;
    private Stage stage;
    
    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;	
    private TextureAtlas textureAtlas;
    private Texture explosionTexture;
    private Texture godModeTexture;
    private Texture enemygodModeTexture;
    private Texture dmgv_blue;
    private Texture dmgv_red;

    private TextureRegion[] backgrounds;
    private float backgroundHeight; //height of background in World units
    
    private Sprite playerShipTextureRegion, enemyShipTextureRegion, enemyShip2TextureRegion, enemyShip3TextureRegion, enemyShip4TextureRegion, boss1TR,
    				boss1TR2;
    private TextureRegion playerShieldTextureRegion, enemyShieldTextureRegion, enemyTankShieldTextureRegion, boss1Laser,
            playerLaserTextureRegion, enemyLaserTextureRegion, boss1Laser2, boss1Beam;
    
    //items passing
    private TextureRegion items1;
    private TextureRegion items2;
    private TextureRegion items3;
    private TextureRegion items4;
    private TextureRegion items5;

    //timing
    private float[] backgroundOffsets = {0, 0, 0}; //check error
    private float backgroundMaxScrollingSpeed;
    private float timeBetweenEnemySpawns = 1f;
    private float enemySpawnTimer = 0;
    private GameTimer gameTimer;

    //world parameters
    private final float WORLD_WIDTH = 86;
    private final float WORLD_HEIGHT = 144;
    private final float TOUCH_MOVEMENT_THRESHOLD = 10f;
    
    //sound
    Sound player_S1 = Gdx.audio.newSound(Gdx.files.internal("sfx_laser1.ogg"));
    Sound enemy_S1 = Gdx.audio.newSound(Gdx.files.internal("sfx_laser1.ogg"));
    Sound chargelaser = Gdx.audio.newSound(Gdx.files.internal("Charge.ogg")); 
    public static Sound bosslaserbeam = Gdx.audio.newSound(Gdx.files.internal("Shootlong.ogg"));
    public static Sound chargelaserloop = Gdx.audio.newSound(Gdx.files.internal("ChargeLoop.ogg")); 
    public static Sound bosslaserbeamloop = Gdx.audio.newSound(Gdx.files.internal("Shootlongloop.ogg"));
    
    

    //game objects
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShipList;
    private LinkedList<Laser> playerLaserList;
    private LinkedList<Laser> enemyLaserList;
    private LinkedList<Explosion> explosionList;
    private LinkedList<BossShip1> bossShip1List;
    private LinkedList<Laser> boss1LaserList;
    private LinkedList<DmgVitt> dmgVittList;
    
    //bonus
    private LinkedList<Bonusitems> bonusitemsList;

    private int score = 0;
    
    //default
    public static Sprite tempskin;
	public static float tempshoot;
	public static float tempbetween;

    //Heads-Up Display
    BitmapFont font;
    float hudVerticalMargin, hudLeftX, hudRightX, hudCentreX, hudRow1Y, hudRow2Y, hudSectionWidth;

    GameScreen(final Main game) {
        this.game = game;
        gameTimer = new GameTimer();
        gameTimer.start();
        
        stage = new Stage(game.screenPort);
        Gdx.input.setInputProcessor(stage);

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //set up the texture atlas
        textureAtlas = new TextureAtlas("images.atlas");

        // setting up the background
        backgrounds = new TextureRegion[3];
        backgrounds[0] = textureAtlas.findRegion("Starscape04");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");

        // กำหนดค่าให้กับ backgroundOffsets
        backgroundOffsets = new float[backgrounds.length];

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = (float) (WORLD_HEIGHT) / 4;

        //initialize texture regions
        playerShipTextureRegion = new Sprite(textureAtlas.findRegion("Shipbase"));
        enemyShipTextureRegion = new Sprite(textureAtlas.findRegion("enemyBlack1"));
        enemyShip2TextureRegion = new Sprite(textureAtlas.findRegion("enemyBlack2"));
        enemyShip3TextureRegion = new Sprite(textureAtlas.findRegion("enemyBlack4"));
        enemyShip4TextureRegion = new Sprite(textureAtlas.findRegion("enemyBlack3"));
        
        boss1TR = new Sprite(new Texture("playerShip2_white.png"));
        boss1TR2 = new Sprite(new Texture("playerShip2_white_red.png"));
        boss1Laser = textureAtlas.findRegion("laserRed09");
        boss1Laser2 = textureAtlas.findRegion("laserRed10");
        boss1Beam = textureAtlas.findRegion("laserRed16");
        boss1Beam.flip(false, true);
        boss1TR.flip(false, true);
        boss1TR2.flip(false, true);
        boss1Laser.flip(false, true);
        enemyShipTextureRegion.flip(false, true);
        enemyShip2TextureRegion.flip(false, true);
        enemyShip3TextureRegion.flip(false, true);
        enemyShip4TextureRegion.flip(false, true);
        
        playerShieldTextureRegion = textureAtlas.findRegion("shield2");
        enemyShieldTextureRegion = textureAtlas.findRegion("shield1");
        enemyTankShieldTextureRegion = textureAtlas.findRegion("shield3");
        enemyShieldTextureRegion.flip(false, true);
        enemyTankShieldTextureRegion.flip(false, true);

        playerLaserTextureRegion = textureAtlas.findRegion("laserBlue01");
        enemyLaserTextureRegion = textureAtlas.findRegion("laserRed01");
        enemyLaserTextureRegion.flip(false, true);

        explosionTexture = new Texture("explosion.png");
        godModeTexture = new Texture("godmode.png");
        enemygodModeTexture = new Texture("godmode.png");
        
        //damage vignette
        dmgv_blue = new Texture("bluevitt.png");
        dmgv_red = new Texture("redvitt.png");
        
        //bonus items
        items1 = textureAtlas.findRegion("powerupRed_bolt");
        items2 = textureAtlas.findRegion("powerupBlue_shield");
        items3 = textureAtlas.findRegion("powerupYellow_star");
        items4 = textureAtlas.findRegion("pill_red");
        items5 = textureAtlas.findRegion("star3");

        //set up game objects
        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
                8, 10,
                48, 3,
                0.9f, 9, 45, 0.5f,
                false, 2, 0, false,
                playerShipTextureRegion, playerShieldTextureRegion, godModeTexture, playerLaserTextureRegion);
        
        tempskin = playerShip.shipTextureRegion;
		tempshoot = playerShip.shoottype;
		tempbetween = playerShip.timeBetweenShots;
        
        enemyShipList = new LinkedList<>();
        bossShip1List = new LinkedList<>();
        boss1LaserList = new LinkedList<>();

        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();
        bonusitemsList = new LinkedList<>();

        explosionList = new LinkedList<>();
        
        dmgVittList = new LinkedList<>();

        batch = new SpriteBatch();

        prepareHUD();
    }

    private void prepareHUD() {
        //Create a BitmapFont from our font file
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);

        font = fontGenerator.generateFont(fontParameter);

        //scale the font to fit world
        font.getData().setScale(0.08f);

        //calculate hud margins, etc.
        hudVerticalMargin = font.getCapHeight() / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCentreX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        //scrolling background
        renderBackground(deltaTime);

        detectInput(deltaTime);
        playerShip.update(deltaTime);

        spawnEnemyShips(deltaTime);

        ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
        while (enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            moveEnemy(enemyShip, deltaTime);
            enemyShip.update(deltaTime);
            enemyShip.draw(batch);
        }
        
        ListIterator<BossShip1> bossShip1ListIterator = bossShip1List.listIterator();
        while (bossShip1ListIterator.hasNext()) {
            BossShip1 bossShip1 = bossShip1ListIterator.next();
            if (bossShip1.shoottype == 1.5) {
            	moveEnemyBossCharged(bossShip1, deltaTime);
            	bossShip1.update(deltaTime);
	            bossShip1.draw(batch);
            } else {
            	moveEnemyBoss(bossShip1, deltaTime);
	            bossShip1.update(deltaTime);
	            bossShip1.draw(batch);
            }
        }
        //player ship
        playerShip.draw(batch);
        
		//damage vignette
        renderdmgvitt(deltaTime);

        //lasers
        renderLasers(deltaTime);
        
        //boss system
        boss1System();
        
        updateRotate(deltaTime);
        
        updateRotatePlayer(deltaTime);
        
        //items
        renderitems(deltaTime);
        
        //sound
        renderSound(deltaTime);
        
        detectbosscol();

        //detect collisions between lasers and ships
        detectCollisions();

        //explosions
        updateAndRenderExplosions(deltaTime);
        
        //hud rendering
        updateAndRenderHUD();

        batch.end();
    }

    private void updateAndRenderHUD() {
        //render top row labels
        font.draw(batch, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(batch, "Shield", hudCentreX, hudRow1Y, hudSectionWidth, Align.center, false);
        font.draw(batch, "Lives", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);
        //render second row values
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.shield), hudCentreX, hudRow2Y, hudSectionWidth, Align.center, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.lives), hudRightX, hudRow2Y, hudSectionWidth, Align.right, false);
    }

    private void spawnEnemyShips(float deltaTime) {
        enemySpawnTimer += deltaTime;

        if (enemySpawnTimer > timeBetweenEnemySpawns) {
        	if (score <= 2000) {
        		enemyShipList.add(new EnemyShip(SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5,
                        WORLD_HEIGHT - 5,
                        10, 12,
                        48, 0,
                        0.7f, 7, 50, 0.8f,
                        false, 1, 100, false,
                        enemyShipTextureRegion, enemyShieldTextureRegion, enemygodModeTexture, enemyLaserTextureRegion, items1, items2, items3, items4, items5));
        	} else if (score > 2000 && score <= 8000) {
	            enemyShipList.add(new EnemyShip(SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5,
	                    WORLD_HEIGHT - 5,
	                    10, 12,
	                    48, 1,
	                    0.7f, 7, 50, 0.8f,
	                    false, 2, 250, false,
	                    enemyShip2TextureRegion, enemyShieldTextureRegion, enemygodModeTexture, enemyLaserTextureRegion, items1, items2, items3, items4, items5));
        	} else if (score > 8000 && score <= 15000) {
        		double randomSpawner = Math.random();
        		if (randomSpawner < 0.6) {
        			enemyShipList.add(new EnemyShip(SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5,
    	                    WORLD_HEIGHT - 5,
    	                    10, 12,
    	                    48, 3,
    	                    0.7f, 7, 50, 0.8f,
    	                    false, 2, 500, false,
    	                    enemyShip4TextureRegion, enemyShieldTextureRegion, enemygodModeTexture, enemyLaserTextureRegion, items1, items2, items3, items4, items5));
        			timeBetweenEnemySpawns = 1.5f;
        		} else {
        			enemyShipList.add(new EnemyShip(SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5,
    	                    WORLD_HEIGHT - 5,
    	                    13, 15,
    	                    48, 10,
    	                    0.7f, 7, 50, 0.8f,
    	                    false, 2, 1000, false,
    	                    enemyShip3TextureRegion, enemyTankShieldTextureRegion, enemygodModeTexture, enemyLaserTextureRegion, items1, items2, items3, items4, items5));
        			timeBetweenEnemySpawns = 3f;
        		}
        	} else if (score > 15000 && score <= 25000) {
        		bossShip1List.add(new BossShip1((WORLD_WIDTH/2) - 10 + 5,
	                    WORLD_HEIGHT - 5,
	                    50, 55,
	                    48, 520,
	                    5f, 7, 50, 0.8f,
	                    false, 2, 10000, false,
	                    boss1TR, enemyTankShieldTextureRegion, enemygodModeTexture, boss1Laser, items1, items2, items3, items4, items5));
    			timeBetweenEnemySpawns = 100f;
        	}
            enemySpawnTimer -= timeBetweenEnemySpawns;
        }
    }
    
    //static storage
    public static long tempphrase;
    public static long tempcharge;
    public static boolean tempchargelaserloop = false;
    public static boolean tempchargesound = false;
    public static boolean tempchargesoundloop = false;
    public static boolean tempbeamsound = false;
    public static boolean tempbeamsoundloop = false;
    public static boolean switchatk = false;
    //static damage vignette
    public static long timeVitt;
    
    private void boss1System() {
    	ListIterator<BossShip1> bossShip1ListIterator = bossShip1List.listIterator();
        while (bossShip1ListIterator.hasNext()) {
            BossShip1 bossShip1 = bossShip1ListIterator.next();
            if (bossShip1.shield >= 470) {
            	bossShip1.timeBetweenShots = 0.8f;
            	bossShip1.laserMovementSpeed = 40;
            	bossShip1.laserTextureRegion = enemyLaserTextureRegion;
            	bossShip1.laserWidth = 1.8f;
            	bossShip1.laserHeight = 15;
            	bossShip1.shoottype = 3;
            } else if (bossShip1.shield < 470 && bossShip1.shield >= 460) {
            	bossShip1.timeBetweenShots = 0.8f;
            	bossShip1.laserMovementSpeed = 50;
            	bossShip1.laserTextureRegion = boss1Laser2;
            	bossShip1.laserWidth = 10f;
            	bossShip1.laserHeight = 10f;
            	bossShip1.shoottype = 3.5f;
            } else if (bossShip1.shield < 470 && bossShip1.shield >= 430) {
    			bossShip1.timeBetweenShots = 0.65f;
    			bossShip1.laserMovementSpeed = 45;
    			bossShip1.laserTextureRegion = boss1Laser;
            	bossShip1.laserWidth = 13f;
            	bossShip1.laserHeight = 13f;
            	bossShip1.shoottype = 2;
            	tempphrase = System.currentTimeMillis();
            } else if (bossShip1.shield < 430 && bossShip1.shield >= 300) {
            	long time = System.currentTimeMillis();
            	if (time < tempphrase + 2000) {
	            	bossShip1.timeBetweenShots = 0.8f;
	            	bossShip1.laserMovementSpeed = 50;
	            	bossShip1.laserTextureRegion = boss1Laser2;
	            	bossShip1.laserWidth = 10f;
	            	bossShip1.laserHeight = 10f;
	            	bossShip1.shoottype = 3.5f;
            	} else if (time < tempphrase + 4000){
            		bossShip1.timeBetweenShots = 0.8f;
                	bossShip1.laserMovementSpeed = 40;
                	bossShip1.laserTextureRegion = enemyLaserTextureRegion;
                	bossShip1.laserWidth = 1.8f;
                	bossShip1.laserHeight = 15;
                	bossShip1.shoottype = 3;
            	} else if (time >= tempphrase + 4000) {
            		tempphrase = System.currentTimeMillis();
            	}
            } else if (bossShip1.shield < 300 && bossShip1.shield >= 265) {
            	tempphrase = System.currentTimeMillis();
            	bossShip1.timeSinceLastShot = 0f;
            	bossShip1.shipTextureRegion = boss1TR2;
            	if (tempchargesound == false) {
            		chargelaser.play(0.6f);
            		tempchargesound = true;
            		tempcharge = System.currentTimeMillis();
            	}
            } else if (bossShip1.shield < 265 && bossShip1.shield >= 195) {
            	if (switchatk == false) {
            		bosslaserbeamloop.play(0.1f);
            		bosslaserbeamloop.loop();
            		bossShip1.timeSinceLastShot = 999f;
            		bossShip1.timeSinceLastDirectionChange = 0;
            		switchatk = !switchatk;
            	}
            	chargelaser.stop();
        		chargelaserloop.stop();
            	bossShip1.timeBetweenShots = 100f;
            	bossShip1.laserMovementSpeed = 40;
            	bossShip1.movementSpeed = 15;
            	bossShip1.laserTextureRegion = boss1Beam;
            	bossShip1.laserWidth = 30f;
            	bossShip1.laserHeight = 150f;
            	bossShip1.shoottype = 1.5f;
            } else if (bossShip1.shield <= 100) {
            	bosslaserbeamloop.stop();
            	bossShip1.timeBetweenShots = 0.8f;
            	bossShip1.laserMovementSpeed = 50;
            	bossShip1.laserTextureRegion = boss1Laser2;
            	bossShip1.laserWidth = 10f;
            	bossShip1.laserHeight = 10f;
            	bossShip1.shoottype = 6;
            }
        }
    }
    
    private void renderSound(float deltaTime) {
    	if (tempchargesound == true) {
    		long time = System.currentTimeMillis();
    		if (time > tempcharge + 8000 && tempchargelaserloop == false) {
    			tempchargelaserloop = true;
    			chargelaserloop.play(0.3f);
    			chargelaserloop.loop();
    		}
    	}
    }

    private void detectInput(float deltaTime) {
        //keyboard input

        //strategy: determine the max distance the ship can move
        //check each key that matters and move accordingly

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = (float) WORLD_HEIGHT-(WORLD_HEIGHT/4) - playerShip.boundingBox.y - playerShip.boundingBox.height;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            playerShip.translate(Math.min(playerShip.movementSpeed * deltaTime, rightLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            playerShip.translate(0f, Math.min(playerShip.movementSpeed * deltaTime, upLimit));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            playerShip.translate(Math.max(-playerShip.movementSpeed * deltaTime, leftLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            playerShip.translate(0f, Math.max(-playerShip.movementSpeed * deltaTime, downLimit));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
        	score += 100; 
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F2)) {
        	ListIterator<BossShip1> bossShip1ListIterator = bossShip1List.listIterator();
	        while (bossShip1ListIterator.hasNext()) {
	            BossShip1 bossShip1 = bossShip1ListIterator.next();
	            bossShip1.shield = 300;
	        }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F3)) {
        	ListIterator<BossShip1> bossShip1ListIterator = bossShip1List.listIterator();
	        while (bossShip1ListIterator.hasNext()) {
	            BossShip1 bossShip1 = bossShip1ListIterator.next();
	            bossShip1.shield = 6;
	        }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
			Gdx.app.exit();

        //touch input (also mouse)
        if (Gdx.input.isTouched()) {
            //get the screen position of the touch
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            //convert to world position
            Vector2 touchPoint = new Vector2(xTouchPixels, yTouchPixels);
            touchPoint = viewport.unproject(touchPoint);

            //calculate the x and y differences
            Vector2 playerShipCentre = new Vector2(
                    playerShip.boundingBox.x + playerShip.boundingBox.width / 2,
                    playerShip.boundingBox.y + playerShip.boundingBox.height / 2);

            float touchDistance = touchPoint.dst(playerShipCentre);
//            if (touchDistance > TOUCH_MOVEMENT_THRESHOLD) {
//                float xTouchDifference = touchPoint.x - playerShipCentre.x;
//                float yTouchDifference = touchPoint.y - playerShipCentre.y;
//
//                //scale to the maximum speed of the ship
//                float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
//                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
//                
//                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
//                else xMove = Math.max(xMove, leftLimit);
//
//                if (yMove > 0) yMove = Math.min(yMove, upLimit);
//                else yMove = Math.max(yMove, downLimit);
//
//                playerShip.translate(xMove, yMove);
//            }
            if (touchDistance < TOUCH_MOVEMENT_THRESHOLD) {
                float xTouchDifference = touchPoint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;

                //scale to the maximum speed of the ship
                float xMove = xTouchDifference;
                float yMove = yTouchDifference;
                
                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);

                if (yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);

                playerShip.translate(xMove, yMove);
            }
        }
    }
    
    private void moveEnemyBoss(BossShip1 bossShip1, float deltaTime) {
        //strategy: determine the max distance the ship can move

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -bossShip1.boundingBox.x-5;
        downLimit = (float) WORLD_HEIGHT / 2 - bossShip1.boundingBox.y;
        rightLimit = WORLD_WIDTH - bossShip1.boundingBox.x - bossShip1.boundingBox.width+5;
        upLimit = WORLD_HEIGHT - bossShip1.boundingBox.y - bossShip1.boundingBox.height;

        float xMove = bossShip1.getDirectionVector().x * bossShip1.movementSpeed * deltaTime;
        float yMove = bossShip1.getDirectionVector().y * bossShip1.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        if (yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        bossShip1.translate(xMove, yMove);
    }
    
    private void moveEnemyBossCharged(BossShip1 bossShip1, float deltaTime) {
        //strategy: determine the max distance the ship can move

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -bossShip1.boundingBox.x-1;
        downLimit = (float) WORLD_HEIGHT / 2 - bossShip1.boundingBox.y;
        rightLimit = WORLD_WIDTH - bossShip1.boundingBox.x - bossShip1.boundingBox.width+1;
        upLimit = WORLD_HEIGHT - bossShip1.boundingBox.y - bossShip1.boundingBox.height;

        float xMove = bossShip1.getDirectionVector().x * bossShip1.movementSpeed * deltaTime;
        float yMove = bossShip1.getDirectionVector().y * bossShip1.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        if (yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        bossShip1.translate(xMove, yMove);
    }

    private void moveEnemy(EnemyShip enemyShip, float deltaTime) {
        //strategy: determine the max distance the ship can move

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -enemyShip.boundingBox.x;
        downLimit = (float) WORLD_HEIGHT / 2 - enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        if (yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        enemyShip.translate(xMove, yMove);
    }
    public static long temptimeshoot;
    public static long temptimegod;
    public static long temptime2;
    private void detectbosscol() { 
    	ListIterator<Laser> laserListIterator = playerLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
	    	ListIterator<BossShip1> bossShip1ListIterator = bossShip1List.listIterator();
	        while (bossShip1ListIterator.hasNext()) {
	            BossShip1 bossShip1 = bossShip1ListIterator.next();
	
	            if (bossShip1.intersects(laser.boundingBox)) {
	                if (bossShip1.hitAndCheckDestroyed(laser)) {
	                	double randomBonus = Math.random();
	                	if (randomBonus < 0) {
	                		Bonusitems[] bonusitems = bossShip1.dropitems();
	                        bonusitemsList.addAll(Arrays.asList(bonusitems));
	                	}
	                    bossShip1ListIterator.remove();
	                    explosionList.add(
	                            new Explosion(explosionTexture,
	                                    new Rectangle(bossShip1.boundingBox),
	                                    0.7f));
	                    score += bossShip1.score;
	                    this.dispose();
                    	game.gotoWinScreen(score, gameTimer);
                    	return;
	                    //end
	                }
	                laserListIterator.remove();
	                break;
	            } else if (bossShip1.intersects(playerShip.boundingBox)) {
	            	if (bossShip1.hitAndCheckDestroyed(playerShip)) {
                    	if (bossShip1.shield <= 0) {
                    		bossShip1ListIterator.remove();
    	                	Sound boom4 = Gdx.audio.newSound(Gdx.files.internal("bomb4.ogg"));
    	                    boom4.play(1f);
                    		explosionList.add(
    	                            new Explosion(explosionTexture,
    	                                new Rectangle(bossShip1.boundingBox),
    	                                0.7f));
                    	}
                        if (playerShip.shield < 0) {
                            explosionList.add(
                                    new Explosion(explosionTexture,
                                            new Rectangle(playerShip.boundingBox),
                                            1.6f));
                            playerShip.shield = 3;
                            if (playerShip.lives > 0) {
                                Sound boom3 = Gdx.audio.newSound(Gdx.files.internal("bomb3.ogg"));
                                boom3.play(1f);
                            	playerShip.lives--; 
                            	playerShip.godmode = true;
                            	temptimegod = System.currentTimeMillis();
                            } else {
    	                    	this.dispose();
    	                    	game.gotoSummaryScreen(score, gameTimer);
    	                    	return; //GameOver
                            }
                        }
                        score += bossShip1.score;
                        this.dispose();
                    	game.gotoWinScreen(score, gameTimer);
                    	return; 
                        //end
	            	} else {
	            		if (playerShip.shield < 0) {
                            explosionList.add(
                                    new Explosion(explosionTexture,
                                            new Rectangle(playerShip.boundingBox),
                                            1.6f));
                            playerShip.shield = 3;
                            if (playerShip.lives > 0) {
                                Sound boom3 = Gdx.audio.newSound(Gdx.files.internal("bomb3.ogg"));
                                boom3.play(1f);
                            	playerShip.lives--; 
                            	playerShip.godmode = true;
                            	temptimegod = System.currentTimeMillis();
                            } else {
    	                    	this.dispose();
    	                    	game.gotoSummaryScreen(score, gameTimer);
//    	                    	game.gotoOverScreen();
    	                    	return; //GameOver
                            }
                        }
	            	}
	            }
	        }
        }
    }
    private void detectCollisions() {
        //for each player laser, check whether it intersects an enemy ship
        ListIterator<Laser> laserListIterator = playerLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();

                if (enemyShip.intersects(laser.boundingBox)) {
                    //contact with enemy ship
                    if (enemyShip.hitAndCheckDestroyed(laser)) {
                    	double randomBonus = Math.random();
                    	if (randomBonus < 0.1) {
                    		Bonusitems[] bonusitems = enemyShip.dropitems();
                            bonusitemsList.addAll(Arrays.asList(bonusitems));
                    	}
                        enemyShipListIterator.remove();
                        explosionList.add(
                                new Explosion(explosionTexture,
                                        new Rectangle(enemyShip.boundingBox),
                                        0.7f));
                        score += enemyShip.score;
                        
                    	double randomBoom = Math.random();
                    	if (randomBoom < 0.5) {
                    		 Sound boom1 = Gdx.audio.newSound(Gdx.files.internal("bomb1.ogg"));
                             boom1.play(1f);
                    	}else {
                   		 Sound boom2 = Gdx.audio.newSound(Gdx.files.internal("bomb2.ogg"));
                         boom2.play(1f);
                    	}
                       
                    }
                    laserListIterator.remove();
                    break;
                } else if (enemyShip.intersects(playerShip.boundingBox)) {
                	if (enemyShip.hitAndCheckDestroyed(playerShip)) {
                    	if (enemyShip.shield <= 0) {
                        	enemyShipListIterator.remove();
                            explosionList.add(
                                new Explosion(explosionTexture,
	                                new Rectangle(enemyShip.boundingBox),
	                                0.7f));
                        	double randomBoom = Math.random();
                        	if (randomBoom < 0.5) {
                        		 Sound boom1 = Gdx.audio.newSound(Gdx.files.internal("bomb1.ogg"));
                                 boom1.play(1f);
                        	}else {
                       		 Sound boom2 = Gdx.audio.newSound(Gdx.files.internal("bomb2.ogg"));
                             boom2.play(1f);
                        	}
                    	}
                    	if (playerShip.shield < 0) {
                            explosionList.add(
                                    new Explosion(explosionTexture,
                                            new Rectangle(playerShip.boundingBox),
                                            1.6f));
                            playerShip.shield = 3;
                            if (playerShip.lives > 0) {
                                Sound boom3 = Gdx.audio.newSound(Gdx.files.internal("bomb3.ogg"));
                                boom3.play(1f);
                            	playerShip.lives--; 
                            	playerShip.godmode = true;
                            	temptimegod = System.currentTimeMillis();
                            } else {
    	                    	this.dispose();
    	                    	game.gotoSummaryScreen(score, gameTimer);
//    	                    	game.gotoOverScreen();
    	                    	return; //GameOver
                            }
                        }
                        score += enemyShip.score;
                	} else {
                		if (playerShip.shield < 0) {
                            explosionList.add(
                                    new Explosion(explosionTexture,
                                            new Rectangle(playerShip.boundingBox),
                                            1.6f));
                            playerShip.shield = 3;
                            if (playerShip.lives > 0) {
                                Sound boom3 = Gdx.audio.newSound(Gdx.files.internal("bomb3.ogg"));
                                boom3.play(1f);
                            	playerShip.lives--; 
                            	playerShip.godmode = true;
                            	temptimegod = System.currentTimeMillis();
                            } else {
    	                    	this.dispose();
    	                    	game.gotoSummaryScreen(score, gameTimer);
//    	                    	game.gotoOverScreen();
    	                    	return; //GameOver
                            }
                        }
                	}
                }
            }
        }
        
        //for each enemy laser, check whether it intersects the player ship
        laserListIterator = enemyLaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            if (playerShip.intersects(laser.boundingBox)) {
                //contact with player ship
                if (playerShip.hitAndCheckDestroyed(laser)) {
                	Sound boom3 = Gdx.audio.newSound(Gdx.files.internal("bomb3.ogg"));
                    boom3.play(1f);
                    explosionList.add(
                            new Explosion(explosionTexture,
                                    new Rectangle(playerShip.boundingBox),
                                    1.6f));
                    playerShip.shield = 3;
                    if (playerShip.lives > 0) {
                    	playerShip.lives--; 
                    	playerShip.godmode = true;
                    	temptimegod = System.currentTimeMillis();
                    } else {
                    	this.dispose();
                    	game.gotoSummaryScreen(score, gameTimer);
//                    	game.gotoOverScreen();
                    	return; //GameOver
                    }
                } else {
                	if (dmgVittList.isEmpty() && playerShip.shield != 0) {
                		dmgVittList.add(new DmgVitt(dmgv_blue, 0, -80, WORLD_WIDTH, backgroundHeight));
                		timeVitt = System.currentTimeMillis();
                	}
                	Sound shielddown = Gdx.audio.newSound(Gdx.files.internal("sfx_shieldDown.ogg"));
                	shielddown.play(0.5f);
                }
                laserListIterator.remove();
            }
        }
        
      //for each boss laser
        laserListIterator = boss1LaserList.listIterator();
        while (laserListIterator.hasNext()) {
            Laser laser = laserListIterator.next();
            if (laser.movetype == 100) {
            	if (playerShip.intersects(laser.boundingBox)) {
            		if (playerShip.hitAndCheckDestroyed(laser)) {
                    	Sound boom3 = Gdx.audio.newSound(Gdx.files.internal("bomb3.ogg"));
                        boom3.play(1f);
	                    explosionList.add(
	                            new Explosion(explosionTexture,
	                                    new Rectangle(playerShip.boundingBox),
	                                    1.6f));
	                    playerShip.shield = 3;
	                    if (playerShip.lives > 0) {
	                    	playerShip.lives--; 
	                    	playerShip.godmode = true;
	                    	temptimegod = System.currentTimeMillis();
	                    } else {
	                    	this.dispose();
	                    	game.gotoSummaryScreen(score, gameTimer);
//	                    	game.gotoOverScreen();
	                    	return; //GameOver
	                    }
	                }
            	}
            } else {
	            if (playerShip.intersects(laser.boundingBox)) {
	                //contact with player ship
	                if (playerShip.hitAndCheckDestroyed(laser)) {
	                	Sound boom3 = Gdx.audio.newSound(Gdx.files.internal("bomb3.ogg"));
	                    boom3.play(1f);
	                    explosionList.add(
	                            new Explosion(explosionTexture,
	                                    new Rectangle(playerShip.boundingBox),
	                                    1.6f));
	                    playerShip.shield = 3;
	                    if (playerShip.lives > 0) {
	                    	playerShip.lives--; 
	                    	playerShip.godmode = true;
	                    	temptimegod = System.currentTimeMillis();
	                    } else {
	                    	this.dispose();
	                    	game.gotoSummaryScreen(score, gameTimer);
//	                    	game.gotoOverScreen();
	                    	return; //GameOver
	                    }
	                } else {
	                	if (dmgVittList.isEmpty() && playerShip.shield != 0) {
	                		dmgVittList.add(new DmgVitt(dmgv_blue, 0, -80, WORLD_WIDTH, backgroundHeight));
	                		timeVitt = System.currentTimeMillis();
	                	}
	                	Sound shielddown = Gdx.audio.newSound(Gdx.files.internal("sfx_shieldDown.ogg"));
	                	shielddown.play(0.5f);
	                }
	                laserListIterator.remove();
	            }
            }
        }
        
        //bonus detection
        ListIterator<Bonusitems> bonusListIterator = bonusitemsList.listIterator();
        while (bonusListIterator.hasNext()) {
        	Bonusitems bonus = bonusListIterator.next();
        	if (playerShip.intersects(bonus.boundingBox)) {
        		if (bonus.btype == 1) {
        			temptime2 = System.currentTimeMillis();
            		playerShip.superform = true;
        		} else if (bonus.btype == 2) {
        			playerShip.shield += 5;
        		} else if (bonus.btype == 4) {
        			playerShip.lives += 1;
        		} else if (bonus.btype == 5) {
        			playerShip.godmode = true;
        			playerShip.timeBetweenShots = 0.2f;
        			temptimegod = System.currentTimeMillis();
                	temptimeshoot = System.currentTimeMillis();
        		}
        		
        		bonusListIterator.remove();
        	}
        }
    }
    
    private void updateRotate (float deltaTime) {
    	ListIterator<Laser> iterator = boss1LaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            if (laser.movetype == 360) {
            	if (laser.rotation <= 360) {
            		laser.rotation += 4;
            	} else {
            		laser.rotation = 0;
            	}
            } else if (laser.movetype == 3601 || laser.movetype == 3602 || laser.movetype == 3603) {
            	if (laser.rotation <= 360) {
            		laser.rotation += 4;
            	} else {
            		laser.rotation = 0;
            	}
            } else if (laser.movetype == 6601 || laser.movetype == 6602 || laser.movetype == 6603 || laser.movetype == 6604 || laser.movetype == 6605 || laser.movetype == 6606) {
            	if (laser.rotation <= 360) {
            		laser.rotation += 4;
            	} else {
            		laser.rotation = 0;
            	}
            }
        }
    }
    
    private void updateRotatePlayer (float deltaTime) {
    	ListIterator<Laser> iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            if (laser.movetype == 360) {
            	if (laser.rotation <= 360) {
            		laser.rotation += 4;
            	} else {
            		laser.rotation = 0;
            	}
            }
        }
    }

    private void updateAndRenderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while (explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            if (explosion.isFinished()) {
                explosionListIterator.remove();
            } else {
                explosion.draw(batch);
            }
        }
    }
    
    private void renderitems(float deltaTime) {
    	if (bonusitemsList != null) {
			ListIterator<Bonusitems> bonusListIterator = bonusitemsList.listIterator();
			while (bonusListIterator.hasNext()) {
				Bonusitems bonusitem = bonusListIterator.next();
	    		bonusitem.draw(batch);
	    		bonusitem.boundingBox.y -= bonusitem.movementSpeed * deltaTime;
	            if (bonusitem.boundingBox.y + bonusitem.boundingBox.height < 0) {
	            	bonusListIterator.remove();
	            }
	        }
    	}
    }
    
    private void renderdmgvitt(float deltaTime) {
    	if (playerShip.godmode == true) {
			dmgVittList.clear();
		}
    	if (playerShip.shield == 0) {
    		if (dmgVittList.isEmpty()) {
    			dmgVittList.add(new DmgVitt(dmgv_red, 0, -80, WORLD_WIDTH, backgroundHeight));
    		}
    	}
    	if (dmgVittList != null) {
	    	ListIterator<DmgVitt> iterator = dmgVittList.listIterator();
	        while (iterator.hasNext()) {
	            DmgVitt dmgvitt = iterator.next();
	            dmgvitt.draw(batch);
	        }
	        long time = System.currentTimeMillis();
			if (time > timeVitt + 250) {
				dmgVittList.clear();
			}
    	}
    }

    private void renderLasers(float deltaTime) {
    	if (playerShip.timeBetweenShots == 0.2f) {
    		long time = System.currentTimeMillis();
    		if (time > temptimeshoot + 5000) {
    			playerShip.timeBetweenShots = tempbetween;
    		}
    	}
    	if (playerShip.superform == true) {
	    	long time = System.currentTimeMillis();
			if (time < temptime2 + 15000) {
				playerShip.shipTextureRegion = new Sprite(textureAtlas.findRegion("Shipnew"));
	    		playerShip.shoottype = 3;
			} else {
				playerShip.shipTextureRegion = tempskin;
	    		playerShip.shoottype = tempshoot;
			}
    	}
    	if (playerShip.godmode == true) {
    		long time = System.currentTimeMillis();
    		if (time > temptimegod + 3000) {
    			playerShip.godmode = false;
    		}
    	}
    	
        //create new lasers
        //player lasers
        if (playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers();
            player_S1.play(1.0f);
            playerLaserList.addAll(Arrays.asList(lasers));
        }
        //enemy lasers
        ListIterator<EnemyShip> enemyShipListIterator = enemyShipList.listIterator();
        while (enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            if (enemyShip.canFireLaser()) {
                Laser[] lasers = enemyShip.fireLasers();
                enemy_S1.play(0.5f);
                enemyLaserList.addAll(Arrays.asList(lasers));
            }
        }
        //bosses lasers
        ListIterator<BossShip1> bossShip1ListIterator = bossShip1List.listIterator();
        while (bossShip1ListIterator.hasNext()) {
            BossShip1 bossShip1 = bossShip1ListIterator.next();
            if (bossShip1.canFireLaser()) {
                Laser[] lasers = bossShip1.fireLasers();
                enemy_S1.play(0.5f);
                boss1LaserList.addAll(Arrays.asList(lasers));
            }
        }
        //draw lasers
        //remove old lasers
        ListIterator<Laser> iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y > WORLD_HEIGHT) {
                iterator.remove();
            }
        }
        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                iterator.remove();
            }
        }
        iterator = boss1LaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            if (laser.movetype == 1) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
        	} else if (laser.movetype == 2) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x -= laser.movementSpeed * deltaTime;
            } else if (laser.movetype == 3) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x += laser.movementSpeed * deltaTime;
            } else if (laser.movetype == 3601) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
        	} else if (laser.movetype == 3602) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x -= laser.movementSpeed/3 * deltaTime;
            } else if (laser.movetype == 3603) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x += laser.movementSpeed/3 * deltaTime;
            } else if (laser.movetype == 100) {
            	ListIterator<BossShip1> bossShip1ListIterator2 = bossShip1List.listIterator();
                while (bossShip1ListIterator2.hasNext()) {
                    BossShip1 bossShip1 = bossShip1ListIterator2.next();
                    float bounding = bossShip1.boundingBox.x + bossShip1.boundingBox.width * 0.50f;
                    laser.boundingBox.y = bossShip1.boundingBox.y - bossShip1.laserHeight;
                    laser.boundingBox.x = bounding - bossShip1.laserWidth / 2;
                    if (bossShip1.shield <= 100) {
                    	iterator.remove();
                    }
                }
            } else if (laser.movetype == 6601) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x += laser.movementSpeed/3 * deltaTime;
            } else if (laser.movetype == 6602) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x += laser.movementSpeed/3 * deltaTime;
            } else if (laser.movetype == 6603) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x += laser.movementSpeed/3 * deltaTime;
            } else if (laser.movetype == 6604) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x -= laser.movementSpeed/3 * deltaTime;
            } else if (laser.movetype == 6605) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x -= laser.movementSpeed/3 * deltaTime;
            } else if (laser.movetype == 6606) {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            	laser.boundingBox.x -= laser.movementSpeed/3 * deltaTime;
        	} else {
            	laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            }
            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                iterator.remove();
            }
        }
    }

    private void renderBackground(float deltaTime) {

        //update position of background images
        backgroundOffsets[0] += deltaTime * backgroundMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollingSpeed / 2;

        //draw each background layer
        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            if (backgroundOffsets[layer] > WORLD_HEIGHT) {
                backgroundOffsets[layer] = 0;
            }
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer],
                    WORLD_WIDTH, backgroundHeight);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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
    public void show() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}