package at.game.utils;


public class Stuff {

	public Stuff() {
		// TODO Auto-generated constructor stub
	}
	/*
	 *
	 PLAYER
	public void otherMovement() {
		final Vector2 lin = new Vector2(this.b2Body.getLinearVelocity().x, this.b2Body.getLinearVelocity().y);
		// System.out.println(b2Body.getLinearVelocity());
		// wenn Spieler "schwebend" am Abgrund stehen würde, Spieler runter schubsen
		final boolean prevStateWalking = this.getRace().getPrevState().equals(HumanStateEnum.WALKING);
		if (prevStateWalking) {
			if ((this.getOuterFootLeftCount() > 0) && (this.getOuterFootRightCount() == 0) && (this.b2Body.getLinearVelocity().x < 1)) {
				lin.x += 1;
				// System.out.println("push Player to right");
			} else
				if ((this.getOuterFootRightCount() > 0) && (this.getOuterFootLeftCount() == 0) && (this.b2Body.getLinearVelocity().x > -1)) {
					lin.x -= 1;
					// System.out.println("push Player to left");
				}
		}
		this.b2Body.setLinearVelocity(lin);
	}*/

	// PLAYER
	/*public void stop() {
	this.up = false;
	this.down = false;
	this.left = false;
	this.right = false;
	this.b2Body.setLinearVelocity(new Vector2(0, 0));
	this.b2Body.setAngularVelocity(0);
	this.directionMoving = DirectionEnum.STAY;
	}*/

	/*
	 * PLAYer
	 * 	/*private void loadPictures(final String prefix) {
		TextureRegion up, down, left, right;
		up = AbstractGameObject.assets.findRegion(prefix + "back");
		down = AbstractGameObject.assets.findRegion(prefix + "front");
		left = AbstractGameObject.assets.findRegion(prefix + "left");
		right = AbstractGameObject.assets.findRegion(prefix + "right");
		this.setInitialPictures(up, down, left, right);
	}*/

}
