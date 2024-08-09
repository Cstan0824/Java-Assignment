package project.global;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

//PBKDF2(Password-Based Key Derivation Function 2) 
//no idea what shit is this 
public class HashAlgorithm { 
    private static final int ITERATIONS = 65536; // the number of iterations the pbkdf2 will run
    private static final int KEY_LENGTH = 256; // the length of derived key in bit
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512"; //Specified type of algorithm used in the pbkdf2
    // HMAC - Hash-based Message Authentication Code
    // SHA512 - produce 512 bit of hash value

    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    
    public static String HashPassword(String _password, byte[] _salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
            
        //generate hash txt with the password
        PBEKeySpec spec = new PBEKeySpec(_password.toCharArray(), _salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder();

        //concat the salt with the hashed password, make it more diff to get cracked
        return enc.encodeToString(_salt) + ":" + enc.encodeToString(hash);
    }

    public static boolean VerifyPassword(String _password, String _hashedPassword, byte[] _salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        //get hashed part
        String[] parts = _hashedPassword.split(":");

        //decode the hashed part
        byte[] hash = Base64.getDecoder().decode(parts[1]);

        //generate the hash txt again with new password
        PBEKeySpec spec = new PBEKeySpec(_password.toCharArray(), _salt, ITERATIONS, hash.length * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] testHash = factory.generateSecret(spec).getEncoded();

        //compare the hash txt
        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        //return true if the hash is the same
        return diff == 0;
    }
}
