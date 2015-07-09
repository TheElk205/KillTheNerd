package at.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Geometrics {
	private Body	b2Body;
	private Vector2	position;			// not needed cause b2Body also contains position? , may be in future
	private Vector2	renderDimension;
	private float	approxB2Width;
	private float	approxB2Height;

	public Geometrics(final Body b2Body, final Vector2 position, final Vector2 renderDimension, final float approxB2Width, final float apprixB2Height) {
		this.b2Body = b2Body;
		this.position = position;
		this.renderDimension = renderDimension;
		this.approxB2Width = approxB2Width;
		this.approxB2Height = apprixB2Height;

	}

	public void update() {
		this.position = this.b2Body.getPosition();
	}

	public Body getB2Body() {
		return this.b2Body;
	}

	public void setB2Body(final Body b2Body) {
		this.b2Body = b2Body;
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public void setPosition(final Vector2 position) {
		this.position = position;
	}

	public Vector2 getRenderDimension() {
		return this.renderDimension;
	}

	public void setRenderDimension(final Vector2 renderDimension) {
		this.renderDimension = renderDimension;
	}

	public float getApproxB2Width() {
		return this.approxB2Width;
	}

	protected void setApproxB2Width(final float approxB2Width) {
		this.approxB2Width = approxB2Width;
	}

	public float getApproxB2Height() {
		return this.approxB2Height;
	}

	protected void setApproxB2Height(final float approxB2Height) {
		this.approxB2Height = approxB2Height;
	}

}
