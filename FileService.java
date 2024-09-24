import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileService {
    public boolean checkIfFileNotExist(String path) {
        File f = new File(path);

        return !f.exists() || f.isDirectory();
    }

    public String readFile(String path) {
        try {
            FileReader reader = new FileReader(path);
            int data = reader.read();
            String fileContent = "";

            while (data != -1) {
                fileContent = fileContent.concat(String.valueOf((char) data));
                data = reader.read();
            }

            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String formatDataAndTime(String text, String regex, String format) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);

        while (m.find()) {
            String date = m.group().substring(m.group().indexOf("(") + 1, m.group().length() - 1);
            ZonedDateTime dateTime = ZonedDateTime.parse(date);
            String formattedDate = dateTime.format(dateFormatter);

            text = text.replace(m.group(), formattedDate);
        }

        return text;
    }

    public List<String> findAllCodes(String input, String type) {
        List<String> codes = new ArrayList<String>();

        Pattern p = Pattern.compile(type);
        Matcher m = p.matcher(input);

        while(m.find()) {
            codes.add(m.group());
        }

        return codes;
    }

    public Map<String, Integer> getIndexesFromCSV(String airportLookup) {
        Map<String, Integer> indexes = new HashMap<String, Integer>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(airportLookup));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                for (int i = 0; i < values.length; i++) {
                    if (values[i].isEmpty()) {
                        throw new Exception("Airport lookup malformed");
                    }

                    switch (values[i]) {
                        case "name" -> indexes.put("name", i);
                        case "iso_country" -> indexes.put("iso_country", i);
                        case "municipality" -> indexes.put("municipality", i);
                        case "icao_code" -> indexes.put("icao_code", i);
                        case "iata_code" -> indexes.put("iata_code", i);
                        case "coordinates" -> indexes.put("coordinates", i);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return indexes;
    }

    public String getAirportName(String airportLookup, String input, List<String> codes, Map<String, Integer> indexesCSV, String typeCSVIndex) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(airportLookup));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                for (String code : codes) {
                    if (values[indexesCSV.get(typeCSVIndex)].equals(code.substring(code.lastIndexOf("#") + 1))) {
                        input = input.replace(code, values[indexesCSV.get("name")]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return input;
    }

    public String getCityName(String airportLookup, String input, List<String> codes, Map<String, Integer> indexesCSV, String typeCSVIndex) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(airportLookup));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                for (String code : codes) {
                    if (values[indexesCSV.get(typeCSVIndex)].equals(code.substring(code.lastIndexOf("#") + 1))) {
                        input = input.replace(code, values[indexesCSV.get("municipality")]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return input;
    }

    public String formatInput(String inputPath, String outputPath, String airportLookup) {
        String inputContent = readFile(inputPath);
        Map<String, Integer> indexes = getIndexesFromCSV(airportLookup);
        inputContent = formatDataAndTime(inputContent, "D\\((.*?)\\)", "dd MMMM yyyy");
        inputContent = formatDataAndTime(inputContent, "T12\\((.*?)\\)", "hh:mma (xxx)");
        inputContent = formatDataAndTime(inputContent, "T24\\((.*?)\\)", "kk:mm (xxx)");

        List<String> citiesICAO = findAllCodes(inputContent, "\\*##\\w+");
        if (!citiesICAO.isEmpty()) {
            inputContent = getCityName(airportLookup, inputContent, citiesICAO, indexes, "icao_code");
        }
        List<String> ICAO = findAllCodes(inputContent, "##\\w+");
        if (!ICAO.isEmpty()) {
            inputContent = getAirportName(airportLookup, inputContent, ICAO, indexes, "icao_code");
        }
        List<String> citiesIATA = findAllCodes(inputContent, "\\*#\\w+");
        if (!citiesIATA.isEmpty()) {
            inputContent = getCityName(airportLookup, inputContent, citiesIATA, indexes, "iata_code");
        }
        List<String> IATA = findAllCodes(inputContent, "#\\w+");
        if (!IATA.isEmpty()) {
            inputContent = getAirportName(airportLookup, inputContent, IATA, indexes, "iata_code");
        }

        writeOutput(inputContent, outputPath);
        return inputContent;
    }

    public void writeOutput(String inputContent, String outputPath) {
        try {
            BufferedReader input = new BufferedReader(new StringReader(inputContent));
            BufferedWriter output = new BufferedWriter(new FileWriter(outputPath));
            int newLineCount = 0;
            boolean lastCharWasNewline = false;

            int ch;
            while ((ch = input.read()) != -1) {
                char currentChar = (char) ch;

                if (currentChar == '\u000B' || currentChar == '\f' || currentChar == '\r') {
                    currentChar = '\n';
                }

                if (currentChar == '\n') {
                    if (lastCharWasNewline && newLineCount > 1) {
                        continue;
                    }
                    newLineCount++;
                    lastCharWasNewline = true;
                }

                if (!(currentChar == '\n')) {
                    newLineCount = 0;
                    lastCharWasNewline = false;
                }

                output.write(currentChar);
            }

            input.close();
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
