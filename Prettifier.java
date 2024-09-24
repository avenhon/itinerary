import java.util.List;

public class Prettifier {
    public static void main(String[] args) {
        boolean shouldOutputted = false;
        String inputPath;
        String outputPath;
        String airportLookup;

        if (args.length == 4 && args[0].equals("-o")) {
            shouldOutputted = true;
            inputPath = args[1];
            outputPath = args[2];
            airportLookup = args[3];
        } else if (args.length != 3 || args[0].equals("-h")) {
            System.out.println("\u001B[33mitinerary usage:\u001B[0m");
            System.out.println("\u001B[32m$ java Prettifier.java ./input.txt ./output.txt ./airport-lookup.csv\u001B[0m");
            return;
        } else {
            inputPath = args[0];
            outputPath = args[1];
            airportLookup = args[2];
        }

        FileService fileService = new FileService();

        if (fileService.checkIfFileNotExist(inputPath)) {
            System.out.println("\u001B[41mInput not found!\u001B[0m");
            return;
        }

        if (fileService.checkIfFileNotExist(airportLookup)) {
            System.out.println("\u001B[41mAirport lookup not found\u001B[0m");
            return;
        }

        try {
            fileService.getIndexesFromCSV(airportLookup);
        } catch (Exception e) {
            System.out.println("\u001B[41mAirport lookup malformed\u001B[0m");
            return;
        }

        if (shouldOutputted) {
            System.out.println(fileService.formatInput(inputPath, outputPath, airportLookup));
        } else {
            fileService.formatInput(inputPath, outputPath, airportLookup);
        }
    }
}

