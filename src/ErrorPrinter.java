import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ErrorPrinter {
    private static final Map<String,Error> errorDB = new HashMap<>();
    public static void errorHandler(String code){
        System.out.println("------------------------------------------------------------------------------------------------");
        Error error = errorDB.get(code);
        if(error==null){error = errorDB.get("XX_ABSTRUSE");}

        //first portion of error printing system: prints out details of what happened
        //ERROR is for actual game-impeding issues; ABNORMALITY is for unintended things of lower destructiveness/priority. (honestly most of these are fringe cases that never happen ever but its good to have a handler)
        //typically ERRORs are rarer if the game isnt actively getting debugged, since they're issues i solve first
        headerBuilder(error.isError, true,"");

        System.out.println(error.cause); //explains what happened and a likely explanation for why
        System.out.println("Error code: "+error.code);

        //second part: prints out relevant variable values, usually for my own debugging use
        headerBuilder(error.isError, false,"DETAILS");
        System.out.println(error.details);

        //additional details, usually aimed at end-users
        if(error.additional!=null){
            headerBuilder(error.isError, false,"ADDITIONAL-DETAILS");
            System.out.println(error.additional);
        }

        if(error.code.equals("XX_ABSTRUSE")){System.out.println("[FALLBACK]: " + code);}
        System.out.println("------------------------------------------------------------------------------------------------");
    }

    private static void headerBuilder(boolean isError, boolean isMainHeader, String message){
        String display = "[[--%s--]]";
        if(isMainHeader){
            if(isError){display = "[[[[----ERROR!----]]]]";
            }else{display = "[[[---ABNORMALITY---]]]";}
        }else if(isError){display="\n[[[---%s---]]]";}

        System.out.printf(display+"\n",message);
    }

    public ErrorPrinter() throws IOException {
        new init();
    }
    private class Error{
        String code;
        String cause;
        boolean isError;
        String details;
        String additional;

        private Error(String code, boolean isError, String cause, String details, String additional){
            this.code = code;
            this.cause = cause;
            this.details = details;
            this.isError = isError;
            this.additional = additional;

            errorDB.put(code, this);
        }
    }

    private class init {
        private void initialize() throws IOException {

            //snake-related
            new Error("SK_IRREGULAR_MOVEMENT", false, "Snake movement is dysfunctional!\nYou likely somehow managed to both row/col values at once, or somehow moved twice in one frame advancement.", "[PAST ROW]: " + Snake.pastRow + " | [CURRENT ROW]: " + Snake.row + "\n[PAST COL]: " + Snake.pastCol + " | [CURRENT COL]: " + Snake.column + "\n[MODIFIER]: " + Snake.modifier, "tbh idk how this would ever happen so its a moot point");
            //errors for highscore reading
            new Error("HS_DNE", false, "Length high-score not found or invalid!", "\n[VALUE]: " + DataReadingInterface.errorOutput(), "The program has already created a new file and added a default value of 0, so the issue's resolved itself.\nIf this is your first time running the program, you can probably ignore this.\nIf this is NOT your first time running the program, please contact me.");
            new Error("HS_MALFORMED", false, "Your high-score is malformed!\nIt's either larger than the amount of cells in the board, or is negative.", "[CELL COUNT]: " + Main.INT_CONSTANTS.CELL_COUNT.value + "\n[HIGH-SCORE]: " + DataReadingInterface.errorOutput(), "HIGH-SCORE DATA HAS BEEN ERASED.\nThis was likely caused by changing the board size, and therefore changing the amount of cells.\nIt's also likely this was caused by intentional savedata editing. (if u rly care enough to edit my fucking snake game lol)\nBoth of those are known issues. It's not neccessary to report those;");

            //unique
            new Error("XX_ABSTRUSE", true, "UNKNOWN", "This is a fallback error: Something called the ErrorPrinting class, but the error-code specified is malformed or does not exist.", "It's very likely I just made a typo somewhere. Send me the fallback code if this happens.");
        }
        private init() throws IOException {initialize();}
    }
}

