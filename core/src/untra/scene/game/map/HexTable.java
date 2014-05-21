package untra.scene.game.map;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;

import untra.scene.game.battle.Battler;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

import untra.database.Database;
import untra.database.Tile_Object;
import untra.driver.Base;
import untra.driver.IXml;

public class HexTable implements Iterable<HexTile>, IXml<HexTable> {

	// private int iteratorposition = 0;

	private HexTile[][] table;
	private int width;
	private int height;
	// private int basic_land_type;
	private int max_z_height;
	private String name;
	// public LinkedList<HexObject> ObjectSet;
	// private LinkedList<Tile_Object> TO;
	private LinkedList<Battler> Enemy_Battlers;
	public LinkedList<HexTile> playerStartHexTiles;
	public HexObject[] OS = new HexObject[1024];

	public int Width() {
		return width;
	}

	public int Height() {
		return height;
	}

	public int Maximum_Z_Height() {
		return max_z_height;
	}

	public HexTable(int height, int width, int value, LinkedList<Tile_Object> TO) {
		if ((height > 255 || width > 255) || (height < 1 || width < 1))
			throw new IndexOutOfBoundsException(
					String.format(
							"Map height or Width is an invalid value! height = {0}, Width = {1}",
							height, width));
		this.width = width;
		this.height = height;
		table = new HexTile[height][width];
		// this.TO = TO;
		playerStartHexTiles = new LinkedList<HexTile>();
		Enemy_Battlers = new LinkedList<Battler>();
		// basic_land_type = value;
		// Horz_Push suggest Row_Major data
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				table[i][j] = new HexTile(i, j, value, 0);
				// all lqiuid tiles have default height 0
				if (!table[i][j].is_liquid())
					// otherwise default height 1
					table[i][j].set_z_height(1);
			}
		}
		OS[0] = new HexObject(Database.tile_objects[0]);
		for (Tile_Object T : TO) {
			OS[T.id] = new HexObject(T);
		}
		heightcheck();
	}

	public void heightcheck() {
		max_z_height = 0;
		for (HexTile H : this) {
			if (H.z_height() > max_z_height)
				max_z_height = H.z_height();
		}
	}

	public int Pixel_Width_Max() {
		return (width * Base.tile_pixel_width) + (Base.tile_pixel_width / 2);
	}

	public int Pixel_Height_Max() {
		return (height * 48) + 16;
	}

	/**
	 * Returns the Distance between two hextiles
	 * 
	 * @param I
	 * @param J
	 * @return
	 */
	public static int distance(HexTile I, HexTile J) {
		Vector3 a = new Vector3(I.X, I.Y, 0);
		Vector3 b = new Vector3(J.X, J.Y, 0);
		int ax = (int) a.x;
		int bx = (int) b.x;
		ax -= (I.Y / 2);
		bx -= (J.Y / 2);
		int az = (int) (0 - ax - a.y);
		int bz = (int) (0 - bx - b.y);
		a.set(ax, a.y, az);
		b.set(bx, b.y, bz);
		Vector3 D = new Vector3(Math.abs(a.x - b.x), Math.abs(a.y - b.y),
				Math.abs(a.z - b.z));
		return (int) Math.max(Math.max(D.x, D.y), D.z);
	}

	/**
	 * Returns the Hextile at the specified coordinates. If the Hextile doesn't
	 * exist, returns null
	 * 
	 * @param y
	 * @param x
	 * @return
	 */
	public HexTile Tile_At(int y, int x) {
		if (0 <= y && 0 <= x && y < height && x < width)
			return table[y][x];
		else
			return null;
	}

	public LinkedList<HexTile> pathfind_neighbors(HexTile H) {
		/*
		 * HexDirectional opposite = HexTile.O(dir_from); HexDirectional left =
		 * HexTile.L(opposite); HexDirectional right = HexTile.R(opposite);
		 * HexTile A = D(H, left); HexTile B = D(H, opposite); HexTile C = D(H,
		 * right); LinkedList<HexTile> neighbors = new LinkedList<HexTile>(); if
		 * (A != null) neighbors.add(A); if (B != null) neighbors.add(B); if (C
		 * != null) neighbors.add(C); return neighbors;
		 */
		HexTile tile;
		LinkedList<HexTile> neighbors = new LinkedList<HexTile>();
		tile = NE(H);
		if (tile != null)
			neighbors.add(tile);
		tile = E(H);
		if (tile != null)
			neighbors.add(tile);
		tile = SE(H);
		if (tile != null)
			neighbors.add(tile);
		tile = SW(H);
		if (tile != null)
			neighbors.add(tile);
		tile = W(H);
		if (tile != null)
			neighbors.add(tile);
		tile = NW(H);
		if (tile != null)
			neighbors.add(tile);
		return neighbors;
	}

	// / <summary>
	// / EAST
	// / </summary>
	public HexTile E(HexTile obj) {
		int x = obj.X;
		int y = obj.Y;
		// if (x >= Width) x = Width - 1;
		// if (y >= Height) y = height - 1;
		return Tile_At(y, x + 1);
	}

	// / <summary>
	// / NORTHEAST
	// / </summary>
	public HexTile NE(HexTile obj) {

		int x = obj.X;
		int y = obj.Y;
		if (y % 2 == 1)
			x++;
		y--;
		// if (x >= Width) x = Width - 1;
		// if (y >= Height) y = height - 1;
		return Tile_At(y, x);

	}

	// / <summary>
	// / NORTHWEST
	// / </summary>
	public HexTile NW(HexTile obj) {
		int x = obj.X;
		int y = obj.Y;
		if (y % 2 == 0)
			x--;
		y--;
		// if (x >= Width) x = Width - 1;
		// if (y >= Height) y = height - 1;
		return Tile_At(y, x);
	}

	// / <summary>
	// / WEST
	// / </summary>
	public HexTile W(HexTile obj) {
		int x = obj.X;
		int y = obj.Y;
		// if (x >= Width) x = Width - 1;
		// if (y >= Height) y = height - 1;
		return Tile_At(y, x - 1);
	}

	// / <summary>
	// / SOUTHWEST
	// / </summary>
	public HexTile SW(HexTile obj) {

		int x = obj.X;
		int y = obj.Y;
		if (y % 2 == 0)
			x--;
		y++;
		// if (x >= Width) x = Width - 1;
		// if (y >= Height) y = height - 1;
		return Tile_At(y, x);
	}

	// / <summary>
	// / SOUTHEAST
	// / </summary>
	public HexTile SE(HexTile obj) {
		int x = obj.X;
		int y = obj.Y;

		if (y % 2 == 1)
			x++;
		y++;
		// if (x >= Width) x = Width - 1;
		// if (y >= Height) y = height - 1;

		return Tile_At(y, x);
	}

	/**
	 * Static method that returns the coordinates of the tile to the i direction
	 * of the specified point.
	 * 
	 * @param obj
	 * @param i
	 * @return
	 */
	public HexTile D(HexTile obj, HexDirectional i) {
		int x = obj.X;
		int y = obj.Y;
		if ((i.equals(HexDirectional.NW()))) {
			if (y % 2 == 0)
				x--;
			y--;
			// if (x >= Width) x = Width - 1;
			// if (y >= Height) y = height - 1;
		} else if ((i.equals(HexDirectional.NE()))) {
			if (y % 2 == 1)
				x++;
			y--;
			// if (x >= Width) x = Width - 1;
			// if (y >= Height) y = height - 1;
		} else if ((i.equals(HexDirectional.W()))) {
			x--;
			// if (x >= Width) x = Width - 1;
			// if (y >= Height) y = height - 1;
		} else if ((i.equals(HexDirectional.E()))) {
			x++;
			// if (x >= Width) x = Width - 1;
			// if (y >= Height) y = height - 1;
		} else if ((i.equals(HexDirectional.SW()))) {
			if (y % 2 == 0)
				x--;
			y++;
			// if (x >= Width) x = Width - 1;
			// if (y >= Height) y = height - 1;
		} else if ((i.equals(HexDirectional.SE()))) {
			if (y % 2 == 1)
				x++;
			y++;
			// if (x >= Width) x = Width - 1;
			// if (y >= Height) y = height - 1;
		}

		return this.Tile_At(y, x);
	}

	/**
	 * returns the direction one tile is to another. (to travel from H1 to H2, a
	 * step must be taken in the x direction). assume H1 is start, H2 is end,
	 * and they may not necesarrily be adjascent.
	 * 
	 */
	public static HexDirectional travel_adjacent(HexTile H1, HexTile H2) {
		if (H1.equals(H2))
			return new HexDirectional();
		if (H1.Y == H2.Y) // if horizontal to each other
		{
			if (H1.X < H2.X)
				return HexDirectional.E();
			else
				return HexDirectional.W();
		} else if (H1.Y > H2.Y) // going north
		{
			if (H1.Y % 2 == 0) {
				if (H2.X < H1.X)
					return HexDirectional.NW();
				else
					return HexDirectional.NE();
			} else {
				if (H2.X > H1.X)
					return HexDirectional.NE();
				else
					return HexDirectional.NW();
			}
		} else // going south
		{
			if (H1.Y % 2 == 0) {
				if (H2.X < H1.X)
					return HexDirectional.SW();
				else
					return HexDirectional.SE();
			} else {
				if (H2.X > H1.X)
					return HexDirectional.SE();
				else
					return HexDirectional.SW();
			}
		}
	}

	/*
	 * public HexTile Pathfinding(HexTile H1, HexTile H2, HexDirectional d = new
	 * HexDirectional()) { HexDirectional L = d; HexTile n; if (H1.Equals(H2))
	 * return H1; if (H1.Y == H2.Y && (d & HexDirectional.E) != HexDirectional.E
	 * && (d & HexDirectional.W) != HexDirectional.W) //if horizontal to each
	 * other { if (H1.X < H2.X) { n = this.E(H1); d = d | HexDirectional.E; }
	 * else { n = this.W(H1); d = d | HexDirectional.W; }
	 * 
	 * } else if (H1.Y > H2.Y) // going north {((i & HexDirectional.SE) ==
	 * HexDirectional.SE) if ((d & HexDirectional.NE) == HexDirectional.NE) n =
	 * this.NW(H1); else if ((d & HexDirectional.NW) == HexDirectional.NW) n =
	 * this.NE(H1); else if (H1.Y % 2 == 0) { if (H2.X < H1.X) { n =
	 * this.NW(H1); d = d | HexDirectional.NW; } else { n = this.NE(H1); d = d |
	 * HexDirectional.NE; } } else { if ((d & HexDirectional.SE) ==
	 * HexDirectional.SE) n = this.SW(H1); else if ((d & HexDirectional.SW) ==
	 * HexDirectional.SW) n = this.SE(H1); else if (H2.X > H1.X) { n =
	 * this.NE(H1); d = d | HexDirectional.NE; } else { n = this.NW(H1); d = d |
	 * HexDirectional.NW; } } } else //going south { if (H1.Y % 2 == 0) { if
	 * (H2.X < H1.X) n = this.SW(H1); else n = this.SE(H1); } else { if (H2.X >
	 * H1.X) n = this.SE(H1); else n = this.SW(H1); } } if (n.passabilities !=
	 * new HexDirectional()) return n; else if (L != d) return Pathfinding(H1,
	 * H2, d); else return null; }
	 */

	// public LinkedList<HexTile> Pathfinding(HexTile start, HexTile goal,
	// LinkedList<HexTile> path, int D_LIMIT, HexDirectional entry,
	// int D_COUNT) {
	// D_COUNT++;
	// LinkedList<HexTile> W = new LinkedList<HexTile>();
	// LinkedList<HexTile> E = new LinkedList<HexTile>();
	// LinkedList<HexTile> NW = new LinkedList<HexTile>();
	// LinkedList<HexTile> NE = new LinkedList<HexTile>();
	// LinkedList<HexTile> SE = new LinkedList<HexTile>();
	// LinkedList<HexTile> SW = new LinkedList<HexTile>();
	// LinkedList<LinkedList<HexTile>> TOTAL = new
	// LinkedList<LinkedList<HexTile>>();
	// if (entry != new HexDirectional()) {
	// if (start == null)
	// return path;
	// if (start.passabilities == new HexDirectional())
	// return path;
	// if (D_COUNT > D_LIMIT)
	// return path;
	// }
	// path.add(start);
	// if (start == goal)
	// return path;
	// // For each additional search, check to see if the point of entry
	// // prohibits a search : the corresponding direction
	// if (HexDirectional.E() != entry && HexDirectional.NE() != entry
	// && HexDirectional.SE() != entry) {
	// W = Pathfinding(D(start, HexDirectional.W()), goal, path, D_LIMIT,
	// HexDirectional.W(), D_COUNT);
	// if (!TOTAL.contains(W))
	// TOTAL.add(W);
	// }
	// if (HexDirectional.W() != entry && HexDirectional.NW() != entry
	// && HexDirectional.SW() != entry) {
	// E = Pathfinding(D(start, HexDirectional.E()), goal, path, D_LIMIT,
	// HexDirectional.E(), D_COUNT);
	// if (!TOTAL.contains(E))
	// TOTAL.add(E);
	// }
	// if (HexDirectional.SW() != entry && HexDirectional.E() != entry
	// && HexDirectional.SE() != entry) {
	// NW = Pathfinding(D(start, HexDirectional.NW()), goal, path,
	// D_LIMIT, HexDirectional.NW(), D_COUNT);
	// if (!TOTAL.contains(W))
	// TOTAL.add(W);
	// }
	// if (HexDirectional.SW() != entry && HexDirectional.W() != entry
	// && HexDirectional.SE() != entry) {
	// NE = Pathfinding(D(start, HexDirectional.NE()), goal, path,
	// D_LIMIT, HexDirectional.NE(), D_COUNT);
	// if (!TOTAL.contains(SW))
	// TOTAL.add(SW);
	// }
	// if (HexDirectional.NE() != entry && HexDirectional.E() != entry
	// && HexDirectional.NW() != entry) {
	// SW = Pathfinding(D(start, HexDirectional.SW()), goal, path,
	// D_LIMIT, HexDirectional.SW(), D_COUNT);
	// if (!TOTAL.contains(NE))
	// TOTAL.add(NE);
	// }
	// if (HexDirectional.NE() != entry && HexDirectional.W() != entry
	// && HexDirectional.NE() != entry) {
	// SE = Pathfinding(D(start, HexDirectional.SE()), goal, path,
	// D_LIMIT, HexDirectional.SE(), D_COUNT);
	// if (!TOTAL.contains(E))
	// TOTAL.add(E);
	// }
	// return shortest_list(TOTAL);
	// }

	/**
	 * Returns a shortest path BFS LinkedList including the first and last
	 * element.
	 * 
	 * @param start
	 * @param finish
	 * @return
	 */
	public LinkedList<HexTile> BFS(HexTile start, HexTile finish) {
		// System.out.println("BFS called.");
		LinkedList<HexTile> list = new LinkedList<HexTile>();
		ArrayBlockingQueue<HexTile> queue = new ArrayBlockingQueue<HexTile>(128);
		queue.add(start);
		HexTile linker;
		// every hextile surround this tile is added to the queue
		while (!queue.isEmpty()) {
			linker = queue.poll();
			for (HexTile H : this.Range(linker, 1)) {
				// if the tile is flagged, skip it
				if (H.flag)
					continue;
				else {
					// mark the tile,
					H.flag = true;
				}
				if (H.passabilities == false)
					continue;
				// chain it to the current tile
				H.chain = linker;
				// shortest path found
				if (H.samePosAs(finish)) {
					HexTile sniffer = H;
					while (sniffer.chain != null && sniffer != start) {
						// System.out.println(sniffer.X + ", " + sniffer.Y);
						list.add(0, sniffer);
						sniffer = sniffer.chain;
					}
					list.add(0, start);
					// list.add(finish);
					// clears map flags
					// clear_map_flags();
					// returns the list
					break;
				}
				// add it to the queue
				queue.add(H);
			}
		}
		clear_map_flags();
		// System.out.print("well, shit.");
		// throw new EmptyStackException();
		return list;
	}

	/**
	 * resets all map flags in the table
	 */
	public void clear_map_flags() {
		for (HexTile H : this) {
			H.flag = false;
			H.chain = null;
		}
	}

	/*
	 * public LinkedList<HexTile> hexpathfinding(HexTile start, HexTile goal) {
	 * LinkedList<HexTile> closed_set = new LinkedList<HexTile>();
	 * 
	 * }
	 */

	// private LinkedList<HexTile> shortest_list(
	// LinkedList<LinkedList<HexTile>> listoflists) {
	// int min = Integer.MAX_VALUE;
	// int d;
	// LinkedList<HexTile> best = new LinkedList<HexTile>();
	// for (LinkedList<HexTile> L : listoflists) {
	// d = L.size();
	// if (d < min) {
	// min = d;
	// best = L;
	// }
	// }
	// return best;
	// }

	public HexTile Tile_At_From_Real_Coordinates(int x, int y) {
		y /= (int) (Base.tile_pixel_height * .75);
		if (y % 2 == 1)
			x -= 28;
		x /= Base.tile_pixel_width;
		return Tile_At(y, x);
	}

	public void Update() {
		for (HexTile HT : this) {
			HT.Update();
		}
	}

	// / <summary>
	// / compares two hexdirections. returns 1 for equal to, -1 for opposite,
	// and 0 for neither
	// / 1 = N && N
	// / -1 = NW && SE
	// / 0 = W && NE
	// / </summary>
	// / <param name="A"></param>
	// / <param name="B"></param>
	// / <returns></returns>
	public static int CompareTo(HexDirectional A, HexDirectional B) {
		if (A.equals(B))
			return 1;
		if (A.isOpposite(B))
			return -1;
		else
			return 0;
	}

	/**
	 * returns true whether a specific tile is passable
	 * 
	 * @param obj
	 *            Hextile in question
	 * @param targets
	 *            targets that may occupy that tile
	 * @return
	 */
	public boolean passable(HexTile obj, LinkedList<Battler> targets) {
		for (Battler T : targets)
			if (T.Pos() == obj)
				return false;
		if (obj.getPassability() && passable_tile_values(obj.Tile_Value))
			return true;
		else
			return false;
	}

	public LinkedList<HexTile> Passable_Range(HexTile obj, int range,
			LinkedList<Battler> LinkedList) {
		// if (range < 0)
		// throw new Exception("range value negative. try again");
		LinkedList<HexTile> collection = new LinkedList<HexTile>();
		range--;
		collection.add(obj);
		collection.addAll(short_passable_range(this.E(obj), range,
				HexDirectional.E(), LinkedList));
		collection.addAll(short_passable_range(this.W(obj), range,
				HexDirectional.W(), LinkedList));
		collection.addAll(short_passable_range(this.NE(obj), range,
				HexDirectional.NE(), LinkedList));
		collection.addAll(short_passable_range(this.SE(obj), range,
				HexDirectional.SE(), LinkedList));
		collection.addAll(short_passable_range(this.SW(obj), range,
				HexDirectional.SW(), LinkedList));
		collection.addAll(short_passable_range(this.NW(obj), range,
				HexDirectional.NW(), LinkedList));
		return collection;
	}

	/**
	 * Returns true or false depending if the tile value is passable
	 * 
	 * @param v
	 * @return
	 */
	private boolean passable_tile_values(int v) {
		switch (v) {
		case 2:
			return false;
		default:
			return true;
		}
	}

	private LinkedList<HexTile> short_passable_range(HexTile obj, int range,
			HexDirectional d, LinkedList<Battler> targets) {
		LinkedList<HexTile> collection = new LinkedList<HexTile>();
		if (obj != null && passable(obj, targets))
			collection.add(obj);
		else
			return collection;
		if (range <= 0)
			return collection;
		range--;
		collection.addAll(short_passable_range(this.D(obj, d), range, d,
				targets));
		collection.addAll(short_passable_range(this.D(obj, d.left()), range,
				d.left(), targets));
		collection.addAll(short_passable_range(this.D(obj, d.right()), range,
				d.right(), targets));

		return collection;
	}

	private LinkedList<HexTile> Short_Range(HexTile obj) {
		LinkedList<HexTile> list = new LinkedList<HexTile>();
		try {
			list.add(this.NW(obj));

			list.add(this.NE(obj));

			list.add(this.W(obj));

			list.add(this.E(obj));

			list.add(this.SW(obj));

			list.add(this.SE(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public LinkedList<HexTile> Range(HexTile obj, int max) {
		return Range(obj, max, false, false);
	}

	/**
	 * Returns a list of all HexObjects within the specified range of HexObject
	 * obj.
	 * 
	 * @param obj
	 *            Specified HexObject Obj
	 * @param max
	 *            Maximum Range. Default is 1
	 * @return
	 */
	public LinkedList<HexTile> Range(HexTile obj, int max,
			boolean allow_impassable_tiles, boolean prevent_oversized_search) {
		LinkedList<HexTile> collection = new LinkedList<HexTile>();
		LinkedList<HexTile> new_additions = new LinkedList<HexTile>();
		collection.add(obj);
		for (int t = 0; t < max; t++) {
			for (HexTile H : collection) {
				if (prevent_oversized_search && H.map_object_properties.X_PU)
					continue;
				new_additions.addAll(this.Short_Range(H));
			}
			collection.addAll(new_additions);
			collection = trimmed_range(collection, !allow_impassable_tiles);
			new_additions.clear();
		}
		return collection;
	}

	/**
	 * Returns a list of all HexObjects within the vision of a battler
	 * 
	 * @param obj
	 *            Specified HexObject Obj
	 * @param max
	 *            Maximum Range. Default is 1
	 * @return
	 */
	public LinkedList<HexTile> VisionRange(HexTile obj, int max) {
		return Range(obj, max, true, false);
	}

	public static LinkedList<HexTile> trimmed_range(
			LinkedList<HexTile> untrimmed) {
		return trimmed_range(untrimmed, false);
	}

	/**
	 * Removes duplicates from a list of hextiles using a dictionary. Running
	 * this process on a list of hextiles with duplicates can significantly
	 * reduce runtime later
	 * 
	 * @param untrimmed
	 * @return
	 */
	public static LinkedList<HexTile> trimmed_range(
			LinkedList<HexTile> untrimmed, boolean remove_impassible_tiles) {
		Map<HexTile, Integer> uniqueStore = new HashMap<HexTile, Integer>();
		LinkedList<HexTile> Range = new LinkedList<HexTile>();
		for (HexTile H : untrimmed) {
			if (H == null)
				continue;
			if (!uniqueStore.containsKey(H)) {
				uniqueStore.put(H, 0);
				if (remove_impassible_tiles && !H.getPassability())
					continue;
				Range.add(H);
			}
		}
		return Range;
	}

	private void short_link(HexTile A, HexTile B, int V, int size, int z_height) {
		if (A.Y == B.Y && A.X == B.X)
			return;
		if ((A.X >= this.width) || (B.X >= this.width) || (A.Y >= this.height)
				|| (B.Y >= this.height))
			return;
		this.table[A.Y][A.X].Tile_Value = V;
		table[A.Y][A.X].set_z_height(z_height);
		this.table[B.Y][B.X].Tile_Value = V;
		table[B.Y][B.X].set_z_height(z_height);
		HexTile H;
		if (A.Y < B.Y) {
			if (A.X > B.X) {
				H = this.SW(A);
			} else {
				H = this.SE(A);
			}
		} else if (A.Y > B.Y) {
			if (A.X > B.X) {
				H = this.NW(A);
			} else {
				H = this.NE(A);
			}
		} else if (A.X > B.X) {
			H = this.W(A);
		} else {
			H = this.E(A);
		}
		try {
			H.Tile_Value = V;
			H.set_z_height(z_height);
			if (size > 0) {
				if (this.NW(H) != null) {
					this.NW(H).Tile_Value = V;
					this.NW(H).set_z_height(z_height);
				}
				if (this.NE(H) != null) {
					this.NE(H).Tile_Value = V;
					this.NE(H).set_z_height(z_height);
				}
				if (this.W(H) != null) {
					this.W(H).Tile_Value = V;
					this.W(H).set_z_height(z_height);
				}
			}
			if (size > 1) {
				if (this.E(H) != null) {
					this.E(H).Tile_Value = V;
					this.E(H).set_z_height(z_height);
				}
				if (this.SW(H) != null) {
					this.SW(H).Tile_Value = V;
					this.SW(H).set_z_height(z_height);
				}
				if (this.SE(H) != null) {
					this.SE(H).Tile_Value = V;
					this.SE(H).set_z_height(z_height);
				}
			}
			short_link(H, B, V, size, 0);
		} catch (NullPointerException e) {
			System.out.println("Short_Link " + e.getMessage());
		}

	}

	// / <summary>
	// / Creates a path from point A to point B using a recursive function
	// / </summary>
	// / <param name="A">Starting Hextile</param>
	// / <param name="B">Ending Hextile</param>
	// / <param name="V">Tile value set</param>
	// / <param name="size">Path size. default is 0. Maximum is 2.</param>
	/**
	 * Creates a path from point A to point B using a recursive function
	 * 
	 * @param A
	 *            start
	 * @param B
	 *            finish
	 * @param V
	 *            path value
	 * @param size
	 *            size of the path. this value ranges from [0,2]
	 * @param z_height
	 *            height of the path
	 */
	public void Recursive_Linked_Path(HexTile A, HexTile B, int V, int size,
			int z_height) {
		if (size > 2)
			return;
		table[A.Y][A.X].Tile_Value = V;
		table[A.Y][A.X].set_z_height(z_height);
		table[B.Y][B.X].Tile_Value = V;
		table[A.Y][A.X].set_z_height(z_height);
		// Finds the midpoint
		int dx = (byte) Math.abs(A.X - B.X);
		int dy = (byte) Math.abs(A.Y - B.Y);
		int r = (int) Math.sqrt((dx + dy));
		dx /= 2;
		dy /= 2;
		dx += (byte) Math.min(A.X, B.X);
		dy += (byte) Math.min(A.Y, B.Y);
		LinkedList<HexTile> list = Range(this.table[dy][dx], r);
		HexTile M = list.get(Base.RANDOMIZER.nextInt(list.size()));
		this.table[M.Y][M.X].Tile_Value = V;
		this.table[M.Y][M.X].set_z_height(z_height);
		// if the two points are close enough, connect them.
		if (((int) Math.abs((A.Y - dy)) <= 2)
				&& ((int) Math.abs((A.X - dx)) <= 2))
			short_link(A, M, V, size, z_height);
		else
			Recursive_Linked_Path(A, M, V, size, z_height);
		if (((int) Math.abs((B.Y - dy)) <= 2)
				&& ((int) Math.abs((B.X - dx)) <= 2))
			short_link(B, M, V, size, z_height);
		else
			Recursive_Linked_Path(B, M, V, size, z_height);
	}

	/**
	 * Searches all hextiles in the table, and hextiles with tile value i have
	 * their border values reset.
	 * 
	 * @param i
	 *            tile value
	 */
	public void reapply_tile_borders(int i) {
		for (HexTile H : this) {
			if (H.Tile_Value != i)
				continue;
			else
				H.border_tiles = new HexDirectional();
		}
		for (HexTile H : this) {
			if (H.Tile_Value != i)
				continue;
			// NW

			if (this.NW(H) == null)
				H.border_tiles.NW = true;
			else if (this.NW(H).Tile_Value != H.Tile_Value)
				H.border_tiles.NW = true;
			else if (this.NW(H).z_height() < H.z_height())
				H.border_tiles.NW = true;

			// NE

			if (this.NE(H) == null)
				H.border_tiles.NE = true;
			else if (this.NE(H).Tile_Value != H.Tile_Value)
				H.border_tiles.NE = true;
			else if (this.NE(H).z_height() < H.z_height())
				H.border_tiles.NE = true;

			// W

			if (this.W(H) == null)
				H.border_tiles.W = true;
			else if (this.W(H).Tile_Value != H.Tile_Value)
				H.border_tiles.W = true;
			else if (this.W(H).z_height() < H.z_height())
				H.border_tiles.W = true;

			// E

			if (this.E(H) == null)
				H.border_tiles.E = true;
			else if (this.E(H).Tile_Value != H.Tile_Value)
				H.border_tiles.E = true;
			else if (this.E(H).z_height() < H.z_height())
				H.border_tiles.E = true;

			// SW

			if (this.SW(H) == null)
				H.border_tiles.SW = true;
			else if (this.SW(H).Tile_Value != H.Tile_Value)
				H.border_tiles.SW = true;
			else if (this.SW(H).z_height() < H.z_height())
				H.border_tiles.SW = true;

			// SE

			if (this.SE(H) == null)
				H.border_tiles.SE = true;
			else if (this.SE(H).Tile_Value != H.Tile_Value)
				H.border_tiles.SE = true;
			else if (this.SE(H).z_height() < H.z_height())
				H.border_tiles.SE = true;

		}
	}

	// / <summary>
	// / Draws registered map objects to the map
	// / THIS METHOD IS REALLY FUCKING BAD. YOU SHOULDN'T USE IT.
	// / </summary>
	// / <param name="obj_set"></param>
	public void Apply_Map_Objects(LinkedList<Tile_Object> obj_set) {
		int prev_count;
		int rand_num;
		int minimum = 0;
		for (Tile_Object obj : obj_set) {
			minimum += obj.frequency;
		}
		// begins loop
		LinkedList<HexTile> surroundings;
		for (HexTile H : this) {
			prev_count = 0;
			if (H.Tile_Value != 2 && H.Tile_Value != 1) {
				rand_num = (Base.RANDOMIZER.nextInt(100 * obj_set.size()));
				if (rand_num > minimum)
					continue;
				for (int J = 0; J < obj_set.size(); J++) {
					if ((rand_num <= obj_set.get(J).frequency + prev_count)
							&& (rand_num > prev_count)) {
						H.map_object_index = obj_set.get(J).id;
						// H.map_object_properties = H.map_object_properties |
						// obj_set[J].Properties;
						if (Base.RANDOMIZER.nextInt(2) == 0)
							H.map_object_properties.Flip = true;
						if (obj_set.get(J).is_oversized_object())
							H.map_object_properties.X_PU = true;
						break;
					} else
						prev_count += obj_set.get(J).frequency;
				}
			}
		}
		// removes an object if it is surrounded by a simmilar object : a
		// surrounding tile
		for (HexTile H : this) {
			// If oversized
			if (this.OS[H.map_object_index].getOversized()) {
				// gets the surrounding tiles
				surroundings = this.Range(H, 0);
				surroundings.remove(H);
				// every surrounding tile should have no oversized objects
				for (HexTile R : surroundings) {
					// If oversized
					if (this.OS[R.map_object_index].getOversized()) {
						R.map_object_index = 0;
						break;
					}
				}
			}
		}
		// Clear passability if the tile contains an object
		for (HexTile H : this) {
			// If the tile contains a hexobject
			if (H.map_object_index != 0) {
				H.setPassability(false);
			} else {
				H.setPassability(true);
				H.map_object_properties = new HexProperty();
			}
		}
		// Clear for Multitile
		for (HexTile H : this) {
			if (this.OS[H.map_object_index].is_multitile()) {
				multitile_level(H, this.OS[H.map_object_index].passables);
			}
		}
	}

	/**
	 * returns a list of battlers from a list of other battlers that come from a
	 * specific origin and a distance away
	 * 
	 * @param battlers
	 * @param origin
	 * @param distance
	 * @return
	 */
	public static LinkedList<Battler> battlers_distance_from(
			LinkedList<Battler> battlers, HexTile origin, int distance) {
		LinkedList<Battler> list = new LinkedList<Battler>();
		for (Battler B : battlers) {
			if (HexTable.distance(B.Pos(), origin) <= distance) {
				list.add(B);
			}
		}
		return list;
	}

	public void multitile_level(HexTile H, PassabilityArray passability) {
		HexTile R;
		boolean b = false;
		int x = H.X;
		int y = H.Y;
		int w = passability.array.size;
		int ww = w / 2;
		int h = passability.get_width_size();
		int hh = h * -1 + 1;
		for (int i = y + hh; i < y; i++) {
			for (int j = x - ww; i < x + w; j++) {
				R = Tile_At(i, j);
				b = passability.passibility_at(i * w + j);
				multitile_clear(R, b);
			}
		}
	}

	/**
	 * Clears variables for a tile that is not located on the designated
	 * Hextile, but nonetheless may occupy it.
	 * 
	 * @param H
	 * @param passable
	 */
	private void multitile_clear(HexTile H, boolean passable) {
		H.setPassability(passable);
		H.map_object_index = 0;
	}

	/**
	 * Returns true if the object to the direction D of the specified tile is
	 * passable
	 * 
	 * @param H
	 * @param D
	 * @return
	 */
	public boolean direction_passable(HexTile H, HexDirectional D) {
		if (H == null || D == new HexDirectional())
			return false;
		if (H.passabilities)
			return true;
		else
			return false;
	}

	/**
	 * Applies Map Barriers to the map. Make sure to run this after Objects //
	 * have been populated
	 * 
	 * @param min
	 * @param max
	 * @param frequency
	 */
	public void Apply_Map_Barriers(int min, int max, int frequency) {
		int temp;
		for (HexTile H : this) {
			if (H.Tile_Value == 4 && H.map_object_index == 0) {
				if (Base.RANDOMIZER.nextInt(100) <= 12) {
					H.barrier_index = Base.RANDOMIZER.nextInt(max - min);
					H.barrier_index += min;
					if (Base.RANDOMIZER.nextInt(2) == 0)
						H.barrier_properties.Flip = true;
					temp = Base.RANDOMIZER.nextInt(3);
					if (temp == 0) // (East)
					{
						// if ((H.passabilities & HexDirectional.E) ==
						// HexDirectional.E && (H.border_tiles &
						// HexDirectional.E) != HexDirectional.E)
						// H.passabilities = H.passabilities ^ HexDirectional.E;
						H.barrier_properties.X_PU = true;
						// if (Direction_Passable(this.E(H), HexDirectional.W))
						// this.E(H).passabilities = this.E(H).passabilities ^
						// HexDirectional.W;
					} else if (temp == 1) // (SouthEast)
					{
						// if ((H.passabilities & HexDirectional.SE) ==
						// HexDirectional.SE && (H.border_tiles &
						// HexDirectional.SE) != HexDirectional.SE)
						// H.passabilities = H.passabilities ^
						// HexDirectional.SE;
						H.barrier_properties.Y_PU = true;
						// if (Direction_Passable(this.SE(H),
						// HexDirectional.NW))
						// this.SE(H).passabilities = this.SE(H).passabilities ^
						// HexDirectional.NW;
					} else // (SouthWest)
					{
						// if ((H.passabilities & HexDirectional.SW) ==
						// HexDirectional.SW && (H.border_tiles &
						// HexDirectional.SW) != HexDirectional.SW)
						// H.passabilities = H.passabilities ^
						// HexDirectional.SW;
						H.barrier_properties.Z_PU = true;
						// if (Direction_Passable(this.SW(H),
						// HexDirectional.NE))
						// this.SW(H).passabilities = this.SW(H).passabilities ^
						// HexDirectional.NE;
					}
				}
			}
		}
	}

	/**
	 * @deprecated
	 * @SuppressWarnings("unused")
	 */
	@SuppressWarnings("unused")
	public void TEMP_Fire_Everywhere() {
		int i;
		for (HexTile H : this) {
			if (H.Tile_Value == 2)
				continue;
			i = Base.RANDOMIZER.nextInt(100);
			/*
			 * if (i < 8) H.hexobject = HexObject.Large_Fire(H.Y, H.X);
			 */
		}
	}

	/**
	 * Returns the Player spawning Locations, and ensures there are no //
	 * obstacles hindering the players.
	 * 
	 * @param H
	 *            initial starting point of the player 0
	 * @return
	 */
	public LinkedList<HexTile> Set_Player_Spawn_Locations(HexTile H) {
		LinkedList<HexTile> Locs = new LinkedList<HexTile>();
		try {
			Locs.addAll(this.Range(H, 2));
		} catch (Exception e) {
		}
		for (HexTile T : Locs) {
			// this.table[T.Y][T.X].hexobject = null;
			this.table[T.Y][T.X].barrier_index = 0;
			this.table[T.Y][T.X].map_object_index = 0;
			this.table[T.Y][T.X].barrier_properties = new HexProperty();
			this.table[T.Y][T.X].passabilities = H.passabilities;
		}
		return Locs;
	}

	@Override
	public Iterator<HexTile> iterator() {
		return new HexTableIterator(table, width);
	}

	/**
	 * Private hextableiterator is used to iterate across all hextiles in a map.
	 * 
	 * @author samuel
	 * 
	 */
	private class HexTableIterator implements Iterator<HexTile> {
		private int count = 0;
		private int width;
		private HexTile[][] table;

		public HexTableIterator(HexTile[][] table, int width) {
			count = 0;
			this.table = table;
			this.width = width;
		}

		public boolean hasNext() {
			if (count == 768) {
				// System.out.println("");
			}
			if (count < table.length * width) {

				return true;
			}
			return false;
		}

		@Override
		public HexTile next() {
			try {
				if (count == table.length * width)
					throw new NoSuchElementException();
				count++;
				return table[(count - 1) / width][(count - 1) % width];
			} catch (Exception e) {
				System.out.print(count);
				e.printStackTrace();

			}
			return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public void xmlWrite(XmlWriter xml) throws IOException {
		xml.element("HexTable");
		xml.element("Name").text(name).pop();
		xml.element("Width").text(width).pop();
		xml.element("Height").text(height).pop();
		xml.element("HexTiles");
		for (int i = 0; i < width * height; i++) {
			table[i / width][i % width].xmlWrite(xml);
		}
		xml.pop();
		xml.element("Tile_Objects");
		for (HexObject T : this.OS) {
			if (T == null)
				continue;
			xml.element("T");
			xml.text(T.id);
			xml.pop();
		}
		xml.pop();
		xml.element("EnemyBattlers");
		for (Battler B : this.Enemy_Battlers) {
			B.xmlWrite(xml);
		}
		xml.pop();
		xml.element("PlayerStartHexTiles");
		for (HexTile H : this.playerStartHexTiles) {
			H.xmlWrite(xml);
		}
		xml.pop();
		xml.pop();
	}

	@Override
	public HexTable xmlRead(Element element) {

		int w = element.getInt("Width");
		int h = element.getInt("Height");
		LinkedList<Tile_Object> TO = new LinkedList<Tile_Object>();
		for (Element e : element.getChildByName("Tile_Objects")
				.getChildrenByNameRecursively("T")) {
			TO.add(Database.tile_objects[Integer.valueOf(e.getText())]);
		}
		HexTable hextable = new HexTable(h, w, 0, TO);
		// hextable.TO = TO;
		hextable.width = w;
		hextable.height = h;
		hextable.name = element.getName();
		HexTile tile = new HexTile(0, 0);

		int x, y;
		for (Element e : element.getChildByName("HexTiles")
				.getChildrenByNameRecursively("HexTile")) {
			tile = tile.xmlRead(e);
			x = tile.X;
			y = tile.Y;
			hextable.table[y][x] = tile;
		}
		hextable.playerStartHexTiles = new LinkedList<HexTile>();
		for (Element e : element.getChildByName("PlayerStartHexTiles")
				.getChildrenByName("HexTile")) {
			tile = tile.xmlRead(e);
			x = tile.X;
			y = tile.Y;
			hextable.playerStartHexTiles.add(hextable.Tile_At(y, x));
		}
		Battler battler = new Battler();
		hextable.Enemy_Battlers = new LinkedList<Battler>();
		for (Element e : element.getChildByName("EnemyBattlers")
				.getChildrenByName("Battler")) {
			battler = battler.xmlRead(e);
			hextable.Enemy_Battlers.add(battler);
		}
		return hextable;
	}

	public void setName(String s) {
		this.name = s;
	}

	public String getName() {
		return this.name;
	}

	public String toString(String s) {
		String string = name + " " + width + "X" + height;
		return string;
	}
}
