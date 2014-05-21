package untra.scene.game.map;

public class HexDirectional {
	public boolean NW = false;
	public boolean NE = false;
	public boolean W = false;
	public boolean E = false;
	public boolean SW = false;
	public boolean SE = false;

	public static HexDirectional None() {
		return new HexDirectional();
	}

	public boolean none() {
		return (((((NW && NE) && W) && E) && SW) && SE);
	}

	public static HexDirectional all() {
		HexDirectional D = new HexDirectional();
		D.NW = true;
		D.NE = true;
		D.SW = true;
		D.SE = true;
		D.W = true;
		D.E = true;
		return D;
	}

	public static HexDirectional NW() {
		HexDirectional directional = new HexDirectional();
		directional.NW = true;
		return directional;
	}

	public static HexDirectional NE() {
		HexDirectional directional = new HexDirectional();
		directional.NE = true;
		return directional;
	}

	public static HexDirectional E() {
		HexDirectional directional = new HexDirectional();
		directional.E = true;
		return directional;
	}

	public static HexDirectional W() {
		HexDirectional directional = new HexDirectional();
		directional.W = true;
		return directional;
	}

	public static HexDirectional SW() {
		HexDirectional directional = new HexDirectional();
		directional.SW = true;
		return directional;
	}

	public static HexDirectional SE() {
		HexDirectional directional = new HexDirectional();
		directional.SE = true;
		return directional;
	}

	public boolean equals(HexDirectional D) {
		if (this.SW != D.SW)
			return false;
		if (this.NW != D.NW)
			return false;
		if (this.W != D.W)
			return false;
		if (this.SE != D.SE)
			return false;
		if (this.NE != D.NE)
			return false;
		if (this.E != D.E)
			return false;
		return true;
	}

	// returns the opposite hexdirectional of this
	public HexDirectional opposite() {
		if (this.equals(HexDirectional.SW()))
			return HexDirectional.NE();
		if (this.equals(HexDirectional.SE()))
			return HexDirectional.NW();
		if (this.equals(HexDirectional.W()))
			return HexDirectional.E();
		if (this.equals(HexDirectional.E()))
			return HexDirectional.W();
		if (this.equals(HexDirectional.NW()))
			return HexDirectional.SE();
		if (this.equals(HexDirectional.NE()))
			return HexDirectional.SW();
		return new HexDirectional();
	}

	// counterclockwise
	public HexDirectional left() {
		if (this.equals(HexDirectional.SW()))
			return HexDirectional.SE();
		if (this.equals(HexDirectional.SE()))
			return HexDirectional.E();
		if (this.equals(HexDirectional.E()))
			return HexDirectional.NE();
		if (this.equals(HexDirectional.NE()))
			return HexDirectional.NW();
		if (this.equals(HexDirectional.NW()))
			return HexDirectional.W();
		if (this.equals(HexDirectional.W()))
			return HexDirectional.SW();
		return new HexDirectional();
	}

	// clockwise
	public HexDirectional right() {
		if (this.equals(HexDirectional.SW()))
			return HexDirectional.W();
		if (this.equals(HexDirectional.W()))
			return HexDirectional.NW();
		if (this.equals(HexDirectional.NW()))
			return HexDirectional.NE();
		if (this.equals(HexDirectional.NE()))
			return HexDirectional.E();
		if (this.equals(HexDirectional.E()))
			return HexDirectional.SE();
		if (this.equals(HexDirectional.SE()))
			return HexDirectional.SW();
		return new HexDirectional();
	}

	// Returns true if the hexdirectional is opposite this
	public boolean isOpposite(HexDirectional D) {
		if (this.opposite().equals(D))
			return true;
		return false;
	}

	public String toString() {
		String string = new String();
		string += NW ? "NW" : "";
		string += NE ? "NE" : "";
		string += W ? "W" : "";
		string += E ? "E" : "";
		string += SW ? "SW" : "";
		string += SE ? "SE" : "";
		return string;
	}

	public String toBitString() {
		String string = "";
		string += NW ? "1" : "0";
		string += NE ? "1" : "0";
		string += W ? "1" : "0";
		string += E ? "1" : "0";
		string += SW ? "1" : "0";
		string += SE ? "1" : "0";
		return string;
	}

	public HexDirectional fromBitString(String s) {
		HexDirectional D = HexDirectional.None();
		try {

			if (s.charAt(0) == '1')
				D.NW = true;
			if (s.charAt(1) == '1')
				D.NE = true;
			if (s.charAt(2) == '1')
				D.W = true;
			if (s.charAt(3) == '1')
				D.E = true;
			if (s.charAt(4) == '1')
				D.SW = true;
			if (s.charAt(5) == '1')
				D.SE = true;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return D;
	}
}
