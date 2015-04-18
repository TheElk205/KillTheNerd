package at.gmaejam.ktn.mapbuilder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MapParser {
	private TileParser tileParser;
	private String path;
	
	TileData[][] tiles;
	BufferedImage buffImage;
	
	public MapParser(String path, TileParser tileParser) {
		this.path = path;
		this.tileParser = tileParser;
		try {
			buffImage = ImageIO.read(new File(this.path));
		} catch (IOException e) {
			System.err.println("Error while opening " + this.path);
		}
		this.tiles = new TileData[buffImage.getWidth()][buffImage.getHeight()];
	}
	
	public void parseMap() {
		int notFound = 0;
		boolean found = false;
		for(int y = 0; y < this.tiles[0].length; y++) {
			for(int x = 0; x < this.tiles.length; x++) {
				Color color = new Color(buffImage.getRGB(x, y));
				//System.out.println(color);
				for(TileData td : tileParser.tileDataList) {
					found = false;
					if(td.getRed() == color.getRed() && td.getGreen() == color.getGreen() && td.getBlue() == color.getBlue()) {
						//System.out.println("found");
						TileData tmp = new TileData();
						tmp.set(td);
						tiles[x][y] = tmp;
						found = true;
						break;
					}
				}
				if(!found) {
					notFound++;
				}
			}
		}
		System.out.println(notFound + " Could not be mapped");
		System.out.println(tileParser.tileDataList.size() + " Tiles for reference");
	}
	
	public TileData[][] getTileData() {
		return this.tiles;
	}
	
}
