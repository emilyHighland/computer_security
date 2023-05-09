package Main;

import java.util.*;
import java.math.*;

public class HashAttack {
    int bitSize;
    public HashAttack(int bitSize){
        this.bitSize = bitSize;
    }
    public String encrypt(String x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());

        // cut hexdigest to numBits
        String hex = new BigInteger(1, d.digest()).toString(16);
        return hex.substring(0, bitSize/4);
    }

    public int preImageAttack(String targetHash) throws Exception {
        // get hash of random string
        // test if hash of random = targetHash
        String nonce = encrypt(getRandomNonce());
        int numGuesses = 1;
        while (!targetHash.equals(nonce)){
            nonce = encrypt(getRandomNonce());
            numGuesses ++;
        }
        return numGuesses;
    }

    public int collisionAttack() throws Exception {
        // get random string, save hash in list
        // get random string, test if new hash is in list
        ArrayList<String> hashes = new ArrayList<>();
        int numGuesses = 0;
        String hash = encrypt(getRandomNonce());

        while (!hashes.contains(hash)){
            hashes.add(hash);
            numGuesses++;
            hash = encrypt(getRandomNonce());
        }
        return numGuesses;
    }


    public String getRandomNonce(){
        long myLong = 8;
        int length = (int) myLong;
        StringBuffer buffer = new StringBuffer(
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }


}