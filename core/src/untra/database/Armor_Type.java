package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public enum Armor_Type implements IXml<Armor_Type> {

	light, medium, heavy;

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Armor_Type").text(this.ordinal()).pop();
	}

	@Override
	public Armor_Type xmlRead(Element element) {
		int o = element.getInt("Armor_Type");
		return values()[o];
	}
}
