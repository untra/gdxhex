package untra.player;

import java.util.ArrayList;

import untra.database.Item;

public class Party {

	// / <summary>
	// / Members of the Party
	// / </summary>
	public ArrayList<Actor> Members = new ArrayList<Actor>();
	// / <summary>
	// / Money possessed by the player
	// / </summary>
	public int Money = 0;
	// / <summary>
	// / Items possessed by the player. Represented as a dictionary of item_id,
	// quantity
	// / </summary>
	public ArrayList<Item> Items = new ArrayList<Item>();

	// / <summary>
	// / Returns true or false indicating if the item at the specified id is
	// useable at that moment.
	// / </summary>
	// / <param name="id">id of item</param>
	// / <returns>boolean</returns>
	/*
	 * public static boolean item_is_useable(short id) { if (Items.size() <= id)
	 * return false; Item_Use use = Database.Items[id].Use; if (use ==
	 * Item_Use.always) return true; if (use == Item_Use.never) return false;
	 * boolean menu_only; if (use == Item_Use.menu_only) menu_only = true; else
	 * menu_only = false; return Base.In_Battle ^ menu_only; }
	 */
}
