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

    public void writeBit(boolean bit) {
        byteBuffer <<= 1;
        if (bit) {
            byteBuffer++;
//            byteBuffer |= 0x000000FF;
        }
        remainingBits--;

        //
        if (remainingBits == 0) {
            assert byteBuffer >= 0 && byteBuffer <= 255;
            try {
                outputStream.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteBuffer = 0;
            remainingBits = 8;
        }
        totalBits++;
    }

    /**
     * Writes a
     * @param code
     */

    public void writeBinaryString(String code) {
        for (int i = 0; i < code.length(); i++) {
            writeBit(code.charAt(i) == '1');
        }
    }

    public void write8bitInt(int key) {
        // construct a string describing the key as an unsigned 8-bit integer.
        String binaryString = Integer.toBinaryString(key);
        assert binaryString.length() <= 8;
        int missingZeroes = 8-binaryString.length();
        if (missingZeroes > 0) {
            char[] zeroes = new char[missingZeroes];
            Arrays.fill(zeroes, '0');
            binaryString = zeroes + binaryString;
        }
        assert binaryString.length() == 8;

        for (int i = 0; i < binaryString.length(); i++) {
            writeBit(binaryString.charAt(i) == '0');
        }
    }

    public void flush() throws IOException {
        outputStream.flush();
    }

    public String report() {
        return Integer.toString(totalBits);
    }
}
