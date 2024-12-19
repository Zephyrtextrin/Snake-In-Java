import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ErrorPrinter {
    private static final Map<String,Error> errorDB = new HashMap<>();
    private static String additionalDetails;
    public static void errorHandler(String code){
        System.out.println("------------------------------------------------------------------------------------------------");
        Error error = errorDB.get(code);
        if(error==null){error = errorDB.get("ABSTRUSE");}

        //refreshes err details
        init.updateValues();

        //first portion of error printing system: prints out details of what happened
        //ERROR is for actual game-impeding issues; ABNORMALITY is for unintended things of lower destructiveness/priority. (honestly most of these are fringe cases that never happen ever but its good to have a handler)
        //typically ERRORs are rarer if the game isnt actively getting debugged, since they're issues i solve first
        headerBuilder(error.isError, true,"");

        System.out.println(error.cause); //explains what happened and a likely explanation for why
        System.out.println("Error code: "+error.code);

        //second part: prints out relevant variable values, usually for my own debugging use
        if(error.details!=null) {
            headerBuilder(error.isError, false, "DETAILS");
            System.out.println(error.details);
        }

        //additional details, usually aimed at end-users
        if(error.additional!=null){
            headerBuilder(error.isError, false,"ADDITIONAL-DETAILS");
            System.out.println(error.additional);
        }

        if(error.code.equals("ABSTRUSE")){System.out.println("[FALLBACK]: " + code);}
       if(error.isError&&!Objects.equals(error.code, "ERR_GM_EXECUTOR_SERVICE_FAULT")){
            System.out.println("\na message from alex regarding errors\n(this is automatically appended to all errors)\nso there's actually two types of issues in the error handler i wrote: abnormalities and errors\nabnormaities are just unintended issues i should probably fix\nand errors are active issues that impede the functioning of the game\nso it's really important you report errors to me\nthanks bro\n-alexander");
            System.exit(0);
        }

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

    public ErrorPrinter() throws IOException {new init();}

    public static void setDetails(String errorDetails, boolean append){
        if(append){additionalDetails += errorDetails;
        }else{additionalDetails=errorDetails;}
    }

    public static void printCellAtts(Board.Cell cell){additionalDetails+="\n[ROW]: "+cell.ROW+"\n[COLUMN]: "+cell.COLUMN+"\n[AGE]: "+cell.age+"\n[TYPE]: "+cell.type;}

    private static class init {
        //some of these errors have values that need updates so they are initialized in the updateValues method instead which is why some have placeholder details
        private void initialize() throws IOException {

            //game-management related
            new Error("ERR_GM_EXECUTOR_SERVICE_FAULT", true, "The frame-advancement protocol's thrown an exception!", "Details have been added in a stacktrace above.", "Unfortunately, this is a very generic error which can be applied to just about anything and if the stacktrace isn't useful there's just about nothing I can actually do about it :/");

            //board-related
            new Error("ERR_BR_CELL_OOB", true, "The specified cell does not exist!", "!!PLACEHOLDER!! this should be overwritten in errorprinter class", null);
            new Error("ABN_BR_CELL_UNDER_CONSTRUXION", false, "Abnormality during the cell construction process!", "!!PLACEHOLDER!! this should be overwritten in errorprinter class", null); //the name is a camellia ref LOL@!
            new Error("ERR_BR_GENERIC", true, "An error occurred regarding the cells!", "!!PLACEHOLDER!! this should be overwritten in errorprinter class", "This is a super generic error I made in the off-chance an error happens with the board that isn't accounted for by other errors and is as a result very vague and it would be incredibly hard to get specific details on");
            //snake-related
            new Error("ABN_SK_IRREGULAR_MOVEMENT", false, "Snake movement is dysfunctional!\nYou likely somehow managed to both row/col values at once, or somehow moved twice in one frame advancement.", Snake.getErrorDetails(), null);
            new Error("ERR_SK_OUROBOROS", true, "Snake turned around in on itself!\nWhile there are checks in place to prevent the snake from going right when it's already going left, this was (somehow) not applied.", Snake.getErrorDetails(), "\n[TEMP]\nthe snake moves every 75 milliseconds, but inputs are constantly being read from the ActionListener.\nits possible to make 2 inputs in-between each frame and bypass the protections against ouroboros-ing yourself");

            //errors for high-score reading
            new Error("ABN_HS_INSUBSTANTIAL", false, "Length high-score not found or invalid!", "\n[VALUE]: " + DataReadingInterface.errorOutput(), "The program has already created a new file and added a default value of 0, so the issue's resolved itself.\nIf this is your first time running the program, you can probably ignore this.");
            new Error("ABN_HS_MALFORMED", false, "Your high-score is malformed!\nIt's either larger than the amount of cells in the board, or is negative.", "[CELL COUNT]: " + GameUI.INT_CONSTANTS.CELL_COUNT.value + "\n[HIGH-SCORE]: " + DataReadingInterface.errorOutput(), "HIGH-SCORE DATA HAS BEEN ERASED.\nThis was likely caused by changing the board size, and therefore changing the amount of cells. (as of 12/18 this isn't an actual feature yet)\nIt's also likely this was caused by intentional savedata editing. (if u rly care enough to edit my fucking snake game lol)\nBoth of those are known issues. It's not neccessary to report those circumstances.\nBut if it happens in any other circumstance, that's an issue.\n[TEMPORARY NOTICE] as of rn it'll just say \"ermmm ur highscore data is fucked up :nerd emoji:\" AND IDK WHY IT HAPPENS THIS IS AWFUL -alexander");

            //unique
            new Error("ABSTRUSE", true, "UNKNOWN", "This is a fallback error: Something called the ErrorPrinter class, but the error-code specified is malformed or does not exist.", "It's very likely I just made a typo somewhere. Send me the fallback code if this happens.");
        }

        private init() throws IOException{initialize();}

        //refreshes values for any error that requires a variable
        private static void updateValues(){
            errorDB.get("ERR_BR_CELL_OOB").details = additionalDetails;
            errorDB.get("ERR_BR_GENERIC").details = additionalDetails;
            errorDB.get("ABN_BR_CELL_UNDER_CONSTRUXION").details = additionalDetails;
        }
    }

    private static class Error {
        String code;
        String cause;
        boolean isError;
        String details;
        String additional;

        private Error(String code, boolean isError, String cause, String details, String additional) {
            this.code = code;
            this.cause = cause;
            this.details = details;
            this.isError = isError;
            this.additional = additional;

            errorDB.put(code, this);
        }
    }
}

