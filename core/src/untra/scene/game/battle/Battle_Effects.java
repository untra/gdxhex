package untra.scene.game.battle;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Battle_Effects implements IXml<Battle_Effects> {
	private int value = 0;
	public boolean Critical;// 1
	public boolean DoubleStrike;// 2
	public boolean Knockback;// 4
	public boolean Counter;// 8
	public boolean Defending;// 16
	public boolean Evading;// 32

	private void setvalue() {
		value = 0;
		value += Critical ? 1 : 0;
		value += DoubleStrike ? 2 : 0;
		value += Knockback ? 4 : 0;
		value += Counter ? 8 : 0;
		value += Defending ? 16 : 0;
		value += Evading ? 32 : 0;
	}

	private Battle_Effects fromValue(int v) {
		Battle_Effects F = new Battle_Effects();
		if (v >= 32) {
			F.Evading = true;
			v -= 32;
		}
		if (v >= 16) {
			F.Defending = true;
			v -= 16;
		}
		if (v >= 8) {
			F.Counter = true;
			v -= 8;
		}
		if (v >= 4) {
			F.Knockback = true;
			v -= 4;
		}
		if (v >= 2) {
			F.DoubleStrike = true;
			v -= 2;
		}
		if (v >= 1) {
			F.Critical = true;
			v -= 1;
		}
		return F;
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		this.setvalue();
		xml.element("Battle_Effects");
		xml.element("Value").text(this.value).pop();
		xml.pop();

	}

	@Override
	public Battle_Effects xmlRead(Element element) {
		int v = element.getInt("Value");
		return fromValue(v);
	}

}
