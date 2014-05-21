package untra.scene.game.map;

import com.badlogic.gdx.utils.Array;

public class PassabilityArray {
	// public HexDirectional[]] array;
	public Array<Array<HexDirectional>> array = new Array<Array<HexDirectional>>();

	public int get_width_size() {
		int i;
		try {
			i = array.get(0).size;
		} catch (Exception e) {
			return 0;
		}
		return i;
	}

	public String ToString() {
		String s = "";
		for (int i = 0; i < array.size; i++) {
			for (int j = 0; j < array.get(i).size; j++) {
				s += array.get(i).get(j).toString();
			}
		}
		return s;
	}

	public PassabilityArray(String s, int width, int height) {
		array = new Array<Array<HexDirectional>>();
		for (char c : s.toCharArray()) {
			for (int i = 0; i < array.size; i++) {
				for (int j = 0; j < array.get(i).size; j++) {
					if (c == CPASSABLE)
						array.get(i).set(j, HPASSABLE);
					else if (c == CUNPASSABLE)
						array.get(i).set(j, HUNPASSABLE);
				}
			}
		}
	}

	public static final char CPASSABLE = 'O';

	public static HexDirectional HPASSABLE = HexDirectional.all();
	public static final char CUNPASSABLE = 'X';
	public static HexDirectional HUNPASSABLE = new HexDirectional();

	public boolean is_multitile() {
		return !(array.size > 1);
	}

	public boolean passibility_at(int index) {
		String s = ToString();
		char c = s.charAt(index);
		if (c == CUNPASSABLE)
			return false;
		return true;
	}
}
