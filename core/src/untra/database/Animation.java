package untra.database;

import untra.graphics.Blend;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Animation implements IXml<Animation>, Idata {
	public enum Focus {
		none, actor, screen
	};

	public int id;
	public String name;
	public String sound;
	public String sheet;
	//public GameColor hue = GameColor.TRUEWHITE;
	public int hue;
	public int columns, rows;
	public int framemax;
	public Timing[] timings = {};
	public Blend blend = Blend.alpha;

	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Animation");
		xml.element("name").text(this.name).pop();
		xml.element("id").text(this.name).pop();
		xml.element("sheet").text(this.sheet).pop();
		xml.element("sound").text(this.sound).pop();
		xml.element("hue").text(this.hue).pop();
		xml.element("columns").text(this.columns).pop();
		xml.element("rows").text(this.rows).pop();
		xml.element("framemax").text(this.framemax).pop();
		xml.element("timings").attribute("length", timings.length);
		for (Timing timing : timings) {
			timing.xmlWrite(xml);
		}
		xml.pop();
		blend.xmlWrite(xml);
		xml.pop();
	}

	public Animation xmlRead(Element element) {
		Animation anim = new Animation();
		anim.id = element.getInt("hue");
		anim.name = element.get("name");
		anim.sheet = element.get("sheet");
		anim.sound = element.get("sound");
		//anim.hue = hue.xmlRead(element.getChildByName("Color"));
		anim.columns = element.getInt("columns");
		anim.rows = element.getInt("rows");
		anim.framemax = element.getInt("framemax", columns * rows);
		// Element test = element.getChildByName("timings");
		anim.timings = new Timing[element.getChildByName("timings")
				.getIntAttribute("length")];
		int x = 0;
		for (Element e : element.getChildByName("timings").getChildrenByName(
				"Timing")) {
			Timing timing = new Timing();
			anim.timings[x] = timing.xmlRead(e);
			x++;
		}
		anim.blend = blend.xmlRead(element);
		return anim;

	}

	public String toString() {
		return this.name;
	}

	public class Timing implements IXml<Timing> {

		@Override
		public void xmlWrite(XmlWriter xml) throws IOException {
			xml.element("Timing").pop();
		}

		@Override
		public Timing xmlRead(Element element) {
			Timing timing = new Timing();

			return timing;
		}

	}
}
