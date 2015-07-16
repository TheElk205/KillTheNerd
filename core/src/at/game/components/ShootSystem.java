package at.game.components;

import at.game.WorldController;
import at.game.mechanics.Item;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;

public class ShootSystem extends AbstractSystem {
	private final ComponentMapper<Geometrics>			positionMovement		= ComponentMapper.getFor(Geometrics.class);
	private final ComponentMapper<PlayerController>		playerControllerCom		= ComponentMapper.getFor(PlayerController.class);
	private final ComponentMapper<ShootComponentWeapon>	shootComponentMapper	= ComponentMapper.getFor(ShootComponentWeapon.class);

	public ShootSystem() {
	}

	@Override
	public void addedToEngine(final Engine engine) {
		this.entities = engine.getEntitiesFor(Family.all(Geometrics.class, PlayerController.class, ShootComponentWeapon.class).get());
	}

	@Override
	public void update(final float deltaTime) {
		for (int i = 0; i < this.entities.size(); ++i) {
			final Entity entity = this.entities.get(i);
			final Geometrics geometrics = this.positionMovement.get(entity);
			final PlayerController playerController = this.playerControllerCom.get(entity);
			final ShootComponentWeapon shootComponentWeapon = this.shootComponentMapper.get(entity);

			shootComponentWeapon.timeSinceLastShoot = shootComponentWeapon.timeSinceLastShoot + deltaTime; // TODO: check if overflow
			if (shootComponentWeapon.timeSinceLastShoot >= (Double.MAX_VALUE - 1)) {
				throw new RuntimeException();
			}
			if ((shootComponentWeapon.ammo > 0) && playerController.isPressingShoot() && (shootComponentWeapon.timeSinceLastShoot > 0.2f)) {
				// shootComponentWeapon.sound.play();
				shootComponentWeapon.timeSinceLastShoot = 0;
				final Vector2 initPos = geometrics.getPosition();
				final Vector2 toApply = new Vector2();
				switch (geometrics.getDirectionLooking()) {
					case N:
						// toApply.y = shootComponentWeapon.throwingSpeed;
						initPos.y += (geometrics.getApproxB2Height() / 2f) + 0.1f;// 0.2f; // dont add because image is not in middle?
						// (this.dimension.y / 2) +
						// initPos.x -= 0.05f;
						break;
					case E:
						// toApply.x = shootComponentWeapon.throwingSpeed;
						initPos.x += (geometrics.getApproxB2Width() / 2f) + 0.1f;// 0.2f; // dont add because image is not in middle?
						// (this.dimension.x / 2) +
						// initPos.y -= 0.1f;
						break;
					case S:
						// toApply.y = -shootComponentWeapon.throwingSpeed;
						initPos.y -= (geometrics.getApproxB2Height() / 2f) + 0.1f;// (geometrics.getRenderDimension().y / 2); TODO
						// -itemHeigth
						// initPos.x -= 0.05f;
						break;
					case W:
						// toApply.x = -shootComponentWeapon.throwingSpeed;
						initPos.x -= (geometrics.getApproxB2Width() / 2f) + 0.1f;// (geometrics.getRenderDimension().x / 2); TODO -itemWidth
						// initPos.y -= 0.1f;
						break;
					default:
						break;
				}
				// System.out.println("throw spawn position: " + initPos + " playerPosition: " + geometrics.getPosition());
				// if (shootComponentWeapon.itemType == ItemType.Wake_Item) {
				final Item bull = new Item(initPos, true, "Test Item");
				bull.getB2Body().setLinearVelocity(toApply);
				WorldController.addTempGameObject(bull);
				// bull.setItemIsThrownBy(this);
				// bull.setFlying();
				shootComponentWeapon.ammo--;
				// }
			}
		}
	}
}
