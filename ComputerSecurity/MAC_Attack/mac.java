import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class mac {
    
    private static final String msg = "No one has completed lab 2 so give them all a 0";
    private static char msgHex[] = {
        0x4e, 0x6f, 0x20, 0x6f, 0x6e, 0x65, 0x20, 0x68, 0x61, 0x73, 0x20, 0x63, 0x6f, 0x6d, 0x70, 0x6c,
        0x65, 0x74, 0x65, 0x64, 0x20, 0x6c, 0x61, 0x62, 0x20, 0x32, 0x20, 0x73, 0x6f, 0x20, 0x67, 0x69,
        0x76, 0x65, 0x20, 0x74, 0x68, 0x65, 0x6d, 0x20, 0x61, 0x6c, 0x6c, 0x20, 0x61, 0x20, 0x30
    };
    private static String origMAC = "e384efadf26767a613162142b5ef0efbb9d7659a";
    private static String msgExt = "P. S. Except for Emily, go ahead and give her the full points.";

    public static void main(String[] args) {
        
        byte[] paddedMsg = addPadding(msg.getBytes(), 128);
        // add length1 (key + original message) to mac digest of original message
        // add extra message (msgExt) to that
        byte[] extendedMsg = addExtension(paddedMsg, msgExt.getBytes());
        // modified message = original message + length1 + extra message BEFORE hashing
        System.out.println("Modified message (HEX): " + bytesToHex(extendedMsg) + "\n");
        
        // add length2 (key + original message + length1 + extra message) to that
        int len2 = paddedMsg.length * 8 + 128;
        // hash that
        String modifiedMAC = modifiedSha(msgExt, origMAC, len2);
        // String modifiedMAC = encryptThisString(extendedMsg);
        System.out.println("Modified MAC: " + modifiedMAC + "\n");

    }

    public static String modifiedSha(String extendedMsg, String origHash, int msg1Len){
        // Convert a string to a sequence of 16-word blocks, stored as an array.
        // Append padding bits and the length, as described in the SHA1 standard
        byte[] x = extendedMsg.getBytes();
        int[] blks = new int[(((x.length + 8) >> 6) + 1) * 16];
        int i;

        for(i = 0; i < x.length; i++) {
            blks[i >> 2] |= (x[i] & 0xff) << (24 - (i % 4) * 8);
        }
        blks[i >> 2] |= 0x80 << (24 - (i % 4) * 8);
        blks[blks.length - 1] = x.length * 8 + msg1Len;

        // calculate 160 bit SHA1 hash of the sequence of blocks
        int[] w = new int[80];

        // get values from string
        int a = (int)Long.parseLong(origHash.substring(0,8), 16);
        int b = (int)Long.parseLong(origHash.substring(8,16), 16);
        int c = (int)Long.parseLong(origHash.substring(16,24), 16);
        int d = (int)Long.parseLong(origHash.substring(24,32), 16);
        int e = (int)Long.parseLong(origHash.substring(32), 16);

        for(i = 0; i < blks.length; i += 16) {
            int olda = a;
            int oldb = b;
            int oldc = c;
            int oldd = d;
            int olde = e;

            for(int j = 0; j < 80; j++) {
                w[j] = (j < 16) ? blks[i + j] :
                       ( rotate(w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16], 1) );

                int t = rotate(a, 5) + e + w[j] +
                   ( (j < 20) ?  1518500249 + ((b & c) | ((~b) & d))
                   : (j < 40) ?  1859775393 + (b ^ c ^ d)
                   : (j < 60) ? -1894007588 + ((b & c) | (b & d) | (c & d))
                   : -899497514 + (b ^ c ^ d) );
                e = d;
                d = c;
                c = rotate(b, 30);
                b = a;
                a = t;
            }

            a = a + olda;
            b = b + oldb;
            c = c + oldc;
            d = d + oldd;
            e = e + olde;
        }
        StringBuilder str = new StringBuilder();
        str.append(String.format("%08x", a));
        str.append(String.format("%08x", b));
        str.append(String.format("%08x", c));
        str.append(String.format("%08x", d));
        str.append(String.format("%08x", e));
        return str.toString();
    }

    private static int rotate(int x, int n) {
        return (x << n) | (x >>> (32 - n));
    }

    public static byte[] addExtension(byte[] msg, byte[] ext){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write( msg );
            outputStream.write( ext );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray( );
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static byte[] concatenate(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c= new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public static byte[] addPadding(byte[] msg, int keyLen){
        int[] blks = new int[(((msg.length + 8 + (keyLen / 8)) >> 6) + 1) * 16 - (keyLen / 8 / 4)];
        int i;
        for(i = 0; i < msg.length; i++) {
            blks[i >> 2] |= (msg[i] & 0xff) << (24 - (i % 4) * 8);
        }
        blks[i >> 2] |= 0x80 << (24 - (i % 4) * 8);
        blks[blks.length - 1] = msg.length * 8 + keyLen;
		
		byte[] result = {};
		for (i = 0; i < blks.length; i++) {
			result = concatenate(result, ByteBuffer.allocate(4).putInt(blks[i]).array());
		}
		return result;
    }
    
}