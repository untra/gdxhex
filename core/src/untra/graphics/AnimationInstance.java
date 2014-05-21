package untra.graphics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import untra.database.Animation;
import untra.database.Database;
import untra.driver.Audio;
import untra.driver.Base;

public class AnimationInstance extends Animation implements Disposable {

	public boolean target_del;
	public boolean target_invert;
	public Vector2 pos;
	public int target_flash_duration;
	public GameColor target_flash_color;
	private GameColor shade = GameColor.TRUEWHITE;
	private String content;
	// private int current_cue = 0;
	private int current_frame = 0;
	private SpriteSet sheet;
	private Frame[] Frames;

	public AnimationInstance(Animation a) {
		sheet = new SpriteSet("Animations/" + a.name + ".png", a.columns,
				a.rows);
		Frames = new Frame[framecount];
	}

	private void update() {
		target_flash_color = GameColor.TRUEWHITE;
		target_flash_duration = 0;
		target_invert = false;
		// update animations every 3 frames. Animations should run at 20 fps
		if (Base.n_frame_check(1))
			current_frame++;
	}

	public void Draw_Next(Draw_Object s_batch, Rectangle pos) {
		// update();
		// update is called after the animation has played.
		if (pos != null)
			this.pos = new Vector2(pos.x, pos.y);
		if (content != null)
			draw_numbers(s_batch, current_frame, content);
		else {
			// this_frame().cell_data[d, 0]//Pattern
			sheet.X_index = current_frame % columns;
			sheet.Y_index = current_frame / columns;
			float f = this_frame().scale;
			// sheet.setScale(f, f);
			// sheet.setRotation((this_frame().rotation));

			// sheet.effect =
			sheet.setopacity(this_frame().opacity);
			// this_frame().cell_data[d, 7]//Blending
			sheet.draw(s_batch);

			if (current_frame == 0 && sound != "null") {
				Audio.se_play(sound);
				/*
				 * switch (Timings[current_cue].flash_scope) { case
				 * Timing.Flash_Scope.none: { break; } case
				 * Timing.Flash_Scope.target: { target_flash_color =
				 * Timings[current_cue].flash_color ?? Color.White;
				 * target_flash_duration = Timings[current_cue].flash_duration;
				 * break; } case Timing.Flash_Scope.screen: {
				 * //Base.SCREEN.flash(timings[current_cue].flash_color,
				 * timings[current_cue].flash_duration); break; } case
				 * Timing.Flash_Scope.blink_target: { target_del = true; break;
				 * } case Timing.Flash_Scope.invert_screen: {
				 * //Base.SCREEN.color_invert(); break; } case
				 * Timing.Flash_Scope.invert_target: { target_invert = true;
				 * break; } }
				 */
				// current_cue++;
			}
		}
		update();
	}

	private void draw_numbers(Draw_Object s_batch, int number, String s) {
		int time;
		Vector2 position;
		for (int c = s.length() - 1; c >= 0; c--) {
			time = number + 2 * c;
			if (time >= 0 && number >= (s.length() - c - 1) * 2) {
				/*
				 * s_batch.Draw_Outlined_Text(s.charAt(c), number_pos(time, c),
				 * shade, new Vector2(number_scale(current_frame - 4),
				 * number_scale(current_frame)));
				 */
				position = number_pos(time, c);
				s_batch.draw_smaller_text("" + s.charAt(c), position.x,
						position.y, shade);
			}
		}
	}

	// / <summary>
	// / returns the scale of the number animation
	// / </summary>
	// / <param name="t"></param>
	// / <returns></returns>
	private float number_scale(int t) {
		switch (t) {
		case 1:
		case 7:
			return 1.125f;
		case 2:
		case 6:
			return 1.25f;
		case 3:
		case 4:
			return 1.375f;
		case 5:
			return 1.5f;
		default:
			return 1.0f;
		}
	}

	// / <summary>
	// / returns the position of the number animation
	// / </summary>
	// / <param name="t"></param>
	// / <returns></returns>
	private Vector2 number_pos(int t, int x) {
		Vector2 pos = this.pos.cpy();
		pos.x += x * 16 + Base.tile_pixel_width / 2;
		/*
		 * if (t <= Base.Pop_Damage_Frames / 2) { float k = (float)(t * Math.PI)
		 * / (Base.Pop_Damage_Frames / 2); pos.y += (float)(12 * Math.Sin(k)); }
		 * else
		 */
		pos.y += y_offest(t);
		pos.y -= Base.tile_pixel_height / 2;
		return pos;
	}

	private int y_offest(int t) {
		switch (t) {
		case 0:
			return 4;
		case 1:
			return 2;
		case 3:
		case 7:
			return -2;
		case 4:
		case 5:
		case 6:
			return -4;
		default:
			return 0;

		}
	}

	public Frame this_frame() {
		return this.Frames[current_frame];
	}

	public boolean has_next_frame() {
		if (current_frame < framecount)
			return true;
		else
			return false;
	}

	public static AnimationInstance Number(String text, GameColor c) {
		AnimationInstance a = new AnimationInstance(Database.animations[0]);
		a.content = text;
		a.shade = c;
		a.framecount = Base.Pop_Damage_Frames;
		return a;
	}

	public class Frame {
		public int x = 0;
		public int y = 0;
		public float scale = 1.0f;
		public float rotation = 1.0f;
		public int effect = 0;
		public int opacity = 255;
	}

	@Override
	public void dispose() {
		sheet.dispose();
		Frames = null;
	}
}
