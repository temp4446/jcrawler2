package cn.edu.sjtu.ltlab.jcrawler.base;

public class WebUrl {

	private String url;
	private int docID;
	private int parentDocID;
	
	public WebUrl(String url, int docID) {
		this.url = url;
		this.docID = docID;
	}
	
	public WebUrl(String url) {
		this(url, 0); // amazing!
	}

	public boolean equals(Object obj) {
		
		if(this == obj)
			return true;
		else if(obj == null || obj.getClass() != this.getClass())
			return false;
		else if(this.url == null)
			return false;
		else return this.url.equals(((WebUrl)obj).getUrl());
	}
	
	public String toString() {
		return url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public int getParentDocID() {
		return parentDocID;
	}

	public void setParentDocID(int parentDocID) {
		this.parentDocID = parentDocID;
	}
	
	
}
