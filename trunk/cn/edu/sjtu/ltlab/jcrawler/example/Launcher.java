package cn.edu.sjtu.ltlab.jcrawler.example;

import cn.edu.sjtu.ltlab.jcrawler.crawler.CrawlerController;

public class Launcher {

	public static void main(String[] args) throws Exception {
		int numberOfCrawlers = 1;
		
		CrawlerController controller = new CrawlerController();		
		controller.addSeed("http://news.sina.com.cn/");
		
		// Be polite:
		// Make sure that we don't send more than 5 requests per second (200 milliseconds between requests).
		controller.setPolitenessDelay(200);
		
		// Do you need to set a proxy?
		// If so, you can uncomment the following line
		// controller.setProxy("proxyserver.example.com", 8080);
		// OR
		// controller.setProxy("proxyserver.example.com", 8080, username, password);
		
		controller.start(SinaCrawler.class, numberOfCrawlers);
	}
}
