package at.game.visuals.tiles;

import at.game.enums.BorderStyle;

public class TileData {
	private String		name;
	private BorderStyle	borderStyle	= BorderStyle.NONE;
	private int			offsetNorth	= 0, offsetSouth = 0, offsetWest = 0, offsetEast = 0;
	private int			red			= 0, green = 0, blue = 0;

	public TileData(final String name, final BorderStyle borderStyle, final int offsetNorth, final int offsetEast, final int offsetSouth, final int offsetWest) {
		this.name = name;

		this.offsetEast = offsetEast;
		this.offsetNorth = offsetNorth;
		this.offsetSouth = offsetSouth;
		this.offsetWest = offsetWest;

		this.borderStyle = borderStyle;
	}

	public TileData(final String name) {
		this.name = name;
	}

	public TileData() {
		this.name = "";
	}

	public int getRed() {
		return this.red;
	}

	public int getGreen() {
		return this.green;
	}

	public int getBlue() {
		return this.blue;
	}

	public void set(final TileData data) {
		this.name = data.name;

		this.borderStyle = data.borderStyle;

		this.offsetEast = data.offsetEast;
		this.offsetNorth = data.offsetNorth;
		this.offsetSouth = data.offsetSouth;
		this.offsetWest = data.offsetWest;

		this.red = data.red;
		this.green = data.green;
		this.blue = data.blue;

	}

	public String getName() {
		return this.name;
	}

	public BorderStyle getBorderStyle() {
		return this.borderStyle;
	}

	public void setOffsetNorth(final int offsetNorth) {
		this.offsetNorth = offsetNorth;
	}

	public void setOffsetSouth(final int offsetSouth) {
		this.offsetSouth = offsetSouth;
	}

	public void setOffsetWest(final int offsetWest) {
		this.offsetWest = offsetWest;
	}

	public void setOffsetEast(final int offsetEast) {
		this.offsetEast = offsetEast;
	}

	protected void setRed(final int red) {
		this.red = red;
	}

	protected void setGreen(final int green) {
		this.green = green;
	}

	protected void setBlue(final int blue) {
		this.blue = blue;
	}

	protected void setBorderStyle(final BorderStyle borderStyle) {
		this.borderStyle = borderStyle;
	}
}
