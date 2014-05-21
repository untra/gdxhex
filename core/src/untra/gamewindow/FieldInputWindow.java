package untra.gamewindow;

import untra.graphics.Draw_Object;

import java.util.LinkedList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.PauseableThread;

import untra.driver.Base;
import untra.driver.Input;

public class FieldInputWindow extends SelectionWindow {

	public static Thread thread;
	public boolean pauseThread = false;
	private String input;
	private String field;
	private String expecting;
	private static final String BOOLEAN = "BOOLEAN";
	private static final String ENUM = "ENUM";
	private static final String INT = "INT";
	private static final String FLOAT = "FLOAT";
	private static final String STRING = "STRING";

	public FieldInputWindow(Runnable runnable) {
		super(new Rectangle(0, 0, Base.window_width(),
				Base.window_height() - 32));
		cursor_rect_appension = new Rectangle(0, 64, 0, 0);
		move_to_bottom();
		Initialize();
		if (runnable != null) {
			thread = new PauseableThread(runnable);
			thread.start();
		}
	}

	private void Initialize() {
		input = "";
		field = "";
		column_max = 2;
	}

	public void update() {
		super.update();
		if (!active)
			return;
		// if selection mode
		if (expecting == ENUM || expecting == BOOLEAN) {
			if (Input.isKeyTrigger(Keys.ENTER)) {
				pauseThread = false;
				// thread.notify();
			} else if (Input.triggerA()) {
				field = commands[index];
				// thread.notify();
			}
		} else
			field += Input.textInput();
		if (Input.isKeyTrigger(Keys.BACKSPACE))
			if (field.length() > 0)
				field = field.substring(0, field.length() - 1);
		if (Input.isKeyTrigger(Keys.ENTER))
			pauseThread = false;
		// thread.notify();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void EnumSelection(String input, Enum E) {
		refresh();
		commands = toStringArray(E);
		item_max = commands.length;
		expecting = ENUM;
		field = E.toString();
		this.input = input;
		// sets the default value
		index = E.ordinal();
		// Klass klass = new Klass();
		// klass.race = (Race) E.getClass().getEnumConstants()[index];
		lock();
	}

	public void BooleanSelection(String input, boolean _default) {
		refresh();
		this.input = input;
		commands = new String[2];
		commands[0] = "TRUE";
		commands[1] = "FALSE";
		item_max = 2;
		expecting = BOOLEAN;
		// sets the default value
		index = _default ? 0 : 1;
		lock();
	}

	public void StringSelection(String input, String _default) {
		refresh();
		this.input = input;
		field = _default;
		expecting = STRING;
		lock();
	}

	public void IntSelection(String input, int _default) {
		refresh();
		this.input = input;
		field = Integer.toString(_default);
		expecting = STRING;
		lock();
	}

	public void FloatSelection(String input, float _default) {
		refresh();
		this.input = input;
		field = Float.toString(_default);
		expecting = STRING;
		lock();
	}

	private void refresh() {
		// System.out.println("REFRESH!");
		input = "";
		field = "";
		expecting = "";
		commands = new String[0];
		item_max = 0;
		pauseThread = true;
	}

	public int getInt() {
		if (expecting == ENUM)
			return index;
		return Integer.valueOf(field);
	}

	public float getFloat() {
		return Float.valueOf(field);
	}

	public boolean getBoolean() {
		return Boolean.valueOf(field);
	}

	public String getString() {
		return field;
	}

	/**
	 * This is the strangest fuckin syntax I've ever seen what is this shit?
	 * Goddamn. It returns a string array of enum values from a generic enum. Is
	 * that a extends keyword in the method signature like da fuq?
	 */
	@SuppressWarnings("unchecked")
	private <E extends Enum<E>> String[] toStringArray(E e) {
		LinkedList<String> list = new LinkedList<String>();
		for (Enum<E> enumval : e.getClass().getEnumConstants()) {
			list.add(enumval.toString());
		}
		String[] stringArray = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			stringArray[i] = list.get(i).toString();
		}
		return stringArray;
	}

	public synchronized void lock() {
		try {
			while (pauseThread) {
				wait();
			}
			notifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Draw_Object s_batch) {
		super.draw(s_batch);
		super.drawCursorRect(s_batch);
		Vector2 p = ocoordinates();
		textColor = NORMAL_COLOR;
		String string;
		int j;
		s_batch.draw_regular_text(input, p.x, p.y, textColor);
		p = ocoordinates();
		p.y += 32;
		s_batch.draw_regular_text(field + "|", p.x, p.y, textColor);
		for (int i = getPage_item_max() * page; i < commands.length
				&& i < getPage_item_max() - 4; i++) {
			j = i % getPage_item_max();
			string = commands[i];
			p = ocoordinates();
			p.x += (j / row_max()) * (width()) / column_max + 4;
			p.y += 4 + ((j % row_max()) * (32));
			p.y += 64;
			s_batch.draw_regular_text(string, p.x, p.y, textColor);
		}
	}
	//
	// public class FieldInput {
	// String type;
	// Integer _int;
	// Float _float;
	// Boolean _bool;
	// String _string;
	// Enum _enum;
	//
	// @SuppressWarnings("rawtypes")
	// public FieldInput(String type, Object _default) {
	// this.type = type;
	// switch (type) {
	// case INT:
	// _int = (Integer) _default;
	// break;
	// case STRING:
	// _string = (String) _default;
	// break;
	// case FLOAT:
	// _float = (Float) _default;
	// break;
	// case BOOLEAN:
	// _bool = (Boolean) _default;
	// break;
	// case ENUM:
	// _enum = (Enum) _default;
	// break;
	// default:
	// return;
	// }
	// }
	// }
	//
	// public class FieldInputStack {
	// int index = 0;
	// private LinkedList<FieldInput> inputList = new LinkedList<FieldInput>();
	//
	// public void add(String type, Object _default) {
	// inputList.add(new FieldInput(type, _default));
	// }
	//
	// public void update() {
	// if (Input.isKeyTrigger(Keys.ENTER)) {
	//
	// }
	// }
	//
	// }
}
