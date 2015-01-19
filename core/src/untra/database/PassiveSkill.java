package untra.database;

public class PassiveSkill extends Skill {
	public static final int P_START_INDEX = 2000;
	public static final int P_STAVES = 2001;
	public static final int P_BOWS = 2002;
	public static final int P_LARGE_BLADES = 2003;
	public static final int P_CLAWS = 2004;
	public static final int P_WANDS = 2005;
	public static final int P_GUNS = 2006;
	public static final int P_SHORT_BLADES = 2007;
	public static final int P_MACES = 2008;
	public static final int P_HAMMERS = 2009;
	public static final int P_LANCES = 2010;
	public static final int P_AXES = 2011;
	public static final int P_KNIVES = 2012;
	public static final int P_BOMBS = 2019;
	public static final int P_NATURAL_WEAPON = 2019;
	public static final int P_LIGHT_ARMOR = 2020;
	public static final int P_MEDIUM_ARMOR = 2021;
	public static final int P_HEAVY_ARMOR = 2022;
	public static final int P_NATURAL_ARMOR = 2029;
	public static final int P_SHIELDS = 2030;
	public static final int P_ELEM_FIRE = 2031;
	public static final int P_ELEM_ICE = 2032;
	public static final int P_ELEM_LIGHTNING = 2033;
	public static final int P_ELEM_WATER = 2034;
	public static final int P_ELEM_EARTH = 2035;
	public static final int P_ELEM_WIND = 2036;
	public static final int P_ELEM_PLANT = 2037;
	public static final int P_ELEM_MACHINE = 2038;
	public static final int P_ELEM_UNDEAD = 2039;
	public static final int P_BALANCE_1 = 2100;
	public static final int P_STRENGTH_1 = 2101;
	public static final int P_AGILITY_1 = 2102;
	public static final int P_CONCENTRATION_1 = 2103;
	public static final int P_FORTITUDE_1 = 2104;
	public static final int P_JUMPING_1 = 2105;
	public static final int P_CHANNELING_1 = 2106;
	public static final int P_REVITALIZE_1 = 2107;
	public static final int P_REGENERATE_1 = 2108;
	public static final int P_RESISTANCE_1 = 2109;
	public static final int P_BALANCE_2 = 2110;
	public static final int P_STRENGTH_2 = 2111;
	public static final int P_AGILITY_2 = 2112;
	public static final int P_CONCENTRATION_2 = 2113;
	public static final int P_FORTITUDE_2 = 2114;
	public static final int P_JUMPING_2 = 2115;
	public static final int P_CHANNELING_2 = 2116;
	public static final int P_REVITALIZE_2 = 2117;
	public static final int P_REGENERATE_2 = 2118;
	public static final int P_RESISTANCE_2 = 2119;
	public static final int P_BALANCE_3 = 2120;
	public static final int P_STRENGTH_3 = 2121;
	public static final int P_AGILITY_3 = 2122;
	public static final int P_CONCENTRATION_3 = 2123;
	public static final int P_FORTITUDE_3 = 2124;
	public static final int P_JUMPING_3 = 2125;
	public static final int P_CHANNELING_3 = 2126;
	public static final int P_REVITALIZE_3 = 2127;
	public static final int P_REGENERATE_3 = 2128;
	public static final int P_RESISTANCE_3 = 2129;
	public static final int P_MONKEYGRIP = 2200;
	public static final int P_MAITENANCE = 2201;
	public static final int P_DEATH_STRIKE = 2202;
	public static final int P_TWINSTRIKE = 2203;
	public static final int P_BONECRUSHER = 2204;
	public static final int P_DUAL_WIELD = 2205;
	public static final int P_BATTLEWISE = 2206;
	public static final int P_PIERCING = 2207;
	public static final int P_ITEM_LORE = 2208;
	public static final int P_BLOOD_PRICE = 2209;
	public static final int P_PERCEPTION = 2210;
	public static final int P_LIGHTFOOTED = 2211;
	public static final int P_DISMANTLING = 2212;
	public static final int P_UNSCARRED = 2213;
	public static final int P_VIGILANTE = 2214;
	public static final int P_TELEPORTATION = 2215;
	public static final int P_IMMUNITY = 2216;
	public static final int P_VERMIN = 2217;
	public static final int P_PHEONIX = 2218;

	
//	P-Weapons
//	P-Shields
//	P-Armors
//	P-Balance X		improves EVA
//	P-Strength X	improves ATK
//	P-Fortitude	X	Improves DEF
//	P-Agility X		Improves SPD
//	P-ConcentrationXImproves ACC
//	P-Monkeygrip	Can wield two-handed weapons with one hand
//	P-Maitenance	Armor and Weapon cannot be stolen	
//	P-Channeling X	Spell cost reduced x%
//	P-Death Strike	Improved critical chance
//	P-TwinStrike	Improved multistrike chance
//	P-Bonecrusher	Improved knockback chance
//	P-Lucky			Reduced chance of being crtical, multistrike or knockback
//	P-Dual Wield	May equip a one-handed weapon in each hand without clumsy
//	P-BattleWise	May learn unique skills when hit with them
//	P-Resistance X	Chance of incurring a debuff reduced x%
//	P-Piercing		Spells ignore reaction abilities
//	P-Item Lore		Improves item effects
//	P-Blood Price	Spells consume HP instead of SP. Damage first draws from SP
//	P-Regenerate X	regain x more hp at the beginning of every turn
//	P-Revitilize X	regain x more sp at the beginning of every turn
//	P-Perception	Can see traps within the sight radius
//	P-Lightfooted	Traps are not triggered	when stepped on
//	P-Dismantling	Can destroy traps as a context command
//	P-Unscarred		Basic attacks critical when at full HP
//	P-Vigilante		Attacks from the side and back do normal damage
//	P-Teleportation	Movement by teleportation
//	P-Jumping X		Can move across elevation increases up to X
//	P-PHEONIX		Character will return in the next battle after death
	
	
	public boolean can_equip_weapon(Weapon_Type type)
	{
		int i = type.ordinal();
		if(P_START_INDEX + 1 + i == this.id)
			return true;
		else return false;
	}
	
	public PassiveSkill() {
		super();
		sanitize();
	}
	
	public PassiveSkill(Data d) {
		super(d);
		sanitize();
	}
	
	private void sanitize()
	{
		this.scope = Skill_Scope.passive;
	}
	
	

}
