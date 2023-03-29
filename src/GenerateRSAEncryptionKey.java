import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class GenerateRSAEncryptionKey {
    public static void main(String[] args) throws IOException {

        //choose a fermat number as e
        BigInteger e = BigInteger.valueOf(17);

        // generate p and q on the basis that ggt(phi(n),e) = 1
        BigInteger p, q;
        do {
            p = BigInteger.probablePrime(1024, new SecureRandom());
            q = BigInteger.probablePrime(1024, new SecureRandom());
        } while (!isValidE(e, p, q));

        //calculate n = p*q
        BigInteger n = p.multiply(q);

        //calculate phi(n) = (p-1)*(q-1)
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        //calculate d with e*d mod phi(n), with extended Euclidean algorithm
        BigInteger d = ggT(phiN, e);

        //create publicKey File
        String publicKey = publicToString(n, e);
        String publicFile = System.getProperty("user.home")+"\\Downloads\\pk.txt";

        try{
            FileWriter fw = new FileWriter(publicFile);
            fw.write(publicKey);
            fw.close();
            System.out.println("File created in: " + publicFile);
        } catch (IOException x){
            x.printStackTrace();
        }

        //create privateKey File
        String privateKey = privateToString(n, d);
        String privateFile = System.getProperty("user.home")+"\\Downloads\\sk.txt";

        try{
            FileWriter fw = new FileWriter(privateFile);
            fw.write(privateKey);
            fw.close();
            System.out.println("File created in: " + privateFile);
        } catch (IOException x){
            x.printStackTrace();
        }
    }

    //check if e is valid for p and q
    private static boolean isValidE(BigInteger e, BigInteger p, BigInteger q) {
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        if (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phiN) >= 0) {
            return false;
        }
        return phiN.gcd(e).equals(BigInteger.ONE);
    }

    private static BigInteger ggT(BigInteger phiN, BigInteger e) {
        BigInteger a, b, x0, y0, x1, y1, q, r, x00, y00;
        //Initialise
        a = phiN;
        b = e;
        x0 = BigInteger.ONE;
        y0 = BigInteger.ZERO;
        x1 = BigInteger.ZERO;
        y1 = BigInteger.ONE;

        do {
            q = a.divide(b);
            r = a.mod(b);
            a = b;
            b = r;
            x00 = x0;
            y00 = y0;
            x0 = x1;
            y0 = y1;
            x1 = x00.subtract((q.multiply(x1)));
            y1 = y00.subtract((q.multiply(y1)));
        } while (!(b.equals(BigInteger.ZERO)));
        return y0;
    }

    public static String privateToString(BigInteger n, BigInteger d) {
        return ("(") + n + "," + d + ")";
    }

    public static String publicToString(BigInteger n, BigInteger e) {
        return ("(") + n + "," + e + ")";
    }
}
