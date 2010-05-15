package cn.edu.sjtu.ltlab.jcrawler.util;

import java.io.File;

public class IOUtil {

	public static boolean deleteFolder(File folder) {
		
		return deleteFolderContent(folder) && folder.delete();
	}

	public static boolean deleteFolderContent(File folder) {
		
		System.out.println("Deleting content of:" + folder.getAbsolutePath());
		
		File[] files = folder.listFiles();
		for(File f: files) {
			if(f.isFile())
				if(!f.delete())
					return false;
			else {
				if( !deleteFolderContent(f) || !f.delete()) // not the same with original
					return false;
			}
		}
		return true;
	}
}
