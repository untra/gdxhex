package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class BattleItem extends Item implements IRanged {

	public int HP_Recovery_value;
	public int SP_Recovery_value;
	public float HP_Recovery_rate;
	public float SP_Recovery_rate;
	public float accuracy;
	public float variance;
	protected int range;
	protected int field;
	public int anim_use_id;
	public int anim_hit_id;
	public int[] plus_states;
	public int[] minus_states;

	public BattleItem() {
		super();
		scope = Skill_Scope.attack;
		use = Item_Use.battle_only;
		init_fields();
	}

	public BattleItem(Item item) {
		id = item.id;
		name = item.name;
		price = item.price;
		description = item.description;
		scope = Skill_Scope.attack;
		use = Item_Use.battle_only;
		init_fields();
	}

	private void init_fields() {
		accuracy = 1.0f;
		variance = 0.15f;
		range = 0;
		field = 0;
		anim_hit_id = 0;
		anim_hit_id = 0;
		plus_states = new int[0];
		minus_states = new int[0];
	}

	public int Range() {
		return range;
	}

	public int Field() {
		return field;
	}

	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("BattleItem");
		super.xmlWrite(xml);
		xml.element("HP_Recovery_rate").text(this.HP_Recovery_rate).pop();
		xml.element("SP_Recovery_rate").text(this.SP_Recovery_rate).pop();
		xml.element("HP_Recovery_value").text(this.HP_Recovery_value).pop();
		xml.element("SP_Recovery_value").text(this.SP_Recovery_value).pop();
		xml.element("accuracy").text(this.accuracy).pop();
		xml.element("variance").text(this.variance).pop();
		xml.element("range").text(this.range).pop();
		xml.element("field").text(this.field).pop();
		xml.element("anim_use_id").text(this.anim_use_id).pop();
		xml.element("anim_hit_id").text(this.anim_hit_id).pop();
		xml.element("plus_states").attribute("length", plus_states.length);
		for (Integer i : plus_states) {
			xml.element("state").text(i).pop();
		}
		xml.pop();
		xml.element("minus_states").attribute("length", minus_states.length);
		for (Integer i : minus_states) {
			xml.element("state").text(i).pop();
		}
		xml.pop();
		xml.pop();

	}

	/**
	 * Returns its own instance
	 */
	@SuppressWarnings("unused")
	public BattleItem xmlRead(Element element) {
		Item i = super.xmlRead(element.getChildByName("Item"));
		BattleItem item = new BattleItem(i);
		item.HP_Recovery_rate = element.getFloat("HP_Recovery_rate");
		item.HP_Recovery_rate = element.getFloat("SP_Recovery_rate");
		item.HP_Recovery_value = element.getInt("HP_Recovery_value");
		item.HP_Recovery_value = element.getInt("SP_Recovery_value");
		item.accuracy = element.getFloat("accuracy");
		item.variance = element.getFloat("variance");
		item.range = element.getInt("range");
		item.field = element.getInt("field");
		item.anim_hit_id = element.getInt("anim_hit_id");
		item.anim_use_id = element.getInt("anim_use_id");
		item.plus_states = new int[element.getChildByName("plus_states")
				.getIntAttribute("length")];
		int x = 0;
		for (Element e : element.getChildByName("plus_states")
				.getChildrenByName("state")) {
			plus_states[x] = element.getInt("state");
			x++;
		}
		item.minus_states = new int[element.getChildByName("minus_states")
				.getIntAttribute("length")];
		x = 0;
		for (Element e : element.getChildByName("minus_states")
				.getChildrenByName("state")) {
			minus_states[x] = element.getInt("state");
			x++;
		}
		return item;
	}
}