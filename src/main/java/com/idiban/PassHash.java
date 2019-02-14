package main.java.com.idiban;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PassHash {

	private static PassHash passHash;

	// to Hashing
	public String toHash(String passToHash) {

		String securityPassHash;

		try {
			securityPassHash = goodPassHash(passToHash);
			return securityPassHash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Boolean comparePass(String newPassToCompare, String oldPassToCompare) {

		Boolean result;

		try {
			result = validatePassword(newPassToCompare, oldPassToCompare);
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static PassHash getInstance() {
		if (passHash == null) {
			passHash = new PassHash();
			return passHash;
		} else {
			return passHash;
		}
	}

	private static String goodPassHash(String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 1250;
		char[] chars = pass.toCharArray();
		byte[] salt = getSalt();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + hex(salt) + ":" + hex(hash);
	}

	private static byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

	private static String hex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int lenght = (array.length * 2) - hex.length();
		if (lenght > 0) {
			return String.format("%0" + lenght + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	private static boolean validatePassword(String originalPass, String savedPass)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		String[] parts = savedPass.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);

		PBEKeySpec spec = new PBEKeySpec(originalPass.toCharArray(), salt, iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length;
		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}

	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

}
