package untra.player;

import org.omg.CORBA.PUBLIC_MEMBER;

import untra.database.Consumable;
import untra.database.Item;
import untra.database.Item_Use;
import untra.database.PassiveSkill;
import untra.database.Reaction_Event;

public class ItemSet {

	private Actor _actor;
	private Item[] _items;
	
	public ItemSet(Actor A)
	{
		_actor = A;
		_items = new Item[6];
	}
	
	public Item item(int i)
	{
		if(i < 0 || i > 5)
			return null;
		else return _items[i];
	}
	
	/**
	 * Returns the index value of the first consumable item that will be consumed at critical hp. if not found, returns -1
	 * @return
	 */
	public int critical_consumable()
	{
		for(int j = 0; j < 6 ; j++ )
		{
			Item i = _items[j];
			if(i == null) 
				continue;
			if(i instanceof Consumable)
			{
				Consumable c = (Consumable) i;
				if(c.reaction_event == Reaction_Event.critical)
					return j;
			}
			else continue;
		}
		return -1;
	}
	
	/**
	 * Returns and removes the ith item in the itemset
	 * @param j
	 */
	public Item get(int i)
	{
		if(i < 0 || i > 5)
			return null;
		Item item = _items[i];
		_items[i] = null;
		return item;
	}
}
