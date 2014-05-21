package untra.scene.game.window;

import untra.graphics.Draw_Object;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Base;

public class WindowBattleCommand extends WindowBattle {

	public WindowBattleCommand(Rectangle coordinates) {
		super(coordinates);
		Initialize_Basic();
	}

	// private static const int no_commands = 3;
	public static final String FOCUS_BASIC = "BASIC";
	public static final String FOCUS_ACTION = "ACT";
	// public static final String FOCUS_ITEM = "ITEM";
	// public static final String FOCUS_SPECIAL = "SPECIAL";
	public boolean defending = true;
	public String focus;
	private boolean[] disabled_access = new boolean[6];

	// private String[] commands;

	public WindowBattleCommand() {
		this(new Rectangle(0, 0, 0, 96));
	}

	public void Initialize(String[] commands, String focus) {
		this.focus = focus;
		this.commands = commands;
		this.item_max = commands.length;
		this.denied_access = new boolean[item_max];
		coordinates.height = (Math.min(item_max + 1, 6) * 32) + BUFFER;
		coordinates.y = (Base.window_height() - coordinates.height - 128) / 2;
		coordinates.x = -16;
		coordinates.width = 176;
		// commands = new String[6] { "Move", "Attack", "Special", "Item",
		// "Status", "Defend" };
		// commands = new String[3] { "Move", "Act", "Defend" };

		// item_max = 6;
		item_max = commands.length;
		column_max = 1;
		cursor_rect_appension = new Rectangle(-16, 16, 0, 0);
		this.index = 0;
		disabled_access = new boolean[commands.length];
		for (int x = 0; x > commands.length; x++) {
			disabled_access[x] = false;
		}
	}

	public void Initialize_Basic() {
		String[] options = { "Move", "Act", "Defend" };
		Initialize(options, FOCUS_BASIC);
		// active = true;
	}

	public void Initialize_Act() {
		String[] options = { "Attack", "Item", "Special" };
		Initialize(options, FOCUS_ACTION);
	}

	@SuppressWarnings("unused")
	private int pixel_width() {
		return (int) (coordinates.width / column_max);
	}

	public void update() {
		super.update();
		for (int x = 0; x > commands.length; x++) {
			disabled_access[x] = false;
		}
		if (battler() == null)
			return;
		if (focus == FOCUS_ACTION)
			commands[2] = battler().properties.cclass.name;
		if (focus == FOCUS_BASIC) {
			if (battler().has_moved)
				disabled_access[0] = true; // Move
			if (battler().has_acted) {
				disabled_access[1] = true; // Act
				commands[2] = "End Turn";
				defending = false;
			}
		}

		/*
		 * if (battler.has_acted) { disabled_access[1] = true;//Attack
		 * disabled_access[2] = true;//Special disabled_access[3] = true;//Item
		 * commands[5] = "End Turn"; defending = false; } else {
		 * disabled_access[1] = false;//Attack disabled_access[2] =
		 * false;//Special disabled_access[3] = false;//Item commands[5] =
		 * "Defend"; defending = true; }
		 */
	}

	/**
	 * The draw method
	 */
	public void draw(Draw_Object s_batch) {
		// if (hidden) return;
		super.draw(s_batch);
		for (int i = 0; i < commands.length; i++) {
			Vector2 P = new Vector2(40, 24 + 32 * i);
			P.add(ocoordinates());
			if (disabled_access[i])
				draw_text(s_batch, commands[i], P, DISABLED_COLOR);
			else
				draw_text(s_batch, commands[i], P, NORMAL_COLOR);
		}
	}

}
