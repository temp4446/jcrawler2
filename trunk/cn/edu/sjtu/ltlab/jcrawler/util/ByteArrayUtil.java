package cn.edu.sjtu.ltlab.jcrawler.util;

public class ByteArrayUtil {
	
	public static byte[] int2ByteArray(int value) {
		
		byte[] array = new byte[4];
		for(int i = 0; i < 4; i++) {
			int shift = (array.length - 1 - i) * 8;
			array[i] = (byte)((value >>> shift) & 0xFF);
		}
		
		return array;
	}
	
	public static byte[] long2ByteArray(long value){
		
		byte[] array = new byte[8];
		for(int i = 0; i < 8; i++) {
			int shift = (array.length - 1 - i) * 8;
			array[i] = (byte)((value >>> shift) & 0xFF);
		}
		
		return array;
	}
	
	public static void longIntoByteArray(long value, byte[] array, int offset) {
		
		for(int i = 0; i < 8; i++) {
			int shift = (array.length - 1 - i) * 8;
			array[i + offset] = (byte)((value >>> shift) & 0xFF);
		}
	}
	
	public static int byteArray2Int(byte[] array) {
		
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (array[i] & 0x000000FF) << shift;
		}
		return value;
	}
	
	public static long byteArray2Long(byte[] array) {
		
		int value = 0;
		for (int i = 0; i < 8; i++) {
			int shift = (8 - 1 - i) * 8;
			value += (array[i] & 0x000000FF) << shift;
		}
		return value;
	}
	
	public static long byteArrayIntoLong(byte[] array) {
		
		 return byteArrayIntoLong(array, 0);
	} 
	
	public static long byteArrayIntoLong(byte[] array, int offset) {
		
		int value = 0;
		for(int i = offset; i < 8; i++) {
			int shift = (8 - 1 - i) * 8;
			value += (array[i] & 0x000000FF) << shift;
		}
		return value;
	}
}
