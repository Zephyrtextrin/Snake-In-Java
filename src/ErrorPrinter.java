import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ErrorPrinter {
    private Map<String,Error> errorDB = new HashMap<>();

    public void errorHandler(String code, boolean error){

        String errorCause;
        //first portion of error printing system: specifies whether the error is an actual issue or just something weird that shouldnt go unnoticed, and prints out details of what happened
        if(error){System.out.println("----[[[[ERROR!]]]]----");
        }else{System.out.println("---[[[ABNORMALITY]]]---");}
        switch(code){
            case "DNE": errorCause = "Length high-score not found or invalid!";
            break;

            default: errorCause = "Error code passed that does not exist!";
            break;
        }

        System.out.println();
    }

    private class Error{
        String code;
        String cause;
        boolean isError;
        String details;
        String additional;

        private Error(String code, String cause, boolean isError String details, String additional){
            this.code = code;
            this.cause = cause;
            this.details = details;
            this.isError = isError;

            if(additional.equals("0")){additional="No additional Details.";
            this.additional = additional;
            errorDB.put(code, this);
        }
    }

    private void Initialize() throws FileNotFoundException{

        //errors for highscore reading
        new Error("HS_DNE", "Length high-score not found or invalid!", false, "Does the high-score document exist? "+DataReadingInterface.DATA_FILE.exists()+"\nIs there a readable integer in the file?\nValue read: "+DataReadingInterface.isReadable(),"The program has already created a new file and added a default value of 0, so the issue's resolved itself.\nIf this is your first time running the program, you can probably ignore this.\nIf this is NOT your first time running the program, please contact me.")
        new Error("HS_MALFORMED", "Your length high-score is malformed!\nIt's either larger than the amount of cells in the board, or is negative.",false,"Amount of Cells in the board: "+Main.INT_CONSTANTS.CELL_COUNT.value+"\nHigh-Score: 
    }
}

