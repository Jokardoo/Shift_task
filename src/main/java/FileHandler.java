import exceptions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    private final InputParser parser;
    private final Map<DataType, BufferedWriter> writers;
    private final Map<DataType, FileStatistic> stats;


    private final Path pathToResults;

    public FileHandler(InputParser parser) {
        this.parser = parser;

        writers = new HashMap<>();
        stats = new HashMap<>();

        if (parser.getOutputPath() != null && !parser.getOutputPath().isEmpty())
            pathToResults = Path.of(Config.PATH_TO_RESULT_FILES + "\\" + parser.getOutputPath() + "\\");

        else
            pathToResults = Path.of(Config.PATH_TO_RESULT_FILES + "\\");


        if (!Files.exists(pathToResults)) {
            try {
                Files.createDirectories(pathToResults);
            } catch (IOException e) {
                throw new DirectoryCreatingException("Error to trying to create a directory.");
            }
        }
    }

    public void execute() {
        try {
            for (String fileName : parser.getFileNames()) {

                try (BufferedReader reader = new BufferedReader(
                        new FileReader(Config.PATH_TO_FILES_FOR_PROCESS + fileName))
                ) {
                    String line;

                    while ((line = reader.readLine()) != null) {

                        DataType curLineType = parseDataTypeFromLine(line);
                        writeToResults(curLineType, line);

                        if (!stats.containsKey(curLineType)) {
                            stats.put(curLineType, new FileStatistic());
                        }

                        updateStatistic(line, stats.get(curLineType), curLineType);
                    }
                } catch (IOException e) {
                    throw new FileReadException("Error reading the file: " + e.getMessage());
                }
            }
        } finally {
            closeAllWriters();
        }
        showStats();
    }


    public void writeToResults(DataType type, String line) {
        String fileNameForCurType = getFileNameForDataType(type);

        if (!writers.containsKey(type)) {
            try {
                String fileNameWithPrefix = parser.getPrefix() + fileNameForCurType;

                Path resultPathToFile = Paths.get(pathToResults.toString(), fileNameWithPrefix);

                createFileIsNecessary(resultPathToFile);
                deleteFileIsNecessary(resultPathToFile);

                boolean append = parser.isAddToExistsFiles();
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(resultPathToFile.toFile(), append)
                );
                writers.put(type, writer);

            } catch (IOException e) {
                throw new RuntimeException("Failed to create writer for type " + type, e);
            }
        }

        try {
            BufferedWriter curWriter = writers.get(type);

            curWriter.write(line);
            curWriter.newLine();
            curWriter.flush();
        } catch (IOException e) {
            throw new WritingLineException("Exception when writing a string '"
                    + line + "' with type '" + type.name() + "'.");
        }
    }

    public void closeAllWriters() {
        for (BufferedWriter writer : writers.values()) {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new FileClosingException("Error closing writer: " + e.getMessage());
            }
        }
        writers.clear();
    }

    public void updateStatistic(String line, FileStatistic fileStats, DataType type) {

        if (type.equals(DataType.FLOAT))
            fileStats.addFloat(line);
        else if (type.equals(DataType.INTEGER))
            fileStats.addInteger(line);
        else if (type.equals(DataType.STRING))
            fileStats.addString(line);

    }

    public void showStats() {
        if (parser.isFullStats()) {
            showFullStats();
        } else if (parser.isShortStats() && !parser.isFullStats())
            showShortStats();

        else throw new ParamNotSelectedException("You should select param 's' or 'f' to show statistic");
    }

    private DataType parseDataTypeFromLine(String line) {
        if (isLineInteger(line)) {
            return DataType.INTEGER;
        } else if (isLineDouble(line)) {
            return DataType.FLOAT;
        } else
            return DataType.STRING;
    }

    private boolean isLineInteger(String line) {
        try {
            Integer.parseInt(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isLineDouble(String line) {
        try {
            Double.parseDouble(line);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showFullStats() {
        System.out.println("---Full statistic---");
        if (stats.containsKey(DataType.INTEGER))
            stats.get(DataType.INTEGER).showIntegerFullStats();

        if (stats.containsKey(DataType.FLOAT))
            stats.get(DataType.FLOAT).showFloatFullStats();

        if (stats.containsKey(DataType.STRING))
            stats.get(DataType.STRING).showStringFullStats();
    }

    private void showShortStats() {
        System.out.println("---Short statistic---");
        if (stats.containsKey(DataType.INTEGER))
            stats.get(DataType.INTEGER).showIntegerShortStats();

        if (stats.containsKey(DataType.FLOAT))
            stats.get(DataType.FLOAT).showFloatShortStats();

        if (stats.containsKey(DataType.STRING))
            stats.get(DataType.STRING).showStringShortStats();
    }

    private String getFileNameForDataType(DataType type) {
        if (type.equals(DataType.INTEGER))
            return "integers.txt";
        else if (type.equals(DataType.FLOAT))
            return "floats.txt";
        else
            return "strings.txt";
    }

    private void createFileIsNecessary(Path path) throws IOException {
        if (!Files.exists(path))
            Files.createFile(path);
    }

    private void deleteFileIsNecessary(Path path) throws IOException {
        if (!Files.exists(path))
            Files.delete(path);
    }

}
