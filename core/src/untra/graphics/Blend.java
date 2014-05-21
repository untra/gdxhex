package untra.graphics;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public enum Blend implements IXml<Blend> {

	alpha, replace, additive, subtractive;

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Blend").text(this.ordinal()).pop();

	}

	@Override
	public Blend xmlRead(Element element) {
		int o = element.getInt("Blend");
		return values()[o];
	}
};
