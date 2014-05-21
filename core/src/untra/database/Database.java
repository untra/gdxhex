package untra.database;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import untra.player.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class Database {

	private static final String ROOT = "data/game data/";

	public static Animation[] animations = new Animation[256];
	public static Klass[] classes = new Klass[256];
	public static Item[] items = new Item[256];
	public static Skill[] skills = new Skill[256];
	public static Status[] states = new Status[64];
	public static Tile_Object[] tile_objects = new Tile_Object[1024];
	public static Weapon[] weapons = new Weapon[128];
	public static Armor[] armors = new Armor[128];

	@SuppressWarnings("unused")
	private static void writetofile(FileHandle f, StringWriter w) {

		f.writeString(w.toString(), false);
	}

	public static int Tile_Object_Count() {
		for (int i = 0; i < tile_objects.length; i++) {
			if (tile_objects[i] == null)
				return i;
		}
		return tile_objects.length;
	}

	public static int DataCount(Object[] _objects) {
		for (int i = 0; i < _objects.length; i++) {
			if (_objects[i] == null)
				return i;
		}
		return _objects.length;
	}

	public static void init() {
		// animations = new ArrayList<Animation>();
		Element e;
		Element element;
		XmlReader reader = new XmlReader();
		// TEMP Writer
		Klass klass = new Klass();
		StringWriter stringWriter = new StringWriter();
		XmlWriter writer = new XmlWriter(stringWriter);
		try {
			klass.xmlWrite(writer);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.print(stringWriter.toString());

		try {
			element = reader.parse(Gdx.files.internal(ROOT + "animations.xml"));

			// animations
			ArrayList<Animation> set = new ArrayList<Animation>();
			Animation type = new Animation();
			for (int x = 0; x < element.getChildCount(); x++) {
				e = element.getChild(x);
				set.add(type.xmlRead(e));
			}
			animations = set.toArray(animations);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			element = reader.parse(Gdx.files.internal(ROOT + "classes.xml"));
			// classes
			ArrayList<Klass> set = new ArrayList<Klass>();
			Klass type = new Klass();
			for (int x = 0; x < element.getChildCount(); x++) {
				e = element.getChild(x);
				set.add(type.xmlRead(e));
			}
			classes = set.toArray(classes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			element = reader
					.parse(Gdx.files.internal(ROOT + "battleitems.xml"));
			// items
			ArrayList<Item> set = new ArrayList<Item>();
			BattleItem type = new BattleItem();
			for (int x = 0; x < element.getChildCount(); x++) {
				e = element.getChild(x);
				set.add(type.xmlRead(e));
			}
			items = set.toArray(items);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			element = reader.parse(Gdx.files.internal(ROOT + "skills.xml"));
			// skills
			ArrayList<Skill> set = new ArrayList<Skill>();
			Skill type = new Skill();
			for (int x = 0; x < element.getChildCount(); x++) {
				e = element.getChild(x);
				set.add(type.xmlRead(e));
			}
			skills = set.toArray(skills);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			element = reader.parse(Gdx.files.internal(ROOT + "states.xml"));
			// states
			ArrayList<Status> set = new ArrayList<Status>();
			Status type = new Status();
			for (int x = 0; x < element.getChildCount(); x++) {
				e = element.getChild(x);
				set.add(type.xmlRead(e));
			}
			states = set.toArray(states);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			element = reader.parse(Gdx.files
					.internal(ROOT + "tile_objects.xml"));
			// tile_objects
			ArrayList<Tile_Object> set = new ArrayList<Tile_Object>();
			Tile_Object type = new Tile_Object();
			for (int x = 0; x < element.getChildCount(); x++) {
				e = element.getChild(x);
				set.add(type.xmlRead(e));
			}
			tile_objects = set.toArray(tile_objects);
		} catch (Exception ex) {
			System.out.println("Tile_Objects exception!\n" + ex.getMessage());
			ex.printStackTrace();
		}
		try {
			element = reader.parse(Gdx.files.internal(ROOT + "weapons.xml"));
			// weapons
			ArrayList<Weapon> set = new ArrayList<Weapon>();
			Weapon type = new Weapon();
			for (int x = 0; x < element.getChildCount(); x++) {
				e = element.getChild(x);
				set.add(type.xmlRead(e));
			}
			weapons = set.toArray(weapons);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			element = reader.parse(Gdx.files.internal(ROOT + "armors.xml"));
			// armors
			ArrayList<Armor> set = new ArrayList<Armor>();
			Armor type = new Armor();
			for (int x = 0; x < element.getChildCount(); x++) {
				e = element.getChild(x);
				set.add(type.xmlRead(e));
			}
			armors = set.toArray(armors);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * given an actor, gives them a random or specific name
	 * 
	 * @param actor
	 */
	public void nameActor(Actor actor) {
		// If monster, the name comes from a table of names by race
		// else 60% general name, 25% class, 15% race
		if (actor.is_monster()) {
			switch (actor.getRace()) {
			case Aquatic: {
				actor.Name = pickRandomString(aquatic_names);
				return;
			}
			case Ignis: {
				actor.Name = pickRandomString(ignis_names);
				return;
			}
			case Beast: {
				actor.Name = pickRandomString(beast_names);
				return;
			}
			case Fauna: {
				actor.Name = pickRandomString(fauna_names);
				return;
			}
			case Undead: {
				actor.Name = pickRandomString(undead_names);
				return;
			}
			default: {
				actor.Name = "Error";
				return;
			}
			}
		} else {
			actor.Name = pickRandomString(general_names_male);
			return;
		}
	}

	/**
	 * picks a random String from an array of possibilities
	 * 
	 * @param options
	 * @return
	 */
	private String pickRandomString(String[] options) {
		int i = MathUtils.random(options.length);
		return options[i];
	}

	private String[] undead_names = { "Rraww", "Uurgg", "Qraw", "Llahr",
			"Shaaw", "Aach", "Eeich", "Ihch", "Grrr", "Durh", "Xaha", "Vlaa",
			"Zrrg" };
	private String[] beast_names = { "Leaf", "Sky", "Bark", "Paw", "Petal",
			"Rock", "Mud", "Berry", "Rain", "Tail", "Mane" };
	private String[] fauna_names = { "..." };
	private String[] ignis_names = { "Fawx", "Calcifer", "Singe", "Magla",
			"Charz", "Koals", "Tindr", "Smoke" };
	private String[] aquatic_names = { "Xffxc", "Reygg", "Pokffsz", "Kllsska",
			"Hunhhzz", "Lolccx", "Nevvcx", "Cthulix", "Thyrx" };
	private String[] general_names_male = { "John", "Chris", "Bobby",
			"Micheal", "Alex", "Danny", "Zach", "Jack", "Thomas", "Nick",
			"Chuck", "Anthony", "Manny", "Peter" };
	private String[] general_names_female = { "Mary", "Natalie", "Jen", "Izzy",
			"Karen", "Elizabeth", "Sandra", "Kristen", "Katie", "Michelle",
			"Sarah" };
}
