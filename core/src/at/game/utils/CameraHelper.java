package at.gamejam.ktn.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class CameraHelper {
	private final float	MAX_ZOOM_IN		= 1.1f;
	private final float	MAX_ZOOM_OUT	= 1.8f;

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

	public float getZoom() {
		return this.zoom;
	}

	public void setZoom(final float zoom) {
		this.zoom = MathUtils.clamp(zoom, this.MAX_ZOOM_IN, this.MAX_ZOOM_OUT);
	}

	public Body getTarget() {
		return this.target;
	}

	public void setTarget(final Body target) {
		this.target = target;
	}

	public void setTarget(final Vector2 position) {
		this.calcPosition = position;
	}

	public boolean hasTarget() {
		return this.target != null;
	}

	public boolean hasTarget(final Body target) {
		return this.hasTarget() && this.target.equals(target);
	}

	public void applyTo(final OrthographicCamera camera) {
		camera.position.x = this.position.x;
		camera.position.y = this.position.y;
		camera.zoom = this.zoom;
		camera.update();
	}

	public void update(Vector2 vector) {
		this.position = vector;
	}
}
