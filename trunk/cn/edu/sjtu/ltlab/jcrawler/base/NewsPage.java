package cn.edu.sjtu.ltlab.jcrawler.base;

import java.util.Calendar;

import cn.edu.sjtu.ltlab.jcrawler.util.DateUtils;

public class NewsPage extends Page{
	
	private String category;
	private String topic;
	private Calendar pubtime;
	
	public NewsPage() {
		this.category = "";
		this.topic = "";
		this.pubtime = null;
	}
	
	public NewsPage(WebUrl url) {
		super(url);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * Get the publish time of this page
	 * @return the publish time of this page
	 */
	public String getPubtime() {
		return DateUtils.getString(pubtime, DateUtils.TIME_PATTERN_SINA);
	}

	/**
	 * Set the publish time of this page
	 * @param the publish time of this page
	 */
	public void setPubTime(String pubtime) {
		this.pubtime = DateUtils.getCalendar(pubtime, DateUtils.TIME_PATTERN_SINA);
	}
}
