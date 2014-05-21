package untra.gamewindow;

import untra.graphics.Draw_Object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Audio;
import untra.driver.Base;
import untra.driver.Input;

public class SelectionWindow extends Window {

	protected Rectangle cursor_rect_appension = new Rectangle(0, 0, 0, 0);
	private Rectangle source_rect = new Rectangle(0, 64, 32, 32);
	public int index;
	protected int item_max = 1;
	protected int page = 0;
	protected int column_max = 1;
	protected int index_row, index_column = 1;
	public String[] commands;
	public boolean[] denied_access;
	private static final boolean KEYBOARD_UPDATE = false;

	public SelectionWindow(Rectangle coordinates) {
		super(coordinates);
		item_max = 0;
		commands = new String[0];
		denied_access = new boolean[0];
		index = -1;
	}

	public SelectionWindow(Rectangle coordinates, String[] cmds) {
		super(coordinates);
		item_max = cmds.length;
		commands = cmds;
		denied_access = new boolean[commands.length];
		index = 0;
	}

	protected int row_max() {
		int i = Math.max(Math.min((item_max + column_max - 1) / column_max,
				getPage_row_max()), 1);
		return i;
	}

	/**
	 * returns true if the window is the focus of the program IE is active and
	 * has a item_max > 0
	 * 
	 * @return
	 */
	public boolean control_focus() {
		if (active)
			if (item_max > 0)
				return true;
		return false;
	}

	protected int getTop_Row() {
		return (int) ocoordinates().y / 32;
	}

	protected void maintainTop_Row() {
		// checks to ensure index_row is accurate
		if (index_row < 0)
			index_row = 0;
		if (index_row > row_max() - 1)
			index_row = row_max() - 1;
		// ocoordinates.Y = coordinates.Y + row_max * 32;
	}

	/**
	 * The maximum number of rows that can be displayed in a single window,
	 * taking into account appension Not the same as the maximum number of rows
	 * 
	 * @return
	 */
	protected int getPage_row_max() {
		return (int) (coordinates.getHeight() - (BUFFER * 2) - cursor_rect_appension.y) / 32;
	}

	/**
	 * Number of items displayable on one page
	 * 
	 * @return
	 */
	protected int getPage_item_max() {
		int i = Math.max((getPage_row_max() * column_max), 1);
		i += 0;
		return i;
	}

	/**
	 * returns the maximum number of pages
	 * 
	 * @return
	 */
	protected int getPage_max() {
		return (item_max / getPage_item_max()) + 1;
	}

	/**
	 * cycles through the pages
	 * 
	 * @return
	 */
	protected void cyclePage() {
		page = (page + 1) % getPage_max();
	}

	/**
	 * Update cursor rectangle
	 */
	protected void Update_cursor_rect() {
		// If selection cursor is at an item less than zero
		if (index < 0) {
			// should dispose of the selectable. In the meantime, it will just
			// reset index to 0;
			index = 0;
			// return;
			Update_cursor_rect();
			return;
		}
		index_row = (index % getPage_item_max()) / column_max; // sets index_row
		if (index_row < getTop_Row()) // if index_row is at the top, scroll
										// content
		{
			maintainTop_Row();
		}
		// calculate cursor width
		cursor_rect.setWidth((coordinates.getWidth() - cursor_rect_appension
				.getX())
				/ column_max
				- (BUFFER * 2)
				+ cursor_rect_appension.getWidth());
		cursor_rect.setHeight(32 + cursor_rect_appension.getHeight());
		// calculate coordinates
		cursor_rect.setX((index % getPage_item_max()) / row_max()
				* (cursor_rect.getWidth() + (BUFFER * 2))
				+ ((int) ocoordinates().x) + cursor_rect_appension.getX());
		cursor_rect.setY((index % getPage_item_max()) % row_max()
				* (BUFFER * 2) + ((int) ocoordinates().y)
				+ cursor_rect_appension.y);
	}

	protected void drawCursorRect(Draw_Object s_batch) {
		float x = cursor_rect.getX();
		float y = cursor_rect.getY();
		float x2 = cursor_rect.getWidth();
		float y2 = cursor_rect.getHeight();
		int srcx = (int) source_rect.getX();
		int srcy = (int) source_rect.getY();
		int srcx2 = (int) source_rect.getWidth();
		int srcy2 = (int) source_rect.getHeight();
		// Draw the cursor rectangle
		if (active == false || item_max == 0)
			return;
		// s_batch.draw(borderTexture, x, y, x2, y2, srcx, srcy, srcx2, srcy2);
		s_batch.draw(borderTexture, x, y, x2, y2, srcx, srcy, srcx2, srcy2,
				false, false);
		// selectionSprite.setPosition(x, y);

	}

	@SuppressWarnings("unused")
	public void update() {
		super.update();
		int originalindex = index;
		if (active == false)
			return;
		if (Input.triggerC() && item_max > 0) {
			Audio.SE_Cursor.play();
			cyclePage();
		}
		mouse_operations();
		if (item_max > 0 && index >= 0 && KEYBOARD_UPDATE) {
			// DOWN
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				if ((column_max == 1 || (index < item_max - column_max))) {
					// Audio.SE_Cursor.play();
					index = (index + column_max) % item_max;
				}
			}
			// Up
			if (Gdx.input.isKeyPressed(Keys.UP)) {
				if ((column_max == 1 || (index >= column_max))) {
					// Audio.SE_Cursor.play();
					index = (index - column_max + item_max) % item_max;
				}
			}
			// Right
			if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				if ((column_max >= 2 && index < item_max - 1)) {
					// Audio.SE_Cursor.play();
					index += 1;
				}
			}
			// Left
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				if ((column_max >= 2 && index > 0)) {
					// Audio.SE_Cursor.play();
					index -= 1;
				}
			}
			// TODO: Add page jumping for large choice menus and controlling
			// selectables
		}
		// plays the cursor movement audio if the index has changed
		if (index != originalindex)
			Audio.SE_Cursor.play();
		// TODO: update help text
		// If help_window != null
		// help_window.update

		// update cursor rectangle
		Update_cursor_rect();
	}

	/**
	 * Draws the selection window and general commands if the class is not
	 * inherited
	 */
	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		// draws the selection rectangle
		drawCursorRect(s_batch);
		// If the class is instantiated, the instantiated class is responsible
		// for drawing the battle commands
		if ((this instanceof SelectionWindow))
			return;
		for (int i = 0; i < item_max; i++) {
			if (denied_access[i] == true) {
				draw_item(s_batch, i, DISABLED_COLOR);
			} else {
				draw_item(s_batch, i, NORMAL_COLOR);
			}
		}
	}

	/**
	 * mouse operations changes the index if the cursor is occupying the space
	 * of a different selection
	 */
	private void mouse_operations() {
		float mx = Input.X() - ocoordinates().x - cursor_rect_appension.getX();
		float my = Input.Y() - ocoordinates().y - cursor_rect_appension.getY();
		// if the cursor is outside the bounds
		if ((mx < 0) || (my < 0))
			return;
		int w = (int) (coordinates.getWidth() - cursor_rect_appension.getX())
				/ column_max;// -cursor_rect.getHeight();
		for (int i = 0; i < item_max; i++) {
			int x = (i / row_max()) * (w);
			int y = (i % row_max()) * 32;
			int oringalindex = index;
			if ((index != i + page * getPage_item_max()) && (mx > x)
					&& (mx < x + w) && (my > y)
					&& (my < y + cursor_rect.getHeight())) {

				index = i + (page * getPage_item_max());
				index = Math.min(index, item_max - 1);
				// if the cursor moved, play the sfx
				if (index != oringalindex)
					Audio.SE_Cursor.play();
			}
		}
	}

	/**
	 * draws the command name at index i with the specified color
	 * 
	 * @param s_batch
	 * @param i
	 * @param c
	 */
	private void draw_item(Draw_Object s_batch, int i, Color c) {
		// text_color = c;
		Vector2 pos = new Vector2((float) ocoordinates().x + 4,
				(float) ocoordinates().y + (32 * i)); // TODO: Fix the
														// multiplier for
														// command sets with
														// multiple columns
		// draw_text(s_batch, Base.default_font, Commands[i], pos, c);
		pos.y = Base.window_height() - pos.y - 8;
		s_batch.draw_regular_text(commands[i], pos.x, pos.y);
	}

	/**
	 * disables selection of a particular item, adding to the list of selections
	 * denied access
	 * 
	 * @param i
	 */
	public void disable_item(int i) {
		if (i >= commands.length)
			return;
		if (i < 0)
			return;
		denied_access[i] = true;
	}

}
