package cn.edu.sjtu.ltlab.jcrawler.util;

import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;

import cn.edu.sjtu.ltlab.jcrawler.config.FetcherConfig;

public class HttpClientUtil {

	public static DefaultHttpClient getHttpClient() {
		
		HttpParams params = new BasicHttpParams();
		HttpProtocolParamBean paramsBean = new HttpProtocolParamBean(params);
		paramsBean.setVersion(HttpVersion.HTTP_1_1);
		paramsBean.setContentCharset("GBK");
		paramsBean.setUseExpectContinue(true);
		params.setParameter("http.useragent", FetcherConfig.user_agent);
		params.setParameter("http.socket.timeout", FetcherConfig.socket_timeout);
		params.setParameter("http.connection.timeout", FetcherConfig.connection_timeout);
		
		ConnPerRouteBean connPerRouteBean = new ConnPerRouteBean();
		connPerRouteBean.setDefaultMaxPerRoute(FetcherConfig.max_connections_per_host);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRouteBean);
		ConnManagerParams.setMaxTotalConnections(params, FetcherConfig.max_total_connections);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);
		return new DefaultHttpClient(connectionManager, params);
	}
}
