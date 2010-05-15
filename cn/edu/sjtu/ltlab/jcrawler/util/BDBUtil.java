package cn.edu.sjtu.ltlab.jcrawler.util;

import java.io.File;

import cn.edu.sjtu.ltlab.jcrawler.config.BDBConfig;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BDBUtil {

	public static Database getDB(String dbName)
	{
		try {
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(BDBConfig.env_allow_create);
			envConfig.setTransactional(BDBConfig.env_transactinal);
			envConfig.setLocking(BDBConfig.env_locking);
			
			File envHome = new File(BDBConfig.env_home);
			if(!envHome.exists())
				envHome.mkdirs();
	//		IOUtil.deleteFolderContent(envHome);
			
			Environment env = new Environment(envHome, envConfig);
			
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(BDBConfig.db_allow_create);
			dbConfig.setTransactional(BDBConfig.db_transactional);
			dbConfig.setDeferredWrite(BDBConfig.db_deferred_write);
			
			return env.openDatabase(null, dbName, dbConfig);
		} catch (DatabaseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
