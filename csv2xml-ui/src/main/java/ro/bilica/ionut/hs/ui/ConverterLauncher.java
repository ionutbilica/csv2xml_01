package ro.bilica.ionut.hs.ui;

import org.apache.commons.cli.*;
import ro.bilica.ionut.hs.core.CsvToXmlConverter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class ConverterLauncher {
    public static void main( String[] args ) throws IOException, JAXBException {
        try {
            CommandLine cmd = parseArguments(args);

            File csvFile = new File(cmd.getArgList().get(0));
            File xmlFile = getXmlFile(cmd, csvFile);

            CsvToXmlConverter converter = new CsvToXmlConverter();
            converter.convert(csvFile, xmlFile);
        } catch (ParseException e) {
            //Already handled.
        } catch (IOException | JAXBException e) {
            System.out.println("Error! " + e.getMessage());
        }
    }

    private static CommandLine parseArguments(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "xml output file name");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse( options, args);
            if (cmd.getArgs().length != 1) {
                throw new ParseException("Invalid number of arguments!");
            }
            return cmd;
        } catch (ParseException e) {
            printUsage(options);
            throw e;
        }
    }

    private static File getXmlFile(CommandLine cmd, File csvFile) {
        String xmlFileName = csvFile.getName().replace(".csv", ".xml");
        if (cmd.hasOption("o")) {
            xmlFileName = cmd.getOptionValue("o");
        }
        return new File(csvFile.getParentFile(), xmlFileName);
    }

    private static void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("app INPUT_CSV_FILE [OPTION]...\nConverts given library CSV file to XML format.\n ", options);
    }
}
