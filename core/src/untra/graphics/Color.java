package untra.graphics;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Color extends com.badlogic.gdx.graphics.Color implements
		IXml<Color> {

	public Color(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	public static Color ALICEBLUE = new Color(240, 248, 255, 255);

	public static Color LIGHTSALMON = new Color(255, 160, 122, 255);

	public static Color ANTIQUEWHITE = new Color(250, 235, 215, 255);

	public static Color LIGHTSEAGREEN = new Color(32, 178, 170, 255);

	public static Color AQUA = new Color(0, 255, 255, 255);

	public static Color LIGHTSKYBLUE = new Color(135, 206, 250, 255);

	public static Color AQUAMARINE = new Color(127, 255, 212, 255);

	public static Color LIGHTSLATEGRAY = new Color(119, 136, 153, 255);

	public static Color AZURE = new Color(240, 255, 255, 255);

	public static Color LIGHTSTEELBLUE = new Color(176, 196, 222, 255);

	public static Color BEIGE = new Color(245, 245, 220, 255);

	public static Color LIGHTYELLOW = new Color(255, 255, 224, 255);

	public static Color BISQUE = new Color(255, 228, 196, 255);

	public static Color LIME = new Color(0, 255, 0, 255);

	public static Color BLACK = new Color(0, 0, 0, 255);

	public static Color LIMEGREEN = new Color(50, 205, 50, 255);

	public static Color BLANCHEDALMOND = new Color(255, 255, 205, 255);

	public static Color LINEN = new Color(250, 240, 230, 255);

	public static Color BLUE = new Color(0, 0, 255, 255);

	public static Color MAGENTA = new Color(255, 0, 255, 255);

	public static Color BLUEVIOLET = new Color(138, 43, 226, 255);

	public static Color MAROON = new Color(128, 0, 0, 255);

	public static Color BROWN = new Color(165, 42, 42, 255);

	public static Color MEDIUMAQUAMARINE = new Color(102, 205, 170, 255);

	public static Color BURLYWOOD = new Color(222, 184, 135, 255);

	public static Color MEDIUMBLUE = new Color(0, 0, 205, 255);

	public static Color CADETBLUE = new Color(95, 158, 160, 255);

	public static Color MEDIUMORCHID = new Color(186, 85, 211, 255);

	public static Color CHARTREUSE = new Color(127, 255, 0, 255);

	public static Color MEDIUMPURPLE = new Color(147, 112, 219, 255);

	public static Color CHOCOLATE = new Color(210, 105, 30, 255);

	public static Color MEDIUMSEAGREEN = new Color(60, 179, 113, 255);

	public static Color CORAL = new Color(255, 127, 80, 255);

	public static Color MEDIUMSLATEBLUE = new Color(123, 104, 238, 255);

	public static Color CORNFLOWERBLUE = new Color(100, 149, 237, 255);

	public static Color MEDIUMSPRINGGREEN = new Color(0, 250, 154, 255);

	public static Color CORNSILK = new Color(255, 248, 220, 255);

	public static Color MEDIUMTURQUOISE = new Color(72, 209, 204, 255);

	public static Color CRIMSON = new Color(220, 20, 60, 255);

	public static Color MEDIUMVIOLETRED = new Color(199, 21, 112, 255);

	public static Color CYAN = new Color(0, 255, 255, 255);

	public static Color MIDNIGHTBLUE = new Color(25, 25, 112, 255);

	public static Color DARKBLUE = new Color(0, 0, 139, 255);

	public static Color MINTCREAM = new Color(245, 255, 250, 255);

	public static Color DARKCYAN = new Color(0, 139, 139, 255);

	public static Color MISTYROSE = new Color(255, 228, 225, 255);

	public static Color DARKGOLDENROD = new Color(184, 134, 11, 255);

	public static Color MOCCASIN = new Color(255, 228, 181, 255);

	public static Color DARKGRAY = new Color(169, 169, 169, 255);

	public static Color NAVAJOWHITE = new Color(255, 222, 173, 255);

	public static Color DARKGREEN = new Color(0, 100, 0, 255);

	public static Color NAVY = new Color(0, 0, 128, 255);

	public static Color DARKKHAKI = new Color(189, 183, 107, 255);

	public static Color OLDLACE = new Color(253, 245, 230, 255);

	public static Color DARKMAGENA = new Color(139, 0, 139, 255);

	public static Color OLIVE = new Color(128, 128, 0, 255);

	public static Color DARKOLIVEGREEN = new Color(85, 107, 47, 255);

	public static Color OLIVEDRAB = new Color(107, 142, 45, 255);

	public static Color DARKORANGE = new Color(255, 140, 0, 255);

	public static Color ORANGE = new Color(255, 165, 0, 255);

	public static Color DARKORCHID = new Color(153, 50, 204, 255);

	public static Color ORANGERED = new Color(255, 69, 0, 255);

	public static Color DARKRED = new Color(139, 0, 0, 255);

	public static Color ORCHID = new Color(218, 112, 214, 255);

	public static Color DARKSALMON = new Color(233, 150, 122, 255);

	public static Color PALEGOLDENROD = new Color(238, 232, 170, 255);

	public static Color DARKSEAGREEN = new Color(143, 188, 143, 255);

	public static Color PALEGREEN = new Color(152, 251, 152, 255);

	public static Color DARKSLATEBLUE = new Color(72, 61, 139, 255);

	public static Color PALETURQUOISE = new Color(175, 238, 238, 255);

	public static Color DARKSLATEGRAY = new Color(40, 79, 79, 255);

	public static Color PALEVIOLETRED = new Color(219, 112, 147, 255);

	public static Color DARKTURQUOISE = new Color(0, 206, 209, 255);

	public static Color PAPAYAWHIP = new Color(255, 239, 213, 255);

	public static Color DARKVIOLET = new Color(148, 0, 211, 255);

	public static Color PEACHPUFF = new Color(255, 218, 155, 255);

	public static Color DEEPPINK = new Color(255, 20, 147, 255);

	public static Color PERU = new Color(205, 133, 63, 255);

	public static Color DEEPSKYBLUE = new Color(0, 191, 255, 255);

	public static Color PINK = new Color(255, 192, 203, 255);

	public static Color DIMGRAY = new Color(105, 105, 105, 255);

	public static Color PLUM = new Color(221, 160, 221, 255);

	public static Color DODGERBLUE = new Color(30, 144, 255, 255);

	public static Color POWDERBLUE = new Color(176, 224, 230, 255);

	public static Color FIREBRICK = new Color(178, 34, 34, 255);

	public static Color PURPLE = new Color(128, 0, 128, 255);

	public static Color FLORALWHITE = new Color(255, 250, 240, 255);

	public static Color RED = new Color(255, 0, 0, 255);

	public static Color FORESTGREEN = new Color(34, 139, 34, 255);

	public static Color ROSYBROWN = new Color(188, 143, 143, 255);

	public static Color FUSCHIA = new Color(255, 0, 255, 255);

	public static Color ROYALBLUE = new Color(65, 105, 225, 255);

	public static Color GAINSBORO = new Color(220, 220, 220, 255);

	public static Color SADDLEBROWN = new Color(139, 69, 19, 255);

	public static Color GHOSTWHITE = new Color(248, 248, 255, 255);

	public static Color SALMON = new Color(250, 128, 114, 255);

	public static Color GOLD = new Color(255, 215, 0, 255);

	public static Color SANDYBROWN = new Color(244, 164, 96, 255);

	public static Color GOLDENROD = new Color(218, 165, 32, 255);

	public static Color SEAGREEN = new Color(46, 139, 87, 255);

	public static Color GRAY = new Color(128, 128, 128, 255);

	public static Color SEASHELL = new Color(255, 245, 238, 255);

	public static Color GREEN = new Color(0, 128, 0, 255);

	public static Color SIENNA = new Color(160, 82, 45, 255);

	public static Color GREENYELLOW = new Color(173, 255, 47, 255);

	public static Color SILVER = new Color(192, 192, 192, 255);

	public static Color HONEYDEW = new Color(240, 255, 240, 255);

	public static Color SKYBLUE = new Color(135, 206, 235, 255);

	public static Color HOTPINK = new Color(255, 105, 180, 255);

	public static Color SLATEBLUE = new Color(106, 90, 205, 255);

	public static Color INDIANRED = new Color(205, 92, 92, 255);

	public static Color SLATEGRAY = new Color(112, 128, 144, 255);

	public static Color INDIGO = new Color(75, 0, 130, 255);

	public static Color SNOW = new Color(255, 250, 250, 255);

	public static Color IVORY = new Color(255, 240, 240, 255);

	public static Color SPRINGGREEN = new Color(0, 255, 127, 255);

	public static Color KHAKI = new Color(240, 230, 140, 255);

	public static Color STEELBLUE = new Color(70, 130, 180, 255);

	public static Color LAVENDER = new Color(230, 230, 250, 255);

	public static Color TAN = new Color(210, 180, 140, 255);

	public static Color LAVENDERBLUSH = new Color(255, 240, 245, 255);

	public static Color TEAL = new Color(0, 128, 128, 255);

	public static Color LAWNGREEN = new Color(124, 252, 0, 255);

	public static Color THISTLE = new Color(216, 191, 216, 255);

	public static Color LEMONCHIFFON = new Color(255, 250, 205, 255);

	public static Color TOMATO = new Color(253, 99, 71, 255);

	public static Color LIGHTBLUE = new Color(173, 216, 230, 255);

	public static Color TURQUOISE = new Color(64, 224, 208, 255);

	public static Color LIGHTCORAL = new Color(240, 128, 128, 255);

	public static Color VIOLET = new Color(238, 130, 238, 255);

	public static Color LIGHTCYAN = new Color(224, 255, 255, 255);

	public static Color WHEAT = new Color(245, 222, 179, 255);

	public static Color LIGHTGOLDENRODYELLOW = new Color(250, 250, 210, 255);

	public static Color WHITE = new Color(255, 255, 255, 255);

	public static Color LIGHTGREEN = new Color(144, 238, 144, 255);

	public static Color WHITESMOKE = new Color(245, 245, 245, 255);

	public static Color LIGHTGRAY = new Color(211, 211, 211, 255);

	public static Color YELLOW = new Color(255, 255, 0, 255);

	public static Color LIGHTPINK = new Color(255, 182, 193, 255);

	public static Color YELLOWGREEN = new Color(154, 205, 50, 255);

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
	public Color xmlRead(Element element) {
		int r = element.getInt("R");
		int g = element.getInt("G");
		int b = element.getInt("B");
		int a = element.getInt("A");
		return new Color(r, g, b, a);
	}

}
