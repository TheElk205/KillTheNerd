package at.gmaejam.ktn.mapbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import at.gmaejam.ktn.mapbuilder.TileData.BorderStyle;

public class TileParser {
	private String			path;

	public Vector<TileData>	tileDataList;

	public TileParser(String path) {
		this.path = path;
		this.tileDataList = new Vector<TileData>();
	}

	public void readTileData() {
		File file = new File(this.path);
		boolean properties = false;
		boolean toFinalize = false;
		try {
			TileData tile = null;
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String line; (line = br.readLine()) != null;) {
				if (line.startsWith(":")) {
					// System.out.println("HU");
					if (toFinalize) {
						this.tileDataList.add(tile);
					}
					tile = new TileData(line.substring(1, line.length() - 1));
					// System.out.println("TILE: " + tile.getName());
					properties = true;
					toFinalize = true;
				} else
					if (properties) {
						String prop = line.substring(0, line.indexOf(":"));
						String data = line.substring(line.indexOf(":") + 1);
						data = data.trim();
						// System.out.println(prop + "-->" + data);
						switch (prop) {
							case "edgetype":
								setEdgetype(tile, data);
								break;
							case "edgeOffset":
								setBoxOffsets(tile, data);
								break;
							case "color":
								setBoxColor(tile, data);
								break;
						}
					}
			}
			if (toFinalize) {
				System.out.println("Add");
				this.tileDataList.add(tile);
			}
			// line is not visible here.
		} catch (IOException e) {

		}

		System.out.println("Added " + this.tileDataList.size() + " Tiles.");
	}

	public void printList() {
		System.out.println("Length: " + this.tileDataList.size());
		for (TileData t : this.tileDataList) {
			System.out.println(t.getName() + ":");
			System.out.println(t.getOffsetNorth() + "," + t.getOffsetEast() + "," + t.getOffsetSouth() + "," + t.getOffsetWest());
			System.out.println(t.getRed() + "," + t.getGreen() + "," + t.getBlue());
		}
	}

	public void setEdgetype(TileData tileData, String data) {
		switch (data) {
			case "box":
				tileData.setBorderStyle(BorderStyle.BOX);
				break;
			default:
				tileData.setBorderStyle(BorderStyle.NONE);
		}
	}

	public void setBoxOffsets(TileData tileData, String data) {
		String[] parts = data.trim().split(",");
		System.out.println(Arrays.toString(parts));
		tileData.setOffsetNorth(Integer.parseInt(parts[0]));
		tileData.setOffsetEast(Integer.parseInt(parts[1]));
		tileData.setOffsetSouth(Integer.parseInt(parts[2]));
		tileData.setOffsetWest(Integer.parseInt(parts[3]));
	}

	public void setBoxColor(TileData tileData, String data) {
		String[] parts = data.trim().split(",");
		tileData.setRed(Integer.parseInt(parts[0]));
		tileData.setGreen(Integer.parseInt(parts[1]));
		tileData.setBlue(Integer.parseInt(parts[2]));
	}
}
