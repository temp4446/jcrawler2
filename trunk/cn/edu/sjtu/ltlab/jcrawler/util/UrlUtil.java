package cn.edu.sjtu.ltlab.jcrawler.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import cn.edu.sjtu.ltlab.jcrawler.config.FetcherConfig;

public class UrlUtil {

	public static String getCanonicalURL(String url) {
		URL canonicalURL = getCanonicalURL(url, null);
		
		if (canonicalURL != null)
			return canonicalURL.toExternalForm();
		
		return null;
	}

	public static URL getCanonicalURL(String url, String context) {

		if(url.contains("#"))
			url = url.substring(0, url.indexOf("#")); // not elegant
		
		try {
			URLEncoder.encode(url, FetcherConfig.default_charset); // encoding should specified
			
			if(context == null)
				return new URL(url);
			else
				return new URL(new URL(context), url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
