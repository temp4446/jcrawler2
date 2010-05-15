package cn.edu.sjtu.ltlab.jcrawler.crawler;

import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cn.edu.sjtu.ltlab.jcrawler.base.Page;
import cn.edu.sjtu.ltlab.jcrawler.base.WebUrl;
import cn.edu.sjtu.ltlab.jcrawler.bdbhelper.UrlDB;
import cn.edu.sjtu.ltlab.jcrawler.config.FetcherConfig;
import cn.edu.sjtu.ltlab.jcrawler.util.HttpClientUtil;
import cn.edu.sjtu.ltlab.jcrawler.util.UrlUtil;

/**
 * Get HTML Content and Store it in page's HTML field
 * @author Wenson
 *
 */
public class PageFetcher {

	private static final Logger logger = Logger.getLogger(PageFetcher.class.getName());
	private static DefaultHttpClient httpClient;
	private static Object mutex = PageFetcher.class.toString() + "_mutex";
	private static int processedCount = 0;
	private static long startOfPeriod = 0;
	private static long lastFetchTime = 0;
	private static long politenessDelay = FetcherConfig.default_politeness_delay;
	private static final int MAX_DOWNLOAD_SIZE = FetcherConfig.max_download_size;
	
	static{
		logger.setLevel(Level.INFO);
		httpClient = HttpClientUtil.getHttpClient();
	}
	
	public static int fetch(Page result) {
		try {
			String url = result.getUrl().getUrl();
			HttpGet get = new HttpGet(url);
			logAndSleep();
			HttpResponse response = httpClient.execute(get);
			//Error handle
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				if(response.getStatusLine().getStatusCode() != HttpStatus.SC_NOT_FOUND)
					logger.info("Failed:" + response.getStatusLine().toString() + ",while fetching " + url);
				return response.getStatusLine().getStatusCode();
			}
			//Redirect handle
			String uri = get.getURI().toString();
			if(!uri.equals(url) && !uri.equals(UrlUtil.getCanonicalURL(url))) {
				int docID = UrlDB.getDocID(uri);
				if(docID != -1)
				{
					if (docID > 0)
						return PageFetchStatus.RedirectedPageIsSeen;
					else
						result.setUrl(new WebUrl(uri, -docID));
				}
			}
			//OK handle
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				long size = entity.getContentLength();
				if (size == -1) {
					Header length = response.getLastHeader("Content-Length");
					if (length == null)
						length = response.getLastHeader("Content-length");
					
					if (length != null)
						size = Integer.parseInt(length.getValue());
					else
						size = -1;
				}
				if (size > MAX_DOWNLOAD_SIZE) {
					return PageFetchStatus.PageTooBig;
				}
				
				if (result.load(entity.getContent(), (int) size)) {
					return PageFetchStatus.OK;
				} else {
					return PageFetchStatus.PageLoadError;
				}
			}
		} catch (Exception e) {
			if (e.getMessage() == null)
				logger.error("Error while fetching " + result.getUrl().getUrl());
			else
				logger.error(e.getMessage() + " while fetching " + result.getUrl().getUrl());
		}
		return PageFetchStatus.UnknownError;
	}
	
	public static void logAndSleep(){
		try {
			synchronized (mutex) {
				long now = (new Date()).getTime();
				if (now - startOfPeriod > 10000) {
					logger.info("Number of pages fetched per second: "
							+ processedCount
							/ ((now - startOfPeriod) / 1000));
					processedCount = 0;
					startOfPeriod = now;
				}
				processedCount++;
	
				if (now - lastFetchTime < politenessDelay) {
					Thread.sleep(politenessDelay - (now - lastFetchTime));
				}
				lastFetchTime = (new Date()).getTime();
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static long getPolitenessDelay() {
		return politenessDelay;
	}
	public static void setPolitenessDelay(long politenessDelay) {
		PageFetcher.politenessDelay = politenessDelay;
	}
	
	public static void setProxy(String host, int port) {
		HttpHost proxy = new HttpHost(host, port);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	}
	
	public static void setProxy(String host, int port, String user, String password){
		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(host, port),
				new UsernamePasswordCredentials(user, password));
		setProxy(host, port);
	}
}
