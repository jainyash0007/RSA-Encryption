import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class RSADecrypt {
    public static void main(String[] args) {
        String encryptedMessage = "";
        try {
            Path filename = Path.of(args[0]);
            encryptedMessage = Files.readString(filename);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Read private key text
        BigInteger D = BigInteger.ZERO;
        BigInteger N = BigInteger.ZERO;
        try {
            File myObj = new File(args[1]);
            Scanner myReader = new Scanner(myObj);

            String pub_d = myReader.nextLine();
            String pub_n = myReader.nextLine();

            D = new BigInteger(pub_d.split(" ")[2]);
            N = new BigInteger(pub_n.split(" ")[2]);

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // split cipher into blocks
        String[] encryptedBlocks = encryptedMessage.split(" ");

        StringBuilder plaintextMessage = new StringBuilder();
        for (String block : encryptedBlocks) {
            // Decrypt block
            BigInteger plainBlockText = new BigInteger(block).modPow(D, N);

            // Pad the block with zeros if necessary
            String plaintextBlkStr = plainBlockText.toString();
            if (plaintextBlkStr.length() < 6) {
                int diff = 6 - plaintextBlkStr.length();
                plaintextBlkStr = "0".repeat(Math.max(0, diff)) + plaintextBlkStr;
            }

            // Encode message block
            StringBuilder decodedBlk = new StringBuilder();
            while (plaintextBlkStr.length() > 0) {
                int val;
                if (plaintextBlkStr.length() > 2) {
                    val = Integer.parseInt(plaintextBlkStr.substring(0, 2));
                    plaintextBlkStr = plaintextBlkStr.substring(2);
                } else {
                    val = Integer.parseInt(plaintextBlkStr);
                    plaintextBlkStr = "";
                }

                // Convert number to character
                char c;
                if (val == 26) {
                    c = ' ';
                } else if (val >= 27) {
                    c = (char)(val + 'A' - 27);
                } else {
                    c = (char)(val + 'a');
                }

                decodedBlk.append(c);
            }

            plaintextMessage.append(decodedBlk);
        }


        // Write plaintext message to file
        try {
            FileWriter fileWriter = new FileWriter("test.dec");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(plaintextMessage);
            printWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Successfully decrypted message to file: test.dec");
    }
}
