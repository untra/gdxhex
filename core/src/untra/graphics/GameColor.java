package untra.graphics;

import java.io.IOException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class GameColor extends Color implements IXml<GameColor> {

	public GameColor(float r, float g, float b, float a) {
		super(r, g, b, a);
	}

	public GameColor(int r, int g, int b, int a) {
		super((float) r / 255, (float) g / 255, (float) b / 255,
				(float) a / 255);
	}

	public boolean equals(GameColor color) {
		if (this.r != color.r)
			return false;
		if (this.g != color.g)
			return false;
		if (this.b != color.b)
			return false;
		if (this.a != color.a)
			return false;
		return true;
	}

	public Color to_badlogic_color() {
		return new Color(this.r, this.g, this.b, this.a);
	}

	public boolean valid() {
		try {
			if (this.r < 0.0f || this.r > 1.0f)
				return false;
			if (this.g < 0.0f || this.g > 1.0f)
				return false;
			if (this.b < 0.0f || this.b > 1.0f)
				return false;
			if (this.a < 0.0f || this.a > 1.0f)
				return false;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static final GameColor WHITE = new GameColor(1.0f, 1.0f, 1.0f, 1.0f);

	public static final GameColor ALICEBLUE = new GameColor(240, 248, 255, 255);

	// this line of code is cursed. It will fuck everything up, I swear.
	public static final GameColor TRUEWHITE = new GameColor(254, 254, 254, 255);

	public static final GameColor LIGHTSALMON = new GameColor(255, 160, 122,
			255);

	public static final GameColor ANTIQUEWHITE = new GameColor(250, 235, 215,
			255);

	public static final GameColor LIGHTSEAGREEN = new GameColor(32, 178, 170,
			255);

	public static final GameColor AQUA = new GameColor(0, 255, 255, 255);

	public static final GameColor LIGHTSKYBLUE = new GameColor(135, 206, 250,
			255);

	public static final GameColor AQUAMARINE = new GameColor(127, 255, 212, 255);

	public static final GameColor LIGHTSLATEGRAY = new GameColor(119, 136, 153,
			255);

	public static final GameColor AZURE = new GameColor(240, 255, 255, 255);

	public static final GameColor LIGHTSTEELBLUE = new GameColor(176, 196, 222,
			255);

	public static final GameColor BEIGE = new GameColor(245, 245, 220, 255);

	public static final GameColor LIGHTYELLOW = new GameColor(255, 255, 224,
			255);

	public static final GameColor BISQUE = new GameColor(255, 228, 196, 255);

	public static final GameColor LIME = new GameColor(0, 255, 0, 255);

	public static final GameColor BLACK = new GameColor(0, 0, 0, 255);

	public static final GameColor LIMEGREEN = new GameColor(50, 205, 50, 255);

	public static final GameColor BLANCHEDALMOND = new GameColor(255, 255, 205,
			255);

	public static final GameColor LINEN = new GameColor(250, 240, 230, 255);

	public static final GameColor BLUE = new GameColor(0, 0, 255, 255);

	public static final GameColor MAGENTA = new GameColor(255, 0, 255, 255);

	public static final GameColor BLUEVIOLET = new GameColor(138, 43, 226, 255);

	public static final GameColor MAROON = new GameColor(128, 0, 0, 255);

	public static final GameColor BROWN = new GameColor(165, 42, 42, 255);

	public static final GameColor MEDIUMAQUAMARINE = new GameColor(102, 205,
			170, 255);

	public static final GameColor BURLYWOOD = new GameColor(222, 184, 135, 255);

	public static final GameColor MEDIUMBLUE = new GameColor(0, 0, 205, 255);

	public static final GameColor CADETBLUE = new GameColor(95, 158, 160, 255);

	public static final GameColor MEDIUMORCHID = new GameColor(186, 85, 211,
			255);

	public static final GameColor CHARTREUSE = new GameColor(127, 255, 0, 255);

	public static final GameColor MEDIUMPURPLE = new GameColor(147, 112, 219,
			255);

	public static final GameColor CHOCOLATE = new GameColor(210, 105, 30, 255);

	public static final GameColor MEDIUMSEAGREEN = new GameColor(60, 179, 113,
			255);

	public static final GameColor CORAL = new GameColor(255, 127, 80, 255);

	public static final GameColor MEDIUMSLATEBLUE = new GameColor(123, 104,
			238, 255);

	public static final GameColor CORNFLOWERBLUE = new GameColor(100, 149, 237,
			255);

	public static final GameColor MEDIUMSPRINGGREEN = new GameColor(0, 250,
			154, 255);

	public static final GameColor CORNSILK = new GameColor(255, 248, 220, 255);

	public static final GameColor MEDIUMTURQUOISE = new GameColor(72, 209, 204,
			255);

	public static final GameColor CRIMSON = new GameColor(220, 20, 60, 255);

	public static final GameColor MEDIUMVIOLETRED = new GameColor(199, 21, 112,
			255);

	public static final GameColor CYAN = new GameColor(0, 255, 255, 255);

	public static final GameColor MIDNIGHTBLUE = new GameColor(25, 25, 112, 255);

	public static final GameColor DARKBLUE = new GameColor(0, 0, 139, 255);

	public static final GameColor MINTCREAM = new GameColor(245, 255, 250, 255);

	public static final GameColor DARKCYAN = new GameColor(0, 139, 139, 255);

	public static final GameColor MISTYROSE = new GameColor(255, 228, 225, 255);

	public static final GameColor DARKGOLDENROD = new GameColor(184, 134, 11,
			255);

	public static final GameColor MOCCASIN = new GameColor(255, 228, 181, 255);

	public static final GameColor DARKGRAY = new GameColor(169, 169, 169, 255);

	public static final GameColor NAVAJOWHITE = new GameColor(255, 222, 173,
			255);

	public static final GameColor DARKGREEN = new GameColor(0, 100, 0, 255);

	public static final GameColor NAVY = new GameColor(0, 0, 128, 255);

	public static final GameColor DARKKHAKI = new GameColor(189, 183, 107, 255);

	public static final GameColor OLDLACE = new GameColor(253, 245, 230, 255);

	public static final GameColor DARKMAGENA = new GameColor(139, 0, 139, 255);

	public static final GameColor OLIVE = new GameColor(128, 128, 0, 255);

	public static final GameColor DARKOLIVEGREEN = new GameColor(85, 107, 47,
			255);

	public static final GameColor OLIVEDRAB = new GameColor(107, 142, 45, 255);

	public static final GameColor DARKORANGE = new GameColor(255, 140, 0, 255);

	public static final GameColor ORANGE = new GameColor(255, 165, 0, 255);

	public static final GameColor DARKORCHID = new GameColor(153, 50, 204, 255);

	public static final GameColor ORANGERED = new GameColor(255, 69, 0, 255);

	public static final GameColor DARKRED = new GameColor(139, 0, 0, 255);

	public static final GameColor ORCHID = new GameColor(218, 112, 214, 255);

	public static final GameColor DARKSALMON = new GameColor(233, 150, 122, 255);

	public static final GameColor PALEGOLDENROD = new GameColor(238, 232, 170,
			255);

	public static final GameColor DARKSEAGREEN = new GameColor(143, 188, 143,
			255);

	public static final GameColor PALEGREEN = new GameColor(152, 251, 152, 255);

	public static final GameColor DARKSLATEBLUE = new GameColor(72, 61, 139,
			255);

	public static final GameColor PALETURQUOISE = new GameColor(175, 238, 238,
			255);

	public static final GameColor DARKSLATEGRAY = new GameColor(40, 79, 79, 255);

	public static final GameColor PALEVIOLETRED = new GameColor(219, 112, 147,
			255);

	public static final GameColor DARKTURQUOISE = new GameColor(0, 206, 209,
			255);

	public static final GameColor PAPAYAWHIP = new GameColor(255, 239, 213, 255);

	public static final GameColor DARKVIOLET = new GameColor(148, 0, 211, 255);

	public static final GameColor PEACHPUFF = new GameColor(255, 218, 155, 255);

	public static final GameColor DEEPPINK = new GameColor(255, 20, 147, 255);

	public static final GameColor PERU = new GameColor(205, 133, 63, 255);

	public static final GameColor DEEPSKYBLUE = new GameColor(0, 191, 255, 255);

	public static final GameColor PINK = new GameColor(255, 192, 203, 255);

	public static final GameColor DIMGRAY = new GameColor(105, 105, 105, 255);

	public static final GameColor PLUM = new GameColor(221, 160, 221, 255);

	public static final GameColor DODGERBLUE = new GameColor(30, 144, 255, 255);

	public static final GameColor POWDERBLUE = new GameColor(176, 224, 230, 255);

	public static final GameColor FIREBRICK = new GameColor(178, 34, 34, 255);

	public static final GameColor PURPLE = new GameColor(128, 0, 128, 255);

	public static final GameColor FLORALWHITE = new GameColor(255, 250, 240,
			255);

	public static final GameColor RED = new GameColor(255, 0, 0, 255);

	public static final GameColor FORESTGREEN = new GameColor(34, 139, 34, 255);

	public static final GameColor ROSYBROWN = new GameColor(188, 143, 143, 255);

	public static final GameColor FUSCHIA = new GameColor(255, 0, 255, 255);

	public static final GameColor ROYALBLUE = new GameColor(65, 105, 225, 255);

	public static final GameColor GAINSBORO = new GameColor(220, 220, 220, 255);

	public static final GameColor SADDLEBROWN = new GameColor(139, 69, 19, 255);

	public static final GameColor GHOSTWHITE = new GameColor(248, 248, 255, 255);

	public static final GameColor SALMON = new GameColor(250, 128, 114, 255);

	public static final GameColor GOLD = new GameColor(255, 215, 0, 255);

	public static final GameColor SANDYBROWN = new GameColor(244, 164, 96, 255);

	public static final GameColor GOLDENROD = new GameColor(218, 165, 32, 255);

	public static final GameColor SEAGREEN = new GameColor(46, 139, 87, 255);

	public static final GameColor GRAY = new GameColor(128, 128, 128, 255);

	public static final GameColor SEASHELL = new GameColor(255, 245, 238, 255);

	public static final GameColor GREEN = new GameColor(0, 128, 0, 255);

	public static final GameColor SIENNA = new GameColor(160, 82, 45, 255);

	public static final GameColor GREENYELLOW = new GameColor(173, 255, 47, 255);

	public static final GameColor SILVER = new GameColor(192, 192, 192, 255);

	public static final GameColor HONEYDEW = new GameColor(240, 255, 240, 255);

	public static final GameColor SKYBLUE = new GameColor(135, 206, 235, 255);

	public static final GameColor HOTPINK = new GameColor(255, 105, 180, 255);

	public static final GameColor SLATEBLUE = new GameColor(106, 90, 205, 255);

	public static final GameColor INDIANRED = new GameColor(205, 92, 92, 255);

	public static final GameColor SLATEGRAY = new GameColor(112, 128, 144, 255);

	public static final GameColor INDIGO = new GameColor(75, 0, 130, 255);

	public static final GameColor SNOW = new GameColor(255, 250, 250, 255);

	public static final GameColor IVORY = new GameColor(255, 240, 240, 255);

	public static final GameColor SPRINGGREEN = new GameColor(0, 255, 127, 255);

	public static final GameColor KHAKI = new GameColor(240, 230, 140, 255);

	public static final GameColor STEELBLUE = new GameColor(70, 130, 180, 255);

	public static final GameColor LAVENDER = new GameColor(230, 230, 250, 255);

	public static final GameColor TAN = new GameColor(210, 180, 140, 255);

	public static final GameColor LAVENDERBLUSH = new GameColor(255, 240, 245,
			255);

	public static final GameColor TEAL = new GameColor(0, 128, 128, 255);

	public static final GameColor LAWNGREEN = new GameColor(124, 252, 0, 255);

	public static final GameColor THISTLE = new GameColor(216, 191, 216, 255);

	public static final GameColor LEMONCHIFFON = new GameColor(255, 250, 205,
			255);

	public static final GameColor TOMATO = new GameColor(253, 99, 71, 255);

	public static final GameColor LIGHTBLUE = new GameColor(173, 216, 230, 255);

	public static final GameColor TURQUOISE = new GameColor(64, 224, 208, 255);

	public static final GameColor LIGHTCORAL = new GameColor(240, 128, 128, 255);

	public static final GameColor VIOLET = new GameColor(238, 130, 238, 255);

	public static final GameColor LIGHTCYAN = new GameColor(224, 255, 255, 255);

	public static final GameColor WHEAT = new GameColor(245, 222, 179, 255);

	public static final GameColor LIGHTGOLDENRODYELLOW = new GameColor(250,
			250, 210, 255);

	public static final GameColor LIGHTGREEN = new GameColor(144, 238, 144, 255);

	public static final GameColor WHITESMOKE = new GameColor(245, 245, 245, 255);

	public static final GameColor LIGHTGRAY = new GameColor(211, 211, 211, 255);

	public static final GameColor YELLOW = new GameColor(255, 255, 0, 255);

	public static final GameColor LIGHTPINK = new GameColor(255, 182, 193, 255);

	public static final GameColor YELLOWGREEN = new GameColor(154, 205, 50, 255);

	public GameColor clone() {
		return new GameColor(this.r, this.g, this.b, this.a);
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Color");
		xml.element("R").text(this.r).pop();
		xml.element("G").text(this.g).pop();
		xml.element("B").text(this.b).pop();
		xml.element("A").text(this.a).pop();
		xml.pop();
	}

	@Override
	public GameColor xmlRead(Element element) {
		float r = element.getFloat("R");
		float g = element.getFloat("G");
		float b = element.getFloat("B");
		float a = element.getFloat("A");
		return new GameColor(r, g, b, a);
	}

	public GameColor lerp(GameColor color, float f) {
		return (GameColor) super.lerp(color.to_badlogic_color().cpy(), f);
	}

}
