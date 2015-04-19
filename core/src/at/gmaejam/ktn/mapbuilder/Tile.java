package at.gmaejam.ktn.mapbuilder;

import at.gamejam.ktn.game.entities.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Tile extends GameObject {
	private TextureRegion	texture;
	private final World		b2World;
	private Body			b2Body;
	private TileData		tileData;

	public Tile(Vector2 position, final World b2World, TileData tiledata) {
		this.position = position;
		this.b2World = b2World;
		this.tileData = tiledata;

		String tilename = tiledata.getName().substring(0, tiledata.getName().indexOf(".")); // ohne .png endung
		this.texture = this.assets.findRegion(tilename);
		initPhysics();

		this.dimension = new Vector2(0.5f, 0.5f);
	}

	@Override
	public void initPhysics() {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(this.position.x + (this.dimension.x / 4f), this.position.y + (this.dimension.y / 4f)));
		bodyDef.type = BodyType.StaticBody;

		this.b2Body = this.b2World.createBody(bodyDef);

		switch (tileData.getBorderStyle()) {
			case BOX:
				createBoxBorder(b2Body);
				break;
			default:
				break;

		}
	}

	private void createBoxBorder(Body b2Body) {
		final PolygonShape poly = new PolygonShape();
		poly.setAsBox(this.dimension.x / 4f, this.dimension.y / 4f);

		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = poly;
		this.b2Body.createFixture(fixtureDef);
		this.b2Body.setUserData(this);
	}

	public void setDimension(float x, float y) {
		this.dimension.x = x;
		this.dimension.y = y;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(this.texture, this.position.x, this.position.y, this.dimension.x, this.dimension.y);
	}

	public TileData getTiledata() {
		return this.tileData;
	}
}
