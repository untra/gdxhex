package untra.database;

//import untra.driver.IXml;

public class ActiveSkill extends Skill implements IRanged {
	public Elemental element;
	public Skill_Type type;
	public int sp_cost;
	public float power;
	public float variance;
	public boolean is_spell;
	protected int range;
	public int anim_use_id;
	public int anim_hit_id;
	protected int field;
	public boolean is_line;
	public Status status = Status._;
	public float accuracy;
	public int common_event_id;
	
	public ActiveSkill()
	{
		super();
		element = Elemental.none;
		type = Skill_Type.POW;
		sp_cost = 0;
		power = 0.5f;
		variance = 0.15f;
		is_spell = true;
		is_line = false;
		range = 1;
		anim_use_id = 0;
		anim_hit_id = 0;
		common_event_id = 0;
		field = 0;
//		level_cost = 0;
		accuracy = 1.0f;
		status = Status._;
//		prereq = 0;
		sanitize();
	}
	
	public ActiveSkill(Data d)
	{
		super(d);
		element = Elemental.values()[ d.getInt("element")];
		type = Skill_Type.values()[(int) d.getInt("type")];
		sp_cost = d.getInt("sp_cost");
		power = d.getFloat("power");
		variance = d.getFloat("variance");
		is_spell = d.getBool("is_spell");
		is_line = d.getBool("is_line");
		range = d.getInt("range");
		anim_use_id = d.getInt("anim_use_id");
		anim_hit_id = d.getInt("anim_hit_id");
		common_event_id = d.getInt("common_event_id");
		field = d.getInt("field");
		accuracy = d.getFloat("accuracy");
		element = Elemental.values()[ d.getInt("status")];
		sanitize();
	}
	
	public int Range() {
		return range;
	}

	public int Field() {
		return field;
	}
	
	public static ActiveSkill range_skill(int range) {
		ActiveSkill s = new ActiveSkill();
		s.range = range;
		return s;
	}
	
	public boolean is_healing_skill()
	{
		return (power < 0);
	}
	
	private void sanitize()
	{
		this.scope = Skill_Scope.active;
	}
	
}
