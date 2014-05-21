package untra.scene.game.map;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class HexTile implements IXml<HexTile> {

	private final int MAX_Z_HEIGHT = 16;

	/**
	 * X position of the Hextile
	 */
	public int X;
	/**
	 * Y component of the Hextile
	 */
	public int Y;
	/**
	 * Value of the tile - grass, path or water?
	 */
	public int Tile_Value;
	Integer xInteger = new Integer(99);

	public boolean passabilities = true;
	public HexDirectional border_tiles = HexDirectional.None();
	public int map_object_index;
	public HexProperty map_object_properties = new HexProperty();
	public int barrier_index;
	public HexProperty barrier_properties = new HexProperty();
	// public HexObject hexobject;
	/**
	 * the supposed "height" of a tile
	 */
	private int z_height;
	// public int walltype = 0;
	// Temporary internal properties.
	public boolean flag = false;
	public HexTile chain;
	public int score;

	public HexTile(int yi, int xi, int v, int zh) {
		X = xi;
		Y = yi;
		Tile_Value = v;
		z_height = zh;
	}

	public int z_height() {
		return z_height;
	}

	public void set_z_height(int i) {
		this.z_height = Math.max(Math.min(i, MAX_Z_HEIGHT), 0);
	}

	/**
	 * Returns a hextile with specific values indicative of a default or 'null'
	 * hextile.
	 * 
	 * @return
	 */
	public static HexTile NULLTILE() {
		return new HexTile(99, 99, 0, 1);
	}

	public HexTile(HexTile H) {
		this.X = H.X;
		this.Y = H.Y;
		this.z_height = H.z_height;
		this.Tile_Value = H.Tile_Value;
		this.passabilities = H.passabilities;
		this.map_object_properties = H.map_object_properties;
		this.map_object_index = H.map_object_index;
		// this.hexobject = H.hexobject;
		this.border_tiles = H.border_tiles;
		this.barrier_properties = H.barrier_properties;
		this.barrier_index = H.barrier_index;
		// this.walltype = H.walltype;
		this.flag = H.flag;
		this.chain = H.chain;
	}

	/**
	 * decrease_cursor_size(); // System.out.println("cursor: " + _cursor_size);
	 * return; returns a string depending on the tile type of the hextile
	 * 
	 * @return
	 */
	public static String toTileValueString(int i) {
		switch (i) {
		case 0:
			return "Grass";
		case 1:
			return "Path";
		case 2:
			return "Water";
		case 3:
			return "Flowers";
		case 4:
			return "Sand";
		case 5:
			return "Dirt";
		case 6:
			return "Dirt";
		case 7:
			return "Path";
		case 8:
			return "Dirt";
		default:
			return "";
		}
	}

	public String toTileValueString() {
		return HexTile.toTileValueString(this.Tile_Value);
	}

	public HexTile(int y, int x) {
		// TODO: Complete member initialization
		this.Y = y;
		this.X = x;
	}

	/**
	 * Compares the positions of another hextile to this ones. Returns true if
	 * they match
	 * 
	 * @param H
	 * @return
	 */
	public boolean samePosAs(HexTile H) {
		if (this.X == H.X)
			if (this.Y == H.Y)
				return true;
		return false;
	}

	public boolean equals(HexTile H) {
		if (H != null)
			return samePosAs(H);
		return false;
	}

	/**
	 * Update
	 */
	public void Update() {

	}

	/**
	 * Returns true if the value type indicates the tile is a liquid
	 * 
	 * @return
	 */
	public boolean is_liquid() {
		if (this.Tile_Value == 2)
			return true;
		return false;
	}

	/**
	 * Returns true if the specified tile is on the same general plane as this
	 * tile if false, borders should be drawn
	 * 
	 * @param tile
	 * @return
	 */
	public boolean border_simmilar(HexTile tile) {
		if (tile == null)
			return false;
		if (this.z_height != tile.z_height)
			return false;
		return true;
	}

	public boolean getPassability() {
		// return (passabilities.W || passabilities.NW || passabilities.NE
		// || passabilities.E || passabilities.SE || passabilities.SW);
		return this.passabilities;
	}

	public void setPassability(boolean value) {
		if (value == true) {
			map_object_properties = new HexProperty();
			passabilities = true;
		} else
			passabilities = false;
	}

	/**
	 * Returns the opposite hex direction
	 * 
	 * @param D
	 * @return
	 */
	public static HexDirectional O(HexDirectional D) {
		if (D.E)
			return HexDirectional.W();
		if (D.W)
			return HexDirectional.E();
		if (D.NE)
			return HexDirectional.SW();
		if (D.SW)
			return HexDirectional.NE();
		if (D.SE)
			return HexDirectional.NW();
		if (D.NW)
			return HexDirectional.SE();
		return HexDirectional.None();
	}

	/**
	 * Returns the hexdirection to the left of the specified direction
	 * 
	 * @param D
	 * @return
	 */
	public static HexDirectional L(HexDirectional D) {
		if (D.E)
			return HexDirectional.NE();
		if (D.NE)
			return HexDirectional.NW();
		if (D.NW)
			return HexDirectional.W();
		if (D.W)
			return HexDirectional.SW();
		if (D.SW)
			return HexDirectional.SE();
		if (D.SE)
			return HexDirectional.E();
		return HexDirectional.None();
	}

	/**
	 * Returns the hexdirection to the right of the specified direction
	 * 
	 * @param D
	 * @return
	 */
	public static HexDirectional R(HexDirectional D) {
		if (D.NE)
			return HexDirectional.E();
		if (D.NW)
			return HexDirectional.NE();
		if (D.W)
			return HexDirectional.NW();
		if (D.SW)
			return HexDirectional.W();
		if (D.SE)
			return HexDirectional.SW();
		if (D.E)
			return HexDirectional.SE();
		return HexDirectional.None();
	}

	public String toString() {
		// return String.format("[%d, %d]", this.X, this.Y);
		return toCompressedString();
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("HexTile");
		xml.text(toCompressedString());
		xml.pop();
	}

	private String toCompressedString() {
		String string = "";
		string += Integer.toHexString(this.X) + "-";// 0
		string += Integer.toHexString(this.Y) + "-";// 1
		string += Integer.toHexString(this.z_height) + "-";// 2
		string += Integer.toHexString(this.Tile_Value) + "-";// 3
		string += Integer.toHexString(this.barrier_index) + "-";// 4
		string += Integer.toHexString(this.map_object_index) + "-";// 5
		string += passabilities ? "1-" : "0-";// 6
		string += border_tiles.toBitString() + "-";// 7
		string += map_object_properties.toHexString() + "-";// 8
		string += barrier_properties.toHexString();// 9
		return string;
	}

	private HexTile FromCompressedString(String string) {
		String[] dataStrings = string.split("-");
		HexTile H = new HexTile(0, 0);
		H.X = Integer.parseInt(dataStrings[0], 16);
		H.Y = Integer.parseInt(dataStrings[1], 16);
		H.z_height = Integer.parseInt(dataStrings[2], 16);
		H.Tile_Value = Integer.parseInt(dataStrings[3], 16);
		H.barrier_index = Integer.parseInt(dataStrings[4], 16);
		H.map_object_index = Integer.parseInt(dataStrings[5], 16);
		H.passabilities = dataStrings[6] == "1" ? true : false;
		H.border_tiles = border_tiles.fromBitString(dataStrings[7]);
		H.map_object_properties = map_object_properties
				.FromHexString(dataStrings[8]);
		H.barrier_properties = barrier_properties.FromHexString(dataStrings[9]);
		return H;
	}

	@Override
	public HexTile xmlRead(Element element) {
		try {
			String string = element.getText();
			return FromCompressedString(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NULLTILE();

	}

	public void deltaHeight(int i) {
		int z = Math.max(Math.min(this.z_height + i, MAX_Z_HEIGHT), 0);
		this.z_height = i;
	}
}
