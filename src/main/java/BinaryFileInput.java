import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BinaryFileInput {
    private BufferedInputStream inputStream;
    int bitsRemaining, total;
    int byteBuffer;

    /**
     * Initializes an instance using a FileInputStream.
     * @param inputStream
     * @throws IOException
     */

    public BinaryFileInput(FileInputStream inputStream) throws IOException {
        this.inputStream = new BufferedInputStream(inputStream);
        this.bitsRemaining = 7;
        this.byteBuffer = inputStream.read();
        this.total = 0;
    }

    /**
     * Reads the next bit from the file
     * @throws IOException
     */

    public boolean readBit() throws IOException {
        int mask = (1 << bitsRemaining);
        boolean bit = ((byteBuffer & mask) != 0);
        bitsRemaining--;
        if (bitsRemaining < 0) {
            byteBuffer = inputStream.read();
            bitsRemaining = 7;
        }
        total++;
        return bit;
    }

    /**
     * Reads an 8-bit unsigned integer
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
     * @return total bits read so far
     */

    public int report() {
        return total;
    }

    public int available() throws IOException {
        return inputStream.available();
    }

    public void close() throws IOException {
        inputStream.close();
    }
}
