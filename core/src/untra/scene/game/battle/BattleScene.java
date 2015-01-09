package untra.scene.game.battle;

import untra.graphics.AnimationInstance;
import untra.graphics.Draw_Object;
import untra.graphics.GameColor;
import untra.graphics.SpriteSet;

import java.util.LinkedList;

import untra.player.Actor;
import untra.scene.game.GameScene;
import untra.scene.game.map.HexDirectional;
import untra.scene.game.map.HexTable;
import untra.scene.game.map.HexTile;
import untra.scene.game.window.WindowBattleCommand;
import untra.scene.game.window.WindowBattlerFocus;
import untra.scene.game.window.WindowItem;
import untra.scene.game.window.WindowRevive;
import untra.scene.game.window.WindowSkill;
import untra.scene.game.window.WindowTile;
import untra.scene.title.TitleScene;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.database.ActiveSkill;
import untra.database.Animation;
import untra.database.Consumable;
import untra.database.Database;
import untra.database.IRanged;
import untra.database.Skill;
import untra.database.Skill_Scope;
import untra.driver.Audio;
import untra.driver.Base;
import untra.driver.Input;

/**
 * The BattleScene handles the actual tactical gameplay. Extends Gamescene. It
 * provides methods to handle the game logic and interaction. The game map
 * cannot be altered through this scene.
 * 
 * @author samuel
 * 
 */
public class BattleScene extends GameScene {
	private static boolean initialize_phase = false;
	private static int _phase;
	private static Battler _active_Battler, _next_up;
	private LinkedList<HexTile> bfspath = new LinkedList<HexTile>();
	private LinkedList<HexDirectional> bfsdirectional = new LinkedList<HexDirectional>();
	private LinkedList<HexTile> playervision = new LinkedList<HexTile>();
	/**
	 * Windows
	 */
	private static WindowBattleCommand windowset_action;
	private static WindowItem windowset_item;
	private static WindowSkill windowset_skill;
	private static WindowBattlerFocus windowset_status;
	private static WindowRevive windowset_revive;
	private static WindowTile windowset_tile;
	/**
	 * Temporary objects
	 * */
	private static ActiveSkill skill;
	private static Consumable item;
	private static LinkedList<Battler> targets, temp_targets;
	public static Animation use_anim, hit_anim;
	private HexDirectional prev_dir;
	private Battler temp_battler;
	public HexTile pl_retreat_loc;
	public HexTile en_retreat_loc;
	/**
	 * Sprites and Sound
	 */
	private SpriteSet move_arrowSet = new SpriteSet("Misc/ma.png", 2, 1);
	private SpriteSet Waiting_sprites = new SpriteSet("Misc/Wait.png", 6, 1);
	private SpriteSet movement_path_sprite = new SpriteSet(
			"Misc/move_marker.png", 2, 2);

	public void initialize() {
		super.initialize();
		initialize_windows();
		spawn_battlers();
		// TEMP
		setActive_Battler(Players.get(0));
	}

	private void initialize_windows() {
		windowset_action = new WindowBattleCommand();
		// windowset_detail = new Window_Actor_Status();
		windowset_item = new WindowItem();
		windowset_skill = new WindowSkill();
		// option Window
		// config Window
		windowset_status = new WindowBattlerFocus();
		windowset_revive = new WindowRevive();
		windowset_tile = new WindowTile();
	}

	public void update() {
		super.update();
		if (battle_ended && PHASE() != 7) {
			setPHASE(8);
			{
				// TODO: end this shit
			}
		}
		// update_selected();
		switch (PHASE()) {
		case 0:
			PHASE_0();
			break;
		case 1:
			PHASE_1();
			break;
		case 2:
			PHASE_2();
			break;
		case 3:
			PHASE_3();
			break;
		case 4:
			PHASE_4();
			break;
		case 5:
			PHASE_5();
			break;
		case 6:
			PHASE_6();
			break;
		case 7:
			PHASE_7();
			break;
		case 8:
			PHASE_8();
			break;
		case 9:
			PHASE_9();
			break;
		default:
			break;
		}
		for (Battler B : Players) {
			B.update();
		}
		for (Battler B : Enemies) {
			B.update();
		}
	}

	public void update_windows() {
		// super.update();
		windowset_action.update();
		windowset_status.update();

		windowset_skill.update();
		windowset_item.update();
		windowset_tile.update();
		if (windowset_item.active || windowset_skill.active)

			cursor_active = false;
	}

	/**
	 * phase = 0 // map browsing phase = 1 // selecting turn action phase = 2 //
	 * using item phase = 3 // using skill phase = 4 // attacking phase = 5 //
	 * moveing phase = 6 // ending turn phase = 7 // AI Processing phase = 8 //
	 * battle over phase = 9 // debug phase = 10 // action processing
	 * (attack/skill/item)
	 * */
	private static int PHASE() {
		return _phase;
	}

	private static void setPHASE(int value) {
		_phase = value;
		initialize_phase = true;
	}

	/**
	 * The current battler focus of the scene
	 * */
	private static Battler active_battler() {
		return _active_Battler;
	}

	private static void setActive_Battler(Battler value) {
		if (value != null) {
			System.out
					.println("Active Battler set to " + value.properties.Name);
			value.sprite.flash(GameColor.DARKGRAY.clone(), 60);
			windowset_status.setBattler(_active_Battler);
			_active_Battler = value;
			// camera.center_camera_on_battler(_Active_Battler);
			ab_reset();
		} else {
			System.out.println("Active Battler set to null");
			_active_Battler = null;
		}
	}

	/**
	 * Performs operations to reset the game focus to the Active Battler
	 * */
	private static void ab_reset() {
		clear_tilerange_sprites();
		camera.center_camera_on_battler(active_battler());
		if (_active_Battler == null) {
			System.out.println("ab_reset: Active Battler not set!");
			return;
		}
		if (!_active_Battler.is_enemy())
			windowset_action.active = true;

		// Check states to set has_moved, has_acted
		windowset_action.setBattler(_active_Battler);
		windowset_status.setBattler(_active_Battler);
		// windowset_detail.setBattler(_active_Battler);
		windowset_action.index = 0;
		windowset_status.active = true;
		windowset_help.move_to_top();
		if (_active_Battler.is_enemy()) {
			setPHASE(7);
		} else {
			windowset_action.active = true;
			windowset_help.active = true;
			setPHASE(1);
		}
	}

	private static void set_next_battler(Battler value) {
		_next_up = value;
	}

	/**
	 * gets the next battler to take their turn. This should be called from a
	 * seperate thread
	 * */
	private static void get_next_battler() {
		Battler next = null;
		int min = Short.MAX_VALUE;
		int n;
		for (Battler B : All_Battlers()) {
			n = B.turn_tick();
			if (n < min) {
				min = n;
				next = B;
			}
		}
		for (Battler B : All_Battlers()) {
			B.turn_increment(min);
		}
		set_next_battler(next);
	}

	/**
	 * Returns the range of all attack, special and healing of the battler
	 * */
	private LinkedList<HexTile> All_Range() {
		LinkedList<HexTile> range = heal_range;
		range.addAll(special_range);
		range.addAll(attack_range);
		return range;
	}

	private void spawn_battlers() {
		Players = new LinkedList<Battler>();
		Enemies = new LinkedList<Battler>();
		Dead_Enemies = new LinkedList<Battler>();
		Dead_Players = new LinkedList<Battler>();
		Battler B;
		targets = new LinkedList<Battler>();
		temp_targets = new LinkedList<Battler>();
		// TEMP remove this!
		map_array.playerStartHexTiles.add(new HexTile(6, 6));
		// TEMP remove above!
		// Add actors to their starting locations
		for (Actor actor : Base.party.Members) {
			// B = new Battler(actor, 0, 0);
			B = new Battler(actor, 0, 0);// A new Battler is created for every
											// Actor within the Base.party.
			B.Y = map_array.playerStartHexTiles.get(0).Y;
			B.X = map_array.playerStartHexTiles.get(0).X;
			map_array.playerStartHexTiles.remove(0);
			if (B.is_dead())
				Dead_Players.add(B);
			else
				Players.add(B);
		}
		// Add enemies to their starting positions
		// TEMP
		Actor a = new Actor(Database.classes[8], 12);
		// a.setMAX_HP(a);
		B = new Battler(a, 10, 8);
		Enemies.add(B);
		// TEMP
		if (Players.size() == 0) {
			System.out
					.println("Error: No players available for battle. Exiting to Title.");
			Base.setScene(new TitleScene());
			return;
		}

	}

	/**
	 * Draws a number of pretty intro graphics before the battle official
	 * begins. Its super pretty
	 */
	public void battle_start() {

	}

	/**
	 * Map browsing, no set objective
	 * */
	private void PHASE_0() {
		if (initialize_phase) {
			// launches certain windows
			windowset_help.active = false;
			windowset_tile.active = true;
		}
		if (freeze_scrolling && active_battler() == null)
			freeze_scrolling = false;
		if (Input.triggerA()) {

		}
		if (Input.triggerB()) {
			windowset_tile.active = false;
			ab_reset();
		}
		hovering = cursor_index();
		if (hovering != null)
			windowset_tile.refresh(hovering);
	}

	/**
	 * Actor Menu, choosing action
	 * */
	private void PHASE_1() {
		if (initialize_phase) {
			// launches menus and windows
			windowset_help.active = true;
			windowset_action.Initialize_Basic();
			windowset_action.active = true;
			windowset_skill.Initialize(active_battler().properties);
			windowset_item.Initialize();
			// clears all highlighted tiles
			clear_tilerange_sprites();
			initialize_phase = false;
			return;
		}
		// General Processing for the active battler
		if (active_battler() != null) {
			// No thinking if the active battler is moving
			if (active_battler().is_moving()) {
				return;
			}
			// If the active battler is dead...
			if (active_battler().is_dead()) {
				// TODO: Natural Removal of States
				setActive_Battler(null);
				windowset_action.active = false;
				// WindowSet_Action.hide();
				setPHASE(0);
				// TODO: Next Actor
			}
			// If the Action Window is active, it should also be visible
			if (windowset_action.active) {
				// If the active battler has moved and acted, it's turn ends
				if (active_battler().has_acted && active_battler().has_moved) {
					setPHASE(6);

					windowset_action.active = false;
					// WindowSet_Action.hide();
					return;
				}
			}

			// // Cancel Status Menu
			// if (Input.triggerB()) {
			// Audio.SE_Cancel.play();
			// // windowset_detail.active = false;
			// windowset_action.active = false;
			// windowset_status.active = false;
			// return;
			// }

			/*
			 * // Cancel Confirm Box if (Input.triggerB() &&
			 * windowset_confirm.active) { windowset_confirm.active = false;
			 * windowset_action.active = true; windowset_status.active = true;
			 * return; }
			 */

			// Cancel Skill Menu
			if (Input.triggerB() && windowset_skill.active) {
				Audio.SE_Cancel.play();
				windowset_skill.active = false;
				windowset_action.active = true;
				windowset_status.active = true;
				windowset_help.move_to_top();
				return;
			}

			// Cancel Item Menu
			if (Input.triggerB() && windowset_item.active) {
				Audio.SE_Cancel.play();
				windowset_item.active = false;
				windowset_action.active = true;
				windowset_status.active = true;
				windowset_help.move_to_top();
				return;
			}

			// Cancel Action screen. Return to map browsing.
			if (Input.triggerB() && windowset_action.active) {
				Audio.SE_Cancel.play();
				windowset_action.active = false;
				// WindowSet_Action.hide();
				windowset_status.active = false;
				// WindowSet_Status.hide();
				setPHASE(0);
				cursor_active = true;
				return;
			}

			// Confirm / Deny selection
			// if ((Input.triggerA() && windowset_confirm.active)
			// || (Input.triggerA() && Input
			// .cursorInRectangle(windowset_confirm.cursor_rect))) {
			// Audio.SE_Selection.play();
			// windowset_confirm.active = false;
			// // WindowSet_Confirm.hide();
			// switch (windowset_confirm.index) {
			// // confirm
			// case 0: {
			// setPHASE(6);
			// break;
			// }
			// // cancel
			// case 1: {
			// windowset_action.active = true;
			// break;
			// }
			// }
			// return;
			// }

			// Item Menu selection
			// The player selected an item for use : battle.
			if ((Input.triggerA() && windowset_item.active)
					&& (Input.triggerA() && Input
							.cursorInRectangle(windowset_item.cursor_rect))) {
				if (Base.party.Items.size() == 0) {
					Audio.SE_Buzzer.play();
					return;
				}
				setPHASE(2);
				// windowset_help.move_to_bottom();
				return;
			}

			// Skill Menu Selection
			// The player selected a skill for use : battle.
			if (Input.triggerA() && windowset_skill.active
					&& (Input.cursorInRectangle(windowset_skill.cursor_rect))) {
				if (active_battler().properties.skills.size() == 0) {
					Audio.SE_Buzzer.play();
					return;
				}
				setPHASE(3);
				// windowset_help.move_to_bottom();
				return;
			}

			// Action Selection, decide which action to take next
			if (Input.triggerA() && windowset_action.active
					&& (Input.cursorInRectangle(windowset_action.cursor_rect))) {
				windowset_status.active = true;
				// WindowSet_Status.hide();
				if (windowset_action.focus == WindowBattleCommand.FOCUS_BASIC) {
					switch (windowset_action.index) {
					case (0): // Move
					{
						if (active_battler().has_moved) {
							Audio.SE_Buzzer.play();
							return;
						}
						Audio.SE_Selection.play();
						check_area(active_battler(), 1, null);
						windowset_action.active = false;
						// WindowSet_Action.hide();
						// set active cursor
						cursor_active = true;
						setPHASE(5); // MOVE PHASE
						bfspath.clear();
						bfsdirectional.clear();

						break;
					}
					case (1): // Act
					{
						if (active_battler().has_acted) {
							Audio.SE_Buzzer.play();
							return;
						}
						Audio.SE_Selection.play();
						windowset_action.Initialize_Act();
						break;
					}

					case (2): // Wait / Defend
					{

						Audio.SE_Selection.play();
						// If defending, sets defending flag
						if (!active_battler().has_acted)
							active_battler().set_use_defend(true);
						windowset_action.active = false;
						setPHASE(6); // END PHASE
						break;
					}
					}
				} else if (windowset_action.focus == WindowBattleCommand.FOCUS_ACTION) {
					switch (windowset_action.index) {
					case (0): // Attack
					{
						if (active_battler().has_acted) {
							Audio.SE_Buzzer.play();
							return;
						}
						windowset_status.active = false;
						Audio.SE_Selection.play();
						check_area(active_battler(), 2, null);
						windowset_action.active = false;
						// WindowSet_Action.hide();
						cursor_active = true;
						setPHASE(4); // ATTACK PHASE
						break;
					}
					case (1): // Item
					{
						// If the active battler hasn't already acted
						if (active_battler().has_acted) {
							Audio.SE_Buzzer.play();
							return;
						}
						// windowset_status.active = false;
						// Displays the item selection menu.
						Audio.SE_Selection.play();
						windowset_action.active = false;
						windowset_item.active = true;
						windowset_item.index = 0;
						cursor_active = false;
						active_battler().current_action.kind = BattleAction.Kind.item;
						// //windowset_help.move_to_bottom();
						// windowset_help.active = true;
						break;
					}
					case (2): // Special
					{
						// If the active battler hasn't already acted
						if (active_battler().has_acted) {
							Audio.SE_Buzzer.play();
							return;
						}
						// windowset_status.active = false;
						// Displays the Special move selection menu.
						Audio.SE_Selection.play();
						// check_area(Active_Battler(), 1, null);
						windowset_action.active = false;
						windowset_skill.active = true;
						// WindowSet_Skill.Initialize(Active_Battler().Properties);
						windowset_skill.index = 0;
						cursor_active = false;
						active_battler().current_action.kind = BattleAction.Kind.special;
						// //windowset_help.move_to_bottom();
						break;
					}

					}
				}
				return;
			}

			// If the cursor is hovering over an option, it will change the Help
			// Box's flavor text
			// if (Input.in_rectangle(WindowSet_Action.cursor_rect))
			if (true)// TEMP
			{
				if (windowset_action.focus == WindowBattleCommand.FOCUS_BASIC) {

					switch (windowset_action.index) {
					case (0): // move
					{
						windowset_help
								.set_content("Move to an available tile.");
						break;
					}
					case (1): // action
					{
						windowset_help.set_content("Perform an action");
						break;
					}
					case (2): // Wait / Defend
					{
						if (!active_battler().has_acted)
							windowset_help.set_content("Ends this turn.");
						else
							windowset_help.set_content("Defend this turn.");
						break;
					}
					}
				} else if (windowset_action.focus == WindowBattleCommand.FOCUS_ACTION) {
					switch (windowset_action.index) {
					case (0): // Attack
					{
						windowset_help
								.set_content("Attack a reachable opponent");
						break;
					}
					case (1): // special
					{
						windowset_help.set_content("Use an item.");
						break;
					}
					case (2):// Item
					{

						windowset_help.set_content("Use one of "
								+ active_battler().properties.Name
								+ "'s special moves.");
						break;
					}
					}
				}
			}
		}
	}

	/**
	 * Using an Item
	 * */
	private void PHASE_2() {
		LinkedList<HexTile> range = new LinkedList<HexTile>();
		// Phase Initialize
		if (initialize_phase) {
			item = (Consumable) windowset_item.item;
			Audio.SE_Selection.play();
			active_battler().current_action = new BattleAction();
			active_battler().current_action.kind = BattleAction.Kind.item;
			cursor_active = true;
			windowset_item.active = false;
			Selected_Tile = null;
			initialize_phase = false;
			if (heal_range.size() == 0 && special_range.size() == 0) {
				// if healing item
				if (item.is_healing_item()) {
					check_area(active_battler(), 3, item);
					range = heal_range;
				}
				// else special item
				else {
					check_area(active_battler(), 4, item);
					range = special_range;
				}
			}
		}

		// if (!cursor_active && !windowset_confirm.active
		// && !windowset_revive.active) {
		// setPHASE(1);
		// cursor_active = false;
		// windowset_item.active = true;
		// // windowset_help.move_to_bottom();
		// windowset_status.skill = null;
		// clear_tilerange_sprites();
		// return;
		// }

		// Cancel
		if (Input.triggerB()) {
			Audio.SE_Cancel.play();
			cursor_active = false;
			// If the game was asking for confirmation before using the item, or
			// the revive
			// window was up, these windows should end and the cursor should
			// remain active

			// if ((windowset_confirm.active || windowset_revive.active)) {
			// windowset_confirm.active = false;
			// windowset_revive.active = false;
			// windowset_status.skill = null;
			// cursor_active = true;
			// }
			// else return to the item selection screen and PHASE 1

			windowset_item.update();
			windowset_item.active = true;
			// windowset_help.move_to_bottom();
			windowset_help.active = true;
			setPHASE(1);

		}

//		// If the selected item is meant to revive a character
//		if (item.scope == Skill_Scope.revive) {
//			if (windowset_revive.active) {
//				// I forgot what was supposed to go here CONTINUE
//			}
//		}

		if (heal_range.size() == 0 && special_range.size() == 0) {
			// if healing item
			if (item.is_healing_item()) {
				check_area(active_battler(), 3, item);
				range = heal_range;
			}
			// else special item
			else {
				check_area(active_battler(), 4, item);
				range = special_range;
			}
		} else if (range.size() == 0) {
			range.addAll(heal_range);
			range.addAll(special_range);
		}

		if (Input.triggerA()) {
			if (Selected_Tile.equals(cursor_index())) {
				set_meta_variables(active_battler().current_action);
				active_battler().turn_to(Selected_Tile);
				targets = get_targets(Selected_Tile, item.Range());
				windowset_item.refresh();
				cursor_active = false;
				// windowset_confirm.active = false;
				clear_tilerange_sprites();

			} else {
				Selected_Tile = cursor_index();
			}

		}
	}

	/**
	 * Selected_Tile = cursor_index(); Using a special move or skill
	 * */
	private void PHASE_3() {
		// LinkedList<HexTile> range = new LinkedList<HexTile>();
		// Initialize
		if (initialize_phase) {
			skill = windowset_skill.skill;
			if (!(active_battler().properties.skill_can_use(skill))) {
				Audio.SE_Buzzer.play();
				setPHASE(1);
				return;
			}
			Audio.SE_Selection.play();
			active_battler().current_action = new BattleAction();
			active_battler().current_action.kind = BattleAction.Kind.special;
			active_battler().current_action.id = skill.id;
			if (skill.is_healing_skill())
				check_area(active_battler(), 3, skill);
			else
				check_area(active_battler(), 4, skill);
			cursor_active = true;
			windowset_skill.active = false;

			if (heal_range.size() == 0 && special_range.size() == 0) {
				// if healing skill
				if (skill.is_healing_skill()) {
					check_area(active_battler(), 3, skill);
					// range = heal_range;
				}
				// else special item
				else {
					check_area(active_battler(), 4, skill);
					// range = special_range;
				}
			}
			Selected_Tile = null;
			initialize_phase = false;
		}

		// Cancel
		if (Input.triggerB()) {
			// If Revive Window is open
			if (windowset_revive.active) {
				windowset_revive.active = false;
				windowset_revive.index = 0;
			}
			Audio.SE_Cancel.play();
			setPHASE(1);
			clear_tilerange_sprites();
			cursor_active = false;
			// Clear Damage Preview
			windowset_skill.update();
			windowset_skill.active = true;
			// windowset_help.move_to_bottom();
			windowset_help.active = true;
			return;
		}

		// If the selected skill revives
//		if (skill.scope == Skill_Scope.revive) {
//			if (windowset_revive.active) {
//				windowset_status.setBattler(Dead_Players
//						.get(windowset_revive.index));
//			} else {
//				windowset_status.update();
//			}
//		}

		if (Input.triggerA()) {
			// If the player has confirmed he wants to use the selected skill
			// the selected tile will match the cursor index
			if (Selected_Tile != null) {
				if (Selected_Tile.equals(cursor_index())) {
					set_meta_variables(active_battler().current_action);
					// turn to target
					active_battler().turn_to(Selected_Tile);
					// Action processing
					// Clear confirmation window
					// windowset_confirm.active = false;
					// windowset_confirm.content = "";
					windowset_action.setBattler(active_battler());
					// Status Window reset skill, attacker
					clear_tilerange_sprites();
					if (active_battler().is_dead()) {
						active_battler().has_acted = false;
						active_battler().has_moved = false;
						setActive_Battler(null);
						cursor_active = false;
						setPHASE(0);
						freeze_scrolling = false;
					}
					Audio.SE_Selection.play();
					return;
				}
			}
			// otherwise the player has selected a new tile

			Selected_Tile = cursor_index();
			if (special_range.contains(Selected_Tile)
					|| heal_range.contains(Selected_Tile)) {
				// //windowset_help.move_to_bottom();
				// windowset_help.active = false;
				targets = get_targets(Selected_Tile, skill.Range());
				// If the skill has a target -or- does not require a target
				// proceed normally
				if (targets.size() >= 1 || skill.scope == Skill_Scope.none) {
					//
					Audio.SE_Selection.play();
					cursor_active = false;
					// Damage preveiw
					windowset_help.move_to_top();
					return;
				} 
				else {
					Audio.SE_Buzzer.play();
					Selected_Tile = null;
				}
			} else
				Selected_Tile = null;

		}

		// if ((Input.triggerA()) && windowset_revive.active) {
		// // If the selected tile is not occupied, and can hold the battler
		// // designated to be revived
		// String battlername =
		// Dead_Players.get(windowset_revive.index).properties.name;
		// if (!is_occupied()) // true
		// {
		// String s = String.format("Revive {0}?", battlername);
		// windowset_confirm.content = (s);
		// windowset_confirm.active = true;
		// windowset_revive.active = false;
		// // damage preview
		// return;
		// } else // false
		// {
		// Audio.SE_Buzzer.play();
		// // Informs the players the selected battler cannot be placed
		// // there"
		// String s = String.format("{0} cannot occupy that tile",
		// battlername);
		// windowset_help.temporary_message(s);
		// return;
		// }
		// }

		// If the player has selected a tile

	}

	/**
	 * Actor Attack
	 * */
	private void PHASE_4() {
		LinkedList<HexTile> range;
		if (initialize_phase) {
			check_area(active_battler(), 2, null);
			windowset_status.active = true;
			windowset_status.active = true;
			active_battler().current_action = new BattleAction();
			active_battler().current_action.kind = BattleAction.Kind.attack;
			cursor_active = true;
			Selected_Tile = null;
			initialize_phase = false;
		}
		range = attack_range;

		// Cancel
		if (Input.triggerB()) {
			if (Selected_Tile != null) {
				Cancel_Attack();
				return;
			} else {
				Audio.SE_Cancel.play();
				// Selected_Tile = cursor_index();
				windowset_action.active = true;
				setPHASE(1);
				// clear_tilerange_sprites();
				return;
			}
		}

		// Selection Made
		if (Input.triggerA()) {
			if (Selected_Tile != null) {
				if (Selected_Tile.equals(cursor_index())) {
					Confirm_Attack();
				} else {
					Selected_Tile = cursor_index();
					Cancel_Attack();
					windowset_help.active = true;
				}
			}
		}
		Selected_Tile = cursor_index();

		// Target Selected
		if (Input.triggerA()) {
			// if in_range?
			Selected_Tile = cursor_index();
			if (range.contains(Selected_Tile)) {
				Audio.SE_Selection.play();
				targets = get_targets(Selected_Tile, active_battler().range());
				targets = get_targets(Selected_Tile, active_battler().field());
				// If the target list contains himself.
				if (targets.contains(active_battler()))
					targets.remove(active_battler());
				if (targets.size() > 0) // if there are targets to aim for
				{
					Audio.SE_Selection.play();
					cursor_active = false;
					// String text = "Attack here?";
					// WindowSet_Status damage preview
					windowset_help.active = false;
					// windowset_confirm.content = (text);
					// windowset_confirm.active = true;
				} else {
					Selected_Tile = null;
					Audio.SE_Buzzer.play();
					return;
				}
			} else
				Audio.SE_Buzzer.play();
		}
	}

	/**
	 * Actor Movement
	 * */
	private void PHASE_5() {
		// Initialization operations
		if (initialize_phase) {
			windowset_status.active = true;
			check_area(active_battler(), 1, null);
			// pre_x = Active_Battler().X;
			// pre_y = Active_Battler().Y;
			hovering = cursor_index();
			bfspath = map_array.BFS(active_battler().Pos(), hovering);
			bfsdirectional = new LinkedList<HexDirectional>();
			initialize_phase = false;
		}
		// Check Attack Area if the confirmation window is up
		// Cancelation
		if (Input.triggerB() == true) {
			Audio.SE_Cancel.play();
			// windowset_confirm.active = false;
			// windowset_confirm.hidden = true;
			windowset_action.active = true;
			move_range.clear();
			// return to option selection
			setPHASE(1);
		}

		if (move_range.size() == 0)
			check_area(active_battler(), 1, null);

		// Tile Selected
		if (Input.triggerA()) {
			Selected_Tile = cursor_index();
			if (move_range.contains(Selected_Tile)) {
				if (!is_occupied()) {

					Audio.SE_Selection.play();
					// windowset_confirm.active = true;
					// windowset_help.active = false;
					// windowset_confirm.content = ("Move Here?");
					Move_Battler();
					clear_hextile_flags();
					move_range.clear();
					attack_range.clear();
					active_battler().has_moved = true;
					clear_tilerange_sprites();
					setPHASE(1);
					windowset_action.active = true;
					windowset_action.update();
					// if (!Active_Battler().has_acted)
					// windowset_action.active = true;
				} else
					Audio.SE_Buzzer.play();
			}
		}

		// no tile selected, get hovering tile;
		if (hovering != cursor_index()) {
			hovering = cursor_index();
			orientActiveBattlerToCursor();
			bfspath.clear();
			bfsdirectional.clear();
			if (move_range.contains(cursor_index())) {
				// gets the bfspath of the movement cursor
				bfspath = map_array.BFS(active_battler().Pos(), hovering);
				// creates the bfsdirectinal queue
				HexTile current, previous;
				LinkedList<HexDirectional> bfslist;
				bfslist = new LinkedList<HexDirectional>();
				for (int i = 1; i < bfspath.size(); i++) {
					current = bfspath.get(i);
					previous = bfspath.get(i - 1);
					bfslist.add(HexTable.travel_adjacent(previous, current));
				}
				bfsdirectional = bfslist;
			}
		}
	}

	public void Move_Battler() {
		/*
		 * int maxsearch = 64; HexTile n = Active_Battler().Pos();
		 * 
		 * for (int i = 0; n != Selected_Tile; i++) { if (i >= maxsearch) {
		 * throw new IndexOutOfBoundsException(
		 * "You are stuck : an endless for loop, and likely fucked by pathfinding."
		 * ); } n = ai.pathfind_to(n, Selected_Tile);
		 * Active_Battler().Move_queue.add(n); // if (n == Selected_Tile) break;
		 * }
		 */
		// Active_Battler().Move_PriorityBlockingQueue = AI.BFS_Path(n,
		// Selected_Tile);
		active_battler().set_movement_path(bfspath);

	}

	private static Battler next_up() {
		if (_next_up == null) {
			get_next_battler();
		}
		Battler temp = _next_up;
		get_next_battler();
		return temp;
	}

	/**
	 * Actor Wait / Defend
	 * */
	private void PHASE_6() {
		if (initialize_phase) {
			prev_dir = active_battler().direction();
			Waiting_sprites.setpos(new Vector2(active_battler().real_X(),
					active_battler().real_Y() - Base.tile_pixel_height));
			initialize_phase = false;
			cursor_active = false;
			windowset_action.active = false;
			windowset_help.active = false;
		}

		if (Input.triggerB()) {
			setPHASE(1);
			active_battler().setDirection(prev_dir);
			active_battler().set_use_defend(false);
			windowset_action.active = true;
			return;
		}

		orientActiveBattlerToCursor();

		Waiting_sprites.update();
		// direction selected
		if (Input.triggerA()) {
			set_wait_variables();
			setActive_Battler(next_up());
		}
	}

	/**
	 * Determines the direction the battler should face by examing the cursor
	 * coordinates, and changes the index of the waiting sprite.
	 */
	private void orientActiveBattlerToCursor() {

		HexDirectional direction = HexTable.travel_adjacent(active_battler()
				.Pos(), cursor_index());
		// Changes the battlers direction according to the cursor
		if (direction.NW) {
			active_battler().setDirection(HexDirectional.NW());
			Waiting_sprites.X_index = 0;

		} else if (direction.W) {
			active_battler().setDirection(HexDirectional.W());
			Waiting_sprites.X_index = 2;

		} else if (direction.SW) {
			active_battler().setDirection(HexDirectional.SW());
			Waiting_sprites.X_index = 4;

		} else if (direction.NE) {
			active_battler().setDirection(HexDirectional.NE());
			Waiting_sprites.X_index = 1;

		} else if (direction.E) {
			active_battler().setDirection(HexDirectional.E());
			Waiting_sprites.X_index = 3;
		} else if (direction.SE) {
			active_battler().setDirection(HexDirectional.SE());
			Waiting_sprites.X_index = 5;
		}

	}

	/**
	 * AI Processing (Not finished)
	 * */
	private void PHASE_7() {
		if (initialize_phase) {
			// STEP = 1;
			initialize_phase = false;
			temp_targets.clear();
			LinkedList<HexTile> vision = active_battler().is_enemy() ? GameScene
					.enemy_vision() : GameScene.player_vision();
			for (Battler B : opponents(active_battler().is_enemy())) {
				if (vision.contains(B.Pos()))
					temp_targets.add(B);
			}
			// AI.opponent_in_sight = (temp_targets.size() != 0);
		}
		// Heads up, the time complexity on AI processing is fucking huge.
		// Threading may be necesarry

		// ok, so we want to now find the best movable position for the
		// character.
		// first thing to check is if they can move, if not return their current
		// position.
		// otherwise, we need to determine if they can attack their 'attack
		// position'
		// without moving. So we will need to generate the attack grid to see if
		// they
		// can attack that position without moving from their current position.
		// If not
		// then they will do so, but if they can, they should do that, then
		// escape to a
		// safe distance. At least that is the way the AI is built. Examine the
		// code
		// for more details if you are interested.
		/*
		 * Can she act and move? First we see if the AB can attack from her
		 * current position, without moving. If not, we see if she can help an
		 * ally without moving. If she can move, she moves to a different
		 * position If she hasn't acted, determine if she should act; If she
		 * should,
		 */
		if (active_battler().is_moving() || camera.is_scolling())
			return;
		// vision is the hextiles : view of the enemy battlers
		// creates the list of opponents the AI controller can see
		// If the AI can see any opponents, it sets the target list to whatever
		// opponents the AB
		// can go after max.(movement + attack range), (skill_range))
		int reach = 0;
		// if the battler has not yet moved, its reach includes its movement.
		reach += active_battler().has_moved ? 0 : active_battler().properties
				.MOV();
		reach += active_battler().has_acted ? 0 : active_battler().range();
		int r = active_battler().has_acted ? reach : Math.max(reach,
				active_battler().max_special_range());
		targets.clear();
		targets = get_targets(active_battler().Pos(), r);
		if (!active_battler().has_acted) {
			Process_Action();
			return;
		} else if (!active_battler().has_moved) {
			Process_Movement();
			return;
		} else {
			Process_End();
			return;
		}
	}

	/**
	 * Battle Ended
	 * */
	private void PHASE_8() {

	}

	public static void reset_target_metavariables() {
		for (Battler B : targets) {
			B.reset_metavariables();
		}
	}

	/**
	 * Action processing (attack/skill/item)
	 * */
	private void PHASE_9() {
		if (initialize_phase) {
			STEP = 1;
			initialize_phase = false;
		}
		switch (STEP) {
		// Sets the action processing environment
		case 1: {
			/*
			 * if (All_Range[0] != null) {
			 * Active_Battler().turn_to(All_Range[0]); }
			 */
			active_battler().target_animation = new AnimationInstance(use_anim);
			Base.wait = use_anim.framecount;
			check_projectiles();

			if (active_battler().current_action.kind == BattleAction.Kind.special) {
				item = (Consumable) Database.consumables[0]; // sets the item to null
				windowset_help.temporary_message(skill.name);
			}
			if (active_battler().current_action.kind == BattleAction.Kind.item) {
				skill = new ActiveSkill(); // sets the skill to null
				windowset_help.temporary_message(item.name);
			}
			STEP = 2;
			return;
		}
		// Ensures nothing extraordinary is going on before performing damage
		// calculations
		case 2: {
			if (active_battler().is_moving())
				return;
			// if(Active_Battler() != null && projectile : motion)
			// return
			// Play target animations

			if (targets.size() > 0)
				targets.get(0).target_animation = new AnimationInstance(
						hit_anim);
			else {
				// no targets, play animation at tile
			}
			Base.wait = hit_anim.framecount - (Base.Pop_Damage_Frames / 2);
			STEP = 3;
			return;
		}
		// Damage calculations performed
		case 3: {
			Base.wait = Base.Pop_Damage_Frames;
			int damage;
			// if(targets.size() > 0) check_battle_effects(Active_Battler(),
			// targets[0]);
			for (int t = 0; t < targets.size(); t++) {
				damage = 0;
				if (active_battler().use_critical())
					targets.get(t).set_hit_critical(true);
				switch (active_battler().current_action.kind) {
				case attack: {
					// Active_Battler().accuracy =
					// BattleMath.Accuracy(Active_Battler(), t);
					damage = basic_attack(damage, targets.get(t));
					break;
				}
				case item: {
					// Active_Battler().accuracy = 1.0f;

						// Active_Battler().damage =
						// BattleMath.damage(Active_Battler(), item);
						damage = BattleMath.damage(active_battler(), item);
						// targets.get(t).damage = damage;
						damage = BattleMath.damage_corrected(damage,
								item.variance, Base.RANDOMIZER);
						targets.get(t).recover_sp(
								BattleMath.sp_restore(targets.get(t), item));

					break;
				}
				case special: {
					active_battler().properties.SP -= skill.sp_cost;
					if (!BattleMath.is_miss(active_battler(), targets.get(t),
							Base.RANDOMIZER)) {
						damage = BattleMath
								.damage(active_battler(), targets.get(t),
										active_battler().current_action);
						damage = BattleMath.damage_corrected(damage,
								skill.variance, Base.RANDOMIZER);
					}
					break;
				}
				}
				damage = targets.get(t).deal_damage(damage,
						active_battler().direction());
				// TEMP commented out exp gain
				// Active_Battler().Gain_EXP(targets.get(t).Properties.LEVEL,
				// damage, skill);
			}
			active_battler().set_use_critical(false);
			// targets.Clear();
			STEP = 4;
			return;
		}
		// post action processing
		case 4: {
			if (!Base.USING_POST_ACTION_PROCESSING) {
				avoid_processing();
				break;
			}
			// check for doublestrike, knockback, counter, extra stuff
			if (active_battler().current_action.kind != BattleAction.Kind.item) // item
																				// use
																				// does
																				// not
																				// apply
			{
				LinkedList<Battler> targets2 = new LinkedList<Battler>();
				// check for doublestrike
				for (int t = 0; t < targets.size(); t++) {
					// if active battler automatically doublstrikes, set so
					if (active_battler().use_doublestrike())
						targets.get(t).set_hit_doublestrike(true);
					// if the target is doublestruck, add them to new target
					// list
					if (targets.get(t).hit_doublestrike()) {
						targets2.add(targets.get(t));
						targets.get(t).set_hit_doublestrike(false);
					}
					// prevents infinite doublestrikes
					active_battler().set_use_doublestrike(false);
				}
				// if the new target list is empty, proceed
				// else, attack the targets again
				if (targets2.size() > 0) {
					targets = targets2;
					STEP = 3;
					return;
				}

				// check for knockback
				for (int t = 0; t < targets.size(); t++) {
					// if active battler automatically knocks back, set so
					if (active_battler().use_knockback())
						targets.get(t).set_hit_knockback(true);
					// if the target is knocked back, add them to new target
					// list
					if (targets.get(t).hit_knockback()) {
						targets2.add(targets.get(t));
						targets.get(t).set_hit_knockback(false);
					}
					// prevents infinite knockback
					active_battler().set_hit_knockback(false);
				}
				LinkedList<Battler> targets_in_range = new LinkedList<Battler>(
						targets);
				// gets the list of battlers who are not also knocked back
				for (Battler B : targets)
					targets_in_range.remove(B);
				for (Battler B : targets2) {
					// if the target can be knocked back, knock them back
					if (map_array.passable(
							map_array.D(B.Pos(), active_battler().direction()),
							targets_in_range))
						B.knockback(active_battler().direction());
				}
				targets2.clear();

				// check for counter
				// first it will switch the active battler if a target was set
				// to counter
				if (temp_battler != null) {
					setActive_Battler(temp_battler);
					temp_battler = null;
					targets = temp_targets;
					temp_targets = null;
				}
				for (int t = 0; t < targets.size(); t++) {
					// if a target can counter
					if (targets.get(t).use_counter()) {
						// temporarily sets them as the active battler.
						// the following code is a big exception to the rule for
						// what should be allowed with Active_Battler()
						// COUNTER:
						temp_battler = active_battler();
						_active_Battler = targets.get(t);// note that this does
															// not call the get
															// accesor of the
															// Active_Battler()
						temp_targets = targets;
						active_battler().turn_to(temp_battler.Pos());
						active_battler().current_action.clear();
						active_battler().current_action.kind = BattleAction.Kind.attack;
						targets = get_targets(active_battler().Pos(),
								active_battler().range());
						// int damage = 0;
						// basic_attack(ref damage, active_temp);
						// active_temp.deal_damage(ref damage,
						// Active_Battler().direction);
						active_battler().set_use_counter(false);
						use_anim = active_battler().use_anim();
						hit_anim = active_battler().hit_anim();
						STEP = 1;
						return;
					}
				}
			}
		}
		// action processing is over. This step determines where to return
		// control
		case 5: {
			// TODO: check for common event occurance
			if (active_battler().is_dead()) {
				active_battler().has_moved = false;
				active_battler().has_acted = false;
				setActive_Battler(null);
				cursor_active = false;
				setPHASE(0);
				freeze_scrolling = false;
				setActive_Battler(next_up());
			}
			if (active_battler().is_enemy()) {
				setPHASE(7);
			} else if (active_battler().has_acted && active_battler().has_moved) {
				setPHASE(6);
			} else {
				windowset_action.active = true;
				windowset_action.setBattler(active_battler());
				setPHASE(1);
			}
			STEP = 0;
			break;
		}
		}
	}

	/**
	 * Sets all applicable variables to set attack : motion for player
	 * */
	private void Confirm_Attack() {
		active_battler().current_action.kind = BattleAction.Kind.attack;
		set_meta_variables(active_battler().current_action);
		// windowset_confirm.active = false;
		// WindowSet_Status.attacker = null;
		// WindowSet_Status.skill = null;
		windowset_action.setBattler(active_battler());
		windowset_action.active = true;
		clear_tilerange_sprites();
	}

	/**
	 * Cancels the user attack session, returns cursor control
	 * */
	private void Cancel_Attack() {
		Audio.SE_Selection.play();
		// windowset_confirm.active = false;
		// WindowSet_Status.attacker = null;
		// WindowSet_Status.skill = null;
		cursor_active = true;
		// setPHASE(1);
	}

	/**
	 * Scans area surrounding the selected battler 1 = move range 2 = attack
	 * range 3 = heal range 4 = skill range set s to null for types 1 and 2
	 * */
	private static void check_area(Battler selected, int type, IRanged s) {
		clear_tilerange_sprites();
		int atk_range_max = 0;
		int move_range_max = 0;
		int heal_range_max = 0;
		int skill_range_max = 0;
		if (type == 2) // attack range
		{
			atk_range_max = selected.range();
			attack_range = map_array.Passable_Range(selected.Pos(),
					atk_range_max, new LinkedList<Battler>());
			attack_range = HexTable.trimmed_range(attack_range);
			intention = 1;
		}
		if (type == 1) // move range
		{
			move_range_max = selected.properties.MOV();
			move_range = map_array.Passable_Range(selected.Pos(),
					move_range_max, All_Battlers());
			move_range = HexTable.trimmed_range(move_range);
			intention = 2;
			// selected.move_range = move_range;
		}
		if (type == 3) // heal range
		{
			heal_range_max = s.Range();
			heal_range = map_array.Passable_Range(selected.Pos(),
					heal_range_max, new LinkedList<Battler>());
			heal_range = HexTable.trimmed_range(heal_range);
			intention = 3;
			// heal_range = heal_range;
		}
		if (type == 4) // skill range
		{
			skill_range_max = s.Range();
			special_range = map_array.Passable_Range(selected.Pos(),
					skill_range_max, new LinkedList<Battler>());
			special_range = HexTable.trimmed_range(special_range);
			intention = 4;
			// selected.skill_range = special_range;
		}
	}

	/**
	 * Clears highlighted tile selection. Calling this removes all tiles from //
	 * the highlighted tile lists
	 * */
	private static void clear_tilerange_sprites() {
		move_range.clear();
		attack_range.clear();
		heal_range.clear();
		special_range.clear();
		intention = 0;
	}

	/**
	 * clears meta flags for the game map
	 * */
	private static void clear_hextile_flags() {
		map_array.clear_map_flags();
	}

	private void avoid_processing() {
		reset_target_metavariables();
		targets.clear();
		Base.wait = 8;
		STEP = 5;
	}

	/**
	 * clears the contents of the game screen. Strongly associated with
	 * thecamera
	 * */
	private static void clear_screen() {

	}

	/**
	 * Performs the necesarry calculations for a basic attack
	 * */
	private int basic_attack(int damage, Battler target) {
		// If the attack doesn't miss
		if (!BattleMath.is_miss(active_battler(), target, Base.RANDOMIZER)) {
			// determine necesarry flags and apply damage
			if (BattleMath.is_critical(active_battler(), target,
					Base.RANDOMIZER))
				target.set_hit_critical(true);
			if (BattleMath.is_doublestrike(active_battler(), target,
					Base.RANDOMIZER))
				target.set_hit_doublestrike(true);
			if (BattleMath.is_knockback(active_battler(), target,
					Base.RANDOMIZER))
				target.set_hit_knockback(true);
			damage = BattleMath.damage(active_battler(), target,
					active_battler().current_action);
			damage = BattleMath
					.damage_corrected(damage, 0.15f, Base.RANDOMIZER);
		}
		// The attack misses
		else {
			damage = 0;
		}
		return damage;
	}

	/**
	 * checks to see if projectiles need to be created (not finished)
	 * */
	private void check_projectiles() {
		// unfinished! CONTINUE
		HexTile t = Selected_Tile;
		Battler b = is_occupied() ? occupied_by(t.X, t.Y) : null;
		switch (active_battler().current_action.kind) {
		case attack: {
			if (active_battler().is_enemy()) {
				int x = active_battler().range();
			}
			break;
		}
		}
	}

	/**
	 * Sets the meta variables for phases 2-4 Meta variables include changing
	 * scene_main skill, item, use and hit anim designations
	 * */
	public static void set_meta_variables(BattleAction act) {
		Audio.SE_Selection.play();
		skill = new ActiveSkill();
		item = new Consumable();
		active_battler().turn_to(Selected_Tile);
		if (act.kind == BattleAction.Kind.special) {
			if (skill.id == 0)
				skill = Database.activeskills[(int) act.id];
			active_battler().properties.SP -= skill.sp_cost;
			use_anim = Database.animations[skill.anim_use_id];
			hit_anim = Database.animations[skill.anim_hit_id];
		}
		if (act.kind == BattleAction.Kind.item) {

			if (item.id == 0)
				item = (Consumable) Database.consumables[(int) act.id];
			Base.party.Items.remove(item);
			use_anim = Database.animations[item.anim_use_id];
			hit_anim = Database.animations[item.anim_hit_id];
		}
		if (act.kind == BattleAction.Kind.attack) {
			use_anim = active_battler().use_anim();
			hit_anim = active_battler().hit_anim();
		}
		active_battler().has_acted = true;
		// sort_battlers();
		setPHASE(9);
	}

	public static void set_wait_variables() {
		// hide waiting_sprites
		if (!active_battler().use_defend())
			active_battler().current_action = new BattleAction(
					BattleAction.Kind.defend);
		else
			active_battler().current_action.clear();
		active_battler().has_acted = false;
		active_battler().has_moved = false;
		// Check for certain event triggers (end of turn things such as doom
		// counter, etc.)
		// Process slip damage
		active_battler().apply_slip_damage();
		active_battler().natural_state_removal();
		setActive_Battler(null);
		cursor_active = false;
		setPHASE(0);
		freeze_scrolling = false;
	}

	private void Draw_Movement_Arrow(Draw_Object s_batch) {
		// System.out.println("draw movement arrow called");
		if (bfspath.size() < 2)
			return;
		if (!move_range.contains(cursor_index()))
			return;
		HexTile drawtile;
		Rectangle P;
		int z_height;
		HexDirectional B, F;
		F = HexTable.travel_adjacent(active_battler().Pos(), cursor_index());
		z_height = active_battler().Pos().z_height();
		for (int i = 1; i < bfsdirectional.size(); i++) {

			drawtile = bfspath.get(i);
			// sets the z_height to the active battlers height.
			// this is because it prevents odd artifacts

			int u = drawtile.Y;
			int v = drawtile.X;

			P = initializePositionRect(u, v);
			P.y -= z_height * Base.tile_pixel_z;
			P.height += z_height * Base.tile_pixel_z;
			// Ascertain within Camera View
			if (!camera.intersects(P))
				return;
			B = bfsdirectional.get(i - 1).opposite();
			F = bfsdirectional.get(i);
			// if the start is from the east or west
			if (B.W || B.E || F.W || F.E)
				movement_path_sprite.X_index = 1;
			else
				movement_path_sprite.X_index = 0;
			// if the start is from the western front
			movement_path_sprite.fliphorz = false;
			if (B.E || F.E) {
				if (B.NW || F.NW)
					movement_path_sprite.fliphorz = true;
			}
			if (B.SW || F.SW) {
				if (B.NE || F.E || B.E || F.NE)
					movement_path_sprite.fliphorz = true;
			}
			if (B.SE || F.SE) {
				if (B.NE || F.NE)
					movement_path_sprite.fliphorz = true;
			}
			// if the start is opposite the finish
			if (B.opposite().equals(F))
				movement_path_sprite.Y_index = 0;
			else
				movement_path_sprite.Y_index = 1;
			// special case for w-se and e-sw
			if ((B.SE && F.W) || (B.SW && F.E) || (B.W && F.SE)
					|| (B.E && F.SW))
				movement_path_sprite.flipvert = true;
			else
				movement_path_sprite.flipvert = false;
			// TEMP: special case for NW/SW and NE/SE
			int x_plus = 0;

			// if (B.opposite().equals(F)) {
			// if ((B.NW && F.SE) || (F.NW && B.SE))
			// x_plus = 4;
			// if ((B.NE && F.SW) || (F.NE && B.SW))
			// x_plus = -4;
			// }

			// TODO: handle height differences in movement_marker
			movement_path_sprite.setpos(new Vector2((float) P.x - camera.X()
					- x_plus, (float) P.y - camera.Y()));
			movement_path_sprite.draw(s_batch);
		}
		drawtile = bfspath.get(bfspath.size() - 1);
		F = F.opposite();
		move_arrowSet.fliphorz = (F.NE || F.E || F.SE);
		move_arrowSet.flipvert = (F.SW || F.SE);
		int u = drawtile.Y;
		int v = drawtile.X;
		P = initializePositionRect(u, v);
		P.y -= z_height * Base.tile_pixel_z;
		P.height += z_height * Base.tile_pixel_z;
		int x_plus = (F.NE || F.E || F.SE) ? (Base.quarter_tile_pixel_width() * 2) - 2
				: 0;
		move_arrowSet.X_index = (F.W || F.E) ? 0 : 1;
		move_arrowSet.setpos(new Vector2((float) P.x - camera.X() + x_plus,
				(float) P.y - camera.Y()));

		move_arrowSet.draw(s_batch);

	}

	/**
	 * Draws the wait cursor
	 */
	private void Draw_Wait_Cursor(Draw_Object s_batch) {
		int x = active_battler().real_X() - camera.X();
		int y = active_battler().real_Y() - (Base.tile_pixel_height / 4)
				- camera.Y();
		Waiting_sprites.setpos(new Vector2(x, y));
		Waiting_sprites.draw(s_batch);
	}

	/**
	 * Draws the battle windows
	 * */
	public void Draw_Windows(Draw_Object s_batch) {
		super.Draw_Windows(s_batch);
		if (windowset_action.active || windowset_action.isPanning())
			windowset_action.draw(s_batch);
		if (windowset_skill.active || windowset_skill.isPanning())
			windowset_skill.draw(s_batch);
		if (windowset_item.active || windowset_item.isPanning())
			windowset_item.draw(s_batch);
		if (windowset_status.active || windowset_status.isPanning())
			windowset_status.draw(s_batch);
		if (windowset_revive.active || windowset_revive.isPanning())
			windowset_revive.draw(s_batch);
		if (windowset_tile.active || windowset_tile.isPanning())
			windowset_tile.draw(s_batch);
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		if (PHASE() == 5)
			Draw_Movement_Arrow(s_batch);
		if (PHASE() == 6)
			Draw_Wait_Cursor(s_batch);
	}

	/**
	 * Returns true if he AI can see an opponent
	 * 
	 * @return
	 */
	public boolean opponent_in_sight() {
		for (Battler B : targets) {
			if (opponents(active_battler().is_enemy()).contains(B))
				return true;
		}
		return false;
	}

	/**
	 * returns the list of active opponent battlers
	 * 
	 * @param is_enemy
	 * @return
	 */
	public LinkedList<Battler> opponents(boolean is_enemy) {
		return is_enemy ? GameScene.Players : GameScene.Enemies;
	}

	/**
	 * Resets the players viewed perspective
	 */
	public void resetPlayerVision() {
		playervision.clear();
		for (Battler B : Players) {
			playervision.addAll(map_array.Range(B.Pos(), B.properties.getVSN(),
					true, true));
		}
		playervision = HexTable.trimmed_range(playervision, false);
	}

	/**
	 * Sets the move position for the chosen action. Will attempt to hit from
	 * behind if physical type. Otherwise will try to get farthest away position
	 * while still being able to deliver action.
	 */
	public void Process_Movement() {
		// checks the moveable range
		// this is mostly to make the move range viewable to the player
		check_area(active_battler(), 1, null);
		// Waits a few frames
		Base.wait = MathUtils.random(12, 24);
		// TODO choose selected tile
		if (opponent_in_sight()) {
			// The AI can see opponenets, but cannot reach them;
			// finds the closest opponent. If the opponent is 4x the
			// distance away than the battlers mov, it'll wander instead;
			int m = 255;
			Battler prey = closest_target(m);
			if (m > 4 * active_battler().properties.MOV())
				Selected_Tile = best_wander_pos();
			// If the AI is within 1.5 x range of the enemy, but has already
			// acted, she should flee
			else if (m > 3 * active_battler().properties.MOV() / 2)
				Selected_Tile = best_flee_pos();
			// otherwise chase the opponent!
			else
				Selected_Tile = best_chase_pos(prey.Pos());
		} else {
			// the selected tile is random - the enemy is wandering
			Selected_Tile = best_wander_pos();
		}
		bfspath = map_array.BFS(active_battler().Pos(), Selected_Tile);
		Move_Battler();
		active_battler().has_moved = true;
	}

	/**
	 * returns the closest target to the AB
	 * 
	 * @param m
	 * @return
	 */
	public Battler closest_target(int m) {
		int min = m;
		Battler closest = targets.get(0);
		int d;
		for (Battler B : targets) {
			d = HexTable.distance(active_battler().Pos(), B.Pos());
			if (d < min) {
				min = d;
				closest = B;
			}
		}
		return closest;
	}

	/**
	 * returns a random tile to move to
	 * 
	 * @return
	 */
	public HexTile best_wander_pos() {
		int p = (int) (Base.RANDOMIZER.nextDouble() * move_range.size());
		return move_range.get(p);
	}

	/**
	 * Process turn end
	 */
	public void Process_End() {
		active_battler().setDirection(face_direction());
		set_wait_variables();
		setActive_Battler(next_up());
	}

	/**
	 * Process Action
	 */
	public void Process_Action() {
		// checks the attackable range
		// Waits a few frames
		Base.wait = MathUtils.random(12, 24);
		// get selection of attack options
		LinkedList<BattleAction> options = attack_options(targets);
		// If there are no good options, skips action
		if (options.size() == 0) {
			active_battler().has_acted = true;
			return;
		}
		// else pick the best action
		BattleAction action = chosen_BattleAction(options);
		check_area(active_battler(), 2, null);
		// check_area(Active_Battler, 4,
		// Active_Battler().max_special_range());//add skill range
		if (action.kind == BattleAction.Kind.attack
				&& !attack_range.contains(action.target_tile)) {
			clear_tilerange_sprites();
			if (!active_battler().has_moved) {
				Selected_Tile = best_attack_pos(action);
				bfspath = map_array.BFS(active_battler().Pos(), Selected_Tile);
				Move_Battler();
				active_battler().has_moved = true;
			}
		}
		active_battler().current_action = action;
		set_meta_variables(action);
		return;
	}

	/**
	 * Returns the best position to move to to attack from
	 * 
	 * @param action
	 * @return
	 */
	public HexTile best_attack_pos(BattleAction action) {
		HexTile H = action.target_tile;
		for (int i = active_battler().range(); i > 0; i--) {
			H = map_array.D(H,
					HexTable.travel_adjacent(H, active_battler().Pos()));
		}
		return H;
	}

	// / <summary>
	// / Returns the best position to chase
	// / </summary>
	// / <param name="action"></param>
	// / <returns></returns>
	public HexTile best_chase_pos(HexTile tile) {
		HexTile H = active_battler().Pos();// H is the tile to chase
		while (!move_range.contains(H)) {
			H = pathfind_to(H, tile);
		}
		return H;
	}

	public BattleAction chosen_BattleAction(LinkedList<BattleAction> actions) {
		return chosen_BattleAction(actions, false);
	}

	/**
	 * Given a dictionary of valued actions, chooses and returns the correct
	 * action to take.
	 * 
	 * @param actions
	 * @param best
	 * @return
	 */
	public BattleAction chosen_BattleAction(LinkedList<BattleAction> actions,
			boolean best) {
		if (actions.size() == 0)
			return null;
		// int level = Active_Battler().Properties.LEVEL;
		LinkedList<BattleAction> Possible_Actions = new LinkedList<BattleAction>();
		BattleAction bestact = new BattleAction();
		int max = 0, min;
		for (BattleAction key : actions) {
			if (key.score > max) {
				max = key.score;
				bestact = key;
			}
		}
		if (best)
			return bestact;

		min = 0;
		// min = Math.Max((int)(((float)(20 + level) / 100) * max), level /
		// 2);
		for (BattleAction key : actions) {
			if (key.score >= min)
				Possible_Actions.add(key);
		}
		double d = Base.RANDOMIZER.nextDouble();
		int at = (int) (d * Possible_Actions.size());
		at = Math.max(at, Possible_Actions.size() - 1);
		return Possible_Actions.get(at);
	}

	/**
	 * Returns the best direction the battler should face
	 * 
	 * @return
	 */
	private HexDirectional face_direction() {
		HexDirectional d = active_battler().direction();
		// Battler closest;
		int min = active_battler().properties.getVSN();
		int n;
		for (Battler b : targets) {
			n = HexTable.distance(b.Pos(), active_battler().Pos());
			if (n < min) {
				min = n;
				d = HexTable.travel_adjacent(active_battler().Pos(), b.Pos());
			}
		}
		return (d.equals(HexDirectional.None())) ? active_battler().direction()
				: d;
	}

	/**
	 * determines the target to attack
	 * 
	 * @param targets
	 * @return
	 */
	private LinkedList<BattleAction> attack_options(LinkedList<Battler> targets) {
		// HexTile position;
		// int damage = 0, max_damage;
		LinkedList<BattleAction> actions = new LinkedList<BattleAction>();
		// int basic = 0;
		// Battler basic_target;
		// Skill special;
		// Dictionary<Skill, Integer> skills = new Dictionary<Skill,int>();
		active_battler().current_action.kind = BattleAction.Kind.attack;
		for (ActiveSkill S : active_battler().properties.skills.activeskills()) {
			// if the skill will do damage
			if (active_battler().properties.skill_can_use(S)) {
				// if the skill is a field skill, and can hit multiple
				// targets
				if (S.Field() > 0) {
					LinkedList<BattleAction> fieldskill = field_skill_value(S,
							targets);
					for (BattleAction act : fieldskill) {
						actions.add(act);
					}
					break;
				}
				// If it is a healing skill
				if (S.is_healing_skill()) {
					check_area(active_battler(), 3, S);
					// for ally of the AB
					for (Battler B : opponents(!active_battler().is_enemy())) {
						// If the healing skil will reach them
						if (heal_range.contains(B.Pos())) {
							BattleAction action = new BattleAction(
									BattleAction.Kind.special);
							action.id = S.id;
							action.target_tile = B.Pos();
							action.score = heal_score(action, B, S.sp_cost);
							actions.add(action);
						}
					}
					break;
				}
				// cycle through every target
				for (Battler B : targets) {
					// if the skill will actually reach
					if (map_array.Range(active_battler().Pos(), S.Range())
							.contains(B.Pos())) {
						BattleAction action = new BattleAction(
								BattleAction.Kind.special);
						action.id = S.id;
						action.target_tile = B.Pos();
						action.score = skill_score(action, B, S.sp_cost);
						actions.add(action);
					}
				}
			}
		}
		LinkedList<BattleAction> attacks = attack_scores(targets);
		for (BattleAction act : attacks) {
			actions.add(act);
		}
		return actions;
	}

	/**
	 * returns the scores of the ABs attack possibilities, and chooses the best
	 * one
	 * 
	 * @param targets
	 * @return
	 */
	public LinkedList<BattleAction> attack_scores(LinkedList<Battler> targets) {
		// Scores Attacks
		int r = active_battler().range();
		r += active_battler().has_moved ? 0 : active_battler().properties.MOV();
		// LinkedList<HexTile> range =
		// map_array.Passable_Range(Active_Battler().Pos, r, new
		// LinkedList<Battler>());
		LinkedList<HexTile> range = map_array.Range(active_battler().Pos(), r);
		LinkedList<BattleAction> actions = new LinkedList<BattleAction>();
		int basic = 0;
		for (Battler B : targets) {
			// If the target is an ally, the AI will not attack them
			if (!opponents(active_battler().is_enemy()).contains(B))
				continue;
			if (range.contains(B.Pos())) {
				basic = BattleMath.damage(active_battler(), B,
						active_battler().current_action);
				// HexDirectional dir =
				// HexTable.travel_adjacent(Active_Battler().Pos, B.Pos);
				basic = (HexTable.travel_adjacent(active_battler().Pos(),
						B.Pos()) == B.direction()) ? (int) (basic * 1.7f)
						: basic;
				BattleAction action = new BattleAction(BattleAction.Kind.attack);
				action.target_tile = B.Pos();
				action.score = basic;
				actions.add(action);
			}
		}
		return actions;
	}

	/**
	 * Field skills do different things depending on where they are cast.
	// This cycles through all possible positions a field skill could be
	// cast
	 * @param skill
	 * @param targets
	 * @return
	 */
	private LinkedList<BattleAction> field_skill_value(ActiveSkill skill,
			LinkedList<Battler> targets) {
		LinkedList<BattleAction> actions = new LinkedList<BattleAction>();
		LinkedList<HexTile> range = new LinkedList<HexTile>();
		LinkedList<Battler> potential_targets = new LinkedList<Battler>();
		BattleAction action = new BattleAction(BattleAction.Kind.special);
		action.id = skill.id;
		for (HexTile H : (map_array
				.Range(active_battler().Pos(), skill.Range()))) {
			range = map_array.Range(H, skill.Field());
			for (Battler battler : targets) {
				if (range.contains(battler.Pos()))
					potential_targets.add(battler);
			}
			if (potential_targets.size() == 0)
				break;
			BattleAction act = action.clone();
			act.target_tile = H;
			act.score = skill_score(act, potential_targets, skill.Field(),
					skill.sp_cost);
			actions.add(act);
		}
		return actions;
	}

	/**
	 * Overloaded. Returns a score for the effectiveness of using a particular
	 * skill with a large field that damages multiple opponents
	 * 
	 * @param act
	 *            Battle Action
	 * @param targets
	 *            enemies affected by the attack
	 * @param field
	 *            AOE of the attack
	 * @param skill_cost
	 *            cost of that particular skill
	 * @return
	 */
	private int skill_score(BattleAction act, LinkedList<Battler> targets,
			int field, int skill_cost) {
		int count = 0, value = 0;
		for (Battler B : targets) {
			count++;
			value += skill_score(act, B, skill_cost);
		}
		value *= (count / field);
		return value;
	}

	// / <summary>
	// / Overloaded. Returns a score for the effectiveness of using a
	// particular skill with a large field
	// / that damages multiple opponents
	// / </summary>
	// / <param name="act"></param>
	// / <param name="target"></param>
	// / <param name="skill_cost"></param>
	// / <returns></returns>
	private int skill_score(BattleAction act, Battler target, int skill_cost) {
		int basic = 0, value = 0;
		basic = (int) (BattleMath.damage(active_battler(), target,
				active_battler().current_action) * BattleMath.Accuracy(
				active_battler(), target));
		basic -= skill_cost / 5;
		value += basic > target.properties.HP ? (int) (basic * 1.2) : basic;
		return value;
	}

	/**
	 * Returns the score associated with healing attacks
	 * 
	 * @85% 1.0x
	 * @70% 2.0x
	 * @50% 3.0x
	 * 
	 * @param act
	 * @param target
	 * @param skill_cost
	 * @return
	 */
	private int heal_score(BattleAction act, Battler target, int skill_cost) {
		int basic;
		basic = (int) (BattleMath.damage(active_battler(), target,
				active_battler().current_action) * -1.0f);
		basic = (int) (basic * (3 - 3 * target.properties.HP_rate()));
		basic -= skill_cost / 5;
		return basic;
	}

	/**
	 * Returns the best tile to flee from
	 * 
	 * @return
	 */
	private HexTile best_flee_pos() {
		int max, d;
		HexTile chosen = null;
		if (move_range.size() > 0)
			return active_battler().Pos();
		LinkedList<HexTile> scores = new LinkedList<HexTile>();
		for (HexTile H : move_range) {
			max = 0;
			for (Battler B : targets) {
				d = HexTable.distance(H, B.Pos());
				if (d > max)
					max = d;
			}
			H.score = max;
		}
		max = 0;
		for (HexTile H : scores) {
			if (H.score > max) {
				chosen = H;
				max = H.score;
			}
		}
		return chosen;
	}

	/**
	 * returns the next best tile to move to
	 * 
	 * @param start
	 * @param goal
	 * @return
	 */
	private HexTile pathfind_to(HexTile start, HexTile goal) {
		// H is the next most-direct tile to the goal
		HexTile H = map_array.D(start, HexTable.travel_adjacent(start, goal));
		// If it's passable, go there
		if (map_array.passable(H, All_Battlers()))
			return H;
		HexTile L = map_array.D(start,
				HexTile.L(HexTable.travel_adjacent(start, goal)));
		HexTile R = map_array.D(start,
				HexTile.R(HexTable.travel_adjacent(start, goal)));
		boolean PL = map_array.passable(L, All_Battlers());
		boolean PR = map_array.passable(R, All_Battlers());
		if (!PL)
			return R;
		if (!PR)
			return L;
		int DL = HexTable.distance(L, goal);
		int DR = HexTable.distance(R, goal);
		if (DL >= DR)
			return R;
		else
			return L;
	}

	/**
	 * Returns a list of possible battler targets within a given range of a //
	 * tile
	 * */
	private LinkedList<Battler> get_targets(HexTile pos, int range) {
		LinkedList<HexTile> AOE = map_array.Range(pos, range);
		LinkedList<Battler> targets = new LinkedList<Battler>();
		for (Battler B : All_Battlers()) {
			for (HexTile H : AOE) {
				if (B.Pos() == H) {
					if (B != active_battler())
						targets.add(B);
				}
			}
		}
		return targets;
	}

	public void dispose() {
		super.dispose();
	}
}
