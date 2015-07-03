package at.game.maps;

import java.util.ArrayList;
import java.util.List;

import at.game.enums.ItemType;
import at.game.gamemechanics.Player;
import at.game.gamemechanics.movement.BodyFactory;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Level1 extends AbstractLevel {
	private final List<Player>	playerList	= new ArrayList<Player>();
	/** The blocks making up the world TODO: delete if using map editor **/
	private final Array<Block>	blocks		= new Array<Block>();

	public Level1() {

		// WorldController.cameraHelper.setTarget(new Vector2(WorldController.playerSleep.position.x - WorldController.playerWake.position.x,
		// WorldController.playerSleep.position.y
		// - WorldController.playerWake.position.y));
		// Vector(x2-x1,y2-y1)
		// this.level = new Level(this.b2World);

		/*midiPlayer.open(Constants.MUSIC);
		midiPlayer.setLooping(true);
		midiPlayer.setVolume(0.5f);
		midiPlayer.play();*/
	}

	@Override
	public void init() {
		this.player1 = new Player(new Vector2(8, 12), ItemType.Sleep_Item);
		this.player1.setName("Player1");
		this.playerList.add(this.player1);
		this.addGameObject(this.player1);
		// WorldController.playerWake = new Player(new Vector2(1.5f, -1.0f), ItemType.Wake_Item, +10, PlayerType.Wake);
		// WorldController.playerWake.setName("PlayerWake");
		// WorldController.getLevel().addGameObject(WorldController.playerWake);
		this.cameraHelper.setTarget(this.player1.getB2Body());
		this.generateLevelToTest();
	}

	private void generateLevelToTest() {
		final int factor = 2;
		for (int i = 0; i < 20; i++) {
			this.blocks.add(new Block(new Vector2(i * factor, 0 * factor))); // bottom
			this.blocks.add(new Block(new Vector2(i * factor, 7 * factor)));
			if (i > 2) {
				this.blocks.add(new Block(new Vector2(i * factor, 1 * factor)));
			}
		}
		this.blocks.add(new Block(new Vector2(0 * factor, 2 * factor)));
		this.blocks.add(new Block(new Vector2(4 * factor, 3 * factor)));
		// this.blocks.add(new Block(new Vector2(9 * factor, 2 * factor)));
		this.blocks.add(new Block(new Vector2(9 * factor, 3 * factor)));
		this.blocks.add(new Block(new Vector2(9 * factor, 4 * factor)));
		this.blocks.add(new Block(new Vector2(9 * factor, 5 * factor)));
		this.blocks.add(new Block(new Vector2(6 * factor, 3 * factor)));
		this.blocks.add(new Block(new Vector2(6 * factor, 4 * factor)));
		this.blocks.add(new Block(new Vector2(6 * factor, 5 * factor)));
		for (final Block block : this.blocks) {
			final Rectangle rect = block.bounds;
			// final float x1 = block.position.x;// + rect.x;
			// final float y1 = block.position.y;// + rect.y;
			final Body body = BodyFactory.createRect(block.position, new Vector2(rect.width, rect.height));
			body.setUserData("ground");
			block.body = body;
			// debugRenderer.setColor(new Color(1, 0, 0, 1));
			// debugRenderer.rect(x1, y1, rect.width, rect.height);
		}
	}

	public class Block {
		Body				body;
		static final float	SIZE		= 2f;
		Vector2				position	= new Vector2();
		Rectangle			bounds		= new Rectangle();

		public Block(final Vector2 pos) {
			this.position = pos;
			this.bounds.width = Block.SIZE;
			this.bounds.height = Block.SIZE;
		}
	}

}
