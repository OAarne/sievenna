import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * The class provides ways to writeBit information to a binary file bit by bit.
 */

public class BinaryFileOutput {
    private BufferedOutputStream outputStream;
    private int byteBuffer, remainingBits, totalBits;


    public BinaryFileOutput(FileOutputStream fileOutputStream) {
        this.byteBuffer = 0;
        this.remainingBits = 8;
        this.totalBits = 0;
        this.outputStream = new BufferedOutputStream(fileOutputStream);
    }

    /**
     * Writes single bit to output stream.
     * @param bit
     */

    public void writeBit(boolean bit) throws IOException {
        byteBuffer <<= 1;
        if (bit) {
            byteBuffer++;
        }
        remainingBits--;

        //
        if (remainingBits == 0) {
            assert byteBuffer >= 0 && byteBuffer <= 255;
            outputStream.write(byteBuffer);
            byteBuffer = 0;
            remainingBits = 8;
        }
        totalBits++;
    }

    /**
     * Writes a String of ones and zeroes to the output.
     * @param code
     */

    public void writeBinaryString(String code) throws IOException {
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) != '0' && code.charAt(i) != '1') throw new IllegalArgumentException();
            writeBit(code.charAt(i) == '1');
        }
    }

    /**
     * Writes the unsigned 8-bit representation of the given integer to the output.
     * @param x
     */

    public void write8bitInt(int x) throws IOException {
        if (x < 0 || x > 255) throw new IllegalArgumentException();

        for (int i = 7; i >= 0; i--) {
            writeBit((x & (1 << i)) != 0);
        }
    }

    /**
     * Writes a positive signed 32-bit integer.
     */

    public void write32bitInt(int x) {
        String binaryString = Integer.toBinaryString(x);
        assert binaryString.length() <= 32;
        int missingZeroes = 32-binaryString.length();
        if (missingZeroes > 0) {
            char[] zeroes = new char[missingZeroes];
            Arrays.fill(zeroes, '0');
            binaryString = new String(zeroes) + binaryString;
        }
        assert binaryString.length() == 32;

        binaryString.chars().forEach(y -> {
            try {
                writeBit(y == '1');
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void close() throws IOException {
        while (remainingBits != 8) {
            writeBit(false);
        }
        write32bitInt(0);
        outputStream.close();
    }

    /**
     * @return Number of bits written so far by this BinaryFileOutput
     */
    public int report() {
        return totalBits;
    }
}
