package untra.driver;

import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Audio {

	// public static List<SoundEffect> s_s3d;
	// public static List<Sound> S3D;

	// private static int currentchannel = 0;
	private static final int SECHANNELMAX = 24;
	private static final String ROOT = "audio/";
	private static Sound BGM;
	private static Sound BGS;
	private static Sound ME;
	private static Sound[] SE = new Sound[SECHANNELMAX];
	private static HashMap<String, Sound> soundTable;

	// SE filenames
	private static final String SE_CURSOR = "Cursor_SE.ogg";
	private static final String SE_SELECTION = "Selection_SE.ogg";
	private static final String SE_CANCEL = "Cancel_SE.ogg";
	private static final String SE_BUZZER = "Buzzer_SE.ogg";
	private static final String SE_EQUIP = "Equip_SE.ogg";
	private static final String SE_PURCHASE = "Buy_SE.ogg";
	private static final String SE_SELL = "Sell_SE.ogg";
	// private static int delta_volume, frames_remaining;
	public static Sound SE_Cursor;
	public static Sound SE_Selection;
	public static Sound SE_Cancel;
	public static Sound SE_Buzzer;
	public static Sound SE_Equip;
	public static Sound SE_Purchase;
	public static Sound SE_Sell;

	// static ContentManager player;

	public Audio() {

	}

	public static void init() {

		try {
			System.out.println("Audio Initializing...");
			for (int i = 0; i < SECHANNELMAX; i++) {
				SE[i] = Gdx.audio.newSound(Gdx.files.internal(ROOT
						+ "SE/" + "NULL.ogg"));
				// SE[i] = Gdx.audio.newSound(Gdx.files.internal(ROOTDIRECTORY +
				// "SE/" + "NULL.ogg");
			}
			// initializes hashmap
			soundTable = new HashMap<String, Sound>(SECHANNELMAX);
			SE_Cursor = Gdx.audio.newSound(Gdx.files.internal(ROOT
					+ "SE/" + SE_CURSOR));
			SE_Selection = Gdx.audio.newSound(Gdx.files.internal(ROOT
					+ "SE/" + SE_SELECTION));
			SE_Cancel = Gdx.audio.newSound(Gdx.files.internal(ROOT
					+ "SE/" + SE_CANCEL));
			SE_Buzzer = Gdx.audio.newSound(Gdx.files.internal(ROOT
					+ "SE/" + SE_BUZZER));
			SE_Equip = Gdx.audio.newSound(Gdx.files.internal(ROOT
					+ "SE/" + SE_EQUIP));
			SE_Purchase = Gdx.audio.newSound(Gdx.files.internal(ROOT
					+ "SE/" + SE_PURCHASE));
			SE_Sell = Gdx.audio.newSound(Gdx.files.internal(ROOT
					+ "SE/" + SE_SELL));
			System.out.println("Audio Initialization Great Success!");
		} catch (Exception e) {
			System.out.println("Audio Initialization Failed!");
			e.printStackTrace();
		}
	}

	/**
	 * Returns a new sound effect from a specific filename
	 * 
	 * @param name
	 * @return
	 */
	public static Sound new_SE(String name) {
		return Gdx.audio.newSound(Gdx.files.internal(ROOT + "SE/"
				+ name));
	}

	public static void addCollectionToSoundTable(LinkedList<String> list) {
		for (String string : list) {
			soundTable.put(
					string,
					Gdx.audio.newSound(Gdx.files.internal(ROOT + "SE/"
							+ string)));
		}
	}

	public static void subtractCollectionFromSoundTable(LinkedList<String> list) {
		for (String string : list) {
			soundTable.get(string).dispose();
			soundTable.remove(string);
		}
	}

	// / <summary>
	// /
	// /Pan :: (float) between -1.0 for left side and 1.0 for right side.
	// Default 0.0 .
	// /Pitch :: (float) between -1.0 for -1 octave and 1.0 for +1 octave.
	// Default 0.0 .
	// /Volume :: (float) between 0.0 and 1.0
	// / </summary>
	public static void bgm_play(String name, float volume, float pitch,
			float pan) {
		if (BGM != null) {
			BGM.stop();
			BGM.dispose();
		}
		BGM = Gdx.audio.newSound(Gdx.files.internal(ROOT + "BGM/"
				+ name));
		BGM.loop(pitch, volume, pan);
	}

	public static void bgm_play(String name) {
		bgm_play(name, 1.0f, 0.0f, 0.0f);
	}

	public static void bgs_play(String name, float volume, float pitch,
			float pan) {
		if (BGS != null) {
			BGS.stop();
			BGS.dispose();
		}
		BGS = Gdx.audio.newSound(Gdx.files.internal(ROOT + "BGS/"
				+ name));
		BGS.loop(pitch, volume, pan);
	}

	public static void bgs_play(String name) {
		bgs_play(name, 0.8f, 0.0f, 0.0f);
	}

	public static void me_play(String name, float volume, float pitch, float pan) {
		if (ME != null)
			ME.stop();
		if (ME != null)
			ME.dispose();
		ME = Gdx.audio.newSound(Gdx.files
				.internal(ROOT + "ME/" + name));
		BGM.stop();
		ME.play(pitch, volume, pan);
	}

	/**
	 * Plays a sound effect with the following name, volume, pitch and pan
	 * 
	 * @param name
	 * @param volume
	 * @param pitch
	 * @param pan
	 */
	public static void se_play(String name, float volume, float pitch, float pan) {
		// int i = allocateSEChannel();
		// SE[i] = Gdx.audio.newSound(Gdx.files.internal(ROOTDIRECTORY + "SE/"
		// + name));
		//
		// SE[i].play(pitch, volume, pan);
		Sound sound = soundTable.get(name);
		if (sound == null) {
			soundTable.put(
					name,
					Gdx.audio.newSound(Gdx.files.internal(ROOT + "SE/"
							+ name)));
			se_play(name, volume, pitch, pan);
		} else {

			// sound.stop();
			sound.play(pitch, volume, pan);
			System.out.println(sound);
		}
	}

	/**
	 * Plays a sound effects with the following name.
	 * 
	 * @param name
	 *            sound effect name
	 */
	public static void se_play(String name) {
		se_play(name, 0.8f, 0.0f, 0.0f);
	}

	public static void BGM_stop() {
		if (BGM == null)
			return;
		BGM.stop();
	}

	public static void BGS_stop() {
		if (BGS == null)
			return;
		BGS.stop();
	}

	/*
	 * public static void BGM_pause() { if (BGM == null) return; BGM.pause(); }
	 */

	public static void BGM_resume() {
		if (BGM == null)
			return;
		BGM.play();
	}
}
