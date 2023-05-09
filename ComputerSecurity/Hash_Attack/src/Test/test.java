package Test;
import Main.HashAttack;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class test {
    HashAttack hashAttack;
    static int[] bitsizes = new int[]{8,12,16,20};


    @Test
    void TEST_preImage() throws Exception {
        FileWriter writer = new FileWriter("outputs.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        for (int size: bitsizes) {
            hashAttack = new HashAttack(size);
            bufferedWriter.write("PRE_IMAGE BitSize: " + size);
            bufferedWriter.newLine();

            for (int i = 0; i < 50; i++) {
                String nonce = hashAttack.getRandomNonce();
                String targetHash = hashAttack.encrypt(nonce);
                int numGuesses = hashAttack.preImageAttack(targetHash);
                bufferedWriter.write( " " + numGuesses);
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.close();
    }

    @Test
    void TEST_collision() throws Exception {
        FileWriter writer2 = new FileWriter("outputs.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer2);

        for (int size: bitsizes) {
            hashAttack = new HashAttack(size);
            bufferedWriter.write("COLLISION BitSize: " + size);
            bufferedWriter.newLine();

            for (int i = 0; i < 50; i++) {
                int numGuesses = hashAttack.collisionAttack();
                bufferedWriter.write(" " + numGuesses);
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.close();
    }

}
