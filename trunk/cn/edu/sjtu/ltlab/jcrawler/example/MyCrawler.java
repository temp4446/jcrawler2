package cn.edu.sjtu.ltlab.jcrawler.example;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cn.edu.sjtu.ltlab.jcrawler.base.Page;
import cn.edu.sjtu.ltlab.jcrawler.base.WebUrl;
import cn.edu.sjtu.ltlab.jcrawler.crawler.BaseCrawler;



public class MyCrawler extends BaseCrawler {

//	Pattern filters = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
//			+ "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf"
//			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	Pattern filters = Pattern.compile(".*sina.*2010-04-25.*shtml$");
	
	public MyCrawler() {
	}

	public boolean shouldVisit(WebUrl url) {
		String href = url.getUrl().toLowerCase();
		if (filters.matcher(href).matches()) {
			return true;
		}
		
		return false;
	}
	
	public void visit(Page page) {
		int docid = page.getUrl().getDocID();
        String url = page.getUrl().getUrl();         
        String text = page.getText();
        ArrayList<WebUrl> links = page.getUrls();
		int parentDocid = page.getUrl().getParentDocID();
		
		System.out.println("Docid: " + docid);
		System.out.println("URL: " + url);
		System.out.println("Title: " + page.getTitle());
//		System.out.println("Test:" + text);
		System.out.println("Text length: " + text.length());
		System.out.println("Number of links: " + links.size());
		System.out.println("Docid of parent page: " + parentDocid);
		System.out.println("=============");
	}	
}
