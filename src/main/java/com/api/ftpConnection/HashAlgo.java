package com.api.ftpConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgo {
	private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		
		byte[] byteArray = new byte[1024];
		int bytesCount = 0;
		
		while((bytesCount = fis.read(byteArray)) != -1) {
			digest.update(byteArray, 0, bytesCount);
		}
		
		fis.close();
		
		byte[] bytes = digest.digest();
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < bytes.length; i++) 
			stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	
		return stringBuilder.toString();
	}

	public static String getFileHash(File file) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		String checkSum = getFileChecksum(messageDigest, file);
		return checkSum;
	}
}
