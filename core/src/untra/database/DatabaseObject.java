package untra.database;

public abstract class DatabaseObject {
	public int id;
	public String name;
	public String description;
	
	public DatabaseObject(Data d)
	{
		id = d.getInt("id");
		name = d.getString("name");
		description = d.getString("description");
	}
	
	public DatabaseObject()
	{
		id = -1;
		name = "??!!?";
		description = "ERROR!";
	}
}
