package untra.driver;

import untra.graphics.Draw_Object;

import com.badlogic.gdx.utils.Disposable;

public interface IGamecycle extends Disposable {

	public void dispose();

	public void update();

	public void draw(Draw_Object s_batch);
}
