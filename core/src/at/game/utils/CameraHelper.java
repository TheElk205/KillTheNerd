package at.game.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class CameraHelper {
	private final float	MAX_ZOOM_IN		= -9999300f;
	private final float	MAX_ZOOM_OUT	= 9999200f;

	private Vector2		position;
	private float		zoom;
	private Body		target;
	private Vector2		calcPosition;

	public CameraHelper() {
		this.position = new Vector2();
		this.zoom = 1.0f;
	}

	public void update(final float deltaTime) {
		/*if (!this.hasTarget()) {
			return;
		}
		if (this.target != null) {
			// this.position = this.target.getPosition();
		} else {*/
		this.position = this.calcPosition;

		// }
	}

	public void setPosition(final float x, final float y) {
		this.position.set(x, y);
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public void addZoom(final float ammount) {
		this.setZoom(this.zoom + ammount);
	}

	private void setZoom(final float zoom) {
		this.zoom = MathUtils.clamp(zoom, this.MAX_ZOOM_IN, this.MAX_ZOOM_OUT);
	}

	public void setTarget(final Body target) {
		this.target = target;
	}

	public void setTarget(final Vector2 position) {
		this.calcPosition = position;
	}

	private boolean hasTarget() {
		return this.target != null;
	}

	private boolean hasTarget(final Body target) {
		return this.hasTarget() && this.target.equals(target);
	}

	/*
	 * @param camera

	public void applyTo(final OrthographicCamera camera) {
		camera.position.x = this.position.x;
		camera.position.y = this.position.y;
		// camera.zoom = this.zoom;
		// System.out.println("CameraHelper: " + camera.zoom);
		// camera.zoom = 0;
		camera.update();
	} */

	@Override
	public String toString() {
		return "CameraHelper - Target: " + this.target + " position: " + this.position;
	}
}
