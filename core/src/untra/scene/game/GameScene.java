package untra.scene.game;

//Three types of saving:
//saving a map from editing
//saving a map from in-game play
//saving a game from play;

import untra.graphics.Draw_Object;
import untra.graphics.GameColor;
import untra.graphics.Sprite;
import untra.graphics.SpriteSet;

import java.util.LinkedList;

import untra.scene.IScene;
import untra.scene.game.battle.Battler;
import untra.scene.game.map.Camera;
import untra.scene.game.map.HexDirectional;
import untra.scene.game.map.HexObject;
import untra.scene.game.map.HexTable;
import untra.scene.game.map.HexTile;
import untra.scene.game.map.Weather;
import untra.scene.game.window.WindowHelp;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.database.Database;
import untra.database.Tile_Object;
import untra.driver.Base;
import untra.driver.Input;

//import scene.game.battle.AI;

public class GameScene implements IScene {

	// public static BattleMath b_math = new BattleMath();
	public boolean battle_ended = false;
	private boolean initalizing = false;
	// #//#//#//#//#//#//#//#//#//#//#//#//#//#//#//#
	// TEMP!
	// private HexObject[] OS = new HexObject[6] {
	// HexObject.Cactus_A(12),
	// HexObject.Cactus_B(12), HexObject.Cactus_C(12),
	// HexObject.Palm_Tree_A(4),
	// HexObject.Palm_Tree_B(4), HexObject.Palm_Tree_C(4) };
	// private HexObject[] OS = new HexObject[2] {
	// HexObject.Oak_Orange_B(16),
	// HexObject.Oak_Red_B(16) };
	protected int _cursor_size = 0;
	private static LinkedList<Tile_Object> TileObjects = new LinkedList<Tile_Object>();
	// TEMP!
	public static Camera camera;
	public static HexTable map_array;
	public static Weather weather = new Weather();
	private HexTile Current_Tile = new HexTile(0, 0, 0, 0);
	// private Sprite Panorama;

	public static SpriteSet Tile_sprites = new SpriteSet("Sprites/FB.png", 4, 9);
	private SpriteSet HighLighter = new SpriteSet("Misc/Highlight.png", 14, 1);
	private Sprite Grid_Tile = new Sprite("Misc/b_grid.png");
	private Sprite Border = new Sprite("Sprites/Borders.png");
	// private Sprite LBorder = new Image("Sprites/LBorder");
	private SpriteSet Walls = new SpriteSet("Sprites/Walls.png", 4, 9);
	private SpriteSet Blur_Set_sprites = new SpriteSet(
			"Sprites/Forest_Blur.png", 6, 4);
	private SpriteSet Artifacts_0 = new SpriteSet("Sprites/0_arti.png", 16, 16);
	private SpriteSet Barriers_1 = new SpriteSet("Sprites/1_barr.png", 16, 16);
	private static HexObject[] Object_Set;
	protected boolean view_passabilities = false;
	protected HexTile hovering;

	public GameScene() {
		weather.initialize_bitmaps();
		TileObjects.add(Database.tile_objects[32]);// cactus a
		TileObjects.add(Database.tile_objects[33]);// cactus b
		TileObjects.add(Database.tile_objects[34]);// catcus c
		TileObjects.add(Database.tile_objects[35]);// palm tree a
		TileObjects.add(Database.tile_objects[36]);// palm tree b
		TileObjects.add(Database.tile_objects[37]);// palm tree c
		map_array = new HexTable(24, 32, 0, TileObjects);
		camera = new Camera(map_array.Pixel_Width_Max(),
				map_array.Pixel_Height_Max(), map_array.Maximum_Z_Height());
		// map_array.Natural_Linked_Path(new HexTile(0, 0, 2), new HexTile(12,
		// 16, 2), 2); //Creates a river from the top left to the map center
		// map_array.Natural_Linked_Path(new HexTile(23, 31, 2)4 new HexTile(12,
		// 16, 2), 2); //Creates a river from the bottom right to the map center
		// map_array.Natural_Linked_Path(new HexTile(0, 31, 2), new HexTile(12,
		// 16, 2), 2); //Creates a river from the top right to the map center
		map_array.Recursive_Linked_Path(new HexTile(0, 0, 1, 0), new HexTile(
				23, 16, 1, 0), 2, 2, 0); // Creates a river from the top right
											// to the map center
		// map_array.Recursive_Linked_Path(new HexTile(8, 0, 1), new HexTile(8,
		// 31, 1), 1); //Creates a river from the top right to the map center
		// map_array.Tile_At(5, 7).z_height() = 4;
		map_array.Tile_At(20, 20).Tile_Value = 2;
		map_array.Tile_At(20, 20).set_z_height(3);
		map_array.reapply_tile_borders(0);
		map_array.reapply_tile_borders(2);
		map_array.reapply_tile_borders(4);
		map_array.Apply_Map_Objects(TileObjects);
		map_array.Apply_Map_Barriers(16, 23, 12);
		map_array.TEMP_Fire_Everywhere();
		weather.setForecast(Weather.Forecast.none);

		Player_Spawn_Locations = map_array
				.Set_Player_Spawn_Locations(new HexTile(6, 4));
		Object_Set = map_array.OS;
		// Blur_Set_sprites.setOrigin(8, 16);
		// This sets the flag to perform initialization operations at the first
		// update call.
		// This is done to prevent errors.
		initalizing = true;
		// temp
	}

	public GameScene(HexTable map) {
		map_array = map;
		camera = new Camera(map_array.Pixel_Width_Max(),
				map_array.Pixel_Height_Max(), map_array.Maximum_Z_Height());
		initalizing = true;
	}

	public void update() {
		if (initalizing)
			initialize();
		// initialize processing + Post Start processing
		update_inputs();
		camera.update_normal_scroll();
		map_array.Update();
		update_windows();
		weather.update(camera.difference());
	}

	private void update_inputs() {
		// Set selection
		// TEMP
		// if (Input.triggerA()) {
		// if (Active_Battler() != null
		// && Input.cursorInRectangle(Active_Battler()
		// .coordinate_rectangle(camera.coordinates()))) {
		//
		// } else if (Active_Battler() == null) {
		// for (Battler B : Players) {
		// if (Input.cursorInRectangle(B.coordinate_rectangle(camera
		// .coordinates()))) {
		//
		// setPHASE(1);
		// // B.Properties.SP -= 30;
		// // B.Properties.HP -= 30;
		// setActive_Battler(B);
		// }
		// break;
		// }
		// }
		// }
		camera.update_normal_scroll();
		if (Input.isPressed(Keys.E))
			Base.Viewing_Grid = true;
		else
			Base.Viewing_Grid = false;
		if (Input.isPressed(Keys.NUM_1))
			Base.Viewing_Height = true;
		else
			Base.Viewing_Height = false;
		if (Input.isPressed(Keys.F9)) {
			System.out.println(contextData());
		}
		/*
		 * if (Input.isPressed(Keys.F9)) Base.setScene(new DebugScene());
		 */
		// TEMP
		// if (Input.isPressed(Keys.NUM_0)) {
		// setPHASE(10);
		// }
		if (Input.isPressed(Keys.F5)) {
			System.out.print("");
		}
		// TEMP

	}

	/**
	 * returns a string containing large amounts of metadata used for bugtesting
	 * only
	 */
	private String contextData() {
		String string = new String();
		string += "Camera X: " + camera.X() + "\n";
		string += "Camera Y: " + camera.Y() + "\n";
		string += "Mouse  X: " + Input.X() + "\n";
		string += "Mouse  Y: " + Input.Y() + "\n";
		return string;
	}

	// private boolean countering;

	protected static boolean cursor_active = true;
	public static long turn_count = 0;
	public static boolean freeze_scrolling = false;
	// public int[] show_dmg_pop_ind;
	private LinkedList<HexTile> Player_Spawn_Locations;
	// private LinkedList<HexTile> Enemy_Spawn_Locations;
	public static LinkedList<Battler> Players;
	public static LinkedList<Battler> Enemies;
	public static LinkedList<Battler> Dead_Players;
	public static LinkedList<Battler> Dead_Enemies;

	// private static Dictionary<int, Battler> sorted_set = new Dictionary<int,
	// Battler>();

	public static LinkedList<HexTile> player_vision() {
		LinkedList<HexTile> vision = new LinkedList<HexTile>();
		for (Battler B : Players) {
			vision.addAll(map_array.Passable_Range(B.Pos(),
					B.properties.getVSN(), new LinkedList<Battler>()));
		}
		return HexTable.trimmed_range(vision);
	}

	public static LinkedList<HexTile> enemy_vision() {
		LinkedList<HexTile> vision = new LinkedList<HexTile>();
		for (Battler B : Enemies) {
			vision.addAll(map_array.Passable_Range(B.Pos(),
					B.properties.getVSN(), new LinkedList<Battler>()));
			// HexTable.trimmed_range(vision);
		}
		return HexTable.trimmed_range(vision);
	}

	/**
	 * Returns all living battlers
	 * */
	public static LinkedList<Battler> All_Battlers() {
		LinkedList<Battler> LinkedList = new LinkedList<Battler>();
		LinkedList.addAll(Players);
		LinkedList.addAll(Enemies);
		return LinkedList;
	}

	/**
	 * Returns the hexobject at the specied index
	 * 
	 * @param index
	 * @return
	 */
	public static HexObject hexObjectFrom(int index) {
		return Object_Set[index];
	}

	/**
	 * Indicates the step : action processing
	 * */
	public int STEP;
	/**
	 * A generic list of tiles used for various purposes
	 * */
	private LinkedList<HexTile> tiles = new LinkedList<HexTile>();
	public static LinkedList<HexTile> move_range = new LinkedList<HexTile>();
	public static LinkedList<HexTile> attack_range = new LinkedList<HexTile>();
	public static LinkedList<HexTile> heal_range = new LinkedList<HexTile>();
	public static LinkedList<HexTile> special_range = new LinkedList<HexTile>();
	// private Battler Selected_Battler;
	public static HexTile Selected_Tile;

	// WINDOWS
	protected static WindowHelp windowset_help = new WindowHelp();;

	/**
	 * Battle Initialization Sequence
	 * */
	public void initialize() {
		// sets the flag for in_battle
		Base.In_Battle = true;
		// initializes static objects
		// use_anim = new Animation();
		// hit_anim = new Animation();
		// Create Map
		// Enable Music
		// Setup Temp Info
		// Spawn Props

		// sort_battlers();

		// open_Scene?
		// Finish Preparations
		// Battle Start
		// resets initalizeing flag
		Players = new LinkedList<Battler>();
		Enemies = new LinkedList<Battler>();
		initalizing = false;
		// TEMP
	}

	public void update_windows() {
		windowset_help.update();
	}

	/**
	 * returns true if the cursor is within the sprite bounds of a battler //
	 * (living or dead)
	 * */
	public boolean is_occupied() {
		LinkedList<Battler> Battlers = new LinkedList<Battler>(Players);
		Battlers.addAll(Enemies);
		for (Battler B : Battlers) {
			if (Input.cursorInRectangle(B.rect()))
				return true;
		}
		return false;
	}

	/**
	 * Returns the battler whose position matches the specified hextile. If no
	 * battler is at the specified position, returns null;
	 * 
	 * @param h
	 * @return
	 */
	public static Battler occupied_by(HexTile h) {
		for (Battler B : All_Battlers()) {
			if (B.Pos().equals(h))
				return B;
		}
		return null;
	}

	/**
	 * Returns the battler within the spritebounds of the designated
	 * coordinates. NOTE: Will throw a nasty, angry exception if there is no
	 * battler found at the specified coordinates.
	 * */
	public static Battler occupied_by(int x, int y) {
		try {
			for (Battler B : All_Battlers()) {
				if (Input.cursorInRectangle(B.rect()))
					return B;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(String.format(
					"No battler found at coordinate {0}, {1}. Derp.", x, y));
		}
		// Base.SCENE = null;
		return null;
	}

	/**
	 * returns the Hextile underneath the mouse cursor
	 * 
	 * @return
	 */
	public HexTile cursor_index() {
		float u, v;
		u = ((camera.Y()) + (float) (Input.Y() - 8))
				/ (int) (Base.tile_pixel_height * .75); // y coordinate
		if ((int) u % 2 == 0)
			v = (float) ((camera.X() + (Input.X())) / Base.tile_pixel_width); // x
																				// coordinate
		else
			v = ((float) (camera.X() + (Input.X() - (Base.tile_pixel_width / 2))) / Base.tile_pixel_width); // x
																											// coordinate

		return map_array.Tile_At(
				(byte) Math.max(Math.min(u, map_array.Height() - 1), 0),
				(byte) Math.max(Math.min(v, map_array.Width() - 1), 0));
	}

	protected LinkedList<HexTile> _cursor_range = new LinkedList<HexTile>();

	protected void increase_cursor_size() {
		set_cursor_size(_cursor_size + 1);
	}

	protected void decrease_cursor_size() {
		set_cursor_size(_cursor_size - 1);
	}

	protected void set_cursor_size(int i) {
		_cursor_size = Math.min(Math.max(0, i), 2);
	}

	/**
	 * Sets the source rectangle for the specified tile.
	 * */
	protected void set_border_src_rect(HexTile tile, HexDirectional dir) {
		// HexTile tile = map_array.Tile_At(u, v);
		// premtively sets the source rectangle to 0
		// if the tile is a liquid, it should have different borders
		Border.src_rect(new Rectangle(0, 0, 0, 0));
		HexTile SW, SE;
		// if the tile doesn't need borders, don't give them any
		if (tile.border_simmilar(map_array.D(tile, dir)))
			return;
		// int xm = (int)((Base.ticks() / 8) % 3);
		int ym = tile.Tile_Value * Base.tile_pixel_height;
		if (dir.E) {
			// if(tile.border_simmilar(map_array.E(tile))) return;
			Border.src_rect(new Rectangle(Base.tile_pixel_width / 2, ym
					+ (Base.tile_pixel_height / 4), Base.tile_pixel_width / 2,
					Base.tile_pixel_height / 2));
		} else if (dir.W) {
			// if (tile.border_simmilar(map_array.W(tile))) return;
			Border.src_rect(new Rectangle(0, ym + (Base.tile_pixel_height / 4),
					(Base.tile_pixel_width / 2), Base.tile_pixel_height / 2));
		} else if (dir.NE) {
			// if (tile.border_simmilar(map_array.NE(tile))) return;
			Border.src_rect(new Rectangle(Base.tile_pixel_width / 2, ym,
					(Base.tile_pixel_width / 2), Base.tile_pixel_height / 4));
		} else if (dir.NW) {
			// if (tile.border_simmilar(map_array.NW(tile))) return;
			Border.src_rect(new Rectangle(0, ym, Base.tile_pixel_width / 2,
					Base.tile_pixel_height / 4));
		} else if (dir.SE) {
			SE = map_array.SE(tile);
			// Special case: if the tile : front of this tile is taller, it
			// should not draw its border
			if (SE != null && SE.z_height() >= tile.z_height())
				return;
			// if (tile.border_simmilar(map_array.SE(tile))) return;
			Border.src_rect(new Rectangle(Base.tile_pixel_width / 2, ym
					+ (3 * Base.tile_pixel_height / 4),
					Base.tile_pixel_width / 2, Base.tile_pixel_height / 4));
		} else if (dir.SW) {
			SW = map_array.SW(tile);
			// Special case: if the tile : front of this tile is taller, it
			// should not draw its border
			if (SW != null && SW.z_height() >= tile.z_height())
				return;
			// if (tile.border_simmilar(map_array.SW(tile))) return;
			Border.src_rect(new Rectangle(0, ym
					+ (3 * Base.tile_pixel_height / 4),
					Base.tile_pixel_width / 2, Base.tile_pixel_height / 4));
		}

		return;
	}

	/**
	 * Draws Tile Borders
	 * */
	private void Draw_Tile_Borders(Draw_Object s_batch, int u, int v) {
		// Blur_Set_sprites.Y_index = (int)(Base.ticks() / 6 % 3);
		// Blur_Set_sprites.Y_index = 0;
		// HexTile Current_Tile = map_array.Tile_At(u, v);
		int z_height = Current_Tile.z_height();
		Rectangle P;
		if (u > 1) {
			System.out.print("");
		}
		P = initializePositionRect(u, v);
		P.y -= z_height * Base.tile_pixel_z;
		P.height += z_height * Base.tile_pixel_z;

		if (!camera.intersects(P))
			return;
		// Draws the tile borders
		// NW

		if (Current_Tile.border_tiles.NW) {
			set_border_src_rect(Current_Tile, HexDirectional.NW());
			// Border.pos().x = P.x - camera.X();
			// Border.pos().y = P.y - camera.Y();
			Border.setPosition(P.x - camera.X(), P.y - camera.Y());
			Border.stamp(s_batch);
			// System.out.println(u + ", " + v + " " + Border.pos());
		}
		// NE
		if (Current_Tile.border_tiles.NE) {
			set_border_src_rect(Current_Tile, HexDirectional.NE());
			// Border.pos().x = P.x - camera.X() + (Base.tile_pixel_width / 2);
			// Border.pos().y = P.y - camera.Y();
			Border.setPosition(P.x - camera.X() + (Base.tile_pixel_width / 2),
					P.y - camera.Y());
			Border.stamp(s_batch);
			// System.out.println(u + ", " + v + " " + Border.pos());
		}
		// E
		if (Current_Tile.border_tiles.E) {
			set_border_src_rect(Current_Tile, HexDirectional.E());
			// Border.pos().x = P.x - camera.X() + (Base.tile_pixel_width / 2);
			// Border.pos().y = P.y - camera.Y() + (Base.tile_pixel_height / 4);
			Border.setPosition(P.x - camera.X() + (Base.tile_pixel_width / 2),
					P.y - camera.Y() + (Base.tile_pixel_height / 4));
			Border.stamp(s_batch);
			// System.out.println(u + ", " + v + " " + Border.pos());
		}
		// W
		if (Current_Tile.border_tiles.W) {
			set_border_src_rect(Current_Tile, HexDirectional.W());
			Border.setPosition(P.x - camera.X(), P.y - camera.Y()
					+ (Base.tile_pixel_height / 4));
			Border.stamp(s_batch);
			// System.out.println(u + ", " + v + " " + Border.pos());
		}
		// SW
		if (Current_Tile.border_tiles.SW) {
			set_border_src_rect(Current_Tile, HexDirectional.SW());
			Border.setPosition(P.x - camera.X(), P.y - camera.Y()
					+ (3 * Base.tile_pixel_height / 4));
			Border.stamp(s_batch);
			// System.out.println(u + ", " + v + " " + Border.pos());
		}
		// SE
		if (Current_Tile.border_tiles.SE) {
			set_border_src_rect(Current_Tile, HexDirectional.SE());
			Border.setPosition(P.x - camera.X() + (Base.tile_pixel_width / 2),
					P.y - camera.Y() + (3 * Base.tile_pixel_height / 4));
			Border.stamp(s_batch);
			// System.out.println(u + ", " + v + " " + Border.pos());
		}

	}

	/**
	 * Scans the map_array for all tiles, and draws those that intersect with
	 * Camera
	 * */
	private void Draw_Tiles(Draw_Object s_batch, int u, int v) {
		// int width = map_array.Width;
		// int height = map_array.Height;
		int z_height = Current_Tile.z_height();
		// int z = map_array.Tile_At(v, u).z_height;
		Rectangle P;
		// HexTile Current_Tile;

		P = initializePositionRect(u, v);
		P.y -= z_height * Base.tile_pixel_z;
		P.height += z_height * Base.tile_pixel_z;

		// Ascertain within Camera View
		if (!camera.intersects(P))
			return;

		// Current_Tile = map_array.TileAt(u, v);

		// /////TTEEMMPP impassability mapping
		// P.y -= (Base.Tile_height / 4) * Current_Tile.z_height;
		if (move_range.contains(Current_Tile)) {
			Tile_sprites.setColor(GameColor.LIGHTBLUE.clone());
		} else if (heal_range.contains(Current_Tile)) {
			Tile_sprites.setColor(GameColor.LIGHTGREEN.clone());
		} else if (special_range.contains(Current_Tile)) {
			Tile_sprites.setColor(GameColor.LIGHTGRAY.clone());
		} else if (attack_range.contains(Current_Tile)) {
			Tile_sprites.setColor(GameColor.LIGHTPINK.clone());
		} else if (view_passabilities && !Current_Tile.passabilities) {
			Tile_sprites.setColor(GameColor.LIGHTPINK.clone());
		} else if (view_passabilities && is_battler_at(Current_Tile)) {
			Tile_sprites.setColor(GameColor.LIGHTPINK.clone());
		} else {
			Tile_sprites.setColor(GameColor.WHITE.clone());
		}
		// Draws the Tile
		Tile_sprites.setopacity(255);
		Tile_sprites.Y_index = Current_Tile.Tile_Value;
		// animates the tile

		// Tile_sprites.X_index = (int) ((Base.ticks() / 8) % 3);
		Tile_sprites.setpos(new Vector2((float) P.x - camera.X(), (float) P.y
				- camera.Y()));
		// Tile_sprites.pos.Y += z_height_pixel_correction(u, v);
		Tile_sprites.draw(s_batch);
		Tile_sprites.setColor(GameColor.WHITE.clone());
		// If the SE and SW tiles are of lower height than this tile, draw walls
		HexTile current = map_array.Tile_At(u, v);
		HexTile SW = map_array.SW(current);
		HexTile SE = map_array.SE(current);
		if ((SW == null || SW.z_height() < z_height)
				|| (SE == null || SE.z_height() < z_height))
			draw_walls(s_batch, u, v, P, z_height);
	}

	private void draw_walls(Draw_Object s_batch, int u, int v, Rectangle P,
			int z_height) {
		P.y += 3 * Base.quarter_tile_pixel_height();
		// int mx = (int) ((Base.ticks() / 8) % 3);
		int my = Current_Tile.Tile_Value;
		if (Base.n_frame_check(3))
			Walls.next_frame_column();
		Walls.Y_index = my;
		Walls.setPosition((float) P.x - camera.X(), P.y - camera.Y());
		int y = Walls.gety();
		for (int i = z_height; i > 0; i--) {
			Walls.draw(s_batch);
			y += Base.quarter_tile_pixel_height();
			Walls.setY(y);
		}
	}

	/**
	 * Z height pixel correction
	 * */
	private int z_height_pixel_correction(int u, int v) {
		int max = map_array.Maximum_Z_Height();
		int z = map_array.Tile_At(v, u).z_height();
		return (max - z) * Base.tile_pixel_z;
	}

	/**
	 * Draws the grid image
	 * */
	private void Draw_Grid(HexTable map_array, Draw_Object s_batch, int u, int v) {
		if (!Base.Viewing_Grid)
			return;
		int z_height = Current_Tile.z_height();
		Rectangle P;
		P = initializePositionRect(u, v);
		P.setY(P.getY() - z_height * Base.tile_pixel_z);
		// Ascertain within Camera View
		if (!camera.intersects(P))
			return;
		else {
			// Draws the grid tile
			Grid_Tile.setX(P.x - camera.X());
			Grid_Tile.setY(P.y - camera.Y());
			Grid_Tile.draw(s_batch);
			if (Base.Viewing_Height) {
				Grid_Tile.setX(Grid_Tile.getx() + Base.tile_pixel_width / 3);
				Grid_Tile.setY(Grid_Tile.gety() + Base.tile_pixel_height / 3);
				s_batch.draw_regular_text(
						Integer.toString(map_array.Tile_At(u, v).z_height()),
						Grid_Tile.pos().x, Grid_Tile.pos().y,
						GameColor.WHITE.clone());
			}
			// temp!
		}
	}

	// /*
	// * /// /** /// Scans the map_array for all tiles with artifacts, and
	// * draws those that intersect with Camera
	// private void Draw_Artifacts(HexTable map_array, Draw_Object s_batch, int
	// u, int v) {
	// * Rectangle P; HexTile Current_Tile; if (u % 2 == 0) { P = new
	// * Rectangle((56 * v) + 12, (48 * u) + 16, 64, 64); } else { P = new
	// * Rectangle((56 * v) + 28 + 12, (48 * u) + 16, 64, 64); } P.x -= 16; P.y
	// -=
	// * 16; //Ascertain within Camera View if (!camera.Intersects(P)) return;
	// * Current_Tile = map_array.TileAt(u, v); /////////////////////////////
	// * ///Draw Tile Artifacts ///////////////////////////// if
	// * (Current_Tile.artifact_index.HasValue) { Artifacts_0.effect =
	// * ((Current_Tile.artifact_properties & HexProperty.Flip) ==
	// * HexProperty.Flip) ? SpriteEffects.None :
	// SpriteEffects.FlipHorizontally;
	// * Artifacts_0.X_index = (int)((Current_Tile.artifact_index %
	// * Artifacts_0.X_Frame_Max) ?? 0); Artifacts_0.Y_index =
	// * (int)((Current_Tile.artifact_index / Artifacts_0.X_Frame_Max) ?? 0);
	// * Artifacts_0.pos.X = (float)P.x - camera.X; Artifacts_0.pos.X +=
	// * ((Current_Tile.artifact_properties & HexProperty.X_PU) ==
	// * HexProperty.X_PU) ? 12 : -12; Artifacts_0.pos.Y = (float)P.y -
	// camera.Y;
	// * Artifacts_0.pos.Y += ((Current_Tile.artifact_properties &
	// * HexProperty.Y_PU) == HexProperty.Y_PU) ? 12 : -12;
	// * Artifacts_0.draw(s_batch); } }
	// */

	/**
	 * Scans the map_array for all tiles with Objects, and draws those that //
	 * intersect with Camera
	 * */
	private void Draw_MapObjects(HexTable map_array, Draw_Object s_batch,
			int u, int v) {
		int i;
		int s_width, s_height;
		Rectangle P;
		// HexTile Current_Tile;
		// Current_Tile = map_array.TileAt(u, v);
		if (Current_Tile.map_object_index == 0)
			return;
		else
			i = Current_Tile.map_object_index;
		s_width = (int) map_array.OS[i].sprite.width();
		s_height = (int) map_array.OS[i].sprite.height();
		P = initializePositionRect(u, v);
		P.x -= (s_width - Base.tile_pixel_width) / 2;
		P.y -= (s_height - Base.tile_pixel_height);
		P.y -= Current_Tile.z_height() * Base.tile_pixel_z;
		// Ascertain within Camera View

		// Draws the Tile Objects
		// If the cursor is within the bounds of P, fade out to half
		// transparency.
		P.x -= camera.X();
		P.y -= camera.Y();
		if (map_array.OS[i].name == "Palm_Tree_A")
			P.x = P.x;
		if (!camera.paddedwindowintersects(P))
			return;
		// P.height -= Base.tile_pixel_height ; // Most MapObjects have 16
		// pixels of padding. This
		// ensures a better fade take.
		// if(P.contains(Input.X, Input.Y)) Object_Set[i].sprite.Opacity =
		// 128;
		// else Object_Set[i].sprite.Opacity = 255;
		map_array.OS[i].sprite
				.flip((Current_Tile.map_object_properties.Flip ? true : false),
						false);
		map_array.OS[i].sprite.setX((float) P.x);
		map_array.OS[i].sprite.setY((float) P.y);
		// map_array.OS[i].sprite.Draw_Outline(s_batch, Color.Black);
		map_array.OS[i].sprite.draw(s_batch);
	}

	/**
	 * Creates a tile bounding rectangle
	 * 
	 * @param u
	 * @param v
	 * @return
	 */
	protected Rectangle initializePositionRect(int u, int v) {
		Rectangle P;
		if (u % 2 == 0) {
			P = new Rectangle(Base.tile_pixel_width * v, 3
					* Base.quarter_tile_pixel_height() * u,
					Base.tile_pixel_width, Base.tile_pixel_height);
		} else {
			P = new Rectangle((Base.tile_pixel_width * v)
					+ (Base.tile_pixel_width / 2),
					(3 * Base.quarter_tile_pixel_height() * u),
					Base.tile_pixel_width, Base.tile_pixel_height);
		}
		return P;
	}

	/**
	 * Draws the Hexobjects on the map
	 * */
	private void Draw_HexObjects(Draw_Object s_batch, int u, int v) {
		int s_width, s_height;
		Rectangle P;
		// HexTile Current_Tile;
		HexObject DrawClone;
		// Current_Tile = map_array.TileAt(u, v);
		if (Current_Tile.map_object_index != 0)
			DrawClone = hexObjectFrom(Current_Tile.map_object_index);
		else
			return;
		s_width = (int) DrawClone.sprite.width();
		s_height = (int) DrawClone.sprite.height();

		P = initializePositionRect(u, v);
		P.x -= (s_width - Base.tile_pixel_width) / 2;
		P.y -= (s_height - Base.tile_pixel_height);
		P.y -= Current_Tile.z_height() * Base.tile_pixel_z;
		P.width = s_width;
		P.height = s_height;
		// Ascertain within Camera View
		if (!camera.intersects(P))
			return;
		// ///////////////////////////
		// /Draw HexObject
		// ///////////////////////////
		DrawClone.sprite.setX((float) P.x - camera.X());
		DrawClone.sprite.setY((float) P.y - camera.Y());
		DrawClone.sprite.draw(s_batch);
	}

	/**
	 * Scans the map_array for all tiles with Objects, and draws those that //
	 * intersect with Camera
	 * */
	private void Draw_Barriers(HexTable map_array, Draw_Object s_batch, int u,
			int v) {
		int i;
		Rectangle P;
		// HexTile Current_Tile;
		// Current_Tile = map_array.Tile_At(u, v);
		if (Current_Tile.barrier_index <= 0)
			return;
		else
			i = (int) Current_Tile.barrier_index;
		// regular P determination
		P = initializePositionRect(u, v);
		// positioning P
		if ((Current_Tile.barrier_properties.X_PU)) // West;
		{
			P.x -= 16;
			P.y -= 16;
		} else if ((Current_Tile.barrier_properties.Y_PU)) // SouthEast;
		{
			P.x += 26;
			P.y -= 28;
		}
		if ((Current_Tile.barrier_properties.Z_PU)) // SothWest;
		{
			P.x -= 2;
			P.y -= 28;
		}
		P.y -= Current_Tile.z_height() * Base.tile_pixel_z;
		// Ascertain within Camera View
		if (!camera.intersects(P))
			return;
		// Draws the barriers on the tile
		Barriers_1.flip((Current_Tile.barrier_properties.Flip ? false : true),
				false);
		Barriers_1.X_index = (int) ((Current_Tile.barrier_index % Artifacts_0.X_Frame_Max));
		Barriers_1.Y_index = (int) ((Current_Tile.barrier_index / Artifacts_0.X_Frame_Max));
		Barriers_1.setPosition((float) P.x - camera.X(), P.y - camera.Y());
		// Barriers_1.Draw_Outline(s_batch, Color.BLACK);
		Barriers_1.draw(s_batch);
	}

	/**
	 * intention = 0 // Nothing intention = 1 // selecting attack intention = 2
	 * // selecting movement intention = 3 // selecting heal intention = 4 //
	 * selecting skill
	 * */
	protected static Byte intention = 0;

	/**
	 * Draws the hexcursor, indicating a selected tile the player hovers over
	 * */
	private void Draw_HexCursor(HexTable map_array, Draw_Object s_batch) {
		if (cursor_active == false)
			return;
		// if (Input.leftPressed())
		// HighLighter.fade_to(255, 8);
		// else
		// HighLighter.fade_to(128, 8);
		Rectangle P;
		int u, v;
		if (Selected_Tile != null) {
			u = Selected_Tile.Y;
			v = Selected_Tile.X;
		} else {
			u = cursor_index().Y;
			v = cursor_index().X;
		}
		Current_Tile = map_array.Tile_At(u, v);
		if (Base.n_frame_check(2))
			HighLighter.next_frame_column();
		for (HexTile tile : map_array.Range(Current_Tile, _cursor_size, true,
				false)) {
			P = initializePositionRect(tile.Y, tile.X);
			P.y -= tile.z_height() * Base.tile_pixel_z;
			P.height += tile.z_height() * Base.tile_pixel_z;
			// Ascertain within Camera View
			if (!camera.intersects(P))
				return;
			else {
				// Draws the hexcursor

				GameColor c = GameColor.BLACK.clone();
				switch (intention) {
				case 0: {
					break;
				}
				case 1: {
					c = GameColor.CRIMSON.clone();
					break;
				}
				case 2: {
					c = GameColor.NAVY.clone();
					break;
				}
				case 3: {
					c = GameColor.YELLOWGREEN.clone();
					break;
				}
				case 4: {
					c = new GameColor(119, 136, 153, 255);
					break;
				}
				}
				// HighLighter.vertstretch((float) (_cursor_size + 1));
				// HighLighter.horzstretch((float) (_cursor_size + 1));
				HighLighter.setX((float) (P.x - camera.X()));
				HighLighter.setY((float) (P.y - camera.Y()));
				// HighLighter.Draw_Outline(s_batch, c);
				HighLighter.draw(s_batch);
			}
		}
	}

	/**
	 * Draws the battler at u,v on the map, if it is view
	 * */
	public void Draw_Battler(Draw_Object s_batch, int u, int v) {
		Battler B = battler_at(Current_Tile);
		// sorted_set.RemoveAt(0);
		Rectangle P = new Rectangle(B.real_X(), B.real_Y(), B.sprite.width(),
				B.sprite.height());
		P.y -= Current_Tile.z_height() * Base.tile_pixel_z;
		if (!camera.intersects(P))
			return;
		// if (B == Active_Battler() && !Active_Battler().sprite.is_flashing())
		// {
		// Active_Battler().sprite.flash(GameColor.GRAY.clone(), 60);
		// }
		// B.sprite.blend = Blend.subtractive;
		B.sprite.setX((float) P.x - camera.X());
		B.sprite.setY((float) P.y - camera.Y());
		B.draw(s_batch, camera.coordinates());
	}

	public boolean is_battler_at(HexTile H) {
		for (Battler B : All_Battlers()) {
			if (B.Pos() == H)
				return true;
		}
		return false;
	}

	public Battler battler_at(HexTile H) {
		for (Battler B : All_Battlers()) {
			if (B.Pos() == H)
				return B;
		}
		return null;
	}

	/**
	 * Draws all the components of the map from the map_array data
	 * */
	public void draw(Draw_Object s_batch) {
		// sort_battlers();
		int width = map_array.Width();
		int height = map_array.Height();
		// preemptively animates the tile sprites
		if (Base.n_frame_check(3))
			Tile_sprites.next_frame_column();
		for (int i = 0; i < 6; i++) // used to represent the order at which
									// various Map properties are drawn
		{ // i used to designate the repetition of the cycle
			for (int u = 0; u < height; u++) {
				for (int v = 0; v < width; v++) {

					// When drawing things, draw z-level lower things first
					// (ground -> objects -> weather)
					// When drawing things, draw smaller things first
					Current_Tile = map_array.Tile_At(u, v);

					if (i == 0)
						Draw_Tiles(s_batch, u, v);

					if (i == 0)
						Draw_Tile_Borders(s_batch, u, v);
					if (i == 1)
						Draw_MapObjects(map_array, s_batch, u, v);
					if (i == 1)
						Draw_HexObjects(s_batch, u, v);
					// if (i == 2) Draw_Artifacts(map_array, s_batch, u, v);
					if (i == 3)
						Draw_Grid(map_array, s_batch, u, v);
					// if (i == 4) Draw_HexObjects(map_array, s_batch, u, v);
					if (i == 4)
						Draw_Barriers(map_array, s_batch, u, v);
					if (i == 4 && is_battler_at(Current_Tile)) {
						Draw_Battler(s_batch, u, v);
					}

				}
			}
			if (i == 0)
				Draw_HexCursor(map_array, s_batch); // outside the bounds to
													// ensure it is only drawn
													// once

			if (i == 5)
				weather.draw(s_batch);
		}
		// Draws active battle windows
		Draw_Windows(s_batch);
		// TEMP
		// s_batch.draw_smaller_text("P: " + Integer.toString(_phase), 32, 64);
	}

	/**
	 * Draws the battle windows
	 * */
	public void Draw_Windows(Draw_Object s_batch) {
		if (windowset_help.active || windowset_help.isPanning())
			windowset_help.draw(s_batch);
	}

	public void dispose() {
		Base.In_Battle = false;
		weather.dispose();
		Blur_Set_sprites.dispose();
		// commented out to allow tile sprites to be static
		// Tile_sprites.dispose();
		HighLighter.dispose();
		Grid_Tile.dispose();
		Border.dispose();
		Border.dispose();
		Walls.dispose();
		Artifacts_0.dispose();
		Barriers_1.dispose();
		for (Battler B : All_Battlers()) {
			B.dispose();
		}
	}

	public String toString() {
		return map_array.getName();
	}
}