import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.Scanner;

public class EncryptFile {
    public static void main(String[] args) {
        String home = System.getProperty("user.home");
        Path pkPath = Paths.get(home, "Downloads", "pk.txt");
        Path textPath = Paths.get(home, "Downloads", "text.txt");
        Path outputPath = Paths.get(home, "Downloads", "chiffre.txt");

        // Read encryption key from pk.txt in downloads folder
        BigInteger n = null;
        BigInteger e = null;
        try (Scanner scanner = new Scanner(pkPath)) {
            String[] values = scanner.nextLine().replaceAll("[() ]", "").split(",");
            n = new BigInteger(values[0]);
            e = new BigInteger(values[1]);
        } catch (IOException ex) {
            System.exit(1);
        }

        // Read text from text.txt in downloads folder
        StringBuilder text = new StringBuilder();
        try (Scanner scanner = new Scanner(textPath)) {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
            }
        } catch (IOException ex) {
            System.exit(1);
        }

        // Encrypt text with encryption key
        StringBuilder ciphertext = new StringBuilder();
        for (char c : text.toString().toCharArray()) {
            BigInteger m = BigInteger.valueOf((int) c);
            BigInteger cip = fastExponentiation(m, e, n);
            ciphertext.append(cip).append(",");
        }

        // Write chiffre to output file
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write(ciphertext.toString());
        } catch (IOException ex) {
            System.exit(1);
        }
    }

    //module to user fast exponentiation
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