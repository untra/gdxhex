package untra.driver;

import untra.graphics.Draw_Object;
import untra.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Input {
	private static boolean[] keystate = new boolean[255];
	private static boolean[] prevkeystate = new boolean[255];
	private static final boolean DRAWCUSTOMCURSOR = false;
	private static final String CURSORPATH = "Misc/Arrow.png";
	private static boolean leftMouse = false;
	private static boolean rightMouse = false;
	private static boolean midMouse = false;
	private static boolean prevMidMouse = false;
	private static boolean prevLeftMouse = false;
	private static boolean prevRightMouse = false;
	private static Sprite cursorSprite;

	public static void update() {
		// prevkeystate = keystate;
		prevLeftMouse = leftMouse;
		prevRightMouse = rightMouse;
		prevMidMouse = midMouse;
		midMouse = Gdx.input.isButtonPressed(2);
		leftMouse = Gdx.input.isButtonPressed(0);
		rightMouse = Gdx.input.isButtonPressed(1);
		cursorSprite.setpos(cursorpos());
		for (int i = 0; i < 255; i++) {
			prevkeystate[i] = keystate[i];
			keystate[i] = Gdx.input.isKeyPressed(i);
		}
		/*
		 * if (Mouse.isInsideWindow()) Gdx.input.setCursorCatched(true);
		 * 
		 * else { Gdx.input.setCursorCatched(false); }
		 */

	}

	public static void init() {
		System.out.println("Input Initializing...");

		try {
			cursorSprite = new Sprite(CURSORPATH);
			System.out.println("Input Initialization Great Success!");
		} catch (Exception e) {
			System.out.println("Input Initialization Failed!");
			e.printStackTrace();
		}
	}

	public static int X() {
		return Gdx.input.getX();
	}

	public static int Y() {
		return Gdx.input.getY();
	}

	public static Vector2 cursor() {
		return new Vector2(X(), Y());
	}

	public static Vector2 cursorpos() {
		return new Vector2(X(), Y());
	}

	public static boolean downPressed() {
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			return true;
		return false;
	}

	public static boolean rightPressed() {
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			return true;
		return false;
	}

	public static boolean leftPressed() {
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			return true;
		return false;
	}

	public static boolean upPressed() {
		if (Gdx.input.isKeyPressed(Keys.UP))
			return true;
		return false;
	}

	/**
	 * Indicates wheather the "alternate" button has been pressed in menu
	 * navigation
	 * 
	 * @return boolean
	 */
	public static boolean triggerC() {
		// if the left mouse button is pressed
		if (triggerMiddleMouse() || isKeyTrigger(Keys.TAB))
			return true;
		return false;
	}

	/**
	 * Indicates wheather the "back" button has been pressed in menu navigation
	 * 
	 * @return boolean
	 */
	public static boolean triggerB() {
		// if the right mouse button is pressed
		if (triggerRightMouse())
			return true;
		return false;
	}

	/**
	 * Indicates wheather the "forward" button has been pressed in menu
	 * navigation
	 * 
	 * @return boolean
	 */
	public static boolean triggerA() {
		// if the left mouse button is pressed
		if (triggerLeftMouse())
			return true;
		return false;
	}

	/**
	 * returns true if the left mouse button is currently being held down
	 * 
	 * @return
	 */
	public static boolean LeftMousePressed() {
		return leftMouse;
	}

	/**
	 * returns true if the left mouse button was just released. This is not the
	 * same as not being pressed down. It returns true only at the instant the
	 * left mouse button is released
	 * 
	 * @return
	 */
	public static boolean LeftMouseReleased() {
		return !leftMouse && prevLeftMouse;
	}

	/**
	 * returns true if the right mouse button was just released. This is not the
	 * same as not being pressed down. It returns true only at the instant the
	 * right mouse button is released
	 * 
	 * @return
	 */
	public static boolean RightMouseReleased() {
		return !rightMouse && prevRightMouse;
	}

	/**
	 * returns true if the right mouse button is currently being held down
	 * 
	 * @return
	 */
	public static boolean RightMousePressed() {
		return rightMouse;
	}

	public static boolean triggerLeftMouse() {
		return (leftMouse && !prevLeftMouse);
	}

	public static boolean triggerMiddleMouse() {
		return (midMouse && !prevMidMouse);
	}

	public static boolean triggerRightMouse() {
		return (rightMouse && !prevRightMouse);
	}

	public static boolean isPressed(int k) {
		return (keystate[k]);
	}

	public static boolean isKeyTrigger(int k) {
		return (keystate[k] && !prevkeystate[k]);
	}

	public static boolean cursorInRectangle(Rectangle r) {
		return GameMath.inRectangle(r, cursor());
	}

	/**
	 * returns a text string indicative of whatever text is input by the user.
	 * 
	 * @return
	 */
	public static String textInput() {
		String in = "";
		if (isPressed(Keys.SHIFT_LEFT) || isPressed(Keys.SHIFT_RIGHT)) {
			in += isKeyTrigger(Keys.A) ? "A" : "";
			in += isKeyTrigger(Keys.B) ? "B" : "";
			in += isKeyTrigger(Keys.C) ? "C" : "";
			in += isKeyTrigger(Keys.D) ? "D" : "";
			in += isKeyTrigger(Keys.E) ? "E" : "";
			in += isKeyTrigger(Keys.F) ? "F" : "";
			in += isKeyTrigger(Keys.G) ? "G" : "";
			in += isKeyTrigger(Keys.H) ? "H" : "";
			in += isKeyTrigger(Keys.I) ? "I" : "";
			in += isKeyTrigger(Keys.J) ? "J" : "";
			in += isKeyTrigger(Keys.K) ? "K" : "";
			in += isKeyTrigger(Keys.L) ? "L" : "";
			in += isKeyTrigger(Keys.M) ? "M" : "";
			in += isKeyTrigger(Keys.N) ? "N" : "";
			in += isKeyTrigger(Keys.O) ? "O" : "";
			in += isKeyTrigger(Keys.P) ? "P" : "";
			in += isKeyTrigger(Keys.Q) ? "Q" : "";
			in += isKeyTrigger(Keys.R) ? "R" : "";
			in += isKeyTrigger(Keys.S) ? "S" : "";
			in += isKeyTrigger(Keys.T) ? "T" : "";
			in += isKeyTrigger(Keys.U) ? "U" : "";
			in += isKeyTrigger(Keys.V) ? "V" : "";
			in += isKeyTrigger(Keys.W) ? "W" : "";
			in += isKeyTrigger(Keys.X) ? "X" : "";
			in += isKeyTrigger(Keys.Y) ? "Y" : "";
			in += isKeyTrigger(Keys.Z) ? "Z" : "";
			in += isKeyTrigger(Keys.NUM_1) ? "!" : "";
			in += isKeyTrigger(Keys.NUM_2) ? "@@" : "";
			in += isKeyTrigger(Keys.NUM_3) ? "#" : "";
			in += isKeyTrigger(Keys.NUM_4) ? "$" : "";
			in += isKeyTrigger(Keys.NUM_5) ? "%" : "";
			in += isKeyTrigger(Keys.NUM_6) ? "^" : "";
			in += isKeyTrigger(Keys.NUM_7) ? "&" : "";
			in += isKeyTrigger(Keys.NUM_8) ? "*" : "";
			in += isKeyTrigger(Keys.NUM_9) ? "(" : "";
			in += isKeyTrigger(Keys.NUM_0) ? ")" : "";
			in += isKeyTrigger(Keys.COMMA) ? "<" : "";
			in += isKeyTrigger(Keys.PERIOD) ? ">" : "";
			in += isKeyTrigger(Keys.SEMICOLON) ? ":" : "";
			in += isKeyTrigger(Keys.SLASH) ? "?" : "";
			// in += isKeyTrigger(Keys.APOSTROPHE) ? "" : "";
		} else {
			in += isKeyTrigger(Keys.A) ? "a" : "";
			in += isKeyTrigger(Keys.B) ? "b" : "";
			in += isKeyTrigger(Keys.C) ? "c" : "";
			in += isKeyTrigger(Keys.D) ? "d" : "";
			in += isKeyTrigger(Keys.E) ? "e" : "";
			in += isKeyTrigger(Keys.F) ? "f" : "";
			in += isKeyTrigger(Keys.G) ? "g" : "";
			in += isKeyTrigger(Keys.H) ? "h" : "";
			in += isKeyTrigger(Keys.I) ? "i" : "";
			in += isKeyTrigger(Keys.J) ? "j" : "";
			in += isKeyTrigger(Keys.K) ? "k" : "";
			in += isKeyTrigger(Keys.L) ? "l" : "";
			in += isKeyTrigger(Keys.M) ? "m" : "";
			in += isKeyTrigger(Keys.N) ? "n" : "";
			in += isKeyTrigger(Keys.O) ? "o" : "";
			in += isKeyTrigger(Keys.P) ? "p" : "";
			in += isKeyTrigger(Keys.Q) ? "q" : "";
			in += isKeyTrigger(Keys.R) ? "r" : "";
			in += isKeyTrigger(Keys.S) ? "s" : "";
			in += isKeyTrigger(Keys.T) ? "t" : "";
			in += isKeyTrigger(Keys.U) ? "u" : "";
			in += isKeyTrigger(Keys.V) ? "v" : "";
			in += isKeyTrigger(Keys.W) ? "w" : "";
			in += isKeyTrigger(Keys.X) ? "x" : "";
			in += isKeyTrigger(Keys.Y) ? "y" : "";
			in += isKeyTrigger(Keys.Z) ? "z" : "";
			in += isKeyTrigger(Keys.NUM_1) ? "1" : "";
			in += isKeyTrigger(Keys.NUM_2) ? "2" : "";
			in += isKeyTrigger(Keys.NUM_3) ? "3" : "";
			in += isKeyTrigger(Keys.NUM_4) ? "4" : "";
			in += isKeyTrigger(Keys.NUM_5) ? "5" : "";
			in += isKeyTrigger(Keys.NUM_6) ? "6" : "";
			in += isKeyTrigger(Keys.NUM_7) ? "7" : "";
			in += isKeyTrigger(Keys.NUM_8) ? "8" : "";
			in += isKeyTrigger(Keys.NUM_9) ? "9" : "";
			in += isKeyTrigger(Keys.NUM_0) ? "0" : "";
			in += isKeyTrigger(Keys.COMMA) ? "," : "";
			in += isKeyTrigger(Keys.PERIOD) ? "." : "";
			in += isKeyTrigger(Keys.SEMICOLON) ? ";" : "";
			in += isKeyTrigger(Keys.SLASH) ? "/" : "";
			// in += isKeyTrigger(Keys.BACKSPACE) ? "\b" : "";
		}
		return in;
	}

	public static void draw(Draw_Object s_batch) {
		if (DRAWCUSTOMCURSOR)
			cursorSprite.draw(s_batch);
	}
}
