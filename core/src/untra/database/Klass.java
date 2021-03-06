package untra.database;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.driver.IXml;

public class Klass extends DatabaseObject implements IXml<Klass>, Idata {
	private static final char DEFAULT_VALUE = 'D';
	private static final int[] GAINS_DEFAULT = {1,1,1,1,1,3,6,10,15,21,28,36,45,55,56,57};

	// public int id;
	public char HPv; // 0
	public char SPv; // 1
	public char POWv; // 2
	public char SKLv; // 3
	public char MNDv; // 4
	public char MOVv; // 5
	public char VSNv; // 6
	public char SPDv; // 7
	public char ATKv; // 8
	public char DEFv; // 9
	//public Race race;
	public int[] skills;
	//public int skl_0, skl_1, skl_2, skl_3, skl_4,skl_5,skl_6,skl_7,skl_8,skl_9,skl_a,skl_b,skl_c,skl_d;
	//these are the levels that skills 14 and 15 are learned at
	
	public int sp1, sp2;

	public Klass() {
		id = 0;
		name = "NULL";
		HPv = DEFAULT_VALUE;
		SPv = DEFAULT_VALUE;
		SKLv = DEFAULT_VALUE;
		MNDv = DEFAULT_VALUE;
		POWv = DEFAULT_VALUE;
		SPDv = DEFAULT_VALUE;
		VSNv = DEFAULT_VALUE;
		MOVv = DEFAULT_VALUE;
		ATKv = DEFAULT_VALUE;
		DEFv = DEFAULT_VALUE;
//		wpn_A = Weapon_Type.none;
//		wpn_B = Weapon_Type.none;
//		race = Race.Human;
		skills = new int[16];
		sp1 = 60;
		sp2 = 60;
	}

	public static Klass K_WARRIOR()
	{
		Klass klass = new Klass();
		klass.id = 11;
		klass.name = "Warrior";
		klass.HPv = 'C';
		klass.SPv = 'D';
		klass.POWv = 'A';
		klass.SKLv = 'F';
		klass.MNDv = 'D';		
		klass.SPDv = 'D';
		klass.VSNv = 'D';
		klass.MOVv = 'D';
		klass.ATKv = 'B';
		klass.DEFv = 'B';
//		klass.race = Race.Human;
		return klass;
	}
	
	public static Klass K_ARCHER()
	{
		Klass klass = new Klass();
		klass.id = 12;
		klass.name = "Archer";
		klass.HPv = 'D';
		klass.SPv = 'C';
		klass.POWv = 'D';
		klass.SKLv = 'A';
		klass.MNDv = 'D';
		klass.MOVv = 'D';
		klass.VSNv = 'C';
		klass.SPDv = 'B';
		klass.ATKv = 'C';
		klass.DEFv = 'C';
//		klass.race = Race.Human;
		return klass;
	}
	
	public static Klass K_THIEF()
	{
		Klass klass = new Klass();
		klass.id = 13;
		klass.name = "Thief";
		klass.HPv = 'D';
		klass.SPv = 'C';
		klass.POWv = 'C';
		klass.SKLv = 'C';
		klass.MNDv = 'C';
		klass.MOVv = 'C';
		klass.VSNv = 'B';
		klass.SPDv = 'B';
		klass.ATKv = 'D';
		klass.DEFv = 'D';
//		klass.race = Race.Human;
		return klass;
	}
	
	public static Klass K_MONK()
	{
		Klass klass = new Klass();
		klass.id = 14;
		klass.name = "Monk";
		klass.HPv = 'A';
		klass.SPv = 'D';
		klass.POWv = 'C';
		klass.SKLv = 'C';
		klass.MNDv = 'C';
		klass.MOVv = 'C';
		klass.VSNv = 'D';
		klass.SPDv = 'C';
		klass.ATKv = 'C';
		klass.DEFv = 'C';
//		klass.race = Race.Human;
		return klass;
	}
	
	public static Klass K_SCHOLAR()
	{
		Klass klass = new Klass();
		klass.id = 15;
		klass.name = "Scholar";
		klass.HPv = 'A';
		klass.SPv = 'B';
		klass.POWv = 'C';
		klass.SKLv = 'C';
		klass.MNDv = 'B';
		klass.MOVv = 'D';
		klass.VSNv = 'D';
		klass.SPDv = 'D';
		klass.ATKv = 'D';
		klass.DEFv = 'D';
//		klass.race = Race.Human;
		return klass;
	}
	
	public static Klass K_MAGICIAN()
	{
		Klass klass = new Klass();
		klass.id = 16;
		klass.name = "Magician";
		klass.HPv = 'C';
		klass.SPv = 'A';
		klass.POWv = 'D';
		klass.SKLv = 'C';
		klass.MNDv = 'A';
		klass.MOVv = 'D';
		klass.VSNv = 'D';
		klass.SPDv = 'C';
		klass.ATKv = 'D';
		klass.DEFv = 'D';
//		klass.race = Race.Human;
		return klass;
	}
	
	/**
	 * returns a positive integer designating an increase in a particular stat
	 * @param b
	 * @param rand
	 * @return
	 */
	public int advance(int b, Random rand) {
		int table = -1;
		char check = 'F';
		switch (b) {
		case 0:
			check = HPv;
			table = 0;
			break;
		case 1:
			check = SPv;
			table = 0;
			break;
		case 2:
			check = POWv;
			table = 1;
			break;
		case 3:
			check = SKLv;
			table = 1;
			break;
		case 4:
			check = MNDv;
			table = 1;
			break;
		case 7:
			check = SPDv;
			table = 1;
			break;
		default:
			break;
		}
		switch (table) {
		case 0: {
			switch (check) {
			case 'A':
				return (byte) rand.nextInt(5) + 4;// 6.5
			case 'B':
				return (byte) rand.nextInt(3) + 4;// 5.5
			case 'C':
				return (byte) rand.nextInt(3) + 3;// 4.5
			case 'D':
				return (byte) rand.nextInt(3) + 2;// 3.5
			default:
				return (byte) rand.nextInt(1) + 2;// 2.5
			}
		}
		case 1: {
			switch (check) {
			case 'A':
				return (byte) rand.nextInt(4) + 2;// 3.0
			case 'B':
				return (byte) rand.nextInt(4) + 1;// 2.5
			case 'C':
				return (byte) rand.nextInt(3) + 1;// 2.0
			case 'D':
				return (byte) rand.nextInt(2) + 1;// 1.5
			default:
				return (byte) rand.nextInt(2);// 1.0
			}
		}
		default:
			break;
		}
		return 0;
	}

	/**
	 * Returns the positive integer of the skill a character is set to gain this level through their class
	 * otherwise returns -1
	 * @param level
	 * @return
	 */
	public int newSkill(int level)
	{
		int[] gains = GAINS_DEFAULT;
		gains[14] = sp1;
		gains[15] = sp2;
		for(int i = 0; i < 16; i++)
		{
			if(gains[i] == level)
				return skills[i];
		}
		return -1;
	}
	
	/**
	 * returns a value from -1 ~ 3 indicating the bonus given from the Class
	 * property indicated
	 * 
	 * @param b
	 * @return
	 */
	public int bonus(int b) {
		char check;
		switch (b) {
		case 5:
			check = MOVv;
			break;
		case 6:
			check = VSNv;
			break;
		case 7:
			check = SPDv;
			break;
		case 8:
			check = ATKv;
			break;
		case 9:
			check = DEFv;
			break;
		default:
			return 0;
		}
		switch (check) {
		case 'A':
			return 3;
		case 'B':
			return 2;
		case 'C':
			return 1;
		case 'F':
			return -1;
		default:
			return 0;
		}
	}

	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("Klass");
		xml.element("id").text(this.id).pop();
		xml.element("Name").text(this.name).pop();
		xml.element("HPv").text(this.HPv).pop();
		xml.element("SPv").text(this.SPv).pop();
		xml.element("POWv").text(this.POWv).pop();
		xml.element("SKLv").text(this.SKLv).pop();
		xml.element("MNDv").text(this.MNDv).pop();
		xml.element("MOVv").text(this.MOVv).pop();
		xml.element("VSNv").text(this.VSNv).pop();
		xml.element("SPDv").text(this.SPDv).pop();
		xml.element("ATKv").text(this.ATKv).pop();
		xml.element("DEFv").text(this.DEFv).pop();
//		xml.element("wpn_A");
//		this.wpn_A.xmlWrite(xml);
//		xml.pop();
//		xml.element("wpn_B");
//		this.wpn_B.xmlWrite(xml);
//		xml.pop();
		xml.element("race");
//		this.race.xmlWrite(xml);
		xml.pop();
		xml.pop();

	}

	public static int readcount = 0;

	@Override
	public Klass xmlRead(Element element) {
		Klass klass = new Klass();
		klass.id = element.getInt("id", readcount);
		klass.name = element.get("Name");
		klass.HPv = toAcceptableChar(element.get("HPv").charAt(0));
		klass.SPv = element.get("SPv").charAt(0);
		klass.POWv = element.get("POWv").charAt(0);
		klass.SKLv = element.get("SKLv").charAt(0);
		klass.MNDv = element.get("MNDv").charAt(0);
		klass.MOVv = element.get("MOVv").charAt(0);
		klass.SPDv = element.get("SPDv").charAt(0);
		klass.VSNv = element.get("VSNv").charAt(0);
		//klass.wpn_A = wpn_A.xmlRead(element.getChildByName("wpn_A"));
		//klass.wpn_B = wpn_A.xmlRead(element.getChildByName("wpn_B"));
//		klass.race = race.xmlRead(element.getChildByName("race"));
		readcount++;
		return klass;
	}

	public static Klass createKlass(Scanner in) {
		Klass klass = new Klass();
		System.out.println("Creating a new KLASS");
		klass.id = readcount;
		readcount++;
		System.out.print("\nNAME:");
		klass.name = in.next();
		System.out.println("\nStats");
		System.out.println(valueString());
		System.out.print("\nHP:");
		klass.HPv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nSP:");
		klass.SPv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nPOW:");
		klass.POWv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nSKL:");
		klass.SKLv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nMND:");
		klass.MNDv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nMOV:");
		klass.MOVv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nVSN:");
		klass.VSNv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nSPD:");
		klass.SPDv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nATK:");
		klass.ATKv = toAcceptableChar(in.next().charAt(0));
		System.out.print("\nDEF:");
		klass.DEFv = toAcceptableChar(in.next().charAt(0));
		System.out.println("Equippable Weapons");
		System.out.println(Weapon_Type.toOrdinalString());
		//System.out.print("Weapon A:");
		//klass.wpn_A = Weapon_Type.values()[in.nextInt()];
		//System.out.print("Weapon B:");
		//klass.wpn_B = Weapon_Type.values()[in.nextInt()];
		System.out.println("Race");
		System.out.println(Race.toOrdinalString());
//		System.out.print("Race:");
//		klass.race = Race.values()[in.nextInt()];
		return klass;
	}

	private static String valueString() {
		return "A - B - C - D - F";
	}

	private static char toAcceptableChar(char c) {
		c = Character.toUpperCase(c);
		c = (c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'F') ? c
				: DEFAULT_VALUE;
		return c;
	}

	public String toString() {
		if (this == null)
			return "NULL";
		else
			return this.name;
	}

	public enum Values {
		A, B, C, D, F;
	}
}
