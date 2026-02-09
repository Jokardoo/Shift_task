

public class Main {
    public static void main(String[] args) {


        InputParser inputParser = new InputParser(args);
        FileHandler fileHandler = new FileHandler(inputParser);

        fileHandler.execute();


    }


}
