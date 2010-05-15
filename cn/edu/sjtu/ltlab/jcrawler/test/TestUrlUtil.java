package cn.edu.sjtu.ltlab.jcrawler.test;

import cn.edu.sjtu.ltlab.jcrawler.util.UrlUtil;

public class TestUrlUtil {

	public static void main(String args[]) {

		// Test Canonical URL
		System.out.println(UrlUtil.getCanonicalURL(
				"http://www.sina.com.cn/p/2010-04-21/index.shtml#news"));

		// Test Encode URL
		System.out.println(UrlUtil.getCanonicalURL(
				"http://www.sina.com.cn/p/2010 04 21/пбне.shtml"));
		
		// Test herf in contex
		System.out.println(UrlUtil.getCanonicalURL(
				"p/2010-04-21/index.shtml#news", "http://www.sina.com.cn/index.html"));

	}

}
