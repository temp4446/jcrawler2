package cn.edu.sjtu.ltlab.jcrawler.crawler;

import java.lang.Thread.State;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import cn.edu.sjtu.ltlab.jcrawler.base.WebUrl;
import cn.edu.sjtu.ltlab.jcrawler.bdbhelper.UrlDB;
import cn.edu.sjtu.ltlab.jcrawler.dispatcher.WorkQueueManager;
import cn.edu.sjtu.ltlab.jcrawler.util.UrlUtil;


/**
 * Control the work of crawler
 * @author Wenson
 *
 */
public final class CrawlerController {
	
	private static final Logger logger = Logger.getLogger(CrawlerController.class.getName());
	private ArrayList<Object> crawlersLocalData = new ArrayList<Object>();
	private ArrayList<Thread> threads;
	
	public CrawlerController() {
		WorkQueueManager.init();
		UrlDB.init();
	}
	
	public <T extends BaseCrawler> void start(Class<T> _c, int numberOfCrawlers) {
		crawlersLocalData.clear();
		ArrayList<T> crawlers = createCrawlersThread(_c, numberOfCrawlers);
		while(true) {
			sleepSeconds(10);
			boolean someoneIsWorking = monitorAllCrawlers(_c, crawlers);
			if(!someoneIsWorking)
				if(tryToFinish(crawlers))
					continue;
		}
	}
	
	private <T extends BaseCrawler> ArrayList<T> createCrawlersThread(Class<T> _c, int numberOfCrawlers) {
		threads = new ArrayList<Thread>();
		ArrayList<T> crawlers = new ArrayList<T>();
		try {
			for(int i = 0; i < numberOfCrawlers; i++) {
				T crawler = _c.newInstance();
				Thread thread = new Thread(crawler, "Crawler_" + i);
				crawler.setController(this);
				crawler.setThread(thread);
				crawler.setId(i);
				thread.start();
				crawlers.add(crawler);
				threads.add(thread);
				logger.info("Crawler_" + i + " started.");
			}
			return crawlers;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public <T extends BaseCrawler> boolean monitorAllCrawlers(Class<T> _c, ArrayList<T> crawlers) {
		boolean someoneIsWorking = false;
		try {
			for (int i = 0; i < threads.size(); i++) {
				Thread thread = threads.get(i);
				if (!thread.isAlive()) {
					logger.info("Thread " + i + " was dead, I'll recreate it.");
					T crawler = _c.newInstance();
					thread = new Thread(crawler, "Crawler " + (i + 1));
					threads.remove(i);
					threads.add(i, thread);
					crawler.setThread(thread);
					crawler.setId(i + 1);
					crawler.setController(this);
					thread.start();
					crawlers.remove(i);
					crawlers.add(i, crawler);
				} else if (thread.getState() == State.RUNNABLE) {
					someoneIsWorking = true;
				}
			}
			return someoneIsWorking;
		} catch (Exception e) {
			e.printStackTrace();
			return someoneIsWorking;
		}
	}
	
	public <T extends BaseCrawler> boolean tryToFinish(ArrayList<T> crawlers) {
		sleepSeconds(60);
		
		if(isAnyThreadWorking())
			return true;
		else {
			long queueLength = WorkQueueManager.getQueueLength();
			if (queueLength > 0) {							
				return true;	//all crawlers are dead but there is urls left
			}
			sleepSeconds(180);
			queueLength = WorkQueueManager.getQueueLength();
			if (queueLength > 0) {							
				return true;	//all crawlers are dead but there is urls left
			}
			WorkQueueManager.close();
			logger.info("All of the crawlers are stopped. Finishing the process.");
			for (T crawler : crawlers) {
				crawler.onBeforeExit();
				crawlersLocalData.add(crawler.getMyLocalData());
			}
			
			// At this step, frontier notifies the threads that were waiting for new URLs and they should stop
			// We will wait a few seconds for them and then return.
			WorkQueueManager.finish();
			sleepSeconds(10);
			return false;
		}
	}

	public void addSeed(String url) {
		String canonicalUrl = UrlUtil.getCanonicalURL(url);
		if(canonicalUrl == null) {
			logger.error("Invalid Seed URL: " + url);
			return;
		}
		int docID = UrlDB.getDocID(canonicalUrl);
		if(docID > 0) //This url is already seen
			return;
		WebUrl URL = new WebUrl(canonicalUrl, -docID);
		WorkQueueManager.schedule(URL);
	}
	
	public void setPolitenessDelay(int milliseconds) {
		if(milliseconds < 0)
			return;
		if(milliseconds > 10000)
			milliseconds = 10000;
		PageFetcher.setPolitenessDelay(milliseconds);
	}
	
	public void setProxy(String host, int port) {
		PageFetcher.setProxy(host, port);
	}
	
	public void setProxy(String host, int port, String user, String password) {
		PageFetcher.setProxy(host, port, user, password);
	}
	
	private void sleepSeconds(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private boolean isAnyThreadWorking() {
		boolean someoneIsWorking = false;
		for(int i = 0; i < threads.size(); i++) {
			Thread thread = threads.get(i);
			if(thread.isAlive() && thread.getState() == State.RUNNABLE)
				someoneIsWorking = true;
		}
		return someoneIsWorking;
	}
	
	public ArrayList<Object> getCrawlersLocalData() {
		return crawlersLocalData;
	}
}
