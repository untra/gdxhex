package untra.database;


import untra.scene.game.battle.Battler;


public class Skill extends DatabaseObject implements Idata {
	public Skill_Scope scope;
	/**
	 * level cost dictates what level the skill is learned at.
	 * A value of x indicates the skill can only be learned when the player is at level
	 * 5x and costs the exp equivalent of leveling up to 3x
	 * a value of 0 indicates a player can instantly gain the skill
	 * a value of 1 
	 */
//	public int level_cost;
//	public int prereq;

	// public List<Status> Status_add;
	// public List<Status> Status_sub;

	public Skill() {
		id = 0;
		name = "NULL";
		description = "???";
		scope = Skill_Scope.none;
//		level_cost = 0;
//		prereq = 0;

	}
	
	public Skill(Data d) {
		super(d);
		scope = Skill_Scope.values()[d.getInt("scope")];
//		prereq = d.getInt("prereq", 0);
//		level_cost = d.getInt("level_cost");
	}

//
//
//	@Override
//	public void xmlWrite(XmlWriter xml) throws IOException {
//		xml.element("Skill").attribute("id", this.id);
//		xml.element("name").text(this.name).pop();
//		xml.element("description").text(this.description).pop();
//		xml.element("scope");
//		scope.xmlWrite(xml);
//		xml.pop();
//		xml.element("element");
//		element.xmlWrite(xml);
//		xml.pop();
//		xml.element("type");
//		type.xmlWrite(xml);
//		xml.pop();
//		xml.element("sp_cost").text(sp_cost).pop();
//		xml.element("power").text(power).pop();
//		xml.element("variance").text(variance).pop();
//		xml.element("is_spell").text(is_spell).pop();
//		xml.element("range").text(range).pop();
//		xml.element("field").text(field).pop();
//		xml.element("is_line").text(is_line).pop();
//		xml.element("anim_use_id").text(anim_use_id).pop();
//		xml.element("anim_hit_id").text(anim_hit_id).pop();
//		xml.element("common_event_id").text(common_event_id).pop();
//		xml.element("level_cost").text(level_cost).pop();
//		xml.element("accuracy").text(accuracy).pop();
//		xml.pop();
//	}
//
//	@Override
//	public Skill xmlRead(com.badlogic.gdx.utils.XmlReader.Element element) {
//		Skill skill = new Skill();
//		skill.id = element.getIntAttribute("id");
//		skill.name = element.get("name");
//		skill.description = element.get("description");
//		skill.scope = skill.scope.xmlRead(element.getChildByName("scope"));
//		skill.element = skill.element
//				.xmlRead(element.getChildByName("element"));
//		skill.type = skill.type.xmlRead(element.getChildByName("type"));
//		skill.sp_cost = element.getInt("sp_cost");
//		skill.anim_use_id = element.getInt("anim_use_id");
//		skill.anim_hit_id = element.getInt("anim_hit_id");
//		skill.common_event_id = element.getInt("common_event_id");
//		skill.range = element.getInt("range");
//		skill.field = element.getInt("field");
//		skill.power = element.getFloat("power");
//		skill.variance = element.getFloat("variance");
//		skill.is_spell = element.getBoolean("is_spell");
//		skill.is_line = element.getBoolean("is_line");
//		skill.level_cost = element.getInt("level_cost", 0);
//		skill.accuracy = element.getFloat("accuracy", 1.0f);
//		return skill;
//	}

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
	 * Invoked when the skill is acquired and added to the Skillset
	 */
	public void acquire()
	{
		return;
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
