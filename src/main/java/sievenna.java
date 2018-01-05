//import org.apache.commons.cli.DefaultParser;

import org.apache.commons.cli.*;

/**
 * The tool uses flags to determine which action to take on the input and output files,
 * which are passed as anonymous arguments in that order.
 */

public class sievenna {

    public static void main(String[] args) {
        // TODO: remove this when unneeded.
        // this code exists for testing purposes only
//        Integer testNum = 200;
//        byte testByte = testNum.byteValue();
//        System.out.println("Value: " + testNum);
//        System.out.println("Default: " + testByte);
//        System.out.println("toUnsigned: " + Byte.toUnsignedInt(testByte));
//        System.out.println(Integer.toBinaryString(testNum));
//        System.out.println("three in binary is " + Integer.toBinaryString(3));

        Options options = defineOptions();

        CommandLineParser parser = new DefaultParser();

        CommandLine line = null;

        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (line.hasOption('c') && line.hasOption('d')) {
            System.out.println("Pick one.");
        } else if (line.hasOption('c')) {
            HuffmanCoder coder = new HuffmanCoder();
            coder.encode(line.getArgList().get(0), line.getArgList().get(1));
        } else if (line.hasOption('d')) {
            System.out.println("This functionality is not currently supported.");
        } else {
            System.out.println("You must give either the option " +
                    "-c to compress or -d to decompress");
        }
    }

    private static Options defineOptions() {

        Options options = new Options();

        options.addOption("d", false, "Decompress the input file into the output path.");
        options.addOption("c", false, "Compress the input file into the output path.");

        return options;
    }
}

