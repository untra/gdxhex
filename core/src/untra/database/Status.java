package untra.database;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Status implements IXml<Status>, Idata {
	public int id;
	public String name;
	public String description;
	public int anim_id;
	public boolean disable_magic;
	public boolean disable_skills;
	public boolean disable_attack;
	public boolean disable_move;
	/**
	 * If true, AI takes control of character and compels to basic or skill
	 * attack enemies exclusively If this and always_target_allies, character
	 * acts confused and will do either
	 */
	public boolean always_target_enemies;
	/**
	 * If true, AI takes control of character and compels to basic or skill
	 * atttack allies exclusively. If this and always_target_enemies, character
	 * acts confused and will do either
	 */
	public boolean always_target_allies;
	public boolean cannot_act;
	/**
	 * Indicates whether the status should not even bother to check whether it
	 * does/does not apply. Rule of thumb: this is true for buffs
	 */
	public boolean non_resistance;
	public boolean is_zero_hp;
	public boolean is_cant_evade;
	public boolean is_slip_damage;
	public boolean is_cant_see;
	/**
	 * percentage of life to subtract each turn
	 */
	public float slip_damage_percentage;
	/**
	 * Value of life to subtract each turn
	 */
	public int slip_damage_value;
	public float ACC_Modifier;
	public float POW_Modifier;
	public float SKL_Modifier;
	public float MND_Modifier;
	public float MOV_Modifier;
	public float VSN_Modifier;
	public float SPD_Modifier;
	public float ATK_Modifier;
	public float DEF_Modifier;
	public int auto_release_time;
	public float auto_release_prob;
	public float shock_release_prob;

	/**
	 * Turn is a private property of statuses. It indicates how long the battler
	 * has held this state. This distinction is used with other properties to
	 * influence the release time of the state. In a states construction, this
	 * value should be set to zero.
	 */
	private int turn;

	/**
	 * returns whether or not the status has run its course and should be auto
	 * released
	 * 
	 * @param rand
	 * @return boolean
	 */
	public boolean auto_release(Random rand) {
		turn++;
		if (turn >= auto_release_time && auto_release_time != 0) {
			turn = 0;
			double d = rand.nextDouble();
			if (d > auto_release_prob)
				return false;
			else
				return true;
		}
		return false;
	}

	/**
	 * returns whether or not the status should release due to shock
	 */
	public boolean shock_release(Random rand) {
		double d = rand.nextDouble();
		if (d > shock_release_prob)
			return false;
		else
			return true;
	}

	public Status() {
		name = "NULL";
		description = "???";
		anim_id = 0;
		disable_attack = false;
		disable_magic = false;
		disable_skills = false;
		disable_move = false;
		always_target_allies = false;
		always_target_enemies = false;
		cannot_act = false;
		non_resistance = false;
		is_zero_hp = false;
		is_slip_damage = false;
		is_cant_see = false;
		is_cant_evade = false;
		slip_damage_percentage = 0.0f;
		slip_damage_value = 0;
		ACC_Modifier = 1.0f;
		POW_Modifier = 1.0f;
		SKL_Modifier = 1.0f;
		MND_Modifier = 1.0f;
		MOV_Modifier = 1.0f;
		VSN_Modifier = 1.0f;
		SPD_Modifier = 1.0f;
		ATK_Modifier = 1.0f;
		DEF_Modifier = 1.0f;
		auto_release_time = 3;
		auto_release_prob = 1.0f;
		shock_release_prob = 1.0f;
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Status").attribute("id", this.id);
		xml.element("name").text(this.name).pop();
		xml.element("description").text(this.description).pop();
		xml.element("anim_id").text(anim_id).pop();
		xml.element("disable_magic").text(disable_magic).pop();
		xml.element("disable_skills").text(disable_skills).pop();
		xml.element("disable_attack").text(disable_attack).pop();
		xml.element("disable_move").text(disable_move).pop();
		xml.element("always_target_enemies").text(always_target_enemies).pop();
		xml.element("always_target_allies").text(always_target_allies).pop();
		xml.element("cannot_act").text(cannot_act).pop();
		xml.element("non_resistance").text(non_resistance).pop();
		xml.element("is_zero_hp").text(is_zero_hp).pop();
		xml.element("is_cant_evade").text(is_cant_evade).pop();
		xml.element("is_slip_damage").text(is_slip_damage).pop();
		xml.element("is_cant_see").text(is_cant_see).pop();
		xml.element("slip_damage_percentage").text(slip_damage_percentage)
				.pop();
		xml.element("slip_damage_value").text(slip_damage_value).pop();
		xml.element("ACC_Modifier").text(ACC_Modifier).pop();
		xml.element("POW_Modifier").text(POW_Modifier).pop();
		xml.element("SKL_Modifier").text(SKL_Modifier).pop();
		xml.element("MND_Modifier").text(MND_Modifier).pop();
		xml.element("MOV_Modifier").text(MOV_Modifier).pop();
		xml.element("VSN_Modifier").text(VSN_Modifier).pop();
		xml.element("SPD_Modifier").text(SPD_Modifier).pop();
		xml.element("ATK_Modifier").text(ATK_Modifier).pop();
		xml.element("DEF_Modifier").text(DEF_Modifier).pop();
		xml.element("auto_release_time").text(auto_release_time).pop();
		xml.element("auto_release_prob").text(auto_release_prob).pop();
		xml.element("shock_release_prob").text(shock_release_prob).pop();
		xml.pop();
	}

	@Override
	public Status xmlRead(Element element) {
		Status status = new Status();
		status.id = element.getIntAttribute("id");
		status.name = element.get("name");
		status.description = element.get("description", "???");
		status.anim_id = element.getInt("anim_id");
		status.disable_magic = element.getBoolean("disable_magic");
		status.disable_attack = element.getBoolean("disable_attack");
		status.disable_skills = element.getBoolean("disable_skills");
		status.disable_move = element.getBoolean("disable_move");
		status.always_target_allies = element
				.getBoolean("always_target_allies");
		status.always_target_enemies = element
				.getBoolean("always_target_enemies");
		status.cannot_act = element.getBoolean("cannot_act");
		status.non_resistance = element.getBoolean("non_resistance");
		status.is_cant_evade = element.getBoolean("is_cant_evade");
		status.is_cant_see = element.getBoolean("is_cant_see");
		status.is_slip_damage = element.getBoolean("is_slip_damage");
		status.is_zero_hp = element.getBoolean("is_zero_hp");
		status.slip_damage_percentage = element
				.getFloat("slip_damage_percentage");
		status.slip_damage_value = element.getInt("slip_damage_value");
		status.auto_release_time = element.getInt("auto_release_time");
		status.auto_release_prob = element.getFloat("auto_release_prob");
		status.shock_release_prob = element.getFloat("shock_release_prob");
		status.ACC_Modifier = element.getFloat("ACC_Modifier");
		status.POW_Modifier = element.getFloat("POW_Modifier");
		status.SKL_Modifier = element.getFloat("SKL_Modifier");
		status.MND_Modifier = element.getFloat("MND_Modifier");
		status.VSN_Modifier = element.getFloat("VSN_Modifier");
		status.MOV_Modifier = element.getFloat("MOV_Modifier");
		status.SPD_Modifier = element.getFloat("SPD_Modifier");
		status.ATK_Modifier = element.getFloat("ATK_Modifier");
		status.DEF_Modifier = element.getFloat("DEF_Modifier");
		return status;
	}

	public String toString() {
		return this.name;
	}

	/**
	 * Returns true if the status is a buff
	 * 
	 * @return
	 */
	public boolean is_buff() {
		return this.non_resistance;
	}

	/**
	 * Returns true if the status is a nerf
	 * 
	 * @return
	 */
	public boolean is_nerf() {
		return !this.is_buff();
	}

}
