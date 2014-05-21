package untra.scene.game.window;

import untra.graphics.Draw_Object;
import untra.graphics.GameColor;
import untra.graphics.Sprite;

import java.util.ArrayList;

import untra.player.Actor;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.database.Skill;
import untra.driver.Base;

public class WindowSkill extends WindowBattle {

	public Skill skill;
	private Actor actor;
	private ArrayList<Skill> skillset;
	private ArrayList<Sprite> skillbitmap;

	public WindowSkill() {
		super(new Rectangle(0, 0, 0, 96));
		skillset = new ArrayList<Skill>();
		skillbitmap = new ArrayList<Sprite>();
		Initialize(null);
	}

	public void Initialize(Actor a) {
		if (a != null)
			skillset = a.skills;
		else
			skillset = new ArrayList<Skill>();
		item_max = skillset.size();
		coordinates.height = (Math.min(item_max + 1, 6) * 32) + BUFFER;
		coordinates.y = (Base.window_height() - coordinates.height - 128) / 2;
		coordinates.x = -16;
		coordinates.width = 256;
		cursor_rect_appension = new Rectangle(-16, 16, 0, 0);
		column_max = 1;
		this.index = 0;
		// this.opacity = 160;
		actor = a;
		refresh();
	}

	public void update() {
		super.update();
		if (this.active && skillset.size() != 0)
			skill = skillset.get(index);

	}

	private void refresh() {
		if (actor == null)
			return;
		skillset = new ArrayList<Skill>();
		skillbitmap.clear();
		// int i = 0;
		for (Skill s : actor.skills) {
			skillset.add(s);
			Sprite sprite;
			try {
				sprite = new Sprite("Skills/" + s.name + ".png");
			} catch (Exception e) {
				sprite = new Sprite("Skills/" + "Null" + ".png");
			}

			skillbitmap.add(sprite);
		}
		item_max = skillset.size();
	}

	/**
	 * Draw method
	 */
	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		GameColor text_color;
		Vector2 p;
		Sprite tempSprite;
		int i = 0;
		for (Skill s : skillset) {
			p = ocoordinates();
			text_color = (actor.skill_can_use(s)) ? NORMAL_COLOR
					: DISABLED_COLOR;
			p.x += (index % 2) * (width() - ocoordinates().x / column_max);
			p.x += BUFFER;
			p.y += (index / 2) * 32 + 20;
			// tempSprite = skillbitmap.get(i);
			// tempSprite.setpos(p);
			// tempSprite.draw(s_batch);
			// p.x += 36;
			p.y += 4;
			draw_text(s_batch, s.name, p, text_color);
			p.x += width() - 96;
			// System.out.println(p.x);
			String f = String.format("%3d", s.sp_cost);
			draw_text(s_batch, f, p, text_color);
			i++;
		}
	}
}
