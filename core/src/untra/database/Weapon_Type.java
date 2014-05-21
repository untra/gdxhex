package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public enum Weapon_Type implements IXml<Weapon_Type> {
	Staves, Bows, Large_Blades, Claws, Wands, Guns, Short_Blades, Maces, Hammers, Lances, Axes, Knives, Bombs, none;

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Weapon_Type").text(this.ordinal()).pop();
	}

	@Override
	public Weapon_Type xmlRead(Element element) {
		int o = element.getInt("Weapon_Type");
		return values()[o];
	}

	public static String toOrdinalString() {
		String string = "";
		for (Weapon_Type weapon_Type : Weapon_Type.values()) {
			string += weapon_Type.ordinal();
			string += ": ";
			string += weapon_Type.toString();
			string += "\n";
		}
		return string;
	}
}
