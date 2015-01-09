package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public enum Skill_Scope implements IXml<Skill_Scope> {
	none, active, passive, reactive;
	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Skill_Scope").text(this.ordinal()).pop();

	}

	@Override
	public Skill_Scope xmlRead(Element element) {
		int o = element.getInt("Skill_Scope");
		return values()[o];
	}
};
