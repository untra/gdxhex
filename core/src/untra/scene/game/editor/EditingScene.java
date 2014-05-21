package untra.scene.game.editor;

import untra.gamewindow.FieldInputWindow;
import untra.gamewindow.FileWindow;
import untra.gamewindow.SelectionWindow;
import untra.gamewindow.TextInputWindow;
import untra.graphics.Draw_Object;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import untra.scene.game.GameScene;
import untra.scene.game.map.HexObject;
import untra.scene.game.map.HexTable;
import untra.scene.game.map.HexTile;
import untra.scene.game.window.WindowObjectSelection;
import untra.scene.game.window.WindowSaveDialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.database.Armor;
import untra.database.Database;
import untra.database.Klass;
import untra.database.Race;
import untra.database.Skill;
import untra.database.Tile_Object;
import untra.database.Weapon;
import untra.database.Weapon_Type;
import untra.driver.Audio;
import untra.driver.Base;
import untra.driver.Input;

/**
 * The EditingScene is the map editor for the game. Extends GameScene. It
 * provides methods to load a map from disk, alter the map, and then resave it.
 * Battles cannot be played through this scene.
 * 
 * Oh boy.
 * 
 * @author samuele
 * 
 */
public class EditingScene extends GameScene {
	private WindowObjectSelection windowset_objectSelection = new WindowObjectSelection();
	private FileWindow windowset_loadmap = new FileWindow("Map Data");
	private String[] data_cmds = { "Commit Changes", "Klass", "Weapon",
			"Armor", "Battle Item", "Skill" };
	private SelectionWindow windowset_database = new SelectionWindow(
			new Rectangle(0, 32, Base.window_width(), Base.window_height()),
			data_cmds);
	private static FieldInputWindow windowset_field = new FieldInputWindow(null);
	private TextInputWindow windowset_textinput = new WindowSaveDialog(
			map_array);
	private static boolean initialize_phase = true;
	// private static int var1, var2;
	private static int var1, var2;
	private static final String SE_dig = "DIG4.ogg";
	private static Sound SE_DIG;

	public EditingScene(HexTable map) {
		super(map);
	}

	public EditingScene() {
		super();
	}

	public void initialize() {
		super.initialize();
		SE_DIG = Audio.new_SE(SE_dig);
		windowset_textinput = new WindowSaveDialog(map_array);
		windowset_database.active = false;

	}

	public synchronized void update() {
		super.update();
		switch (STEP) {
		case 0:
			// If the delete key is pressed during step 0
			// it will exit the debugger
			if (Input.isKeyTrigger(Keys.DEL)) {
				System.out.println("==EXITING DEBUG EDITOR==");
				Base.setScene(null);
				return;
			}
			if (Input.isKeyTrigger(Keys.S)) {
				System.out.println("==SAVING CURRENT MAP==");// STEP
				System.out.println("Save map confirm? Y/N");
				windowset_help.move_to_top();
				windowset_help.active = true;
				windowset_help.set_content("Save map confirm? Y/N");
				STEP = 1;
				return;
			}
			if (Input.isKeyTrigger(Keys.L)) {
				System.out.println("==LOADING NEW MAP==");// STEP
				windowset_loadmap = new FileWindow("Map Data");
				windowset_loadmap.move_to_bottom();
				windowset_loadmap.active = true;
				windowset_help.move_to_top();
				windowset_help.active = true;
				windowset_help.set_content("Y: Load Map");
				STEP = 2;
				return;
			}
			if (Input.isKeyTrigger(Keys.D)) {
				System.out.println("==Changing Tile Height==");// STEP
				STEP = 3;
				return;
			}
			if (Input.isKeyTrigger(Keys.V)) {
				System.out.println("==Changing Tile Type==");// STEP
				System.out.println("use , and . to change the tile value");
				var1 = 0;
				STEP = 4;
				return;
			}
			if (Input.isKeyTrigger(Keys.P)) {
				System.out.println("==Changing Tile Passabilites==");// STEP
				System.out.println("left click to change tile passability");
				view_passabilities = true;
				STEP = 5;
				return;
			}
			if (Input.isKeyTrigger(Keys.T)) {
				System.out.println("==Tile Object Placement==");// STEP
				set_cursor_size(0);
				windowset_objectSelection.move_to_bottom();
				windowset_objectSelection.active = true;
				windowset_help.move_to_top();
				windowset_help.active = true;
				windowset_help.set_content("Select Tile");
				STEP = 6;
				return;
			}
			if (Input.isKeyTrigger(Keys.K)) {
				System.out.println("==Database Editing==");// STEP
				// System.out.println("left click to change tile passability");
				// view_passabilities = true;
				// windowset_field = new FieldInputWindow(createKlass);
				// windowset_field.active = true;
				windowset_database.active = true;
				STEP = 7;
				return;
			}
			DEBUG_STEP_0();
			break;
		case 1: {
			DEBUG_STEP_1();
			break;
		}
		case 2: {
			DEBUG_STEP_2();
			break;
		}
		case 3: {
			DEBUG_STEP_3();
			break;
		}
		case 4: {
			DEBUG_STEP_4();
			break;
		}
		case 5: {
			DEBUG_STEP_5();
			break;
		}
		case 6: {
			DEBUG_STEP_6();
			break;
		}
		case 7: {
			DEBUG_STEP_7();
			break;
		}
		default:
			break;
		}
	}

	public void update_windows() {
		super.update_windows();
		windowset_loadmap.update();
		windowset_textinput.update();
		windowset_objectSelection.update();
		windowset_database.update();
		windowset_field.update();
	}

	/**
	 * Debug phase
	 */
	private void DEBUG_STEP_0() {
		if (initialize_phase) {
			STEP = 0;
			System.out.print("==DEBUGGING MAP==");// STEP 0
			System.out.println("PRESS ESCAPE ANY TIME TO LEAVE DEBUG");
			System.out.println("S: save map to xml");// STEP 1
			System.out.println("L: load map from xml (sketchy)");// STEP 2
			System.out.println("D: change tile height");// STEP 3
			System.out.println("V: change tile type");// STEP 4
			System.out.println("T: tile placement");// STEP 5
			System.out.println("P: alter passabilities");// STEP 6
			System.out.println("K: create new Klass");// STEP 7
			initialize_phase = false;
		}

	}

	/**
	 * Saving the Map
	 */
	private void DEBUG_STEP_1() {
		if (Input.isKeyTrigger(Keys.N)) {
			// returns to the main debug phase
			windowset_help.active = false;
			STEP = 0;
			initialize_phase = true;
		}
		if (Input.isKeyTrigger(Keys.Y)) {
			// gets a unique string from datetime to save map as
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// spawns a new xmlwriter and filehandle
			String string = sdf.format(cal.getTime());
			// TODO: the problem is this will not pause the method. processing
			// does not wait for input. fix this
			Gdx.input.getTextInput(windowset_textinput, "Save Map As", string);
			string = windowset_textinput.getUserInput();
			windowset_help.active = false;
			// returns to the main debug phase
			STEP = 0;
			initialize_phase = true;
		}
	}

	/**
	 * Loading the Map
	 */
	private void DEBUG_STEP_2() {
		if (Input.isKeyTrigger(Keys.ESCAPE)) {
			// returns to the main debug phase
			STEP = 0;
			initialize_phase = true;
			windowset_loadmap.active = false;
			windowset_help.set_content("");
			windowset_help.active = false;
			return;
		}
		if (Input.isKeyTrigger(Keys.Y) || Input.triggerA()) {
			FileHandle file = windowset_loadmap.fileAtIndex();
			XmlReader reader = new XmlReader();
			Element element = null;
			try {
				element = reader.parse(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HexTable table = map_array.xmlRead(element);
			// Changes the scene, creating a new editing scene using the loaded
			// map
			Base.setScene(new EditingScene(table));
			windowset_help.active = false;
			return;
		}
	}

	/**
	 * Changing tile height
	 */
	private void DEBUG_STEP_3() {
		if (Input.isKeyTrigger(Keys.ESCAPE)) {
			// returns to the main debug phase
			STEP = 0;
			initialize_phase = true;
			return;
		}
		if (Input.isKeyTrigger(Keys.LEFT_BRACKET)) {
			increase_cursor_size();
			// System.out.println("cursor: " + _cursor_size);
			return;
		}
		if (Input.isKeyTrigger(Keys.RIGHT_BRACKET)) {
			decrease_cursor_size();
			// System.out.println("cursor: " + _cursor_size);
			return;
		}
		if (Input.triggerLeftMouse()) {
			// selectedCursorX = Input.X();
			var1 = Input.Y();
			Selected_Tile = cursor_index();
			var2 = Selected_Tile.z_height();
			return;
		} else if (Input.LeftMousePressed()) {
			int prev = Selected_Tile.z_height();
			Selected_Tile.set_z_height(var2
					- ((Input.Y() - var1) / (Base.tile_pixel_height / 2)));
			int now = Selected_Tile.z_height();
			if (prev != now) {
				SE_DIG.play(0.8f);
				for (HexTile tile : map_array.Range(Selected_Tile,
						_cursor_size, true, false)) {
					tile.set_z_height(now);
				}
			}
			return;
		} else if (Input.LeftMouseReleased()) {
			// resets the maximum z_height to the highest tile on the map. This
			// is used to help readjust the camera
			map_array.heightcheck();
			camera.refresh_map_z(map_array.Maximum_Z_Height());
			// Reapplies tile borders to the tiles. This makes the map
			// aesthetically pleasing.
			map_array.reapply_tile_borders(Selected_Tile.Tile_Value);
			// resets the selected tile so cursor snaps back to whatever tile is
			// underneath the cursor.
			Selected_Tile = null;
		}
	}

	/**
	 * Changing tile type
	 */
	private void DEBUG_STEP_4() {
		// var1 indicates tile type, and is a saved value.
		if (Input.isKeyTrigger(Keys.ESCAPE)) {
			// returns to the main debug phase
			STEP = 0;
			initialize_phase = true;
			return;
		}
		if (Input.isKeyTrigger(Keys.LEFT_BRACKET)) {
			increase_cursor_size();
			// System.out.println("cursor: " + _cursor_size);
			return;
		}
		if (Input.isKeyTrigger(Keys.RIGHT_BRACKET)) {
			decrease_cursor_size();
			// System.out.println("cursor: " + _cursor_size);
			return;
		}
		if (Input.isKeyTrigger(Keys.COMMA)) {
			var1 = Math.max(var1 - 1, 0);
			System.out.println(HexTile.toTileValueString(var1));
			return;
		}
		if (Input.isKeyTrigger(Keys.PERIOD)) {
			var1 = Math.min(var1 + 1, 8);
			System.out.println(HexTile.toTileValueString(var1));
			return;
		}
		if (Input.LeftMousePressed()) {

			// int prev = Selected_Tile.Tile_Value;
			// Selected_Tile.Tile_Value = var1;
			// int now = Selected_Tile.Tile_Value;
			if (Selected_Tile != cursor_index()) {
				Selected_Tile = cursor_index();
				SE_DIG.play(0.8f);
				for (HexTile tile : map_array.Range(Selected_Tile,
						_cursor_size, true, false)) {
					tile.Tile_Value = var1;
				}
			}
			return;
		} else if (Input.LeftMouseReleased()) {
			// Reapplies tile borders to the tiles. This makes the map
			// aesthetically pleasing.
			map_array.reapply_tile_borders(Selected_Tile.Tile_Value);
			// resets the selected tile so cursor snaps back to whatever tile is
			// underneath the cursor.
			Selected_Tile = null;
		}
	}

	/**
	 * Passability viewing
	 */
	private void DEBUG_STEP_5() {
		if (Input.isKeyTrigger(Keys.ESCAPE)) {
			// returns to the main debug phase
			STEP = 0;
			view_passabilities = false;
			initialize_phase = true;
		}
		if (Input.triggerA()) {
			Selected_Tile = cursor_index();
			Selected_Tile.setPassability(!Selected_Tile.passabilities);
			Selected_Tile = null;
		}
	}

	/**
	 * Tile Object Placement
	 */
	private void DEBUG_STEP_6() {
		if (Input.isKeyTrigger(Keys.ESCAPE)) {
			// returns to the main debug phase
			var1 = 0;
			STEP = 0;
			initialize_phase = true;
			windowset_help.active = false;
			windowset_objectSelection.active = false;
		}
		if (Input.isKeyTrigger(Keys.T)) {
			// returns to the main debug phase
			var1 = 0;
			windowset_objectSelection.active = true;
		}
		if (Input.triggerA()) {
			if (windowset_objectSelection.active) {
				// sets var1 to tileobject id;
				var1 = windowset_objectSelection.index;
				// loads the tile object sprite into the OS
				// (Object Sprite) array
				map_array.OS[var1] = new HexObject(Database.tile_objects[var1]);
				// closes the window
				windowset_objectSelection.active = false;
				return;
			}
			Selected_Tile = cursor_index();
			// if the selected tile matches the current tile, flip it
			if (Selected_Tile.map_object_index == var1) {
				Selected_Tile.map_object_properties.Flip = !Selected_Tile.map_object_properties.Flip;
				Selected_Tile = null;
				return;
			}
			// otherwise set it with certain properties
			set_tileobject(Database.tile_objects[var1], Selected_Tile);
			Selected_Tile = null;
			return;
		}
		if (Input.triggerB()) {
			Selected_Tile = cursor_index();
			Selected_Tile.map_object_index = 0;
			Selected_Tile.passabilities = true;
			Selected_Tile = null;
		}
	}

	/**
	 * Database editing
	 */
	private void DEBUG_STEP_7() {
		if (Input.isKeyTrigger(Keys.ESCAPE)) {
			// returns to the main debug phase
			// not sure yet how to kill this thread
			STEP = 0;
			windowset_database.active = false;
			windowset_field.active = false;
			initialize_phase = true;
		}

		try {
			if (Input.triggerA() || Input.isKeyTrigger(Keys.ENTER)) {
				if (windowset_field.active) {
					if (!windowset_field.pauseThread)
						windowset_field.lock();
				}
				if (windowset_database.active) {
					// "Commit Changes", "Klass", "Weapon",
					// "Armor", "Battle Item", "Skill"
					switch (windowset_database.index) {
					case 0:// commit changes
					{
						// recompile klass

						StringWriter stringWriter = new StringWriter();
						XmlWriter writer = new XmlWriter(stringWriter);
						FileHandle handle;
						// klass
						System.out.println("rebuilding classes DB");
						writer.element("classes");
						for (Klass klass : Database.classes) {
							if (klass == null)
								break;
							klass.xmlWrite(writer);
						}
						writer.pop();
						handle = Gdx.files
								.local("src/Content/Data/Game Data/classes.xml");
						handle.writeString(stringWriter.toString(), false);
						stringWriter.flush();
						// Weapon
						System.out.println("rebuilding weapons DB");
						writer.element("weapon");
						for (Weapon weapon : Database.weapons) {
							if (weapon == null)
								break;
							weapon.xmlWrite(writer);
						}
						writer.pop();
						handle = Gdx.files
								.local("src/Content/Data/Game Data/weapons.xml");
						handle.writeString(stringWriter.toString(), false);
						stringWriter.flush();
						// Armor
						System.out.println("rebuilding armors DB");
						writer.element("armors");
						for (Armor armor : Database.armors) {
							if (armor == null)
								break;
							armor.xmlWrite(writer);
						}
						writer.pop();
						handle = Gdx.files
								.local("src/Content/Data/Game Data/armors.xml");
						handle.writeString(stringWriter.toString(), false);
						stringWriter.flush();
						// Skill
						System.out.println("rebuilding skills DB");
						writer.element("skills");
						for (Skill skill : Database.skills) {
							if (skill == null)
								break;
							skill.xmlWrite(writer);
						}
						writer.pop();
						handle = Gdx.files
								.local("src/Content/Data/Game Data/skills.xml");
						handle.writeString(stringWriter.toString(), false);
						stringWriter.flush();

					}
					case 1:// klass
					{
						windowset_field = new FieldInputWindow(createKlass);
						windowset_field.active = true;
						break;
					}

					default:
						break;
					}
					windowset_database.active = false;
				}
			}
			// else if (windowset_field.pauseThread
			// && windowset_field.thread.getState() == State.RUNNABLE)
			// windowset_field.thread.wait();
		} catch (IllegalMonitorStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * sets the tile object at the specified tile
	 * 
	 * @param T
	 * @param H
	 */
	private void set_tileobject(Tile_Object T, HexTile H) {
		H.map_object_index = T.id;
		/*
		 * if (T.is_oversized_object()) { LinkedList<HexTile> surroundings =
		 * map_array.Range(H, 1); surroundings.remove(H); // every surrounding
		 * tile should have no oversized objects for (int i = 0; i <
		 * surroundings.size(); i++) { // If oversized if
		 * (map_array.OS[surroundings.get(i).map_object_index] .getOversized())
		 * { surroundings.get(i).map_object_index = 0;
		 * surroundings.get(i).passabilities = true; break; } } }
		 */
		if (T.is_multitile_object()) {
			map_array.multitile_level(H, map_array.OS[T.id].passables);
		}
		H.passabilities = false;
		H.map_object_properties.Flip = MathUtils.randomBoolean();
	}

	public void Draw_Windows(Draw_Object s_batch) {
		super.Draw_Windows(s_batch);
		if (windowset_loadmap.active || windowset_loadmap.isPanning())
			windowset_loadmap.draw(s_batch);
		if (windowset_textinput.active || windowset_textinput.isPanning())
			windowset_textinput.draw(s_batch);
		if (windowset_objectSelection.active
				|| windowset_objectSelection.isPanning())
			windowset_objectSelection.draw(s_batch);
		if (windowset_field.active || windowset_field.isPanning())
			windowset_field.draw(s_batch);
		if (windowset_database.active || windowset_database.isPanning())
			windowset_database.draw(s_batch);
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
	}

	/**
	 * Creates a new klass
	 */
	private Runnable createKlass = new Runnable() {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Klass klass = new Klass();
				klass.id = Database.DataCount(Database.classes);
				// klass.readcount++;
				windowset_field.StringSelection("Name", "name");

				klass.name = windowset_field.getString();
				windowset_field.EnumSelection("HP", Klass.Values.C);
				klass.HPv = windowset_field.getString().toUpperCase().charAt(0);
				windowset_field.EnumSelection("SP", Klass.Values.C);
				klass.SPv = windowset_field.getString().toUpperCase().charAt(0);
				windowset_field.EnumSelection("POW", Klass.Values.C);
				klass.POWv = windowset_field.getString().toUpperCase()
						.charAt(0);
				windowset_field.EnumSelection("SKL", Klass.Values.C);
				klass.SKLv = windowset_field.getString().toUpperCase()
						.charAt(0);
				windowset_field.EnumSelection("MND", Klass.Values.C);
				klass.MNDv = windowset_field.getString().toUpperCase()
						.charAt(0);
				windowset_field.EnumSelection("MOV", Klass.Values.C);
				klass.MOVv = windowset_field.getString().toUpperCase()
						.charAt(0);
				windowset_field.EnumSelection("VSN", Klass.Values.C);
				klass.VSNv = windowset_field.getString().toUpperCase()
						.charAt(0);
				windowset_field.EnumSelection("SPD", Klass.Values.C);
				klass.SPDv = windowset_field.getString().toUpperCase()
						.charAt(0);
				windowset_field.EnumSelection("ATK", Klass.Values.C);
				klass.ATKv = windowset_field.getString().toUpperCase()
						.charAt(0);
				windowset_field.EnumSelection("DEF", Klass.Values.C);
				klass.DEFv = windowset_field.getString().toUpperCase()
						.charAt(0);
				windowset_field.EnumSelection("Weapon A", Weapon_Type.none);
				klass.wpn_A = Weapon_Type.values()[windowset_field.getInt()];
				windowset_field.EnumSelection("Weapon B", Weapon_Type.none);
				klass.wpn_B = Weapon_Type.values()[windowset_field.getInt()];
				windowset_field.EnumSelection("Race", Race.Human);
				klass.race = Race.values()[windowset_field.getInt()];
				Database.classes[Database.DataCount(Database.classes)] = klass;
				STEP = 0;
				windowset_field.active = false;
				Thread.currentThread().interrupt();
				return;
			}
			return;
		}
	};

	/**
	 * Creates a new Weapon
	 */
	private Runnable createWeapon = new Runnable() {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Weapon weapon = new Weapon();
				weapon.id = Database.DataCount(Database.weapons);
				windowset_field.StringSelection("Name", "name");
				weapon.name = windowset_field.getString();
				windowset_field.StringSelection("Description", "desc");
				weapon.description = windowset_field.getString();
				windowset_field.EnumSelection("Weapon Type", Weapon_Type.none);
				weapon.type = Weapon_Type.values()[windowset_field.getInt()];
				windowset_field.IntSelection("ATK", 8);
				weapon.ATK = windowset_field.getInt();
				windowset_field.IntSelection("POW", 0);
				weapon.POW = windowset_field.getInt();
				windowset_field.IntSelection("SKL", 0);
				weapon.SKL = windowset_field.getInt();
				windowset_field.IntSelection("MND", 0);
				weapon.MND = windowset_field.getInt();
				windowset_field.IntSelection("MOV", 0);
				weapon.MOV = windowset_field.getInt();
				windowset_field.IntSelection("VSN", 0);
				weapon.VSN = windowset_field.getInt();
				windowset_field.IntSelection("SPD", 0);
				weapon.SPD = windowset_field.getInt();
				windowset_field.IntSelection("Range", 1);
				weapon.range = windowset_field.getInt();
				windowset_field.BooleanSelection("Line Attack", false);
				weapon.line = windowset_field.getBoolean();
				windowset_field.IntSelection("use animation", 0);
				weapon.use_anim_id = windowset_field.getInt();
				windowset_field.IntSelection("hit animation", 0);
				weapon.hit_anim_id = windowset_field.getInt();
				Database.weapons[Database.DataCount(Database.weapons)] = weapon;
				STEP = 0;
				windowset_field.active = false;
				Thread.currentThread().interrupt();
				return;
			}
			return;
		}
	};

	public void dispose() {
		super.dispose();
	}

	public String toString() {
		return "EditingScene_" + super.toString();
	}
}
