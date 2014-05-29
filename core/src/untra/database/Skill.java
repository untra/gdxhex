package untra.database;

import java.io.IOException;

import untra.scene.game.battle.Battler;

import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Skill implements IXml<Skill>, Idata, IRanged {
	public int id;
	public String name;
	public String description;
	public Skill_Scope scope;
	public Elemental element;
	public Skill_Type type;
	public int sp_cost;
	public float power;
	public float variance;
	public boolean is_spell;
	protected int range;
	public int anim_use_id;
	public int anim_hit_id;
	public int common_event_id;
	protected int field;
	public boolean is_line;
	public int level_cost;
	public float accuracy;

	// public List<Status> Status_add;
	// public List<Status> Status_sub;

	public Skill() {
		id = 0;
		name = "NULL";
		description = "???";
		scope = Skill_Scope.none;
		element = Elemental.none;
		type = Skill_Type.POW;
		sp_cost = 0;
		power = 0.5f;
		variance = 0.15f;
		is_spell = true;
		is_line = false;
		range = 1;
		anim_use_id = 0;
		anim_hit_id = 0;
		common_event_id = 0;
		field = 0;
		level_cost = 0;
		accuracy = 1.0f;
	}
	
	public Skill(Data d) {
		id = (int) d.properties().get("id");
		name = (String) d.properties().get("name");
		description = (String) d.properties().get("description");
		scope = Skill_Scope.values()[(int) d.properties().get("scope")];
		element = Elemental.values()[(int) d.properties().get("element")];
		type = Skill_Type.values()[(int) d.properties().get("type")];
		sp_cost = (int) d.properties().get("sp_cost");
		power = (float) d.properties().get("power");
		variance = (float) d.properties().get("variance");
		is_spell = (boolean) d.properties().get("is_spell");
		is_line = (boolean) d.properties().get("is_line");
		range = (int) d.properties().get("range");
		anim_use_id = (int) d.properties().get("anim_use_id");
		anim_hit_id = (int) d.properties().get("anim_hit_id");
		common_event_id = (int) d.properties().get("common_event_id");
		field = (int) d.properties().get("field");
		level_cost = (int) d.properties().get("level_cost");
		accuracy = (float) d.properties().get("accuracy");
	}

	public int Range() {
		return range;
	}

	public int Field() {
		return field;
	}

	public static Skill range_skill(int range) {
		Skill s = new Skill();
		s.range = range;
		return s;
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Skill").attribute("id", this.id);
		xml.element("name").text(this.name).pop();
		xml.element("description").text(this.description).pop();
		xml.element("scope");
		scope.xmlWrite(xml);
		xml.pop();
		xml.element("element");
		element.xmlWrite(xml);
		xml.pop();
		xml.element("type");
		type.xmlWrite(xml);
		xml.pop();
		xml.element("sp_cost").text(sp_cost).pop();
		xml.element("power").text(power).pop();
		xml.element("variance").text(variance).pop();
		xml.element("is_spell").text(is_spell).pop();
		xml.element("range").text(range).pop();
		xml.element("field").text(field).pop();
		xml.element("is_line").text(is_line).pop();
		xml.element("anim_use_id").text(anim_use_id).pop();
		xml.element("anim_hit_id").text(anim_hit_id).pop();
		xml.element("common_event_id").text(common_event_id).pop();
		xml.element("level_cost").text(level_cost).pop();
		xml.element("accuracy").text(accuracy).pop();
		xml.pop();
	}

	@Override
	public Skill xmlRead(com.badlogic.gdx.utils.XmlReader.Element element) {
		Skill skill = new Skill();
		skill.id = element.getIntAttribute("id");
		skill.name = element.get("name");
		skill.description = element.get("description");
		skill.scope = skill.scope.xmlRead(element.getChildByName("scope"));
		skill.element = skill.element
				.xmlRead(element.getChildByName("element"));
		skill.type = skill.type.xmlRead(element.getChildByName("type"));
		skill.sp_cost = element.getInt("sp_cost");
		skill.anim_use_id = element.getInt("anim_use_id");
		skill.anim_hit_id = element.getInt("anim_hit_id");
		skill.common_event_id = element.getInt("common_event_id");
		skill.range = element.getInt("range");
		skill.field = element.getInt("field");
		skill.power = element.getFloat("power");
		skill.variance = element.getFloat("variance");
		skill.is_spell = element.getBoolean("is_spell");
		skill.is_line = element.getBoolean("is_line");
		skill.level_cost = element.getInt("level_cost", 0);
		skill.accuracy = element.getFloat("accuracy", 1.0f);
		return skill;
	}

	public String toString() {
		return this.name;
	}

	/**
	 * Invokes a specific method , using an integer to invoke the specified
	 * common event
	 * 
	 * @param i
	 * @param A
	 * @param T
	 */
	public static final void INVOKE_COMMON_EVENT(int i, Battler A, Battler T) {
		switch (i) {
		case 0:
			return;

		case 1:
			EVENT_001(A, T);
			break;

		case 2:
			EVENT_002(A, T);
			break;

		case 3:
			EVENT_003(A, T);
			break;

		case 4:
			EVENT_004(A, T);
			break;

		default:
			break;
		}
	}

	/**
	 * Cure common event removes negative status from the target
	 * 
	 * @param A
	 * @param T
	 */
	private static final void EVENT_001(Battler A, Battler T) {
		for (int i = 0; i < T.STATES.size(); i++) {
			if (T.STATES.get(i).is_nerf())
				T.STATES.remove(i);
		}
	}

	/**
	 * Knockback swing common event sets attacking battler to hit critical
	 * 
	 * @param A
	 * @param T
	 */
	private static final void EVENT_002(Battler A, Battler T) {
		A.set_use_knockback(true);
	}

	/**
	 * Twinstrike swing common event sets attacking battler to doublestrike
	 * 
	 * @param A
	 * @param T
	 */
	private static final void EVENT_003(Battler A, Battler T) {
		A.set_use_doublestrike(true);
	}

	/**
	 * Criticalstrike swing common event sets attacking battler to critical
	 * 
	 * @param A
	 * @param T
	 */
	private static final void EVENT_004(Battler A, Battler T) {
		A.set_use_critical(true);
	}
}
