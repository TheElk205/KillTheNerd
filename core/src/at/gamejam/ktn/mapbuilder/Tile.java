package at.gamejam.ktn.mapbuilder;

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
	private TextureRegion	texture		= null;
	private final World		b2World;
	private Body			b2Body;
	private final TileData	tileData;
	private String			tileName	= null;

	public Tile(final Vector2 position, final World b2World, final TileData tiledata) {
		this.position = position;
		this.b2World = b2World;
		this.tileData = tiledata;
		if (tiledata != null) {
			this.tileName = tiledata.getName();
			final int fileNameIndex = this.tileName.indexOf("."); // ohne .png Endung
			this.tileName = tiledata.getName().substring(0, fileNameIndex);
			if (GameObject.textureList.contains(this.tileName)) {
				for (int i = 0; i < GameObject.textureList.size(); i++) {
					if (GameObject.textureNameList.get(i).equalsIgnoreCase(this.tileName)) {
						this.texture = GameObject.textureList.get(i);
						break;
					}
				}

			} else {
				this.texture = GameObject.assets.findRegion(this.tileName);
				GameObject.textureList.add(this.texture);
			}
			if (!GameObject.textureNameList.contains(this.tileName)) {
				GameObject.textureNameList.add(this.tileName);
			}
		} else {
			this.texture = GameObject.assets.findRegion("errorTile");
		}

		this.initPhysics();

		this.dimension = new Vector2(0.5f, 0.5f);
	}

	@Override
	public void initPhysics() {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(this.position.x + (this.dimension.x / 4f), this.position.y + (this.dimension.y / 4f)));
		bodyDef.type = BodyType.StaticBody;

		this.b2Body = this.b2World.createBody(bodyDef);

		if (this.tileData != null) {
			switch (this.tileData.getBorderStyle()) {
				case BOX:
					this.createBoxBorder();
					break;
				default:
					break;
			}
		}
	}

	private void createBoxBorder() {
		final PolygonShape poly = new PolygonShape();
		poly.setAsBox(this.dimension.x / 4f, this.dimension.y / 4f);

		final FixtureDef fixtureDef = new FixtureDef();

		fixtureDef.shape = poly;
		this.b2Body.createFixture(fixtureDef);
		this.b2Body.setUserData(this);
	}

	public void setDimension(final float x, final float y) {
		this.dimension.x = x;
		this.dimension.y = y;
	}

	@Override
	public void render(final SpriteBatch batch) {
		try {
			batch.draw(this.texture, this.position.x, this.position.y, this.dimension.x, this.dimension.y);
		} catch (final Exception e) {
			System.out.println("cant render tile: " + this.tileName);
			System.exit(0);
		}
	}

	public TileData getTiledata() {
		return this.tileData;
	}
}
