package untra.database;

public class ReactiveSkill extends Skill {
	//TRIGGERS: end of turn, critical hp, on_hit
	public static final int R_START_INDEX = 1000;
	//AT CRITICAL HP
	public static final int R_CRIT_HASTE = 1001;
	//AT HIT
	public static final int R_HIT_COUNTER = 1101; //attempt to counter
	//ON TARGETED
	public static final int R_TARGET_REFLEX = 1201; //dodge all basic attacks
	public static final int R_TARGET_ARCHERS_REFLEX = 1202; //dodge all missile attacks
	public static final int R_TARGET_RETURN_VOLLEY = 1203; //return all missile attacks
			
	

	
	public ReactiveSkill() {
		super();
		sanitize();
	}
	
	public ReactiveSkill(Data d) {
		super(d);
		sanitize();
	}
	
	private void sanitize()
	{
		this.scope = Skill_Scope.reactive;
	}
	
	public boolean crit_trigger()
	{
		//id = this.id;
		if(id > 4000 && id <= 4100 )
			return true;
		return false;
	}
	
	public boolean hit_trigger()
	{
		//id = this.id;
		if(id > 4100 && id <= 4200 )
			return true;
		return false;
	}
	
	public boolean targetted_trigger()
	{
		//id = this.id;
		if(id > 4200 && id <= 4300 )
			return true;
		return false;
	}
	
	
	
	

}
