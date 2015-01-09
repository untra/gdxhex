package untra.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;

public class Data {
	private final static String CSV_SPLIT = ",";
	private ObjectMap<String, Object> _properties;

	private String[] _keys;

	public String[] keys() {
		return _keys;
	}

	public ObjectMap<String, Object> properties() {
		return _properties;
	}

	public Data(String[] keys, ObjectMap<String, Object> properties) {
		this._keys = keys;
		this._properties = properties;
	}

	public Data() {
		_properties = new ObjectMap<String, Object>();
		_keys = new String[0];
	}

	public String getString(String key) {
		Object s = _properties.get(key);
		return (String) s;
	}

	public int getInt(String key) {
		String string = getString(key);
		if(string == null)
			//bad, errors abound
			string = "0";
		return Integer.valueOf(string);
		// return Integer.valueOf(key);
	}

	public boolean getBool(String key) {
		return Boolean.valueOf(getString(key));
	}

	public Float getFloat(String key) {
		return Float.valueOf(getString(key));
	}

	public String getString(String key, String alt) {
		String string = (String) _properties.get(key);
		return (string == "null") ? alt : string;
	}

	public int getInt(String key, int alt) {
		String string = getString(key);
		return (string == "null") ? alt : Integer.valueOf(string);
	}

	public boolean getBool(String key, boolean alt) {
		String string = getString(key);
		return (string == "null") ? alt : Boolean.valueOf(string);
	}

	public Float getFloat(String key, float alt) {
		String string = getString(key);
		return (string == "null") ? alt : Float.valueOf(string);
	}

	public static ArrayList<Data> load(String path) {
		ArrayList<Data> data = new ArrayList<Data>();
		BufferedReader br = null;
		String line = "";
		try {
			FileHandle file = Gdx.files.internal(path);

			br = new BufferedReader(file.reader());
			String[] keys = br.readLine().split(CSV_SPLIT);
			int max = keys.length;
			ObjectMap<String, Object> prop = new ObjectMap<String, Object>(max);
			while ((line = br.readLine()) != null) {
				prop = new ObjectMap<String, Object>(max);
				// use comma as separator
				String[] values = line.split(CSV_SPLIT);
				for (int i = 0; i < max; i++) {
					prop.put(keys[i], values[i]);

				}
				data.add(new Data(keys, prop));

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}
}
