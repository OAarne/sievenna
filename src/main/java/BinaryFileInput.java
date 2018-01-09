import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BinaryFileInput {
    private FileInputStream inputStream;
    int index, total;
    String byteBuffer;

    /**
     * Initializes an instance using a FileInputStream.
     * @param inputStream
     * @throws IOException
     */

    public BinaryFileInput(FileInputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.index = 0;
        this.byteBuffer = toBinaryString(inputStream.read());
        this.total = 0;
    }

    /**
     * Reads the next bit from the file
     * @return
     * @throws IOException
     */

    public boolean readBit() throws IOException {
        boolean bit = ('1' == byteBuffer.charAt(index));
        index++;
        if (index == 8) {
            byteBuffer = toBinaryString(inputStream.read());
            index = 0;
        }
        total++;
        return bit;
    }

    /**
     * Reads an 8-bit unsigned integer
     * @return
     * @throws IOException
     */

    public int read8bitInt() throws IOException {
        int x = 0;
        if (readBit()) x++;
        for (int i = 0; i < 7; i++) {
            x <<= 1;
            if (readBit()) x++;
        }
        if (x < 0 || x > 255) throw new IllegalStateException();
        return x;
    }

    /**
     * Reads a 32-bit positive signed integer.
     * @return
     * @throws IOException
     */

    public int read32bitInt() throws IOException {
        int x = 0;
        if (readBit()) x++;
        for (int i = 0; i < 31; i++) {
            x <<= 1;
            if (readBit()) x++;
        }
        return x;
    }

    /**
     * @param x
     * @return 8-bit unsigned representation of the given integer as a String
     */

    public String toBinaryString(int x) {
        if (x == -1) System.out.println("The stream you were reading is out of data at "+ report()/8 + " Bytes");
        if (x < 0 || x > 255) throw new IllegalArgumentException();
        String binaryString = Integer.toBinaryString(x);
        assert binaryString.length() <= 8;
        int missingZeroes = 8-binaryString.length();
        if (missingZeroes > 0) {
            char[] zeroes = new char[missingZeroes];
            Arrays.fill(zeroes, '0');
            binaryString = new String(zeroes) + binaryString;
        }
        assert binaryString.length() == 8;

        return binaryString;
    }

    /**
     * @return total bits read so far
     */

    public int report() {
        return total;
    }

    public int available() throws IOException {
        return inputStream.available();
    }
}
