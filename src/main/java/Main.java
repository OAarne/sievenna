
import org.apache.commons.cli.*;

import java.util.logging.*;

/**
 * The tool uses flags to determine which action to take on the input and output files,
 * which are passed as anonymous arguments in that order.
 */

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String fileSuffix = "sie";

    public static void main(String[] args) {

        Options options = defineOptions();

        CommandLineParser parser = new DefaultParser();

        CommandLine line = null;

        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (line.hasOption('l')) {
            LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.FINEST);
            Handler handler = new ConsoleHandler();
            LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).addHandler(handler);
        } else {
            LogManager.getLogManager().reset();
        }

        if (line.getArgList().size() == 0) {
            System.out.println("Give input and output paths after flags.");
            return;
        }
        if (line.hasOption('c') && line.hasOption('d')) {
            System.out.println("Pick one.");
        } else if (line.hasOption('c')) {
            if (line.getArgList().size() == 2) {
                HuffmanCoder.encode(line.getArgList().get(0), line.getArgList().get(1));
            } else {
                HuffmanCoder.encode(line.getArgList().get(0), line.getArgList().get(0) + fileSuffix);
            }
        } else if (line.hasOption('d')) {
            if (line.getArgList().size() < 2) {
                System.out.println("Please specify output path after input path.");
                return;
            }
            HuffmanCoder.decode(line.getArgList().get(0), line.getArgList().get(1));
        } else {
            System.out.println("You must give either the option " +
                    "-c to compress or -d to decompress");
        }
    }

    private static Options defineOptions() {

        Options options = new Options();

        options.addOption("l", false, "Log progress to console");
        options.addOption("d", false, "Decompress the input file into the output path.");
        options.addOption("c", false, "Compress the input file into the output path.");

        return options;
    }
}

