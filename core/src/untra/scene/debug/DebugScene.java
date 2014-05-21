package untra.scene.debug;

import untra.graphics.Draw_Object;

import java.util.Scanner;

import untra.scene.IScene;
import untra.database.Database;
import untra.database.Klass;
import untra.driver.Input;

public class DebugScene implements IScene {
	private boolean refresh = true;
	private DebugWindow debugwindow = new DebugWindow();

	@Override
	public void update() {
		debugwindow.update();
		if (refresh) {
			refresh = false;
			refresh();
			return;
		}
		if (debugwindow.active && Input.leftPressed()) {
			int i = debugwindow.index;

		}
		if (debugwindow.active && Input.rightPressed()) {
			refresh = true;
		}
	}

	public void refresh() {
		debugwindow.active = false;
		Scanner in = new Scanner(System.in);
		System.out.println("SELECT DATA ITEM\n" + "A - Classes\n" + "");
		char selection = in.next().charAt(0);
		selection = Character.toUpperCase(selection);
		String[] commands;
		switch (selection) {
		case 'A': // classes
		{
			commands = new String[Database.classes.length];
			int i = 0;
			for (Klass klass : Database.classes) {
				commands[i] = klass.toString();
				System.out.println(i);
				i++;
			}
			debugwindow.commands = commands;
			debugwindow.active = true;
			break;
		}

		default:
			break;
		}

	}

	@Override
	public void draw(Draw_Object s_batch) {
		debugwindow.draw(s_batch);
	}

	@Override
	public void dispose() {
		debugwindow.dispose();

	}

}
