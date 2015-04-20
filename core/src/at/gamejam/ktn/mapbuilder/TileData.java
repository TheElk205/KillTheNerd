package at.gamejam.ktn.mapbuilder;

public class TileData {
	public enum BorderStyle {BOX, NONE};
	
	private String name;
	private BorderStyle borderStyle = BorderStyle.NONE;
	private int offsetNorth = 0, offsetSouth = 0, offsetWest = 0, offsetEast = 0;
	
	private int red = 0, green = 0, blue = 0;
	
	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public TileData(String name, BorderStyle borderStyle, int offsetNorth, int offsetEast, int offsetSouth, int offsetWest) {
		this.name = name;
		
		this.offsetEast = offsetEast;
		this.offsetNorth = offsetNorth;
		this.offsetSouth = offsetSouth;
		this.offsetWest = offsetWest;
		
		this.borderStyle = borderStyle;
	}
	
	public TileData(String name) {
		this.name = name;
	}
	
	public TileData() {
		this.name = "";
	}
	
	public void set(TileData data) {
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BorderStyle getBorderStyle() {
		return borderStyle;
	}

	public void setBorderStyle(BorderStyle borderStyle) {
		this.borderStyle = borderStyle;
	}

	public int getOffsetNorth() {
		return offsetNorth;
	}

	public void setOffsetNorth(int offsetNorth) {
		this.offsetNorth = offsetNorth;
	}

	public int getOffsetSouth() {
		return offsetSouth;
	}

	public void setOffsetSouth(int offsetSouth) {
		this.offsetSouth = offsetSouth;
	}

	public int getOffsetWest() {
		return offsetWest;
	}

	public void setOffsetWest(int offsetWest) {
		this.offsetWest = offsetWest;
	}

	public int getOffsetEast() {
		return offsetEast;
	}

	public void setOffsetEast(int offsetEast) {
		this.offsetEast = offsetEast;
	}
	
	
}
