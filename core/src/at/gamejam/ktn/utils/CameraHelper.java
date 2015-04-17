package at.gamejam.ktn.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Lukas on 11.04.2015.
 */
public class CameraHelper {
	private final float	MAX_ZOOM_IN		= 0.25f;
	private final float	MAX_ZOOM_OUT	= 10.0f;

	private Vector2		position;
	private float		zoom;
	private Body		target;

	public CameraHelper() {
		this.position = new Vector2();
		this.zoom = 1.0f;
	}

	public void update(final float deltaTime) {
		if (!this.hasTarget()) {
			return;
		}
		this.position = this.target.getPosition();
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
}
