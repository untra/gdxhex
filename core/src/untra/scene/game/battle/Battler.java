package untra.scene.game.battle;

import untra.graphics.AnimationInstance;
import untra.graphics.Draw_Object;
import untra.graphics.FaceSprite;
import untra.graphics.GameColor;
import untra.graphics.Sprite;
import untra.graphics.SpriteSet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import untra.player.Actor;
import untra.scene.game.GameScene;
import untra.scene.game.map.HexDirectional;
import untra.scene.game.map.HexTable;
import untra.scene.game.map.HexTile;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.database.ActiveSkill;
import untra.database.Animation;
import untra.database.Database;
import untra.database.Skill;
import untra.database.Skill_Scope;
import untra.database.Status;
import untra.driver.Base;
import untra.driver.IXml;

public class Battler implements IXml<Battler> {
	// public int tentative_damage;
	public int tentative_exp;
	public Actor properties;
	public BattleAction current_action;
	private int turn;
	public int Y;
	public int X;
	private int mx; // Movement X;
	private int my; // Movement Y
	private int rx; // Real X appension
	private int ry; // Real Y appension
	public AnimationInstance target_animation, damage_animation;
	// private HexDirectional _direction;
	private HexDirectional _direction;

	public void setDirection(HexDirectional d) {
		if (d.equals(HexDirectional.None()))
			return;
		_direction = d;
	}

	public HexDirectional direction() {
		return _direction;
	}

	public SpriteSet sprite;
	public Sprite wpn_graphic;
	public FaceSprite face_graphic;

	public List<HexTile> Move_queue;
	// public int damage;
	// public float accuracy;
	// public boolean damage_pop;
	// public boolean critical;
	// public boolean doublestrike;
	// public boolean knockback;
	private Battle_Effects use_effects, hit_effects;
	public boolean is_flying;
	public boolean is_swimming;
	// public boolean is_defending;
	public boolean has_moved;
	public boolean has_acted;
	// public boolean through;
	public LinkedList<Status> STATES;
	public boolean fixed_direction;
	public boolean step_anime;
	// public GameColor outline_glow = GameColor.BLACK;
	// public TacticType tactic;
	public int anim_speed;
	/**
	 * Studied flag indicates whether specific details of the battler are //
	 * available for the player.
	 */
	public boolean studied = false;
	/**
	 * Targeting flag indicates a special condition of the battler before being
	 * passed through parameters. Reset at the recieve.
	 */
	public boolean targeting;

	// public int turn_counter; //begins at zero. Every frame adds 1

	/**
	 * returns the threshhold for the battlers next turn
	 */
	private int turn_threshhold() {
		//return (int) (600 - properties.getSPD());
		return 64000;
	}

	/**
	 * Increases the battlers tentative exp. Levels are gained after battle
	 */
	public void Gain_EXP(int target_level, int damage, ActiveSkill skill) {
		int d = damage;
		float f = (target_level / this.properties.LEVEL);
		if (this.current_action.kind == BattleAction.Kind.attack)
			this.tentative_exp += (int) (d * f);
		else if (this.current_action.kind == BattleAction.Kind.special)
			if (skill.is_attacking_skill())
				this.tentative_exp += (int) (d * f * (float) (skill.sp_cost / this.properties
						.MAX_SP()));
			else if (skill.is_healing_skill())
				this.tentative_exp += (int) (-d * f);
	}

	/**
	 * Returns the maximum range of the battlers special attacks
	 */

	public int max_special_range() {
		int b = 0;
		for (ActiveSkill S : properties.skills.activeskills()) {
			b = Math.max(b, S.Range());
		}
		return b;
	}

	public boolean is_enemy() {
		LinkedList<Battler> enemies = GameScene.Enemies;
		enemies.addAll(GameScene.Dead_Enemies);
		if (enemies.contains(this))
			return true;
		else
			return false;
	}

	// TODO: all this
	// private LinkedList<HexTile> _move_positions = new LinkedList<HexTile>();

	// public int skill_counter; //begins at zero. Every frame adds 1
	// public int skill_threshhold; //skill used when skillcounter exceeds skill
	// threshold
	// Large_Size enemies that occupy multiple tiles

	public Battler(Actor A, int y, int x) // Or an enemy. Acotrs and enemies
											// will both require a interface to
											// manage their morph into Battlers
	{
		properties = A;
		Y = y;
		X = x;
		mx = 0; // Movement X;
		my = 0; // Movement Y
		rx = 0;
		ry = 0;
		_direction = HexDirectional.SE();
		Move_queue = new LinkedList<HexTile>();
		current_action = new BattleAction();
		// damage = 0;
		// damage_pop = false;
		// critical = false;
		is_flying = false;
		is_swimming = false;
		has_moved = false;
		has_acted = false;
		// through = false;
		fixed_direction = false;
		step_anime = true;
		use_effects = new Battle_Effects();
		hit_effects = new Battle_Effects();
		// tactic = TacticType.Offensive;
		anim_speed = 12;
		try {
			sprite = new SpriteSet("Sprites/Characters/Human/"
					+ properties.cclass.name + ".png", 4, 6);
		} catch (Exception e) {
			sprite = new SpriteSet("Sprites/Characters/Human/NULLSET" + ".png",
					4, 6);
		}
		// WPN
		try {
			wpn_graphic = new Sprite("Sprites/Icons/Weapons/"
					+ properties.weapon.name + ".png");
		} catch (Exception e) {
			wpn_graphic = new Sprite("Sprites/Icons/Weapons/NULLSET" + ".png");
		}
		// Face
		try {
			// String s = properties.is_male ? "m" : "f";
			// face_graphic = new FaceSprite(properties.cclass.Name + "_" + s +
			// ".png");
			face_graphic = new FaceSprite(properties.cclass.name + ".png");
		} catch (Exception e) {
			face_graphic = new FaceSprite("NULLSET.png");
		}
	}

	public Battler() {
	}

	public Animation use_anim() {
		return Database.animations[properties.weapon != null ? properties.weapon.use_anim_id
				: 0];
	}

	/**
	 * If slip damage is applicable, apply it
	 */
	public void apply_slip_damage() {
		if (is_dead())
			return;
		int damage = 0;
		Status status = properties.state;
		if (status.is_slip_damage()) {
			damage += (properties.MAX_HP() * (status.slip_damage_float()));
			//damage += status.slip_damage_value;
		}
		if (damage > 0) {
			// target_animation = target_animation.Number(damage.ToString(),
			target_animation = AnimationInstance.Number(
					Integer.toString(damage), GameColor.DARKCYAN);
			Base.wait = Base.Pop_Damage_Frames;
			properties.HP -= damage;
		}
	}

	/**
	 * Removes all states and battler properties at the end of their turn
	 */
	public void natural_state_removal() {
		properties.remove_auto_states(Base.RANDOMIZER);
		// remove blink
		has_moved = false;
		has_acted = false;
		hit_effects = new Battle_Effects();
		reset_metavariables();
		// damage = 0;
		// remove states if properties.states suggests so TODO
	}

	/**
	 * Resets the meta variables associated with the battler
	 */
	public void reset_metavariables() {
		step_anime = true;
		fixed_direction = false;
	}

	/**
	 * Corrects tentative damage for direction and defending
	 */

	private int correct_damage(HexDirectional attacker_direction, int damage) {

		if (damage < 0)
			return damage; // if healing, don't proceed
		int v = HexTable.CompareTo(attacker_direction, _direction);
		if (v == 1) // if attack comes from the back, do 1.5x damage
			damage = (int) (damage * 1.5f);
		else if (v == -1)// if it comes from the front, do .75x damage
			damage = (int) (damage * 0.75f);
		if (use_defend())// if the target is defending, do .5x damage
			damage = (int) (damage * 0.5f);
		if (hit_critical())// if the target is critically attacked
			damage = (int) (damage * 2.0f);
		// A minimum of 1 damage is done. There is no zero damage.
		damage = Math.max(1, damage);
		return damage;
	}

	/**
	 * Returns the standard field of attack for the battler
	 */
	public int field() {
		// TODO: Implement this!
		return 0;

	}

	/**
	 * Restores the battlers sp and plays the relevant animation
	 */
	public void recover_sp(int sp) {
		if (sp == 0)
			return;
		sp = Math.min(sp, Math.abs(properties.MAX_SP() - properties.SP));
		properties.SP += sp;
		damage_animation = AnimationInstance.Number(Integer.toString(sp),
				GameColor.BLUE);
	}

	/**
	 * Corrects and deals damage to the battler
	 */
	public int deal_damage(int damage, HexDirectional d) {

		correct_damage(d, damage);
		if (damage == 0) {
			target_animation = AnimationInstance.Number("miss",
					GameColor.YELLOWGREEN);
			return damage;
		}
		System.out.println(damage + " damage done!");
		if (damage >= 0)// if the attack does positive damage
		{
			if (Math.max(damage, properties.HP) == properties.HP)// if the
																	// attack
																	// doesn't
																	// kill
			{
				properties.HP -= damage;
				// animation = AnimationInstance.number(damage.ToString(),
				// Color.PaleVioletRed);
				// return;
			} else {
				damage = properties.HP;
				properties.HP = 0;
				// return;
			}
			// Changes the color of the animation depending on whether the
			// attack is doing critical damage
			target_animation = !(hit_critical()) ? AnimationInstance.Number(
					Integer.toString(damage), GameColor.PALEVIOLETRED)
					: AnimationInstance.Number(Integer.toString(damage),
							GameColor.MEDIUMVIOLETRED);
			return damage;
		}

		else if (damage < 0)// attack heals
		{
			if (Math.min(properties.HP - properties.MAX_HP(), damage) == properties.HP
					- properties.MAX_HP())// if the heal doesn't overheal
			{
				properties.HP += (damage * -1);
				// return;
			} else {
				damage = properties.HP - properties.MAX_HP();
				properties.HP = properties.MAX_HP();
			}
			target_animation = !(hit_critical()) ? AnimationInstance.Number(
					Integer.toString(damage), GameColor.GREEN)
					: AnimationInstance.Number(Integer.toString(damage),
							GameColor.YELLOWGREEN);
		}

		return damage;
	}

	/**
	 * knocks the battler back one tile
	 */
	public void knockback(HexDirectional d) {
		step_anime = false;
		fixed_direction = true;
		move_to_next(d);
		fix_coordinates(d);
	}

	/**
	 * Returns the animation the battler uses in natural attacking her //
	 * opponents
	 */

	public Animation hit_anim() {
		return Database.animations[properties.weapon != null ? properties.weapon.hit_anim_id
				: 0];
	}

	/**
	 * Returns the natural attack range of the battler
	 */
	public int range() {
		if (properties.is_monster()) {
			// fuck. What should go here? TODO
			return 1;
		} else if (properties.weapon != null) {
			return properties.weapon.range;
		} else
			return 1;
	}

	/**
	 * Determines if the battler is capable of taking their turn
	 */

	public boolean status_influence() {
		// the battler absolutely cannot act. Thus, they also loose their wait
		// turn, and they are skipped
		if (properties.status_cannot_move() && properties.status_cannot_act())
			return false;
		if (properties.status_cannot_move())
			has_moved = true;
		if (properties.status_cannot_act())
			has_acted = true;
		current_action.clear();
		return true;
	}

	/**
	 * Returns the battlers real coordinates
	 */
	public Rectangle rect() {
		return new Rectangle(real_X(), real_Y(), sprite.width(),
				sprite.height());

	}

	/**
	 * Returns the battlers coordinates within the camera view. Returns the zero
	 * rectangle if off camera.
	 */
	public Rectangle coordinate_rectangle(Rectangle camera) {
		Rectangle r = rect();
		r.x -= camera.x;
		r.y -= camera.y;
		if (r.x < 0 || r.x > camera.width || r.y < 0 || r.y > camera.height)
			return new Rectangle(0, 0, 0, 0);
		else
			return r;
	}

	/**
	 * returns true if the battler is considered out-of-play
	 */
	public boolean is_dead() {
		return (properties.HP == 0);
	}

	/**
	 * The Update method
	 */
	public void update() {
		// targeting = false;
		// Base.n_frame_check(4) is the default speed
		if (step_anime && Base.n_frame_check(4))
			sprite.next_frame_column();
		if (!fixed_direction)
			sprite.Y_index = anim_direction_frame();
		// change movement depending on haste/slow states
		// If the battler is moving, perform movement operations
		if (is_moving()) {
			int n = sub_x();
			mx -= n; // remaining x movement subtracted
			rx += n; // appending real x added
			n = sub_y();
			my -= n; // remaining y movement subtracted
			ry += n; // appending real y added
			if (mx == 0 && my == 0) // If the remaining x and y = zero, change
									// x, y coordinates and reset real x and y
									// appensions
			{
				fix_coordinates(_direction);
				move_to_next(HexDirectional.None());
			}
		}

	}

	/**
	 * Corrects metavalues associated with movement. Fixes sloppy code elsewhere
	 */
	private void fix_coordinates(HexDirectional dir) {
		HexTile P = Pos();
		rx = 0;
		ry = 0;
		P = GameScene.map_array.D(P, dir);
		X = (int) P.X;
		Y = (int) P.Y;
	}

	/**
	 * Whether the current attack facing the target is critical
	 */
	public boolean use_critical() {
		return use_effects.Critical;
	}

	public void set_use_critical(boolean value) {
		use_effects.Critical = value;
	}

	/**
	 * Whether the current attack facing the target is critical
	 */
	public boolean use_counter() {
		return use_effects.Counter;
	}

	public void set_use_counter(boolean value) {
		use_effects.Counter = true;
	}

	/**
	 * Whether the current attack facing the target is defended
	 */
	public boolean use_defend() {
		return use_effects.Defending;
	}

	public void set_use_defend(boolean value) {
		use_effects.Defending = true;
	}

	/**
	 * Whether the target will successfully evade an attack
	 */
	public boolean use_evade() {
		return use_effects.Evading;
	}

	public void set_use_evade(boolean value) {
		use_effects.Evading = value;
	}

	/**
	 * Whether the current attack facing the target is Knockback
	 */
	public boolean use_knockback() {
		return use_effects.Knockback;
	}

	public void set_use_knockback(boolean value) {
		use_effects.Knockback = value;
	}

	/**
	 * Whether the current attack facing the target is Knockback
	 */
	public boolean use_doublestrike() {
		return use_effects.DoubleStrike;
	}

	public void set_use_doublestrike(boolean value) {
		use_effects.DoubleStrike = value;
	}

	/**
	 * Whether the current attack facing the target is critical
	 */
	public boolean hit_critical() {
		return hit_effects.Critical;
	}

	public void set_hit_critical(boolean value) {
		hit_effects.Critical = value;
	}

	/**
	 * Whether the current attack facing the target is Knockback
	 */
	public boolean hit_knockback() {
		return hit_effects.Knockback;
	}

	public void set_hit_knockback(boolean value) {
		hit_effects.Knockback = value;
	}

	/**
	 * Whether the current attack facing the target is Doublestrike
	 */
	public boolean hit_doublestrike() {
		return hit_effects.Critical;
	}

	public void set_hit_doublestrike(boolean value) {
		hit_effects.Critical = value;
	}

	/**
	 * Returns true if the battler has a healing skill in their inventory
	 */
	public boolean can_heal() {
		for (ActiveSkill s : properties.skills.activeskills()) {
			if (s.is_healing_skill())
				if (properties.skill_can_use(s))
					return true;
		}
		return false;
	}

	/**
	 * Returns the true Y coordinate of the topleft location of the sprite
	 */
	public int real_Y() {
		return (48 * Y) + ry;
	}

	/**
	 * returns the true X coordinate of the topleft location of the sprite
	 */
	public int real_X() {
		if (Y % 2 == 0)
			return (Base.tile_pixel_width * X) + rx;
		else
			return (Base.tile_pixel_width * X) + (Base.tile_pixel_width / 2)
					+ rx;
	}

	/**
	 * returns the corresponding int value associated with a sprites direction
	 */
	private int anim_direction_frame() {
		if (_direction.E)
			return 3;
		if (_direction.NE)
			return 1;
		if (_direction.NW)
			return 0;
		if (_direction.W)
			return 2;
		if (_direction.SW)
			return 4;
		if (_direction.SE)
			return 5;
		else
			return 255;
	}

	/**
	 * The x value to subtract per movement call
	 */
	private int sub_x() {
		if (mx == 0)
			return 0;
		if (_direction.equals(HexDirectional.E()))
			return 4;
		if (_direction.equals(HexDirectional.NE()))
			return 2;
		if (_direction.equals(HexDirectional.NW()))
			return -2;
		if (_direction.equals(HexDirectional.W()))
			return -4;
		if (_direction.equals(HexDirectional.SW()))
			return -2;
		if (_direction.equals(HexDirectional.SE()))
			return 2;
		else
			return 1;
	}

	/**
	 * The y value to subtract per movement
	 */
	private int sub_y() {
		if (my == 0)
			return 0;
		if (_direction.equals(HexDirectional.E()))
			return 0;
		if (_direction.equals(HexDirectional.NE()))
			return -3;
		if (_direction.equals(HexDirectional.NW()))
			return -3;
		if (_direction.equals(HexDirectional.W()))
			return 0;
		if (_direction.equals(HexDirectional.SW()))
			return 3;
		if (_direction.equals(HexDirectional.SE()))
			return 3;
		else
			return 1;
	}

	/**
	 * turns the battler to face in the direction of hextile h
	 */
	public void turn_to(HexTile h) {
		HexDirectional temp = HexTable.travel_adjacent(Pos(), h);
		if (!temp.equals(HexDirectional.None()))
			_direction = temp;
	}

	/**
	 * Returns true if the battler is moving or set to move
	 */
	public boolean is_moving() {
		if (mx == 0 && my == 0) {
			rx = 0;
			ry = 0;
			return false;
		} else
			return true;
	}

	private int _move_queue_index;

	public void set_movement_path(LinkedList<HexTile> path) {
		Move_queue = path;
		_move_queue_index = 1;
		move_to_next(HexDirectional.None());
	}

	/**
	 * Moves the battler to the next expected tile in the move_queue
	 */
	private void move_to_next(HexDirectional dir) {
		HexDirectional D;
		// Because I stil don't understand switch/case with bit flag enums...
		if (dir.equals(HexDirectional.None())) {
			if (_move_queue_index >= Move_queue.size())
				return;
			D = HexTable.travel_adjacent(Pos(),
					Move_queue.get(_move_queue_index));
			_move_queue_index++;
		} else
			D = dir;
		// NORTHWEST
		if (D.NW) {
			mx = (int) (Base.tile_pixel_width * -.5);
			my = (int) (Base.tile_pixel_height * -.75);
			_direction = HexDirectional.NW();
		}
		// NORTHEAST
		if (D.NE) {
			mx = (int) (Base.tile_pixel_width * .5);
			my = (int) (Base.tile_pixel_height * -.75);
			_direction = HexDirectional.NE();
		}
		// WEST
		if (D.W) {
			mx = (int) (Base.tile_pixel_width * -1);
			my = 0;
			_direction = HexDirectional.W();
		}
		// EAST
		if (D.E) {
			mx = (int) (Base.tile_pixel_width);
			my = 0;
			_direction = HexDirectional.E();
		}
		// SOUTHWEST
		if (D.SW) {
			mx = (int) (Base.tile_pixel_width * -.5);
			my = (int) (Base.tile_pixel_height * .75);
			_direction = HexDirectional.SW();
		}
		// SOUTHEAST
		if (D.SE) {
			mx = (int) (Base.tile_pixel_width * .5);
			my = (int) (Base.tile_pixel_height * .75);
			_direction = HexDirectional.SE();
		}
		D = HexDirectional.None();
	}

	/**
	 * returns the Hextile position of the Battler
	 * 
	 * @return
	 */
	public HexTile Pos() {
		return GameScene.map_array.Tile_At(Y, X);
	}

	public int turn_tick() {
		return (turn_threshhold() - turn);
	}

	public void turn_increment(int n) {
		turn = ((turn + n) % turn_threshhold());
	}

	/**
	 * The draw method
	 */
	public void draw(Draw_Object s_batch, Rectangle camera) {
		this.sprite.draw(s_batch);// (s_batch, outline_glow);
		if (target_animation != null && target_animation.has_next_frame())
			target_animation.Draw_Next(s_batch, coordinate_rectangle(camera));
		else
			target_animation = null;

	}

	/**
	 * Returns a single integer representative of the battlers position
	 */

	public int linear_pos_index() {
		return (Y * X) + X;
	}

	public void dispose() {
		wpn_graphic.dispose();
		sprite.dispose();
		face_graphic.dispose();
	}

	/**
	 * If weapons change, this resets the graphics.
	 */
	public void Refresh_Graphics() {
		// WPN
		try {
			wpn_graphic = new Sprite("Sprites/Icons/Weapons/"
					+ properties.weapon.name);
		} catch (Exception e) {
			wpn_graphic = new Sprite("Sprites/Icons/Weapons/NULLSET.png");
		}
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Battler xmlRead(Element element) {
		// TODO Auto-generated method stub
		return null;
	}
}
