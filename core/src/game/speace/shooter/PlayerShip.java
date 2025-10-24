package game.speace.shooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

class PlayerShip extends Ship {

    int lives;
    Vector2 directionVector;

    public PlayerShip(float xCentre, float yCentre,
                      float width, float height,
                      float movementSpeed, int shield,
                      float laserWidth, float laserHeight,
                      float laserMovementSpeed, float timeBetweenShots, boolean godmode, float shoottype, float score, boolean superform,
                      Sprite shipTextureRegion,
                      TextureRegion shieldTextureRegion, Texture godModeTexture,
                      TextureRegion laserTextureRegion) {
        super(xCentre, yCentre, width, height, movementSpeed, shield, laserWidth, laserHeight, laserMovementSpeed, 
        		timeBetweenShots, godmode, shoottype, score, superform, shipTextureRegion, shieldTextureRegion, godModeTexture, 
        		laserTextureRegion, laserTextureRegion, laserTextureRegion, laserTextureRegion, laserTextureRegion, laserTextureRegion);
        lives = 3;
        directionVector = new Vector2(0, -1);
    }
    
    @Override
    public Bonusitems[] dropitems() {
    	Bonusitems[] dropdirect = new Bonusitems[1];
    	dropdirect[0] = new Bonusitems(boundingBox.x + boundingBox.width * 0.50f, boundingBox.y - 20,
                20, 20,
                10, laserTextureRegion, 0);
    	return dropdirect;
    }

    @Override
    public Laser[] fireLasers() {
    	int num_s = (int) shoottype;
    	Laser[] laser = new Laser[num_s];
    	if (shoottype == 2) {
    		laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.07f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[1] = new Laser(boundingBox.x + boundingBox.width * 0.93f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
    	} else if (shoottype == 3) {
    		laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.25f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[1] = new Laser(boundingBox.x + boundingBox.width * 0.50f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[2] = new Laser(boundingBox.x + boundingBox.width * 0.75f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
    	} else if (shoottype == 4) {
    		laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.20f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[1] = new Laser(boundingBox.x + boundingBox.width * 0.40f, boundingBox.y + boundingBox.height * 0.65f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[2] = new Laser(boundingBox.x + boundingBox.width * 0.60f, boundingBox.y + boundingBox.height * 0.65f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[3] = new Laser(boundingBox.x + boundingBox.width * 0.80f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
    	} else if (shoottype == 5) {
    		laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.15f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[1] = new Laser(boundingBox.x + boundingBox.width * 0.30f, boundingBox.y + boundingBox.height * 0.65f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[2] = new Laser(boundingBox.x + boundingBox.width * 0.45f, boundingBox.y + boundingBox.height * 0.85f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[3] = new Laser(boundingBox.x + boundingBox.width * 0.60f, boundingBox.y + boundingBox.height * 0.65f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[4] = new Laser(boundingBox.x + boundingBox.width * 0.75f, boundingBox.y + boundingBox.height * 0.45f,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
    	} else if (shoottype > 5) {
    		float guns = 0f;
    		for (int i = 0; i < num_s; i++) {
				laser[i] = new Laser(boundingBox.x + boundingBox.width * (guns), boundingBox.y + boundingBox.height * 0.45f,
	                laserWidth, laserHeight,
	                laserMovementSpeed, laserTextureRegion, 1, 0);
				guns += 0.1f;
    		}
    	}

        timeSinceLastShot = 0;

        return laser;
    }
}