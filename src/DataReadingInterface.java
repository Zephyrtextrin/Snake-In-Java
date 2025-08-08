import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataReadingInterface{

    private static final String dataPath = "./LENGTHDATA.txt";
    private static final File DATA_FILE = new File(dataPath); //path to the txt with all the shit that matters in it

    protected static int readFile() throws IOException {
        final Scanner scan = new Scanner(DATA_FILE);
        int length;
        if(scan.hasNextInt()){
            length = scan.nextInt();
            if(length>GameUI.cellCount){ //err handler if data is malformed
                ErrorPrinter.errorHandler(ErrorPrinter.ERROR_CODE.ABN_HS_MALFORMED, null);

                writeFile("0");
                return 0;
            }
        }else{ //error handler in case highscore data does not exist
            ErrorPrinter.errorHandler(ErrorPrinter.ERROR_CODE.ABN_HS_INSUBSTANTIAL, null);
            writeFile("0");
            return 0;
        }
        return length;
    }

    protected static void writeFile(String output) throws IOException {
        FileWriter myWriter = new FileWriter(dataPath);
        myWriter.write(output);
        myWriter.close();
    }

    //used exclusively for error output
    public static String errorOutput() throws IOException {
        final Scanner scan = new Scanner(DATA_FILE);

        if(scan.hasNextLine()){return scan.nextLine();}
        return "(does not exist)";
    }
}
