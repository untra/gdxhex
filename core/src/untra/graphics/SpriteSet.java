package untra.graphics;

import com.badlogic.gdx.math.Rectangle;

public class SpriteSet extends Sprite {

	public int X_Frame_Max, Y_Frame_Max;
	public int X_index = 0;
	public int Y_index = 0;

	/**
	 * Spriteset
	 * 
	 * @param path
	 *            file path from root graphics folder
	 * @param columns
	 *            columns to cut sprite
	 * @param rows
	 *            rows of sprites to cut
	 */
	public SpriteSet(String path, int columns, int rows) {
		super(path);
		if (columns < 1 || rows < 1) {
			throw new IndexOutOfBoundsException("Sprite_Set " + this
					+ " has too few rows or columns");
		}
		X_Frame_Max = columns;
		Y_Frame_Max = rows;
	}

	public float width() {
		return (int) (super.width() / X_Frame_Max);
	}

	public float height() {
		return (int) (super.height() / Y_Frame_Max);
	}

	public Rectangle getsrc_rect() {
		return new Rectangle(X_index * width(), Y_index * height(), width(),
				height());

	}

	public void update() {
		super.update();
		if ((X_index >= X_Frame_Max) || (Y_index >= Y_Frame_Max)) {
			throw new IndexOutOfBoundsException("Index out of bounds: X ("
					+ X_index + " / " + X_Frame_Max + "), Y (" + Y_index
					+ " / " + Y_Frame_Max + ")");
		}
		src_rect(getsrc_rect());
	}

	public void draw(Draw_Object s_batch) {
		update();
		super.draw(s_batch);

	}

	/**
	 * increments the x index frame
	 */
	public void next_frame_column() {
		if (X_index < X_Frame_Max - 1)
			X_index++;
		else
			X_index = 0;
	}

	/**
	 * increments the y index frame
	 */
	public void next_frame_row() {
		if (Y_index < Y_Frame_Max - 1)
			Y_index++;
		else
			Y_index = 0;
	}

}
