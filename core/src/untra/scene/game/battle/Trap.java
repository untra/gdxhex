package untra.scene.game.battle;

import java.util.LinkedList;

import untra.scene.game.GameScene;
import untra.scene.game.map.HexDirectional;
import untra.scene.game.map.HexTable;
import untra.scene.game.map.HexTile;

/**
 * A trap triggers a small attack when stepped across, and does more damage and
 * is destroyed when a battler ends their turn on it.
 * 
 * @author samuel
 * 
 */
public class Trap {
	public boolean playerOwned = false;
	public boolean aware = false;
	public Trap_Type type = Trap_Type.Blade_Trap;
	public HexTile pos;

	/**
	 * small trigger; the battler has stepped across this, so it does some
	 * damage
	 * 
	 * @param T
	 */
	public void small_trigger(Battler T) {
		switch (type) {
		case Alarm_Trap: {
			LinkedList<Battler> bonus;
			if (playerOwned)
				bonus = HexTable.battlers_distance_from(GameScene.Enemies,
						this.pos, 3);
			else
				bonus = HexTable.battlers_distance_from(GameScene.Players,
						this.pos, 3);
			for (Battler B : bonus) {
				B.turn_increment(200);
			}
		}
		default:
			T.deal_damage(T.properties.LEVEL, HexDirectional.None());
			// TODO: play animation on target
			break;
		}
	}

	/**
	 * big trigger; the battler has ended their movement on this tile, so their
	 * turn ends and they take damage
	 * 
	 * @param T
	 */
	public void big_trigger(Battler T) {
		T.has_acted = true;
		switch (type) {
		case Alarm_Trap: {
			LinkedList<Battler> bonus;
			if (playerOwned)
				bonus = HexTable.battlers_distance_from(GameScene.Enemies,
						this.pos, 5);
			else
				bonus = HexTable.battlers_distance_from(GameScene.Players,
						this.pos, 5);
			for (Battler B : bonus) {
				B.turn_increment(300);
				// TODO: all opponent battlers recieve [!] animation
				B.turn_to(this.pos);

			}
		}
		default:
			T.deal_damage(T.properties.LEVEL, HexDirectional.None());
			// TODO: play animation on target
			break;
		}
	}
}
