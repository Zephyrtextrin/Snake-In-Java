import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataReadingInterface{

    private static final String dataPath = "./LENGTHDATA.txt";
    public static final File DATA_FILE = new File(dataPath); //path to the txt with all the shit that matters in it

    public DataReadingInterface() throws IOException {readFile();}

    public static int readFile() throws IOException {
        DATA_FILE.createNewFile(); //creates new lengthdata if it does not exist FOR SOME REASON INTELLIJ CANT SHUT UP ABOUT THIS LINE BUT ITS ACTUALLY IMPORTANT
        final Scanner scan = new Scanner(DATA_FILE);
        int length;
        if(scan.hasNextInt()){
            length = scan.nextInt();
            if(length>Main.INT_CONSTANTS.BOARD_SIZE.value){
                ErrorPrinter.errorHandler("HS_MALFORMED_SCORE");
                return 0;
            }
        }else{
            ErrorPrinter.errorHandler("HS_DNE");
            return 0;
        }

        return length;
    }

    public static void writeFile(String output) throws IOException {
        FileWriter myWriter = new FileWriter(dataPath);
        myWriter.write(output);
        myWriter.close();
    }

    //used exclusively for error output
    public static String errorOutput() throws IOException {
        DATA_FILE.createNewFile(); //creates new lengthdata if it does not exist FOR SOME REASON INTELLIJ CANT SHUT UP ABOUT THIS LINE BUT ITS ACTUALLY IMPORTANT
        final Scanner scan = new Scanner(DATA_FILE);

        if(scan.hasNextLine()){return scan.nextLine();}
        return "(does not exist)";
    }
}
