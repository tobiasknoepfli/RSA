import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DecryptChiffre {
    public static void main(String[] args) throws Exception {
        String home = System.getProperty("user.home");
        Path chiffrePath = Paths.get(home, "Downloads", "chiffre.txt");
        Path skPath = Paths.get(home, "Downloads", "sk.txt");
        Path outputPath = Paths.get(home, "Downloads", "text-d.txt");

        // Read decryption key from sk.txt in downloads folder
        BigInteger n = null;
        BigInteger d = null;
        try {
            String keyText = Files.readString(skPath);
            keyText = keyText.replace("(", "").replace(")", "");
            String[] splitString = keyText.split(",");
            n = new BigInteger(splitString[0].trim());
            d = new BigInteger(splitString[1].trim());
        } catch (IOException ex) {
            System.exit(1);
        }

        // Read text from chiffre.txt in downloads folder
        String ciphertext = null;
        try {
            ciphertext = Files.readString(chiffrePath);
        } catch (IOException ex) {
            System.exit(1);
        }

        // Decrypt text with decryption key
        StringBuilder plaintext = new StringBuilder();
        String[] ciphertextArray = ciphertext.split(",");
        for (String s : ciphertextArray) {
            BigInteger cip = new BigInteger(s.trim());
            BigInteger m = fastExponentiation(cip, d, n);
            char c = (char) m.intValue();
            plaintext.append(c);
        }

        // Write text to output file
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write(plaintext.toString());
        } catch (IOException ex) {
            System.exit(1);
        }
    }

    // Use fast exponentiation algorithm to compute modular exponentiation
    private static BigInteger fastExponentiation(BigInteger x, BigInteger exponent, BigInteger modulo) {
        BigInteger result = BigInteger.ONE;

        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.testBit(0)) {
                result = result.multiply(x).mod(modulo);
            }
            x = x.multiply(x).mod(modulo);
            exponent = exponent.shiftRight(1);
        }

        return result;
    }
}
