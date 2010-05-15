package cn.edu.sjtu.ltlab.jcrawler.util;

import java.util.Properties;

public class ConfigUtil {

	private static Properties prop = new Properties();

	static {
		try {
			prop.load(ConfigUtil.class.getClassLoader().getResourceAsStream(
					"config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getStringProperty(String key) {
		return prop.getProperty(key);
	}

	public static boolean getBooleanProperty(String key) {
		return Boolean.parseBoolean(prop.getProperty(key));
	}
	
	public static int getIntProperty(String key) {
		return Integer.parseInt(prop.getProperty(key));
	}

	public static long getLongProperty(String key) {
		return Long.parseLong(prop.getProperty(key));
	}
}
