import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
public class CalgaryTest {

    @Ignore
    @Test
    public void calgaryTest() throws IOException {

        long before = System.nanoTime();
        HuffmanCoder.encode("out/test/resources/calgary.tar", "src/test/resources/calgary.sie");
        long after = System.nanoTime();
        System.out.println("Compressed 22.128 MB file in " + (after - before)/1000000000.0 + " s");
        long before2 = System.nanoTime();
        HuffmanCoder.decode("src/test/resources/calgary.sie", "src/test/resources/output.tar");
        long after2 = System.nanoTime();
        System.out.println("Decompressed file in " + (after2 - before2)/1000000000.0 + " s");

        System.out.println("Round trip took " + (after2 - before)/1000000000.0 + " s");
        Assert.assertTrue((after2 - before)/1000000000.0 < 3);

        FileInputStream expected = new FileInputStream(new File("src/test/resources/calgary.tar"));
        FileInputStream actual = new FileInputStream(new File("src/test/resources/output.tar"));

        int expectedByte = expected.read();
        int actualByte = actual.read();

        while (expectedByte != -1) {
            Assert.assertEquals(expectedByte, actualByte);
            expectedByte = expected.read();
            actualByte = actual.read();
        }
    }
}
