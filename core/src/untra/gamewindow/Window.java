package untra.gamewindow;

import untra.graphics.Draw_Object;
import untra.graphics.GameColor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Base;
import untra.driver.IGamecycle;

public class Window implements IGamecycle {

	protected static final int FRAMESTOPAN = 16;
	protected static final int MINIMUMPIXELSTOPANIN = 8;
	protected boolean panInWindow = false;
	protected int remainingPanTime = 0;
	protected int remainingPanX = 0;
	protected int remainingPanY = 0;
	protected int PanXPerFrame = 0;
	protected int PanYPerFrame = 0;

	protected static Texture borderTexture;
	protected static Sprite selectionSprite;
	protected static Texture backTexture;
	protected static Texture cornerTexture;
	protected static Texture sideTexture;
	protected static Texture barTexture;

	protected Rectangle coordinates;

	private boolean previousactive;
	public boolean active;
	public Rectangle cursor_rect = new Rectangle(0, 0, 0, 0);

	protected GameColor textColor = GameColor.TRUEWHITE;
	protected static final int BUFFER = 16;
	protected static final GameColor NORMAL_COLOR = new GameColor(255, 255,
			255, 255);
	protected static final GameColor INACTIVE_COLOR = new GameColor(224, 224,
			224, 128);
	protected static final GameColor DISABLED_COLOR = new GameColor(128, 128,
			128, 128);

	public Window(Rectangle coordinates) {
		this.coordinates = coordinates;
		this.active = false;
		this.panInWindow = true;
	}

	public Window() {
		this(new Rectangle(0, 0, 0, 0));
	}

	/**
	 * returns practical coordinates of the window.
	 * 
	 * @return
	 */
	public Vector2 coordinates() {
		return new Vector2(coordinates.getX(), coordinates.getY());
	}

	/**
	 * returns operational coordinates. At the particular instance where this
	 * method is called, returns the real position of the window. Used for
	 * positioning window content
	 * 
	 * @return
	 */
	protected Vector2 ocoordinates() {
		return new Vector2(coordinates().x + BUFFER + remainingPanX,
				coordinates().y + BUFFER + remainingPanY);
	}

	public static void init() {
		// Import all Textures here
		System.out.println("Window Initializing...");
		try {
			backTexture = new Texture(
					"graphics/Windowskin/Backcolor.png");
			borderTexture = new Texture(
					"graphics/Windowskin/W_skin.png");

			cornerTexture = new Texture(
					"graphics/Windowskin/corner.png");
			sideTexture = new Texture("graphics/Windowskin/side.png");
			barTexture = new Texture("graphics/Windowskin/bar.png");
			selectionSprite = new Sprite(borderTexture);
			System.out.println("Window Initialization Great Success!");
			// System.out.println(barTexture.getWidth() + " x "
			// + barTexture.getHeight());
		} catch (Exception e) {
			System.out.println("Window Initialization Failed!");
		}

	}

	public boolean isPanning() {
		return (remainingPanTime > 0);
	}

	private void setPanVariables() {
		int orientation = findOrientation();
		if (orientation == 5)
			return;
		PanXPerFrame = -(int) (coordinates.width / FRAMESTOPAN);
		PanYPerFrame = -(int) (coordinates.height / FRAMESTOPAN);
		remainingPanX = (int) coordinates.width;
		remainingPanY = (int) coordinates.height;
		remainingPanTime = FRAMESTOPAN;
		if (orientation % 4 == 0) {
			remainingPanX *= -1;
			remainingPanY *= -1;
			PanYPerFrame *= -1;
			PanXPerFrame *= -1;
		}
		if (!active) {
			remainingPanX *= 0;
			remainingPanY *= 0;
			PanYPerFrame *= -1;
			PanXPerFrame *= -1;

		}
		if (orientation % 6 == 2) {
			remainingPanX = 0;
			PanXPerFrame = 0;
		} else {
			remainingPanY = 0;
			PanYPerFrame = 0;
		}
	}

	/**
	 * 2 = down 4 = left 6 = right 8 = up returns the positioning orientation of
	 * the window, used for paning operations
	 * 
	 * @return
	 */
	private int findOrientation() {
		int y = (int) coordinates.y;
		int x = (int) coordinates.x;
		int w = (int) coordinates.width;
		int h = (int) coordinates.height;
		int H = Base.window_height();
		int W = Base.window_width();
		if (y <= 0)// up
			return 8;
		if (x <= 0)// left
			return 4;
		if (x + w >= W)// right
			return 6;
		if (y + h >= H)// down
			return 2;
		return 5;
	}

	public void update() {
		if (active != previousactive && panInWindow)
			setPanVariables();
		previousactive = active;
		if (panInWindow) {
			if (remainingPanTime > 0) {
				remainingPanX += PanXPerFrame;
				remainingPanY += PanYPerFrame;
				remainingPanTime--;
			} else {
				remainingPanX = 0;
				remainingPanY = 0;
			}
		}
	}

	public void draw(Draw_Object s_batch) {

		if (active != previousactive && panInWindow)
			setPanVariables();
		previousactive = active;
		int exp = 4;
		if (!active && !isPanning())
			return;
		if (coordinates.getWidth() < 8)
			return;
		if (coordinates.getHeight() < 8)
			return;
		float x = coordinates.getX() + remainingPanX;
		float y = coordinates.getY() + remainingPanY;
		float w = coordinates.getWidth();
		float h = coordinates.getHeight();
		// s_batch.draw(borderTexture, 200, 200);
		// s_batch.draw(backTexture, x, y, w - (2 * exp), h - (2 * exp));
		s_batch.draw(backTexture, x - 2, y - 2, w - exp + 1, h - exp + 1);
		// Draw the corners
		x -= exp;
		y -= exp;
		s_batch.draw(cornerTexture, x, y, false, !false);
		x += (w - exp);
		s_batch.draw(cornerTexture, x, y, true, !false);
		y += (h - exp);
		s_batch.draw(cornerTexture, x, y, true, !true);
		x -= (w - exp);
		s_batch.draw(cornerTexture, x, y, false, !true);
		y -= (h - exp);
		// Draw the sides
		s_batch.draw(sideTexture, x, y + exp, 2 * exp, h - (2 * exp), false,
				!true);
		s_batch.draw(sideTexture, x + w - exp, y + exp, 2 * exp, h - (2 * exp),
				true, !false);
		s_batch.draw(barTexture, x + exp, y, w - (2 * exp), 2 * exp, false,
				!false);
		s_batch.draw(barTexture, x + exp, y + h - exp, w - (2 * exp), 2 * exp,
				true, !true);
		// s_batch.draw(, y + exp, exp, exp, true,
		// true);
		// s_batch.draw(sideTexture, x + exp, y + h - exp, w - (2 * exp), exp,
		// false, true);

	}

	public void move_to_bottom() {
		coordinates.setY(Base.DEFAULTWINDOWHEIGHT - coordinates.getHeight());
	}

	public void move_to_top() {
		coordinates.setY(0);
	}

	public void move_to_left() {
		coordinates.setX(0);
	}

	public void move_to_right() {
		coordinates.setX(Base.DEFAULTWINDOWWIDTH - coordinates.getWidth());
	}

	public int width() {
		return (int) coordinates.width;
	}

	public int height() {
		return (int) coordinates.height;
	}

	public void dispose() {
		// Method intentionally left empty
	}

}
