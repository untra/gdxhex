package untra.scene.title;

import untra.gamewindow.SelectionWindow;
import untra.graphics.Draw_Object;
import untra.graphics.SpriteSet;
import untra.scene.IScene;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import untra.driver.Audio;
import untra.driver.Base;

public class TitleScene implements IScene, Disposable {

	boolean continue_enabled = false;
	String c0 = "New Game";
	String c1 = "Continue";
	String c2 = "End";
	String[] cmds = new String[] { c0, c1, c2 }; // cmds array of string options
													// 550 580 192
	// SelectionWindow command_window = new SelectionWindow(new
	// Rectangle((Base.Window_width / 2 - 96), (Base.Window_height / 2 - 96),
	// 192, cmds.length * 3));
	SelectionWindow command_window = new SelectionWindow(new Rectangle(32, 32,
			64, 64), cmds);
	SpriteSet hexagonSet = new SpriteSet("Sprites/FB.png", 4, 9);

	public TitleScene() {
		init();
	}

	public void init() {
		System.out.println("Initializing Title Scene...");
		// TODO: Check to see if any save files exist. If so, enable Continue
		if (continue_enabled == false)
			command_window.disable_item(1);
		else
			command_window.index = 1;
		//
		command_window.active = true;
		// command_window.opacity = 191;
		// Play title BGM
		Audio.bgm_play(Base.BGM_Title);
		Audio.BGS_stop();
		System.out.println("Title Scene Initializaion Great Success!");
		// TEMP
		hexagonSet.setpos(new Vector2(128, 256));
		hexagonSet.X_index = 0;
		hexagonSet.Y_index = 2;
	}

	@Override
	public void draw(Draw_Object s_batch) {
		command_window.draw(s_batch);
		hexagonSet.draw(s_batch);
	}

	public void update() {

	}

	public void cmd_new_game() {
		Audio.SE_Selection.play();
		// Base.SCENE = new
	}

	public void cmd_continue() {
		Audio.SE_Selection.play();
	}

	public void cmd_end() {
		Audio.SE_Selection.play();
		System.exit(0); // Note: setting Base.SCENE to null will ALWAYS exit the
						// game.
		// This is both to prevent errors in the main code, and have an easy
		// exit method.
	}

	public void dispose() {
		// Nothing to dispose
	}

}
