package cn.edu.sjtu.ltlab.jcrawler.config;

import cn.edu.sjtu.ltlab.jcrawler.util.ConfigUtil;

public class BDBConfig {

	public static boolean env_allow_create = ConfigUtil.getBooleanProperty("env.allow_create");
	public static boolean env_transactinal = ConfigUtil.getBooleanProperty("evn.transactional");
	public static boolean env_locking = ConfigUtil.getBooleanProperty("env.locking");
	public static String env_home = ConfigUtil.getStringProperty("env.home");
	public static boolean db_allow_create = ConfigUtil.getBooleanProperty("db.allow_create");
	public static boolean db_transactional = ConfigUtil.getBooleanProperty("db.transactional");
	public static boolean db_deferred_write = ConfigUtil.getBooleanProperty("db.deferred_write");
	
	/*static {
		env_allow_create = ConfigUtil.getBooleanProperty("env.allow_create");
		env_transactinal = ConfigUtil.getBooleanProperty("evn.transactional");
		env_locking = ConfigUtil.getBooleanProperty("env.locking");
		env_home = ConfigUtil.getStringProperty("env.home");
		db_allow_create = ConfigUtil.getBooleanProperty("db.allow_create");
		db_transactional = ConfigUtil.getBooleanProperty("db.transactional");
		db_deferred_write = ConfigUtil.getBooleanProperty("db.deferred_write");
	}*/
}
