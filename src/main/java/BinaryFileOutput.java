import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The class provides ways to write information to a binary file bit by bit.
 */

public class BinaryFileOutput {
    private BufferedOutputStream outputStream;
    private int byteBuffer, remainingBits;

    public BinaryFileOutput(FileOutputStream fileOutputStream) {
        this.byteBuffer = 0;
        this.remainingBits = 8;
        this.outputStream = new BufferedOutputStream(fileOutputStream);
    }

    /**
     * Writes single bit to output stream.
     * @param bit
     */

    public void write(boolean bit) {
        byteBuffer <<= 1;
        if (bit) {
            byteBuffer++;
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
    }

    /**
     * Writes a
     * @param code
     */

    public void write(String code) {
        for (int i = 0; i < code.length(); i++) {
            write(code.charAt(i) == '1');
        }
    }

    public void writeByte(String inputCode) {
        if (remainingBits != 8) throw new IllegalStateException(
                "Writing a new byte with bits remaining is not currently supported");
        try {
            this.write(inputCode);
            outputStream.write(byteBuffer);
            byteBuffer = 0;
            remainingBits = 8;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
