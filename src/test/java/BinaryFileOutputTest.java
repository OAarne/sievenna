import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class BinaryFileOutputTest {

    @Test
    public void writeBit() throws IOException {
        File testFile = new File("out/test/testOutput.ppm");
        testFile.createNewFile();
        BinaryFileOutput out = new BinaryFileOutput(new FileOutputStream(testFile));
    }

    @Test
    public void writeBinaryString() {
    }

    @Test
    public void write8bitInt() {
    }
}