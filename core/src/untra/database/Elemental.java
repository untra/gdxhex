package untra.database;

import java.io.IOException;

import untra.player.Actor;
import untra.scene.game.battle.BattleAction;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public enum Elemental implements IXml<Elemental> {
	none, Fire, Ice, Thunder, Water, Earth, Wind, Plant, Machine, Undead, Holy;

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Element").text(this.ordinal()).pop();

	}

	@Override
	public Elemental xmlRead(Element element) {
		int o = element.getInt("Element");
		return values()[o];
	}

	public static float elementalMultiplier(BattleAction action, Actor target) {
		switch (action.elemental) {
		case Fire:
			if (target.elementals.contains(Fire))
				return -0.3f;
			if (target.elementals.contains(Water))
				return 0.5f;
			if (target.elementals.contains(Ice))
				return 1.5f;
			if (target.elementals.contains(Plant))
				return 1.5f;
			break;
		case Water:
			if (target.elementals.contains(Water))
				return -0.3f;
			if (target.elementals.contains(Plant))
				return 0.5f;
			if (target.elementals.contains(Fire))
				return 1.5f;
			break;
		case Thunder:
			if (target.elementals.contains(Machine))
				return -0.3f;
			if (target.elementals.contains(Earth))
				return 0.3f;
			if (target.elementals.contains(Water))
				return 1.5f;
			break;
		case Ice:
			if (target.elementals.contains(Ice))
				return -0.3f;
			if (target.elementals.contains(Fire))
				return 0.5f;
			if (target.elementals.contains(Plant))
				return 1.5f;
			break;
		case Earth:
			if (target.elementals.contains(Plant))
				return 0.5f;
			if (target.elementals.contains(Wind))
				return 1.5f;
			break;
		case Wind:
			if (target.elementals.contains(Earth))
				return 0.5f;
			if (target.elementals.contains(Wind))
				return 1.5f;
			break;
		case Plant:
			if (target.elementals.contains(Plant))
				return 0.3f;
			if (target.elementals.contains(Fire))
				return 0.3f;
			if (target.elementals.contains(Water))
				return 1.5f;
			if (target.elementals.contains(Earth))
				return 1.5f;
			break;
		case Machine:
			if (target.elementals.contains(Thunder))
				return 0.3f;
			if (target.elementals.contains(Water))
				return 0.3f;
			if (target.elementals.contains(Plant))
				return 0.5f;
			if (target.elementals.contains(Undead))
				return 0.5f;
			if (target.elementals.contains(Machine))
				return 1.5f;
		case Undead:
			if (target.elementals.contains(Undead))
				return -0.3f;
			if (target.elementals.contains(Fire))
				return 0.3f;
			if (target.elementals.contains(Ice))
				return 0.5f;
		case Holy:
			if (target.elementals.contains(Undead))
				return 1.5f;
			if (target.elementals.contains(Holy))
				return -0.3f;
			if (target.elementals.contains(Machine))
				return 0.3f;
		default:
			return 1.0f;
		}
		return 1.0f;
	}
};
