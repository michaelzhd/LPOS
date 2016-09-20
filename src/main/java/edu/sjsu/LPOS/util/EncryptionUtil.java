package edu.sjsu.LPOS.util;

import java.security.MessageDigest;
import java.util.Base64;



public class EncryptionUtil {
	
	public static String digestWithMD5(String toDigest) {
		MessageDigest md5instance = null;
		try {
			md5instance = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		md5instance.update(toDigest.getBytes());
		byte[] digestedBytes = md5instance.digest();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < digestedBytes.length; i++) {
			String hexa = Integer.toHexString(digestedBytes[i] & 0xFF);
			//a byte should be two letters, but if the number is less than 16
			// then it will be only one letter
			if (hexa.length() == 1) {
				sb.append('0');
			}
			sb.append(hexa);
		}
		return sb.toString();
	}
	
	public static String decodeBase64(String encodedString) {
		if (encodedString == null) {
			return null;
		}
		byte[] decodedBytes = null;
		try{
			decodedBytes = Base64.getDecoder().decode(encodedString);
		} catch(Exception e) {
			e.printStackTrace();
		}
		String decodedString = new String(decodedBytes);
		return decodedString;
	}
	
	public static String encodeBase64(String srcString) {
		if (srcString == null) {
			return null;
		}
		String encodedString = Base64.getEncoder().encodeToString(srcString.getBytes());
		return encodedString;
	}

//	public static void main(String[] args) {
//		System.out.println(digestWithMD5("123456"));
//		
//	}
}
