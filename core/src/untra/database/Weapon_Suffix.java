package untra.database;

public enum Weapon_Suffix {
	_;
	
	public float durrability_max_modifier()
	{
		switch (this) {
			default:
				return 1.0f;
		}
	}
	
	public float atk_modifier()
	{
		switch (this) {
			default:
				return 1.0f;
		}
	}
}
