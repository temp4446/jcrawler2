package cn.edu.sjtu.ltlab.jcrawler.bdbhelper;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import cn.edu.sjtu.ltlab.jcrawler.base.WebUrl;

public class WebUrlTupleBinding extends TupleBinding<WebUrl> {

	@Override
	public WebUrl entryToObject(TupleInput input) {
		
		WebUrl url = new WebUrl(input.readString(), input.readInt());
		url.setParentDocID(input.readInt());
		
		return url;
	}

	@Override
	public void objectToEntry(WebUrl url, TupleOutput output) {
		
		output.writeString(url.getUrl());
		output.writeInt(url.getDocID());
		output.writeInt(url.getParentDocID());
	}

}
