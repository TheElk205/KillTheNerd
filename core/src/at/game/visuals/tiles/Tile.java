package at.game.visuals.tiles;

import at.game.RenderObject;
import at.game.objects.AbstractGameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * @author Herkt Kevin
 */
public class Tile extends RenderObject {
	private TextureRegion	texture		= null;
	private Body			b2Body;
	private final TileData	tileData;
	private String			tileName	= null;

	public Tile(final Vector2 position, final TileData tiledata) {
		this.position = position;
		this.tileData = tiledata;
		if (tiledata != null) {
			this.tileName = tiledata.getName();
			final int fileNameIndex = this.tileName.indexOf("."); // ohne .png Endung
			this.tileName = tiledata.getName().substring(0, fileNameIndex);
			if (AbstractGameObject.getTextureList().contains(this.tileName)) {
				for (int i = 0; i < AbstractGameObject.getTextureList().size(); i++) {
					if (AbstractGameObject.getTextureNameList().get(i).equalsIgnoreCase(this.tileName)) {
						this.texture = AbstractGameObject.getTextureList().get(i);
						break;
					}
				}

			} else {
				this.texture = AbstractGameObject.assets.findRegion(this.tileName);
				AbstractGameObject.getTextureList().add(this.texture);
			}
			if (!AbstractGameObject.getTextureNameList().contains(this.tileName)) {
				AbstractGameObject.getTextureNameList().add(this.tileName);
			}
		} else {
			this.texture = AbstractGameObject.assets.findRegion("errorTile");
		}

		this.initPhysics();

		this.dimension = new Vector2(0.5f, 0.5f);
	}

	private void initPhysics() {
		/*final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2((this.position.x) + (this.dimension.x / 4f), (this.position.y) + (this.dimension.y / 4f)));
		bodyDef.type = BodyType.StaticBody;

		// TODO: may be generate tiles here, but generate the bodies/borders for it in the level - see Level1 class
		final World world = WorldController.getB2World();
		this.b2Body = world.createBody(bodyDef);

		if (this.tileData != null) {
			switch (this.tileData.getBorderStyle()) {
				case BOX:
					this.createBoxBorder();
					break;
				default:
					break;
			}
		}*/
	}

	private void createBoxBorder() {
		final PolygonShape poly = new PolygonShape();
		poly.setAsBox(this.dimension.x, this.dimension.y);

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

	@Override
	public String toString() {
		return "Tile (" + this.tileName + ")";
	}

	public void update(final float deltaTime) {
		// a tile has nothing to update, maybe introduce states for tiles? -> animated tile with changeable state, water lava
	}
}
