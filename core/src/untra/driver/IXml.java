package untra.driver;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public interface IXml<T> {

	public void xmlWrite(XmlWriter xml) throws IOException;

	public T xmlRead(Element element);
}
