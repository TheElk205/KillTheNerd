package at.game.maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author Ferdinand Koeppen
 */
public class TileParser {
	private final String			path;
	private final Vector<TileData>	tileDataList;

	/**
	 * @param path
	 */
	public TileParser(final String path) {
		this.path = path;
		this.tileDataList = new Vector<TileData>();
		this.readTileData();
	}

	private void readTileData() {
		final File file = new File(this.path);
		boolean properties = false;
		boolean toFinalize = false;
		BufferedReader br = null;
		try {
			TileData tile = null;
			br = new BufferedReader(new FileReader(file));
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
						final String prop = line.substring(0, line.indexOf(":"));
						String data = line.substring(line.indexOf(":") + 1);
						data = data.trim();
						// System.out.println(prop + "-->" + data);
						switch (prop) {
							case "edgetype":
								TileParser.setEdgetype(tile, data);
								break;
							case "edgeOffset":
								TileParser.setBoxOffsets(tile, data);
								break;
							case "color":
								TileParser.setBoxColor(tile, data);
								break;
							default:
								break;
						}
					}
			}
			if (toFinalize) {
				this.tileDataList.add(tile);
			}
		} catch (final IOException e) {
			// do nothing
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		// System.out.println("Added " + this.tileDataList.size() + " Tiles.");
	}

	/*private void printList() {
		System.out.println("Length: " + this.tileDataList.size());
		for (final TileData t : this.tileDataList) {
			System.out.println(t.getName() + ":");
			System.out.println(t.getOffsetNorth() + "," + t.getOffsetEast() + "," + t.getOffsetSouth() + "," + t.getOffsetWest());
			System.out.println(t.getRed() + "," + t.getGreen() + "," + t.getBlue());
		}
	}*/

	private static void setEdgetype(final TileData tileData, final String data) {
		switch (data) {
			case "box":
				tileData.setBorderStyle(BorderStyle.BOX);
				break;
			default:
				tileData.setBorderStyle(BorderStyle.NONE);
		}
	}

	private static void setBoxOffsets(final TileData tileData, final String data) {
		final String[] parts = data.trim().split(",");
		System.out.println(Arrays.toString(parts));
		tileData.setOffsetNorth(Integer.parseInt(parts[0]));
		tileData.setOffsetEast(Integer.parseInt(parts[1]));
		tileData.setOffsetSouth(Integer.parseInt(parts[2]));
		tileData.setOffsetWest(Integer.parseInt(parts[3]));
	}

	private static void setBoxColor(final TileData tileData, final String data) {
		final String[] parts = data.trim().split(",");
		tileData.setRed(Integer.parseInt(parts[0]));
		tileData.setGreen(Integer.parseInt(parts[1]));
		tileData.setBlue(Integer.parseInt(parts[2]));
	}

	/**
	 * @return the tileDataList
	 */
	protected Vector<TileData> getTileDataList() {
		return this.tileDataList;
	}
}
