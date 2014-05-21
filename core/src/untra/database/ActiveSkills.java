package untra.database;

/**
 * 00-99 Elemental magic 100-199 Healing spells 200-299 physical attacks 300-399
 * 400-499
 * 
 * @author samuel
 * 
 */
public class ActiveSkills {

	public static Skill Flame(int scale) {
		Skill skill = new Skill();
		skill.id = 10 + scale;
		skill.element = Elemental.Fire;
		skill.name = "Pyron " + scale;
		skill.description = "Brings forth a burst of angry hot flame";
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.POW;
		skill.sp_cost = scale * (scale + 7);
		skill.power = 0.3f + (scale * 0.35f);
		skill.range = 1 + scale;
		skill.field = (scale / 3) >= 1 ? 1 : 0;
		skill.level_cost = (scale + 1) + ((scale - 1) * scale);
		return skill;
	}

	public static Skill Ice(int scale) {
		Skill skill = new Skill();
		skill.id = 20 + scale;
		skill.element = Elemental.Ice;
		skill.name = "Cryon " + scale;
		skill.description = "Brings forth a burst of freezing cold justice";
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.MND;
		skill.sp_cost = scale * (scale + 8);
		skill.power = 0.5f + (scale * 0.20f);
		skill.range = 1 + scale;
		skill.field = (scale / 2);
		skill.level_cost = (scale + 2) + ((scale - 1) * scale);
		return skill;
	}

	public static Skill Lightning(int scale) {
		Skill skill = new Skill();
		skill.id = 30 + scale;
		skill.element = Elemental.Thunder;
		skill.name = "Electro " + scale;
		skill.description = "Brings forth a strike of lightning";
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.SKL;
		skill.sp_cost = scale * (scale + 9);
		skill.power = 0.4f + (scale * 0.25f);
		skill.range = 2 * scale;
		skill.field = 0;
		skill.level_cost = (scale + 3) + ((scale - 1) * scale);
		return skill;
	}

	public static Skill Earth(int scale) {
		Skill skill = new Skill();
		skill.id = 40 + scale;
		skill.element = Elemental.Earth;
		skill.name = "Terran " + scale;
		skill.description = "Brings forth a Pillar of Earth";
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.POW;
		skill.sp_cost = (scale + 1) * (scale + 7);
		skill.is_line = true;
		skill.power = 0.4f + (scale * 0.30f);
		skill.range = 1 + scale;
		skill.field = 0;
		skill.level_cost = (scale + 4) + ((scale - 2) * scale);
		return skill;
	}

	public static Skill Water(int scale) {
		Skill skill = new Skill();
		skill.id = 50 + scale;
		skill.element = Elemental.Fire;
		skill.name = "Aqua " + scale;
		skill.description = "Brings forth a burst of water";
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.MND;
		skill.sp_cost = (scale + 1) * (scale + 8);
		skill.variance = 0.1f + (scale * 0.15f);
		skill.power = 0.6f + (scale * 0.15f);
		skill.range = 2 + scale;
		skill.field = 0;
		skill.level_cost = (scale + 5) + ((scale - 2) * scale);
		return skill;
	}

	public static Skill Wind(int scale) {
		Skill skill = new Skill();
		skill.id = 60 + scale;
		skill.element = Elemental.Wind;
		skill.name = "Aero " + scale;
		skill.description = "Brings forth a burst of wind";
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.SKL;
		skill.sp_cost = (scale + 1) * (scale + 9);
		skill.power = 0.5f + (scale * 0.20f);
		skill.range = scale;
		skill.field = ((scale - 1) / 3);
		skill.level_cost = (scale + 6) + ((scale - 2) * scale);
		return skill;
	}

	public static Skill Heal(int scale) {
		Skill skill = new Skill();
		skill.id = 100 + scale;
		skill.element = Elemental.none;
		skill.name = "Vitae " + scale;
		skill.description = "Heals the target";
		skill.scope = Skill_Scope.heal;
		skill.type = Skill_Type.MND;
		skill.sp_cost = 4 * scale;
		skill.power = -0.5f - (0.1f * scale);
		skill.range = scale;
		skill.field = 0;
		skill.level_cost = scale * scale;
		return skill;
	}

	public static Skill Aura(int scale) {
		Skill skill = new Skill();
		skill.id = 110 + scale;
		skill.element = Elemental.none;
		skill.name = "Aura " + scale;
		skill.description = "Heals multiple targets";
		skill.scope = Skill_Scope.heal;
		skill.type = Skill_Type.MND;
		skill.sp_cost = (16 * scale);
		skill.power = -0.25f - (0.15f * scale);
		skill.range = scale;
		skill.field = (scale / 3) + 1;
		skill.level_cost = (scale + 2) * (scale + 3);
		return skill;
	}

	public static Skill Cure(int scale) {
		Skill skill = new Skill();
		skill.id = 120 + scale;
		skill.element = Elemental.none;
		skill.name = "Cure " + scale;
		skill.description = "Removes a number of statuses from the target";
		skill.scope = Skill_Scope.heal;
		skill.type = Skill_Type.MND;
		skill.sp_cost = (2 + scale) * (scale);
		skill.power = 0;
		skill.range = scale - 1;
		skill.field = (scale - 1 / 2);
		skill.level_cost = scale * 3;
		skill.common_event_id = 1;
		return skill;
	}

	public static Skill Knockback_Swing(int scale) {
		Skill skill = new Skill();
		skill.id = 200 + scale;
		skill.element = Elemental.none;
		skill.name = "Knockback " + scale;
		skill.description = "Does damage to the target, knocking them back.";
		skill.is_spell = false;
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.POW;
		skill.sp_cost = 4 * scale;
		skill.power = 0.7f + (0.1f * scale);
		skill.range = 1;
		skill.field = 0;
		skill.level_cost = scale * 5 - 1;
		skill.common_event_id = 2;
		return skill;
	}

	public static Skill Twinstrike_Swing(int scale) {
		Skill skill = new Skill();
		skill.id = 210 + scale;
		skill.element = Elemental.none;
		skill.name = "Twinstrike " + scale;
		skill.description = "Does damage to the target, striking multiple times.";
		skill.is_spell = false;
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.SKL;
		skill.sp_cost = 4 * scale;
		skill.power = 0.45f + (0.05f * scale);
		skill.range = 1;
		skill.field = 0;
		skill.level_cost = scale * 5 - 1;
		skill.common_event_id = 3;
		return skill;
	}

	public static Skill criticalstrike_Swing(int scale) {
		Skill skill = new Skill();
		skill.id = 220 + scale;
		skill.element = Elemental.none;
		skill.name = "Criticalstrike " + scale;
		skill.description = "Does damage to the target, striking critically.";
		skill.is_spell = false;
		skill.scope = Skill_Scope.attack;
		skill.type = Skill_Type.MND;
		skill.sp_cost = 4 * scale;
		skill.power = 0.45f + (0.05f * scale);
		skill.range = 1;
		skill.field = 0;
		skill.level_cost = scale * 5 - 1;
		skill.common_event_id = 4;
		return skill;
	}

}
