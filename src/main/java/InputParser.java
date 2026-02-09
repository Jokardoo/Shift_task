import exceptions.FilesPathAlreadtExistException;
import exceptions.PrefixAlreadyExistsException;
import exceptions.ValueForParamNotPresentException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.PriorityQueue;
import java.util.Queue;

public class InputParser {

    private final String[] inputArgs;
    private boolean addToExistsFiles;
    private String prefix;
    private String outputPath = "";

    private boolean fullStats;
    private boolean shortStats;

    public boolean isFullStats() {
        return fullStats;
    }

    public boolean isShortStats() {
        return shortStats;
    }

    private final Queue<String> fileNames = new PriorityQueue<>();

    public InputParser(String[] input) {
        if (input == null || input.length == 0)
            throw new IllegalArgumentException("Input data should not be empty or null.");

        this.inputArgs = input;
        prepareToUse();
    }

    private void prepareToUse() {
        int i = 0;

        while (inputArgs.length > i) {

            switch (inputArgs[i]) {
                case "-p":
                    try {
                        updateValueForPrefix(inputArgs, i);
                        i += 2;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "-o":
                    try {
                        updateValueForFilesPath(inputArgs, i);
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "-a":
                    addToExistsFiles = true;
                    i++;
                    break;
                case "-s":
                    if (!fullStats) {
                        shortStats = true;
                        i++;
                    } else throw new IllegalStateException("You can select only one flag between 's' and 'f'!");
                    break;
                case "-f":
                    if (!shortStats) {
                        fullStats = true;
                        i++;
                    } else throw new IllegalStateException("You can select only one flag between 's' and 'f'!");
                    break;
                default:

                    if (inputArgs[i].startsWith("-")) {
                        System.out.println("Unknown param '" + inputArgs[i] + "' was skipped.");
                    } else {
                        if (Files.exists(Path.of(Config.PATH_TO_FILES_FOR_PROCESS + "\\" + inputArgs[i]))) {
                            fileNames.add(inputArgs[i]);
                        } else {
                            System.out.println("File with name '" + inputArgs[i] + "' not exists and was skipped");
                        }

                    }
                    i++;
                    break;
            }
        }
    }

    private void updateValueForPrefix(String[] args, int index) {

        // если параметр p еще не задан и если в массиве есть еще элементы
        if (prefix == null && args.length > index + 1) {
            index += 1;
            prefix = args[index];
        } else if (prefix != null)
            throw new PrefixAlreadyExistsException("Prefix 'p' has already been set.");

        else
            throw new ValueForParamNotPresentException("Value for prefix not exists.");


    }

    private void updateValueForFilesPath(String[] args, int index) {

        if (args.length > index + 1) {
            index += 1;

            //TODO проверить наличие файла

            outputPath = args[index];
        } else if (outputPath != null)
            throw new FilesPathAlreadtExistException("Value for files path already exists!");

        else
            throw new ValueForParamNotPresentException("Value for files path not exists!");
    }

    public boolean isAddToExistsFiles() {
        return addToExistsFiles;
    }

    public String getPrefix() {
        return prefix == null ? "" : prefix;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public Queue<String> getFileNames() {
        return fileNames;
    }

}
