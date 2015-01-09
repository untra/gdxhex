package untra.player;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.sun.corba.se.impl.interceptors.SlotTable;

import untra.database.Armor;
import untra.database.Weapon;
import untra.database.Weapon_Type;

public class EquipmentSet {

	private Actor _actor;
	private Armor _armor;
	private Weapon _lhand;
	private Weapon _rhand;
	//private Bonus _bonus;
	
	public EquipmentSet(Actor A)
	{
		_actor = A;
	}
	
	public boolean can_wield(Weapon_Type t)
	{
		return _actor.skills.can_wield(t);
	}
	
	/**
	 * attempts to equip item s into slot i.
	 * @param i item slot. 0-armor, 1-lhand, 2-rhand, 3-bonus
	 * @param s
	 * @return
	 */
	public boolean equip(int i, int s)
	{
		switch (i) {
		default:
			return false;
		}
	}
	
	public boolean dequip(int i)
	{
		if(is_empty(i))
			return false;
		switch (i) {
		default:
			return false;
		}
	}
	
	public boolean is_empty(int i)
	{
		switch (i) {
		case 0:
			return (_armor == null);
		case 1:
			return (_lhand == null);
		case 2:
			return (_rhand == null);
		//case 3:
		//	return (_armor == null);
		default:
			return false;
		}
	}
	
}
