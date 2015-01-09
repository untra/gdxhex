package untra.database;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Armor extends Item implements Idata {

	public int id;
	public String name;
	public String description;
	public Armor_Type type;
	public int DEF;
	public int POW;
	public int SKL;
	public int MND;
	public int MOV;
	public int VSN;
	public int SPD;
	public int HPP;
	public int SPP;
	public int durability_max;

	public Armor() {
		id = 0;
		name = "Rags";
		description = "Thin cotton rags help protect against a cold breeze. Sort of.";
		type = Armor_Type.light;
		DEF = 0;
		POW = 0;
		SKL = 0;
		MND = 0;
		MOV = 0;
		VSN = 0;
		SPD = 0;
		HPP = 0;
		SPP = 0;
		durability_max = 8;
	}
	
	public Armor(Data d) {
		super(d);
		type = Armor_Type.values()[d.getInt("type")];
		DEF = d.getInt("DEF");
		POW = d.getInt("POW");
		SKL = d.getInt("SKL");
		MND = d.getInt("MND");
		MOV = d.getInt("MOV");
		VSN = d.getInt("VSN");
		SPD = d.getInt("SPD");
		HPP = d.getInt("HPP");
		SPP = d.getInt("SPP");
		//durability_max = d.getInt("durability_max");
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Armor");
		xml.element("id").text(id).pop();
		xml.element("name").text(name).pop();
		xml.element("description").text(description).pop();
		xml.element("type");
		type.xmlWrite(xml);
		xml.pop();
		xml.element("DEF").text(DEF).pop();
		xml.element("POW").text(POW).pop();
		xml.element("SKL").text(SKL).pop();
		xml.element("MND").text(POW).pop();
		xml.element("SPD").text(SPD).pop();
		xml.element("MOV").text(MOV).pop();
		xml.element("VSN").text(VSN).pop();
		xml.element("HPP").text(HPP).pop();
		xml.element("SPP").text(SPP).pop();
		xml.pop();
	}

	@Override
	public Armor xmlRead(Element element) {
		Armor armor = new Armor();
		armor.id = element.getInt("id");
		armor.name = element.get("name");
		armor.description = element.get("description");
		armor.type = armor.type.xmlRead(element.getChildByName("type"));
		armor.DEF = element.getInt("DEF");
		armor.POW = element.getInt("POW");
		armor.SKL = element.getInt("SKL");
		armor.MND = element.getInt("MND");
		armor.SPD = element.getInt("SPD");
		armor.MOV = element.getInt("MOV");
		armor.VSN = element.getInt("VSN");
		armor.HPP = element.getInt("HPP");
		armor.SPP = element.getInt("SPP");
		return armor;
	}

	public String toString() {
		return this.name;
	}

}
