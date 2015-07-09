package at.game.mechanics;

/**
 * every (?) normal item has a AbstractInventoryItem, which represents the for example sword in the inventory may be remove "abstract" otherwhise to
 * many classes
 *
 * @author Herkt Kevin
 */
public abstract class AbstractInventoryItem {
	private final boolean		equipped		= false;
	private float				weightInKg		= 1.5f;
	/** may be not needed */
	private final int			sizeInSquares	= 2;
	private InventoryFormEnum	form			= InventoryFormEnum.SQUARE_1X1;

	private enum InventoryFormEnum {
		SQUARE_1X1, SQUARE_2X2, SQUARE_1X2, SQUARE_2X1
	}

	public AbstractInventoryItem(final float weightInKg, final InventoryFormEnum form) {
		this.weightInKg = weightInKg;
		this.form = form;
	}

}
