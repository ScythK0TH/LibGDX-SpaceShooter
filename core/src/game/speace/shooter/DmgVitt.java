package game.speace.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class DmgVitt {
	Texture texture;
	float x;
	float y;
	float width;
	float height;

    DmgVitt(Texture texture, float x, float y, float width, float height) {

        this.texture = texture;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

    }

    public void draw (SpriteBatch batch) {
        batch.draw(texture,
                x,
                y,
                width,
                height);
    }

}
