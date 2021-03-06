package cellsociety.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import cellsociety.grid.AbstractGrid;

public class ConfigManager {
	private static final String CONFIG_FILE = "config.properties";
	private static final ConfigManager config = new ConfigManager();
	private Map<String, List<String>> properties;

	private ConfigManager() {
		properties = new HashMap<String, List<String>>();
		load();
	}

	private void load() {
		Scanner input = new Scanner(ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
		while (input.hasNext()) {
			String nextLine = input.nextLine();
			if (!nextLine.contains("#") && !nextLine.equals("")) {
				String[] string = nextLine.split("=");
				List<String> values = new ArrayList<>(Arrays.asList(string[1].split(",")));
				properties.put(string[0], values);
			}
		}
		input.close();
	}

	public static String scope(String a, String b) {
		return String.format("%s.%s", a, b);
	}

	public static int getInt(String s) {
		return getInt(s, 0);
	}

	public static int getInt(String s, int n) {
		List<String> value = config.properties.get(s);
		return value != null ? Integer.parseInt(value.get(0).toString()) : n;
	}

	public static String getString(String s) {
		return getString(s, "");
	}

	public static String getString(String s, String n) {
		List<String> value = config.properties.get(s);
		return value != null ? value.get(0).toString() : n;
	}

	public static List<String> getStringList(String s) {
		return config.properties.get(s);
	}

	public static List<Double> getDoubleList(String s) {
		List<String> StringList = getStringList(s);
		List<Double> output = new ArrayList<Double>();
		for (String cur : StringList){
			output.add(Double.parseDouble(cur));
		}
		return output;
	}

	public static <T> T getObject(String s) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(config.properties.get(s).get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getObject(clazz, config.properties.get(s).get(0));
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObject(Class<?> T, String s) {
		T ret = null;
		try {
			ret = (T) T.getClassLoader().loadClass(s).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static AbstractGrid getAbstractGrid(String s, Object...args) {
		AbstractGrid ret = null;
		try {
			ret = (AbstractGrid) AbstractGrid.class.getClassLoader().loadClass(s).getConstructor(String.class).newInstance(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
