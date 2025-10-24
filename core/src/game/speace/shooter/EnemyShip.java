package game.speace.shooter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

class EnemyShip extends Ship {

    Vector2 directionVector;
    float timeSinceLastDirectionChange = 0;
    float directionChangeFrequency = 0.75f;
    

    public EnemyShip(float xCentre, float yCentre,
                     float width, float height,
                     float movementSpeed, int shield,
                     float laserWidth, float laserHeight,
                     float laserMovementSpeed, float timeBetweenShots, boolean godmode, float shoottype, float score, boolean superform,
                     Sprite enemyShipTextureRegion,
                     TextureRegion shieldTextureRegion, Texture godModeTexture,
                     TextureRegion laserTextureRegion, TextureRegion items1, TextureRegion items2, TextureRegion items3, TextureRegion items4, TextureRegion items5) {
        super(xCentre, yCentre, width, height, movementSpeed, shield, laserWidth, laserHeight, laserMovementSpeed, timeBetweenShots, godmode, shoottype, score, superform, enemyShipTextureRegion, shieldTextureRegion, godModeTexture, laserTextureRegion, items1, items2, items3, items4, items5);

        directionVector = new Vector2(0, -1);
    }

    public Vector2 getDirectionVector() {
        return directionVector;
    }

    private void randomizeDirectionVector() {
        double bearing = SpaceShooterGame.random.nextDouble()*6.283185; //0 to 2*PI
        directionVector.x = (float)Math.sin(bearing);
        directionVector.y = (float)Math.cos(bearing);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            randomizeDirectionVector();
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }
    }
    
    @Override
    public Bonusitems[] dropitems() {
    	Bonusitems[] dropdirect = new Bonusitems[1];
    	double randomBonus = Math.random();
    	if (randomBonus <= 0.25) {
    		dropdirect[0] = new Bonusitems(boundingBox.x + boundingBox.width * 0.50f, boundingBox.y - 5,
                    5, 6,
                    10, items1, 1);
    	} else if (randomBonus > 0.25 && randomBonus <= 0.50) {
    		dropdirect[0] = new Bonusitems(boundingBox.x + boundingBox.width * 0.50f, boundingBox.y - 5,
                    5, 6,
                    10, items2, 2);
    	} else if (randomBonus > 0.50 && randomBonus <= 0.75) {
    		dropdirect[0] = new Bonusitems(boundingBox.x + boundingBox.width * 0.50f, boundingBox.y - 5,
                    5, 6,
                    10, items4, 4);
    	} else if (randomBonus > 0.75 && randomBonus <= 1) {
    		dropdirect[0] = new Bonusitems(boundingBox.x + boundingBox.width * 0.50f, boundingBox.y - 5,
                    6, 6,
                    10, items5, 5);
    	}
    	return dropdirect;
    }

    @Override
    public Laser[] fireLasers() {
    	int num_s = (int) shoottype;
    	Laser[] laser = new Laser[num_s];
    	if (shoottype == 1) {
            laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.50f, boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
    	} else if (shoottype == 2) {
            laser[0] = new Laser(boundingBox.x + boundingBox.width * 0.18f, boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
            laser[1] = new Laser(boundingBox.x + boundingBox.width * 0.82f, boundingBox.y - laserHeight,
                    laserWidth, laserHeight,
                    laserMovementSpeed, laserTextureRegion, 1, 0);
    	}

        timeSinceLastShot = 0;

        return laser;
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(shipTextureRegion, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if (shield > 0) {
            batch.draw(shieldTextureRegion, boundingBox.x-2, boundingBox.y-3 - boundingBox.height * 0.2f, boundingBox.width+4, boundingBox.height+6);
        }
    }
}