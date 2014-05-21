package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public enum Item_Use implements IXml<Item_Use> {
	always, battle_only, menu_only, never;

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Item_Use").text(this.ordinal()).pop();

	}

	@Override
	public Item_Use xmlRead(Element element) {
		int o = element.getInt("Item_Use");
		return values()[o];
	}
};
