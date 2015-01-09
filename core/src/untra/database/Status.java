package untra.database;

import java.util.Random;

public enum Status {
	_, dead, poison, paralyze, stunned, confused, charmed, berserk, burned, sleep, blind, immobilize, disabled, silenced, addled, frozen, invisible, focused;
	public int turn = 0;
	private Status()
	{
		turn = 0;
	}
	
	protected int priority()
	{
		switch (this) {
		case dead:
			return 255;
		case _:
			return 0;
		case stunned:
			return 2;
		case charmed:
			return 3;
		case confused:
			return 3;
		case berserk:
			return 3;
		case blind:
			return 3;
		case silenced:
			return 3;
		case immobilize:
			return 3;
		case disabled:
			return 3;
		case addled:
			return 3;
		
		default:
			if(is_buff())
				return 1;
			else return 5;
		}
	}
	
	public Status apply(Status s)
	{
		int p = this.priority();
		int x = s.priority();
		if(x > p)
			return s;
		else return this;
		//TODO: percolate status effect applied.
	}
	
	public String description()
	{
		switch (this) {
		default:
			return "";
		}
	}
	
	public int anim_id()
	{
		switch (this) {
		default:
			return 0;
		}
	}
	
	public boolean disable_magic()
	{
		if(this == dead) return true;
		if(this == frozen) return true;
		if(this == paralyze) return true;
		if(this == sleep) return true;
		
		if(this == silenced) return true;
		else return false;
	}
	public boolean disable_special()
	{
		if(this == dead) return true;
		if(this == frozen) return true;
		if(this == paralyze) return true;
		if(this == sleep) return true;
		
		if(this == addled) return true;
		else return false;
	}
	
	public boolean cannot_act()
	{
		if(this == dead) return true;
		if(this == frozen) return true;
		if(this == paralyze) return true;
		if(this == sleep) return true;
		
		if(this == disabled) return true;
		else return false;
	}
	
	public boolean disable_move()
	{
		if(this == dead) return true;
		if(this == frozen) return true;
		if(this == paralyze) return true;
		if(this == sleep) return true;
		
		if(this == immobilize) return true;
		else return false;
	}
	
	public boolean cannot_evade()
	{
		if(this.disable_move()) return true;
		if(this == blind) return true;
		if(this == burned) return true;
		else return false;
	}
	/**
	 * If true, AI takes control of character and compels to basic or skill
	 * attack enemies exclusively If this and always_target_allies, character
	 * acts confused and will do either
	 */
	public boolean always_target_enemies()
	{
		if(this == berserk) return true;
		else return false;
	}
	/**
	 * If true, AI takes control of character and compels to basic or skill
	 * attack allies exclusively. If this and always_target_enemies, character
	 * acts confused and will do either
	 */
	public boolean always_target_allies()
	{
		if(this == charmed) return true;
		else return false;
	}
	/**
	 * If true, AI takes control of character and makes a random move.
	 */
	public boolean random_targeting()
	{
		if(this == confused) return true;
		else return false;
	}
	
	/**
	 * Indicates whether the status should not even bother to check whether it
	 * does/does not apply. Rule of thumb: this is true for buffs
	 */
	public boolean non_resistance()
	{
		switch (this) {
		case focused:
			return true;
		case invisible:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Indicates whether the actor can evade attacks while this status is in effect
	 * 
	 */
	public boolean is_cant_evade()
	{
		switch (this) {
		case dead:
			return true;
		case paralyze:
			return true;
		case stunned:
			return true;
		case blind:
			return true;
		case immobilize:
			return true;
		case addled:
			return true;
		case frozen:
			return true;
		default:
			return false;
		}
	}
	/**
	 * Indicates whether the actor can evade attacks while this status is in effect
	 */
	public boolean is_slip_damage()
	{
		switch (this) {
		case poison:
			return true;
		case burned:
			return true;
		
		default:
			return false;
		}
	}
	
	public void update()
	{
		turn++;
	}
	
	/**
	 * returns whether or not the status has run its course and should be auto
	 * released
	 * 
	 * @param rand
	 * @return boolean
	 */
	public boolean auto_release(Random rand) {
		if (turn >= auto_release_time() && auto_release_time() != 0) {
			turn = 0;
			double d = rand.nextDouble();
			if (d > auto_release_prob())
				return false;
			else
				return true;
		}
		return false;
	}
	
	public int auto_release_time()
	{
		switch (this) {
		case dead:
			return Integer.MAX_VALUE;
		default:
			return 3;
		}
	}
	
	public float auto_release_prob()
	{
		switch (this) {
		case sleep:
			return 0.25f;
		default:
			return 0.0f;
		}
	}
	
	public float shock_release_prob()
	{
		switch (this) {
		case charmed:
			return 1.0f;
		case confused:
			return 1.0f;
		case berserk:
			return 1.0f;
		case sleep:
			return 0.25f;
		default:
			return 0.0f;
		}
	}
	
	/**
	 * returns whether or not the status should release due to shock
	 */
	public boolean shock_release(Random rand) {
		double d = rand.nextDouble();
		if (d > shock_release_prob())
			return false;
		else
			return true;
	}
	
	/**
	 * Returns true if the status is a buff
	 * 
	 * @return
	 */
	public boolean is_buff() {
		return this.non_resistance();
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
