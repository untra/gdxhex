package untra.scene.game.map;

import untra.graphics.Sprite;
import untra.database.Tile_Object;

/**
 * Hexobject
 * 
 * @author samuel
 * 
 */
public class HexObject {
	public String name;
	public Sprite sprite;
	public int id;
	// public HexDirectional blocked_passes;
	// Flipped Designates Fliping
	// X_PU designates wide size- no other X_PU objects allowed
	// Y_PU designates inconsistantly blocked passabilities
	private HexProperty properties;
	public int frequency;
	public PassabilityArray passables;

	public HexObject(Tile_Object T) {
		name = T.name;
		// TODO: Fix this!
		sprite = new Sprite("Sprites/Objects/Static/" + name + ".png");
		id = T.id;
		passables = new PassabilityArray(T.passability, T.width, T.height);
		properties = new HexProperty();
		setOversized(T.is_oversized_object());
		frequency = T.frequency;

	}

	/**
	 * Returns true if object is oversized. Two oversized objects cannot be
	 * adjascent to one another
	 * 
	 * @return
	 */
	public boolean getOversized() {

		return properties.X_PU;
	}

	public void setOversized(boolean value) {
		if (value == true)
			properties.X_PU = true;
		else
			properties.Y_PU = false;
	}

	public boolean is_multitile() {
		return passables.is_multitile();
	}
}
