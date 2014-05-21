package untra.scene.game.window;

import untra.graphics.Draw_Object;
import untra.graphics.Sprite;
import untra.scene.game.GameScene;
import untra.scene.game.battle.Battler;
import untra.scene.game.map.HexObject;
import untra.scene.game.map.HexTile;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import untra.driver.Base;

public class WindowTile extends WindowBattle {
	private static final int WWIDTH = 256, WHEIGHT = 96;
	private HexTile tile = null;
	private Battler battler = null;
	private Sprite sprite = null;
	private String name = "";
	private String coord = "";

	public WindowTile() {
		super(new Rectangle(0, Base.window_height() - WHEIGHT, WWIDTH, WHEIGHT));
		// TODO Auto-generated constructor stub
	}

	public void refresh(HexTile tile) {
		if (is_tile(tile))
			return;
		this.tile = tile;
		this.battler = null;
		sprite = null;
		name = "";
		coord = "X:";
		coord += tile.X + " Y:";
		coord += tile.Y + " Z:";
		coord += tile.z_height() + " ";
		if (tile.map_object_index != 0) {
			// this assumes that the hexobject exists in the scene (and
			// therefore is not null) and that the sprite has been initialized
			// for the hexobject (and therefore is not null
			HexObject object = GameScene.hexObjectFrom(tile.map_object_index);
			if (object != null) {
				name = object.name;
				sprite = object.sprite;
			}
		} else
			this.battler = GameScene.occupied_by(tile);
		if (this.battler != null) {
			name = battler.properties.Name;
			sprite = battler.sprite;
		}
	}

	public boolean is_tile(HexTile tile) {
		return tile.equals(this.tile);
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		Vector2 pos = ocoordinates();
		ocoordinates().y += WHEIGHT - (2 * BUFFER) - Base.tile_pixel_height;
		GameScene.Tile_sprites.Y_index = tile.Tile_Value;
		GameScene.Tile_sprites.setpos(pos);
		GameScene.Tile_sprites.draw(s_batch);
		pos = ocoordinates();
		// pos.y += WHEIGHT - sprite.height();
		// ocoordinates().x += Base.tile_pixel_width / 2;
		// ocoordinates().x -= sprite.width() / 2;
		// sprite.setpos(pos);
		// sprite.draw(s_batch);
		pos = ocoordinates();
		pos.y -= 8;
		pos.x += Base.tile_pixel_height;
		s_batch.draw_smaller_text(name, pos.x, pos.y);
		pos.y += 32;
		s_batch.draw_smaller_text(tile.toTileValueString(), pos.x, pos.y);
		pos.y += 32;
		s_batch.draw_smaller_text(coord, pos.x, pos.y);
	}
}
