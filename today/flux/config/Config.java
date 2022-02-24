package today.flux.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import sun.misc.Unsafe;
import today.flux.Flux;
import today.flux.addon.FluxAPI;
import today.flux.config.preset.PresetManager;
import today.flux.event.TickEvent;
import today.flux.gui.altMgr.Alt;
import today.flux.gui.altMgr.GuiAltMgr;
import today.flux.gui.altMgr.kingAlts.KingAlts;
import today.flux.gui.hud.window.HudWindow;
import today.flux.gui.hud.window.HudWindowManager;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Render.NameProtect;
import today.flux.module.value.*;
import today.flux.utility.TimeHelper;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Objects;

public class Config {
	public static final String ROOT_DIR = "Flux";
	public static boolean hasLoadedConfig = false;
	public Config() {
		new Thread(new Runnable() {
			@SoterObfuscator.Obfuscation(flags = "+native,+tiger-black")
			@Override
			public void run() {
				try {
					
					createNewDir(ROOT_DIR);
					createNewDir(ROOT_DIR + "/presets");
					Flux.INSTANCE.moduleManager = new ModuleManager();
					Flux.INSTANCE.api = new FluxAPI();
					loadConfig();
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("Error loading from file " + ROOT_DIR);
				}

			}
		}).start();

		EventManager.register(this);
	}

	TimeHelper saveTimer = new TimeHelper();

	@EventTarget
	public void onTicks(TickEvent e) {
		if (hasLoadedConfig && saveTimer.isDelayComplete(3000)) {
			new Thread() {
				@Override
				public void run() {
					loadPresets();
					saveConfig();
				}
			}.start();
			saveTimer.reset();
		}
	}

	private void createNewDir(String name) {
		final File file = new File(name);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void loadPresets() {
		File presetsFolder = new File(ROOT_DIR + "/presets");

		PresetManager.presets.clear();
		for (File file : Objects.requireNonNull(presetsFolder.listFiles())) {
			if (!file.isDirectory() && file.getName().endsWith(".prs")) {
				PresetManager.presets.add(file.getName().substring(0, file.getName().length() - 4));
			}
		}
	}

	public void saveConfig() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(ROOT_DIR + "/config.json"));
			JSONObject total = new JSONObject(true);

			// Save Friends
			JSONArray friends = new JSONArray();
			friends.addAll(Flux.INSTANCE.getFriendManager().getFriends());
			total.put("Friends", friends);

			// Save NameProtect String
			total.put("NameProtect", NameProtect.name);

			// Save KingAlts API
			total.put("KingAltsAPI", KingAlts.API_KEY);

			// Save Alts
			JSONArray alts = new JSONArray();
			for (Alt alt : GuiAltMgr.alts) {
				JSONObject altObj = new JSONObject(true);
				altObj.put("Email", alt.getEmail());
				if (!alt.isCracked()) {
					altObj.put("Password", alt.getPassword());
					altObj.put("Name", alt.getName());
				}
				altObj.put("Star", alt.isStarred());
				alts.add(altObj);
			}
			total.put("Alts", alts);

			// Save HUD Windows Position
			JSONObject windows = new JSONObject();
			for (HudWindow w : HudWindowManager.windows) {
				JSONObject position = new JSONObject(true);
				position.put("X", Math.floor(w.x));
				position.put("Y", Math.floor(w.y));
				if (w.resizeable) {
					position.put("Width", Math.floor(w.width));
					position.put("Height", Math.floor(w.height));
				}
				windows.put(w.windowID, position);
			}
			total.put("Windows", windows);

			// Save Modules Configs
			total.put("Modules", saveModules(true));

			writer.write(JSONObject.toJSONString(total, true));
			writer.flush();
			writer.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public JSONObject saveModules(boolean saveBinds) {
		JSONObject modules = new JSONObject();
		for (Module module : ModuleManager.getModList()) {
			JSONObject moduleConfig = new JSONObject(true);
			moduleConfig.put("isEnabled", module.isEnabled());
			if (saveBinds)
				moduleConfig.put("Bind", module.getBind());
			moduleConfig.put("isHide", module.isHide());
			JSONObject values = new JSONObject(true);
			for (Value value : ValueManager.getValueByModName(module.getName())) {
				JSONObject valueConfig = new JSONObject(true);
				if (value instanceof BooleanValue) {
					valueConfig.put("Type", "Boolean");
					valueConfig.put("Value", ((BooleanValue) value).getValue());
				} else if (value instanceof FloatValue) {
					valueConfig.put("Type", "Float");
					valueConfig.put("Value", ((FloatValue) value).getValue());
				} else if (value instanceof ModeValue) {
					valueConfig.put("Type", "Mode");
					valueConfig.put("Value", ((ModeValue) value).getValue());
				} else if (value instanceof ColorValue) {
					valueConfig.put("Type", "Color");
					valueConfig.put("Value", ((ColorValue) value).getValue().getRGB());
				}

				values.put(value.getKey(), valueConfig);
			}
			moduleConfig.put("Value", values);
			modules.put(module.getName(), moduleConfig);
		}
		return modules;
	}

	public void loadModules(JSONObject modules) {
		for (Module module : ModuleManager.getModList()) {
			if (modules.containsKey(module.getName())) {
				JSONObject moduleConfig = modules.getJSONObject(module.getName());
				try {
					module.isEnabled = moduleConfig.getBoolean("isEnabled");
					module.update();
				} catch (Throwable ignored) {}

				if (moduleConfig.containsKey("Bind")) {
					module.setBind(moduleConfig.getInteger("Bind"));
				}
				module.setHide(moduleConfig.getBoolean("isHide"));

				if (moduleConfig.containsKey("Value")) {
					JSONObject moduleValues = moduleConfig.getJSONObject("Value");
					for (Value value : ValueManager.getValueByModName(module.getName())) {
						if (moduleValues.containsKey(value.getKey())) {
							JSONObject currentValue = moduleValues.getJSONObject(value.getKey());
							if (currentValue.getString("Type").equals("Float") && value instanceof FloatValue) {
								value.setValue(currentValue.getFloat("Value"));
							} else if (currentValue.getString("Type").equals("Boolean") && value instanceof BooleanValue) {
								value.setValue(currentValue.getBoolean("Value"));
							} else if (currentValue.getString("Type").equals("Mode") && value instanceof ModeValue) {
								value.setValue(currentValue.getString("Value"));
							} else if (currentValue.getString("Type").equals("Color") && value instanceof ColorValue) {
								value.setValue(new Color(currentValue.getInteger("Value")));
							} else {
								System.out.println("Wrong Value Type: " + module.getName() + " " + value.getKey());
							}
						} else {
							System.out.println("Skipping load Value: " + value.getKey());
						}
					}
				}
			} else {
				System.out.println("Skipping loading module: " + module.getName());
			}
		}
	}

	public void loadConfig() {
		try {
			StringBuilder configString = new StringBuilder();

			BufferedReader reader = new BufferedReader(new FileReader(ROOT_DIR + "/config.json"));
			String line;

			while ((line = reader.readLine()) != null) {
				configString.append(line);
			}

			JSONObject config = JSON.parseObject(configString.toString());

			// Read Friends
			if (config.containsKey("Friends")) {
				JSONArray friends = config.getJSONArray("Friends");
				Flux.INSTANCE.getFriendManager().getFriends().addAll(friends.toJavaList(String.class));
			}

			// Read NameProtect String
			if (config.containsKey("NameProtect")) {
				NameProtect.name = config.getString("NameProtect");
			}

			// Read KingAlts API
			if (config.containsKey("KingAltsAPI")) {
				KingAlts.API_KEY = config.getString("KingAltsAPI");
			}

			// Read Alts
			if (config.containsKey("Alts")) {
				JSONArray alts = config.getJSONArray("Alts");
				for (JSONObject alt : alts.toJavaList(JSONObject.class)) {
					GuiAltMgr.alts.add(new Alt(alt.getString("Email"), alt.getString("Password"), alt.getString("Name"), alt.getBoolean("Star")));
				}
				GuiAltMgr.sortAlts();
			}

			// Read HUD Windows Position
			if (config.containsKey("Windows")) {
				JSONObject windows = config.getJSONObject("Windows");
				for (HudWindow w : HudWindowManager.windows) {
					if (windows.containsKey(w.windowID)) {
						JSONObject windowPosition = windows.getJSONObject(w.windowID);
						w.x = windowPosition.getInteger("X");
						w.y = windowPosition.getInteger("Y");
						if (w.resizeable) {
							w.width = windowPosition.getInteger("Width");
							w.height = windowPosition.getInteger("Height");
						}
					} else {
						System.out.println("Skipping loading window: " + w.windowID);
					}
				}
			}

			// Save Modules Configs
			if (config.containsKey("Modules")) {
				JSONObject modules = config.getJSONObject("Modules");
				loadModules(modules);
			}
			hasLoadedConfig = true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
