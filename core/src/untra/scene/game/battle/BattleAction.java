package untra.scene.game.battle;

import untra.scene.game.map.HexTile;
import untra.database.Elemental;

public class BattleAction {
	public enum Kind {
		none, move, attack, special, item, defend;
	}

	/**
	 * The score assigned to this action by the battler. This is used by the AI
	 * to judge moves based on their quality
	 */
	public int score;
	public Kind kind;
	/**
	 * Specifies the id of skill or item used. This must be specified when using
	 * a skill to get correct damage
	 */
	public int id;
	public HexTile target_tile;
	/**
	 * If the action cannot miss. Rule of thumb, this is true for buffs and
	 * healing
	 */
	public boolean forcing;
	public Elemental elemental;

	public BattleAction(Kind k, boolean AI) {
		kind = k;
		score = 0;
		id = 0;
		target_tile = null;
		forcing = false;
		elemental = Elemental.none;
	}

	public BattleAction(Kind k) {
		this(k, false);
	}

	public BattleAction() {
		this(Kind.none, false);
	}

	public void clear() {
		this.score = 0;
		this.kind = Kind.none;
		this.id = 0;
		this.target_tile = null;
		this.forcing = false;
		this.elemental = Elemental.none;

	}

	/**
	 * Returns a clone of the battle action;
	 */
	public BattleAction clone() {
		BattleAction B = new BattleAction(kind, false);
		B.score = this.score;
		B.id = this.id;
		B.target_tile = this.target_tile;
		B.forcing = this.forcing;
		B.elemental = this.elemental;
		return B;
	}
}
