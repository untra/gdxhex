package untra.scene.game.window;

import untra.gamewindow.SelectionWindow;
import untra.graphics.Draw_Object;
import untra.graphics.GameColor;
import untra.graphics.Gradient;
import untra.player.Actor;
import untra.scene.game.battle.Battler;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.database.Stats;
import untra.database.Status;

public class WindowBattle extends SelectionWindow {
	// private static final int FRAMESTOFADE = 15;
	// private static final int MINIMUMPIXELSTOFADEIN = 8;

	public WindowBattle(Rectangle coordinates) {
		super(coordinates);
	}

	protected static Battler window_battler;

	protected void draw_actor_level(Draw_Object s_batch, Actor actor,
			Vector2 pos) {
		s_batch.draw_regular_text("LV", pos.x, pos.y, new GameColor(38, 127, 0,
				255));
		s_batch.draw_regular_text(Integer.toString(actor.LEVEL), pos.x, pos.y,
				new GameColor(38, 127, 0, 255));
	}

	protected void draw_actor_hp(Draw_Object s_batch, Actor actor, Vector2 pos,
			boolean known) {
		pos.y -= 2;
		s_batch.draw_regular_text("HP", pos.x, pos.y, GameColor.WHITE);
		pos.x += 48;
		if (known)
			s_batch.draw_smaller_text(actor.HP + "/" + actor.MAX_HP(), pos.x,
					pos.y, GameColor.WHITE);
		else
			s_batch.draw_smaller_text("??" + "/" + "??", pos.x, pos.y,
					GameColor.WHITE);
	}

	protected void draw_actor_sp(Draw_Object s_batch, Actor actor, Vector2 pos,
			boolean known) {
		pos.y -= 2;
		s_batch.draw_regular_text("SP", pos.x, pos.y, GameColor.WHITE);
		pos.x += 48;
		if (known)
			s_batch.draw_smaller_text(actor.SP + "/" + actor.MAX_SP(), pos.x,
					pos.y, GameColor.WHITE);
		else
			s_batch.draw_smaller_text("??" + "/" + "??", pos.x, pos.y,
					GameColor.WHITE);
	}

	protected void draw_actor_hit(Draw_Object s_batch, Battler battler,
			Vector2 pos) {
		// int accuracy = (int) (battler.accuracy * 100);
		// String s = String.format("{0:d}%", accuracy);
		// pos.y -= 2;
		// s_batch.draw_regular_text(s, pos.x, pos.y, GameColor.WHITE);
		// pos.x += 48;
		// int damage = battler.tentative_damage;
		// s = String.format("{0:d}pts", damage);
		// s_batch.draw_regular_text(s, pos.x, pos.y, GameColor.WHITE);
	}

	private String status_string(Actor actor) {
		String text = "";
		for (Status s : actor.states) {
			if (s.id == 0) // Actor is dead
			{
				text = "[DOWN]\n";
				break;
			} else
				text = "[" + s.name + "]\n";
		}
		if (text == "")
			text = "";
		return text;
	}

	protected void draw_actor_status(Draw_Object s_batch, Actor actor,
			Vector2 pos) {
		s_batch.draw_regular_text(status_string(actor), pos.x, pos.y);
	}

	protected void draw_actor_parameter(Draw_Object s_batch, Actor actor,
			Vector2 pos, Stats e) {
		String param_name = "";
		String param_value = "";
		switch (e) {
		case ATK: {
			param_name = "Attack";
			param_value = Integer.toString(actor.ATK());
			break;
		}
		case DEF: {
			param_name = "Defence";
			param_value = Integer.toString(actor.DEF());
			break;
		}
		case POW: {
			param_name = "Power";
			param_value = Integer.toString(actor.POW());
			break;
		}
		case SKL: {
			param_name = "Skill";
			param_value = Integer.toString(actor.SKL());
			break;
		}
		case MND: {
			param_name = "Mind";
			param_value = Integer.toString(actor.MND());
			break;
		}
		case MOV: {
			param_name = "Move";
			param_value = Integer.toString(actor.MOV());
			break;
		}
		case VSN: {
			param_name = "Vision";
			param_value = Integer.toString(actor.getVSN());
			break;
		}
		case SPD: {
			param_name = "Initiative";
			param_value = Integer.toString(actor.getSPD());
			break;
		}
		default:
			break;
		}
		s_batch.draw_regular_text(param_name, pos.x, pos.y, GameColor.TRUEWHITE);
		s_batch.draw_regular_text(param_value, pos.x + 120, pos.y,
				GameColor.TRUEWHITE);
	}

	private Battler _batter;

	public Battler battler() {
		return _batter;
	}

	public void setBattler(Battler value) {
		_batter = value;
	}

	protected Gradient actor_hp_gauge(Actor actor, Vector2 pos) {

		GameColor color1, color2;
		float rate = actor.HP_rate();
		int width = (int) (200 * rate);
		color1 = new GameColor(80 - (int) (24 * rate), (int) (80 * rate),
				(int) (14 * rate), 255);

		color2 = new GameColor(240 - (int) (72 * rate), (int) (240 * rate),
				(int) (62 * rate), 255);
		Gradient g = new Gradient(width, 20, color1, color2, true, false);
		g.setpos(pos);
		return g;
	}

	protected Gradient actor_sp_gauge(Actor actor, Vector2 pos) {
		float rate = actor.SP_rate();
		int width = (int) (200 * rate);
		GameColor color1 = new GameColor((int) (14 * rate),
				80 - (int) (24 * rate), (int) (80 * rate), 255);
		GameColor color2 = new GameColor((int) (62 * rate),
				240 - (int) (32 * rate), (int) (240 * rate), 255);
		Gradient g = new Gradient(width, 20, color1, color2, true, false);
		g.setpos(pos);
		return g;
	}

	protected Gradient actor_hit_gauge(Actor actor, Vector2 pos, float ACC) {
		float rate = ACC;
		int width = (int) (120 * rate);
		GameColor color1 = new GameColor((int) (80 * rate), (int) (14 * rate),
				80 - (int) (24 * rate), 200);
		GameColor color2 = new GameColor((int) (240 * rate), (int) (32 * rate),
				92 - (int) (52 * rate), 200);
		Gradient g = new Gradient(width, 16, color1, color2, true, true);
		g.setpos(pos);
		return g;
	}

	protected void draw_text(Draw_Object s_batch, String string, Vector2 p,
			GameColor color) {
		s_batch.draw_regular_text(string, p.x, p.y, color);

	}
}
