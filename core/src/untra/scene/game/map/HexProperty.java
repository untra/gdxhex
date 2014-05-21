package untra.scene.game.map;

public class HexProperty {
	/**
	 * The Object's sprite is flipped
	 */
	public boolean Flip = false;
	/**
	 * The Object is oversized; other oversized objects cannot occupy adjacent
	 * // cells
	 */
	public boolean X_PU = false;
	/**
	 * FREE
	 */
	public boolean Y_PU = false;
	/**
	 * The Object is Multitiled; it occupies multiple tiles of varying
	 * passabilities
	 */
	public boolean Z_PU = false;

	public String toHexString() {
		int i = 0;
		i += (Flip) ? 1 : 0;
		i += (X_PU) ? 2 : 0;
		i += (Y_PU) ? 4 : 0;
		i += (Z_PU) ? 8 : 0;
		return Integer.toHexString(i);
	}

	public HexProperty FromHexString(String hex) {
		HexProperty property = new HexProperty();
		int i = Integer.parseInt(hex, 16);
		if (i >= 8) {
			property.Z_PU = true;
			i -= 8;
		}
		if (i >= 4) {
			property.Y_PU = true;
			i -= 4;
		}
		if (i >= 2) {
			property.X_PU = true;
			i -= 2;
		}
		if (i >= 1) {
			property.Flip = true;
			i -= 1;
		}
		return property;
	}

}
