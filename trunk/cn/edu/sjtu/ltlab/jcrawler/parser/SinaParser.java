package cn.edu.sjtu.ltlab.jcrawler.parser;

import java.io.File;
import java.util.Calendar;

import cn.edu.sjtu.ltlab.jcrawler.config.SysConfig;
import cn.edu.sjtu.ltlab.jcrawler.util.DateUtils;

public class SinaParser extends BaseHTMLParser {

	private String pubtime;
	private String category;
	private String topic;
	
	public void parse(String html, String url) {
		setTitle(html);
		setText(html);
		super.setUrls(html, url);
		setPubTime(html);
		setCategory(html);
		setTopic(html);
	}
	
	public void setTitle(String html) {
		int start = html.indexOf("<h1 id=\"artibodyTitle\"");
		int end = html.indexOf("</", start);
		if(start > 0 && end > 0)
		{
			String titleHTML = html.substring(start, end);
			int titleWordStart = titleHTML.indexOf('>');
			super.setTitleStr(titleHTML.substring(titleWordStart + 1));
		}
		else
			super.setTitle(html);
	}
	
	public void setText(String html) {
		int start = html.indexOf("<!-- 正文内容 begin -->");
		int end = html.indexOf("<!-- 正文内容 end -->", start);
		if(start > 0 && end > 0)
		{
			String textHTML = html.substring(start, end);
			super.setText(textHTML);
		}
		else
			super.setText(html);
	}
	
	public void setPubTime(String html) {
		int start = html.indexOf("<span id=\"pub_date\">");
		int end = html.indexOf("</span>", start);
		if(start > 0 && end > 0)
		{
			String pubtime = super.filterHTML(html.substring(start + 20, end));
			pubtime = pubtime.replace(" ", "");
			this.pubtime = pubtime;
		}
		else
			this.pubtime = DateUtils.getString(Calendar.getInstance(), DateUtils.TIME_PATTERN_SINA);
	}
	
	public void setCategory(String html) {
		this.category = "";
	}
	
	public void setTopic(String html) {
		this.topic = "";
	} 
	
	public String getPubTime() {
		return this.pubtime;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public String getTopic() {
		return this.topic;
	}
	
	public void writeXML() {
		File f = new File(SysConfig.sys_save_path);
		if(!f.exists())
			f.mkdir();
		
		
	}
}
