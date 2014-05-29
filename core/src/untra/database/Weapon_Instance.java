package untra.database;

public class Weapon_Instance extends Weapon{
	public int durability;
	public Weapon_Prefix prefix = Weapon_Prefix._;
	public Elemental elemental = Elemental.none;
	public Weapon_Suffix suffix = Weapon_Suffix._;
	
	public String weapon_name()
	{
		return prefix.toString() + name + suffix.toString();
	}
	
	public int ATK()
	{
		int atk = this.ATK;
		atk *= prefix.atk_modifier();
		atk *= suffix.atk_modifier();
		return atk;
	}
}
