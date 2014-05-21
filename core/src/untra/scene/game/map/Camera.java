package untra.scene.game.map;

import java.awt.Point;

import untra.scene.game.battle.Battler;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Base;
import untra.driver.GameMath;
import untra.driver.Input;

public class Camera {
	// private static int cwidth, cheight;
	public static int Scroll_Speed = 4;
	private Rectangle camera, paddedstaticcamera;
	private int map_width_max, map_height_max, map_z_height;
	private static int to_x, to_y;
	private static Vector2 prev_coords;
	private static int basic_height;

	public Vector2 difference() {
		float x = (float) (camera.x - prev_coords.x);
		float y = (float) (camera.y - prev_coords.y);
		return new Vector2(x, y);
	}

	public Camera(int map_width_max, int map_height_max, int map_z_max) {
		this.map_width_max = map_width_max;
		this.basic_height = map_height_max;
		this.map_z_height = map_z_max;
		refresh_map_z(map_z_max);
		camera = new Rectangle(0, 0, Base.DEFAULTWINDOWWIDTH,
				Base.DEFAULTWINDOWHEIGHT);
		paddedstaticcamera = GameMath.newinflatedRectangle(camera,
				Base.tile_pixel_width, Base.tile_pixel_height);
	}

	public void refresh_map_z(int map_z_max) {
		this.map_z_height = map_z_max;
		this.map_height_max = basic_height + (map_z_max * Base.tile_pixel_z);
	}

	public void update_normal_scroll() {
		// Scroll_Speed is the speed of the ingame map scrolling. This value can
		// be edited!
		// int Scroll_Speed = 6;
		// Scrolling by keyboard
		// if (!Base.window_in_focus())

		// If the map is scrolling, player input should not be recognized
		prev_coords = GameMath.locationPoint(camera);
		if (!is_scolling()) {
			if (true) {
				if (Input.downPressed())
					camera.y += Scroll_Speed * 3;
				if (Input.upPressed())
					camera.y -= Scroll_Speed * 3;
				if (Input.rightPressed())
					camera.x += Scroll_Speed * 3;
				if (Input.leftPressed())
					camera.x -= Scroll_Speed * 3;
			}

			// Scrolling by mouse coordinates
			if (!GameMath.inRectangle(new Rectangle(16, 16,
					Base.window_width() - 32, Base.window_height() - 32), Input
					.cursor())) {
				// The cursor is in a far edge of the screen. Time to scroll!
				if (Input.X() <= 16)
					camera.x -= Scroll_Speed; // Scroll Left
				if (Input.X() >= Base.window_width() - 32)
					camera.x += Scroll_Speed; // Scroll Right
				if (Input.Y() <= 16)
					camera.y -= Scroll_Speed; // Scroll Up
				if (Input.Y() >= Base.window_height() - 32)
					camera.y += Scroll_Speed; // Scroll Down
			}
		} else {
			scroll_camera_to();
		}

		correction();
	}

	public int X() {
		return (int) camera.x;
	}

	public int Y() {
		return (int) camera.y;
	}

	public int width() {
		return (int) camera.width;
	}

	public int height() {
		return (int) camera.height;
	}

	public Rectangle coordinates() {
		return camera;
	}

	public boolean intersects(Rectangle p) {
		return GameMath.intersects(camera, p);
	}

	public boolean paddedwindowintersects(Rectangle p) {
		return GameMath.intersects(paddedstaticcamera, p);
	}

	public boolean is_scolling() {
		if (to_x != -1 && to_y != -1)
			return true;
		else
			return false;
	}

	private void correction() {
		int xmin = Base.tile_pixel_width / 2;
		int ymin = (Base.tile_pixel_height / 4) - Base.tile_pixel_z
				* map_z_height;
		int xmax = (int) (map_width_max - camera.width - xmin);
		int ymax = (int) (map_height_max - camera.height - ymin - Base.tile_pixel_z
				* map_z_height);

		// Correction
		if (camera.x < xmin)
			camera.x = xmin;
		if (camera.y < ymin)
			camera.y = ymin;
		if (camera.x > (xmax))
			camera.x = xmax;
		if (camera.y > (ymax))
			camera.y = ymax;
		// If the camera has not moved from the previous update, disable any
		// scrolling requests.
		// This prevents hard to find errors
		if (prev_coords.x == camera.x && prev_coords.y == camera.y) {
			to_x = -1;
			to_y = -1;
		}
	}

	/**
	 * returns a rectangle specifying weather draw boundries
	 * 
	 * @return
	 */
	public static Rectangle weatherbounds() {
		Rectangle rect = new Rectangle(0, 0, Base.window_width(),
				Base.window_height());
		GameMath.inflateRectangle(rect, Base.DEFAULTWINDOWWIDTH / 10,
				Base.DEFAULTWINDOWHEIGHT / 10);
		return rect;
	}

	private void scroll_camera_to() {
		if (to_x != 0) {
			int x_mov = (int) (to_x - camera.x);
			x_mov = Math.max(Math.min(x_mov, 10), -10);
			camera.x += x_mov;
			if (!(x_mov == 10 || x_mov == -10))
				to_x = 0;
			if (camera.x == to_x)
				to_x = 0;
		}
		if (to_y != 0) {
			int y_mov = (int) (to_y - camera.y);
			y_mov = Math.max(Math.min(y_mov, 10), -10);
			camera.y += y_mov;
			if (!(y_mov != 10 || y_mov != -10))
				to_y = 0;
			if (camera.y == to_y)
				to_y = 0;
		}
	}

	/**
	 * Centers the Camera on the active battler.
	 * 
	 * @param battler
	 */
	public void center_camera_on_battler(Battler battler) {
		if (battler == null)
			return;
		int xmin = Base.tile_pixel_width / 2;
		int ymin = Base.tile_pixel_height / 4;
		to_x = Math.max(Math.min(battler.real_X()
				- (Base.DEFAULTWINDOWWIDTH / 2), map_width_max - xmin), xmin);
		to_y = Math.max(Math.min(battler.real_Y()
				- (Base.DEFAULTWINDOWHEIGHT / 2), map_height_max - ymin), ymin);
	}
}
