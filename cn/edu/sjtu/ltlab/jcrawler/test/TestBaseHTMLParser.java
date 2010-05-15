package cn.edu.sjtu.ltlab.jcrawler.test;

import cn.edu.sjtu.ltlab.jcrawler.parser.BaseHTMLParser;

public class TestBaseHTMLParser {

	public static void main(String args[]) {
		BaseHTMLParser parser = new BaseHTMLParser();
		String html = "<html><head><title>Test title</title></head><body>Test text<p><a href=\"http://g.cn\">url</a></body></html>";
		parser.parse(html, null);
		
		System.out.println(parser.getTitle());
		System.out.println(parser.getText());
		System.out.println(parser.getUrls());
	}
}
