package game.speace.shooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

abstract class Ship {

    //ship characteristics
    float movementSpeed;  //world units per second
    int shield;

    //position & dimension
    Rectangle boundingBox;

    //laser information
    float laserWidth, laserHeight;
    float laserMovementSpeed;
    float timeBetweenShots;
    float timeSinceLastShot = 0;
    
    //Score
    float score;
    
    //Type
    float shoottype;
    
    //items
    TextureRegion items1;
    TextureRegion items2;
    TextureRegion items3;
    TextureRegion items4;
    TextureRegion items5;
    
    //Drop
    boolean superform = false;
    
    //god mode
    boolean godmode = false;

    //graphics
    Sprite shipTextureRegion;
    TextureRegion shieldTextureRegion, laserTextureRegion;
    Texture godModeTexture;

    public Ship(float xCentre, float yCentre,
                float width, float height,
                float movementSpeed, int shield,
                float laserWidth, float laserHeight, float laserMovementSpeed,
                float timeBetweenShots, boolean godmode, float shoottype, float score, boolean superform,
                Sprite shipTextureRegion, TextureRegion shieldTextureRegion, Texture godModeTexture,
                TextureRegion laserTextureRegion, TextureRegion items1, TextureRegion items2, TextureRegion items3, TextureRegion items4, TextureRegion items5) {
        this.movementSpeed = movementSpeed;
        this.shield = shield;
        this.godmode = godmode;
        this.superform = superform;

        this.boundingBox = new Rectangle(xCentre - width / 2, yCentre - height / 2, width, height);

        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.shipTextureRegion = shipTextureRegion;
        this.shieldTextureRegion = shieldTextureRegion;
        this.godModeTexture = godModeTexture;
        this.laserTextureRegion = laserTextureRegion;
        this.shoottype = shoottype;
        this.score = score;
        this.items1 = items1;
        this.items2 = items2;
        this.items3 = items3;
        this.items4 = items4;
        this.items5 = items5;
    }

	public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser() {
        return (timeSinceLastShot - timeBetweenShots >= 0);
    }

    public abstract Laser[] fireLasers();
    
    public abstract Bonusitems[] dropitems();

    public boolean intersects(Rectangle otherRectangle) {
        return boundingBox.overlaps(otherRectangle);
    }

    public boolean hitAndCheckDestroyed(Laser laser) {
    	if (godmode) {
    		return false;
    	}
        if (shield > 0) {
            shield--;
            return false;
        }
        return true;
    }

    public boolean hitAndCheckDestroyed(PlayerShip playerShip) {
    	if (playerShip.godmode) {
    		return false;
    	}
        if (shield > playerShip.shield) {
    		shield = shield - playerShip.shield;
        	playerShip.shield -= playerShip.shield + 1;
        	return false;
        } else if (shield < playerShip.shield){
        	if (shield == 0) {
        		if (playerShip.shield > 0) {
        			playerShip.shield--;
        		}
            	return true;
            } else {
	        	playerShip.shield = playerShip.shield - shield;
	        	shield = 0;
	        	return true;
            }
        } else if (shield == playerShip.shield) {
        	shield -= playerShip.shield;
        	playerShip.shield -= playerShip.shield + 1;
        	return true;
        }
        return true;
    }
    
    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x+xChange, boundingBox.y+yChange);
    }

    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, boundingBox.x-2, boundingBox.y-3, boundingBox.width+4, boundingBox.height+6);
        }
        if (godmode == true) {
        	batch.draw(godModeTexture, boundingBox.x-2, boundingBox.y-4, boundingBox.width+4, boundingBox.height+8);
        }
    }
}
