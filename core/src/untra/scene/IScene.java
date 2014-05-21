package untra.scene;

import untra.graphics.Draw_Object;

public interface IScene{
    void update();
    void draw(Draw_Object s_batch);
    void dispose();
}
