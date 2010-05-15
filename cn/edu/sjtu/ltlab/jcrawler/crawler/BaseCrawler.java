package cn.edu.sjtu.ltlab.jcrawler.crawler;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cn.edu.sjtu.ltlab.jcrawler.base.Page;
import cn.edu.sjtu.ltlab.jcrawler.base.WebUrl;
import cn.edu.sjtu.ltlab.jcrawler.bdbhelper.UrlDB;
import cn.edu.sjtu.ltlab.jcrawler.dispatcher.WorkQueueManager;
import cn.edu.sjtu.ltlab.jcrawler.parser.BaseHTMLParser;


public class BaseCrawler implements Runnable{

	private static final Logger logger = Logger.getLogger(BaseCrawler.class.getName());
	private static final int PROCESS_OK = -12;
	private CrawlerController controller;
	private BaseHTMLParser parser;
	int id;
	private Thread thread;
	
	public BaseCrawler() {
		controller = new CrawlerController();
		parser = new BaseHTMLParser();
	}
	
	public BaseCrawler(BaseHTMLParser parser) {
		controller = new CrawlerController();
		this.parser = parser;
	}
	
	public BaseCrawler(int id, BaseHTMLParser parser) {
		this.id = id;
		this.parser = parser;
		controller = new CrawlerController();
	}
	
	public void run() {
		onStart();
		while (true) {
			ArrayList<WebUrl> assignedURLs = new ArrayList<WebUrl>(50);
			WorkQueueManager.getNextURLs(50, assignedURLs);
			if (assignedURLs.size() == 0) {
				if (WorkQueueManager.isFinished()) {
					return;
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				for (WebUrl url : assignedURLs) {
					if (url != null) {
						preProcessPage(url);
					}
				}
			}
		}
	}
	
	private int preProcessPage(WebUrl curURL) {
		if (curURL == null)
			return -1;

		try {
			Page page = new Page(curURL);
			
			int statusCode = PageFetcher.fetch(page);
			// The page might have been redirected. So we have to refresh curURL
			curURL = page.getUrl();
			int docid = curURL.getDocID();
			if (statusCode != PageFetchStatus.OK) {
				if (statusCode == PageFetchStatus.PageTooBig) {
					logger.error("Page was bigger than max allowed size: " + curURL.getUrl());
				} 
				return statusCode;
			}

			parser.parse(page.getHtml(), curURL.getUrl());
			page.setText(parser.getText());/*need to be modified*/
			page.setTitle(parser.getTitle());

			Iterator<String> it = parser.getUrls().iterator();
			ArrayList<WebUrl> toSchedule = new ArrayList<WebUrl>();
			ArrayList<WebUrl> toList = new ArrayList<WebUrl>();
			while (it.hasNext()) {
				String url = it.next();				
				if (url != null) {
					int newdocid = UrlDB.getDocID(url);
					if (newdocid > 0) {
						if (newdocid != docid) {
							toList.add(new WebUrl(url, newdocid));
						}
					} else {
						toList.add(new WebUrl(url, -newdocid));
						WebUrl cur = new WebUrl(url, -newdocid);
						if (shouldVisit(cur)) {
							cur.setParentDocID(docid);
							toSchedule.add(cur);
						}
					}
				}
			}
			WorkQueueManager.scheduleAll(toSchedule);
			page.setUrls(toList);
			visit(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage() + ", while processing: "+ curURL.getUrl());
		}
		return PROCESS_OK;
	}
	
	public boolean shouldVisit(WebUrl url) {		
		return true;
	}

	public void visit(Page page) {	
	}
	
	public void onStart() {
	}
	
	public void onBeforeExit() {
	}
	
	public Object getMyLocalData() {
		return null;
	}
	
	public CrawlerController getController() {
		return controller;
	}
	
	public void setController(CrawlerController controller) {
		this.controller = controller;
	}
	
	public BaseHTMLParser getParser() {
		return parser;
	}
	
	public void setParser(BaseHTMLParser parser) {
		this.parser = parser;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread myThread) {
		this.thread = myThread;
	}
}
