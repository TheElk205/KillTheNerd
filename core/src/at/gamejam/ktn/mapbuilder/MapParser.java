package at.gamejam.ktn.mapbuilder;

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

	public MapParser(final String path, final TileParser tileParser) {
		this.path = path;
		this.tileParser = tileParser;
		try {
			this.buffImage = ImageIO.read(new File(this.path));
		} catch (final IOException e) {
			System.err.println("Error while opening " + this.path);
		}
		this.tiles = new TileData[this.buffImage.getWidth()][this.buffImage.getHeight()];
	}

	public void parseMap() {
		int notFound = 0;
		boolean found = false;
		Color color;
		this.setMapImageWidth(this.buffImage.getWidth());
		this.setMapImageHeight(this.buffImage.getHeight());
		for (int y = 0; y < this.tiles[0].length; y++) {
			for (int x = 0; x < this.tiles.length; x++) {
				color = new Color(this.buffImage.getRGB(x, y));
				// System.out.println(color);
				for (final TileData td : this.tileParser.tileDataList) {
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
		System.out.println(notFound + " Could not be mapped");
		System.out.println(this.tileParser.tileDataList.size() + " Tiles for reference");
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
