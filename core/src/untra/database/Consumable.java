package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class Consumable extends Item implements IRanged {

	public int HP_recovery_value;
	public int SP_recovery_value;
	public float HP_recovery_rate;
	public float SP_recovery_rate;
	public float accuracy;
	public float variance;
	protected int range;
	protected int field;
	public int anim_use_id;
	public int anim_hit_id;
	public int add_state;
	/** resets the status of the user if their current status priority is at or below sub_state */
	public int sub_state;
	public Reaction_Event reaction_event;

	public Consumable() {
		super();
		use = Item_Use.consumable;
		reaction_event = Reaction_Event.none;
		init_fields();
	}

	public Consumable(Item item) {
		id = item.id;
		name = item.name;
		price = item.price;
		description = item.description;
		use = Item_Use.consumable;
		init_fields();
	}
	
	public Consumable(Data d)
	{
		super(d);
		reaction_event = Reaction_Event.values()[d.getInt("reaction_event")];
		HP_recovery_value = d.getInt("HP_recovery_value");
		SP_recovery_value = d.getInt("SP_recovery_value");
		HP_recovery_rate = d.getFloat("HP_recovery_rate");
		SP_recovery_rate = d.getFloat("SP_recovery_rate");
		accuracy = d.getFloat("accuracy");
		variance = d.getFloat("variance");
		range = d.getInt("range");
		field = d.getInt("field");
		add_state = d.getInt("add_state");
		sub_state = d.getInt("sub_state");
		anim_hit_id = d.getInt("anim_hit_id");
		anim_use_id = d.getInt("anim_use_id");
	}

	private void init_fields() {
		accuracy = 1.0f;
		variance = 0.15f;
		range = 0;
		field = 0;
		anim_hit_id = 0;
		anim_hit_id = 0;
		add_state = 0;
		sub_state = 0;
		reaction_event = Reaction_Event.none;
	}

	public int Range() {
		return range;
	}

	public int Field() {
		return field;
	}
	
	public boolean is_healing_item()
	{
		return (HP_recovery_value >= 0);
			
	}

	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("BattleItem");
		super.xmlWrite(xml);
		xml.element("HP_Recovery_rate").text(this.HP_recovery_rate).pop();
		xml.element("SP_Recovery_rate").text(this.SP_recovery_rate).pop();
		xml.element("HP_Recovery_value").text(this.HP_recovery_value).pop();
		xml.element("SP_Recovery_value").text(this.SP_recovery_value).pop();
		xml.element("accuracy").text(this.accuracy).pop();
		xml.element("variance").text(this.variance).pop();
		xml.element("range").text(this.range).pop();
		xml.element("field").text(this.field).pop();
		xml.element("anim_use_id").text(this.anim_use_id).pop();
		xml.element("anim_hit_id").text(this.anim_hit_id).pop();
		xml.pop();

	}

	/**
	 * Returns its own instance
	 */
	public Consumable xmlRead(Element element) {
		Item i = super.xmlRead(element.getChildByName("Item"));
		Consumable item = new Consumable(i);
		item.HP_recovery_rate = element.getFloat("HP_Recovery_rate");
		item.HP_recovery_rate = element.getFloat("SP_Recovery_rate");
		item.HP_recovery_value = element.getInt("HP_Recovery_value");
		item.HP_recovery_value = element.getInt("SP_Recovery_value");
		item.accuracy = element.getFloat("accuracy");
		item.variance = element.getFloat("variance");
		item.range = element.getInt("range");
		item.field = element.getInt("field");
		item.anim_hit_id = element.getInt("anim_hit_id");
		item.anim_use_id = element.getInt("anim_use_id");
		return item;
	}
}