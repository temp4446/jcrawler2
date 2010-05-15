package cn.edu.sjtu.ltlab.jcrawler.bdbhelper;

import cn.edu.sjtu.ltlab.jcrawler.util.BDBUtil;
import cn.edu.sjtu.ltlab.jcrawler.util.ByteArrayUtil;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;

public class UrlDB {

	private static Database urlDB = null;
	private static Object mutex = UrlDB.class.toString() + "_mutex";
	private static int lastDocID = 0;
	
	public static void init() throws DatabaseException {
		urlDB = BDBUtil.getDB("Urls");
		lastDocID = getDocCount();
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 * 		-1 error
	 * 		>0 already seen
	 * 		<0 new doc
	 */
	public static int getDocID(String url) {
		if(urlDB == null)
			return -1;
		
		synchronized(mutex) {
			try {
				DatabaseEntry key = new DatabaseEntry(url.getBytes());
				DatabaseEntry value = new DatabaseEntry();
				OperationStatus result = urlDB.get(null, key, value, null);
				
				// already seen this url
				if(result == OperationStatus.SUCCESS && value.getData().length > 0)
					return ByteArrayUtil.byteArray2Int(value.getData());
				else { // this url hasn't crawled
					lastDocID++;
					putDocID(url, lastDocID);
					return -lastDocID;
				} 
			} catch (Exception e) {
				e.printStackTrace();
				return -1; 
			}
		}
	}
	
	public static int putDocID(String url, int docID) {
		if(urlDB == null)
			return -1;
		
		synchronized(mutex) {
			try {
				DatabaseEntry key = new DatabaseEntry(url.getBytes());
				DatabaseEntry value = new DatabaseEntry(ByteArrayUtil.int2ByteArray(docID));
				urlDB.put(null, key, value);
				return 0;
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
	}
	
	public static int getDocCount() {
		try {
			return (int)urlDB.count();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void sync() {
		if(urlDB == null)
			return;
		
		try {
			urlDB.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close() {
		try {
			urlDB.close();
		} catch (DatabaseException e)
		{
			e.printStackTrace();
		}
	}
}
