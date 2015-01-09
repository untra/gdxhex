package untra.scene.game.window;

import untra.graphics.Draw_Object;
import untra.graphics.GameColor;
import untra.graphics.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.database.Consumable;
import untra.database.Item;
import untra.driver.Base;

public class WindowItem extends WindowBattle {

	public Consumable item;
	private ArrayList<Consumable> itemset;
	private Map<Consumable, Sprite> itembitmap;

	public WindowItem() {
		super(new Rectangle(0, 0, 0, 96));
		Initialize();
	}

	@SuppressWarnings("serial")
	public void Initialize() {
		coordinates.height = (Math.min(item_max + 1, 6) * 32) + BUFFER;
		coordinates.y = (Base.window_height() - coordinates.height - 128) / 2;
		coordinates.x = -16;
		coordinates.width = 176;
		itemset = new ArrayList<Consumable>();
		itembitmap = new HashMap<Consumable, Sprite>() {
		};
		column_max = 1;
		this.index = 0;
		// this.opacity = 160;
		refresh();
	}

	public void update() {
		if (this.active && itemset.size() != 0)
			item = itemset.get(index);
		super.update();
	}

	@SuppressWarnings("unused")
	public void refresh() {
		Item previous = new Item();
		// itemset = Party.Items;
		for (Item i : Base.party.Items) {
			if (i instanceof Consumable) {
				itemset.add((Consumable) i);
			}
		}
		itembitmap.clear();
		for (Consumable i : itemset) {
			if (itembitmap.containsKey(i))
				continue;
			itembitmap.put(i, new Sprite("Items/" + i.name));
			previous = i;
		}
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		GameColor text_color;
		Vector2 p;
		Sprite tempSprite;
		for (Item i : itemset) {
			p = ocoordinates();
			text_color = NORMAL_COLOR;
			p.x += 4 + (index % 2) * (width() - ocoordinates().x / column_max);
			p.y += (index / 2) * 32 + 4;
			tempSprite = itembitmap.get(i);
			tempSprite.setpos(p);
			tempSprite.draw(s_batch);
			p.x += 36;
			draw_text(s_batch, i.name, p, text_color);
		}
	}
}
