package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public enum Skill_Type implements IXml<Skill_Type> {
	POW, SKL, MND;

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Skill_Type").text(this.ordinal()).pop();

	}

	@Override
	public Skill_Type xmlRead(Element element) {
		int o = element.getInt("Skill_Type");
		return values()[o];
	}
};
