package untra.scene.game.map;

import untra.graphics.Draw_Object;
import untra.graphics.GameColor;
import untra.graphics.Sprite;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Audio;
import untra.driver.Base;

public class Weather {

	public enum Forecast {
		none, rain, storm, snow, hail, thunderstorm, falling_autumn, blowing_autumn, swirling_autumn, falling_leaves, sakura
	}

	private static Random RANDOMIZER = new Random();
	private static final int MAX_SPRITES = 160;
	private static final int THUNDERWAIT = 320000;
	private static Sprite[] sprites = new Sprite[MAX_SPRITES];
	private static short[] info = new short[MAX_SPRITES];
	private static int index = 0;
	private static final String THUNDER_SE = "Thunderclap";

	// colors
	private static final GameColor semi_transparent = new GameColor(255, 255,
			255, 128);
	private static final GameColor bluegray = new GameColor(215, 227, 227, 150);
	private static final GameColor gray = new GameColor(217, 217, 217, 150);
	private static final GameColor lightgray = new GameColor(233, 233, 233, 250);
	private static final GameColor lightblue = new GameColor(222, 239, 243, 250);
	private static final GameColor lightpink = new GameColor(255, 167, 192, 255);
	private static final GameColor darkpink = new GameColor(215, 106, 136, 255);
	private static final GameColor brightorange = new GameColor(248, 88, 0, 255);
	private static final GameColor orangebrown = new GameColor(144, 80, 56, 255);
	private static final GameColor burntred = new GameColor(152, 0, 0, 255);
	private static final GameColor paleorange = new GameColor(233, 160, 128,
			255);
	private static final GameColor darkbrown = new GameColor(72, 40, 0, 255);
	private static final GameColor darkgreen = new GameColor(62, 76, 31, 255);
	private static final GameColor midgreen = new GameColor(76, 91, 43, 255);
	private static final GameColor khaki = new GameColor(105, 114, 66, 255);
	private static final GameColor lightgreen = new GameColor(128, 136, 88, 255);
	private static final GameColor mint = new GameColor(146, 154, 106, 255);
	// private static final int AUTUMN_MAX = 4;
	// private static final int LEAF_MAX = 13;

	// bitmaps
	private static Pixmap storm_bitmap = new Pixmap(68, 128, Format.RGBA8888);
	private Pixmap rain_bitmap = new Pixmap(14, 112, Format.RGBA8888);
	private static Pixmap snow_bitmap = new Pixmap(12, 12, Format.RGBA8888);
	private static Pixmap hail_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap petal_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap autumn_1_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap autumn_2_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap autumn_3_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap autumn_4_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_1_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_2_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_3_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_4_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_5_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_6_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_7_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_8_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_9_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_10_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_11_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_12_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static Pixmap leaf_13_bitmap = new Pixmap(8, 8, Format.RGBA8888);
	private static ArrayList<Pixmap> autumn_sprites = new ArrayList<Pixmap>();
	private static ArrayList<Pixmap> leaf_sprites = new ArrayList<Pixmap>();

	// / <summary>
	// / Initializes all the bitmaps used for weather effects.
	// / </summary>
	public void initialize_bitmaps() {
		// TODO finish this!
		// RAIN
		for (int i = 0; i < 7; i++) {
			fill_rect(rain_bitmap, new Rectangle(12 - i * 2, i * 16, 2, 16),
					GameColor.TRUEWHITE);
		}
		// SNOW
		fill_rect(snow_bitmap, new Rectangle(0, 2, 12, 8), semi_transparent);
		fill_rect(snow_bitmap, new Rectangle(2, 0, 8, 12), semi_transparent);
		fill_rect(snow_bitmap, new Rectangle(2, 4, 8, 4), semi_transparent);
		fill_rect(snow_bitmap, new Rectangle(4, 2, 4, 8), semi_transparent);
		// STORM
		for (int i = 0; i < 62; i++) {
			fill_rect(storm_bitmap, new Rectangle(66 - i, i * 2, 2, 4),
					semi_transparent);
			fill_rect(storm_bitmap, new Rectangle(64 - i, i * 2, 2, 4),
					semi_transparent);
			fill_rect(storm_bitmap, new Rectangle(62 - i, i * 2, 2, 4),
					semi_transparent);
		}
		// HAIL
		fill_rect(hail_bitmap, new Rectangle(2, 0, 4, 2), bluegray);
		fill_rect(hail_bitmap, new Rectangle(0, 2, 2, 4), bluegray);
		fill_rect(hail_bitmap, new Rectangle(6, 2, 2, 4), gray);
		fill_rect(hail_bitmap, new Rectangle(2, 6, 4, 2), gray);
		fill_rect(hail_bitmap, new Rectangle(2, 2, 4, 4), lightgray);
		fill_rect(hail_bitmap, new Rectangle(2, 2, 2, 2), lightblue);
		// PETAL
		fill_rect(petal_bitmap, new Rectangle(0, 6, 2, 2), lightpink);
		fill_rect(petal_bitmap, new Rectangle(2, 4, 2, 2), lightpink);
		fill_rect(petal_bitmap, new Rectangle(4, 2, 2, 2), lightpink);
		fill_rect(petal_bitmap, new Rectangle(6, 0, 2, 2), lightpink);
		fill_rect(petal_bitmap, new Rectangle(2, 6, 2, 2), darkpink);
		fill_rect(petal_bitmap, new Rectangle(4, 4, 2, 2), darkpink);
		fill_rect(petal_bitmap, new Rectangle(6, 2, 2, 2), darkpink);
		// AUTUMN1
		fill_rect(autumn_1_bitmap, new Rectangle(5, 1, 1, 1), orangebrown);
		fill_rect(autumn_1_bitmap, new Rectangle(6, 1, 1, 1), brightorange);
		fill_rect(autumn_1_bitmap, new Rectangle(7, 1, 1, 1), paleorange);
		fill_rect(autumn_1_bitmap, new Rectangle(3, 2, 1, 1), orangebrown);
		fill_rect(autumn_1_bitmap, new Rectangle(4, 2, 2, 1), brightorange);
		fill_rect(autumn_1_bitmap, new Rectangle(6, 2, 1, 1), paleorange);
		fill_rect(autumn_1_bitmap, new Rectangle(2, 3, 1, 1), orangebrown);
		fill_rect(autumn_1_bitmap, new Rectangle(3, 3, 1, 1), brightorange);
		fill_rect(autumn_1_bitmap, new Rectangle(4, 3, 2, 1), paleorange);
		fill_rect(autumn_1_bitmap, new Rectangle(1, 4, 1, 1), orangebrown);
		fill_rect(autumn_1_bitmap, new Rectangle(2, 4, 1, 1), brightorange);
		fill_rect(autumn_1_bitmap, new Rectangle(3, 4, 1, 1), paleorange);
		fill_rect(autumn_1_bitmap, new Rectangle(1, 5, 1, 1), brightorange);
		fill_rect(autumn_1_bitmap, new Rectangle(2, 5, 1, 1), paleorange);
		fill_rect(autumn_1_bitmap, new Rectangle(0, 6, 1, 1), orangebrown);
		fill_rect(autumn_1_bitmap, new Rectangle(1, 6, 1, 1), paleorange);
		fill_rect(autumn_1_bitmap, new Rectangle(0, 7, 1, 1), paleorange);
		// AUTUMN2
		fill_rect(autumn_2_bitmap, new Rectangle(3, 0, 1, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(7, 0, 1, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(3, 1, 1, 1), orangebrown);
		fill_rect(autumn_2_bitmap, new Rectangle(4, 1, 1, 1), burntred);
		fill_rect(autumn_2_bitmap, new Rectangle(6, 1, 1, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(0, 2, 1, 1), paleorange);
		fill_rect(autumn_2_bitmap, new Rectangle(1, 2, 1, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(2, 2, 1, 1), orangebrown);
		fill_rect(autumn_2_bitmap, new Rectangle(3, 2, 1, 1), burntred);
		fill_rect(autumn_2_bitmap, new Rectangle(4, 2, 1, 1), orangebrown);
		fill_rect(autumn_2_bitmap, new Rectangle(5, 2, 1, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(1, 3, 3, 1), orangebrown);
		fill_rect(autumn_2_bitmap, new Rectangle(4, 3, 2, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(6, 3, 1, 1), orangebrown);
		fill_rect(autumn_2_bitmap, new Rectangle(2, 4, 1, 1), burntred);
		fill_rect(autumn_2_bitmap, new Rectangle(3, 4, 3, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(6, 4, 1, 1), burntred);
		fill_rect(autumn_2_bitmap, new Rectangle(7, 4, 1, 1), darkbrown);
		fill_rect(autumn_2_bitmap, new Rectangle(1, 5, 1, 1), orangebrown);
		fill_rect(autumn_2_bitmap, new Rectangle(2, 5, 2, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(4, 5, 1, 1), orangebrown);
		fill_rect(autumn_2_bitmap, new Rectangle(5, 5, 1, 1), burntred);
		fill_rect(autumn_2_bitmap, new Rectangle(1, 6, 2, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(4, 6, 2, 1), burntred);
		fill_rect(autumn_2_bitmap, new Rectangle(0, 7, 1, 1), brightorange);
		fill_rect(autumn_2_bitmap, new Rectangle(5, 7, 1, 1), darkbrown);
		// AUTUMN3
		fill_rect(autumn_3_bitmap, new Rectangle(7, 1, 1, 1), paleorange);
		fill_rect(autumn_3_bitmap, new Rectangle(6, 2, 1, 1), paleorange);
		fill_rect(autumn_3_bitmap, new Rectangle(7, 2, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(5, 3, 1, 1), paleorange);
		fill_rect(autumn_3_bitmap, new Rectangle(6, 3, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(4, 4, 1, 1), paleorange);
		fill_rect(autumn_3_bitmap, new Rectangle(5, 4, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(6, 4, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(2, 5, 2, 1), paleorange);
		fill_rect(autumn_3_bitmap, new Rectangle(4, 5, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(5, 5, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(1, 6, 1, 1), paleorange);
		fill_rect(autumn_3_bitmap, new Rectangle(2, 6, 2, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(4, 6, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(0, 7, 1, 1), paleorange);
		fill_rect(autumn_3_bitmap, new Rectangle(1, 7, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(2, 7, 1, 1), orangebrown);
		// AUTUMN4
		fill_rect(autumn_3_bitmap, new Rectangle(3, 0, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(7, 0, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(3, 1, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(4, 1, 1, 1), burntred);
		fill_rect(autumn_3_bitmap, new Rectangle(6, 1, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(0, 2, 1, 1), paleorange);
		fill_rect(autumn_3_bitmap, new Rectangle(1, 2, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(2, 2, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(3, 2, 1, 1), burntred);
		fill_rect(autumn_3_bitmap, new Rectangle(4, 2, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(5, 2, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(1, 3, 3, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(4, 3, 2, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(6, 3, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(2, 4, 1, 1), burntred);
		fill_rect(autumn_3_bitmap, new Rectangle(3, 4, 3, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(6, 4, 1, 1), burntred);
		fill_rect(autumn_3_bitmap, new Rectangle(7, 4, 1, 1), darkbrown);
		fill_rect(autumn_3_bitmap, new Rectangle(1, 5, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(2, 5, 2, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(4, 5, 1, 1), orangebrown);
		fill_rect(autumn_3_bitmap, new Rectangle(5, 5, 1, 1), burntred);
		fill_rect(autumn_3_bitmap, new Rectangle(1, 6, 2, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(4, 6, 2, 1), burntred);
		fill_rect(autumn_3_bitmap, new Rectangle(0, 7, 1, 1), brightorange);
		fill_rect(autumn_3_bitmap, new Rectangle(5, 7, 1, 1), darkbrown);

		// TODO FINISH!
		autumn_sprites.add(autumn_1_bitmap);
		autumn_sprites.add(autumn_2_bitmap);
		autumn_sprites.add(autumn_3_bitmap);
		autumn_sprites.add(autumn_4_bitmap);
		leaf_sprites.add(leaf_1_bitmap);
		leaf_sprites.add(leaf_2_bitmap);
		leaf_sprites.add(leaf_3_bitmap);
		leaf_sprites.add(leaf_4_bitmap);
		leaf_sprites.add(leaf_5_bitmap);
		leaf_sprites.add(leaf_6_bitmap);
		leaf_sprites.add(leaf_7_bitmap);
		leaf_sprites.add(leaf_8_bitmap);
		leaf_sprites.add(leaf_9_bitmap);
		leaf_sprites.add(leaf_10_bitmap);
		leaf_sprites.add(leaf_11_bitmap);
		leaf_sprites.add(leaf_12_bitmap);
		leaf_sprites.add(leaf_13_bitmap);

		// TODO finish!

	}

	private Forecast _forecast;

	public Forecast forecast() {
		return _forecast;
	}

	public void setForecast(Forecast value) {
		if (value == _forecast)
			return;
		_forecast = value;
		// Sprite Sprite = new Sprite("Windowskin/Backcolor");
		Sprite sprite;
		for (int i = 0; i < MAX_SPRITES; i++) {
			sprite = new Sprite("Null.png");
			if (value == Forecast.rain || value == Forecast.thunderstorm) {
				sprite = new Sprite(rain_bitmap);
				// Sprite.src_rect = new Rectangle(0, 0, 14, 112);
			} else if (value == Forecast.hail) {
				sprite = new Sprite(hail_bitmap);
				// Sprite.src_rect = new Rectangle(0, 0, 8, 8);
			} else if (value == Forecast.snow) {
				// Sprite.src_rect = new Rectangle(0, 0, 12, 12);
				sprite = new Sprite(snow_bitmap);
			} else if (value == Forecast.sakura) {
				sprite = new Sprite(petal_bitmap);
				// Sprite.src_rect = new Rectangle(0, 0, 8, 8);
			} else if (value == Forecast.storm) {
				sprite = new Sprite(storm_bitmap);
				// Sprite.src_rect = new Rectangle(0, 0, 68, 128);
			}
			// sets the sprite : the index
			scatter_sprite(sprite);
			sprites[i] = sprite;

			info[i] = 0;
		}
	}

	// / <summary>
	// / Given a bitmap, rectangle and color, colors the birmap : that rectangle
	// the specific color and returns the bitmap
	// / </summary>
	// / <param name="bitmap">bitmap to be edited</param>
	// / <param name="rect">rectangle to be edited</param>
	// / <param name="color">color to fill in</param>
	// / <returns></returns>
	private static Pixmap fill_rect(Pixmap bitmap, Rectangle rect,
			GameColor color) {
		// check if coordinates are inavlid or outofbounds
		// if(rect.x < 0 || rect.x > bitmap.GetLength(0) || rect.y < 0 || rect.y
		// > bitmap.GetLength(1))
		// throw new IndexOutOfRangeException(rect.x + " " + rect.y + " " +
		// rect.Width + " " + rect.Height);
		// if(rect.x + rect.Width > bitmap.GetLength(0) || rect.y + rect.Height
		// > bitmap.GetLength(1))
		// throw new IndexOutOfRangeException(rect.x + " " + rect.y + " " +
		// rect.Width + " " + rect.Height);
		// for(int i = 0; i < rect.Width; i++)
		// {
		// for(int j = 0; j < rect.Height; j++)
		// {
		// bitmap[rect.x + i, rect.y + j] = color;
		// }
		// }
		int x = (int) rect.x;
		int y = (int) rect.y;
		int w = (int) rect.width;
		int h = (int) rect.height;
		bitmap.setColor(color);
		bitmap.fillRectangle(x, y, w, h);
		return bitmap;
	}

	private static int _spritecount = 0;

	/**
	 * Returns the count of active sprites currently in use
	 * 
	 * @return
	 */
	private static int spritecount() {
		return _spritecount;
	}

	private static void set_spritecount(int value) {
		if (MAX_SPRITES == value)
			return;
		_spritecount = Math.min(Math.max(_spritecount, 0), MAX_SPRITES);
		// disable sprites if necesarry
		Sprite sprite;
		for (int i = 0; i < MAX_SPRITES; i++) {
			sprite = sprites[i];
			// sets sprites visibility to false if they exceed the bounds of the
			// maximum sprite count
			if (sprite != null)
				sprite.visible = (1 <= _spritecount);
		}
	}

	// / <summary>
	// / Plays the thunderclap SE with randomly generated parameters
	// / </summary>
	private static void play_thunderclap_se() {
		// volume ranges from 0.5 to 1.0
		float pan = 0.0f;
		float volume = (float) RANDOMIZER.nextDouble();
		volume = Math.max(volume, 0.5f);
		// theres a 50% chance the volume is at 0.5f; If it is, the thunderclap
		// is panned
		if (volume < 0.5f) {
			pan = RANDOMIZER.nextInt() * 2;
			pan -= 1.0f;
		}
		// pitch ranges from -.15 to .15, and is entirely random.
		float pitch = (float) RANDOMIZER.nextDouble() - 0.5f;
		pitch /= 4.0f;
		Audio.se_play(THUNDER_SE, volume, pitch, pan);
	}

	public void update(Vector2 difference) {
		// If theres no weather, return;
		if (forecast() == Forecast.none)
			return;
		Sprite sprite;
		float x, y;
		for (int i = 0; i < MAX_SPRITES; i++) {
			sprite = sprites[i];
			x = sprite.pos().x;
			y = sprite.pos().y;
			if (sprite == null)
				break;
			// rain + thunderstorm
			if (forecast() == Forecast.rain
					|| forecast() == Forecast.thunderstorm) {
				sprite.setX(x - 2.0f);
				sprite.setY(y + 16.0f);
				sprite.setopacity(sprite.opacity() - 8);
				if (forecast() == Forecast.thunderstorm) {
					if (RANDOMIZER.nextInt(THUNDERWAIT) == 0) {
						play_thunderclap_se();
						// FLASH SCREEN
					}
				}
			}
			// storm
			else if (forecast() == Forecast.storm) {
				sprite.setX(x - 8.0f);
				sprite.setY(y + 16.0f);
				sprite.setopacity(sprite.opacity() - 12);
			}
			// snow
			else if (forecast() == Forecast.snow) {
				sprite.setX(x - 2.0f);
				sprite.setY(y + 8.0f);
				sprite.setopacity(sprite.opacity() - 8);
			}
			// hail
			else if (forecast() == Forecast.hail) {
				sprite.setX(x - 1.0f);
				sprite.setY(y + 2.0f);
				sprite.setopacity(sprite.opacity() - 15);
			}
			// sakura
			else if (forecast() == Forecast.sakura) {
				if (info[i] < 25)
					sprite.setX(x - 1.0f);
				else
					sprite.setX(x + 1.0f);
				info[i] = (short) ((info[i] + 1) % 50);
				sprite.setY(y + 1.0f);
				sprite.setopacity(sprite.opacity() - info[i] % 4);
			}
			// falling autumn
			else if (forecast() == Forecast.falling_autumn) {
				int count = RANDOMIZER.nextInt(40);
				if (count == 0) {
					sprite = new Sprite(autumn_sprites.get(index));
					index = (index + 1) % autumn_sprites.size();
				}
				sprite.setX(x - 1.0f);
				sprite.setY(y + 1.0f);
			}
			// swirling autumn
			// blowing autumn
			// falling leaves

			Rectangle pos = new Rectangle((int) sprite.pos().x,
					(int) sprite.pos().y, 0, 0);

			if ((sprite.opacity() < 64)
					|| !Camera.weatherbounds().contains(pos)) {
				info[i] = 0;
				sprite.setX(MathUtils.random(smallwidth(), bigwidth()));
				sprite.setY(MathUtils.random(smallheight(), bigheight()));
				if (forecast() == Forecast.storm)
					sprite.setopacity(128);
				else
					sprite.setopacity(255);
			} else {
				sprite.setpos(sprite.pos().sub(difference));
			}
			// sprites[i] = sprite;
		}
	}

	/**
	 * Given an Sprite, will scatter its position randomly across the map
	 * 
	 * @param sprite
	 */
	public void scatter_sprite(Sprite sprite) {
		int bigwidth = (int) (Base.window_width() * 1.1f);
		int bigheight = (int) (Base.window_height() * 1.1f);
		int smallwidth = (int) (Base.window_width() * -0.1f);
		int smallheight = (int) (Base.window_height() * -0.1f);
		sprite.setX(MathUtils.random(smallwidth, bigwidth));
		sprite.setY(MathUtils.random(smallheight, bigheight));
	}

	public void draw(Draw_Object s_batch) {
		if (forecast() == Forecast.none)
			return;

		// Sprite sprite;
		for (Sprite sprite : sprites) {
			if (sprite == null)
				break;
			sprite.draw(s_batch);
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	private int bigwidth() {
		return (int) (Base.window_width() * 1.1f);
	}

	private int bigheight() {
		return (int) (Base.window_height() * 1.1f);
	}

	private int smallwidth() {
		return (int) (Base.window_width() * -0.1f);
	}

	private int smallheight() {
		return (int) (Base.window_height() * -0.1f);
	}
}
