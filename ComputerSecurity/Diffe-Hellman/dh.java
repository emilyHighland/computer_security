import java.math.BigInteger;
import java.security.SecureRandom;

class dh{

    static BigInteger P, a, b, x, y, secretKeyA, secretKeyB;
    static long G = 5;
    
    public static void main(String[] args) {
        // generateLargePrime();
        
        a = new BigInteger("9596082393400792651613797890252903330331720862894926454383856979590677673976773303438519169562742238039845573541166915781987889276079078767623496382120061");
        P = new BigInteger("11031600707493035015446072869117509686195186520207900177213777066592227918446423640328047505608914504631256792628710970601328033252354021954551926186401113");
        System.out.println("P: " + P.intValue());

        // Alice sends G^a % P to Bob
        x = modExp(BigInteger.valueOf(G), a, P);
        System.out.println("x: " + x.intValue());

        // Bob picks a secure rand private number b
        b = new BigInteger("1287517725278773503241876322205798263967433460366043048576376313928112629035489594278378127083234094345742119066133700502271631917442192756832031995756");
        
        // sends G^b % P to Alice
        y = new BigInteger("1291991459");

        // Alice takes (y)^a % P || (g^b % P)^a % P  == password
        // Bob takes (x)^b % P || (g^a % P)^b % P  == password
        secretKeyA = modExp(y, a, P);
        
        // Both parties have the same password
        System.out.println("SecretKey: " + secretKeyA.intValue());
    }

    private static BigInteger modExp(BigInteger base, BigInteger exponent, BigInteger modulo) {

        BigInteger result = BigInteger.ONE;
        base = base.mod(modulo);

        while (exponent.intValue() > 0){ // while exponent > 0
            if ((exponent.and(BigInteger.ONE).compareTo(BigInteger.ZERO)) == 1){ // if exp > 0
                result = result.multiply(base); 
            }
            base = base.multiply(base); 
            exponent = exponent.shiftRight(1); // divide by 2
            base = base.mod(modulo);
        }
        return result;
    }

    private void generateLargePrime(){
        // Alice picks a generator G, a prime P, and a secure rand private number a
        // P must be Prime and (P-1)/2 should also be prime
        // int bitLength = 512;
        // SecureRandom rnd = new SecureRandom();
        // BigInteger a = BigInteger.probablePrime(bitLength, rnd);
        // BigInteger P = BigInteger.probablePrime(bitLength, rnd);
        // while (P.longValue() < G){
        //     P = BigInteger.probablePrime(bitLength, rnd);
        // }
        // while (a.signum() == -1){
        //     a = BigInteger.probablePrime(bitLength, rnd);
        // }
        // System.out.println("P: " + P);
        // System.out.println("a: " + a);
    }

}