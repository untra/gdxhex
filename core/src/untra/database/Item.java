package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Item extends DatabaseObject implements IXml<Item>, Idata {
	public int price;
	// public Enums.Elements Element;
	public Item_Use use;

	// public int HP_Recovery_value;
	// public float HP_Recovery_rate;
	// public int SP_Recovery_value;
	// public float SP_Recovery_rate;
	// public float Accuracy;
	// public float Variance;
	// public Stats parameter;
	// public int parameter_increase;
	// protected int range;
	// protected int field;
	// public int Anim_use_id;
	// public int Anim_hit_id;
	// public int[] plus_states;
	// public int[] minus_states;

	public Item() {
		id = 0;
		name = "NULL";
		description = "???";
		price = 1;
		use = Item_Use.bonus;

		// HP_Recovery_rate = 0.0f;
		// HP_Recovery_value = 0;
		// SP_Recovery_rate = 0.0f;
		// SP_Recovery_value = 0;
		// Accuracy = 1.0f;
		// Variance = 1.0f;
		// parameter = Stats.none;
		// range = 0;
		// field = 0;
		// Anim_use_id = 0;
		// Anim_hit_id = 0;
		// plus_states = new int[1];
		// minus_states = new int[1];
		// plus_states[0] = 0;
		// minus_states[0] = 0;
	}


	public Item(Data d) {
		super(d);
		price = d.getInt("price");
		use = Item_Use.values()[d.getInt("use")];
//		prereq = d.getInt("prereq", 0);
//		level_cost = d.getInt("level_cost");
	}
	
	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Item").attribute("id", this.id);
		xml.element("name").text(this.name).pop();
		xml.element("description").text(this.description).pop();
		xml.element("price").text(price).pop();
		use.xmlWrite(xml);
		xml.pop();
	}

	/**
	 * public float ACC_Modifier; public float POW_Modifier; public float
	 * SKL_Modifier; public float MND_Modifier; public float MOV_Modifier;
	 * public float VSN_Modifier; public float SPD_Modifier; public float
	 * ATK_Modifier; public float DEF_Modifier;
	 */

	@Override
	public Item xmlRead(Element element) {
		Item item = this;
		item.id = element.getIntAttribute("id");
		item.name = element.get("name");
		item.description = element.get("description");
		item.price = element.getInt("price");
		item.use = use.xmlRead(element);
		return item;
	}

	// List<Status> plus_state_set;// = new List<Status>();
	// List<Status> minus_state_set;// = new List<Status>();

	public String toString() {
		return this.name;
	}
}
