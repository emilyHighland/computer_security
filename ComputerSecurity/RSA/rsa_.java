import java.math.BigInteger;
import java.security.SecureRandom;

public class rsa_ {
    static final BigInteger e = new BigInteger("65537"); 
	public static void main(String[] args) {
		// 1. Find large primes p and q
        // 2. verify that phi is relatively prime to e -> if not, pick new p and q 
        BigInteger p, q, phi;
        do {
            p = getLargePrime();
            q = getLargePrime();
            phi = getPhi(p, q);
        } while (!(gcd(e, phi)).equals(BigInteger.ONE));
        
		// 3. Compute n from p and q
		BigInteger n = p.multiply(q);

		// 4. Calculate d where  d ≡ e^(-1) (mod Phi(n))
		BigInteger d = extendedEuclid(e, phi)[1];
        if (d.compareTo(BigInteger.ZERO) == -1) {
            d = d.add(phi);
        }

        // Print generated values
		System.out.println("p: " + p);
		System.out.println("q: " + q);
        System.out.println("n: " + n);
		System.out.println("d: " + d);


        // *** AUTO_GRADER values for encryption and decryption ***
        // BigInteger p = new BigInteger("11697225679264198604249403196136653840591264456982846728536205485147315193097127803914971375064865612795155645075268137138331021105694085573896424699653487");
        // BigInteger q = new BigInteger("10410825388241454273694947397720216033628325757177861749346710160370482188680262474784322083277841053233196687516622995606594209036304464091036164130688203");
		// BigInteger n = p.multiply(q);
        // BigInteger d = new BigInteger("101174573455261216456852806912329874633440403963190708857485616510930932571152554806117588488338174820059286758402292941927274820129527974805967328388681475151873521963463303951904234844905236733365092825388306479924195841793733613180831521311789389469734525216489544666708005148502740085624865079191261314317");
        // BigInteger cipherTestMsg = new BigInteger("85201570680568390133452659961662548285211328370288307062291456404606343878005264978274304932657012223980332838618070077313446477535739411013835173430772790558830845080911600666990628936402099397811454758317405450657398570735269516180329584954393182766566260221698596486648017874480212811213383133303151694563");
        // BigInteger encryptedTestMsg = new BigInteger("42305175087573746792527848428759283853839099034782809402274255426294035564433595078226891518040205811596706954672984331221634221929051528323652918520158111860300315372972554970232458346023258348798044622594181172708773103535222476578529132175216771283742269138825294276821657489642993474060822459137980435686");
        
		// encryption / decryption example
		String message = "Test example";
		// Convert string to numbers using a cipher
        BigInteger cipherMessage = new BigInteger(message.getBytes());
		// Encrypt the ciphered message
		BigInteger encrypted = encrypt(cipherMessage, e, n);
		// Decrypt the encrypted message
		BigInteger decrypted = decrypt(encrypted, d, n);
		// Uncipher the decrypted message to text
        String restoredMessage = new String(decrypted.toByteArray());

		System.out.println("Original message: " + message);
		System.out.println("Ciphered: " + cipherMessage);
		System.out.println("Encrypted: " + encrypted);
		System.out.println("Decrypted: " + decrypted);
		System.out.println("Restored: " + restoredMessage);
	}

	// Generates a random large prime number of specified bitlength
	public static BigInteger getLargePrime() {
        int bitLength = 512;
		SecureRandom rnd = new SecureRandom();
		BigInteger largePrime = BigInteger.probablePrime(bitLength, rnd);
		return largePrime;
	}

    // Phi(n) = (p-1)(q-1)
	public static BigInteger getPhi(BigInteger p, BigInteger q) {
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		return phi;
	}

	// Recursive implementation of Euclidian algorithm to find greatest common denominator
	public static BigInteger gcd(BigInteger a, BigInteger b) {
		if (b.equals(BigInteger.ZERO)) {
			return a;
		} else {
			return gcd(b, a.mod(b));
		}
	}

	// Recursive EXTENDED Euclidean algorithm, solves (ax + by = gcd(a,b))
	// finds the multiplicative inverse
	// returns [d, p, q] where d = gcd(a,b) and ap + bq = d
	public static BigInteger[] extendedEuclid(BigInteger a, BigInteger b) {
		if (b.equals(BigInteger.ZERO)) return new BigInteger[] {
			a, BigInteger.ONE, BigInteger.ZERO
		}; // { a, 1, 0 }
		BigInteger[] vals = extendedEuclid(b, a.mod(b));
		BigInteger d = vals[0];
		BigInteger p = vals[2];
		BigInteger q = vals[1].subtract(a.divide(b).multiply(vals[2]));
		return new BigInteger[] {
			d, p, q
		};
	}

    private static BigInteger modExp(BigInteger base, BigInteger exponent, BigInteger modulo) {
        BigInteger result = BigInteger.ONE;
        while (exponent.compareTo(BigInteger.ZERO) == 1){ // while exponent > 0
            if ((exponent.and(BigInteger.ONE).compareTo(BigInteger.ZERO)) != 0){ // if exp > 0
                BigInteger temp1 = result.mod(modulo);
                BigInteger temp2 = base.mod(modulo);
                result = (temp1.multiply(temp2)).mod(modulo);
            }
            BigInteger temp3 = base.mod(modulo);
            base = (temp3.multiply(temp3)).mod(modulo);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

    // Encryption: c = m^e % n   ||   m^e = c % n
	public static BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
		return modExp(message, e, n);
	}

    // Decryption: m = c^d % n   ||   c^d = m % n
	public static BigInteger decrypt(BigInteger message, BigInteger d, BigInteger n) {
		return modExp(message, d, n);
	}
}
