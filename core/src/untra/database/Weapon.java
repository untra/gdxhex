package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Weapon extends DatabaseObject implements IXml<Weapon>, Idata {

	private static final int DEFAULT_USE_ANIM = 0;
	private static final int DEFAULT_HIT_ANIM = 0;

	public int id;
	public String name;
	public String description;
	public Weapon_Type type;
	public int ATK;
	public int POW;
	public int SKL;
	public int MND;
	public int MOV;
	public int VSN;
	public int SPD;
	public int range;
	public boolean line;
	public int use_anim_id;
	public int hit_anim_id;
	public int durability_max;
	//public boolean two_handed;
	

	public Skill_Type damage_type() {
		switch (type) {
		case Lances:
			return Skill_Type.SKL;
			
		case Staves:
			return Skill_Type.MND;

		case Wands:
			return Skill_Type.MND;

		case Guns:
			return Skill_Type.SKL;

		case Bows:
			return Skill_Type.SKL;

		case Maces:
			return Skill_Type.SKL; 
			
		default:
			return Skill_Type.POW;

		}
	}
	
	

	public Weapon() {
		id = 0;
		name = "unarmed";
		description = "Bare Fists, slightly used";
		type = Weapon_Type.none;
		ATK = 0;
		POW = 0;
		SKL = 0;
		MND = 0;
		MOV = 0;
		VSN = 0;
		SPD = 0;
		range = 1;
		line = false;
		use_anim_id = DEFAULT_USE_ANIM;
		hit_anim_id = DEFAULT_HIT_ANIM;
		sanitize();
	}
	
	public boolean two_handed()
	{
		switch (this.type) {
		case Short_Blades:
			return false;
		case Staves:
			return false;
		case Claws:
			return false;
		case Wands:
			return false;
		case Guns:
			return false;
		case Maces:
			return false;
		case Knives:
			return false;
		default:
			return true;
		}
	}
	
	
	public Weapon(Data d) {
		super(d);
		type = Weapon_Type.values()[d.getInt("type")];
		ATK = d.getInt("ATK");
		POW = d.getInt("POW");
		SKL = d.getInt("SKL");
		MND = d.getInt("MND");
		MOV = d.getInt("MOV");
		VSN = d.getInt("VSN");
		SPD = d.getInt("SPD");
		range = d.getInt("range");
		line =  d.getBool("line");
		use_anim_id = d.getInt("use_anim_id");
		hit_anim_id = d.getInt("hit_anim_id");
		//durability_max = d.getInt("durability_max");
		//two_handed = d.getBool("two_handed");
		sanitize();
	}
	
	private void sanitize()
	{
		durability_max = Math.max(Math.min(durability_max, 160), 20);
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Weapon");
		xml.element("id").text(id).pop();
		xml.element("name").text(name).pop();
		xml.element("description").text(description).pop();
		xml.element("type");
		type.xmlWrite(xml);
		xml.pop();
		xml.element("ATK").text(ATK).pop();
		xml.element("POW").text(POW).pop();
		xml.element("SKL").text(SKL).pop();
		xml.element("MND").text(POW).pop();
		xml.element("SPD").text(SPD).pop();
		xml.element("MOV").text(MOV).pop();
		xml.element("VSN").text(VSN).pop();
		xml.element("range").text(range).pop();
		xml.element("line").text(line).pop();
		xml.element("use_anim_id").text(use_anim_id).pop();
		xml.element("hit_anim_id").text(hit_anim_id).pop();
		xml.pop();
	}

	@Override
	public Weapon xmlRead(Element element) {
		Weapon weapon = new Weapon();
		weapon.id = element.getInt("id");
		weapon.name = element.get("name");
		weapon.description = element.get("description");
		weapon.type = weapon.type.xmlRead(element.getChildByName("type"));
		weapon.ATK = element.getInt("ATK");
		weapon.POW = element.getInt("POW");
		weapon.SKL = element.getInt("SKL");
		weapon.MND = element.getInt("MND");
		weapon.SPD = element.getInt("SPD");
		weapon.MOV = element.getInt("MOV");
		weapon.VSN = element.getInt("VSN");
		weapon.line = element.getBoolean("line");
		weapon.range = element.getInt("range");
		weapon.use_anim_id = element.getInt("use_anim_id");
		weapon.hit_anim_id = element.getInt("hit_anim_id");
		return weapon;
	}

	public String toString() {
		return this.name;
	}
}
