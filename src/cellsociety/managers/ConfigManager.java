package cellsociety.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
		while(input.hasNext()) {
			String[] string = input.nextLine().split("=");
			List<String> values = new ArrayList<>(Arrays.asList(string[1].split(",")));
			properties.put(string[0], values);
		}
		input.close();
	}
	
	public static int getInt(String s) {
		return getInt(s, 0);
	}
	
	public static int getInt(String s, int n) {
		Object value = config.properties.get(s).get(0);
		return value != null ? Integer.parseInt(value.toString()) : n;
	}
	
	public static String getString(String s) {
		return getString(s, "");
	}
	
	public static String getString(String s, String n) {
		Object value = config.properties.get(s).get(0);
		return value != null ? value.toString() : n;
	}
	
	public static List<String> getStringList(String s) {
		return config.properties.get(s);
	}	
	
	@SuppressWarnings("unchecked")
	public static <T> T getObject(Class<?> T, String s) {
		T ret = null;
		try {
			Class<?> clazz = Class.forName(config.properties.get(s).get(0));
			ret = (T) clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;
	}
}
