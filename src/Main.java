import java.io.IOException;

public class Main extends GameManager{
    //sets up frame, initializes some constructors, and runs method that actually makes the game work
    public static void main(String[] args) throws IOException{
        new ErrorPrinter();
        SettingsUI.UIInit();
        if(DEBUG){
            System.out.println("[DEBUG MODE ENABLED]");
            Debug.displayOptions();
        }
    }
}
