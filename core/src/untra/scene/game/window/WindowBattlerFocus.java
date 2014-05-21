package untra.scene.game.window;

import untra.graphics.Draw_Object;
import untra.graphics.GameColor;
import untra.graphics.Sprite;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.database.Skill;
import untra.driver.Base;

public class WindowBattlerFocus extends WindowBattle {
	private boolean advanced_info = true;
	private int fHP, fSP;
	// private Battler battler();
	// private Gradient hp_gauge;
	// private Gradient sp_gauge;
	private Sprite hpa;
	private Sprite hpb;
	private Sprite spa;
	private Sprite spb;
	// private Gradient hit_gauge;
	public Skill skill;

	public WindowBattlerFocus() {
		super(new Rectangle(0, 0, 320, 96));
		Initialize();
	}

	public void Initialize() {
		this.coordinates.x = 0;
		this.coordinates.y = Base.DEFAULTWINDOWHEIGHT - this.height();
		// hit_gauge = new Gradient(1, 1, GameColor.TRUEWHITE,
		// GameColor.TRUEWHITE);
		// disable_cursor_rect = true;
		item_max = 0;
		move_to_right();
		hpa = new Sprite("gradients/hpa.png");
		hpb = new Sprite("gradients/hpb.png");
		spa = new Sprite("gradients/spa.png");
		spb = new Sprite("gradients/spb.png");
		hpa.vertstretch(2.0f);
		hpb.vertstretch(2.0f);
		spa.vertstretch(2.0f);
		spb.vertstretch(2.0f);
		refresh();
	}

	public void update() {
		super.update();
		if (battler() == null)
			return;
		if (this.active == false && !isPanning())
			return;

		if (fHP != battler().properties.HP || fSP != battler().properties.SP)
			refresh();
		if (isPanning()) {
			float x = ocoordinates().x;
			float y = ocoordinates().y;
			hpa.setPosition(x, y + 16);
			hpb.setPosition(x, y + 16);
			spa.setPosition(x, y + 48);
			spb.setPosition(x, y + 48);
			// hp_gauge.setPosition(x, y + 16);
			// sp_gauge.setPosition(x, y + 48);
			battler().face_graphic.setPosition(x + width() - 144, y + height()
					- 128);
		}
		/*
		 * if (Main.Ticks % 10 == 0 && status_actor.Properties.HP > 1 &&
		 * status_actor.Properties.SP > 1) { status_actor.Properties.HP--;
		 * status_actor.Properties.SP--; }
		 */
	}

	private void refresh() {
		if (battler() == null)
			return;
		// battler() = battler();
		// if (battler().studied) this.Height = 128;
		// else this.Height = 160;
		if (advanced_info) {
			// hp_gauge = actor_hp_gauge(battler().properties, new Vector2(
			// ocoordinates().x, ocoordinates().y + 32 - 8));
			// sp_gauge = actor_sp_gauge(battler().properties, new Vector2(
			// ocoordinates().x, ocoordinates().y + 64 - 8));

		}
		// hp_gauge.horzstretch((status_actor.Properties.HP_rate));
		// sp_gauge.horzstretch((status_actor.Properties.SP_rate));
		hpa.horzstretch(200 * battler().properties.HP_rate());
		hpb.horzstretch(200 * battler().properties.HP_rate());
		spa.horzstretch(200 * battler().properties.SP_rate());
		spb.horzstretch(200 * battler().properties.SP_rate());
		hpa.setopacity(battler().properties.HP_rate());
		hpb.setopacity(battler().properties.SP_rate());
		battler().face_graphic.setPosition(ocoordinates().x + width() - 144,
				ocoordinates().y + height() - 128);
		fHP = battler().properties.HP;
		fSP = battler().properties.SP;
		// if(Main.Ticks % 10 == 0 && status_actor.Properties.HP > 1)
		// status_actor.Properties.HP--;
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		// hp_gauge.draw(s_batch);
		// sp_gauge.draw(s_batch);
		hpb.draw(s_batch);
		spb.draw(s_batch);
		hpa.draw(s_batch);
		spa.draw(s_batch);
		if (advanced_info)
			draw_advanced_info(s_batch);
		else
			draw_basic_info(s_batch);
	}

	private void draw_advanced_info(Draw_Object s_batch) {
		// Vector2 P = ocoordinates();
		// s_batch.Draw_Outlined_Text(battler().Properties.name, new
		// Vector2(
		// ocoordinates().x, ocoordinates().y - 8), GameColor.TRUEWHITE);
		s_batch.draw_smaller_text(battler().properties.Name, ocoordinates().x,
				ocoordinates().y - 8, GameColor.WHITE);
		// s_batch.Draw_Outlined_Text(battler().Properties.cclass.Name, P,
		// Color.White);
		battler().face_graphic.draw(s_batch);
		draw_actor_status(s_batch, battler().properties, new Vector2(
				ocoordinates().x + 128, ocoordinates().y + 96));
		draw_actor_hp(s_batch, battler().properties, new Vector2(
				ocoordinates().x - 8, ocoordinates().y + 20), true);
		draw_actor_sp(s_batch, battler().properties, new Vector2(
				ocoordinates().x - 8, ocoordinates().y + 52), true);
	}

	private void draw_basic_info(Draw_Object s_batch) {
		Vector2 P = ocoordinates();
		draw_text(s_batch, "????", P, GameColor.TRUEWHITE);
		P.x += width() - 84;
		// s_batch.Draw_Outlined_Text(battler().Properties.cclass.Name, P,
		// Color.White);
		battler().face_graphic.draw(s_batch);
		draw_actor_status(s_batch, battler().properties, new Vector2(
				ocoordinates().x + 128, ocoordinates().y + 96));
		draw_actor_hp(s_batch, battler().properties, new Vector2(
				ocoordinates().x, ocoordinates().y + 32 - 8), false);
		draw_actor_sp(s_batch, battler().properties, new Vector2(
				ocoordinates().x, ocoordinates().y + 64 - 8), false);
	}
}
