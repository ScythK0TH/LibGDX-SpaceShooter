package game.speace.shooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

class Laser {

    //position and dimensions
    Rectangle boundingBox;

    //laser physical characteristics
    float movementSpeed; //world units per second

    //graphics
    TextureRegion textureRegion;
    
    //rotations
    float rotation;
    
    //move type
    float movetype;

    public Laser(float xCentre, float yBottom, float width, float height, float movementSpeed, TextureRegion textureRegion, float movetype, float rotation) {
        this.boundingBox = new Rectangle(xCentre - width / 2, yBottom, width, height);

        this.movementSpeed = movementSpeed;
        this.textureRegion = textureRegion;
        this.movetype = movetype;
        this.rotation = rotation;
    }

    public void draw(Batch batch) {
        batch.draw(textureRegion, boundingBox.x, boundingBox.y, boundingBox.width/2, boundingBox.height/2, boundingBox.width, boundingBox.height, 1, 1, rotation);
    }

//    public Rectangle getBoundingBox() {
//        return boundingBox;
//    }
}