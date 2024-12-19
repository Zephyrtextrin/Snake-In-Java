import java.io.IOException;

public class Main extends GameManager{
    //sets up frame, initializes some constructors, and runs method that actually makes the game work
    public static void main(String[] args) throws IOException{
        new ErrorPrinter();
        SettingsUI.UIInit();
        if(DEBUG){System.out.println("[DEBUG MODE ENABLED]\nQ: print current fruit position\nW: slow game down to 1/3rd of the speed\nE: create a new fruit\nR: invoke abstruse error");}
    }
}
