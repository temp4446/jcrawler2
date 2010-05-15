package cn.edu.sjtu.ltlab.jcrawler.parser;

import it.unimi.dsi.parser.BulletParser;
import it.unimi.dsi.parser.callback.LinkExtractor;
import it.unimi.dsi.parser.callback.TextExtractor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cn.edu.sjtu.ltlab.jcrawler.config.FetcherConfig;
import cn.edu.sjtu.ltlab.jcrawler.util.UrlUtil;

public class BaseHTMLParser {

	private String title;
	private String text;
	private Set<String> urls;
	
	private BulletParser bulletParser = new BulletParser();
	
	/**
	 * Extract title, text and urls from the html
	 * @param html
	 * 		The html content
	 * @param url
	 * 		The url of this html
	 */
	public void parse(String html, String url) {
		
		setTitle(html);
		setText(html);
		setUrls(html, url);
	}

	/**
	 * Return title of the html
	 * @return
	 * 		title of the html
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Extract title from the html
	 * @param html
	 * 		The html content
	 */
	public void setTitle(String html) {
		TextExtractor textExtractor = new TextExtractor();
		char[] chars = html.toCharArray();
		bulletParser.setCallback(textExtractor);
		bulletParser.parse(chars);
		title = textExtractor.title.toString().trim();
	}
	
	public void setTitleStr(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	public void setText(String html) {
		TextExtractor textExtractor = new TextExtractor();
		char[] chars = html.toCharArray();
		bulletParser.setCallback(textExtractor);
		bulletParser.parse(chars);
		text = textExtractor.text.toString().trim();
	}

	public String filterHTML(String html) {
		TextExtractor textExtractor = new TextExtractor();
		char[] chars = html.toCharArray();
		bulletParser.setCallback(textExtractor);
		bulletParser.parse(chars);
		return textExtractor.text.toString().trim();
	}
	
	public Set<String> getUrls() {
		return urls;
	}

	/**
	 * Extract urls from this html
	 * @param html
	 * @param url
	 */
	public void setUrls(String html, String url) {
		char[] chars = html.toCharArray();
		urls = new HashSet<String>();
		final int MAX_OUT_LINKS = FetcherConfig.max_outlinks;
		LinkExtractor linkExtractor = new LinkExtractor();
		bulletParser.setCallback(linkExtractor);
		bulletParser.parse(chars);
		Iterator<String> it = linkExtractor.urls.iterator();
		for(int urlCount = 0; it.hasNext() && urlCount < MAX_OUT_LINKS; ) {
			String href = it.next().trim();
			if(href.length() == 0)
				continue;
			if(href.startsWith("http://")) {
				String hrefWithoutProtocol = href.substring(7);
				if(!hrefWithoutProtocol.contains("@") && !hrefWithoutProtocol.contains(":")) {
					urls.add(UrlUtil.getCanonicalURL(href, url).toExternalForm());
					urlCount++;
				}
			}
		}
	}

	
}
