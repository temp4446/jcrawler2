package cn.edu.sjtu.ltlab.jcrawler.config;

import cn.edu.sjtu.ltlab.jcrawler.util.ConfigUtil;

public class FetcherConfig {
	
	public static int max_connections_per_host = ConfigUtil.getIntProperty("fetcher.max_connections_per_host");
	public static int max_total_connections = ConfigUtil.getIntProperty("fetcher.max_total_connections");
	public static int socket_timeout = ConfigUtil.getIntProperty("fetcher.socket_timeout");
	public static int connection_timeout = ConfigUtil.getIntProperty("fetcher.connection_timeout");;
	public static int max_outlinks = ConfigUtil.getIntProperty("fetcher.max_outlinks");;
	public static int max_download_size = ConfigUtil.getIntProperty("fetcher.max_download_size");;
	public static String user_agent = ConfigUtil.getStringProperty("fetcher.user_agent");;
	public static int default_politeness_delay = ConfigUtil.getIntProperty("fetcher.default_politeness_delay");
	public static String default_charset = ConfigUtil.getStringProperty("fetcher.default_charset");
	
/*	static {
		max_connections_per_host 	= ConfigUtil.getIntProperty("fetcher.max_connections_per_host");
		max_total_connections 		= ConfigUtil.getIntProperty("fetcher.max_total_connections");
		socket_timeout 				= ConfigUtil.getIntProperty("fetcher.socket_timeout");
		connection_timeout 			= ConfigUtil.getIntProperty("fetcher.connection_timeout");
		max_outlinks 				= ConfigUtil.getIntProperty("fetcher.max_outlinks");
		max_download_size 			= ConfigUtil.getIntProperty("fetcher.max_download_size");
		user_agent 					= ConfigUtil.getStringProperty("fetcher.user_agent");
		default_politeness_delay 	= ConfigUtil.getIntProperty("fetcher.default_politeness_delay");
		default_charset 			= ConfigUtil.getStringProperty("fetcher.default_charset");
	}*/
}
