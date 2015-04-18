package at.gmaejam.ktn.mapbuilder;

public class execute {
	public static void main(String[] args) {
		TileParser tp = new TileParser("D:\\testtile.txt");
		tp.readTileData();
		tp.printList();
	}
}
