package untra.database;

import java.io.IOException;

import untra.player.Actor;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public enum Race {
	Human, Emberborn, Ratmen, Fey, Canid, Avis, Machina, Undead, Fauna, Beast, Aquatic, Ignis;
	private static final int[] DEFAULT_GAINS = {1,11,22,33,47};
	public int[] gains()
	{
		switch (this) {
		case Human:
			return DEFAULT_GAINS;
		default:
			return DEFAULT_GAINS;
		}
	}
	
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Race").text(this.ordinal()).pop();

	}

	public Race xmlRead(Element element) {
		int o = element.getInt("Race");
		return values()[o];
	}

	public static String toOrdinalString() {
		String string = "";
		for (Race race : Race.values()) {
			string += race.ordinal();
			string += ": ";
			string += race.toString();
			string += "\n";
		}
		return string;
	}

	public static String[] toStringArray() {
		String[] array = new String[values().length];
		for (int i = 0; i < values().length; i++) {
			array[i] = values()[i].toString();
		}
		return array;
	}

	public void applyRacialStats(Actor actor) {
		switch (this) {
		case Human: {
			actor.changeMAX_HP(2);
			break;
		}
		case Emberborn: {
			actor.elementals.add(Elemental.Fire);
			actor.changeSPD(-5);
			break;
		}
		case Ratmen: {
			actor.changeMAX_SP(-3);
			actor.changeMAX_HP(-3);
			actor.changeSPD(5);
			actor.changeVSN(1);
			actor.changeMOV(1);
			actor.changeMND(-1);
			actor.changePOW(-1);
			break;
		}
		case Fey: {
			actor.changeMAX_SP(2);
			actor.changeMND(3);
			actor.changePOW(-3);
			break;
		}
		case Canid: {
			actor.changeSKL(-3);
			actor.changeSPD(5);
			break;
		}
		case Machina: {
			actor.elementals.add(Elemental.Machine);
			actor.changePOW(-3);
			actor.changeSKL(-3);
			actor.changeMND(-3);
			break;
		}
		case Avis: {
			actor.elementals.add(Elemental.Wind);
			actor.changeSPD(5);
			actor.changeSKL(-3);
			break;
		}
		case Undead: {
			actor.elementals.add(Elemental.Undead);
			actor.changeSPD(-5);
			actor.changeMOV(-1);
			actor.changeVSN(1);
			break;
		}
		case Fauna: {
			actor.elementals.add(Elemental.Plant);
			actor.changeSPD(-5);
			actor.changeMOV(-1);
			actor.changePOW(2);
			actor.changeMAX_HP(3);
			break;
		}
		case Beast: {
			actor.changeMAX_HP(-3);
			actor.changePOW(4);
			break;
		}
		case Aquatic: {
			actor.elementals.add(Elemental.Water);
			actor.changeMAX_HP(5);
			break;
		}
		case Ignis: {
			actor.elementals.add(Elemental.Fire);
			actor.changeMAX_HP(3);
			actor.changeMND(1);
			actor.changeMAX_SP(-6);
			break;
		}
		}
	}
}
