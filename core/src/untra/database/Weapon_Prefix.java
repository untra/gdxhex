package untra.database;

public enum Weapon_Prefix {
	_, broken, dull, sharpened, masterwork;
	
	public float atk_modifier()
	{
		switch (this) {
		case broken:
			return 0.7f;
		case dull:
			return 0.85f;
		case sharpened:
			return 1.15f;
		case masterwork:
			return 1.3f;
		default:
			return 1.0f;
		}
	}
	
	public float durrability_max_modifier()
	{
		switch (this) {
		case broken:
			return 0.4f;
		case dull:
			return 0.7f;
		case sharpened:
			return 1.3f;
		case masterwork:
			return 1.6f;
		default:
			return 1.0f;
		}
	}
}
