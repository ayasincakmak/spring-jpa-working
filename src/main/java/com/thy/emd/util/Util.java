package com.thy.emd.util;

import java.nio.charset.Charset;

public class Util {
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static final String EMPTY_STRING = "";
	
	public static String convertHexToString(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}

	public static String byteArrayToHexString(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String ConvertByteArrayToEbcdicText(byte[] inventoryFieldValueArray) throws Exception {
		String text = "";
		text = new String(inventoryFieldValueArray, 0, inventoryFieldValueArray.length, "Cp037");
		text = text.replace("", EMPTY_STRING);
		return text;
	}

	public static String ebcdicToUtf16(byte... b) {
		String utf16 = new String(b, Charset.forName("IBM500"));
		return System.out.format("%02x -> %04x%n", b[0] & 0xFF, utf16.charAt(0) & 0xFFFF).toString();
	}

	public static String utf16ToEbcdic(String s) {
		byte[] b = s.getBytes(Charset.forName("IBM500"));
		return System.out.format("%04x -> %02x%n", s.charAt(0) & 0xFFFF, b[0] & 0xFF).toString();
	}

}
