package cn.edu.sjtu.ltlab.jcrawler.dispatcher;

import java.util.ArrayList;

import cn.edu.sjtu.ltlab.jcrawler.base.WebUrl;
import cn.edu.sjtu.ltlab.jcrawler.bdbhelper.WebUrlTupleBinding;
import cn.edu.sjtu.ltlab.jcrawler.util.BDBUtil;
import cn.edu.sjtu.ltlab.jcrawler.util.ByteArrayUtil;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;

public class WorkQueue {

	private Database pendingUrlsDB = null;
	private Cursor cursor;
	private WebUrlTupleBinding webUrlBinding;
	private static Object mutex = WorkQueue.class.toString() + "_mutex";
	
	public WorkQueue() throws DatabaseException {
		
		pendingUrlsDB = BDBUtil.getDB("PendingUrls");
		webUrlBinding = new WebUrlTupleBinding();
	}
	
	public ArrayList<WebUrl> get(int max) {
		synchronized(mutex) {
			ArrayList<WebUrl> results = new ArrayList<WebUrl>(max);
			
			try {
				cursor = pendingUrlsDB.openCursor(null, null);
				DatabaseEntry key = new DatabaseEntry();
				DatabaseEntry value = new DatabaseEntry();
				OperationStatus status = cursor.getFirst(key, value, null);
				
				for(int counter = 0; counter < max && status == OperationStatus.SUCCESS; ) {
					if(value.getData().length > 0) {
						WebUrl url = (WebUrl)webUrlBinding.entryToObject(value);
						results.add(url);
						counter ++;
					}
					status = cursor.getNext(key, value, null);
				}
				 
			} finally {
				if(cursor != null)
					cursor.close();
			}
			return results;
		}
	}
	
	public void put(WebUrl url) throws DatabaseException {
		synchronized(mutex) {
			DatabaseEntry key = new DatabaseEntry(ByteArrayUtil.int2ByteArray(url.getDocID()));
			DatabaseEntry value = new DatabaseEntry();
			webUrlBinding.objectToEntry(url, value);
			pendingUrlsDB.put(null, key, value);
		}
	}
	
	public void delete(int count) throws DatabaseException {
		synchronized(mutex) {
			try {
				cursor = pendingUrlsDB.openCursor(null, null);
				DatabaseEntry key = new DatabaseEntry();
				DatabaseEntry value = new DatabaseEntry();
				OperationStatus status = cursor.getFirst(key, value, null);
				
				for(int i = 0; i < count && status == OperationStatus.SUCCESS; i++) {
					cursor.delete();
					status = cursor.getNext(key, value, null);
				}
			} finally {
				if(cursor != null)
					cursor.close();
			}
		}
	}
	
	public void sync() {
		if(pendingUrlsDB == null)
			return;
		
		try {
			pendingUrlsDB.sync();
		} catch(DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if(pendingUrlsDB == null)
			return;
		
		try {
			pendingUrlsDB.close();
		} catch(DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public void closeCursor() {
		if(cursor == null)
			return;
		
		try {
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public WebUrl getFirst() {
		try {
			cursor = pendingUrlsDB.openCursor(null, null);
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry value = new DatabaseEntry();
			OperationStatus status = cursor.getFirst(key, value, null);
			if(status == OperationStatus.SUCCESS && value.getData().length > 0)
				return (WebUrl)webUrlBinding.entryToObject(value);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public WebUrl getNext() {
		try {
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry value = new DatabaseEntry();
			OperationStatus status = cursor.getNext(key, value, null);
			if(status == OperationStatus.SUCCESS && value.getData().length > 0)
				return (WebUrl)webUrlBinding.entryToObject(value);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public long getQueueLength() {
		try {
			return pendingUrlsDB.count();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}
