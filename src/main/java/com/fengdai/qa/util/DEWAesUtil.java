package com.fengdai.qa.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DEWAesUtil {
	public static final String VIPARA = "1269571569321021";
	public static final String bm = "utf-8";

	/**
	 * 字节数组转化为大写16进制字符串
	 * @param b
	 * @return
	 */
	private static String byte2HexStr(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			String s = Integer.toHexString(b[i] & 0xFF);
			if (s.length() == 1) {
				sb.append("0");
			}
			sb.append(s.toUpperCase());
		}
		return sb.toString();
	}
	/**
	 * 16进制字符串转字节数组
	 * @param s
	 * @return
	 */
	private static byte[] str2ByteArray(String s) {
		int byteArrayLength = s.length() / 2;
		byte[] b = new byte[byteArrayLength];
		for (int i = 0; i < byteArrayLength; i++) {
			byte b0 = (byte) Integer.valueOf(s.substring(i * 2, i * 2 + 2), 16).intValue();
			b[i] = b0;
		}
		return b;
	}
	/**
	 * AES 加密
	 * @param content 明文
	 * @param password 生成秘钥的关键字
	 * @return
	 */
	public static String aesEncrypt(String content, String password) {
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			/*
			 * KeyGenerator kgen = KeyGenerator.getInstance("AES"); SecureRandom secureRandom =
			 * SecureRandom.getInstance("SHA1PRNG");
			 * secureRandom.setSeed(password.getBytes("utf-8")); kgen.init(128, secureRandom);
			 * SecretKey secretKey = kgen.generateKey(); byte[] deraw = secretKey.getEncoded();
			 * SecretKeySpec key = new SecretKeySpec(deraw, "AES");
			 */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(content.getBytes(bm));
			String baseStr = new Base64().encodeToString(encryptedData);
			String encryptedString = baseStr.replaceAll("=", "_");
			return encryptedString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * AES 解密
	 * @param content 密文
	 * @param password 生成秘钥的关键字
	 * @return
	 */
	public static String aesDecrypt(String content, String password) {
		try {
			content = content.replaceAll("_", "=");
			byte[] byteMi = new Base64().decode(content);
			// byte[] byteMi= str2ByteArray(content);
			IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
			SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
			/*
			 * KeyGenerator kgen = KeyGenerator.getInstance("AES"); SecureRandom secureRandom =
			 * SecureRandom.getInstance("SHA1PRNG");
			 * secureRandom.setSeed(password.getBytes("utf-8")); kgen.init(128, secureRandom);
			 * SecretKey secretKey = kgen.generateKey(); byte[] deraw = secretKey.getEncoded();
			 * SecretKeySpec key = new SecretKeySpec(deraw, "AES");
			 */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte[] decryptedData = cipher.doFinal(byteMi);
			return new String(decryptedData, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String args[]) throws Exception {
		System.out.println(aesDecrypt("hnHjzg+q9PfykRUijD9OoSsR/6xjaiJHeeVaQ3hejGY_", "DWERP@#12$3458ta"));
	}
}
