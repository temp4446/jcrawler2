package cn.edu.sjtu.ltlab.jcrawler.base;

import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import cn.edu.sjtu.ltlab.jcrawler.config.FetcherConfig;

public class Page {

	private WebUrl url;
	private String html;
	private String text;
	private String title;
	private ArrayList<WebUrl> urls;
	private ByteBuffer bBuf;
	
	public Page() {
	}
	
	public Page(WebUrl url) {
		this.url = url;
	}
	
	public boolean load(final InputStream in, final int totalSize) {
		
		if(totalSize > 0)
			this.bBuf = ByteBuffer.allocate(totalSize + 1024);
		else
			this.bBuf = ByteBuffer.allocate(FetcherConfig.max_download_size);
		
		final byte[] b = new byte[1024];
		int len;
		double finished = 0;
		try {
			while((len = in.read(b)) != -1) {
				if(finished + b.length > this.bBuf.capacity())
					break;
				
				this.bBuf.put(b, 0, len);
				finished += len;
			}
		} catch (final BufferOverflowException boe) {
			System.err.println("Page Size Exceeds maximum allowed");
			return false;
		} catch (final Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
		this.html = "";
		this.bBuf.flip();
		this.html += Charset.forName(FetcherConfig.default_charset).decode(this.bBuf);
		this.bBuf.clear();
		if (!this.html.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public WebUrl getUrl() {
		return url;
	}

	public void setUrl(WebUrl url) {
		this.url = url;
	}

	public String getHtml() {
		return html;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<WebUrl> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<WebUrl> urls) {
		this.urls = urls;
	}
}
