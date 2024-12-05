import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataReadingInterface{

    private static final String dataPath = "./LENGTHDATA.txt";
    private static final File DATA_FILE = new File(dataPath); //path to the txt with all the shit that matters in it

    public DataReadingInterface() throws IOException {readFile();}

    public static int readFile() throws IOException {
        DATA_FILE.createNewFile();
        final Scanner scan = new Scanner(DATA_FILE);

        if(scan.hasNextInt()){return scan.nextInt();
        }else{return 0;}
    }

    public static void writeFile(String output) throws IOException {
        FileWriter myWriter = new FileWriter(dataPath);
        myWriter.write(output);
        myWriter.close();
    }
}
