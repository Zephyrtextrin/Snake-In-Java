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
        DATA_FILE.createNewFile(); //creates new lengthdata if it does not exist
        final Scanner scan = new Scanner(DATA_FILE);
        int length;
        String abnormalValue = "(does not exist)";
        if(scan.hasNextInt()){length = scan.nextInt();
        }else{
            if(scan.hasNextLine()){abnormalValue = scan.nextLine();}
            System.out.println("----[[[[ERROR!]]]]----\nLength high-score not found or invalid!\n\n---[[[DETAILS]]]---\nDoes the high-score document exist? "+DATA_FILE.exists()+"\nIs there a readable integer in the file? "+scan.hasNextInt()+"\nValue read: "+abnormalValue+"\n\n---[[[WHAT TO DO]]]---\nIf this is your first time running the program, you can probably ignore this.\n(if it is not your first time running the program and this is a genuine error, please contact me.)");
            return 0;
        }

        if(length>Main.INT_CONSTANTS.BOARD_SIZE.value){
            System.out.println("---[[[ABNORMALITY]]]---\nYour length high-score is abnormal!\n\n---[[[DETAILS]]]---\n[CAUSE]: Length exceeds the amount of cells on the board!\n[HIGH-SCORE]: "+length+"\nHIGH-SCORE DATA HAS BEEN ERASED.\n(if this is a mistake, please contact me)");
            writeFile("0");
        }else if(length<0){
            System.out.println("---[[[ABNORMALITY]]]---\nYour length high-score is physically impossible!\n[CAUSE]: Length value is negative.\nHIGH-SCORE DATA HAS BEEN ERASED.\n(if this is a mistake, please contact me)");
            writeFile("0");
        }

    }

    public static void writeFile(String output) throws IOException {
        FileWriter myWriter = new FileWriter(dataPath);
        myWriter.write(output);
        myWriter.close();
    }

    //used exclusively for error output
    public static String isReadable() throws FileNotFoundException {
        final Scanner scan = new Scanner(DATA_FILE);
        String abnormalValue = "(does not exist)";
        if(scan.hasNextLine()){return scan.nextLine();}
        return "(does not exist)";
    }
}
