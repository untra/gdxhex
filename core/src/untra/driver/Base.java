package untra.driver;

import untra.gamewindow.Window;
import untra.graphics.Draw_Object;

import java.util.Random;
import java.util.Scanner;

import untra.player.Actor;
import untra.player.Party;
import untra.scene.IScene;
import untra.scene.game.battle.BattleScene;
import untra.scene.game.editor.EditingScene;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import untra.database.Database;
import untra.database.Klass;

public class Base implements ApplicationListener {

	public static final String Title = "Hexagons";
	private static IScene game_scene;
	public static BitmapFont regularFont;
	private Draw_Object s_batch;
	private AssetManager manager;
	private OrthographicCamera oCam;
	public static Random RANDOMIZER;
	public static final int DEFAULTWINDOWWIDTH = 800;
	public static final int DEFAULTWINDOWHEIGHT = 480;
	// public static Rectangle Camera = new Rectangle(0, 0, Window_width,
	// Window_height);
	public static final String cursor_texture_name = "Arrow";
	public static final String windowskin_texture_border = "W_skin";
	public static final String windowskin_texture_background = "Backcolor";
	public static final String default_font_name = "m16.fnt";
	public static final String small_font_name = "m12.fnt";
	public static final int Magic_Number = 256;
	public static final int tile_pixel_width = 56; // minimum map width is 23
	public static boolean DEBUG = false;
	public static boolean FRIENDLY_FIRE = true;
	public static int quarter_tile_pixel_width() {
		return tile_pixel_width / 4;
	}

	public static int quarter_tile_pixel_height() {
		return tile_pixel_height / 4;
	}

	public static final int tile_pixel_height = 64; // minimum map height is 24
	public static final int tile_pixel_z = 16;
	public static final int FrameRate = 60; // NOTE: other components of
											// software assume this is the
											// standard.
	/**
	 * Indicates the number of animation frames the animation plays on the
	 * screen. Should be less than 20
	 */
	public static final byte Pop_Damage_Frames = 13;

	// Temp
	public static boolean Viewing_Grid = false;
	public static boolean Viewing_Height = false;
	// public static System.Diagnostics.Stopwatch StopWatch = new
	// System.Diagnostics.Stopwatch();
	public static final boolean USING_POST_ACTION_PROCESSING = false;

	// Input
	public static int save_count = 0;
	public static final boolean using_cursor = true;
	public static final boolean using_dir4 = true; // dir4 is for four
													// directional
													// game Keys. Default is
													// true
	public static final boolean using_dir8 = false; // dir8 is for eight
													// directional game Keys.
													// Default false
	public static final boolean using_WASD = true; // WASD int

	public static final int A1key = Keys.ENTER;
	public static final int B1key = Keys.ESCAPE;
	public static final int A2key = Keys.L;
	public static final int B2key = Keys.K;
	public static final int A3key = Keys.Z;
	public static final int B3key = Keys.X;
	public static final int X1key = Keys.P;
	public static final int Y1key = Keys.O;
	public static final int X2key = Keys.Q;
	public static final int Y2key = Keys.E;
	public static final int DOWNKEY = Keys.DOWN;
	public static final int UPKEY = Keys.UP;
	public static final int LEFTKEY = Keys.LEFT;
	public static final int RIGHTKEY = Keys.RIGHT;
	// Enumerations

	// BGM filenames
	public static final String BGM_Title = "Quiet_Forest.ogg";

	// Ingame Data
	public static Party party;
	public static boolean In_Battle = false;
	// private SelectionWindow tempSelectionWindow;
	// private Sprite sausageSprite;
	private static double _ticks;
	public static int wait = 0;
	public static StringBuilder debugTitle = new StringBuilder(Title);

	/**
	 * Initialization method for the main body of the program. Here all init()
	 * calls are made and other variables initialized. Many things created here
	 * are simmarily destroyed in the destructor at the end of the program
	 */
	public void create() {
		Gdx.graphics.setDisplayMode(DEFAULTWINDOWWIDTH,
				DEFAULTWINDOWHEIGHT,
                false);
		Draw_Object.init();
		Input.init();
		Window.init();
		Audio.init();
		Database.init();
		// camera initialization
		oCam = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		oCam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		s_batch = new Draw_Object();
		s_batch.enableBlending();
		s_batch.setProjectionMatrix(oCam.combined);
		// party initialization
		party = new Party();
		Actor actor = new Actor(Klass.K_MONK(), 15);
		//actor.skills.add(Database.skills[1]);
		party.Members.add(actor);
		//
		RANDOMIZER = new Random();
		manager = new AssetManager();
		regularFont = new BitmapFont();

		_ticks = 0;
		// oCam must be initially updated as well.
		oCam.update();
		if (DEBUG)
			game_scene = new EditingScene();
		else
			game_scene = new BattleScene();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		System.out.println("Game Finalizing...");
		s_batch.dispose();
		manager.clear();
		// game_scene.dispose();
		System.out.println("Game Finalization Great Success!");
		System.out.println("Goodbye! <3");

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/**
	 * This method is called multiple times a second, and is the shell for the
	 * standard update and draw methods.
	 */
	public void render() {
		// Gnarly Hardware stuff here
		Gdx.graphics.getGL20().glClearColor(.392f, .583f, .929f, 1.0f);
		Gdx.graphics.getGL20().glClear(
				GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		// My code
		update();

		draw(s_batch);

	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void resume() {
		// TODO Auto-generated method stub

	}

	private void update() {
		increment_ticks();
		oCam.update();
		Input.update();
		// If the game is not in any scene, this signals the program to exit
		if (game_scene == null) {
			// shutdown signal caught. dispose of processes and exit
			// disposes of resources
			this.dispose();
			System.exit(0);
			// If the wait flag has been raised, prevents standard processing
			// for the turn and decrements the counter
		} else if (wait > 0)
			wait--;
		// otherwise the scene is updated
		else {
			game_scene.update();
			// tempSelectionWindow.update();
		}

	}

	private void draw(Draw_Object s_batch) {

		s_batch.begin();
		// updateDebugTitle();
		draw_SCENE(s_batch, game_scene);
		regularFont.setColor(Color.WHITE);

		// s_batch.draw_smaller_text("X: " + Integer.toString(Input.X()), 32,
		// 32);
		// s_batch.draw_smaller_text("Y: " + Integer.toString(Input.Y()), 32,
		// 16);
		// s_batch.draw_smaller_text(
		// "F: " + Integer.toString(Gdx.graphics.getFramesPerSecond()),
		// 32, 48);
		Input.draw(s_batch);
		s_batch.end();
		s_batch.flush();
	}

	public void updateDebugTitle() {
		debugTitle = new StringBuilder(Title + " - "
				+ Integer.toString(Gdx.graphics.getFramesPerSecond()) + " FPS");
	}

	/**
	 * update_SCENE updates the current Scene interface
	 * 
	 * @param Scene
	 */
	void update_SCENE(IScene Scene) {
		if (Scene == null)
			return;
		Scene.update();
	}

	/**
	 * Draw_SCENE performs the draw method on parameter Scene
	 * 
	 * @param spriteBatch
	 * @param Scene
	 */
	void draw_SCENE(Draw_Object spriteBatch, IScene Scene) {
		if (Scene == null)
			return;
		Scene.draw(spriteBatch);
	}

	public static void setScene(IScene scene) {
		if (scene != null)
			System.out.println("SCENE SET TO: " + scene.toString());
		else
			System.out.println("SCENE SET TO: null. Program exiting.");
		game_scene.dispose();
		game_scene = scene;
	}

	/**
	 * returns the number of "ticks" or elapsed frame updates since the
	 * beginning of the program. Used as a guage for measuring elapsed frames.
	 * 
	 * @return elapsed frames
	 */
	public static long ticks() {
		return (long) _ticks;
	}

	// private double rounded_ticks = 0.0f;

	private void increment_ticks() {
		update_checks();
		_ticks++;
	}

	/**
	 * Returns the current width of the gamewindow
	 * 
	 * @return
	 */
	public static int window_width() {
		return Gdx.graphics.getWidth();
	}

	/**
	 * Returns the current height of the gamewindow
	 * 
	 * @return
	 */
	public static int window_height() {
		return Gdx.graphics.getHeight();
	}

	// its a curse I tell you, a curse.

	private static float[] frame_values = new float[20];
	private static boolean[] frame_checks = new boolean[20];

	/**
	 * Returns true if the ith twentieth of a second (n x 3 frames at 60 fps)
	 * has elapsed in the current tick. This is used for updating sprites and
	 * ensuring graphics are updated a consistant rate
	 * 
	 * @param i
	 * @return
	 */
	public static boolean n_frame_check(int i) {
		if (i <= 0 || i > 20)
			return false;
		return frame_checks[i - 1];
	}

	private static void update_checks() {
		float d = 3.0f; // 1/20 of a second
		float fps = (float) Gdx.graphics.getFramesPerSecond();
		float f = (float) (60.0f / fps);
		f = Math.min(f, d);
		for (int i = 0; i < 20; i++) {
			frame_values[i] += f;
			if (frame_values[i] >= (i + 1) * d) {
				frame_values[i] -= Math.max((i + 1) * d, f);
				frame_checks[i] = true;
			} else {
				frame_checks[i] = false;
			}
		}
		d += 5;
	}

	private static void debug() {
		Scanner in = new Scanner(System.in);
		System.out.println("DEBUG EDITOR ENABLED");
		System.out.println("1: Klass editor\n"
				+ "any other entry exits the application");
		System.out.println("@> ");
		int input = in.nextInt();
		switch (input) {
		case 1:

			break;

		default:
			game_scene = null;
			break;
		}
	}

}
