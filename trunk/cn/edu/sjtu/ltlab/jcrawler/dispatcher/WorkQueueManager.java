package cn.edu.sjtu.ltlab.jcrawler.dispatcher;

import java.util.ArrayList;
import java.util.Iterator;

import cn.edu.sjtu.ltlab.jcrawler.base.WebUrl;
import cn.edu.sjtu.ltlab.jcrawler.bdbhelper.UrlDB;

import com.sleepycat.je.DatabaseException;

public class WorkQueueManager {

	private static WorkQueue workQueue;
	private static Object mutex = WorkQueueManager.class.toString() + "_mutex";
	private static Object waitingList = WorkQueueManager.class.toString() + "_WaitingList";
	private static boolean isFinished = false;
	
	public static void init() {
		workQueue = new WorkQueue();
	}
	
	public static void schedule(WebUrl url) {
		synchronized(mutex) {
			try {
				workQueue.put(url);
			} catch(DatabaseException e) {
				System.err.println("Error while putting the url to the work queue!");
			}
		}
	}
	
	public static void scheduleAll(ArrayList<WebUrl> urls) {
		synchronized(mutex) {
			Iterator<WebUrl> it = urls.iterator();
			while(it.hasNext())
			{
				WebUrl url = it.next();
				schedule(url);
			}
		}
		synchronized(waitingList) {
			waitingList.notifyAll();
		}
	}
	
	public static void getNextURLs(int max, ArrayList<WebUrl> result) {
		while (true) {
			synchronized (mutex) {
				try {						
					ArrayList<WebUrl> curResults = workQueue
							.get(max);
					workQueue.delete(curResults.size());
					result.addAll(curResults);
				} catch (DatabaseException e) {
					System.err
							.println("Error while getting next urls: "
									+ e.getMessage());
					e.printStackTrace();
				}
				if (result.size() > 0) {
					return;
				}
			}
			try {
				synchronized (waitingList) {
					waitingList.wait();
				}
			} catch (InterruptedException e) {
			}
			if (isFinished) {
				return;
			}
		}
	}
	
	public static boolean isFinished() {
		return isFinished;
	}
	
	public static void finish() {
		isFinished = true;
		synchronized(waitingList) {
			waitingList.notifyAll();
		}
	}
	
	public static long getQueueLength() {
		return workQueue.getQueueLength();
	}
	
	public static void sync() {
		workQueue.sync();
		UrlDB.sync();
	}
	
	public static void close() {
		sync();
		workQueue.close();
	}
}
