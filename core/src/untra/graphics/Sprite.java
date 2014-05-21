package untra.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Sprite implements Disposable {
	/**
	 * Sets the specified blend flavor for the sprite. By default, this is
	 * alpha. *Alpha - transparent images are layered over the previous
	 * background. *Replace - Images are "stamped" onto the screen. *additive -
	 * colors are added, leading to a brighter result. *subtractive - colors are
	 * subtracted, leading to a darker result.
	 * 
	 * @author samuel
	 * 
	 */

	// private Texture texture;
	protected float x, y;
	protected Rectangle src_rect;
	private static final String ROOT = "graphics/";
	protected Texture texture;
	private Vector2 scale;
	private int flash_frames = 0;
	private int elapsed_flash_frames;
	private GameColor flash_color = GameColor.WHITE.clone();
	private int elapsed_fade_frames;
	private float alphachange, alpha;
	private GameColor blend_color;
	protected int _width, _height;
	// public float Opacity;
	public boolean visible = true;
	public Blend blend = Blend.alpha;
	public boolean fliphorz = false;
	public boolean flipvert = false;

	public Sprite(String path) {
		texture = new Texture(Gdx.files.internal(ROOT + path));
		// texture = getTexture();
		// super(new Texture(Gdx.files.internal(ROOT + path)));
		initialize();
	}

	public Sprite(Texture texture) {
		this.texture = texture;
		initialize();
	}

	public Sprite(Pixmap path) {
		texture = new Texture(path);
		initialize();
	}

	private void initialize() {
		// texture = super.getTexture();
		_width = texture.getWidth();
		_height = texture.getHeight();
		alpha = 1.0f;
		scale = new Vector2(1.0f, 1.0f);
		visible = true;
		x = 0;
		y = 0;
		blend_color = new GameColor(1.0f, 1.0f, 1.0f, 1.0f);
		setopacity(255);
		src_rect = new Rectangle(0, 0, _width, _height);
		blend = Blend.alpha;
	}

	public int opacity() {
		return (int) (alpha * 255);
	}

	public void setopacity(int a) {
		setopacity((float) a / 255.0f);
	}

	public void setopacity(float f) {
		alpha = (Math.max(Math.min((f), 1.0f), 0.0f));
	}

	/**
	 * Resets the opacity value of the blendcolor, to the prevent overflow into
	 * spritebatch
	 */
	private void resetopacity() {
		setopacity((int) (alpha * 255));
	}

	public void update() {
		if (elapsed_flash_frames > 0) {
			float f = ((float) elapsed_flash_frames / (float) flash_frames);
			setColor(GameColor.WHITE.clone().lerp(flash_color, (f)));
			elapsed_flash_frames--;
		} else {
			// blend_color = GameColor.WHITE.clone();
		}
		if (elapsed_fade_frames > 0) {
			alpha += alphachange;

			elapsed_fade_frames--;
		} else {
			alphachange = 0.0f;
		}
		resetopacity();
		return;
	}

	public float width() {
		return (int) (_width * scale.x);
	}

	public float height() {
		return (int) (_height * scale.y);
	}

	public void flash(GameColor c, int frames) {
		if (frames == 0)
			return;
		flash_frames = frames;
		elapsed_flash_frames = frames;
		flash_color = c;
	}

	public boolean is_flashing() {
		return (elapsed_flash_frames > 0);
	}

	public void setY(int value) {
		y = (float) value;
	}

	@Override
	public void dispose() {
		texture.dispose();
	}

	public void draw(Draw_Object s_batch) {
		if (!visible)
			return;
		// fixblenddata(s_batch);

		// super.setBounds(x, y, width(), height());
		// super.setRegion(src_rect.x, src_rect.y, src_rect.x + src_rect.width,
		// src_rect.y + src_rect.height);
		// super.setColor(blend_color);
		// super.draw(s_batch);

		blend_color.a = alpha;
		setbatchColor(s_batch, blend_color);
		s_batch.draw(texture, x, y, width(), height(), (int) src_rect.x,
				(int) src_rect.y, (int) src_rect.width, (int) src_rect.height,
				fliphorz, !flipvert);
		setbatchColor(s_batch, GameColor.WHITE);
	}

	/**
	 * Simmilar to the draw method, but used in conjunction with src_rect to
	 * prevent unwarrented scaling tansforms in the draw process.
	 * 
	 * @param s_batch
	 */
	public void stamp(Draw_Object s_batch) {
		if (!visible)
			return;
		// fixblenddata(s_batch);

		// super.setBounds(x, y, src_rect.width, src_rect.height);
		// // super.setRegion(src_rect.x, src_rect.y, src_rect.x +
		// src_rect.width,
		// // src_rect.y + src_rect.height);
		// super.setColor(blend_color);
		// super.draw(s_batch);

		blend_color.a = alpha;
		setbatchColor(s_batch, blend_color);
		s_batch.draw(texture, x, y, (int) src_rect.width,
				(int) src_rect.height, (int) src_rect.x, (int) src_rect.y,
				(int) src_rect.width, (int) src_rect.height, fliphorz,
				!flipvert);

		setbatchColor(s_batch, GameColor.WHITE);

	}

	/**
	 * FixBlendData corrects a sprites draw style.
	 * 
	 * @param s_batch
	 */
	@SuppressWarnings("unused")
	private void fixblenddata(Draw_Object s_batch) {
		if (blend == Blend.replace && s_batch.isBlendingEnabled())
			s_batch.disableBlending();
		else if (!s_batch.isBlendingEnabled())
			s_batch.enableBlending();
		switch (blend) {
		case alpha: {
			s_batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		}
		case replace: {
			s_batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
			break;
		}
		case additive: {
			s_batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			break;
		}
		case subtractive: {
			// TODO: figure this bullshit out
			// s_batch.setBlendFunction(GL11.GL_MINUS_SRC_ALPHA, GL11.GL_ONE);
			break;
		}
		}
		// Sets the blending color
		s_batch.setColor(blend_color);
	}

	public Vector2 pos() {
		return new Vector2(x, y);
	}

	public void setpos(Vector2 pos) {
		setX(pos.x);
		setY(pos.y);
	}

	public void fade_to(int alpha, int frames) {
		if (elapsed_fade_frames > 0)
			return;
		// fade_frames = frames;
		elapsed_fade_frames = frames;
		alphachange = (((float) alpha - (float) opacity()) / (float) (frames * 255));
	}

	/**
	 * Sets the source Rectangle to the specified coordinates
	 * 
	 * @param r
	 */
	public void src_rect(Rectangle r) {
		src_rect = r;
	}

	public void vertstretch(float f) {
		scale.y = f;
		// super.setScale(scale.x, scale.y);
	}

	public void horzstretch(float f) {
		scale.x = f;
		// super.setScale(scale.x, scale.y);
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void flip(boolean x, boolean y) {
		fliphorz = x;
		flipvert = y;
	}

	public int getx() {
		return (int) x;
	}

	public int gety() {
		return (int) y;
	}

	public void setPosition(float f, float g) {
		x = f;
		y = g;
	}

	public void setColor(GameColor color) {
		blend_color = color.clone();
	}

	public void setbatchColor(Draw_Object s_batch, GameColor tint) {
		if (tint.valid()) {
			s_batch.setColor(tint.to_badlogic_color());
		} else
			System.out.println("Bad Color! " + tint.toString());
	}
}
