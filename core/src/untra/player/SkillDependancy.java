package untra.player;

import untra.database.Data;

public class SkillDependancy {

	public int id = 0;
	public int unlocks = 0;
	
	public SkillDependancy(Data d)
	{
		id = d.getInt("id");
		unlocks = d.getInt("unlocks");
	}
}
