package untra.database;

public class PassiveSkill extends Skill {
	public static final int P_START_INDEX = 3000;
	public static final int P_STAVES = 3001;
	public static final int P_BOWS = 3002;
	public static final int P_LARGE_BLADES = 3003;
	public static final int P_CLAWS = 3004;
	public static final int P_WANDS = 3005;
	public static final int P_GUNS = 3006;
	public static final int P_SHORT_BLADES = 3007;
	public static final int P_MACES = 3008;
	public static final int P_HAMMERS = 3009;
	public static final int P_LANCES = 3010;
	public static final int P_AXES = 3011;
	public static final int P_KNIVES = 3012;
	public static final int P_BOMBS = 3013;
	public static final int P_LIGHT_ARMOR = 3020;
	public static final int P_MEDIUM_ARMOR = 3021;
	public static final int P_HEAVY_ARMOR = 3022;
	public static final int P_SHIELDS = 3023;
	public static final int P_BALANCE_1 = 3100;
	public static final int P_STRENGTH_1 = 3101;
	public static final int P_AGILITY_1 = 3102;
	public static final int P_CONCENTRATION_1 = 3103;
	public static final int P_FORTITUDE_1 = 3104;
	public static final int P_JUMPING_1 = 3105;
	public static final int P_CHANNELING_1 = 3106;
	public static final int P_REVITALIZE_1 = 3107;
	public static final int P_REGENERATE_1 = 3108;
	public static final int P_RESISTANCE_1 = 3109;
	public static final int P_BALANCE_2 = 3110;
	public static final int P_STRENGTH_2 = 3111;
	public static final int P_AGILITY_2 = 3112;
	public static final int P_CONCENTRATION_2 = 3113;
	public static final int P_FORTITUDE_2 = 3114;
	public static final int P_JUMPING_2 = 3115;
	public static final int P_CHANNELING_2 = 3116;
	public static final int P_REVITALIZE_2 = 3117;
	public static final int P_REGENERATE_2 = 3118;
	public static final int P_RESISTANCE_2 = 3119;
	public static final int P_BALANCE_3 = 3120;
	public static final int P_STRENGTH_3 = 3121;
	public static final int P_AGILITY_3 = 3122;
	public static final int P_CONCENTRATION_3 = 3123;
	public static final int P_FORTITUDE_3 = 3124;
	public static final int P_JUMPING_3 = 3125;
	public static final int P_CHANNELING_3 = 3126;
	public static final int P_REVITALIZE_3 = 3127;
	public static final int P_REGENERATE_3 = 3128;
	public static final int P_RESISTANCE_3 = 3129;
	public static final int P_MONKEYGRIP = 3200;
	public static final int P_MAITENANCE = 3201;
	public static final int P_DEATH_STRIKE = 3202;
	public static final int P_TWINSTRIKE = 3203;
	public static final int P_BONECRUSHER = 3204;
	public static final int P_DUAL_WIELD = 3205;
	public static final int P_BATTLEWISE = 3206;
	public static final int P_PIERCING = 3207;
	public static final int P_ITEM_LORE = 3208;
	public static final int P_BLOOD_PRICE = 3209;
	public static final int P_PERCEPTION = 3210;
	public static final int P_LIGHTFOOTED = 3211;
	public static final int P_DISMANTLING = 3212;
	public static final int P_UNSCARRED = 3213;
	public static final int P_VIGILANTE = 3214;
	public static final int P_TELEPORTATION = 3215;

	
//	P-Weapons
//	P-Shields
//	P-Armors
//	P-Balance X		improves EVA
//	P-Strength X	improves ATK
//	P-Fortitude	X	Improves DEF
//	P-Agility X		Improves SPD
//	P-ConcentrationXImproves ACC
//	P-Monkeygrip	Can wield two-handed weapons with one hand
//	P-Maitenance	Armor and Weapon durability decreases slower
//	P-Channeling X	Spell cost reduced x%
//	P-Death Strike	Improved critical chance
//	P-TwinStrike	Improved multistrike chance
//	P-Bonecrusher	Improved knockback chance
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
