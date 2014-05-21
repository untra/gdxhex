package untra.scene.game.battle;

import java.util.Random;

import untra.database.BattleItem;
import untra.database.Database;
import untra.database.Skill_Type;

/**
 * Performs Math Operations during Battle. Consists Mostly of Battle Formulas
 * and such
 * 
 * @author samuel
 * 
 */
public class BattleMath {
	/**
	 * Returns a value between 0.0 and 1.0 representing the chance an attack
	 * lands from Attacker to Target AI can somewhat see this value by default
	 * 
	 * @param A
	 *            Attacking battler
	 * @param T
	 *            Target Battler
	 * @return
	 */
	public static float Accuracy(Battler A, Battler T) {
		if (A.current_action.forcing == true)
			return 1.0f;
		// int Tspd, Aspd, Dspd;
		// float Aacc;
		// Tspd = T.properties.getSPD();
		// Aspd = A.properties.getSPD();
		// Dspd = Math.max(Tspd - Aspd, 0);
		// Aacc = A.properties.getACC();
		// Aacc *= (float) ((1000.0f - Tspd) / (1000.0f));
		// Aacc *= (float) ((255.0f - Dspd) / (255.0f));
		float Aacc = A.properties.getACC();
		Aacc -= T.properties.getEVA();
		if (T.properties.status_cannot_evade())
			Aacc += 0.25f;
		else if (T.use_evade())
			Aacc -= 0.25f;
		return Math.min(Math.max(Aacc, 0.0f), 1.0f);
	}

	// / <summary>
	// / Returns true if an attack between Attacker and Target doublestrikes
	// / </summary>
	// / <param name="A">Attacking Battler</param>
	// / <param name="T">Target Battler</param>
	// / <param name="rand">Random Varible</param>
	// / <returns></returns>
	public static boolean is_doublestrike(Battler A, Battler T, Random rand) {
		int Diff = Math.max(A.properties.SKL() - T.properties.SKL(), 0);
		if (rand.nextDouble() * 1600 < A.properties.SKL())
			return true;
		else if (rand.nextDouble() * 400 < Diff)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if an attack between Attacker and Target misses
	 * 
	 * @param A
	 * @param T
	 * @param rand
	 * @return
	 */
	public static boolean is_miss(Battler A, Battler T, Random rand) {
		if (T.properties.status_cannot_evade())
			return false;
		else if (T.use_evade()) {
			T.set_use_evade(false);
			return false;
		}
		if (rand.nextDouble() > Accuracy(A, T))
			return true;
		else
			return false;
	}

	// / <summary>
	// / Returns true if an attack between Attacker and Target critical hits
	// / </summary>
	// / <param name="A">Attacking Battler</param>
	// / <param name="T">Target Battler</param>
	// / <param name="rand">Random Varible</param>
	// / <returns></returns>
	public static boolean is_critical(Battler A, Battler T, Random rand) {
		int Diff = Math.max(A.properties.MND() - T.properties.MND(), 0);
		if (rand.nextDouble() * 1200 < A.properties.MND())
			return true;
		else if (rand.nextDouble() * 300 < Diff)
			return true;
		else
			return false;
	}

	/**
	 * Returns true if an attack between Attacker and Target Knocks Back
	 * 
	 * @param A
	 * @param T
	 * @param rand
	 * @return
	 */
	public static boolean is_knockback(Battler A, Battler T, Random rand) {
		int Diff = Math.max(A.properties.POW() - T.properties.POW(), 0);
		if (rand.nextDouble() * 1400 < A.properties.POW())
			return true;
		else if (rand.nextDouble() * 350 < Diff)
			return true;
		else
			return false;
	}

	/**
	 * Returns the expected damage an attack would do, pre flag and variance
	 * 
	 * @param A
	 * @param T
	 * @param act
	 * @return
	 */
	@SuppressWarnings("unused")
	public static int damage(Battler A, Battler T, BattleAction act) {

		float power = 1.0f, damage; // Influencing factor to damage. Basic
									// attacks have power = 1.0, special attacks
									// have variable power, healing attacks have
									// negative power.
		int Af, Tf; // Variables represesenting Damage_Factor stat
		int attack = A.properties.ATK();
		int defence = T.properties.DEF();
		Skill_Type ftype = Skill_Type.POW;
		if (act.kind == BattleAction.Kind.special)
			power = Database.skills[act.id].power;
		ftype = Database.skills[act.id].type;
		// float power = 1.0f;
		if (act.kind == BattleAction.Kind.attack) {
			ftype = A.properties.getdamage_factor();
			power = ((float) attack / (float) defence);
		}
		if (ftype == Skill_Type.MND) {
			Af = A.properties.MND();
			Tf = T.properties.MND();
		} else if (ftype == Skill_Type.SKL) {
			Af = A.properties.SKL();
			Tf = T.properties.SKL();
		} else if (ftype == Skill_Type.POW) {
			Af = A.properties.POW();
			Tf = T.properties.POW();
		} else {
			Af = 0;
			Tf = 0;
		}
		// Main Formula
		damage = Math.max(Math.min(power, 8.0f), -4.0f); //
		// if healing attack, power will be negative, and forcing set to true
		damage *= Af;
		if (!act.forcing) {
			damage -= defence;
			damage = Math.max(1.0f, damage);
		}

		return (int) damage;
	}

	/**
	 * Battle damage as a function of item use
	 * 
	 * @param T
	 * @param item
	 * @return
	 */
	public static int damage(Battler T, BattleItem item) {
		return ((item.HP_Recovery_value) + (int) (item.HP_Recovery_rate * (T.properties
				.MAX_HP()))) * -1;
	}

	/**
	 * SP restoration as a function of item use
	 * 
	 * @param T
	 * @param item
	 * @return
	 */
	public static int sp_restore(Battler T, BattleItem item) {
		return (item.SP_Recovery_value + (int) (T.properties.MAX_SP() * item.SP_Recovery_rate));
	}

	/**
	 * Returns by reference true damage, corrected for variance
	 * 
	 * @param damage
	 * @param variance
	 * @param rand
	 * @return
	 */
	public static int damage_corrected(int damage, float variance, Random rand) {
		float temp = (float) damage;
		temp *= 1.0f + ((float) rand.nextDouble() - 0.5f) * 2.0f * variance;
		// temp *= critical ? 1.5f : 1.0f;
		damage = (int) temp;
		damage = Math.max(1, damage);
		return damage;
	}
}
