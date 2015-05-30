package at.game.visuals.tiles;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapParser {
	private final TileParser	tileParser;
	private final String		path;
	private final TileData[][]	tiles;
	private BufferedImage		buffImage;
	private int					mapImageWidth;
	private int					mapImageHeight;

	public MapParser(final String path, final String pathConfig) {
		this.tileParser = new TileParser(pathConfig);
		this.path = path;
		try {
			this.buffImage = ImageIO.read(new File(this.path));
		} catch (final IOException e) {
			System.err.println("Error while opening " + this.path);
			// TODO handle
		}
		this.tiles = new TileData[this.buffImage.getWidth()][this.buffImage.getHeight()];
	}

	public void parseMap() {
		int notFound = 0;
		boolean found = false;
		Color color;
		this.setMapImageWidth(this.buffImage.getWidth());
		this.setMapImageHeight(this.buffImage.getHeight());
		for (int y = 0; y < this.mapImageHeight; y++) { //
			for (int x = 0; x < this.mapImageWidth; x++) { // this.tiles.length;
				color = new Color(this.buffImage.getRGB(x, y));
				// System.out.println(color);
				for (final TileData td : this.tileParser.getTileDataList()) {
					found = false;
					if ((td.getRed() == color.getRed()) && (td.getGreen() == color.getGreen()) && (td.getBlue() == color.getBlue())) {
						// System.out.println("found");
						final TileData tmp = new TileData();
						tmp.set(td);
						this.tiles[x][y] = tmp;
						found = true;
						break;
					}
				}
				if (!found) {
					notFound++;
				}
			}
		}
		// System.out.println(notFound + " tiles not mapped (not used)");
		// System.out.println(this.tileParser.tileDataList.size() + " Tiles for reference");
	}

	public TileData[][] getTileData() {
		return this.tiles;
	}

	public int getMapImageWidth() {
		return this.mapImageWidth;
	}

	public void setMapImageWidth(final int mapImageWidth) {
		this.mapImageWidth = mapImageWidth;
	}

	public int getMapImageHeight() {
		return this.mapImageHeight;
	}

	public void setMapImageHeight(final int mapImageHeight) {
		this.mapImageHeight = mapImageHeight;
	}

}
