package at.game.visuals.tiles;

import at.game.visuals.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Lukas on 11.04.2015.
 */
public class BasicBlock extends GameObject {
	private final boolean			flipped;
	private TextureRegion			texture;
	private final World				b2World;
	private Body					b2Body;
	private final BasicBlockType	type;
	private boolean					physics	= true;

	public BasicBlock(final Vector2 position, final BasicBlockType type, final World b2World) {
		this(position, type, b2World, false);
	}

	public BasicBlock(final Vector2 position, final BasicBlockType type, final World b2World, final boolean flipped) {
		super();
		this.b2World = b2World;
		this.position = position;
		this.dimension = new Vector2(1, 1);
		this.type = type;
		this.flipped = flipped;
		switch (type) {
			case BlockDirt:
				this.texture = GameObject.assets.findRegion("grassCenter");
				// this.physics = false;
				break;
			case BlockGrass:
				this.texture = GameObject.assets.findRegion("grassMid");
				break;
			case BlockSlope:
				this.texture = GameObject.assets.findRegion("grassHillLeft");
				break;
			case BlockSlopeStart:
				this.texture = GameObject.assets.findRegion("grassHillLeft2");
				this.physics = false;
				break;
			case BlockBorder:
				this.texture = GameObject.assets.findRegion("grassCenter");
				break;
			default:
				throw new RuntimeException("no image defined for this block type");
		}
		if (flipped) {
			this.texture = new TextureRegion(this.texture); // clone texture for performing flip
			this.texture.flip(true, false);
		}
		if (this.physics) {
			this.initPhysics();
		}
	}

	@Override
	public void initPhysics() {
		final BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(this.position.x, this.position.y));
		bodyDef.type = BodyDef.BodyType.StaticBody;

		this.b2Body = this.b2World.createBody(bodyDef);
		final EdgeShape edgeShape = new EdgeShape();
		if (this.type == BasicBlockType.BlockSlope) {
			if (this.flipped) {
				edgeShape.set(new Vector2(0, this.dimension.y), new Vector2(this.dimension.x, 0));
			} else {
				edgeShape.set(new Vector2(0, 0), this.dimension);
			}
		}
		if (this.type == BasicBlockType.BlockBorder) {
			edgeShape.set(new Vector2(0, 0), new Vector2(0, this.dimension.y));
			// edgeShape.set(new Vector2(0, this.dimension.x), this.dimension);
			// edgeShape.set(new Vector2(this.dimension.x, 0), this.dimension);
			// edgeShape.set(new Vector2(this.dimension.y, 0), this.dimension);
		} else {
			edgeShape.set(new Vector2(0, this.dimension.y), this.dimension);
		}

		edgeShape.setVertex0(new Vector2(-this.dimension.x, this.dimension.y));
		edgeShape.setVertex3(new Vector2(2 * this.dimension.x, this.dimension.y));
		edgeShape.setHasVertex0(true);
		edgeShape.setHasVertex3(true);

		final FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = edgeShape;
		fixtureDef.friction = 1f;

		this.b2Body.createFixture(fixtureDef);
	}

	@Override
	public void render(final SpriteBatch batch) {
		batch.draw(this.texture, this.position.x, this.position.y, this.dimension.x, this.dimension.y);
	}

	@Override
	public void update(final float deltaTime) {
		// TODO should extend a other class, because update is not needed
	}
}
